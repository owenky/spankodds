package com.sia.client.ui;

import com.sia.client.ui.control.SportsTabPane;

import javax.swing.JComponent;
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public abstract class AbstractLayeredDialog {

    private final AnchoredLayeredPane anchoredLayeredPane;

    abstract protected JComponent getUserComponent();

    public AbstractLayeredDialog(SportsTabPane stp,String title,int layer_index) {
        anchoredLayeredPane = new AnchoredLayeredPane(stp,layer_index);
        anchoredLayeredPane.setTitle(title);
    }
    public AbstractLayeredDialog(SportsTabPane stp,String title) {
        anchoredLayeredPane = new AnchoredLayeredPane(stp);
        anchoredLayeredPane.setTitle(title);
    }
    public void setTitlePanelLeftComp(JComponent titlePanelLeftComp) {
        anchoredLayeredPane.setTitlePanelLeftComp(titlePanelLeftComp);
    }
    public void setTitlePanelRightComp(JComponent titlePaneRightComp) {
        anchoredLayeredPane.setTitlePanelRightComp(titlePaneRightComp);
    }
    public void show(Dimension dim) {
        anchoredLayeredPane.openAndCenter(getUserComponent(),dim,false);
    }
    public void show(Dimension dim, Supplier<Point> anchorLocSupplier) {
        anchoredLayeredPane.openAndAnchoredAt(getUserComponent(),dim,false,anchorLocSupplier);
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
    public synchronized void addCloseAction(Runnable r) {
        anchoredLayeredPane.addCloseAction(r);
    }
}
