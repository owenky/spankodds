package com.sia.client.ui.sbt;

import com.sia.client.config.Utils;
import com.sia.client.model.SelectionItem;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;
import java.lang.reflect.Method;

// see original comments -- 03/20/2022
public class ComboBoxInputLineEditor extends BasicComboBoxEditor {
	
	private SelectionItem oldValue; // see original comments -- 03/20/2022
	public ComboBoxInputLineEditor() {
		init();
	}
	public void setLength(int length){
		Component theEditField = getEditorComponent();
		int height_ = theEditField.getHeight();
		theEditField.setPreferredSize(new Dimension(length,height_));
	}
	public void setEditable(boolean b){
		JTextField theEditField = (JTextField)getEditorComponent();
		theEditField.setEditable(b);
	}
	public boolean isEditable(){
		return ((JTextField)this.getEditorComponent()).isEditable();
	}
	private void init() {
	}

	// see original comments -- 03/20/2022
	@Override
    public void setItem(Object anObject) {
       super.setItem(anObject);
       
       String text_;
       if ( anObject instanceof SelectionItem){
    	   oldValue = (SelectionItem)anObject;
       }
	   //commented out on 03/20/2022
//	   else {
//    	   String str_ = String.valueOf(anObject);
//    	   oldValue = new SBTSelectionItem(str_,str_);
//       }

        if ( null != oldValue) {
            text_ = oldValue.getDisplay();
        } else {
            text_ = "";
        }
       
       if ( text_ == null){
    	   text_ = "";
       }

		// see original comments -- 03/20/2022
       if (! text_.equals(editor.getText())) {
           editor.setText(text_);
       }
    }
	// see original comments -- 03/20/2022
	@Override
	public Object getItem() {
        Object newValue = editor.getText();

          if (oldValue != null )  {
              if (newValue.equals(oldValue.getDisplay()))  {
                return oldValue;
              } else {
                // Must take the value from the editor and get the value and cast it to the new type.
                Class<?> cls = oldValue.getClass();
                try {
                    Method method = cls.getMethod("valueOf", new Class[]{String.class});
                    newValue = method.invoke(oldValue, new Object[] { editor.getText()});
                } catch (Exception ex) {
                   Utils.log(ex);
                }
            }
        }
        return newValue;
    }
	// see original comments -- 03/20/2022
	@Override
    protected JTextField createEditorComponent() {
		final SBTComboBoxInputField theEditField = new SBTComboBoxInputField();
    	theEditField.setEditable(true);
    	theEditField.setHorizontalAlignment(SwingConstants.CENTER); 	
        return theEditField;
    }

}