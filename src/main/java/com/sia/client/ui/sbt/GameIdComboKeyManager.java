package com.sia.client.ui.sbt;

import com.sia.client.config.GameUtils;
import com.sia.client.model.Game;
import com.sia.client.ui.games.GameSelectionItem;

import javax.swing.*;

public class GameIdComboKeyManager implements JComboBox.KeySelectionManager {

    private static final long maxSequeceTimeInterval = 2000L; //within this interval, keystrokes are considered as one search string
    private long lastKeyStroke = 0;
    private int lastIndex = 0;
    private boolean toSearchId;
    private final StringBuilder keyStrokeSequence = new StringBuilder();
    public GameIdComboKeyManager() {
    }
    @Override
    public int selectionForKey(char key,ComboBoxModel aModel) {
        GameSelectionItem selectedItem = (GameSelectionItem)aModel.getSelectedItem();
        Game game = selectedItem.getGame();
        if ( ! GameUtils.isRealGame(game)) {
            keyStrokeSequence.setLength(0);
            lastIndex = 0;
            toSearchId = Character.isDigit(key);
        }
//        long now = System.currentTimeMillis();
//        if ( maxSequeceTimeInterval < (now - lastKeyStroke))  {
//            keyStrokeSequence.setLength(0);
//            lastIndex = 0;
//            toSearchId = Character.isDigit(key);
//        }
//        lastKeyStroke = now;

        keyStrokeSequence.append(key);
        String searchStr = keyStrokeSequence.toString().toUpperCase();
        int i;
        for ( i=lastIndex;i<aModel.getSize();i++ ) {
            GameSelectionItem item = (GameSelectionItem)aModel.getElementAt(i);
            String sourceStr;
            if ( toSearchId) {
                sourceStr = String.valueOf(item.getGame().getGame_id());
            } else {
                sourceStr = GameSelectionItem.getGameDesc(item.getGame()).toUpperCase();
            }

            if ( sourceStr.startsWith(searchStr)) {
                lastIndex = i;
                break;
            }
        }

        return 0==i || i==aModel.getSize()? -1:lastIndex;
    }
}
