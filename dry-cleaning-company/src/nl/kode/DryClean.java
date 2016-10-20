package nl.kode;

import nl.kode.days.DayOfWeek;

import java.text.SimpleDateFormat;

public class DryClean {

    public static void main(String[] args) {

        SimpleDateFormat dt1 = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");

        BusinessHourCalculator businessHourCalculator = new BusinessHourCalculator("09:00", "15:00");
        businessHourCalculator.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHourCalculator.setOpeningHours("2010-12-24", "8:00", "13:00");
        businessHourCalculator.setClosed(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY);
        businessHourCalculator.setClosed("2010-12-25");

        // example #10
        System.out.println(dt1.format(businessHourCalculator.calculateDeadline(2 * 60 * 60, "2010-06-07 09:10")));
        // => Mon Jun 07 11:10:00 2010

        // example #2
        System.out.println(dt1.format(businessHourCalculator.calculateDeadline(15 * 60, "2010-06-08 14:48")));
        // => Thu Jun 10 09:03:00 2010

        // example #3
        System.out.println(dt1.format(businessHourCalculator.calculateDeadline(7 * 60 * 60, "2010-12-24 6:45")));
        // => Mon Dec 27 11:00:00 2010

        runOwnTest();
    }

    private static void runOwnTest() {

        SimpleDateFormat dt1 = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");

        BusinessHourCalculator businessHourCalculator = new BusinessHourCalculator("09:00", "18:00");
        businessHourCalculator.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHourCalculator.setOpeningHours("2016-10-24", "12:00", "18:00");
        businessHourCalculator.setClosed(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        businessHourCalculator.setClosed("2016-10-25");

        System.out.println("\nPICKUP TIME: " + dt1.format(businessHourCalculator.calculateDeadline(12 * 60 * 60, "2016-10-21 16:30")));
        businessHourCalculator.printSpecials();

    }
}
