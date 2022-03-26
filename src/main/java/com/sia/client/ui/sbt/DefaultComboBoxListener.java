package com.sia.client.ui.sbt;

import com.sia.client.config.UiFunc;
import com.sia.client.config.Utils;
import com.sia.client.model.SelectionItem;

import javax.swing.*;
import java.awt.event.*;

public class DefaultComboBoxListener<K,T extends SelectionItem<K>> implements FocusListener, MouseListener, ItemListener, KeyListener {

    public DefaultComboBoxListener(final SBTComboBox<K,T> comboBox_) {
        comboBox = comboBox_;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (UiFunc.isEnterKey(e)) {

            Object highlightedValue = comboBox.getHighLightValue(); // 被highlight但尚未被选中的项目--XFZ@2010-12-17
            Object selectedItemValue = comboBox.getSelectedItem();
            if (highlightedValue != null) {
                e.consume();
                comboBox.setPopupVisible(false);
                comboBox.setSelectedItem(highlightedValue);
            } else if (selectedItemValue != null) {
                e.consume();
                comboBox.setPopupVisible(false);
                comboBox.setSelectedItem(selectedItemValue);
            } else {
                if (!comboBox.isNewItemAllowed()) {
                    String comboBoxName = comboBox.getName();
                    if (Utils.isBlank(comboBoxName)) {
                        comboBoxName = "";
                    }

                    UiFunc.showDialogAndWait("Invalid" + comboBoxName
                            + "selection" + comboBox.getEditor().getItem()
                            + ". Please try again.");
                    comboBox.setSelectedItem(comboBox.getBlankItem());
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!UiFunc.isArrowKey(e) && !UiFunc.isEnterKey(e)) {

            // see original comments -- 03/20/2022
            boolean editable_ = comboBox.isEditable();
            if (!editable_) {
                invokeKeyEvent4Uneditable(e);
            } else {
                invokeInputEvent4Editable(e);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public final void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.DESELECTED) { // 选项目被除掉时,不要继续,否则下面回被执行两次
            return;
        }
        SBTComboBox<?,?> box = (SBTComboBox<?,?>) e.getSource();
        boolean rtn = itemStateChanged2(e);
        if (rtn) {
            box.fireValueChangedEvent();
        }

    }
    protected boolean itemStateChanged2(ItemEvent e) {

        return true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (comboBox.isEnabled()) {
            invokeInputEvent4Editable(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void focusGained(FocusEvent e) {
        if (comboBox.isEnabled()) {
            setPopupVisible(e, true);
        }

    }

    @Override
    public void focusLost(FocusEvent e) {
        // see original comments -- 03/20/2022
    }

    private void setPopupVisible(FocusEvent e, boolean status) {
        // see original comments -- 03/20/2022
        ComboBoxEditor editor = comboBox.getEditor();
        if (editor != null && e.getSource() == editor.getEditorComponent()) {
            if (comboBox.isDisplayable()) {
                comboBox.setPopupVisible(status);
            }
        }
    }

    private void invokeKeyEvent4Uneditable(KeyEvent e) {
        //implementation moved to GameIdComboKeyManager -- 03/26/2022
//
//        int totalCount = comboBox.getItemCount();
//        if (totalCount == 0) {
//            return;
//        }
//
//        char c = e.getKeyChar();
//        int currentSelect = comboBox.getSelectedIndex();
//
//        if (currentSelect < 0 || currentSelect >= totalCount) {
//            currentSelect = 0;
//        }
//        boolean find = false;
//        int target = (currentSelect + 1) % totalCount;
//        while (target != currentSelect) {
//            T item = comboBox.getItemAt(target);
//            if (item == null) {
//                target = (target + 1) % totalCount;
//                continue;
//            }
//            if (this.getKeyStrokeSelectionItemMatcher().match(item, c)) {
//                find = true;
//                break;
//            }
//            target = (target + 1) % totalCount;
//        }
//        if (find) {
//            comboBox.setSelectedIndex(target);
//        }
    }

    private void invokeInputEvent4Editable(InputEvent e) {
        JTextField jtf = ((JTextField) comboBox.getEditor().getEditorComponent());
        String str = jtf.getText();

        if (e instanceof KeyEvent) {
            comboBox.selectWithKeyChar(((KeyEvent) e).getKeyChar());
        } else if (e instanceof MouseEvent) {
            if (Utils.isBlank(str)) {
                comboBox.resetRange(comboBox.getBlankItem());
            }
        }
    }

    public void setKeyStrokeSelectionItemMatcher(KeyStrokeSelectionItemMatcher keyStrokeSelectionItemMatcher) {
        this.keyStrokeSelectionItemMatcher = keyStrokeSelectionItemMatcher;
    }

    private KeyStrokeSelectionItemMatcher getKeyStrokeSelectionItemMatcher() {
        if (null == keyStrokeSelectionItemMatcher) {
            keyStrokeSelectionItemMatcher = new DefaultKeyStrokeSelectionItemMatcher();
        }
        return keyStrokeSelectionItemMatcher;
    }

    private KeyStrokeSelectionItemMatcher keyStrokeSelectionItemMatcher;
    private final SBTComboBox<K,T> comboBox;
}