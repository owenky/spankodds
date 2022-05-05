package com.sia.client.model;

import java.util.Comparator;

public class GameTimeSorter implements Comparator<Game> {
	@Override
    public int compare(Game g1, Game g2) {

		if ( null == g1 && null != g2) {
			return -1;
		} else if ( null != g1 && null == g2) {
			return 1;
		} else if ( null == g1 && null== g2) {
			return 0;
		}

		long diff = g1.getGametime() - g2.getGametime();
		if ( diff > 0L) {
			return 1;
		} else if ( diff < 0) {
			return -1;
		} else {
			return 0;
		}
    }
}