package nl.kode;

import java.util.*;

/**
 * Created by koenvandeleur on 18/10/2016.
 */
public interface BusinessHourCalculator {


    void setOpeningHours(DayOfWeek dayOfWeek, String openingTime, String closingTime);

    void setOpeningHours(String date, String openingTime, String closingTime);

    void setClosed(DayOfWeek... dayOfWeeks);

    void setClosed(String... dates);

    Date calculateDeadline(long dryCleanTime, String dateString);

    void printSpecials();

}
