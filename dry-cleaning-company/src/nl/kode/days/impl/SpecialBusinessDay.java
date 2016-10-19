package nl.kode.days.impl;

import nl.kode.DayOfWeek;
import org.joda.time.DateTime;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class SpecialBusinessDay extends BusinessDay {

    private DayOfWeek dayOfWeek;

    public SpecialBusinessDay(long openingTime, long closingTime, DayOfWeek dayOfWeek) {
        super(openingTime, closingTime);
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
}
