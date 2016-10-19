package nl.kode;

import com.google.common.collect.Iterables;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by koenvandeleur on 18/10/2016.
 */
public class DryCleanBusinessHourCalculator implements BusinessHourCalculator{

    private TimeService timeService;

    private BusinessDayService businessDayService;

    public DryCleanBusinessHourCalculator(String defaultOpeningTime, String defaultClosingTime) {
        super();
        this.timeService = new TimeService();
        this.businessDayService = new BusinessDayService(timeService);
        this.businessDayService.setTimeService(timeService);
        this.businessDayService.setRegularDay(
                timeService.timeStringToMillis(defaultOpeningTime),
                timeService.timeStringToMillis(defaultClosingTime));
    }

    @Override
    public void setOpeningHours(DayOfWeek dayOfWeek, String openingTime, String closingTime) {
        businessDayService.addSpecialWeekDay(dayOfWeek,
                timeService.timeStringToMillis(openingTime),
                timeService.timeStringToMillis(closingTime));
    }

    @Override
    public void setOpeningHours(String date, String openingTime, String closingTime) {
        businessDayService.addSpecialDate(new DateTime(timeService.parseDate(date)),
                timeService.timeStringToMillis(openingTime),
                timeService.timeStringToMillis(closingTime));
    }

    public void setClosed(DayOfWeek... dayOfWeeks)
    {
        for (DayOfWeek day : dayOfWeeks)
        {
            businessDayService.addClosedDay(day);
        }
    }

    @Override
    public void setClosed(String... dates)
    {
        for (String date : dates)
        {
            businessDayService.addClosedDate(timeService.parseDate(date));
        }
    }

    @Override
    public Date calculateDeadline(long steamTimeSeconds, String dateString) {

        Date startDate = timeService.parseDate(dateString);
        DateTime pointer = new DateTime(startDate);

        System.out.println("DROP OFF DATE/TIME: " + startDate);
        System.out.println(pointer);

        Duration waitTime = Duration.standardSeconds(steamTimeSeconds);
        Duration businessTimeLeft = new Duration(0);

        List<Interval> intervals = new ArrayList<>();
        while(waitTime.isLongerThan(businessTimeLeft)) {

            Interval timeSlot = businessDayService.getTimeSlotForDate(pointer);
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

    @Override
    public void printSpecials() {
        businessDayService.printClosedDates();
    }
    
}
