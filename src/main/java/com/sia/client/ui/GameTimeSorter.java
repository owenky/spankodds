package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;

import java.text.SimpleDateFormat;
import java.util.Comparator;

public class GameTimeSorter implements Comparator<Game> {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public int compare(Game g1, Game g2) {
        //if(g1.getGame_id() == 483)
        //{
        //	log("gmtime for 483 ="+g1.getGametime();)
        //}
        if (g1 == null || g2 == null || g1.getGametime() == null || g2.getGametime() == null) {
            return 0;
        }
		if ( g1.getGame_id() == SiaConst.BlankGameId) {
			return -1;
		} else if ( g2.getGame_id() == SiaConst.BlankGameId) {
			return 1;
		}
        String gametime1 = sdf.format(g1.getGametime());
        String gametime2 = sdf.format(g2.getGametime());
        return gametime1.compareTo(gametime2);
    }
}
 
