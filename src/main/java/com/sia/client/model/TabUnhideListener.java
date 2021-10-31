package com.sia.client.model;

import com.sia.client.config.Utils;
import com.sia.client.ui.AppController;
import com.sia.client.ui.control.MainScreen;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.ImageIcon;
import java.util.List;

public class TabUnhideListener {

    private final SportType sportType;
    private final int index;
    public TabUnhideListener(SportType sportType,int index) {
        this.sportType = sportType;
        this.index = index;
    }
    public void unHide() {
        String stName = sportType.getSportName();
        AppController.SpotsTabPaneVector.put(index, stName);
        List<String> currentvec = AppController.getMainTabVec();
        int idx = currentvec.indexOf(stName);
        for (SportsTabPane tp1 : AppController.getTabPanes()) {
            MainScreen ms = tp1.createMainScreen(sportType);
            tp1.insertTab(stName, new ImageIcon(Utils.getMediaResource(sportType.getIcon())), ms, stName, idx);
            tp1.setSelectedIndex(tp1.indexOfTab(stName));

        }
    }
}
