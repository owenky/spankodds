package com.sia.client.simulator;

import com.sia.client.ui.GamesConsumer;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.sia.client.config.Utils.log;

public class GameMessageSimulator extends TestExecutor{

    private final GamesConsumer gamesConsumer;
    public GameMessageSimulator(GamesConsumer gamesConsumer) {
        super(10,60);
        this.gamesConsumer = gamesConsumer;
    }
    @Override
    public void run() {
        File testfile = new File("c:\\temp\\gameMesg.txt");
        if ( testfile.exists() && ! testfile.isDirectory()) {
            try {
                Stream<String> lines = Files.lines(testfile.toPath(), Charset.defaultCharset());
                StringBuilder sb = new StringBuilder();
                lines.forEach(sb::append);
                lines.close();
                String text = sb.toString().trim();
                if ( text.length() > 0 &&  ! "#".equals(text.substring(0,1))) {
                    TestTextMessage textMessage = new TestTextMessage();
                    textMessage.setText(sb.toString());
                    textMessage.setStringProperty("messageType", "NEWORUPDATE2");
                    textMessage.setStringProperty("repaint", "false");
                    gamesConsumer.processMessage(textMessage);
                }

            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    }
    public static void main(String [] argv) throws ParseException {
//        GameMessageSimulator testInst = new GameMessageSimulator(null);
//        testInst.start();
//        testInst.start();
        String dateStr = "2021-01-11";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        log(date+", time="+date.getTime());
    }
    private static class TestTextMessage implements TextMessage {

        private String text;
        private final Map<String,String> strProp = new HashMap<>();
        @Override
        public void setText(final String s) throws JMSException {
            text = s;
        }

        @Override
        public String getText() throws JMSException {
            return text;
        }

        @Override
        public String getJMSMessageID() throws JMSException {
            return null;
        }

        @Override
        public void setJMSMessageID(final String s) throws JMSException {

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
            strProp.put(s,s1);
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
