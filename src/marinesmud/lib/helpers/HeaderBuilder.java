/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.helpers;

import marinesmud.system.Config;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class HeaderBuilder {

	private HeaderBuilder() {
	}

	public static String equalsHeader(String text) {
		int base = (Config.getInt("output width") - text.length() - 4);
		int equalswidth = base / 2;
		return "{B>" + TextUtils.repeatString("=", equalswidth) + "<{x" + text + "{B>" + TextUtils.repeatString("=", equalswidth+(base%2)) + "<{x";
	}

	public static String equalsBottom() {
		return "{B>" + TextUtils.repeatString("=", Config.getInt("output width") - 2) + "<{x";
	}
}
