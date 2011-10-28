/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.security;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import marinesmud.lib.logging.Level;
import marinesmud.system.Config;
import marinesmud.system.threadmanagers.Tickers;
import pl.jblew.code.jutils.utils.RandomUtils;
import pl.jblew.code.timeutils.TimeValue;

/**
 *
 * @author jblew
 */
public class MudSecurityManager {

    public final static List<String> IP_BLACKLIST = Collections.unmodifiableList(Config.getStringList("ip blacklist"));
    public final static TimeValue BAN_HOST_FOR_TIME = TimeValue.valueOf(Config.get("ban host for time"));
    private static final int MAX_CONNECTIONS_PER_TICK = Config.getInt("max connections per tick");
    private static final int MAX_CONNECTIONS_PER_SECOND = Config.getInt("max connections per second");
    private static final long SESSION_UID = (long) (System.currentTimeMillis() * Math.random() + 1256 / Math.random());
    private static final String SESSION_SALT = RandomUtils.generateRandomString(Config.getInt("salt length"), Config.get("salt characters")); //RandomUtils.chooseStringRandomly(TextList.getTextList("session_salts").getElementsAsArray())
    private static final String PASSWORD_SALT = Config.get("password salt");
    private final static Map<String, Integer> socketConnectionsPerSecond = Collections.synchronizedMap(new HashMap<String, Integer>());
    private final static Map<String, Integer> socketConnectionsPerTick = Collections.synchronizedMap(new HashMap<String, Integer>());
    private final static List<String> temporarilyBannedHosts = Collections.synchronizedList(new ArrayList<String>());

    private MudSecurityManager() {
        Tickers.registerTask(new Runnable() {

            @Override
            public void run() {
                socketConnectionsPerSecond.clear();
            }
        }, 1, TimeUnit.SECONDS);
        Tickers.registerTickable(new Runnable() {

            @Override
            public void run() {
                socketConnectionsPerTick.clear();
            }
        }, Tickers.Unit.TICK, 1);
        Tickers.registerTask(new Runnable() {

            @Override
            public void run() {
                temporarilyBannedHosts.clear();
            }
        }, BAN_HOST_FOR_TIME.value,  BAN_HOST_FOR_TIME.unit);
    }

    private static MudSecurityManager getInstance() {
        return MudSecurityManagerHolder.INSTANCE;
    }

    public void _init() {
    }

    public static void init() {
        getInstance()._init();
    }

    public static void checkSocket(Socket socket) throws SecurityException {
        InetAddress address = socket.getInetAddress();
        String hostAddress = address.getHostAddress();
        //System.out.println("Address: "+hostAddress);

        if (temporarilyBannedHosts.contains(hostAddress)) {
            throw new SecurityException("This host (address=" + hostAddress + ") is on list of temporarily banned hosts. List is refreshed every " + BAN_HOST_FOR_TIME.toString() + ".");
        }
        if (socketConnectionsPerSecond.containsKey(hostAddress)) {
            int connectionsCount = socketConnectionsPerSecond.get(hostAddress) + 1;

            socketConnectionsPerSecond.put(hostAddress, connectionsCount);
            if (connectionsCount > MAX_CONNECTIONS_PER_SECOND) {
                temporarilyBannedHosts.add(hostAddress);
                throw new SecurityException("This socket (address=" + hostAddress + ") has exceeded the limit of connections (" + MAX_CONNECTIONS_PER_SECOND + ") per second.");
            }
        } else {
            socketConnectionsPerSecond.put(hostAddress, 1);
        }

        if (socketConnectionsPerTick.containsKey(hostAddress)) {
            int connectionsCount = socketConnectionsPerTick.get(hostAddress) + 1;
            socketConnectionsPerTick.put(hostAddress, connectionsCount);
            if (connectionsCount > MAX_CONNECTIONS_PER_TICK) {
                temporarilyBannedHosts.add(hostAddress);
                throw new SecurityException("This socket (address=" + hostAddress + ") has exceeded the limit of connections (" + MAX_CONNECTIONS_PER_TICK + ") per tick.");
            }
        } else {
            socketConnectionsPerTick.put(hostAddress, 1);
        }

        if (IP_BLACKLIST.contains(hostAddress) || IP_BLACKLIST.contains(address.getCanonicalHostName()) || IP_BLACKLIST.contains(address.getHostName())) {
            temporarilyBannedHosts.add(hostAddress);
            throw new SecurityException("This socket's address(" + hostAddress + ") or host is on blacklist defined in config.");
        }
    }

    public static void temporarilyBan(InetAddress address, String reason) {
        String sAddress = address.getHostAddress();
        temporarilyBannedHosts.add(sAddress);
        Logger.getLogger("MudSecurityManager").log(Level.NOTICE, "Temporarily banned address ''{0}''. Reason: {1}", new Object [] {sAddress, reason});
    }

    public void _checkSecurity() throws SecurityException {
    }

    public static void checkSecurity() throws SecurityException {
        getInstance()._checkSecurity();
    }

    public static String getSessionUID() {
        return SESSION_UID + SESSION_SALT;
    }

    public static String getPasswordSalt() {
        return PASSWORD_SALT;
    }

    public static int getTemporarilyBannedHostsCount() {
        return temporarilyBannedHosts.size();
    }

    private static class MudSecurityManagerHolder {

        private static final MudSecurityManager INSTANCE = new MudSecurityManager();
    }
}
