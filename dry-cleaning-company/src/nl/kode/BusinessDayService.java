package nl.kode;

import nl.kode.days.Day;
import nl.kode.days.impl.BusinessDay;
import nl.kode.days.impl.ClosedDay;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class BusinessDayService {

    private Day closedDay;
    private Day regularDay;

    private List<DayOfWeek> closedDays;
    private List<DateTime> closedDates;
    private List<Day> specialDays;

    private TimeService timeService;

    public BusinessDayService(TimeService timeService) {
        this.timeService = timeService;
        this.closedDays = new LinkedList<>();
        this.closedDates = new LinkedList<>();
        this.closedDay = new ClosedDay();
    }

    public Interval getTimeSlotForDate(DateTime start) {

        Day day = regularDay;

        System.out.println("----------------------");
        System.out.println("day open at: " + day.getTimeSlot(start).getStartMillis()/1000/60/60);
        System.out.println("day close at: " + day.getTimeSlot(start).getEndMillis()/1000/60/60);
        System.out.println("----------------------");
        System.out.println("start: " + start);

        // Skip if closed on this day
        if (closedDays.contains(DayOfWeek.values()[timeService.getDayIndex(start)])) {
            System.out.println("CLOSED ON DAY: " + DayOfWeek.values()[timeService.getDayIndex(start)]);
            return closedDay.getTimeSlot(start);
        }

        if(closedDates.contains(timeService.getDateWithoutTime(start))) {
            System.out.println("CLOSED ON DATE: " + timeService.getDateWithoutTime(start));
            return closedDay.getTimeSlot(start);
        }

        // Skip if already past end of the day
        if (start.getMillisOfDay() >= day.getTimeSlot(start).getEndMillis()) {
            System.out.println("start time: " + start.getMillisOfDay()/1000/60);
            System.out.println("default close: " + day.getTimeSlot(start).getEndMillis()/1000/60);
            return closedDay.getTimeSlot(start);
        }

        // Set start of timeslot
        DateTime fromTime = start;

        // If start time is too early, set start of timeslot to opening time
        if (fromTime.getMillisOfDay() < day.getTimeSlot(start).getStartMillis()) {
            System.out.println("start time too early");
            fromTime = timeService.getDateWithoutTime(fromTime).withMillisOfDay((int) day.getTimeSlot(start).getStartMillis());
        }

//        System.out.println("from: " + fromTime.getHourOfDay()+ " o'clock");
//        System.out.println("to: " + timeService.getDateWithoutTime(start).withMillisOfDay((int) day.getTimeSlot(start).getEndMillis()).getHourOfDay() + " o'clock");

        return new Interval(fromTime, timeService.getDateWithoutTime(start).withMillisOfDay((int) day.getTimeSlot(start).getEndMillis()));
    }

    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
    }

    public void addClosedDay(DayOfWeek closedDay) {
        this.closedDays.add(closedDay);
    }

    public void addClosedDate(Date date) {
        this.closedDates.add(timeService.getDateWithoutTime(new DateTime(date)));
    }

    public void setRegularDay(long openingTime, long closingTime) {
        regularDay = new BusinessDay(openingTime, closingTime);
    }

    public void printClosedDates() {
        System.out.println("PRINT CLOSED DATES");
        for (DateTime dateTime : closedDates) {
            System.out.println(dateTime);
        }
    }
}
