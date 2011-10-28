/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.security;

import pl.jblew.code.jutils.utils.security.MD5Generator;
import pl.jblew.code.jutils.utils.security.SHA1Generator;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public final class PasswordEncryptor {

	private PasswordEncryptor() {
	}

	public static String encrypt(String istr) {
		return MD5Generator.getHexMD5(SHA1Generator.getSHA1(istr+MudSecurityManager.getPasswordSalt())+istr);
	}
}

