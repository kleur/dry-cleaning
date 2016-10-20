package nl.kode.services;

import nl.kode.days.DayOfWeek;
import nl.kode.days.Day;
import org.joda.time.DateTime;

/**
 * Created by koenvandeleur on 20/10/2016.
 */
public interface BusinessDayService {

    Day getDay(DateTime dateTime);

    void setRegularDay(long defaultOpeningTime, long defaultClosingTime);

    void addClosedDay(DayOfWeek dayOfWeek);

    void addClosedDate(DateTime dateTime);

    void addSpecialWeekDay(DayOfWeek dayOfWeek, long openingTime, long closingTime);

    void addSpecialDate(DateTime dateTime, long openingTime, long closingTime);

    void printClosedDates();

    int getDayIndex(DateTime date);
}
