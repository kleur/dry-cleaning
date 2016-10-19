package nl.kode;

import nl.kode.days.Day;
import nl.kode.days.impl.BusinessDay;
import nl.kode.days.impl.ClosedDay;
import nl.kode.days.impl.SpecialBusinessDate;
import nl.kode.days.impl.SpecialBusinessDay;
import org.joda.time.DateTime;

import java.util.*;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class BusinessDayService {

    private Day closedDay;
    private Day regularDay;
    private Map<DayOfWeek, Day> weekDays;
    private Map<DateTime, Day> dates;

    public BusinessDayService() {
        this.closedDay = new ClosedDay();
        this.weekDays = new LinkedHashMap<>();
        this.dates = new LinkedHashMap<>();
    }

    public Day getDay(DateTime dateTime) {

        DayOfWeek weekDayName = DayOfWeek.values()[getDayIndex(dateTime)];

        Day day = regularDay;

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

    private int getDayIndex(DateTime date) {
        return date.getDayOfWeek()==7? 0 : date.getDayOfWeek();
    }

    public void addClosedDay(DayOfWeek dayOfWeek) {
        weekDays.put(dayOfWeek, new ClosedDay());
    }

    public void addClosedDate(Date date) {
        dates.put((new DateTime(date).withTimeAtStartOfDay()), new ClosedDay());
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
    }
}
