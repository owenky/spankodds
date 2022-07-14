package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.ui.control.SportsTabPane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.sia.client.config.Utils.log;

public class SpankyWindow extends JFrame {

    public static final List<SpankyWindow> winList = new ArrayList<>();
    private static final String spankoddsicon = "spanky.jpg";
    private static final String spankoddsiconnew = "spankoddsrgb.png";
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final SportsTabPane stp;
    private final TopView tv;
    private static GameClockUpdater gameClockUpdater;

    public static SpankyWindow create() {
        return create(AppController.getUser().getUsername() + " Logged In"+ SiaConst.Version);
    }
    public synchronized static SpankyWindow create(String title) {
        int windowIndex = counter.getAndAdd(1);
        SportsTabPane stp = new SportsTabPane(windowIndex);
        SpankyWindow instance = new SpankyWindow(title,stp);
        instance.setName(String.valueOf(windowIndex));
        winList.add(instance);
        try {
            URL imgResource = Utils.getMediaResource(spankoddsiconnew);
            Image spankyimage = ImageIO.read(imgResource);
           // spankyimage =  Utils.getImage(spankoddsiconnew);
            instance.setIconImage(spankyimage);
        } catch (Exception ex) {
            log(ex);
        }
        instance.performLayout();
        instance.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        instance.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if ( 1==winList.size()) {

                    JOptionPane optionPane = new JOptionPane("Do you want to exit ?",
                            JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
                    JDialog dialog = optionPane.createDialog("Alert");
                    dialog.setAlwaysOnTop(true);
                    dialog.setVisible(true);
                    dialog.dispose();

                    Object  selectedValue = optionPane.getValue();
                    int result;
                    if ( selectedValue instanceof Integer) {
                        result = (Integer)selectedValue;
                    } else {
                        result = JOptionPane.OK_OPTION;
                    }
                    if (  JOptionPane.CANCEL_OPTION ==  result ) {
                        return;
                    }

                    AppController.getUserPrefsProducer().sendUserPrefs(true);
                }
                instance.unBindAlertsComp();
                log("closing SpankyWindow instance "+instance.getName());
                AppController.removeFrame(instance);
                instance.setVisible(false);
                ShortCut.removeSportTabPane(instance.stp);
                instance.dispose();
            }
        });
        if ( null == gameClockUpdater) {
            gameClockUpdater = GameClockUpdater.instance();
            gameClockUpdater.start();
        }
        return instance;
    }
    public static void applyToAllWindows(Consumer<SportsTabPane> method) {
        for(int i=0;i<SpankyWindow.openWindowCount();i++){
            SportsTabPane stb = winList.get(i).getSportsTabPane();
            method.accept(stb);
        }
    }
    public static int openWindowCount() {
        return winList.size();
    }
    public static SpankyWindow findSpankyWindow(int windowIndex) {
        SpankyWindow result = null;
        for(SpankyWindow window: winList) {
            if ( window.getSportsTabPane().getWindowIndex() == windowIndex) {
                result = window;
                break;
            }
        }
        return result;
    }
    public static SpankyWindow getFirstSpankyWindow() {
        return winList.get(0);
    }
    public static Iterator<SpankyWindow> getAllSpankyWindows() {
        return winList.iterator();
    }
    public static void removeWindow(SpankyWindow frame) {
        winList.remove(frame);
    }
    private SpankyWindow(String title,SportsTabPane stp) {
        super(title);
        this.stp = stp;
        this.tv = new TopView(stp);
    }
    private void performLayout() {
        tv.initComponents();
        SportsMenuBar smb = new SportsMenuBar(stp, tv);

        setJMenuBar(smb);
//        JDesktopPane desktopPane = new JDesktopPane();
        Container desktopPane = getContentPane();
        desktopPane.setLayout(new BorderLayout());
        desktopPane.add(tv, BorderLayout.PAGE_START);
        desktopPane.add(stp,BorderLayout.CENTER);
        //setLayeredPane must be called before setContentPane 06/07/2022
//        setLayeredPane(new JDesktopPane());
    }
    public TopView getTopView() {
        return this.tv;
    }
    public void populateTabPane() {
        stp.populateComponents();
    }
    public SportsTabPane getSportsTabPane() {
       return stp;
    }
    private void unBindAlertsComp() {
        tv.unBindAlertsComp();
    }
    public static void setLocationaAndSize(JFrame frame,int screenXmargin,int screenYmargin) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int)screenSize.getWidth();
        int screenHeight = (int)screenSize.getHeight();
        frame.setSize(screenWidth - 2* screenXmargin, screenHeight - 2* screenYmargin);
        frame.setLocation(new Point(screenXmargin,screenYmargin));
    }
}
