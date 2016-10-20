package nl.kode.days.impl;

import nl.kode.days.Day;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class BusinessDay implements Day {

    private LocalTime openingTime;
    private LocalTime closingTime;

    public BusinessDay(LocalTime openingTime, LocalTime closingTime) {
        super();
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    @Override
    public Interval getTimeSlot(DateTime fromTime) {

        // Skip if already past end of the day
        if (fromTime.getMillisOfDay() >= closingTime.getMillisOfDay()) {
            return null;
        }

        // If start time is too early, set start of timeslot to opening time
        if (fromTime.getMillisOfDay() < openingTime.getMillisOfDay()) {
            fromTime = fromTime.withTimeAtStartOfDay().withMillisOfDay((int) openingTime.getMillisOfDay());
        }

        DateTime toTime = fromTime.withTimeAtStartOfDay().withMillisOfDay((int) closingTime.getMillisOfDay());

        return new Interval(fromTime, toTime);
    }
}
