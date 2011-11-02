/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.helpers;

import java.util.Arrays;
import java.util.Collection;
import marinesmud.tap.CloseUserConnectionRuntimeException;
import marinesmud.tap.TelnetAsProxyInstance;
import marinesmud.tap.TelnetServer;

/**
 *
 * @author jblew
 */
public class QuestionsHelper {
    private QuestionsHelper() {
    }

    public static QuestionsHelper getInstance() {
        return QuestionUtilsHolder.INSTANCE;
    }

    private static class QuestionUtilsHolder {
        private static final QuestionsHelper INSTANCE = new QuestionsHelper();
    }

    public static String prompt(TelnetAsProxyInstance tapi, String s) throws InterruptedException {
        tapi.print(s + " ");
        //try {
            //System.out.println("Before blockingReadLine in prompt");
            return tapi.blockingReadLine();
        //} finally {
        //    System.out.println("After blockingReadLine prompt");
        //}
    }

    public static boolean booleanPrompt(TelnetAsProxyInstance tapi, String msg) throws InterruptedException {
        String out;
        while (true) {
            out = prompt(tapi, msg + " [y/n] ");
            if (out.equalsIgnoreCase("y")) {
                return true;
            } else if (out.equalsIgnoreCase("n")) {
                return false;
            } else {
                tapi.println("{rType {Ry{x lub {Rn{x.");
            }
        }
    }

    public static String prompt(TelnetAsProxyInstance tapi, String msg, String[] options) throws InterruptedException {
        String sOptions = "";
        boolean first = true;
        for (String s : options) {
            if (!first) {
                sOptions += ", ";
            }
            first = false;
            sOptions += s;
        }
        String out;
        while (true) {
            out = prompt(tapi, msg + " [" + sOptions + "] ");
            if (Arrays.asList(options).contains(out)) {
                return out;
            } else {
                tapi.println("{xYou have to choos one of the following options: " + sOptions + ".{x");
            }
        }
    }

    public static String prompt(TelnetAsProxyInstance tapi, String msg, Collection<String> options) throws InterruptedException {
        String sOptions = "";
        boolean first = true;
        for (String s : options) {
            if (!first) {
                sOptions += ", ";
            }
            first = false;
            sOptions += s;
        }
        String out;
        while (true) {
            out = prompt(tapi, msg + " [" + sOptions + "] ");
            if (Arrays.asList(options).contains(out)) {
                return out;
            } else {
                tapi.println("{xMusisz wybrać jedną z następujących opcji: " + sOptions + ".{x");
            }
        }
    }
}
