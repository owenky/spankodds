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
}
