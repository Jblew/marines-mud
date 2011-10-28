/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.system.enities;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public class RoomMessage {
	public final String message;
	public final int roomVnum;

	public RoomMessage(String message_, int roomVnum_) {
		message = message_;
		roomVnum = roomVnum_;
	}
}
