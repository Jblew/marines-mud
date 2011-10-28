/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.tap;

/**
 *
 * @author jblew
 */
public class CloseUserConnectionRuntimeException extends RuntimeException {
	public CloseUserConnectionRuntimeException(String message) {
		super(message);
	}

	public CloseUserConnectionRuntimeException() {
		super();
	}
}
