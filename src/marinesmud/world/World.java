/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import marinesmud.lib.help.HelpTopic;
import marinesmud.world.persistence.EnityManager;
import marinesmud.world.persistence.WorldEnity;
import marinesmud.system.shutdown.MudShutdown;
import marinesmud.system.shutdown.Shutdownable;
import marinesmud.world.communication.Message;
import pl.jblew.code.jutils.utils.IdGenerator;
import pl.jblew.code.libevent.EventManager;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public final class World extends WorldEnity implements Shutdownable, WorldInterface {
    @Persistent private List<String> reports = new LinkedList<String>();
    @Persistent public List<String> twitterMessages = Collections.synchronizedList(new LinkedList<String>());
    @Persistent public final Map<Integer, HelpTopic> helpTopics = new HashMap<Integer, HelpTopic>();
    public transient final EventManager<Message> messagesEventManager = new EventManager<Message>();
    private transient final long ssid = IdGenerator.generate();

    private World() {
        super();
        MudShutdown.registerShutdownable(this);
    }

    private World(int id) {
        super(id);
        MudShutdown.registerShutdownable(this);
    }

    private World(int id, File f) {
        super(id, f);
        MudShutdown.registerShutdownable(this);
    }

    public void shutdown() {
    }

    public void sendMessage(Message msg) {
        messagesEventManager.fireEvent(msg);
    }

    @Override
    public long version() {
        return 10L;
    }

    @Override
    public String enityName() {
        return "world";
    }

    @Override
    public boolean needsCasting() {
        return false;
    }

    @Override
    public String getCastTo() {
        throw new UnsupportedOperationException("World needn't casting.");
    }

    public long getSSID() {
        return ssid;
    }

    public String hello() {
        return "HELLO";
    }

    @Override
    public EnityManager<World> getManager() {
        return Manager.getInstance();
    }

    /**
     * PL: Raportuj wiadomość
     * EN: Report message
     * @param str - message; wiadomość
     */
    public void report(String str) {
        reports.add(str);
    }

    /**
     * @return reports, wiadomości
     */
    public List<String> getReports() {
        return Collections.unmodifiableList(reports);
    }

    /**
     * PL: Usuwa zgłoszenie
     * EN: Deletes report
     * @param id - id number of report; numer id zgłoszenia
     */
    public void removeReport(int index) {
        reports.remove(index);
    }

    public static World getInstance() {
        return Manager.getInstance().getFirst();
    }

    public static final class Manager extends EnityManager<World> {
        private Manager() {
            super(World.class, "world", 2, 3);
        }

        public static Manager getInstance() {
            return InstanceHolder.INSTANCE;
        }

        @Override
        protected void createFirst() {
            new World(getFirstId());
        }

        private static class InstanceHolder {
            public static final Manager INSTANCE = new Manager();
        }
    }
}
