package com.sia.client.ui;

import com.sia.client.ui.comps.ActionOnEditablePanes;
import com.sia.client.ui.control.SportsTabPane;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;

public abstract class AbstractLayerEditablePanes extends AbstractLayeredDialog implements ActionOnEditablePanes {

    public AbstractLayerEditablePanes(SportsTabPane stp, String title, int layer_index,String helpurl) {
        super(stp, title, layer_index,helpurl);
    }

    public AbstractLayerEditablePanes(SportsTabPane stp, String title, int layer_index) {
        super(stp, title, layer_index);
    }

    public AbstractLayerEditablePanes(SportsTabPane stp, String title) {
        super(stp, title);
    }

    public AbstractLayerEditablePanes(SportsTabPane stp, int layer_index) {
        super(stp, layer_index);
    }

    public void save(ActionEvent ae) {
        savePanes();
        close();
    }
    @Override
    public void show(Dimension dim) {
        super.show(dim);
        updatePanes();
    }

    @Override
    public void show(Dimension dim, Supplier<Point> anchorLocSupplier) {
        super.show(dim, anchorLocSupplier);
        updatePanes();
    }

    @Override
    public void show(Dimension dim, boolean toHideOnMouseOut, Supplier<Point> anchorLocSupplier) {
        super.show(dim, toHideOnMouseOut, anchorLocSupplier);
        updatePanes();
    }

    @Override
    public void close() {
        super.close();
        ActionOnEditablePanes.super.clearPanes();
    }

    @Override
    public boolean isAnyPaneEdited() {
        return ActionOnEditablePanes.super.isAnyPaneEdited();
    }
    @Override
    public boolean isEdited() {
        return isAnyPaneEdited();
    }
}
