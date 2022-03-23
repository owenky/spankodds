package com.sia.client.ui.sbt;

import com.sia.client.config.Utils;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * JComponent��ֵ�����仯ʱҪ�������¼�,ValueChangedListener�õ�
 *
 * @author XFZ@2008-03-15
 */
public class ValueChangedEvent extends AWTEvent {
    /**
     * source������ֵҪ�仯���Ǹ�JComponent
     */
    public ValueChangedEvent(Object source_) {
        super(source_, ActionEvent.ACTION_PERFORMED);
    }

    /**
     * oldvalueString������isValueChanged()�еģ��ⲿ��getValue()
     */
    public String getOldValueString() {
        return oldvalueString;
    }

    /**
     * �ϴδ������¼��Ķ�����ַ���ֵ����Ҫ�������ж����ϴ���ȣ�ֵ�Ƿ���ˡ��ж�ֵ�Ƿ����仯��������Object,
     * ��Ϊͬһ��Object,����ֵ�����ݣ����ᷢ���仯��ֻ�����ַ�����¼�����Ƿ����仯 -- XFZ@2010-11-11
     *
     * @return
     */
    public void setOldValueString(String oldvalueString_) {
        this.oldvalueString = oldvalueString_;
    }

    /**
     * newvalueString������isValueChanged()�еģ��ⲿ��getValue()
     */

    public String getNewValueString() {
        return newvalueString;
    }

    /**
     * �������¼��Ķ�����ַ���ֵ����Ҫ�������ж����ϴ���ȣ�ֵ�Ƿ���ˡ��ж�ֵ�Ƿ����仯��������Object,
     * ��Ϊͬһ��Object,����ֵ�����ݣ����ᷢ���仯��ֻ�����ַ�����¼�����Ƿ����仯 -- XFZ@2010-11-11
     *
     * @return
     */
    public void setNewValueString(String newvalueString_) {
        this.newvalueString = newvalueString_;
    }

    /**
     * �������¼��Ķ��� -- XFZ@2010-11-11
     *
     * @return
     */
    public Object getValue() {
        return value;
    }

    public void setValue(Object v) {
        value = v;
    }

    public boolean isValueChanged() {
        if (newvalueString == null) {
            if (oldvalueString == null) {
                return false;
            } else {
                return true;
            }
        }


        return !(newvalueString.equalsIgnoreCase(oldvalueString));

    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        if (!Utils.isBlank(property_name)) {
            property_name = property_name.replaceAll("[\\s:��]", "");
        }
        this.property_name = property_name;
    }

    /**
     * ��sourceEvent������Ҫ������
     *
     * @param sourceEvent
     */
    public void copyValues(ValueChangedEvent sourceEvent) {
        Object s_ = sourceEvent.getSource();
        String property_name_;
        if (s_ instanceof Component)
            property_name_ = ((Component) s_).getName();
        else
            property_name_ = sourceEvent.getProperty_name(); //s_.toString();

        this.setNewValueString(sourceEvent.newvalueString);
        this.setOldValueString(sourceEvent.oldvalueString);
        this.setValue(sourceEvent.value);
        this.setSourceTableCellEditor(sourceEvent.isSourceTableCellEditor);
        this.setProperty_name(property_name_);
    }

    /**
     * �ж�Event��Դ(source)�Ƿ��Ǳ��ı༭�������������DyDataContainer$processValueChangedEvent()���õ� -- XFZ@2010-01-31
     *
     * @return
     */
    public boolean isSourceTableCellEditor() {
        return isSourceTableCellEditor;
    }

    public void setSourceTableCellEditor(boolean isSourceTableCellEditor) {
        this.isSourceTableCellEditor = isSourceTableCellEditor;
    }

    private boolean isSourceTableCellEditor = false;
    private String oldvalueString, newvalueString; //��Ҫ�������ж�ֵ�Ƿ���ˣ�������value���жϣ��������ַ������жϣ���isValueChanged() -- XFZ@2010-11-11
    private Object value; //�µ�ֵ��
    private String property_name; //ֵ�����仯���Ǹ����Ե�����
    private static final long serialVersionUID = 20080315;
}
