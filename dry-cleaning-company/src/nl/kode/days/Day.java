package nl.kode.days;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.time.LocalTime;
import java.util.Calendar;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public interface Day {

    public Interval getTimeSlot(DateTime dateTime);

    public long getTimeStillOpen(Calendar calendar);

}
