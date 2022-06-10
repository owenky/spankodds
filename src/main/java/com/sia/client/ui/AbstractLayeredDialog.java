package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public abstract class AbstractLayeredDialog {

    private final AnchoredLayeredPane anchoredLayeredPane;

    abstract protected JComponent getUserComponent();

    public AbstractLayeredDialog(SportsTabPane stp,String title,int layer_index) {
       this(stp,layer_index);
        anchoredLayeredPane.setTitle(title);
    }
    public AbstractLayeredDialog(SportsTabPane stp,String title) {
        this(stp, SiaConst.LayedPaneIndex.SportConfigIndex);
        anchoredLayeredPane.setTitle(title);
    }
    public AbstractLayeredDialog(SportsTabPane stp,int layer_index) {
        anchoredLayeredPane = new AnchoredLayeredPane(stp,layer_index);
        anchoredLayeredPane.setCloseValidor(() -> {
            if ( isEdited()) {
                int option = Utils.showOptions(getSportsTabPane(),"Do you want to discard changes?");
                return JOptionPane.YES_OPTION == option;
            } else {
                return true;
            }
        });
    }
    public void setIsFloating(boolean isFloating) {
        anchoredLayeredPane.setIsFloating(isFloating);
    }
    public void setIsSinglePaneMode(boolean isSinglePaneMode) {
        anchoredLayeredPane.setIsSinglePaneMode(isSinglePaneMode);
    }
    public void setTitle(String title){
        anchoredLayeredPane.setTitle(title);
    }
    public SportsTabPane getSportsTabPane() {
        return anchoredLayeredPane.getSportsTabPane();
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
    public void show(Dimension dim, boolean toHideOnMouseOut,Supplier<Point> anchorLocSupplier) {
        anchoredLayeredPane.openAndAnchoredAt(getUserComponent(),dim,toHideOnMouseOut,anchorLocSupplier);
    }
    public AnchoredLayeredPane getAnchoredLayeredPane() {
        return anchoredLayeredPane;
    }
    public void closePanes() {
        this.anchoredLayeredPane.close();
    }
    public void setCloseValidor(Callable<Boolean> closeValidor) {
        anchoredLayeredPane.setCloseValidor(closeValidor);
    }
    public synchronized void addCloseAction(Runnable r) {
        anchoredLayeredPane.addCloseAction(r);
    }
    public boolean isEdited() {
        return false;
    }
}
