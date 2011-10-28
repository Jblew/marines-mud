/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.time;

import java.text.NumberFormat;
import pl.jblew.code.jutils.data.containers.tuples.string.TwoStringTuple;
import pl.jblew.code.jutils.utils.MathUtils;

/**
 *
 * @author jblew
 * @todo: TODO: Dodać pory roku i nachylenie słońca w dwóch osiach; Dokonczyc cykl sloneczny.
 */
public final class MudDate {
    private final long timestamp;
    //private final long mudTimestamp;
    private final long mudYear;
    private final long mudMonth;
    private final long mudDay;
    private final long mudHour;
    private final long mudMin;
    private final long mudSec;
    private final long mudMSec;
    private final long mudDayOfWeek;
    private final double yearPointer;
    private final double lengthOfDayInHours;
    private final double lengthOfNightInHours;
    private final double dayStartHour;
    private final double dayEndHour;
    private final double sunPosition;
    private final double moonPosition;
    private final Era era;

    protected MudDate(long timestamp_) {
        VirtualTimeConstants v = VirtualTimeConstants.getInstance();
        timestamp = timestamp_ - v.zeroTimeMillis;
        //mudTimestamp = timestamp*v.timeMultiplayer;

        long _mudMSec = timestamp * v.timeMultiplayer;
        long _mudSec = _mudMSec / v.millisecondsInSecond;
        long _mudMin = _mudSec / v.secondsInMinute;
        long _mudHour = _mudMin / v.minutesInHour;
        long _mudDay = _mudHour / v.hoursInDay;
        long _mudMonth = _mudDay / v.daysInMonth;

        long _mudYear = _mudMonth / v.monthsInYear;
        mudYear = _mudYear;

        mudMonth = _mudMonth - _mudYear * v.monthsInYear + 1;
        mudDay = _mudDay - _mudMonth * v.daysInMonth + 1;
        mudHour = _mudHour - _mudDay * v.hoursInDay;
        mudMin = _mudMin - _mudHour * v.minutesInHour;
        mudSec = _mudSec - _mudMin * v.secondsInMinute;
        mudMSec = _mudMSec - _mudSec * v.millisecondsInSecond;

        mudDayOfWeek = _mudDay % v.daysInWeek;

        Era era_ = null;

        for (Era e : v.eras) {
            if (e.start.getTime() <= this.timestamp + v.zeroTimeMillis && this.timestamp + v.zeroTimeMillis < e.end.getTime()) {
                era_ = e;
                break;
            }
        }

        if (era_ == null) {
            throw new RuntimeException("No era fits current mud time!");
        }

        era = era_;


        long secondsFromYearStart = (((getMonth() * v.daysInMonth + getDayOfMonth()) * v.hoursInDay + getHour()) * v.minutesInHour + getMinute()) * v.secondsInMinute + getSecond();

        yearPointer = ((double) secondsFromYearStart) / ((double) (v.secondsInYear));


        lengthOfDayInHours = ((double)v.hoursInDay/2d)+MathUtils.sin(yearPointer - 0.25 + v.longestDayPointer) * (((double)v.dayLengthAmplitudeInHours)/2d);
        lengthOfNightInHours = (double)v.hoursInDay - lengthOfDayInHours;

        dayStartHour = ((double)v.hoursInDay/2d) - (lengthOfDayInHours / 2d);
        dayEndHour = ((double)v.hoursInDay/2d) + (lengthOfDayInHours / 2d);

        double _sunPosition = ((double)getHour() + (double)getMinute()/(double)v.minutesInHour)/lengthOfDayInHours - dayStartHour / lengthOfDayInHours;
        if(_sunPosition < 0 || _sunPosition > 1) _sunPosition = -1;
        sunPosition = _sunPosition;

        double nightHour = ((double)getHour() + (double)getMinute()/(double)v.minutesInHour)+((double)v.hoursInDay/2d) - dayEndHour + ((double)v.hoursInDay/2d);
        while(nightHour >= (double)v.hoursInDay) nightHour -= (double)v.hoursInDay;
        double _moonPosition = nightHour/lengthOfNightInHours;

        if(_moonPosition < 0 || _moonPosition > 1) _moonPosition = -1;
        moonPosition = _moonPosition;
    }

    public Era getEra() {
        return era;
    }

    public int getAbsoluteYear() {
        return (int) mudYear;
    }

    public int getYearOfEra() {
        //System.out.println(new MudDate(era.start.getTime()).getAbsoluteYear());
        return (int) (mudYear - new MudDate(era.start.getTime()).getAbsoluteYear());
    }

    public int getMonth() {
        return (int) mudMonth;
    }

    public TwoStringTuple getMonthName() {
        return VirtualTimeConstants.getInstance().months[(((int) mudMonth) - 1)];
    }

    public int getDayOfMonth() {
        return (int) mudDay;
    }

    public int getDayOfWeek() {
        return (int) mudDayOfWeek;
    }

    public String getDayOfWeekName() {
        return VirtualTimeConstants.getInstance().daysOfWeek[(int) mudDayOfWeek];
    }

    public int getHour() {
        return (int) mudHour;
    }

    public int getMinute() {
        return (int) mudMin;
    }

    public int getSecond() {
        return (int) mudSec;
    }

    public int getMilisecond() {
        return (int) mudMSec;
    }

    public double getSunPosition() {
        return sunPosition;
    }

    public double getMoonPosition() {
        return moonPosition;
    }

    public String getPoeticTimeRepresentation() {
        return (String) VirtualTimeConstants.getInstance().getPoeticTimeRepresentationFunction().doIt(this);
    }

    public String getTimeCommandResponse() {
        return (String) VirtualTimeConstants.getInstance().getTimeCommandResponseFunction().doIt(this);
    }

    public String getStringTime() {
        return "era=" + era.name
                + "; absoluteYear=" + getAbsoluteYear()
                + "; yearOfEra=" + getYearOfEra()
                + "; month=" + getMonth() + "(" + getMonthName().first + ")"
                + "; day=" + getDayOfMonth()
                + "; dayOfWeek=" + getDayOfWeek() + " (" + VirtualTimeConstants.getInstance().daysOfWeek[(int) mudDayOfWeek] + ")"
                + "; hour=" + getHour()
                + "; min=" + getMinute()
                + "; sec=" + mudSec
                + "; msec=" + mudMSec
                + "; partOfDay=" + getPartOfDay().name
                + "; yearPointer=" + NumberFormat.getPercentInstance().format(yearPointer)
                + ".\n sunPosition=" + sunPosition
                +"; dayStartHour = "+dayStartHour
                +"; dayEndHour = "+dayEndHour
                +"; dayLength = "+lengthOfDayInHours;
    }

    public PartOfDay getPartOfDay() {
        Object[] _partsOfDay = PartOfDay.Manager.getPartsOfDay().toArray();
        PartOfDay last = (PartOfDay) _partsOfDay[_partsOfDay.length - 1];
        for (Object o : _partsOfDay) {
            PartOfDay partOfDay = (PartOfDay) o;
            double _sunPosition = getSunPosition();
            if(_sunPosition == 1) _sunPosition = 2;
            if (_sunPosition > partOfDay.sunPosition ) {
                last = partOfDay;
                continue;
            } else {
                return last;
            }
        }
        return last;
    }

    public boolean isPartOfDay(String name) {
        return (getPartOfDay() == PartOfDay.Manager.getPartOfDay(name));
    }

    public double getDayLength() {
        return lengthOfDayInHours;
    }
}
