package com.sia.client.model;

import java.util.Comparator;

public class GameSorter implements Comparator<Game> {
	@Override
    public int compare(Game g1, Game g2) {

		if ( null == g1 && null != g2) {
			return -1;
		} else if ( null != g1 && null == g2) {
			return 1;
		} else if ( null == g1 && null== g2) {
			return 0;
		}

		return g1.getGame_id() - g2.getGame_id();
    }
}