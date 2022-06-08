package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.SpankyWindowConfig;
import com.sia.client.ui.control.SportsTabPane;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class GameHistPane extends AbstractLayeredDialog {

    private static final DateTimeFormatter gameDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Dimension paneSize = new Dimension(800,500);
    private static final String host = "sof300732.com:9998";
    private final Game game;
    private final int bookieId;
    private String lineType;
    private String gameDateStr;
    private int period;
    private WebView webView;
    private JInternalFrame jInteralFrame;

    static {
        Platform.setImplicitExit(false);
    }
    public static void showHistPane(SportsTabPane stp,Point pointOnScreen, Game game,int bookieId) {

        GameHistPane gameHistPane = new GameHistPane(stp,game,bookieId);
        gameHistPane.setIsSinglePaneMode(false);
        gameHistPane.show(paneSize,()->pointOnScreen);
        gameHistPane.jInteralFrame.toFront();
    }
    public GameHistPane(SportsTabPane stp, Game game,int bookieId) {
        super(stp,null,SiaConst.LayedPaneIndex.SportDetailPaneIndex);
        this.game = game;
        this.bookieId = bookieId;
        DisplayTransformer displayTransformer = new DisplayTransformer(game.getGame_id());
        SpankyWindowConfig spankyWindowConfig = stp.getSpankyWindowConfig();
        lineType = displayTransformer.transformDefault(spankyWindowConfig.getDisplay());
        period = displayTransformer.transformPeriod(spankyWindowConfig.getPeriod());
        gameDateStr = gameDateFormatter.format(game.getGamedate());
    }
    @Override
    protected JComponent getUserComponent() {
        //        String title = game.getVisitorteam()+"/"+game.getHometeam()+"    View Type: "+lineType;
        String title = game.getVisitorteam()+"/"+game.getHometeam();
        jInteralFrame = new JInternalFrame(title,true,true,true,true);
        jInteralFrame.setLayout(new BorderLayout());
        JFXPanel jFxPanel = new JFXPanel();
        jInteralFrame.add(new JScrollPane(jFxPanel),BorderLayout.CENTER);
        Platform.runLater(() -> createGameHistScene(jFxPanel));
        jInteralFrame.setVisible(true);
        return jInteralFrame;
    }
    private void createGameHistScene(JFXPanel jFxPanel) {
        try {
            String url = "http://" + host + "/gamedetails/linehistorynew.jsp?bookieID=" + bookieId + "&gameNum=" + game.getGame_id() + "&period=" + period + "&lineType=" + lineType + "&strgameDate=" + gameDateStr;
            Group root = new Group();
            Scene scene = new Scene(root);
            webView = new WebView();
            root.getChildren().add(webView);
            jFxPanel.setScene(scene);
            webView.getEngine().load(url);
        }catch(Exception e) {
            Utils.log(e);
        }
    }
}
