package com.sia.client.model;

import com.sia.client.config.Utils;
import com.sia.client.simulator.LocalMessageLogger;
import com.sia.client.ui.AppController;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SportTypeTest {

    @BeforeClass
    public static void setup() {
        Utils.logger = new LocalMessageLogger();
        User.instance().init("username","password","email","address","city","state","country","phoneumber",
                "timezone","oddstype","notificationmethod","subscriptiontype",true,
                "bookiecolumnprefs","fixedcolumnprefs",
                "columncolors",
                "customtabs",
                "finalalert",
                "startedalert",
                "halftimealert",
                "lineupalert",
                "officialalert",
                "injuryalert",
                "timechangealert",
                "limitchangealert",
                "bigearnalert",
                "footballpref",
                "basketballpref",
                "baseballpref",
                "hockeypref",
                "fightingpref",
                "soccerpref",
                "autoracingpref",
                "golfpref",
                "tennispref",
                "chartfilename",
                1,
                2,
                "tabsindex",
                "linealerts");
        AppController.enrichUserProperties(User.instance());
    }
    @Test
    public void testGameSorter() {
       assertEquals("autoracingpref",SportType.AutoRacing.getPrefName());
       assertEquals("footballpref",SportType.Football.getPrefName());
    }
}
