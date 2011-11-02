/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world.persistence;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import marinesmud.system.Config;
import marinesmud.system.threadmanagers.Tickers;
import marinesmud.system.threadmanagers.TickersTime;
import pl.jblew.code.ccutils.ThreadsManager;
import pl.jblew.code.timeutils.TimeValue;

/**
 *
 * @author jblew
 */
public class WorldPersistence {
    private final Set<EnityManager<?>> managers = new HashSet<EnityManager<?>>();
    
    private WorldPersistence() {
    }

    public synchronized void registerManager(EnityManager<?> men) {
        managers.add(men);
        //S/ystem.out.println("Registered MEN to WorldPersistence ("+managers.size()+" managers)!");
    }

    public synchronized void save() {
        //S/ystem.out.println("Saving WorldPersistence ("+managers.size()+" managers)!");
        for(EnityManager<?> men : managers) {
            men.save();
        }
    }

    public synchronized void init() {
        //S/ystem.out.println("Initialized WorldPersistence ("+managers.size()+" managers)!");
        ThreadsManager.getGlobal().scheduleCyclicTask(new Runnable() {
            public void run() {
                WorldPersistence.getInstance().save();
            }
        }, TimeValue.valueOf(Config.get("world persistence save period")));
        save();
    }

    public static WorldPersistence getInstance() {
        return WorldPersistenceHolder.INSTANCE;
    }

    private static class WorldPersistenceHolder {
        private static final WorldPersistence INSTANCE = new WorldPersistence();
    }
 }
