/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.game;

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
 * @author jblew
 */
public class Game extends WorldEnity implements GameInterface {

    private Game() {
        super();
    }

    private Game(int id) {
        super(id);
    }

    private Game(int id, File f) {
        super(id, f);
    }

    @Override
    public long version() {
        return 10L;
    }

    @Override
    public String enityName() {
        return "game";
    }

    @Override
    public boolean needsCasting() {
        return false;
    }

    @Override
    public String getCastTo() {
        throw new UnsupportedOperationException("World needn't casting.");
    }

    @Override
    public EnityManager<Game> getManager() {
        return Manager.getInstance();
    }

    public static Game getInstance() {
        return Manager.getInstance().getFirst();
    }

    public static final class Manager extends EnityManager<Game> {
        private Manager() {
            super(Game.class, "world", 0, 1);
        }

        public static Manager getInstance() {
            return InstanceHolder.INSTANCE;
        }

        @Override
        protected void createFirst() {
            new Game(getFirstId());
        }

        private static class InstanceHolder {
            public static final Manager INSTANCE = new Manager();
        }
    }
}
