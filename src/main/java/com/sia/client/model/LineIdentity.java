package com.sia.client.model;

import com.sia.client.config.Utils;

import javax.jms.JMSException;
import javax.jms.MapMessage;

public class LineIdentity {

    private final int bookieId;
    private final int gameId;
    private final int period;

    public static LineIdentity parse(MapMessage mapMessage) {
        LineIdentity lineIdentity = null;
        try {
            String bookieIdStr = mapMessage.getString("bookieid");
            String periodStr = mapMessage.getString("period");
            String gameIdStr = mapMessage.getString("gameid");
            lineIdentity = new LineIdentity(Integer.parseInt(bookieIdStr), Integer.parseInt(gameIdStr), Integer.parseInt(periodStr));
        } catch (JMSException e) {
            Utils.log(e);
        }
        return lineIdentity;
    }

    public LineIdentity(int bookieId, int gameId, int period) {
        this.bookieId = bookieId;
        this.gameId = gameId;
        this.period = period;
    }

    public int getBookieId() {
        return bookieId;
    }

    public int getGameId() {
        return gameId;
    }

    public int getPeriod() {
        return period;
    }
}
