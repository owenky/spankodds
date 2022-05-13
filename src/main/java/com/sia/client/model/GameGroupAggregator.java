package com.sia.client.model;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.ui.AppController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;

public class GameGroupAggregator {

    private final SportType sportType;
    private final boolean toIncludeStatusHeader;
    private final Function<Game,Boolean> gameFilter;

    public GameGroupAggregator(SportType sportType,Function<Game,Boolean> gameFilter,boolean toIncludeStatusHeader) {
        this.sportType = sportType;
        this.toIncludeStatusHeader = toIncludeStatusHeader;
        this.gameFilter = gameFilter;
    }
    public Map<GameGroupHeader, Vector<Game>> aggregate() {
        Map<GameGroupHeader, Vector<Game>> headerMap = new HashMap<>();
        Games allgames = AppController.getGamesVec();
        Iterator<Game> ite = allgames.iterator();
        while ( ite.hasNext()) {
            Game g = ite.next();
            if (!gameFilter.apply(g) ) {
                Sport sport = GameUtils.getSport(g);
                if ( sportType.getSportName().equals(SiaConst.SportName.Baseball) && null != sport && SiaConst.SportName.Baseball.equals(sport.getSportname())) {
                    Utils.log("DEBUG GAME MISSING: game not added to sport " + sportType.getSportName() + ", game info=" + GameUtils.getGameDebugInfo(g));
                }
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
