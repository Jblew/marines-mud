/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.system;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.system.shutdown.MudShutdown;
import pl.jblew.code.jutils.utils.FileUtils;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 * 
 */
public final class Config {

    private static Config INSTANCE = new Config();
    private final Map variables;
    public static final String SECRET_SALT = "ED11Ab45";
    public static final String DS = System.getProperty("file.separator");
    public static final String CONFIG_FILE_PATH = "./config/config.yml";

    public static void init() {
        getInstance();
    }

    private Config() {
        Map o = null;
        try {
            YamlReader reader = new YamlReader(new FileReader(CONFIG_FILE_PATH));
            o = (Map) reader.read();
        } catch (YamlException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, "file: "+CONFIG_FILE_PATH, ex);
            MudShutdown.panicShutdown();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, "file: "+CONFIG_FILE_PATH, ex);
            MudShutdown.panicShutdown();
        }
        List<String> includeFiles = (List<String>) o.get("include");
        for (String filepath : includeFiles) {
            File file = new File(filepath);
            try {
                YamlReader reader2 = new YamlReader(new FileReader(file));
                Map map2 = (Map) reader2.read();
                String packageName = FileUtils.getFileNameWithoutExtension(file);
                for (Object _k : map2.keySet()) {
                    String k = (String) _k;
                    o.put(packageName + "." + k, map2.get(_k));

                }
            } catch (YamlException ex) {
                MudShutdown.panicShutdown();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, "file: "+filepath, ex);
                MudShutdown.panicShutdown();
            }
        }

        variables = Collections.unmodifiableMap(o);
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public static Object getObject(String key) {
        if(!getInstance().variables.containsKey(key)) throw new NoSuchElementException("Config variable "+key);
        return getInstance().variables.get(key);
    }

    public static String get(String key) {
        return (String) getObject(key);
    }

    public static int getInt(String key) {
        return Integer.valueOf(get(key));
    }

    public static float getFloat(String key) {
        return Float.valueOf(get(key));
    }

    public static boolean getBool(String key) {
        return Boolean.valueOf(get(key));
    }

    public static List<String> getStringList(String key) {
        return (List<String>) getObject(key);
    }

    public static Map getMap(String key) {
        return (Map) getObject(key);
    }

    public static List getList(String key) {
        return (List) getObject(key);
    }
}
