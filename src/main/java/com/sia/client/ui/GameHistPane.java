package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.SpankyWindowConfig;
import com.sia.client.ui.comps.EmbededFrame;
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
//    public static void main(String [] argv) {
//        JDesktopPane jDesktopPane = new JDesktopPane();
//        JFrame jFrame = new JFrame("Test Main Frame");
//        jFrame.setLayeredPane(new JDesktopPane());
//        jFrame.setContentPane(jDesktopPane);
//
//        JPanel bckPanel = new JPanel();
//        bckPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
//        bckPanel.add(new JButton("test button"));
//        jFrame.getContentPane().setLayout(new BorderLayout());
//        jFrame.getContentPane().add(bckPanel,BorderLayout.CENTER);
//
//        JInternalFrame internalFrame = new EmbededFrame("HELLO",true,true,true,true);
//        internalFrame.setLayout(new BorderLayout());
//        internalFrame.add(new JTextField("internalFrame1"),BorderLayout.SOUTH);
//        internalFrame.setPreferredSize(paneSize);
//        internalFrame.toFront();
//        internalFrame.setVisible(true);
//
//        JInternalFrame internalFrame2 = new EmbededFrame("HELLO2",true,true,true,true);
//        internalFrame2.setLayout(new BorderLayout());
//        internalFrame2.add(new JTextField("internalFrame12"),BorderLayout.SOUTH);
//        internalFrame2.setPreferredSize(paneSize);
//        internalFrame2.toFront();
//        internalFrame2.setVisible(true);
//
//        JLayeredPane jLayeredPane = jFrame.getLayeredPane();
//        jLayeredPane.add(internalFrame,100);
//        jLayeredPane.add(internalFrame2,100);
//        internalFrame.setBounds(10,10,300,300);
//        internalFrame2.setBounds(20,20,300,300);
//
//        jFrame.setPreferredSize(new Dimension(1600, 800));
//        jFrame.setLocation(100,100);
//        jFrame.pack();
//        jFrame.setVisible(true);
//
//
//
//    }
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
        //        String title = game.getVisitorteam()+"/"+game.getHometeam()+"    View Type: "+lineType;
        String title = game.getVisitorteam()+"/"+game.getHometeam();
        setTitle(title);
        setIsFloating(true);
    }
    @Override
    protected JComponent getUserComponent() {
        JFXPanel jFxPanel = new JFXPanel();
        Platform.runLater(() -> createGameHistScene(jFxPanel));
        return jFxPanel;
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
