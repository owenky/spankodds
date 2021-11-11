package com.sia.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewWindowAction implements ActionListener {


    public void actionPerformed(ActionEvent e) {

        SpankyWindow frame = SpankyWindow.create();
        frame.populateTabPane();
        AppController.addFrame(frame);
        //frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


}