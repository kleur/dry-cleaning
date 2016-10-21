package nl.kode.days.impl;

import nl.kode.days.Day;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import java.util.Calendar;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class ClosedDay implements Day {
    @Override
    public Interval getTimeSlot(DateTime dateTime) {
        return null;
    }

    @Override
    public long getTimeStillOpen(Calendar calendar) {
        return 0;
    }

}
