package nl.kode.services.impl;

import nl.kode.days.DayOfWeek;
import nl.kode.days.Day;
import nl.kode.days.impl.BusinessDay;
import nl.kode.days.impl.ClosedDay;
import nl.kode.services.BusinessDayService;
import java.time.LocalTime;
import java.util.*;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class DefaultBusinessDayService implements BusinessDayService {

    private Day regularBusinessDay;
    private Map<DayOfWeek, Day> weekDays;
    private Map<Calendar, Day> dates;

    public DefaultBusinessDayService(LocalTime defaultOpeningTime, LocalTime defaultClosingTime) {
        this.weekDays = new LinkedHashMap<>();
        this.dates = new LinkedHashMap<>();
        this.setRegularDay(defaultOpeningTime, defaultClosingTime);
    }

    @Override
    public Day getDay(Calendar cal) {

        DayOfWeek weekDayName = DayOfWeek.values()[getDayIndex(cal)];

        System.out.println("WeekdayName: " + weekDayName + " actually: " + cal.getTime() );

        Day day = regularBusinessDay;

        // Check for special weekday
        if (weekDays.containsKey(weekDayName)) {
            day = weekDays.get(weekDayName);
        }

        // Check for special date
        if (dates.containsKey(cal)) {
            day = dates.get(cal);
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
    public void addClosedDate(Calendar cal) {
        dates.put(cal, new ClosedDay());
    }

    @Override
    public void addSpecialWeekDay(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        weekDays.put(dayOfWeek, (new BusinessDay(openingTime, closingTime)));
    }

    @Override
    public void addSpecialDate(Calendar cal, LocalTime openingTime, LocalTime closingTime) {
        dates.put(cal, (new BusinessDay(openingTime, closingTime)));
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
            if ((map.get(o)).getTimeStillOpen(null) > 0) {

            }
        }
    }

//    private void printOpeningTimes(Interval timeSlot, String daySpecification) {
//        String msg = "closed";
//        if (timeSlot != null) {
//            LocalTime openingTime = new LocalTime(timeSlot.getStart().getHourOfDay(), timeSlot.getStart().getMinuteOfHour());
//            LocalTime closingTime = new LocalTime(timeSlot.getEnd().getHourOfDay(), timeSlot.getEnd().getMinuteOfHour());
//            msg = openingTime.toString("HH:mm") + " until " + closingTime.toString("HH:mm");
//        }
//
//        System.out.println("Special day: " + msg + " on " + daySpecification);
//    }

    @Override
    public int getDayIndex(Calendar date) {
        return  date.get(Calendar.DAY_OF_WEEK) -1;
    }

}
