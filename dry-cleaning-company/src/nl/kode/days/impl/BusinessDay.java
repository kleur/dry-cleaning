package nl.kode.days.impl;

import nl.kode.days.Day;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class BusinessDay implements Day {

    long openingTime;
    long closingTime;

    public BusinessDay(long openingTime, long closingTime) {
        super();
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    @Override
    public Interval getTimeSlot(DateTime dateTime) {
        return new Interval(dateTime.withMillis(openingTime), dateTime.withMillis(closingTime));
    }

    public long getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(long openingTime) {
        this.openingTime = openingTime;
    }

    public long getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(long closingTime) {
        this.closingTime = closingTime;
    }
}
