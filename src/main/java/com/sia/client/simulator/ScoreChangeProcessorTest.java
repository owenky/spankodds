package com.sia.client.simulator;

import com.sia.client.config.GameUtils;
import com.sia.client.model.Game;
import com.sia.client.model.GameStatus;
import com.sia.client.model.Games;
import com.sia.client.model.SportType;
import com.sia.client.ui.AppController;
import com.sia.client.ui.control.MainScreen;
import com.sia.client.ui.ScoreChangedProcessor;
import com.sia.client.ui.ScoresConsumer;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.log;

public class ScoreChangeProcessorTest extends TestExecutor {

    private final ScoresConsumer scoreConsumer;

    public ScoreChangeProcessorTest(ScoresConsumer scoreConsumer) {
        super(60, 600000000);
        this.scoreConsumer = scoreConsumer;
    }

    public static void main(String[] argv) throws ParseException {

    }

    @Override
    public void run() {

        try {
            int count=0;
            log("START ScoreChangeProcessorTest++++++++++++++++++++++++++++++++++++++++++++++++++++");
            MainScreen testScreen = AppController.getTabPanes().get(0).findMainScreen(SportType.Soccer.getSportName());
            Games games = AppController.getGames();
            List<Game> allGames = new ArrayList<>();
            games.iterator().forEachRemaining(allGames::add);
            List<Game> allSoccerGames = allGames.stream().filter(game->9==game.getLeague_id()).collect(Collectors.toList());
            List<Game> soccerGames = allSoccerGames.stream().filter(testScreen::shouldAddToScreen)
                    .filter(game-> ! GameStatus.Final.isSame(game.getStatus())).collect(Collectors.toList());
            for(Game game:soccerGames) {
                if ( testScreen.shouldAddToScreen(game)) {
log("moving game "+game.getVisitorteam()+"/"+game.getHometeam()+" from "+ GameUtils.getGameGroupHeader(game)+" to "+GameStatus.InProgress.getGroupHeader());
                    ScoreChangedProcessor.process(GameStatus.InProgress, game,0, 0);
                }
                if ( count++ > 1000) {
                    break;
                }
            }
            log("END OF START ScoreChangeProcessorTest-----------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class TestTextMessage implements TextMessage {

        private final Map<String, String> strProp = new HashMap<>();
        private String text;

        @Override
        public String getJMSMessageID() throws JMSException {
            return null;
        }        @Override
        public void setText(final String s) throws JMSException {
            text = s;
        }

        @Override
        public void setJMSMessageID(final String s) throws JMSException {

        }        @Override
        public String getText() throws JMSException {
            return text;
        }

        @Override
        public long getJMSTimestamp() throws JMSException {
            return 0;
        }

        @Override
        public void setJMSTimestamp(final long l) throws JMSException {

        }

        @Override
        public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
            return new byte[0];
        }

        @Override
        public void setJMSCorrelationIDAsBytes(final byte[] bytes) throws JMSException {

        }





        @Override
        public void setJMSCorrelationID(final String s) throws JMSException {

        }

        @Override
        public String getJMSCorrelationID() throws JMSException {
            return null;
        }

        @Override
        public Destination getJMSReplyTo() throws JMSException {
            return null;
        }

        @Override
        public void setJMSReplyTo(final Destination destination) throws JMSException {

        }

        @Override
        public Destination getJMSDestination() throws JMSException {
            return null;
        }

        @Override
        public void setJMSDestination(final Destination destination) throws JMSException {

        }

        @Override
        public int getJMSDeliveryMode() throws JMSException {
            return 0;
        }

        @Override
        public void setJMSDeliveryMode(final int i) throws JMSException {

        }

        @Override
        public boolean getJMSRedelivered() throws JMSException {
            return false;
        }

        @Override
        public void setJMSRedelivered(final boolean b) throws JMSException {

        }

        @Override
        public String getJMSType() throws JMSException {
            return null;
        }

        @Override
        public void setJMSType(final String s) throws JMSException {

        }

        @Override
        public long getJMSExpiration() throws JMSException {
            return 0;
        }

        @Override
        public void setJMSExpiration(final long l) throws JMSException {

        }

        @Override
        public int getJMSPriority() throws JMSException {
            return 0;
        }

        @Override
        public void setJMSPriority(final int i) throws JMSException {

        }

        @Override
        public void clearProperties() throws JMSException {

        }

        @Override
        public boolean propertyExists(final String s) throws JMSException {
            return false;
        }

        @Override
        public boolean getBooleanProperty(final String s) throws JMSException {
            return false;
        }

        @Override
        public byte getByteProperty(final String s) throws JMSException {
            return 0;
        }

        @Override
        public short getShortProperty(final String s) throws JMSException {
            return 0;
        }

        @Override
        public int getIntProperty(final String s) throws JMSException {
            return 0;
        }

        @Override
        public long getLongProperty(final String s) throws JMSException {
            return 0;
        }

        @Override
        public float getFloatProperty(final String s) throws JMSException {
            return 0;
        }

        @Override
        public double getDoubleProperty(final String s) throws JMSException {
            return 0;
        }

        @Override
        public String getStringProperty(final String s) throws JMSException {
            return strProp.get(s);
        }

        @Override
        public Object getObjectProperty(final String s) throws JMSException {
            return null;
        }

        @Override
        public Enumeration getPropertyNames() throws JMSException {
            return null;
        }

        @Override
        public void setBooleanProperty(final String s, final boolean b) throws JMSException {

        }

        @Override
        public void setByteProperty(final String s, final byte b) throws JMSException {

        }

        @Override
        public void setShortProperty(final String s, final short i) throws JMSException {

        }

        @Override
        public void setIntProperty(final String s, final int i) throws JMSException {

        }

        @Override
        public void setLongProperty(final String s, final long l) throws JMSException {

        }

        @Override
        public void setFloatProperty(final String s, final float v) throws JMSException {

        }

        @Override
        public void setDoubleProperty(final String s, final double v) throws JMSException {

        }

        @Override
        public void setStringProperty(final String s, final String s1) throws JMSException {
            strProp.put(s, s1);
        }

        @Override
        public void setObjectProperty(final String s, final Object o) throws JMSException {

        }

        @Override
        public void acknowledge() throws JMSException {

        }

        @Override
        public void clearBody() throws JMSException {

        }
    }
}
