package nl.kode;

import com.google.common.collect.Iterables;
import nl.kode.days.DayOfWeek;
import nl.kode.services.TimeService;
import nl.kode.services.impl.DefaultBusinessDayService;
import nl.kode.services.impl.DefaultTimeService;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by koenvandeleur on 18/10/2016.
 */
public class BusinessHourCalculator {

    private TimeService timeService;
    private DefaultBusinessDayService businessDayService;

    public BusinessHourCalculator(String defaultOpeningTime, String defaultClosingTime) {
        super();
        this.timeService = new DefaultTimeService();
        this.businessDayService = new DefaultBusinessDayService(
                timeService.parseDate(defaultOpeningTime).toLocalTime(),
                timeService.parseDate(defaultClosingTime).toLocalTime()
        );
    }

    public void setOpeningHours(DayOfWeek dayOfWeek, String openingTime, String closingTime) {
        businessDayService.addSpecialWeekDay(dayOfWeek,
                timeService.parseDate(openingTime).toLocalTime(),
                timeService.parseDate(closingTime).toLocalTime()
        );
    }

    public void setOpeningHours(String date, String openingTime, String closingTime) {
        businessDayService.addSpecialDate(new DateTime(date),
                timeService.parseDate(openingTime).toLocalTime(),
                timeService.parseDate(closingTime).toLocalTime());
    }

    public void setClosed(DayOfWeek... dayOfWeeks) {
        for (DayOfWeek day : dayOfWeeks) {
            businessDayService.addClosedDay(day);
        }
    }

    public void setClosed(String... dates) {
        for (String date : dates) {
            businessDayService.addClosedDate(timeService.parseDate(date));
        }
    }

    public Date calculateDeadline(long steamTimeSeconds, String dateString) {

        DateTime pointer = timeService.parseDate(dateString);

        // set duration of time it takes to dryclean
        // and duration the items have actually been in the store during business hours
        Duration waitTime = Duration.standardSeconds(steamTimeSeconds);
        Duration businessTimeLeft = new Duration(0);

        List<Interval> intervals = new ArrayList<>();
        while (waitTime.isLongerThan(businessTimeLeft)) {

            Interval timeSlot = businessDayService.getDay(pointer).getTimeSlot(pointer);
            if (timeSlot != null) {
                businessTimeLeft = businessTimeLeft.plus(timeSlot.toDuration());
                intervals.add(timeSlot);
            }

            // to the next day if closed this day or past closing time
            pointer = pointer.withTimeAtStartOfDay().plusDays(1);
        }

        Interval lastInterval = Iterables.getLast(intervals);
        DateTime endTime = lastInterval.getEnd().minus(businessTimeLeft.minus(waitTime));

        return new Date(endTime.getMillis());
    }

    public void printSpecials() {
        businessDayService.printClosedDates();
    }

}
