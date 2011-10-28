/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.helpers;

import java.util.Collection;
import pl.jblew.code.jutils.data.containers.tuples.string.StringTuple;
import pl.jblew.code.jutils.utils.MathUtils;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class TableBuilder {

	private TableBuilder() {
	}

	public static TableBuilder getInstance() {
		return TableBuilderHolder.INSTANCE;
	}

	private static class TableBuilderHolder {

		private static final TableBuilder INSTANCE = new TableBuilder();
	}

	public static String createTable(int leftPadding, String[] headers, StringTuple[] data) {
		String out = "";

		int maxTableWidth = 4;

		int[] columnsWidth = new int[headers.length];

		int i = 0;
		for (String header : headers) {
			if (i == 0) {
				columnsWidth[i] = 1 + header.length() + 2;
			} else {
				columnsWidth[i] = header.length() + 3;
			}
			i++;
		}

		for (StringTuple row : data) {
			String[] elements = row.getElements();
			int j = 0;
			for (String header : headers) {
				if (j == 0) {
					if (2 + elements[j].length() + 1 > columnsWidth[j]) {
						columnsWidth[j] = 2 + elements[j].length() + 1;
					}
				} else {
					if (elements[j].length() + 3 > columnsWidth[j]) {
						columnsWidth[j] = elements[j].length() + 3;
					}
				}
				j++;
			}
		}

		if (MathUtils.sum(columnsWidth) > maxTableWidth) {
			maxTableWidth = MathUtils.sum(columnsWidth);
		}

		out += TextUtils.repeatString(" ", leftPadding);
		out += "+";
		for (int w : columnsWidth) {
			out += TextUtils.repeatString("-", w - 1);
			out += "+";
		}
		out += "\n";
		out += TextUtils.repeatString(" ", leftPadding);
		out += "|";
		int j = 0;
		for (String header : headers) {
			out += " ";
			out += "{X"+header+"{x";
			out += TextUtils.repeatString(" ", columnsWidth[j] - 3 - header.length());
			out += " |";
			j++;
		}

		out += TextUtils.repeatString(" ", leftPadding);
		out += "\n";
		out += TextUtils.repeatString(" ", leftPadding);
		out += "+";
		for (int w : columnsWidth) {
			out += TextUtils.repeatString("-", w - 1);
			out += "+";
		}
		out += "\n";
		out += TextUtils.repeatString(" ", leftPadding);

		for (StringTuple row : data) {
			String[] elements = row.getElements();
			int k = 0;
			out += "|";
			for (int w : columnsWidth) {
				out += " ";
				out += elements[k];
				out += TextUtils.repeatString(" ", w - 3 - elements[k].length());
				out += " |";
				k++;
			}

			out += "\n";
			out += TextUtils.repeatString(" ", leftPadding);
			out += "+";
			for (int w : columnsWidth) {
				out += TextUtils.repeatString("-", w - 1);
				out += "+";
			}
			out += "\n";
			out += TextUtils.repeatString(" ", leftPadding);
		}

		return out;
	}

        public static String createSimpleTable(int leftPadding, int numColumns, Collection<StringTuple> data) {
		String out = "";


		int[] columnsWidth = new int[numColumns];

		for (StringTuple row : data) {
			String[] elements = row.getElements();
			int j = 0;
			for (int jj : columnsWidth) {
				if (j == 0) {
					if (2 + elements[j].length() + 1 > columnsWidth[j]) {
						columnsWidth[j] = 2 + elements[j].length() + 1;
					}
				} else {
					if (elements[j].length() + 3 > columnsWidth[j]) {
						columnsWidth[j] = elements[j].length() + 3;
					}
				}
				j++;
			}
		}

		out += TextUtils.repeatString(" ", leftPadding);


		for (StringTuple row : data) {
			String[] elements = row.getElements();
			int k = 0;
			out += " ";
			for (int w : columnsWidth) {
				out += " ";
				out += elements[k];
				out += TextUtils.repeatString(" ", w - 3 - elements[k].length());
				out += "  ";
				k++;
			}
			out += "\n";
			out += TextUtils.repeatString(" ", leftPadding);
		}

		return out;
	}
}
