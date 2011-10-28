/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.system.enities;

import marinesmud.world.area.room.Room;

/**
 *
 * @author jblew
 */
public class XYRoom {
	public final int x;
	public final int y;
	public final Room r;

	public XYRoom(int x_, int y_, Room r_) {
		x = x_;
		y = y_;
		r = r_;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final XYRoom other = (XYRoom) obj;
		if (this.x != other.x) {
			return false;
		}
		if (this.y != other.y) {
			return false;
		}
		if (this.r != other.r && (this.r == null || !this.r.equals(other.r))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + this.x;
		hash = 79 * hash + this.y;
		hash = 79 * hash + (this.r != null ? this.r.hashCode() : 0);
		return hash;
	}
}