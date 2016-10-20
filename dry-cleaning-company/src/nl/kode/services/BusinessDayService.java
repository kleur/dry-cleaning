package nl.kode.services;

import nl.kode.days.DayOfWeek;
import nl.kode.days.Day;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

/**
 * Created by koenvandeleur on 20/10/2016.
 */
public interface BusinessDayService {

    Day getDay(DateTime dateTime);

    void setRegularDay(LocalTime defaultOpeningTime, LocalTime defaultClosingTime);

    void addClosedDay(DayOfWeek dayOfWeek);

    void addClosedDate(DateTime dateTime);

    void addSpecialWeekDay(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime);

    void addSpecialDate(DateTime dateTime, LocalTime openingTime, LocalTime closingTime);

    void printClosedDates();

    int getDayIndex(DateTime date);
}
