package com.sia.client.simulator.filter;

import com.sia.client.config.GameUtils;
import com.sia.client.model.Game;
import com.sia.client.model.Sport;
import com.sia.client.ui.AppController;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SportFilter extends MesgFilterType {
    private static final Pattern pattern = Pattern.compile("(gameid=)(\\d+)");
    @Override
    public boolean test(String text) {

        boolean status;
        Matcher matcher = pattern.matcher(text);
        if ( matcher.find() && 2== matcher.groupCount()) {
           String gameId = matcher.group(2);
           Game game = AppController.getGame(Integer.parseInt(gameId));
           Sport sport = GameUtils.getSport(game);
           if (null != sport) {
               String sportName = sport.getSportname();
               status = Arrays.stream(getConditions()).anyMatch(s -> s.equalsIgnoreCase(sportName));
           } else {
               status = false;
           }
        } else {
            status = false;
        }
        return status;
    }
    public static void main(String [] argv) {
        String str = "####@@@@gameid=224741@#_#_#@period=0@#_#_";
        new SportFilter().test(str);
    }
}
