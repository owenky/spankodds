package com.sia.client.simulator;

import com.sia.client.model.SportType;
import com.sia.client.ui.control.MainScreen;
import com.sia.client.ui.control.SportsTabPane;

import java.util.concurrent.atomic.AtomicInteger;

import static com.sia.client.config.Utils.log;

public class MainScreenRefresh extends TestExecutor{

    private final MainScreen mainScreen;
    private static final AtomicInteger counter = new AtomicInteger(0);
    public MainScreenRefresh(MainScreen mainScreen) {
        super(5,15);
        this.mainScreen = mainScreen;
        setValid(mainScreen.getName().equals(SportType.Soccer.getSportName()));
    }
    @Override
    public void run() {

        try {
            if(0==counter.getAndAdd(1)%5) {
                log("***** MainScreen Refreshing count=" + counter.get());
            }
            SportsTabPane.refreshMainScreen(mainScreen);
        } catch(Exception e) {
            log(e);
        }
    }
}
