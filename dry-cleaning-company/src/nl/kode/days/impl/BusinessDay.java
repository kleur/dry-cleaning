package nl.kode.days.impl;

import nl.kode.days.Day;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.time.*;
import java.util.Calendar;

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
        System.out.println("default opening times:");
        System.out.println("opening: " + openingTime.toString());
        System.out.println("closing: " + closingTime.toString());
    }

    @Override
    public Interval getTimeSlot(DateTime fromTime) {
        return null;
    }

    @Override
    public long getTimeStillOpen(Calendar calendar) {
        Instant instant = Instant.ofEpochMilli(calendar.getTime().getTime());
        LocalTime pointerTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();

        if (pointerTime.isAfter(closingTime)) {
            return 0;
        }

        if (pointerTime.isBefore(openingTime)) {
            pointerTime = openingTime;
        }

        Duration duration = Duration.between(pointerTime, closingTime);
        long time = duration.getSeconds();
        return time;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
}
