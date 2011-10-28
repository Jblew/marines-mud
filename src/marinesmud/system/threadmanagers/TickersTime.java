/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.system.threadmanagers;

/**
 *
 * @author jblew
 */
public class TickersTime {

    public final long value;
    public final Tickers.Unit unit;

    public TickersTime(long value, Tickers.Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public static TickersTime valueOf(String s) {
        if (s.length() < 1) {
            throw new IllegalArgumentException("Bad TickersTime(" + s + ").");
        }
        char lastLetter = s.charAt(s.length() - 1);
        long value = Long.valueOf(s.substring(0, s.length() - 1));
        if(value < 1) throw new IllegalArgumentException("Tickers time cannot be negative or zero.");

        if (lastLetter == 'm') {
            return new TickersTime(value, Tickers.Unit.MINITICK);
        } else if (lastLetter == 't') {
            return new TickersTime(value, Tickers.Unit.MINITICK);
        } else if (lastLetter == 'u') {
            return new TickersTime(value, Tickers.Unit.MICROTICK);
        } else {
            throw new IllegalArgumentException("Bad TickersTime(" + s + ").");
        }
    }

    @Override
    public String toString() {
        return value+" "+unit.name().toLowerCase()+((value > 1)? "s" : "");
    }
}
