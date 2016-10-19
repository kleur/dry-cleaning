package nl.kode.days.impl;

import nl.kode.days.Day;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class ClosedDay implements Day {
    @Override
    public Interval getTimeSlot(DateTime dateTime) {
        return null;
    }
}
