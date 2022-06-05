package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;
import com.sia.client.ui.control.SportsTabPane;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;

public class GameHistPane extends AbstractLayeredDialog {

    private static final Dimension paneSize = new Dimension(800,500);
    private static final String host = "sof300732.com:9998";
    private final Game game;
    private final int bookieId;
    private int period = 1;
    private int lineType = 1;
    public static void showHistPane(SportsTabPane stp,Point pointOnScreen, Game game,int bookieId) {

        GameHistPane gameHistPane = new GameHistPane(stp,game,bookieId);
        gameHistPane.show(paneSize,false,()->pointOnScreen);
    }
    public GameHistPane(SportsTabPane stp, Game game,int bookieId) {
        super(stp,game.getVisitorteam()+"/"+game.getHometeam(), SiaConst.LayedPaneIndex.SportConfigIndex);
        this.game = game;
        this.bookieId = bookieId;
    }

    @Override
    protected JComponent getUserComponent() {
        JFXPanel jFxPanel = new JFXPanel();
        Platform.runLater(() -> createGameHistScene(jFxPanel));
        return jFxPanel;
    }
    private void createGameHistScene(JFXPanel jFxPanel) {
        String url="http://"+host+"/gamedetails/linehistory.jsp?bookieID="+bookieId+"&gameNum="+game.getGame_id()+"&period="+period+"&lineType="+lineType;
        Group root  =  new  Group();
        Scene scene  =  new  Scene(root);
        WebView webView = new WebView();

        webView.getEngine().load(url);
        root.getChildren().add(webView);
        jFxPanel.setScene(scene);
    }
}
