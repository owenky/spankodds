package com.sia.client.model;

import java.util.Comparator;

public interface GameGroupHeaderSorter extends Comparator<GameGroupHeader> {

    default int compareAnchorPos(GameGroupHeader g1, GameGroupHeader g2) {

        if (g1 == null || g2 == null) {
            return 0;
        } else {
            return g1.getAnchorPos() - g2.getAnchorPos();
        }
    }
}