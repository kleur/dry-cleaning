package nl.kode.days.impl;

import nl.kode.TimeService;
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
    public Interval getTimeSlot(DateTime fromTime) {

        // Skip if already past end of the day
        if (fromTime.getMillisOfDay() >= closingTime) {
            return null;
        }

        // If start time is too early, set start of timeslot to opening time
        if (fromTime.getMillisOfDay() < openingTime) {
            fromTime = fromTime.withTimeAtStartOfDay().withMillisOfDay((int) openingTime);
        }

        DateTime toTime = fromTime.withTimeAtStartOfDay().withMillisOfDay((int) closingTime);

        return new Interval(fromTime, toTime);
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
