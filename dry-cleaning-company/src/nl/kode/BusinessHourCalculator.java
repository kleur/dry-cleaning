package nl.kode;

import nl.kode.days.Day;
import nl.kode.days.DayOfWeek;
import nl.kode.days.impl.BusinessDay;
import nl.kode.services.BusinessDayService;
import nl.kode.services.impl.DefaultBusinessDayService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by koenvandeleur on 18/10/2016.
 */
public class BusinessHourCalculator {

    private BusinessDayService businessDayService;

    public BusinessHourCalculator(String defaultOpeningTime, String defaultClosingTime) {
        super();
        businessDayService = new DefaultBusinessDayService(parseTime(defaultOpeningTime), parseTime(defaultClosingTime));
    }

    private LocalTime parseTime(String localTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(localTimeString, formatter);
    }

    public void setOpeningHours(DayOfWeek dayOfWeek, String openingTime, String closingTime) {

    }

    public void setOpeningHours(String date, String openingTime, String closingTime) {

    }

    public void setClosed(DayOfWeek... dayOfWeeks) {
        for (DayOfWeek day : dayOfWeeks) {

        }
    }

    public void setClosed(String... dates) {
        for (String date : dates) {

        }
    }

    public Date calculateDeadline(long steamTimeSeconds, String dateString) {
        Date dropOffDate = parseDate(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm"), dateString);

        Calendar calDropOff = Calendar.getInstance();
        calDropOff.setTime(dropOffDate);

        System.out.println("DROPOFF TIME: " + calDropOff.getTime());

        Calendar calPointer = calDropOff;
        long workingTimeSeconds = Long.valueOf(0);

        while(workingTimeSeconds < steamTimeSeconds) {

            // get the day
            Day day = businessDayService.getDay(calPointer);
            long timeStillOpen = day.getTimeStillOpen(calPointer);
            if(timeStillOpen > 0) {
                workingTimeSeconds += timeStillOpen;
                if(timeStillOpen >= steamTimeSeconds) {

                    int secondsToOpeningTime = ((BusinessDay)day).getOpeningTime().toSecondOfDay();
                    calPointer.add(Calendar.SECOND, secondsToOpeningTime);
                    calPointer.add(Calendar.SECOND, (int) steamTimeSeconds);
                    return calPointer.getTime();
                }
            }

            calPointer.add(Calendar.DAY_OF_MONTH, 1);
            calPointer = starOfDay(calPointer);
        }

//        calPointer.add(Calendar.SECOND, (Long.valueOf(workingTimeSeconds).intValue()));

        return calPointer.getTime();
    }

    private Calendar starOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public void printSpecials() {

    }

    public Date parseDate(DateFormat df, String input) {
        try {
            return df.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("unable to parse String to Date");
    }

}
