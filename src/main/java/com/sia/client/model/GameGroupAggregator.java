package com.sia.client.model;

import com.sia.client.config.GameUtils;
import com.sia.client.ui.AppController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class GameGroupAggregator {

    private final SportType sportType;
    private final boolean toIncludeStatusHeader;

    public GameGroupAggregator(SportType sportType,boolean toIncludeStatusHeader) {
        this.sportType = sportType;
        this.toIncludeStatusHeader = toIncludeStatusHeader;
    }
    public Map<GameGroupHeader, Vector<Game>> aggregate() {
        Map<GameGroupHeader, Vector<Game>> headerMap = new HashMap<>();
        Games allgames = AppController.getGamesVec();
        Iterator<Game> ite = allgames.iterator();
        while ( ite.hasNext()) {
            Game g = ite.next();
            if (!sportType.shouldSelect(g)) {
//if ( sportType.equals(SportType.Fighting) && null != g.getGamedate() && "2021-12-05".equals(g.getGamedate().toString())) {
//    System.out.println("skiped game id:"+g.getGame_id());
//}
                continue;
            }
            if (null == g.getStatus()) {
                g.setStatus("");
            }
            if (null == g.getTimeremaining()) {
                g.setTimeremaining("");
            }

            GameGroupHeader gameGroupHeader;
            GameStatus status;
            if ( toIncludeStatusHeader) {
                status = GameStatus.getGameStatus(g);
            } else {
                status = null;
            }

            if ( null != status) {
                gameGroupHeader = status.getGroupHeader();
            } else {
                gameGroupHeader = GameUtils.createGameGroupHeader(g);
            }

            Vector<Game> gameVec = headerMap.computeIfAbsent(gameGroupHeader,(key)-> new Vector<>());
            gameVec.add(g);
        }
        return headerMap;
    }
}
