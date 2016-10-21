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
        System.out.println("\nDROPOFF TIME: " + dropOffDate);

        return calculateDeadline(steamTimeSeconds, dropOffDate);
    }

    public Date calculateDeadline(long steamTimeSeconds, Date dropOffDate) {
        Calendar calDropOff = Calendar.getInstance();
        calDropOff.setTime(dropOffDate);
        System.out.println("\nTime to dryclean: " + printTime(steamTimeSeconds));

        Calendar calPointer = calDropOff;
        long timeActuallySteamed = Long.valueOf(0);
        long timeLeftBeforePickup = steamTimeSeconds;

        while(timeActuallySteamed < steamTimeSeconds) {

            // get the day
            Day day = businessDayService.getDay(calPointer);
            long timeStillOpen = day.getTimeStillOpen(calPointer);

            if(timeStillOpen > 0) {

                System.out.println("\nStart of day: " + ((BusinessDay)day).getOpeningTime());

                if (timeStillOpen < timeLeftBeforePickup) {
                    timeActuallySteamed += timeStillOpen;
                    timeLeftBeforePickup = steamTimeSeconds - timeActuallySteamed;
                    logDay(timeStillOpen, timeActuallySteamed, calPointer);
                    calPointer = startOfDay(calPointer);

                    // move pointer back to opening time
                    calPointer.add(Calendar.SECOND, ((BusinessDay)day).getOpeningTime().toSecondOfDay());

                } else {
                    calPointer.add(Calendar.SECOND, (int) timeLeftBeforePickup);
                    return  calPointer.getTime();
                }
            }

            // move to next day
            calPointer.add(Calendar.DAY_OF_MONTH, 1);
        }
        throw new RuntimeException("The drycleaner has gone bankrupt");
    }

    private void logDay(long timeStillOpen, long timeActuallySteamed, Calendar calPointer) {
        System.out.println("time still open: " + printTime(timeStillOpen));
        System.out.println("time actually steamed: " + printTime(timeActuallySteamed));
        System.out.println("End of day: " + calPointer.getTime());
    }

    private String printTime(long time) {
        return (time/(60*60)) + " hours " + (time % (60*60))/60 + " minutes";
    }

    private Calendar startOfDay(Calendar cal) {
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
