/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.system;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TemporaryGlobalVariablesManager is container for global variables that are used only for current session.
 * Notice, that they won't be saved on shutdown.
 * @author jblew
 */
public class TemporaryGlobalVariablesManager {

    private static final Lock lock = new ReentrantLock();
    private static final ConcurrentHashMap<String, String> variables = new ConcurrentHashMap<String, String>();

    private TemporaryGlobalVariablesManager() {
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException ex) {
        }
        lock.unlock();
    }

    /**
     *
     * @return instance of TemporaryGlobalVariablesManager
     */
    private static TemporaryGlobalVariablesManager getInstance() {
        return TemporaryGlobalVariablesManagerHolder.INSTANCE;
    }

    private static class TemporaryGlobalVariablesManagerHolder {

        private static final TemporaryGlobalVariablesManager INSTANCE = new TemporaryGlobalVariablesManager();
    }

    /**
     * @param key - name of variable
     * @return String variable under [key]
     */
    public static String getString(String key) {
        String out = null;
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException ex) {
            return null;
        }

        if (variables.containsKey(key)) {
            out = variables.get(key);
        } else {
            throw new NoSuchElementException(key);
        }
        lock.unlock();
        return out;
    }


    /**
     *
     * @param key - name of variable
     * @return Integer variable under [key]
     */
    public static int getInt(String key) {
        return Integer.valueOf(getString(key)).intValue();
    }

    /**
     *
     * @param key - name of variable
     * @return Float variable under [key]
     */
    public static float getFloat(String k) {
        return Float.valueOf(getString(k)).floatValue();
    }

    /**
     * Sets variable [key] to [value].
     * @param key - name of variable
     * @param value - value of variable
     */
    public static void set(String k, String value) {
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException ex) {
            return;
        }
        variables.put(k, value);
        lock.unlock();
    }

    /**
     *Sets variable [key] to [value].
     * @param key - name of variable
     * @param value - value of variable
     */
    public static void set(String k, int value) {
        set(k, "" + value);
    }

    /**
     *
     * @param key - name of variable
     * @return if variable [key] exists
     */
    public static boolean hasVariable(String k) {
        return variables.containsKey(k);
    }

    /**
     *
     * @return map of variables (keys and values)
     */
    public static Map<String, String> getVariables() {
        return Collections.unmodifiableMap(variables);
    }
}
