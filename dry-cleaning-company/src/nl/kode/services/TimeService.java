package nl.kode.services;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by koenvandeleur on 18/10/2016.
 */
public interface TimeService {

    long timeStringToMillis(String time);

    DateTime parseDate(String date);

    List<SimpleDateFormat> getPatterns();
}
