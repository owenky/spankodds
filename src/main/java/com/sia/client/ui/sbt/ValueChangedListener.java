package com.sia.client.ui.sbt;

import java.util.EventListener;


public interface ValueChangedListener extends EventListener {
    void processValueChangedEvent(ValueChangedEvent e_);
}
