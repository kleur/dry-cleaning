package nl.kode.services;

import nl.kode.days.DayOfWeek;
import nl.kode.days.Day;
import java.time.LocalTime;
import java.util.Calendar;


/**
 * Created by koenvandeleur on 20/10/2016.
 */
public interface BusinessDayService {

    Day getDay(Calendar cal);

    void setRegularDay(LocalTime defaultOpeningTime, LocalTime defaultClosingTime);

    void addClosedDay(DayOfWeek dayOfWeek);

    void addClosedDate(Calendar date);

    void addSpecialWeekDay(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime);

    void addSpecialDate(Calendar dateTime, LocalTime openingTime, LocalTime closingTime);

    int getDayIndex(Calendar date);
}
