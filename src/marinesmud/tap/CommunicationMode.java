/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap;

import java.nio.charset.Charset;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 * @todo TODO: dodatkowy tryb utf82
 * @author jblew
 */
/*class TwoCharsTuple extends TwoTuple<Character, Character> {

    public TwoCharsTuple(char c1, char c2) {
        super(c1, c2);
    }

    public TwoCharsTuple(int c1, int c2) {
        super((char) c1, (char) c2);
    }
}

interface CharsetByteTresholds {

    public static final int TRESHOLD_2 = Integer.parseInt("1111111111000000", 2);
    public static final int TRESHOLD_3 = Integer.parseInt("1111111111100000", 2);
    public static final int TRESHOLD_4 = Integer.parseInt("1111111111110000", 2);
    public static final int TRESHOLD_5 = Integer.parseInt("1111111111111000", 2);
    public static final int TRESHOLD_6 = Integer.parseInt("1111111111111100", 2);
    public static final int MULTIBYTE_LOWER_LIMIT = Integer.parseInt("1111111110000000", 2);
    public static final int MULTIBYTE_UPPER_LIMIT = Integer.parseInt("1111111111000000", 2);
    public static final int MULTIBYTE_SINGLE_LOWER_LIMIT = Integer.parseInt("10000000", 2);
    public static final int MULTIBYTE_SINGLE_UPPER_LIMIT = Integer.parseInt("11000000", 2);
}

interface Translator<V> {
    public V translate(V in);
}
 
    utf(Charset.forName("UTF-8")) {

        @Override
        public ByteBuffer prepareByteInput(ByteBuffer in) {
            byte[] byteArray = in.array();
            ByteBuffer out = ByteBuffer.allocate(in.limit() * 2);
            char c = 0;
            char nextC = 0;
            for (int i = 0; i < in.limit(); i++) {
                c = (char) byteArray[i];
                //S/ystem.out.print(c+"("+(int)c+") ");
                if ((i + 1) < in.limit()) {
                    nextC = (char) byteArray[i + 1];
                    //S/ystem.out.print("1");
                } else {
                    //S/ystem.out.print("0");
                    nextC = 0;
                }

                if (c > 0x1f && c < 0x7E) {
                    out.put((byte) c);
                } else if (c == 0xa) {
                    out.put((byte) c);
                } else if (c == 0xd) {
                    out.put((byte) c);
                }///ALLOWED UTF CHARACTERS:
                else if (c == 0xffc3 && nextC == 0xff93) { //Ó
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc3 && nextC == 0xffb3) {//ó
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff84) {//Ą
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff86) {//Ć
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff87) {//ć
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff98) {//Ę
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff99) {//ę
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xff81) {//Ł
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xff82) {//ł
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xff83) {//Ń
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if ((i + 5) < in.limit() && c == 65477 && nextC == 65535) {//Ń
                    if ((char) byteArray[i + 2] == 65524 && (char) byteArray[i + 3] == 65535 && (char) byteArray[i + 4] == 65533 && (char) byteArray[i + 5] == 6) {
                        out.put((byte) 0xffc5);
                        out.put((byte) 0xff83);

                        i += 4;
                        throw new CloseUserConnectionRuntimeException("Character (N z OGONKIEM) is unsupported in utf!");
                    }
                } else if (c == 0xffc5 && nextC == 0xff84) {//ń
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xff9a) {//Ś
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if ((i + 5) < in.limit() && c == 65477 && nextC == 65535) {//Ś
                    if ((char) byteArray[i + 2] == 65517 && (char) byteArray[i + 3] == 65535 && (char) byteArray[i + 4] == 65533 && (char) byteArray[i + 5] == 6) {
                        out.put((byte) 0xffc5);
                        out.put((byte) 0xff9a);

                        i += 4;
                        throw new CloseUserConnectionRuntimeException("Character (S z OGONKIEM) is unsupported in utf!");
                    }
                } else if (c == 0xffc5 && nextC == 0xff9b) {//ś
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xffb9) {//Ź
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xffba) {//ź
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xffbb) {//Ż
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xffbc) {//ż
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff85) {//ą
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4) {//ą
                    out.put((byte) 0xffc4);
                    out.put((byte) 0xff85);
                    continue;
                } ///END OF ALLOWED UTF CHARACTERS:
                else if (c >= CharsetByteTresholds.TRESHOLD_3) {
                    throw new CloseUserConnectionRuntimeException("User typed unknown character longer or egual 3 bytes!");
                } else {
                    if (c >= CharsetByteTresholds.TRESHOLD_2) {
                        Logger.getLogger("CommunicationMode-utf").log(Level.SEVERE, "Bad multi-byte sign in input: (0x{0}, {1}, {2}) and {3} (0x{4}, {5}, {6})", new Object[]{Integer.toHexString(c), Integer.toBinaryString(c), Integer.toString(c), nextC, Integer.toHexString(nextC), Integer.toBinaryString(nextC), Integer.toString(nextC)});
                    } else {
                        Logger.getLogger("CommunicationMode-utf").log(Level.SEVERE, "Bad one-byte sign in input: (0x{0}, {1}, {2})", new Object[]{Integer.toHexString(c), Integer.toBinaryString(c), Integer.toString(c)});
                    }
                }

                if (c >= CharsetByteTresholds.TRESHOLD_2) {
                    i++;
                }
            }
            out.flip();
            return out;
        }

        public String afterReceive(String in) {
            return in;
        }

        public String beforeSend(String in) {
            return in;
        }
    },
    utf82(Charset.forName("UTF-8")) {

        public ByteBuffer prepareByteInput(ByteBuffer in) {
            byte[] byteArray = in.array();
            ByteBuffer out = ByteBuffer.allocate(in.limit() * 2);
            char c = 0;
            char nextC = 0;
            for (int i = 0; i < in.limit(); i++) {
                c = (char) byteArray[i];
                //S/ystem.out.print(c+"("+(int)c+") ");
                if ((i + 1) < in.limit()) {
                    nextC = (char) byteArray[i + 1];
                    //S/ystem.out.print("1");
                } else {
                    //S/ystem.out.print("0");
                    nextC = 0;
                }

                if (c > 0x1f && c < 0x7E) {
                    out.put((byte) c);
                } else if (c == 0xa) {
                    out.put((byte) c);
                } else if (c == 0xd) {
                    out.put((byte) c);
                }///ALLOWED UTF CHARACTERS:
                else if (c == 0xffc3 && nextC == 0xff93) { //Ó
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc3 && nextC == 0xffb3) {//ó
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff84) {//Ą
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff86) {//Ć
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff87) {//ć
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff98) {//Ę
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff99) {//ę
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xff81) {//Ł
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xff82) {//ł
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xff83) {//Ń
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if ((i + 5) < in.limit() && c == 65477 && nextC == 65535) {//Ń
                    if ((char) byteArray[i + 2] == 65524 && (char) byteArray[i + 3] == 65535 && (char) byteArray[i + 4] == 65533 && (char) byteArray[i + 5] == 6) {
                        out.put((byte) 0xffc5);
                        out.put((byte) 0xff83);

                        i += 4;
                        throw new CloseUserConnectionRuntimeException("Character (N z OGONKIEM) is unsupported in utf!");
                    }
                } else if (c == 0xffc5 && nextC == 0xff84) {//ń
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xff9a) {//Ś
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if ((i + 5) < in.limit() && c == 65477 && nextC == 65535) {//Ś
                    if ((char) byteArray[i + 2] == 65517 && (char) byteArray[i + 3] == 65535 && (char) byteArray[i + 4] == 65533 && (char) byteArray[i + 5] == 6) {
                        out.put((byte) 0xffc5);
                        out.put((byte) 0xff9a);

                        i += 4;
                        throw new CloseUserConnectionRuntimeException("Character (S z OGONKIEM) is unsupported in utf!");
                    }
                } else if (c == 0xffc5 && nextC == 0xff9b) {//ś
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xffb9) {//Ź
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xffba) {//ź
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xffbb) {//Ż
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc5 && nextC == 0xffbc) {//ż
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4 && nextC == 0xff85) {//ą
                    out.put((byte) c);
                    out.put((byte) nextC);
                } else if (c == 0xffc4) {//ą
                    out.put((byte) 0xffc4);
                    out.put((byte) 0xff85);
                } ///END OF ALLOWED UTF CHARACTERS:
                else if (c >= CharsetByteTresholds.TRESHOLD_3) {
                    throw new CloseUserConnectionRuntimeException("User typed unknown character longer or egual 3 bytes!");
                } else {
                    if (c >= CharsetByteTresholds.TRESHOLD_2) {
                        Logger.getLogger("CommunicationMode-utf82").log(Level.SEVERE, "Bad two-byte sign in input: (0x{0}, {1}, {2}) and {3} (0x{4}, {5}, {6})", new Object[]{Integer.toHexString(c), Integer.toBinaryString(c), Integer.toString(c), nextC, Integer.toHexString(nextC), Integer.toBinaryString(nextC), Integer.toString(nextC)});
                    } else {
                        Logger.getLogger("CommunicationMode-utf82").log(Level.SEVERE, "Bad byte sign in input: (0x{0}, {1}, {2})", new Object[]{Integer.toHexString(c), Integer.toBinaryString(c), Integer.toString(c)});
                    }
                }

                if (c >= CharsetByteTresholds.TRESHOLD_2) {
                    i++;
                }
            }
            out.flip();
            return out;
        }

        public String afterReceive(String in) {
            return in;
        }

        public String beforeSend(String in) {
            return in;
        }
    },
    iso(Charset.forName("ISO-8859-2")) {

        public ByteBuffer prepareByteInput(ByteBuffer in) {
            byte[] byteArray = in.array();
            ByteBuffer out = ByteBuffer.allocate(in.limit());
            char c = 0;
            for (int i = 0; i < in.limit(); i++) {
                c = (char) byteArray[i];

                if (c > 0x1f && c <= 0x7E) {
                    out.put((byte) c);
                } else if (c == 0xa || c == 0xffa) {
                    out.put((byte) c);
                } else if (c == 0xa1 || c == 0xffa1) {
                    out.put((byte) c);
                } else if (c == 0xa3 || c == 0xffa3) {
                    out.put((byte) c);
                } else if (c == 0xa6 || c == 0xffa6) {
                    out.put((byte) c);
                } else if (c == 0xac || c == 0xffac) {
                    out.put((byte) c);
                } else if (c == 0xaf || c == 0xffaf) {
                    out.put((byte) c);
                } else if (c == 0xb1 || c == 0xffb2) {
                    out.put((byte) c);
                } else if (c == 0xd3 || c == 0xffd3) {
                    out.put((byte) c);
                } else if (c == 0xe6 || c == 0xffe6) {
                    out.put((byte) c);
                } else if (c == 0xea || c == 0xffea) {
                    out.put((byte) c);
                } else if (c == 0xb3 || c == 0xffb3) {
                    out.put((byte) c);
                } else if (c == 0xf1 || c == 0xfff1) {
                    out.put((byte) c);
                } else if (c == 0xb6 || c == 0xffb6) {
                    out.put((byte) c);
                } else if (c == 0xf3 || c == 0xfff3) {
                    out.put((byte) c);
                } else if (c == 0xbc || c == 0xffbc) {
                    out.put((byte) c);
                } else if (c == 0xb1 || c == 0xffb1) {
                    out.put((byte) c);
                } else if (c == 0xbf || c == 0xffbf) {
                    out.put((byte) c);
                } else if (c == 0xca || c == 0xffca) {
                    out.put((byte) c);
                } else if (c == 0xd1 || c == 0xffd1) {
                    out.put((byte) c);
                } else if (c == 0xd || c == 0xffd) {
                    continue;
                } else if (c >= CharsetByteTresholds.TRESHOLD_3) {
                    throw new CloseUserConnectionRuntimeException("(CommunicationMode: iso) User typed character with code equal or longer than 3 bytes.");
                } else {
                    Logger.getLogger("CommunicationMode-iso").log(Level.SEVERE, "iso: Bad byte sign in input: (0x{0}, {1}, {2})", new Object[]{Integer.toHexString(c), Integer.toBinaryString(c), Integer.toString(c)});
                }
            }
            out.flip();
            return out;
        }

        public String afterReceive(String in) {
            return in;
        }

        public String beforeSend(String in) {
            return in;
        }
    },
    win(Charset.forName("windows-1250")) {

        public ByteBuffer prepareByteInput(ByteBuffer in) {
            return in;
        }

        public String beforeSend(String in) {
            return in;
        }

        public String afterReceive(String in) {
            return in;
        }
    },
    nopol(Charset.forName("US-ASCII")) {
        @Override
        public String afterReceive(String in) {
            return TextUtils.removePolishLetters(in);
        }

        @Override
        public String beforeSend(String in) {
            return TextUtils.removePolishLetters(in);
        }

        public ByteBuffer prepareByteInput(ByteBuffer in) {
            byte[] byteArray = in.array();
            ByteBuffer out = ByteBuffer.allocate(in.limit());
            char c = 0;
            for (int i = 0; i < in.limit(); i++) {
                c = (char) byteArray[i];

                if (c > 0x1f && c < 0x7E) {
                    out.put((byte) c);
                } else if (c == 0xa) {
                    out.put((byte) c);
                } else if (c == 0xd) {
                    continue;
                } else if (c >= CharsetByteTresholds.TRESHOLD_3) {
                    throw new CloseUserConnectionRuntimeException("(CommunicationMode: nopol) User typed character with code equal or longer than 3 bytes.");
                } else {
                    Logger.getLogger("CommunicationMode-utf-nopol").log(Level.SEVERE, "Bad byte sign in input: (0x{0}, {1}, {2})", new Object[]{Integer.toHexString(c), Integer.toBinaryString(c), Integer.toString(c)});
                }

                if (c >= CharsetByteTresholds.TRESHOLD_2) {
                    i++;
                }
            }
            out.flip();
            return out;
        }
    };

    private final Charset charset;

    private CommunicationMode(Charset charset_) {
        charset = charset_;
    }

    public Charset getCharset() {
        return charset;
    }

    public abstract String beforeSend(String in);

    public abstract String afterReceive(String in);

    public abstract ByteBuffer prepareByteInput(ByteBuffer in);
 */

public enum CommunicationMode {
    nopol(Charset.forName("US-ASCII"), Charset.forName("US-ASCII")) {
        @Override
        public String beforeSend(String in) {
            return TextUtils.removePolishLetters(in);
        }

        @Override
        public String afterReceive(String in) {
            return TextUtils.removePolishLetters(in);
        }
    },
    utf(Charset.forName("UTF-8"), Charset.forName("UTF-8")) {
        @Override
        public String beforeSend(String in) {
            return in;
        }

        @Override
        public String afterReceive(String in) {
            return in;
        }
    },
    iso(Charset.forName("ISO-8859-2"), Charset.forName("ISO-8859-2")) {
        @Override
        public String beforeSend(String in) {
            return in;
        }

        @Override
        public String afterReceive(String in) {
            return in;
        }
    },
    win(Charset.forName("windows-1250"), Charset.forName("windows-1250")) {
        @Override
        public String beforeSend(String in) {
            return in;
        }

        @Override
        public String afterReceive(String in) {
            return in;
        }
    };

    public static final CommunicationMode DEFAULT_MODE = CommunicationMode.nopol;

    public final Charset inputCharset;
    public final Charset outputCharset;
    
    private CommunicationMode(Charset inputCharset, Charset outputCharset) {
        this.inputCharset = inputCharset;
        this.outputCharset = outputCharset;
    }

    public abstract String beforeSend(String in);
    public abstract String afterReceive(String in);

    public static String[] getModeNames() {
        String[] out = new String[CommunicationMode.values().length];
        int i = 0;
        for (CommunicationMode mode : CommunicationMode.values()) {
            out[i] = mode.name();
            i++;
        }
        return out;
    }

    public static boolean modeExists(String name) {
        for (CommunicationMode mode : CommunicationMode.values()) {
            if (mode.name().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
