package com.sia.client.ui;

import com.sia.client.ui.control.SportsTabPane;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.util.concurrent.Callable;

public abstract class AbstractLayeredDialog {

    private final AnchoredLayeredPane anchoredLayeredPane;

    abstract protected JComponent getUserComponent();

    public AbstractLayeredDialog(SportsTabPane stp,String title) {
        anchoredLayeredPane = new AnchoredLayeredPane(stp);
        anchoredLayeredPane.setTitle(title);
    }
    public void setTitlePanelLeftComp(JComponent titlePanelLeftComp) {
        anchoredLayeredPane.setTitlePanelLeftComp(titlePanelLeftComp);
    }
    public void show(Dimension dim) {
        anchoredLayeredPane.openAndCenter(getUserComponent(),dim,false);
    }
    public AnchoredLayeredPane getAnchoredLayeredPane() {
        return anchoredLayeredPane;
    }
    public void close() {
        this.anchoredLayeredPane.close();
    }
    public AbstractLayeredDialog withCloseValidor(Callable<Boolean> closeValidor) {
        anchoredLayeredPane.withCloseValidor(closeValidor);
        return this;
    }
}
