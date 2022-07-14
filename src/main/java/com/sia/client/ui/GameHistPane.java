package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.SpankyWindowConfig;
import com.sia.client.ui.comps.WebViewPane;
import com.sia.client.ui.control.SportsTabPane;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class GameHistPane {

    private static final DateTimeFormatter gameDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Dimension paneSize = new Dimension(800, 500);
    private static final String host = "sof300732.com:9998";
    private final Game game;
    private final int bookieId;
    private final String title;
    private final String lineType;
    private final String gameDateStr;
    private final int period;
    private boolean embededMode = false;
    private WebViewPane webViewPane;

    static {
        Platform.setImplicitExit(false);
    }

    public static void showHistPane(SportsTabPane stp, Point pointOnScreen, Game game, int bookieId) {

        GameHistPane gameHistPane = new GameHistPane(stp, game, bookieId);
        if (gameHistPane.embededMode) {
            AbstractLayeredDialog embeded = new AbstractLayeredDialog(stp, null, SiaConst.LayedPaneIndex.SportDetailPaneIndex) {
                @Override
                protected JComponent getUserComponent() {
                    return gameHistPane.getUserComponent();
                }
            };
            embeded.setIsSinglePaneMode(false);
            embeded.setIsFloating(true);
            embeded.setTitle(gameHistPane.title);
            embeded.show(paneSize, () -> pointOnScreen);
        } else {
            JFrame jFrame = new JFrame();
            jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    gameHistPane.clear();
                    jFrame.dispose();
                }
            });
            jFrame.setAlwaysOnTop(true);
            jFrame.setTitle(gameHistPane.title);
            jFrame.setPreferredSize(paneSize);
            jFrame.setLayout(new BorderLayout());
            JComponent userComponent = gameHistPane.getUserComponent();
            userComponent.setPreferredSize(paneSize);
            jFrame.add(userComponent, BorderLayout.CENTER);
            jFrame.pack();
            jFrame.setVisible(true);
            Point adjustedLoc = AnchoredLayeredPane.adjustAnchorLocation(stp, jFrame,AnchoredLayeredPane.getJLayeredPane(userComponent),pointOnScreen);
            jFrame.setLocation(adjustedLoc);
        }

    }

    public GameHistPane(SportsTabPane stp, Game game, int bookieId) {
        this.game = game;
        this.bookieId = bookieId;
        DisplayTransformer displayTransformer = new DisplayTransformer(game.getGame_id());
        SpankyWindowConfig spankyWindowConfig = stp.getSpankyWindowConfig();
        lineType = displayTransformer.transformDefault(spankyWindowConfig.getDisplay());
        period = displayTransformer.transformPeriod(spankyWindowConfig.getPeriod());
        gameDateStr = gameDateFormatter.format(game.getGamedate());
        //        String title = game.getVisitorteam()+"/"+game.getHometeam()+"    View Type: "+lineType;
        Bookie b = AppController.getBookie(bookieId);

        title = b.getShortname()+" - "+game.getGame_id()+" "+game.getVisitorteam() + "/" + game.getHometeam();
    }
    protected JComponent getUserComponent() {
        JFXPanel jFxPanel = new JFXPanel();
        Platform.runLater(() -> createGameHistScene(jFxPanel));
        return jFxPanel;
    }

    private void createGameHistScene(JFXPanel jFxPanel) {

        String url = "http://" + host + "/gamedetails/linehistorynew.jsp?bookieID=" + bookieId + "&gameNum=" + game.getGame_id() + "&period=" + period + "&lineType=" + lineType + "&strgameDate=" + gameDateStr;
        webViewPane = new WebViewPane();
        Scene scene = new Scene(webViewPane);
        jFxPanel.setScene(scene);
        webViewPane.loadUrl(url);
    }
    private void clear() {
        if ( null != webViewPane) {
            webViewPane.clear();
        }
    }
}
