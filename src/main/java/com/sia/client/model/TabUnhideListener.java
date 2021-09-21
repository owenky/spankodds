package com.sia.client.model;

import com.sia.client.config.Utils;
import com.sia.client.ui.AppController;
import com.sia.client.ui.MainScreen;
import com.sia.client.ui.SportsTabPane;

import javax.swing.ImageIcon;
import java.util.Vector;

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
        Vector currentvec = AppController.getMainTabVec();
        int idx = currentvec.indexOf(stName);
        for (Object o : AppController.getTabPanes()) {
            SportsTabPane tp1 = (SportsTabPane) o;
            tp1.insertTab(stName, new ImageIcon(Utils.getMediaResource(sportType.getIcon())), new MainScreen(sportType), stName, idx);
            tp1.setSelectedIndex(tp1.indexOfTab(stName));

        }
    }
}
