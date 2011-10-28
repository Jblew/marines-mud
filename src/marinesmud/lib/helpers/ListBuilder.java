/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.helpers;

/**
 *
 * @author jblew
 */
public class ListBuilder {

    private ListBuilder() {
    }

    public static ListBuilder getInstance() {
        return ListBuilderHolder.INSTANCE;
    }

    private static class ListBuilderHolder {

        private static final ListBuilder INSTANCE = new ListBuilder();
    }

    public static String createSimpleList(Iterable<String> elements, String separator) {
        String out = "";
        boolean first = true;

        for (String elem : elements) {
            if (!first) {
                out += separator;
            }
            first = false;

            out += elem;
        }
        return out;
    }

    public static String createSimpleList(String[] elements, String separator) {
        String out = "";
        boolean first = true;

        for (String elem : elements) {
            if (!first) {
                out += separator;
            }
            first = false;

            out += elem;
        }
        return out;
    }

    public static String createSimpleList(Object[] elements, String separator) {
        String out = "";
        boolean first = true;

        for (Object elem : elements) {
            if (!first) {
                out += separator;
            }
            first = false;

            out += elem;
        }
        return out;
    }

    public static String createSimpleList(byte[] elements, String separator) {
        String out = "";
        boolean first = true;

        for (byte elem : elements) {
            if (!first) {
                out += separator;
            }
            first = false;

            out += elem;
        }
        return out;
    }
}
