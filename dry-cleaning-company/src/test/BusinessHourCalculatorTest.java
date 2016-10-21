package test;

import nl.kode.BusinessHourCalculator;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by koenvandeleur on 20/10/2016.
 */
public class BusinessHourCalculatorTest {

    private final String DEFAULT_OPEN = "09:00";
    private final String DEFAULT_CLOSE = "18:00";

    private BusinessHourCalculator calculator;
    private DateFormat formatFullDate;

    public BusinessHourCalculatorTest() {
        calculator = new BusinessHourCalculator(DEFAULT_OPEN, DEFAULT_CLOSE);
        formatFullDate = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
    }

    @org.junit.Before
    public void setUp() throws Exception {
        Assert.assertTrue(calculator != null);
    }

    @org.junit.Test
    public void calculateDeadline() throws Exception {
        String dateString;
        long seconds;

        DateTime result;
        DateTime expected;

        seconds = 2 * 60 * 60;
        dateString= "2016-10-21 10:30";

        DateTime dropOffTime = new DateTime(formatFullDate.parse(dateString));

        // expected is <seconds> after dropoff on the same business day
        result = new DateTime(calculator.calculateDeadline(seconds, dateString));
        expected = dropOffTime.plusSeconds((int) seconds);

        Assert.assertEquals(result, expected);




    }

    @org.junit.Test
    public void setOpeningHours() throws Exception {

    }

    @org.junit.Test
    public void setOpeningHours1() throws Exception {

    }

    @org.junit.Test
    public void setClosed() throws Exception {

    }

    @org.junit.Test
    public void setClosed1() throws Exception {

    }



}