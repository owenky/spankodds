package com.sia.client.model;

import com.sia.client.config.SiaConst;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class GameTest {

    @Test
    public void testSetGameDateTime() throws ParseException {
        Game game = new Game();

        final String dateTimeStr = "2022-05-05 09:06";
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateTime = dateTimeFormat.parse(dateTimeStr);
        final java.sql.Date sqlDateTime = new java.sql.Date(dateTime.getTime());


        final String dateStr = "2022-05-05";
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateStr);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        Long sqltime = (9*60+6)*60*1000L;

        game.setGameDateTime(sqlDate,sqltime);
        assertEquals(dateTime.getTime()- SiaConst.diffBetweenEasternAndUTC, game.gameDate.getTime());

        game.setGameDateTime(sqlDateTime,sqlDateTime.getTime());
        assertEquals(sqltime, (Long)(game.getGametime()+SiaConst.diffBetweenEasternAndUTC));
    }
}
