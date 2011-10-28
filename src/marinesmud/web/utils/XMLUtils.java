/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.web.utils;

/**
 *
 * @author jblew
 */
public class XMLUtils {
	private XMLUtils() {
	}

	public static String getXMLTag(String version, String encoding) {
		return "<?xml version=\""+version+"\" encoding=\""+encoding+"\"?>";
	}
	
}
