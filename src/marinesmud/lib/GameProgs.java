/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib;


/**
 *
 * @author jblew
 *
public final class GameProgs {
    //private final CharSequenceCompiler<Prog> progCompiler = new CharSequenceCompiler<Prog>(getClass().getClassLoader(), Arrays.asList(new String[]{"-target", "1.5"}));
    //private final CharSequenceCompiler<RealProg> realProgCompiler = new CharSequenceCompiler<RealProg>(getClass().getClassLoader(), Arrays.asList(new String[]{"-target", "1.5"}));
    //private final String PACKAGE_NAME;
    //private Map<Integer, Prog> progs = Collections.synchronizedMap(new HashMap<Integer, Prog>());
    //private int classNameSuffix = 0;
    //private static final String DEFAULT_CODE = "int x = 0;";

    private GameProgs() {
        PACKAGE_NAME = getClass().getPackage().getName() + ".runtime";
        if (Config.getBool("game progs on")) {
            Logger.getLogger("GameProgs").log(Level.INFO, "Compiling GameProgs... Please wait patiently, this may take a while.");
            //Prog lastp = null;

            long sTime = System.currentTimeMillis();
            int count = 0;
            ResultSet rs = Database.getInstance().executeAndGet("SELECT * FROM progs");
            try {
                while (rs.next()) {
                    Prog p = _recompileProg(rs.getInt("id"), ProgTemplate.valueOf(rs.getString("template")), rs.getString("code"));
                    //lastp = p;
                    progs.put(rs.getInt("id"), p);
                    count++;
                }
            } catch (SQLException ex) {
                Logger.getLogger(GameProgs.class.getName()).log(Level.SEVERE, null, ex);
            }
            long eTime = System.currentTimeMillis();
            Logger.getLogger("GameProgs").log(Level.INFO, "Compiling GameProgs finished. Compiling " + count + " progs took " + (eTime - sTime) + "ms.");
            if (Config.getBool("gc after compiling game progs")) {
                System.gc();
                Logger.getLogger("GameProgs").log(Level.INFO, "Asked java to perform garbage collection.");
            }

        }
    }

    public static GameProgs getInstance() {
        return GameProgsHolder.INSTANCE;
    }

    public void init() {
        getInstance();
    }

    public Prog getProg(int vnum) {
        if (!progs.containsKey(vnum)) {
            throw new NoSuchElementException("There is no such prog(vnum=" + vnum + ")!");
        }
        return progs.get(vnum);
        return null;
    }

    public boolean progExist(int vnum) {
        return progs.containsKey(vnum);
    }

    public Prog createProg(int vnum) {
        if (progExist(vnum)) {
            throw new UnsupportedOperationException("Prog(vnum=" + vnum + ") already exists!");
        }
        Database.getInstance().execute("INSERT INTO progs (id, code, template) values (" + vnum + ", '" + Database.escapeString(DEFAULT_CODE) + "', '" + Database.escapeString(ProgTemplate.global.name()) + "');");
        Prog p = _recompileProg(vnum, ProgTemplate.global, DEFAULT_CODE);
        progs.put(vnum, p);
        return p;
    }

    public void deleteProg(int vnum) {
        if (!progExist(vnum)) {
            throw new NoSuchElementException("There is no such prog(vnum=" + vnum + ")!");
        }
        progs.remove(vnum);
        Database.getInstance().execute("DELETE FROM progs WHERE id = '" + vnum + "';");
    }

    public Map<Integer, Prog> getProgs() {
        return Collections.unmodifiableMap(progs);
    }

    private String escape(String in) {
        return in.replace("\n", "\\n").replace("\"", "\\\"");
    }

    private Prog _recompileProg(int vnum, ProgTemplate template, String code) {
        try {
            // generate semi-secure unique package and class names
            final String packageName = PACKAGE_NAME + generateUniqueName();
            final String className = "Prog_" + (classNameSuffix++) + generateUniqueName();
            final String qName = packageName + '.' + className;

            String header = "package " + packageName + ";\n";

            for (TwoStringTuple tst : SourceScanner.getClassesList()) {
                if ((code + " ProgTemplate Prog RealProg").toLowerCase().contains(tst.second.toLowerCase())) {
                    header += "import " + tst.first + ";\n";
                }
            }

            header += DynamicCompilerUtils.prepareImports(Config.getStringList("dynamic compiler default imports"));

            //System.err.println(header);

            String realPrepareVariables = "";
            int i = 0;
            for (StringClassTuple elem : template.getVariablesMapping()) {
                realPrepareVariables += "                   + \"      " + elem.second.getName() + " " + elem.first + " = (" + elem.second.getName() + ") variables[" + i + "];\\n\";\n";
                i++;
            }
            //System.out.println(realPrepareVariables);
            String realSource = "String source = header + \"\\n\"\n"
                    + "                    + \"public class \" + className + \" implements RealProg {\\n\"\n"
                    + "                    + \"   public void doIt(Object [] variables) {\\n\"\n"
                    + realPrepareVariables + ";\n"
                    + "            int codeStartLine = source.split(\"\\n\").length;\n"
                    + "            source += newCode + \"\\n\"\n"
                    + "                    + \"}\\n\"\n"
                    + "                    + \"   public String getCode() {\\n\"\n"
                    + "                    + \"      return \\\"\" + newCode.replace(\"\\n\", \"\\\\n\").replace(\"\\\"\", \"\\\\\\\"\") + \"\\\";\\n\"\n"
                    + "                    + \"   }\\n\"\n"
                    + "                    + \"   public ProgTemplate getTemplate() {\\n\"\n"
                    + "                    + \"      return ProgTemplate.\" + newTemplate.name() + \";\\n\"\n"
                    + "                    + \"   }\\n\"\n"
                    + "                    + \"   public int getVnum() {\\n\"\n"
                    + "                    + \"      return \" + getVnum() + \";\\n\"\n"
                    + "                    + \"   }\\n\"\n"
                    + "                    + \"   public int getCodeStartLine() {\\n\"\n"
                    + "                    + \"      return \" + codeStartLine + \";\\n\"\n"
                    + "                    + \"   }\\n\"\n"
                    + "                    + \"}\";\n"
                    + "\n";

            String source = "package " + packageName + ";\n"
                    + "\n"
                    + "import marinesmud.lib.progstemplates.*;\n"
                    + "import pl.jblew.code.jutils.utils.IdGenerator;\n"
                    + "import java.lang.reflect.Method;\n"
                    + "import pl.jblew.code.jutils.utils.ReflectionUtils;\n"
                    + "import pl.jblew.code.dynamiccompiler.CharSequenceCompiler;\n"
                    + "import pl.jblew.code.dynamiccompiler.CharSequenceCompilerException;\n"
                    + "import javax.tools.Diagnostic;\n"
                    + "import javax.tools.DiagnosticCollector;\n"
                    + "import javax.tools.JavaFileObject;\n"
                    + "import marinesmud.lib.GameProgs;\n"
                    + "import marinesmud.lib.chucknorris.ChuckNorrisRuntimeException;\n"
                    + "import marinesmud.system.enities.StringClassTuple;\n"
                    + "import marinesmud.system.Database;\n"
                    + "\n"
                    + "public class " + className + " implements Prog {\n"
                    + "   private RealProg realProg;\n"
                    + "   private final CharSequenceCompiler<RealProg> compiler;\n"
                    + "   \n"
                    + "   public " + className + "(CharSequenceCompiler<RealProg> compiler_) {\n"
                    + "      compiler = compiler_;\n"
                    + "   }"
                    + "   \n"
                    + "   public void doIt(Object [] variables) {\n"
                    + "      realProg.doIt(variables);\n"
                    + "   }\n"
                    + "   public String getCode() {\n"
                    + "      return realProg.getCode();\n"
                    + "   }\n"
                    + "   public ProgTemplate getTemplate() {\n"
                    + "      return realProg.getTemplate();\n"
                    + "   }\n"
                    + "   public int getVnum() {\n"
                    + "      return " + vnum + ";\n"
                    + "   }\n"
                    + "   public int getCodeStartLine() {\n"
                    + "      return realProg.getCodeStartLine();\n"
                    + "   }\n"
                    + "   public void recompile(String newCode, ProgTemplate newTemplate) throws CharSequenceCompilerException {\n"
                    + "      try {\n"
                    + "         final String packageName = \"" + packageName + "\";\n"
                    + "         final String className = \"RealProg_\" + IdGenerator.generate();\n"
                    + "         final String qName = packageName + '.' + className;\n"
                    + "         String header = \"" + escape(header) + "\";\n"
                    + "         \n"
                    + "         " + realSource + "\n"
                    + "         //System.out.println(source);\n"
                    + "         final DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();\n"
                    + "         Class<RealProg> compiledFunction = compiler.compile(qName, source, errs, new Class<?>[]{RealProg.class});\n"
                    + "         log(errs);\n"
                    + "         \n"
                    + "         Database.getInstance().execute(\"UPDATE progs SET template = '\"+newTemplate.name()+\"' WHERE id = '\"+getVnum()+\"';\");\n"
                    + "         Database.getInstance().execute(\"UPDATE progs SET code = '\"+Database.escapeString(newCode)+\"' WHERE id = '\"+getVnum()+\"';\");\n"
                    + "         realProg = compiledFunction.newInstance();\n"
                    + "      } catch (InstantiationException e) {\n"
                    + "         System.out.println(e.getMessage());\n"
                    + "      } catch (IllegalAccessException e) {\n"
                    + "         System.out.println(e.getMessage());\n"
                    + "      }\n"
                    + "   }\n"
                    + "   private void log(final DiagnosticCollector<JavaFileObject> diagnostics) {\n"
                    + "      final StringBuilder msgs = new StringBuilder();\n"
                    + "      for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {\n"
                    + "         msgs.append(diagnostic.getMessage(null)).append(\" on line \").append(diagnostic.getLineNumber()).append(\"\\n\");\n"
                    + "      }\n"
                    + "      if(msgs.length() > 0) System.out.println(msgs.toString());\n"
                    + "   }"
                    + "}\n";
            if (Config.getBool("compiler debug")) {
                System.out.println(source);
            }
            final DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();
            Class<Prog> compiledFunction = progCompiler.compile(qName, source, errs, new Class<?>[]{Prog.class});
            log(errs);
            Prog p;
            try {
                p = compiledFunction.getConstructor(progCompiler.getClass()).newInstance(progCompiler);
                p.recompile(code, template);
                return p;
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(GameProgs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(GameProgs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(GameProgs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(GameProgs.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        } catch (CharSequenceCompilerException e) {
            log(e.getDiagnostics());
        } catch (InstantiationException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
        return NULL_PROG;
    }

    private String generateUniqueName() {
        return "_" + IdGenerator.generate();
    }

    private void log(final DiagnosticCollector<JavaFileObject> diagnostics) {
        final StringBuilder msgs = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            msgs.append(diagnostic.getMessage(null)).append(" on line ").append(diagnostic.getLineNumber()).append(" in file ").append(diagnostic.getSource().getName()).append("\n");
        }
        if (msgs.length() > 0) {
            System.out.println(msgs.toString());
        }
    }

    private static class GameProgsHolder {
        private static final GameProgs INSTANCE = new GameProgs();
    }
    public static final Prog NULL_PROG = new Prog() {
        public void doIt(Object[] variables) {
            throw new UnsupportedOperationException("Cannot run null prog!");
        }

        public String getCode() {
            throw new UnsupportedOperationException("Cannot get code of null prog!");
        }

        public ProgTemplate getTemplate() {
            throw new UnsupportedOperationException("Cannot get template of null prog!");
        }

        public int getVnum() {
            throw new UnsupportedOperationException("Cannot get vnum of null prog!");
        }

        public int getCodeStartLine() {
            throw new UnsupportedOperationException("Cannot get code line of null prog!");
        }

        public void recompile(String newCode, ProgTemplate newTemplate) {
            throw new UnsupportedOperationException("Cannot recompile null prog!");
        }
    };
}
*/