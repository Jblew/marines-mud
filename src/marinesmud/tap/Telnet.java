/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import marinesmud.lib.logging.Level;
import java.util.logging.Logger;
import marinesmud.Main;
import marinesmud.lib.security.MudSecurityManager;
import marinesmud.system.Config;
import marinesmud.system.shutdown.MudShutdown;
import marinesmud.system.shutdown.Shutdownable;
import pl.jblew.code.jutils.data.containers.EnumOnOff;
import pl.jblew.code.jutils.utils.ConversionUtils;
import pl.jblew.code.jutils.utils.IdGenerator;
import pl.jblew.code.timeutils.TimeValue;

/**
 *
 *  *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public final class Telnet {

    private final Selector selector;
    private final static CharsetDecoder ANSI_DECODER = Charset.forName("US-ASCII").newDecoder();

    private Telnet() {
        try {
            selector = Selector.open();
        } catch (IOException ex) {
            throw new RuntimeException("Cannot open Selector for Telnet operations. IOException: " + ex.getMessage() + ".");
        }
    }

    public static Telnet getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {

        public static final Telnet INSTANCE = new Telnet();
    }

    private static enum Option {

        ECHO((byte) 1),
        TERMINAL_TYPE((byte) 24);
        public final byte code;

        public static Option getOptionForCode(byte code) {
            for (Option o : values()) {
                if (o.code == code) {
                    return o;
                }
            }
            return null;
        }

        private Option(byte code) {
            this.code = code;
        }
    }

    public static class CommunicationManager {

        private static final byte NULL = 0;  //Null
        private static final byte LF = 10;   //Line feed
        private static final byte CR = 13;   //Carriage return
        private static final byte BELL = 7;  //Audible or visible bell
        private static final byte BS = 8;    //Backspace (move cursor one character to left
        private static final byte HT = 9;    //Horizontal tab
        private static final byte VT = 11;   //Vertical Tab
        private static final byte FF = 12;   //Form feed
        private static final byte IS = (byte) 0; //for subnegotiation
        private static final byte SEND = (byte) 1; //for subnegotiation
        private static final byte SE = (byte) 240;  //End of subnegotiation parameters
        private static final byte NOP = (byte) 241; //No operation
        private static final byte DM = (byte) 242;  //Data mark
        private static final byte BRK = (byte) 243; //Break
        private static final byte IP = (byte) 244;  //Interrupt process
        private static final byte AO = (byte) 245;  //Abort output
        private static final byte AYT = (byte) 246; //Are you there
        private static final byte EC = (byte) 247;  //Erase character
        private static final byte EL = (byte) 248;  //Erase line
        private static final byte GA = (byte) 249;  //Go ahead
        private static final byte SB = (byte) 250;  //Subnegotiation
        private static final byte WILL = (byte) 251;//Telnet WILL
        private static final byte WONT = (byte) 252;//Telnet WONT
        private static final byte DO = (byte) 253;  //Telnet DO
        private static final byte DONT = (byte) 254;//Telnet DONT
        private static final byte IAC = (byte) 255; //Interpret as command
        private static final List<Byte> IGNORE_CODES = Arrays.asList(NULL, BELL, BS, HT, VT, FF, LF, CR);
        private static final int READ_BUFFER_SIZE = 2048;
        private static final byte[] WELCOME_MESSAGE = ConversionUtils.stringToBytes(Config.get("tap.telnet welcome message"), "US-ASCII");
        private final String NL = "\n";
        private final String REAL_NL = "\n";
        private final SocketChannel channel;
        private final TelnetAsProxy tap;
        private final Lock lock = new ReentrantLock();
        private final ByteBuffer readBuffer = ByteBuffer.allocateDirect(READ_BUFFER_SIZE);
        private final ByteBuffer tmpBuffer = ByteBuffer.allocateDirect(READ_BUFFER_SIZE);
        private final ByteBuffer tmpBuf2 = ByteBuffer.allocate(READ_BUFFER_SIZE);
        private final LinkedBlockingQueue<byte[]> commandsBuffer = new LinkedBlockingQueue<byte[]>();
        private final LinkedBlockingQueue<String> linesBuffer = new LinkedBlockingQueue<String>();
        private final EnumOnOff<Option> serverOptions = new EnumOnOff<Option>();
        private final EnumOnOff<Option> clientOptions = new EnumOnOff<Option>();
        private CommunicationMode mode = CommunicationMode.DEFAULT_MODE;
        private CharsetEncoder encoder;
        private CharsetDecoder decoder;
        private AtomicBoolean interruptBlockingRead = new AtomicBoolean(false);

        public CommunicationManager(SocketChannel channel, TelnetAsProxy tap) {
            this.channel = channel;
            this.tap = tap;
            try {
                channel.configureBlocking(false);
            } catch (IOException ex) {
                throw new CloseUserConnectionRuntimeException("Cannot set SocketChannel.blocking to 'false'. IOException: " + ex.getMessage() + ".");
            }

            setCommunicationMode(CommunicationMode.DEFAULT_MODE);

            try {
                channel.register(getInstance().selector, SelectionKey.OP_READ, this);
            } catch (IOException ex) {
                throw new CloseUserConnectionRuntimeException("Cannot register Selector in SocketChannel. IOException: " + ex.getMessage() + ".");
            }

            sendToOutput(ByteBuffer.wrap(WELCOME_MESSAGE));

            sendToOutput(Config.get("init message"));

            sendToOutput(new byte[]{NULL, NULL, NULL, IAC, DO, Option.TERMINAL_TYPE.code, CR, LF});
            checkRunning();
        }

        public void print(String s) {
            sendToOutput(s);
        }

        public void println(String s) {
            sendToOutput(s + NL);
        }

        private void sendToOutput(String out) {
            checkRunning();
            out = mode.beforeSend(Color.colorify(out).replace(NL, REAL_NL));
            try {
                sendToOutput(encoder.encode(CharBuffer.wrap(out)));
            } catch (IOException e) {
                throw new CloseUserConnectionRuntimeException("Cannot write data to channel (IOException: " + e.getMessage() + "). Data: '" + out.replace(REAL_NL, "\\n") + "'.");
            }
        }

        private void sendToOutput(byte[] bytes) {
            sendToOutput(ByteBuffer.wrap(bytes));
        }

        private void sendToOutput(byte b) {
            sendToOutput(ByteBuffer.wrap(new byte[]{b}));
        }

        private void sendToOutput(ByteBuffer buffer) {
            try {
                lock.lockInterruptibly();
                /*if (buffer.limit() > 1 || buffer.get(0) != NULL) {
                //S/ystem.out.println("WRITE: [" + ListBuilder.createSimpleList(buffer.array(), " ") + "].");
                }*/
                try {
                    channel.write(buffer);
                } catch (IOException e) {
                    throw new CloseUserConnectionRuntimeException("Cannot write data to channel (IOException: " + e.getMessage() + "). ");
                }
                lock.unlock();
            } catch (InterruptedException ex) {
                return; //if closed, return
            }
        }

        public void close() {
            try {
                channel.close();
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }

        public int readyLines() {
            return linesBuffer.size();
        }

        public boolean isReady() {
            return !linesBuffer.isEmpty();
        }

        public String blockingReadLine() {
            try {
                return linesBuffer.take();
            } catch (InterruptedException ex) {
                return null;
            }
        }

        public String blockingReadLine(long timeout, TimeUnit unit) {
            try {
                return linesBuffer.poll(timeout, unit);
            } catch (InterruptedException ex) {
                return null;
            }
        }

        public String nonBlockingReadLine() {
            return linesBuffer.poll();
        }

        public CommunicationMode getCommunicationMode() {
            return mode;
        }

        public void setCommunicationMode(CommunicationMode mode) {
            checkRunning();
            try {
                lock.lockInterruptibly();
                this.mode = mode;

                encoder = mode.outputCharset.newEncoder();
                //encoder.onMalformedInput(CodingErrorAction.REPORT);
                //encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);

                decoder = mode.inputCharset.newDecoder();
                //decoder.onMalformedInput(CodingErrorAction.IGNORE);
                //decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
                lock.unlock();
            } catch (InterruptedException e) {
                return;
            }
        }

        private void checkRunning() {
            /*if (mudUserThread != null && !mudUserThread.isRunning()) {
                throw new CloseUserConnectionRuntimeException();
            }*/
        }

        /*public MudUserThread getUserThread() {
            return mudUserThread;
        }*/

        public synchronized void echoOff() {
            sendToOutput(new byte[]{IAC, DONT, Option.ECHO.code});
        }

        public void echoOn() {
            sendToOutput(new byte[]{IAC, DO, Option.ECHO.code});
        }

        private synchronized void notifyChannelIsReadable() {
            try {
                lock.lockInterruptibly();
                int numBytesRead = channel.read(readBuffer);

                if (numBytesRead < 0) {
                    Logger.getLogger(getClass().getName()).log(Level.NOTICE, "SocketChannel.read() returned -1. Probably user closed his terminal. Closing socket.");
                    close();
                } else if (numBytesRead > 0) {
                    boolean readLineFinished = true;
                    boolean readSomeLines = false;
                    readBuffer.flip();

                    byte lastb = NULL;
                    for (int i = 0; i < readBuffer.limit(); i++) {
                        byte b = readBuffer.get(i);
                        if (b == CR) { //(lastb == CR && b == NULL) || (lastb == CR && b == LF) || (lastb == LF && b == CR) 
                            tmpBuffer.put((byte) CR);
                            //S/ystem.out.println("New line for byte: 0x"+Integer.toHexString(b)+"; Last byte: 0x"+Integer.toHexString(lastb)+".");
                            readLineFinished = true;
                            readSomeLines = true;
                        } else if (b == IAC) {
                            //S/ystem.out.print("RECEIVED [IAC ");
                            if (i + 1 < readBuffer.limit()) {
                                i++;
                                byte operation = readBuffer.get(i);
                                if (operation == IAC) {
                                    tmpBuffer.put(IAC);
                                    //S/ystem.out.println(" IAC]");
                                } else {
                                    //S/ystem.out.println("...");
                                    if (i + 1 < readBuffer.limit()) {
                                        //S/ystem.out.println("2");
                                        i++;
                                        byte option = readBuffer.get(i);
                                        String soption = option + "";
                                        Option o = Option.getOptionForCode(option);
                                        if (o != null) {
                                            soption = o.name();
                                        }
                                        if (operation == DO) {
                                            commandsBuffer.add(new byte[]{DO, option});
                                            //S/ystem.out.println("IAC DO " + soption);
                                        } else if (operation == DONT) {
                                            commandsBuffer.add(new byte[]{DONT, option});
                                            //S/ystem.out.println("IAC DONT " + soption);
                                        } else if (operation == WILL) {
                                            commandsBuffer.add(new byte[]{WILL, option});
                                            //S/ystem.out.println("IAC WILL " + soption);
                                        } else if (operation == WONT) {
                                            commandsBuffer.add(new byte[]{WONT, option});
                                            //S/ystem.out.println("IAC WONT " + soption);
                                        } else if (operation == SB && readBuffer.get(i + 1) == IS) {
                                            i++;//first time to omit i+1
                                            i++;//second to prepare for loop.
                                            tmpBuf2.put(SB);
                                            tmpBuf2.put(option);
                                            for (; i < readBuffer.limit(); i++) {
                                                b = readBuffer.get(i);

                                                if (b == IAC && i + 1 < readBuffer.limit()) {
                                                    i++;
                                                    byte nextB = readBuffer.get(i);
                                                    if (nextB == IAC) {
                                                        tmpBuf2.put(IAC);
                                                    } else if (nextB == SE) {
                                                        break;
                                                    }
                                                } else {
                                                    //S/ystem.out.println(">>> "+b);
                                                    tmpBuf2.put(b);
                                                }
                                            }
                                            tmpBuf2.flip();
                                            byte[] arr = new byte[tmpBuf2.limit()];
                                            for (int j = 0; j < tmpBuf2.limit(); j++) {
                                                arr[j] = tmpBuf2.get(j);
                                            }
                                            tmpBuf2.clear();
                                            commandsBuffer.add(arr);
                                            //S/ystem.out.println("RECEIVED COMMAND: IAC" + ListBuilder.createSimpleList(arr, " "));
                                        } else {
                                            //S/ystem.out.println("IAC " + operation + soption);
                                        }
                                    } else {
                                        //S/ystem.out.println("too short!");
                                    }
                                }
                            }
                        } else if (b == LF) {
                        } else if (b == BS && tmpBuffer.position() > 0) {
                            //S/ystem.out.println("BACKSPACE");
                            tmpBuffer.position(tmpBuffer.position() - 1);
                        } else if (!IGNORE_CODES.contains(b)) {
                            tmpBuffer.put(b);
                            readLineFinished = false;
                        }
                        lastb = b;
                    }

                    if (readSomeLines) {
                        lastb = NULL;
                        tmpBuffer.flip();
                        readBuffer.clear();
                        for (int i = 0; i < tmpBuffer.limit(); i++) {
                            byte b = tmpBuffer.get(i);
                            //S/ystem.out.print("[0x"+Integer.toHexString(b)+"] ");
                            if (b == CR) {
                                readBuffer.flip();
                                String line = mode.afterReceive(decoder.decode(readBuffer).toString());
                                linesBuffer.add(line);
                                //S/ystem.out.println("Received line: '"+line+"'. ReadBuffer limit: "+readBuffer.limit()+" TmpBuffer limit: "+tmpBuffer.limit()+". SessionCounter: "+counter+". i = "+i+".");
                                readBuffer.clear();
                            } else {
                                readBuffer.put(b);
                            }
                            lastb = b;
                        }
                    }

                    while (commandsBuffer.size() > 0) {
                        byte[] cmd = commandsBuffer.take();
                        if (cmd[0] == WILL && cmd[1] == Option.TERMINAL_TYPE.code) {
                            sendToOutput(new byte[]{IAC, SB, Option.TERMINAL_TYPE.code, SEND, IAC, SE});
                        } else if (cmd[0] == SB && cmd[1] == Option.TERMINAL_TYPE.code) {
                            //S/ystem.out.println("terminal type bytes: ["+ListBuilder.createSimpleList(cmd, " ")+"].");
                            System.out.println("TERMINAL TYPE: " + ANSI_DECODER.decode(ByteBuffer.wrap(cmd, 2, cmd.length - 2)));
                        }
                    }

                    readBuffer.clear();

                    if (!readLineFinished) {
                        readBuffer.put(tmpBuffer);
                    }
                    tmpBuffer.clear();
                    lock.unlock();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Telnet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Telnet.class.getName()).log(java.util.logging.Level.SEVERE, "IOException when reading from channel: {0}. Closing.", ex.getMessage());
                if (tap != null) {
                    this.tap.quit("IOException: " + ex.getMessage() + ".");
                }
                try {
                    channel.close();
                } catch (IOException ex1) {
                }
            }
        }
    }

    public static class Server {
        //private static final boolean BLOCKING_MODE = Config.getBool("blocking mode");

        private static final int SELECT_DURATION_MS = 150;
        private static final TimeValue WELCOME_WAIT = TimeValue.valueOf(Config.get("tap.telnet welcome wait"));

        private Server() {
            //, "telnet-server-thread-" + IdGenerator.generate()).start();
        }

        /**
         * starts mud server
         */
        public static void start(ServerSocketChannel mudSocketChannel) {
            getInstance()._start(mudSocketChannel);
        }

        private void _start(ServerSocketChannel mudSocketChannel) {
            getInstance();
            new Server.Runner(mudSocketChannel).run();
        }

        public static Server getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {

            private static final Server INSTANCE = new Server();
        }

        /**
         * I'v created this inner class to hide the run and shutdown methods.
         */
        private class Runner implements Runnable, Shutdownable {

            private AtomicBoolean running = new AtomicBoolean(true);
            private final ServerSocketChannel channel;
            private Thread serverThread = null;

            public Runner(ServerSocketChannel mudSocketChannel) {
                channel = mudSocketChannel;
            }

            @Override
            public void run() {
                MudShutdown.registerShutdownable(this);
                Thread.currentThread().setName("telnet-server-thread-" + IdGenerator.generate());
                serverThread = Thread.currentThread();

                try {
                    channel.configureBlocking(false);
                    channel.register(Telnet.getInstance().selector, SelectionKey.OP_ACCEPT);
                    Logger.getLogger("TelnetServer").log(Level.INFO, "Listening on {0} {1}; Host name: {2}", new Object[]{Main.HOST, Main.MUD_PORT, Config.get("host name")});

                    Logger.getLogger("TelnetServer").log(Level.INFO, "Telnet is now waiting for selector keys...");
                    while (running.get()) {
                        int numSelectedKeys = Telnet.getInstance().selector.select(SELECT_DURATION_MS);
                        if (numSelectedKeys > 0) {
                            Iterator it = Telnet.getInstance().selector.selectedKeys().iterator();

                            while (it.hasNext()) {
                                SelectionKey selKey = (SelectionKey) it.next();

                                it.remove();
                                if (selKey.isValid()) {
                                    if (selKey.isAcceptable()) {
                                        SocketChannel userSocket = channel.accept();
                                        Logger.getLogger("TelnetServer").log(Level.INFO, "Telnet connection accepted");
                                        this.newUser(userSocket);
                                    } else if (selKey.isReadable()) {
                                        Object attachment = selKey.attachment();
                                        //if(attachment != null && attachment instanceof Telnet.CommunicationManager) {
                                        CommunicationManager connectionManager = (CommunicationManager) attachment;
                                        connectionManager.notifyChannelIsReadable();
                                        //}
                                    }
                                }
                            }
                        }
                    }
                    quit();
                    Logger.getLogger("TelnetServer").log(Level.INFO, "Quit socket server");
                } catch (IOException ex) {
                    Logger.getLogger(Telnet.Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private synchronized void newUser(final SocketChannel userSocket) {
                try {
                    MudSecurityManager.checkSocket(userSocket.socket());
                    userSocket.socket().setReuseAddress(true);
                    TelnetAsProxy tap = new TelnetAsProxy(userSocket);
                } catch (SocketException ex) {
                    Logger.getLogger(Telnet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (SecurityException e) {
                    Logger.getLogger(Telnet.Server.class.getName()).log(Level.NOTICE, "Security problem: {0}. Closing socket.", e.getMessage());
                    try {
                        userSocket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Telnet.Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }
            }

            private synchronized void quit() {
                Logger.getLogger("TelnetServer").log(Level.INFO, "Stop socket server");
            }

            @Override
            public void shutdown() {
                running.set(false);
                quit();
                this.serverThread.interrupt(); //stop server
            }
        }
    }
}
