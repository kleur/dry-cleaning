package nl.kode.days;

import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public interface Day {

    public Interval getTimeSlot(DateTime dateTime);

}
