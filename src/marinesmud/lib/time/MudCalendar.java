/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.lib.time;

/**
 *
 * @author jblew
 */
public class MudCalendar {
    private MudCalendar() {
    }

    public static MudCalendar getInstance() {
        return MudCalendarHolder.INSTANCE;
    }

    private static class MudCalendarHolder {
        private static final MudCalendar INSTANCE = new MudCalendar();
    }

    public MudDate getDateTime() {
        return new MudDate(PrecisionTime.getCurrentTimeMillis());
    }
 }
