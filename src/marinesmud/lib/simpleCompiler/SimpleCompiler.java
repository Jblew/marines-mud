/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.simpleCompiler;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import marinesmud.system.Config;
import pl.jblew.code.dynamiccompiler.CharSequenceCompiler;
import pl.jblew.code.dynamiccompiler.CharSequenceCompilerException;
import pl.jblew.code.dynamiccompiler.DynamicCompilerUtils;
import pl.jblew.code.jutils.data.containers.tuples.string.TwoStringTuple;
import pl.jblew.code.jutils.utils.IdGenerator;

/**
 *
 * @author jblew
 */
public final class SimpleCompiler {

    private final CharSequenceCompiler<SimpleOperation> compiler = new CharSequenceCompiler<SimpleOperation>(getClass().getClassLoader(), Arrays.asList(new String[]{"-target", "1.5"}));
    private final String PACKAGE_NAME = getClass().getPackage().getName() + ".runtime";
    private int classNameSuffix = 0;

    private SimpleCompiler() {
    }

    public static SimpleCompiler getInstance() {
        return SimpleCompilerHolder.INSTANCE;
    }

    private String escape(String in) {
        return in.replace("\n", "\\n").replace("\"", "\\\"");
    }

    public static SimpleOperation compile(String code) {
        return getInstance()._compile(code);
    }

    private SimpleOperation _compile(String code) {
        //System.out.println("CODE: ["+code+"]");
        try {
            // generate semi-secure unique package and class names
            final String packageName = PACKAGE_NAME + generateUniqueName();
            final String className = "SimpleOperation_" + (classNameSuffix++) + generateUniqueName();
            final String qName = packageName + '.' + className;

            String header = "package " + packageName + ";\n";

            /*for (String importPackageName : SourceUtils.getPackagesList()) {
            header += "import " + importPackageName + ".*;\n";
            }*/
            /*for (TwoStringTuple tst : SourceScanner.getClassesList()) {
                if ((code + " SimpleOperation").toLowerCase().contains(tst.second.toLowerCase())) {
                    header += "import " + tst.first + ";\n";
                }
            }*/

            header += DynamicCompilerUtils.prepareImports(Config.getStringList("dynamic compiler default imports"));

            //System.err.println(header);

            String source = header
                    + "\n"
                    + "public class " + className + " implements SimpleOperation {\n"
                    + "   \n"
                    + "   public Object doIt(Object... args) {\n"
                    + "      " + code + "\n"
                    + "   }\n"
                    + "}\n";

            if (Config.getBool("compiler debug")) {
                System.out.println(source);
            }

            final DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();
            Class<SimpleOperation> compiledFunction = compiler.compile(qName, source, errs, new Class<?>[]{SimpleOperation.class});
            log(errs);
            SimpleOperation s = compiledFunction.getConstructor().newInstance();
            return s;
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(SimpleCompiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(SimpleCompiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(SimpleCompiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(SimpleCompiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CharSequenceCompilerException ex) {
            log(ex.getDiagnostics());
            Logger.getLogger(SimpleCompiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(SimpleCompiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SimpleCompiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Unable to compile SimpleOperation.");
    }

    private String generateUniqueName() {
        return "_" + IdGenerator.generate();
    }

    private void log(final DiagnosticCollector<JavaFileObject> diagnostics) {
        final StringBuilder msgs = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            try {
                msgs.append(diagnostic.getMessage(null)).append(" on line ").append(diagnostic.getLineNumber()).append(" in file ").append(diagnostic.getSource().getName()).append("\n");

                if (msgs.length() > 0) {
                    Logger.getLogger("SimpleCompiler").log(Level.WARNING, "Compilation errors: ", msgs.toString());
                }
            } catch (NullPointerException e) {
                Logger.getLogger("SimpleCompiler").log(Level.WARNING, "Compilation diagnostics: "+diagnostic.getMessage(null), e);
            }
        }
    }

    private static class SimpleCompilerHolder {

        private static final SimpleCompiler INSTANCE = new SimpleCompiler();
    }
}
