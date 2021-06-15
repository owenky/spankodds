package com.sia.client.ui.simulator;

import com.sia.client.model.AlertStruct;
import com.sia.client.ui.UrgentMesgHistComp;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class UrgentMessageComponent extends JPanel {

    private final Dimension dim = new Dimension(0,50);
    public UrgentMessageComponent() {
        init();
    }
    private void init() {
        setLayout(new BorderLayout());
        JPanel toolBar = new JPanel();
        toolBar.setPreferredSize(dim);
        this.add(toolBar,BorderLayout.NORTH);
        UrgentMesgHistComp histBox = new UrgentMesgHistComp();
        JComponent jcomp = histBox.getComponent();
//        jcomp.setPreferredSize(new Dimension(200,50));
        toolBar.add(jcomp);
//        jcomp.setPreferredSize(dim);
//        jcomp.setMaximumSize(dim);
        histBox.addItem(new AlertStruct("20:19","<html>this is line 1this is line this is line this is line this is line this is line this is line this is line this is line this is line <br>line2<br>line3<br>line4</html>"));
        histBox.addItem(new AlertStruct("21:19","<html>Row2<br>RowLine2</html>"));
    }
}
