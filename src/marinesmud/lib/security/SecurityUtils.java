/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.lib.security;

import marinesmud.system.Config;
import pl.jblew.code.jutils.utils.security.MD5Generator;
import pl.jblew.code.jutils.utils.security.SHA1Generator;

/**
 *
 * @author jblew
 */
public class SecurityUtils {

    private SecurityUtils() {
    }

    public static SecurityUtils getInstance() {
        return SecurityUtilsHolder.INSTANCE;
    }

    private static class SecurityUtilsHolder {
        private static final SecurityUtils INSTANCE = new SecurityUtils();
    }

	public static String getCtrls(String s) {
		String out = MD5Generator.getHexMD5((SHA1Generator.getSHA1(s)+MD5Generator.getHexMD5(MudSecurityManager.getSessionUID()+MudSecurityManager.getSessionUID())).toUpperCase().replace("1", "H").replace("A", "2"));
		out += MD5Generator.getHexMD5(Config.SECRET_SALT+"ffff4535")+Config.SECRET_SALT+MudSecurityManager.getSessionUID();
		return out;
	}
 }
