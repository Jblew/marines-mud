/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.helpers;

import java.util.Arrays;
import java.util.Collection;
import marinesmud.tap.CloseUserConnectionRuntimeException;
import marinesmud.tap.Telnet;


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

    public static String prompt(Telnet.CommunicationManager communicationManager, String s) {
        communicationManager.print(s + " ");
        return communicationManager.blockingReadLine();
    }

    public static boolean booleanPrompt(Telnet.CommunicationManager communicationManager, String msg) {
        String out;
        while (true) {
            out = prompt(communicationManager, msg + " [t/n] ");
            if (out.equalsIgnoreCase("t")) {
                return true;
            } else if (out.equalsIgnoreCase("n")) {
                return false;
            } else {
                communicationManager.println("{rWpisz {Rt{x lub {Rn{x.");
            }
        }
    }

    public static String prompt(Telnet.CommunicationManager communicationManager, String msg, String[] options) {
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
            out = prompt(communicationManager, msg + " [" + sOptions + "] ");
            if (Arrays.asList(options).contains(out)) {
                return out;
            } else {
                communicationManager.println("{xMusisz wybrać jedną z następujących opcji: " + sOptions + ".{x");
            }
        }
    }

    public static String prompt(Telnet.CommunicationManager communicationManager, String msg, Collection<String> options) {
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
            out = prompt(communicationManager, msg + " [" + sOptions + "] ");
            if (Arrays.asList(options).contains(out)) {
                return out;
            } else {
                communicationManager.println("{xMusisz wybrać jedną z następujących opcji: " + sOptions + ".{x");
            }
        }
    }
}
