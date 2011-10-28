/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world;

import marinesmud.world.items.Item;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public class CountableMudObject {
	private final Item obj;
	private final int count;

	public CountableMudObject(Item obj_, int count_) {
		obj = obj_;
		count = count_;
	}

	public Item getObject() {
		return obj;
	}

	public int getCount() {
		return count;
	}
}
