/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.system.enities;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author jblew
 */
public class LagTime implements Cloneable {
	public final TimeUnit unit;
	public final int value;

	public LagTime(TimeUnit unit_, int value_) {
		unit = unit_;
		value = value_;
	}

	@Override
	public LagTime clone() {
		return new LagTime(unit, value);
	}
}
