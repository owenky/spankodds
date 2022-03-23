package com.sia.client.ui.sbt;

import com.sia.client.config.Utils;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * JComponent的值发生变化时要触发的事件,ValueChangedListener用到
 *
 * @author XFZ@2008-03-15
 */
public class ValueChangedEvent extends AWTEvent {
    /**
     * source必须是值要变化的那个JComponent
     */
    public ValueChangedEvent(Object source_) {
        super(source_, ActionEvent.ACTION_PERFORMED);
    }

    /**
     * oldvalueString是用在isValueChanged()中的，外部用getValue()
     */
    public String getOldValueString() {
        return oldvalueString;
    }

    /**
     * 上次触发该事件的对象的字符串值，主要是用来判断与上次相比，值是否变了。判断值是否发生变化，不能用Object,
     * 因为同一个Object,可能值（内容）都会发生变化，只能用字符串记录两次是否发生变化 -- XFZ@2010-11-11
     *
     * @return
     */
    public void setOldValueString(String oldvalueString_) {
        this.oldvalueString = oldvalueString_;
    }

    /**
     * newvalueString是用在isValueChanged()中的，外部用getValue()
     */

    public String getNewValueString() {
        return newvalueString;
    }

    /**
     * 触发该事件的对象的字符串值，主要是用来判断与上次相比，值是否变了。判断值是否发生变化，不能用Object,
     * 因为同一个Object,可能值（内容）都会发生变化，只能用字符串记录两次是否发生变化 -- XFZ@2010-11-11
     *
     * @return
     */
    public void setNewValueString(String newvalueString_) {
        this.newvalueString = newvalueString_;
    }

    /**
     * 触发该事件的对象 -- XFZ@2010-11-11
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
            property_name = property_name.replaceAll("[\\s:：]", "");
        }
        this.property_name = property_name;
    }

    /**
     * 从sourceEvent拷贝主要的属性
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
     * 判断Event的源(source)是否是表格的编辑器，这个变量在DyDataContainer$processValueChangedEvent()中用到 -- XFZ@2010-01-31
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
    private String oldvalueString, newvalueString; //主要是用来判断值是否变了，不能用value来判断，必须用字符串来判断，见isValueChanged() -- XFZ@2010-11-11
    private Object value; //新的值；
    private String property_name; //值发生变化的那个属性的名称
    private static final long serialVersionUID = 20080315;
}
