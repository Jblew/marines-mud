/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.system.bootstrap;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import pl.jblew.code.jutils.JUtilsVersion;

/**
 *
 * @author jblew
 */
public class EnvironmentChecker {

    private EnvironmentChecker() {
    }

    public static void check() throws BadEnvironmentException {
        try {
            float javaVersion = Float.parseFloat(System.getProperty("java.version").substring(0, 3));
            if (javaVersion < 1.6) {
                throw new BadEnvironmentException("Java must be in version 1.6 or greather.");
            }
        } catch (Exception e) {
            throw new BadEnvironmentException("Cannot check java version: " + e);
        }

        if (JUtilsVersion.VERSION < 1.4f) {
            throw new BadEnvironmentException("Minimal version of JUtils library is 1.4, found version "+JUtilsVersion.VERSION+".");
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new BadEnvironmentException("Cannot find the system Java compiler. Check that your class path includes tools.jar.");
        }
    }
}
