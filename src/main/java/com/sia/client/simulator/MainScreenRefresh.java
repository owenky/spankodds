package com.sia.client.simulator;

import com.sia.client.model.SportType;
import com.sia.client.ui.MainScreen;
import com.sia.client.ui.SportsTabPane;

import static com.sia.client.config.Utils.log;

public class MainScreenRefresh extends TestExecutor{

    private final MainScreen mainScreen;
    public MainScreenRefresh(MainScreen mainScreen) {
        super(5,15);
        this.mainScreen = mainScreen;
        setValid(mainScreen.getName().equals(SportType.Soccer.getSportName()));
    }
    @Override
    public void run() {

        try {
            SportsTabPane.refreshMainScreen(mainScreen);
        } catch(Exception e) {
            log(e);
        }
    }
}
