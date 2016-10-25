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

    private SimpleDateFormat df;

    private BusinessDayService businessDayService;

    public BusinessHourCalculator(String defaultOpeningTime, String defaultClosingTime) {
        super();
        businessDayService = new DefaultBusinessDayService(
                parseTime(defaultOpeningTime),
                parseTime(defaultClosingTime));
        df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
    }

    public void setOpeningHours(DayOfWeek dayOfWeek, String openingTime, String closingTime) {
        businessDayService.addSpecialWeekDay(dayOfWeek, parseTime(openingTime), parseTime(closingTime));
    }

    public void setOpeningHours(String date, String openingTime, String closingTime) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(parseDate(df, date));
//        businessDayService.addSpecialDate(cal, parseTime(openingTime), parseTime(closingTime));
    }

    public void setClosed(DayOfWeek... dayOfWeeks) {
        for (DayOfWeek day : dayOfWeeks) {
//            businessDayService.addClosedDay(day);
        }
    }

    public void setClosed(String... dates) {
        for (String date : dates) {
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(parseDate(df, date));
//            businessDayService.addClosedDate(cal);
        }
    }

    public Date calculateDeadline(long steamTimeSeconds, String dateString) {
        Date dropOffDate = parseDate(df, dateString);
        System.out.println("\nDROPOFF TIME: " + dropOffDate);

        return calculateDeadline(steamTimeSeconds, dropOffDate);
    }

    private Date calculateDeadline(long steamTimeSeconds, Date dropOffDate) {
        Calendar calDropOff = Calendar.getInstance();
        calDropOff.setTime(dropOffDate);
        System.out.println("Time to dryclean: " + printTime(steamTimeSeconds));

        Calendar calPointer = calDropOff;
        long timeActuallySteamed = Long.valueOf(0);
        long timeLeftBeforePickup = steamTimeSeconds;

        while(timeActuallySteamed < steamTimeSeconds) {

            // get the day
            Day day = businessDayService.getDay(calPointer);
            long timeStillOpen = day.getTimeStillOpen(calPointer);

            // check if store is open today and if it is before closing time, otherwise to next day
            if(timeStillOpen > 0) {

                // add full timeslot if the items can't be picked up today
                if (timeStillOpen < timeLeftBeforePickup) {

                    timeActuallySteamed += timeStillOpen;
                    timeLeftBeforePickup = steamTimeSeconds - timeActuallySteamed;

                    calPointer.add(Calendar.SECOND, (int)timeStillOpen);

                    logDay(timeStillOpen, timeActuallySteamed, calPointer);

                    System.out.println("\ntime still open today: " + printTime(timeStillOpen));
                    System.out.println("time steamed today: " + printTime(timeStillOpen));
                    System.out.println("time actually steamed in total: " + printTime(timeActuallySteamed));
                    System.out.println("time to go : " + printTime(timeLeftBeforePickup));
                    System.out.println("End of day: " + calPointer.getTime() + "\n");

                } else {
                    // move pointer back to opening time
                    calPointer.add(Calendar.SECOND, ((BusinessDay) day).getOpeningTime().toSecondOfDay());
                    System.out.println("\nStart of day: " + ((BusinessDay) day).getOpeningTime());
                    System.out.println("actual pointer: " + calPointer.getTime());

                    calPointer.add(Calendar.SECOND, (int) timeLeftBeforePickup);
                    System.out.println("time to go: " + printTime(timeLeftBeforePickup));
                    return  calPointer.getTime();
                }
            }

            // move to next day
            calPointer.add(Calendar.DAY_OF_MONTH, 1);
            System.out.println("added one day");
            calPointer = startOfDay(calPointer);
            System.out.println("pointer back to 00:00 of day");
        }
        throw new RuntimeException("The drycleaner has gone bankrupt");
    }

    private void logDay(long timeStillOpen, long timeActuallySteamed, Calendar calPointer) {

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

    private LocalTime parseTime(String localTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(localTimeString, formatter);
    }

}
