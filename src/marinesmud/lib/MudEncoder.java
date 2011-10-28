/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib;

import java.util.Map;
import pl.jblew.code.jutils.data.containers.tuples.string.TwoStringTuple;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 * EL: interface of simple encoder used eg. to "translate" from human language to ancient or dwarf.
 * PL: interfejs prostego enkodera, używanego np. do "tłumaczenia" z języka ludzi na starożytny, czy krasnoludow.
 *
 * @author jblew
 */
public interface MudEncoder {

    /**
     *
     * @param str - input string; wejściowy ciąg znaków
     * @return encoded string; zmieniony ciąg znaków
     */
    public String encode(String str);

    /**
     * PL: Typ enumeracyjny zawierający enkodery
     * EN: An enum type, containing encoders
     */
    public static enum Manager {

        /**
         * PL: Pusty enkoder, zwraca ciąg wejściowy
         * EN: Empty encoder, returns input string
         */
        EMPTY(new MudEncoder() {
            public String encode(String str) {
                return str;
            }
        }),

        /**
         * PL: Konwertuje ciąg wejściowy na język starożytny
         * EN: Converts input string to ancient language
         */
        ANCIENT(new MudEncoder() {
            private final Map<String, String> TRANSFORMS_MAP = Dictionary.getDictionary("ancient letter transforms").getElements();

            public String encode(String str) {
                String out = "";
                String in = str.toLowerCase();
                boolean[] upperCasedLetters = TextUtils.getUpperCasedLetters(str);
                String elem = "";
                int i = 0;

                for (char c : in.toCharArray()) {
                    elem = String.valueOf(c);
                    if (TRANSFORMS_MAP.containsKey(elem)) {
                        if (upperCasedLetters[i]) {
                            out += TRANSFORMS_MAP.get(elem).toUpperCase();
                        } else {
                            out += TRANSFORMS_MAP.get(elem);
                        }
                    } else {
                        if (upperCasedLetters[i]) {
                            out += elem.toUpperCase();
                        } else {
                            out += elem;
                        }
                    }
                    i++;
                }
                return out;
            }
        }),

        /**
         * PL: Konwertuje ciąg wejściowy na język krasnoludów
         * EN: Converts input string to dwarf language
         */
        DWARF(new MudEncoder() {
            private final TwoStringTuple[] replacements = new TwoStringTuple[]{
                new TwoStringTuple("sz", "sh"),
                new TwoStringTuple("ak", "agg"),
                new TwoStringTuple("ta", "dda")
            };

            public String encode(String str) {
                String out = str;
                for (TwoStringTuple tst : replacements) {
                    out = out.replace(tst.first, tst.second);
                }
                return out;
            }
        }),

        /**
         * PL: Konwertuje ciąg wejściowy na język elfów
         * EN: Converts input string to elf language
         */
        ELF(new MudEncoder() {
            private final TwoStringTuple[] replacements = new TwoStringTuple[]{
                new TwoStringTuple("f", "ff"),
                new TwoStringTuple("w", "f")
            };

            public String encode(String str) {
                String out = str;
                for (TwoStringTuple tst : replacements) {
                    out = out.replace(tst.first, tst.second);
                }
                return out;
            }
        }),

        /**
         * PL: Konwertuje ciąg wejściowy na base64
         * EN: Converts input string to base64
         */
        BASE64(new MudEncoder() {
            private final String base64code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
            private final int splitLinesAt = 76;

            private byte[] zeroPad(int length, byte[] bytes) {
                byte[] padded = new byte[length]; // initialized to zero by JVM
                System.arraycopy(bytes, 0, padded, 0, bytes.length);
                return padded;
            }

            public String encode(String string) {
                String encoded = "";
                byte[] stringArray;
                try {
                    stringArray = string.getBytes("UTF-8");  // use appropriate encoding string!
                } catch (Exception ignored) {
                    stringArray = string.getBytes();  // use locale default rather than croak
                }
                // determine how many padding bytes to add to the output
                int paddingCount = (3 - (stringArray.length % 3)) % 3;
                // add any necessary padding to the input
                stringArray = zeroPad(stringArray.length + paddingCount, stringArray);
                // process 3 bytes at a time, churning out 4 output bytes
                // worry about CRLF insertions later
                for (int i = 0; i < stringArray.length; i += 3) {
                    int j = ((stringArray[i] & 0xff) << 16)
                            + ((stringArray[i + 1] & 0xff) << 8)
                            + (stringArray[i + 2] & 0xff);
                    encoded = encoded + base64code.charAt((j >> 18) & 0x3f)
                            + base64code.charAt((j >> 12) & 0x3f)
                            + base64code.charAt((j >> 6) & 0x3f)
                            + base64code.charAt(j & 0x3f);
                }
                // replace encoded padding nulls with "="
                return splitLines(encoded.substring(0, encoded.length() - paddingCount) + "==".substring(0, paddingCount)).trim();
            }

            private String splitLines(String string) {
                String lines = "";
                for (int i = 0; i < string.length(); i += splitLinesAt) {

                    lines += string.substring(i, Math.min(string.length(), i + splitLinesAt));
                    lines += "\r\n";

                }
                return lines;
            }
        });


        private final MudEncoder encoder;

        private Manager(MudEncoder encoder) {
            this.encoder = encoder;
        }

        /**
         *
         * @return encoder; enkoder
         */
        public MudEncoder getEncoder() {
            return encoder;
        }
    }
}
