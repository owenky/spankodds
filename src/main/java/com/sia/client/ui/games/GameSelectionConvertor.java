package com.sia.client.ui.games;

import com.sia.client.model.Game;
import com.sia.client.ui.sbt.SelectionConvertor;

public class GameSelectionConvertor implements SelectionConvertor<Integer,GameSelectionItem> {
    @Override
    public GameSelectionItem convertToSelectionItem(Integer keyValue, String display) {
        Game game = new Game();
        game.setGame_id(keyValue);
        return (GameSelectionItem)new GameSelectionItem(game).withDisplay(display);
    }

    @Override
    public Integer convertStringToKey(String keyString) {
        return Integer.parseInt(keyString);
    }

    @Override
    public Integer convertIntToKey(Integer keyValue) {
        return keyValue;
    }
}
