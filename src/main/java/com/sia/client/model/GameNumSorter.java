package com.sia.client.model;

import com.sia.client.config.SiaConst;

import java.util.Comparator;

public class GameNumSorter implements Comparator<Game> {
    public int compare(Game g1, Game g2) {
        if (g1 == null || g2 == null) {
            return 0;
        }

        if ( g1.getGame_id() == SiaConst.BlankGameId) {
            return -1;
        } else if ( g2.getGame_id() == SiaConst.BlankGameId) {
            return 1;
        } else if (g1.getGame_id() <= g2.getGame_id()) {
            return -1;
        } else {
            return 1;
        }
    }
}
