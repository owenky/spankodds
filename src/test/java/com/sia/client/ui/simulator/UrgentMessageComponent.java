package com.sia.client.ui.simulator;

import com.sia.client.model.AlertStruct;
import com.sia.client.ui.UrgentMesgHistComp;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicInteger;

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
        toolBar.add(jcomp);
//        histBox.addItem(new AlertStruct("20:19","<html>this is line 1this is line this is line this is line this is line this is line this is line this is line this is line this is line <br>line2<br>line3<br>line4</html>"));
//        histBox.addItem(new AlertStruct("21:19","<html>Row2<br>RowLine2</html>"));
        AtomicInteger counter = new AtomicInteger(0);
        Timer timer = new Timer(3000, (e)-> {

            if ( counter.addAndGet(1) <  10) {
                histBox.addItem(new AlertStruct("21:19",counter.get()+":This is test Row test Row Test ROW"));
            }
        });
        timer.start();
    }
}
