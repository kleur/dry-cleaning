package nl.kode.services.impl;

import nl.kode.services.TimeService;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by koenvandeleur on 18/10/2016.
 */
public class DefaultTimeService implements TimeService {

    @Override
    public long timeStringToMillis(String time) {
        return new DateTime(parseDate(time)).getMillisOfDay();
    }

    @Override
    public Date parseDate(String date) {
        for (SimpleDateFormat pattern : getPatterns()) {
            try {
                return new Date(pattern.parse(date).getTime());
            } catch (ParseException pe) {
                // do nothing
            }
        }
        throw new RuntimeException("no matching patterns");
    }

    @Override
    public List<SimpleDateFormat> getPatterns(){
        List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();

//        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
//        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm.ss'Z'"));
//        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
//        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"));
//        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd"));
        knownPatterns.add(new SimpleDateFormat("HH:mm"));

        return knownPatterns;
    }
}
