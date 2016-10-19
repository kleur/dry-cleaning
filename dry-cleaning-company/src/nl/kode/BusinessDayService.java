package nl.kode;

import nl.kode.days.Day;
import nl.kode.days.impl.BusinessDay;
import nl.kode.days.impl.ClosedDay;
import nl.kode.days.impl.SpecialBusinessDate;
import nl.kode.days.impl.SpecialBusinessDay;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.*;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class BusinessDayService {

    private Day closedDay;
    private Day regularDay;

    private List<DayOfWeek> closedDays;
    private List<DateTime> closedDates;
    private Map<DayOfWeek, Day> weekDays;
    private Map<DateTime, Day> dates;

    private TimeService timeService;

    public BusinessDayService(TimeService timeService) {
        this.timeService = timeService;
        this.closedDays = new LinkedList<>();
        this.closedDates = new LinkedList<>();
        this.closedDay = new ClosedDay();
        this.weekDays = new LinkedHashMap<>();
        this.dates = new LinkedHashMap<>();
    }

    public Interval getTimeSlotForDate(DateTime start) {

        System.out.println("pointer: " + start);
        DayOfWeek startDayOfWeek = DayOfWeek.values()[timeService.getDayIndex(start)];

        Day day = regularDay;

        // Check for special day
        if (weekDays.containsKey(startDayOfWeek)) {
            day = weekDays.get(startDayOfWeek);
        }

        // Check for special date
        if (dates.containsKey(timeService.getDateWithoutTime(start))) {
            day = dates.get(timeService.getDateWithoutTime(start));
        }

        // Skip if closed on this day
        if (closedDays.contains(startDayOfWeek)) {
            System.out.println("CLOSED ON DAY: " + startDayOfWeek);
            return closedDay.getTimeSlot(start);
        }

        // Skip if closed on this date
        if (closedDates.contains(timeService.getDateWithoutTime(start))) {
            System.out.println("CLOSED ON DATE: " + timeService.getDateWithoutTime(start));
            return closedDay.getTimeSlot(start);
        }



        System.out.println("----------------------");
        System.out.println("day open at: " + day.getTimeSlot(start).getStartMillis()/1000/60/60);
        System.out.println("day close at: " + day.getTimeSlot(start).getEndMillis()/1000/60/60);
        System.out.println("----------------------");


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

        DateTime toTime = timeService.getDateWithoutTime(start).withMillisOfDay((int) day.getTimeSlot(start).getEndMillis());

        System.out.println("still open from: " + fromTime.getHourOfDay() + ":" + fromTime.getMinuteOfHour() + " to: " +
                toTime.getHourOfDay() + ":" + toTime.getMinuteOfHour());

        return new Interval(fromTime, timeService.getDateWithoutTime(start).withMillisOfDay((int) day.getTimeSlot(start).getEndMillis()));
    }

    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
    }

    public void addClosedDay(DayOfWeek dayOfWeek) {
        weekDays.put(dayOfWeek, new ClosedDay());
        this.closedDays.add(dayOfWeek);
    }

    public void addClosedDate(Date date) {
        this.closedDates.add(timeService.getDateWithoutTime(new DateTime(date)));
    }

    public void setRegularDay(long openingTime, long closingTime) {
        regularDay = new BusinessDay(openingTime, closingTime);
    }

    public void addSpecialWeekDay(DayOfWeek dayOfWeek, long openingTime, long closingTime) {
        weekDays.put(dayOfWeek, (new SpecialBusinessDay(openingTime, closingTime, dayOfWeek)));
    }

    public void addSpecialDate(DateTime dateTime, long openingTime, long closingTime) {
        dates.put(dateTime, (new SpecialBusinessDate(openingTime, closingTime, dateTime)));
    }

    public void printClosedDates() {

        System.out.println("PRINT SPECIAL WEEKDAYS");
        for (DayOfWeek wkday : weekDays.keySet()) {
            String msg = weekDays.get(wkday).getTimeSlot(DateTime.now()) != null ? (weekDays.get(wkday).getTimeSlot(DateTime.now()).getStartMillis()/1000/60/60 + " till "
                    + weekDays.get(wkday).getTimeSlot(DateTime.now()).getEndMillis()/1000/60/60 + " o'clock") : "closed";

            System.out.println("Special day: " + msg + " on " + wkday.toString());
        }
        System.out.println("");

        System.out.println("PRINT SPECIAL DATES");
        for (DateTime dateTime : dates.keySet()) {
            String msg = dates.get(dateTime).getTimeSlot(DateTime.now()) != null ? (dates.get(dateTime).getTimeSlot(DateTime.now()).getStartMillis()/1000/60/60 + " till "
                    + dates.get(dateTime).getTimeSlot(DateTime.now()).getEndMillis()/1000/60/60 + " o'clock" + dateTime.getDayOfMonth() + "/" + dateTime.getMonthOfYear()) : "closed";

            System.out.println("Special day: " + msg + " on " + dateTime);
        }

        System.out.println("\nold list:");
        for (DateTime dateTime : closedDates) {
            System.out.println(dateTime);
        }

    }
}
