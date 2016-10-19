package nl.kode;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class BusinessDayService {

    private long defaultOpen;
    private long defaultClose;

    private final List<DayOfWeek> closedDays;
    private final List<DateTime> closedDates;

    private TimeService timeService;

    public BusinessDayService(TimeService timeService) {
        this.timeService = timeService;
        this.closedDays = new LinkedList<>();
        this.closedDates = new LinkedList<>();
    }

    public Interval getTimeSlotForDay(DateTime start) {

        System.out.println("start: " + start);

        // Skip if closed on this day
        if (closedDays.contains(DayOfWeek.values()[timeService.getDayIndex(start)])) {
            System.out.println("CLOSED ON DAY: " + DayOfWeek.values()[timeService.getDayIndex(start)]);
            return null;
        }

        if(closedDates.contains(timeService.getDateWithoutTime(start))) {
            System.out.println("CLOSED ON DATE: " + timeService.getDateWithoutTime(start));
            return null;
        }

        // Skip if already past end of the day
        if (start.getMillisOfDay() >= defaultClose) {
            System.out.println("start time: " + start.getMillisOfDay()/1000/60);
            System.out.println("default close: " + defaultClose /1000/60);
            return null;
        }

        // Set start of timeslot
        DateTime fromTime = start;

        // If start time is too early, set start of timeslot to opening time
        if (fromTime.getMillisOfDay() < defaultOpen) {
            System.out.println("start time too early");
            fromTime = timeService.getDateWithoutTime(fromTime).withMillisOfDay((int) defaultOpen);
        }

        System.out.println("from: " + fromTime.getHourOfDay()+ " o'clock");
        System.out.println("to: " + timeService.getDateWithoutTime(start).withMillisOfDay((int) defaultClose).getHourOfDay() + " o'clock");

        return new Interval(fromTime, timeService.getDateWithoutTime(start).withMillisOfDay((int) defaultClose));
    }

    public TimeService getTimeService() {
        return timeService;
    }

    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
    }

    public long getDefaultOpen() {
        return defaultOpen;
    }

    public void setDefaultOpen(long defaultOpen) {
        this.defaultOpen = defaultOpen;
    }

    public long getDefaultClose() {
        return defaultClose;
    }

    public void setDefaultClose(long defaultClose) {
        this.defaultClose = defaultClose;
    }

    public void addClosedDay(DayOfWeek closedDay) {
        this.closedDays.add(closedDay);
    }

    public void addClosedDate(Date date) {
        this.closedDates.add(timeService.getDateWithoutTime(new DateTime(date)));
    }

    public void printClosedDates() {
        System.out.println("PRINT CLOSED DATES");
        for (DateTime dateTime : closedDates) {
            System.out.println(dateTime);
        }
    }
}
