package com.sia.client.config;

import org.junit.Test;

import java.net.URL;

import static com.sia.client.config.Utils.log;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}
