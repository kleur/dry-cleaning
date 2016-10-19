package nl.kode;

import com.google.common.collect.Iterables;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import java.util.*;

/**
 * Created by koenvandeleur on 18/10/2016.
 */
public class BusinessHourCalculator {

    private TimeService timeService;

    private BusinessDayService businessDayService;

    public BusinessHourCalculator(String defaultOpeningTime, String defaultClosingTime) {
        super();
        this.timeService = new TimeService();
        this.businessDayService = new BusinessDayService(timeService);
        this.businessDayService.setTimeService(timeService);
        this.businessDayService.setDefaultOpen(timeService.timeStringToMillis(defaultOpeningTime));
        this.businessDayService.setDefaultClose(timeService.timeStringToMillis(defaultClosingTime));
    }

    public void setOpeningHours(DayOfWeek dayOfWeek, String openingTime, String closingTime) {
        //
    }

    public void setOpeningHours(String date, String openingTime, String closingTime) {
        //
    }

    public void setClosed(DayOfWeek... dayOfWeeks)
    {
        for (DayOfWeek day : dayOfWeeks)
        {
            businessDayService.addClosedDay(day);
        }
    }

    public void setClosed(String... dates)
    {
        for (String date : dates)
        {
            businessDayService.addClosedDate(timeService.parseDate(date));
        }
    }

    public Date calculateDeadline(long steamTimeSeconds, String dateString) {

        Date startDate = timeService.parseDate(dateString);
        DateTime pointer = new DateTime(startDate);

        System.out.println(startDate);
        System.out.println(pointer);

        Duration waitTime = Duration.standardSeconds(steamTimeSeconds);
        Duration businessTimeLeft = new Duration(0);

        List<Interval> intervals = new ArrayList<>();
        while(waitTime.isLongerThan(businessTimeLeft)) {

            Interval timeSlot = businessDayService.getTimeSlotForDay(pointer);
            if (timeSlot != null) {
                businessTimeLeft = businessTimeLeft.plus(timeSlot.toDuration());
                intervals.add(timeSlot);
                System.out.println(businessTimeLeft.getStandardHours() + " uur over");
            }

            // to the next day
            pointer = timeService.getDateWithoutTime(pointer).plusDays(1);
        }

        Interval lastInterval = Iterables.getLast(intervals);
        DateTime endTime = lastInterval.getEnd().minus(businessTimeLeft.minus(waitTime));

        return new Date(endTime.getMillis());
    }



}
