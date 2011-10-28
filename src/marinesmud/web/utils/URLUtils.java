/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jblew
 */
public class URLUtils {

	private URLUtils() {
	}

	public static Map<String, String> parseGetVars(String url) {
		Map<String, String> params = new HashMap<String, String>();
		String[] urlParts = url.split("\\?");
		if (urlParts.length > 1) {
			String query = urlParts[1];
			for (String param : query.split("&")) {
				try {
					String[] pair = param.split("=");
					if (pair.length > 1) {
						String key = URLDecoder.decode(pair[0], "UTF-8");
						String value = URLDecoder.decode(pair[1], "UTF-8");
						params.put(key, value);
					}
				} catch (UnsupportedEncodingException ex) {
					Logger.getLogger(URLUtils.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		return params;
	}
}
