package com.sia.client.ui.simulator;

import com.jidesoft.combobox.TableComboBox;
import com.sia.client.model.AlertStruct;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class UrgentMessageComponent extends JPanel {

    public UrgentMessageComponent() {
        init();
    }
    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        TableComboBox histBox = new TableComboBox();
        this.add(histBox,BorderLayout.NORTH);
        histBox.setPreferredSize(new Dimension(60,30));
        histBox.setMaximumSize(new Dimension(60,30));
        histBox.addItem(new AlertStruct("20:19","<html>this is line 1this is line this is line this is line this is line this is line this is line this is line this is line this is line <br>line2<br>line3</html>"));
    }
}
