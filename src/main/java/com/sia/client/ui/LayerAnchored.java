package com.sia.client.ui;

import javax.swing.JComponent;

public interface LayerAnchored {
    AnchoredLayeredPane getAnchoredLayeredPane();
    default JComponent getUserComponent() {
        return (JComponent)this;
    }
    default void openAndCenter(boolean toHideOnMouseOut) {
        getAnchoredLayeredPane().openAndCenter(getUserComponent(),toHideOnMouseOut);
    }
}
