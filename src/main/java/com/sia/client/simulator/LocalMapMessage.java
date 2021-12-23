package com.sia.client.simulator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.Map;

public class LocalMapMessage extends LocalMessage implements MapMessage {

    private final Map<String,String> content;
    public LocalMapMessage(Map<String,String> content,LocalDateTime messageTime) {
        super(messageTime);
        this.content = content;
    }
    @Override
    public String toString() {
        return String.valueOf(content);
    }
    @Override
    public boolean getBoolean(final String s) throws JMSException {
        return Boolean.parseBoolean(content.get(s));
    }

    @Override
    public byte getByte(final String s) throws JMSException {
        return 0;
    }

    @Override
    public short getShort(final String s) throws JMSException {
        String value = content.get(s);
        if ( null == value) {
            throw new JMSException("Can't parse null value");
        } else {
            return Short.parseShort(value);
        }
    }

    @Override
    public char getChar(final String s) throws JMSException {
        return 0;
    }

    @Override
    public int getInt(final String s) throws JMSException {
        String value = content.get(s);
        if ( null == value) {
            throw new JMSException("Can't parse null value");
        } else {
            return Integer.parseInt(value);
        }
    }

    @Override
    public long getLong(final String s) throws JMSException {
        String value = content.get(s);
        if ( null == value) {
            return 0L;
        } else {
            return Long.parseLong(value);
        }
    }

    @Override
    public float getFloat(final String s) throws JMSException {
        String value = content.get(s);
        if ( null == value) {
            throw new JMSException("Can't parse null value");
        } else {
            return Float.parseFloat(value);
        }
    }

    @Override
    public double getDouble(final String s) {
        String value = content.get(s);
        if ( null == value) {
            return 0;
        } else {
            return Double.parseDouble(value);
        }
    }

    @Override
    public String getString(final String s) throws JMSException {
        return content.get(s);
    }

    @Override
    public byte[] getBytes(final String s) throws JMSException {
        return new byte[0];
    }

    @Override
    public Object getObject(final String s) throws JMSException {
        return content.get(s);
    }

    @Override
    public Enumeration getMapNames() throws JMSException {
        return null;
    }

    @Override
    public void setBoolean(final String s, final boolean b) throws JMSException {

    }

    @Override
    public void setByte(final String s, final byte b) throws JMSException {

    }

    @Override
    public void setShort(final String s, final short i) throws JMSException {

    }

    @Override
    public void setChar(final String s, final char c) throws JMSException {

    }

    @Override
    public void setInt(final String s, final int i) throws JMSException {

    }

    @Override
    public void setLong(final String s, final long l) throws JMSException {

    }

    @Override
    public void setFloat(final String s, final float v) throws JMSException {

    }

    @Override
    public void setDouble(final String s, final double v) throws JMSException {

    }

    @Override
    public void setString(final String s, final String s1) throws JMSException {

    }

    @Override
    public void setBytes(final String s, final byte[] bytes) throws JMSException {

    }

    @Override
    public void setBytes(final String s, final byte[] bytes, final int i, final int i1) throws JMSException {

    }

    @Override
    public void setObject(final String s, final Object o) throws JMSException {

    }

    @Override
    public boolean itemExists(final String s) throws JMSException {
        return false;
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
        return System.currentTimeMillis();
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
        return content.containsKey(s);
    }

    @Override
    public boolean getBooleanProperty(final String s) throws JMSException {
        return getBoolean(s);
    }

    @Override
    public byte getByteProperty(final String s) throws JMSException {
        return 0;
    }

    @Override
    public short getShortProperty(final String s) throws JMSException {
        return getShort(s);
    }

    @Override
    public int getIntProperty(final String s) throws JMSException {
        return getInt(s);
    }

    @Override
    public long getLongProperty(final String s) throws JMSException {
        return getLong(s);
    }

    @Override
    public float getFloatProperty(final String s) throws JMSException {
        return getFloat(s);
    }

    @Override
    public double getDoubleProperty(final String s) throws JMSException {
        return getDouble(s);
    }

    @Override
    public String getStringProperty(final String s) throws JMSException {
        return getString(s);
    }

    @Override
    public Object getObjectProperty(final String s) throws JMSException {
        return getObject(s);
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
