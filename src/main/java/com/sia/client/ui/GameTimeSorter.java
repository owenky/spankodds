package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Comparator;

public class GameTimeSorter implements Comparator<Game> {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public int compare(Game g1, Game g2) {

        if ( null == g1 && null == g2) {
            return 0;
        } else if ( null == g1 && null != g2) {
            return -1;
        } else if ( null != g1 && null == g2) {
            return 1;
        }

		if ( g1.getGame_id() == SiaConst.BlankGameId) {
			return -1;
		} else if ( g2.getGame_id() == SiaConst.BlankGameId) {
			return 1;
		}

        if ( null == g1.getGametime() && null == g2.getGametime()) {
            return 0;
        } else if ( null == g1.getGametime() && null != g2.getGametime()) {
            return -1;
        } else if ( null != g1.getGametime() && null == g2.getGametime()) {
            return 1;
        }

        LocalTime time1 = g1.getGametime();
        LocalTime time2 = g2.getGametime();
        return time1.isBefore(time2)?-1:time1.isAfter(time2)?1:0;
    }
}
 
