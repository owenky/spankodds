package com.sia.client.ui;

import javax.swing.JComponent;
import java.awt.Dimension;

public interface LayerAnchored {
    AnchoredLayeredPane getAnchoredLayeredPane();
    default JComponent getUserComponent() {
        return (JComponent)this;
    }
    default void openAndCenter(Dimension totalSize,boolean toHideOnMouseOut) {
        getAnchoredLayeredPane().openAndCenter(getUserComponent(),totalSize,toHideOnMouseOut);
    }
}
