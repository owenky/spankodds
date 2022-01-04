package com.sia.client.ui;

import com.sia.client.config.SiaConst.UIProperties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewWindowAction implements ActionListener {


    public void actionPerformed(ActionEvent e) {

        SpankyWindow frame = SpankyWindow.create();
        frame.populateTabPane();
        AppController.addFrame(frame);
        //frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        SpankyWindow.setLocationaAndSize(frame, UIProperties.screenXmargin+50,UIProperties.screenYmargin+50);
    }


}