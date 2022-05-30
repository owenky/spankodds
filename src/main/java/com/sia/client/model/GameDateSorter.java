package com.sia.client.model;

import com.sia.client.config.SiaConst;

import java.time.LocalDate;
import java.util.Comparator;

public class GameDateSorter implements Comparator<Game> {

    public int compare(Game g1, Game g2) {
        if (g1 == null || g2 == null || g1.getGamedate() == null || g2.getGamedate() == null) {
            return 0;
        }
        if ( g1.getGame_id() == SiaConst.BlankGameId) {
        	return -1;
		} else if ( g2.getGame_id() == SiaConst.BlankGameId) {
			return 1;
		}
        LocalDate date1 = getNonNullDate(g1);
        LocalDate date2 = getNonNullDate(g1);
        return date1.isBefore(date2)?-1:date1.isAfter(date2)?1:0;
    }
    private static LocalDate getNonNullDate(Game g) {
        LocalDate rtn = g.getGamedate();
        if ( null == rtn) {
            rtn = LocalDate.MIN;
        }
        return rtn;
    }
}
 
