package com.sia.client.config;

import com.sia.client.model.Game;
import org.junit.Test;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sia.client.config.Utils.log;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class UtilsTest {

    public static void main(String [] argv) {
        URL url = Utils.getMediaResource("boxing.png");
        log("url="+url);

        url = Utils.getConfigResource("spankoddsurlinfo.txt");
        log("url="+url);
    }
    @Test
    public void testContainsOnlyAlphanumeric() {
        assertFalse(Utils.containsOnlyAlphanumeric(""));
        assertFalse(Utils.containsOnlyAlphanumeric(" "));
        assertFalse(Utils.containsOnlyAlphanumeric("a b"));
        assertFalse(Utils.containsOnlyAlphanumeric("a.b"));
        assertTrue(Utils.containsOnlyAlphanumeric("aa"));
        assertTrue(Utils.containsOnlyAlphanumeric("aa"));
        assertTrue(Utils.containsOnlyAlphanumeric("0Aaa09Z"));
        assertFalse(Utils.containsOnlyAlphanumeric(" 0Aaa09Z"));
        assertFalse(Utils.containsOnlyAlphanumeric("0Aaa09Z "));
        assertFalse(Utils.containsOnlyAlphanumeric(".0Aaa09Z"));
        assertFalse(Utils.containsOnlyAlphanumeric("-0Aaa09Z"));
        assertFalse(Utils.containsOnlyAlphanumeric("+0Aaa09Z"));
        assertFalse(Utils.containsOnlyAlphanumeric("0Aa+a09Z"));
    }
    @Test
    public void testIsIntegerString() {
        assertTrue(Utils.isIntegerString("-123"));
        assertTrue(Utils.isIntegerString("123"));
        assertTrue(Utils.isIntegerString(" 123"));
        assertTrue(Utils.isIntegerString("123 "));
        assertTrue(Utils.isIntegerString(" 123 "));
        assertFalse(Utils.isIntegerString("123a1"));
        assertFalse(Utils.isIntegerString(""));
        assertFalse(Utils.isIntegerString("1.23"));
        assertFalse(Utils.isIntegerString("1 23"));
        assertFalse(Utils.isIntegerString(null));
        assertFalse(Utils.isIntegerString(""));
        assertFalse(Utils.isIntegerString(" "));
        assertFalse(Utils.isIntegerString("abc"));
    }
    @Test
    public void testIsNumericString() {
        assertTrue(Utils.isNumericString("123"));
        assertTrue(Utils.isNumericString(" 123"));
        assertTrue(Utils.isNumericString("123 "));
        assertTrue(Utils.isNumericString(" 123 "));
        assertTrue(Utils.isNumericString("0.1"));
        assertTrue(Utils.isNumericString("-123"));
        assertTrue(Utils.isNumericString("-123."));
        assertTrue(Utils.isNumericString("-123.0"));
        assertTrue(Utils.isNumericString("-.01"));
        assertTrue(Utils.isNumericString(".01"));
        assertTrue(Utils.isNumericString("1."));
        assertTrue(Utils.isNumericString("-1."));
        assertTrue(Utils.isNumericString("1.23"));
        assertTrue(Utils.isNumericString("-1.23"));
        assertFalse(Utils.isNumericString(""));
        assertFalse(Utils.isNumericString("1 23"));
        assertFalse(Utils.isNumericString(null));
        assertFalse(Utils.isNumericString(""));
        assertFalse(Utils.isNumericString(" "));
        assertFalse(Utils.isNumericString("abc"));
        assertFalse(Utils.isNumericString("."));
        assertFalse(Utils.isNumericString("1..0"));
        assertFalse(Utils.isNumericString("123a1"));
    }
    @Test
    public void testGetTime1() throws ParseException {

        final int hours = 9;
        final int minute = 6;
        long milliseconds = (hours*60+minute)*60*1000L;
        final String dateStr = "2022-05-05";
        final String format = dateStr+" %02d:%02d";
        final String dateTimeStr = String.format(format,hours,minute);
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateTime = dateTimeFormat.parse(dateTimeStr);

        long time = Utils.getTime(dateTime);
        long diff = time - milliseconds;
        System.out.println("datetime="+dateTime+", diff="+diff/(60000*60));
        assertEquals(0, diff);

    }
    @Test
    public void testGetTime2() throws ParseException {

        final int hours = 19;
        final int minute = 6;
        long milliseconds = (hours*60+minute)*60*1000L;
        final String dateStr = "2022-05-05";
        final String format = dateStr+" %02d:%02d";
        final String dateTimeStr = String.format(format,hours,minute);
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateTime = dateTimeFormat.parse(dateTimeStr);

        long time = Utils.getTime(dateTime);
        long diff = time - milliseconds;
        System.out.println("datetime="+dateTime+", diff="+diff/(60000*60));
        assertEquals(0, diff);

    }
}
