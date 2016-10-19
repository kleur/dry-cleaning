package nl.kode;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by koenvandeleur on 18/10/2016.
 */
public class TimeService {

    public long timeStringToMillis(String time) {
        String[] units = time.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]);
        return ((hours * 60) + minutes) * 60 * 1000;
    }

    public DateTime getDateWithoutTime(DateTime d) {

        return d.withTimeAtStartOfDay();
    }

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

    public List<SimpleDateFormat> getPatterns(){
        List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();

        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm.ss'Z'"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd"));

        return knownPatterns;
    }

    public int getDayIndex(DateTime date) {
        return date.getDayOfWeek()==7? 0 : date.getDayOfWeek();
    }
}