package nl.kode.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by koenvandeleur on 18/10/2016.
 */
public interface TimeService {

    long timeStringToMillis(String time);

    Date parseDate(String date);

    List<SimpleDateFormat> getPatterns();
}
