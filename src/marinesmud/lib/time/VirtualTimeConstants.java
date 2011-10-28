/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.time;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import marinesmud.lib.simpleCompiler.SimpleCompiler;
import marinesmud.lib.simpleCompiler.SimpleOperation;
import marinesmud.system.Config;
import pl.jblew.code.jutils.data.containers.tuples.ThreeTuple;
import pl.jblew.code.jutils.data.containers.tuples.string.TwoStringTuple;
import pl.jblew.code.jutils.jezykpolski.PolskieLiczby;

/**
 *
 * @author jblew
 */
public class VirtualTimeConstants {
    public final int timeMultiplayer;
    public final int millisecondsInSecond;
    public final int secondsInMinute;
    public final int minutesInHour;
    public final int hoursInDay;
    public final int daysInMonth;
    public final int daysInWeek;
    public final int monthsInYear;
    public final Era[] eras;
    public final TwoStringTuple[] months;
    public final String[] daysOfWeek;
    private final SimpleOperation poeticTimeRepresentation;
    private final SimpleOperation timeCommandResponse;
    public final long zeroTimeMillis;
    public final long secondsInYear;
    public final double longestDayPointer;
    public final double shortestDayPointer;
    public final int dayLengthAmplitudeInHours;

    private VirtualTimeConstants() {
        timeMultiplayer = Config.getInt("time.time multiplayer");
        millisecondsInSecond = Config.getInt("time.milliseconds in second");
        secondsInMinute = Config.getInt("time.seconds in minute");
        minutesInHour = Config.getInt("time.minutes in hour");
        hoursInDay = Config.getInt("time.hours in day");
        daysInMonth = Config.getInt("time.days in month");

        List<Map<String, String>> months_ = Config.getList("time.months");
        months = new TwoStringTuple[months_.size()];
        int i = 0;
        for (Map<String, String> map : months_) {
            months[i] = new TwoStringTuple(map.get("mianownik"), map.get("dopelniacz"));
            i++;
        }
        monthsInYear = months.length;

        List<String> daysOfWeek_ = Config.getStringList("time.days of week");
        daysOfWeek = new String[daysOfWeek_.size()];
        i = 0;
        for (String elem : daysOfWeek_) {
            daysOfWeek[i] = elem;
            i++;
        }
        daysInWeek = daysOfWeek.length;

        List<Map<String, String>> rawEras = (List<Map<String, String>>) Config.getList("time.eras");
        List<ThreeTuple<String, String, Date>> eras_ = new LinkedList<ThreeTuple<String, String, Date>>();

        for (Map<String, String> map : rawEras) {
            int year = Integer.parseInt(map.get("start year"));
            int month = Integer.parseInt(map.get("start month"));
            int day = Integer.parseInt(map.get("start day"));
            int hour = Integer.parseInt(map.get("start hour"));
            int minute = Integer.parseInt(map.get("start minute"));
            int second = Integer.parseInt(map.get("start second"));

            GregorianCalendar calendar = new GregorianCalendar(year, month, day, hour, minute, second);
            eras_.add(new ThreeTuple<String, String, Date>(map.get("name"), map.get("dopelniacz"), calendar.getTime()));
        }

        eras = new Era[eras_.size()];
        i = 0;
        for (ThreeTuple<String, String, Date> tuple : eras_) {
            Date endDate = new Date(Long.MAX_VALUE); //CAUTION! Do not save this date objects! This value depends on architecture and runtime!
            if (i < eras_.size() - 1) {
                endDate = eras_.get(i + 1).third;
            }
            Era era = new Era(tuple.first, tuple.second, tuple.third, endDate);
            eras[i] = era;

            i++;
        }
        zeroTimeMillis = eras[0].start.getTime();

        secondsInYear = monthsInYear * daysInMonth * hoursInDay * minutesInHour * secondsInMinute;

        longestDayPointer = Config.getInt("time.longest day month") * daysInMonth + Config.getInt("time.longest day day");
        shortestDayPointer = 1 - longestDayPointer;

        dayLengthAmplitudeInHours = Config.getInt("time.day length amplitude in hours");

        poeticTimeRepresentation = new SimpleOperation() {
            public Object doIt(Object... args) {
                MudDate date = (MudDate) args[0];
                int hour = date.getHour();
                int minute = date.getMinute();
                String out = "";
                hour += 1;

                if (minute > 5 && minute < 22) {
                    out += "Minęła ";
                    hour -= 1;
                } else if (minute >= 22 && minute < 35) {
                    out += "Za pół godziny będzie ";
                } else if (minute >= 35 && minute < 53) {
                    out += "Dochodzi ";
                } else {
                    out += "Jest ";
                    if (minute >= 0 && minute <= 5) {
                        hour -= 1;
                    }
                }

                if (hour == 0) {
                    out += "północ.";
                } else if (hour > 0 && hour < 5) {
                    out += PolskieLiczby.godzina(hour) + " w nocy.";
                } else if (hour > 4 && hour < 12) {
                    out += PolskieLiczby.godzina(hour) + " rano.";
                } else if (hour > 11 && hour < 19) {
                    if ((hour - 12) == 0) {
                        out += "dwunasta po południu.";
                    } else {
                        out += PolskieLiczby.godzina(hour - 12) + " po południu.";
                    }
                } else if (hour > 18) {
                    out += PolskieLiczby.godzina(hour - 12) + " wieczorem.";
                }

                return out;
            }
        };

        timeCommandResponse = new SimpleOperation() {
            public Object doIt(Object... args) {
                MudDate date = (MudDate) args[0];
                String out = "Jest " + date.getDayOfWeekName() + ", "
                        + PolskieLiczby.liczebnikPorzadkowy(date.getDayOfMonth())
                        + " dzień " + date.getMonthName().second
                        + ", " + PolskieLiczby.liczebnikPorzadkowy(date.getYearOfEra())
                        + " rok ery " + date.getEra().dopelniacz + ".\n";

                out += date.getPoeticTimeRepresentation();

                return out;
            }
        };
    }

    public SimpleOperation getPoeticTimeRepresentationFunction() {
        return poeticTimeRepresentation;
    }

    public SimpleOperation getTimeCommandResponseFunction() {
        return timeCommandResponse;
    }

    public static VirtualTimeConstants getInstance() {
        //return InstanceHolder.INSTANCE;
        return INSTANCE;
    }
    //private static class InstanceHolder {
    private static final VirtualTimeConstants INSTANCE = new VirtualTimeConstants();
    //}
}
