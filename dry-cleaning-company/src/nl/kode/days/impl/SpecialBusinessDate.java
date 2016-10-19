package nl.kode.days.impl;

import org.joda.time.DateTime;

/**
 * Created by koenvandeleur on 19/10/2016.
 */
public class SpecialBusinessDate extends BusinessDay{

    private DateTime dateTime;

    public SpecialBusinessDate(long openingTime, long closingTime, DateTime dateTime) {
        super(openingTime, closingTime);
        this.dateTime = dateTime;
    }
}
