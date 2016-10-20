package nl.kode.services;

import org.joda.time.DateTime;

/**
 * Created by koenvandeleur on 18/10/2016.
 */
public interface TimeService {

    DateTime parseDate(String date);
}
