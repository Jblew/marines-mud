/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.helpers;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author jblew
 */
public class TextWrapper {

	public final static List<String> colorLetters = Arrays.asList(new String[]{
				"v", "V", "r", "R", "g", "G", "y", "Y", "b", "B", "m", "M", "c", "C", "w", "W", "w", "x", "X"
			});

	private TextWrapper() {
	}

	public static TextWrapper getInstance() {
		return TextWrapperHolder.INSTANCE;
	}

	private static class TextWrapperHolder {

		private static final TextWrapper INSTANCE = new TextWrapper();
	}

	public static String prettyLongTextWrapWithTrim(String text, int width) {
		String out = "";
		int x = 0;
		char c = 'a';
		for (int i = 0; i < text.length(); i++) {
			c = text.charAt(i);
			if (c == '{' && (i + 1 < text.length() && colorLetters.contains(text.charAt(i + 1) + ""))) {
				out += "{" + text.charAt(i + 1);
				i++;
				continue;
			}

			if (x == 0 && c == ' ') {
			} else {
				x++;
			}

			if (x >= width - 1 && c != '\n') {
				if (c == ' ') {
					out += c + "\n";
					x = 0;
				} else if ((i + 1 < text.length() && text.charAt(i + 1) == ' ')) {
					out += c + "\n";
					x = 0;
				} else {
					if (i - 1 > 0 && text.charAt(i - 1) == ' ') {
						out += "\n" + c;
					} else {
						if (c != '-' && (i + 1 < text.length() && text.charAt(i + 1) != '\n')) {
							out += "-\n" + c;
						} else {
							out += "-";
						}
					}
					x = 1;
				}
			} else if (x == 0 && c == ' ') {
			} else {
				out += c + "";
			}
			if (c == '\n') {
				x = 0;
			}
		}
		return out.replace(" - ", "a{-}a").replace("- ", "").replace("a{-}a", " - ");
	}

	public static String hardWrap(String in, int width) {
		String out = "";
		String lines[] = in.split("\n");
		char c = 'a';
		for (String line : lines) {
			if (line.isEmpty()) {
				out += "\n";
				continue;
			}
			for (char ch : line.toCharArray()) {
				if (ch == ' ') {
					out += " ";
				} else {
					break;
				}
			}
			line = line.trim();
			int x = 0;

			for (int i = 0; i < line.length(); i++) {
				c = line.charAt(i);
				if (c == '{' && colorLetters.contains(line.charAt(i + 1) + "")) {
					out += "{" + line.charAt(i + 1);
					i++;
					continue;
				}

				if (c == ' ' && x == 0) {
				} else {
					out += c + "";
					x++;
					if (x >= width) {
						x = 0;
						if (c != '\n') {
							out += "\n";
						}
					}
				}
			}
			if (!out.endsWith("\n")) {
				out += "\n";
			}
		}

		/*int x = 0;
		for (char c : in.toCharArray()) {
		if (c == '\n') {
		x = 0;
		out += "\n";
		} else {
		out += c+"";
		}

		x++;

		if (x >= width && c != '\n') {
		x = 0;
		out += "\n";
		}

		}*/
		return out;
	}
}
