package com.sia.client.simulator;

import com.sia.client.ui.control.MainScreen;
import com.sia.client.ui.control.SportsTabPane;

import java.util.concurrent.atomic.AtomicInteger;

import static com.sia.client.config.Utils.log;

public class MainScreenRefresh extends TestExecutor{

    private final SportsTabPane sportTablePane;
    private static final AtomicInteger counter = new AtomicInteger(0);
    public MainScreenRefresh(SportsTabPane sportTablePane) {
        super(15,20);
        this.sportTablePane = sportTablePane;
//        setValid(mainScreen.getName().equals(SportType.Soccer.getSportName()));
        setValid(true);
    }
    @Override
    public void run() {

        try {
            MainScreen mainScreen = (MainScreen)sportTablePane.getSelectedComponent();
            if(0==counter.getAndAdd(1)%5) {
                log("***** Refreshing MainScreen "+mainScreen.getSportType().getSportName()+", count=" + counter.get());
            }
            sportTablePane.refreshMainScreen(mainScreen);
        } catch(Exception e) {
            log(e);
        }
    }
}
