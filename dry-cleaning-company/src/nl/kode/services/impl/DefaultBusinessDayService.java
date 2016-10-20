package nl.kode.services.impl;

import nl.kode.days.DayOfWeek;
import nl.kode.days.Day;
import nl.kode.days.impl.BusinessDay;
import nl.kode.days.impl.ClosedDay;
import nl.kode.services.BusinessDayService;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import java.util.*;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class DefaultBusinessDayService implements BusinessDayService{

    private Day regularBusinessDay;
    private Map<DayOfWeek, Day> weekDays;
    private Map<DateTime, Day> dates;

    public DefaultBusinessDayService(LocalTime defaultOpeningTime, LocalTime defaultClosingTime) {
        this.weekDays = new LinkedHashMap<>();
        this.dates = new LinkedHashMap<>();
        this.setRegularDay(defaultOpeningTime, defaultClosingTime);
    }

    @Override
    public Day getDay(DateTime dateTime) {

        DayOfWeek weekDayName = DayOfWeek.values()[getDayIndex(dateTime)];

        Day day = regularBusinessDay;

        // Check for special day
        if (weekDays.containsKey(weekDayName)) {
            day = weekDays.get(weekDayName);
        }

        // Check for special date
        if (dates.containsKey(dateTime.withTimeAtStartOfDay())) {
            day = dates.get(dateTime.withTimeAtStartOfDay());
        }

        return day;
    }

    @Override
    public void setRegularDay(LocalTime defaultOpeningTime, LocalTime defaultClosingTime) {
        regularBusinessDay = new BusinessDay(defaultOpeningTime, defaultClosingTime);
    }

    @Override
    public void addClosedDay(DayOfWeek dayOfWeek) {
        weekDays.put(dayOfWeek, new ClosedDay());
    }

    @Override
    public void addClosedDate(DateTime dateTime) {
        dates.put((dateTime.withTimeAtStartOfDay()), new ClosedDay());
    }

    @Override
    public void addSpecialWeekDay(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        weekDays.put(dayOfWeek, (new BusinessDay(openingTime, closingTime)));
    }

    @Override
    public void addSpecialDate(DateTime dateTime, LocalTime openingTime, LocalTime closingTime) {
        dates.put(dateTime, (new BusinessDay(openingTime, closingTime)));
    }

    @Override
    public void printClosedDates() {
        System.out.println("\nPRINT SPECIAL WEEKDAYS");
        printDates(weekDays);

        System.out.println("\nPRINT SPECIAL DATES");
        printDates(dates);
    }

    private void printDates(Map<?, Day> map) {
        for (Object o : map.keySet()) {
            Interval timeSlot = map.get(o).getTimeSlot(DateTime.now().withTimeAtStartOfDay());
            printOpeningTimes(timeSlot, o.toString());
        }
    }

    private void printOpeningTimes(Interval timeSlot, String daySpecification) {
        String msg = "closed";
        if (timeSlot != null) {
            LocalTime openingTime = new LocalTime(timeSlot.getStart().getHourOfDay(), timeSlot.getStart().getMinuteOfHour());
            LocalTime closingTime = new LocalTime(timeSlot.getEnd().getHourOfDay(), timeSlot.getEnd().getMinuteOfHour());
            msg = openingTime.toString("HH:mm") + " until " + closingTime.toString("HH:mm");
        }

        System.out.println("Special day: " + msg + " on " + daySpecification);
    }

    @Override
    public int getDayIndex(DateTime date) {
        return date.getDayOfWeek()==7? 0 : date.getDayOfWeek();
    }

}
