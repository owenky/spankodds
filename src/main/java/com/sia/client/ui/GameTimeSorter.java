package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;

import java.text.SimpleDateFormat;
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

        return g1.getGametime().isBefore(g2.getGametime())?-1:1;
    }
}
 
