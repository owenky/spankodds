package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.ui.control.SportsTabPane;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.sia.client.config.Utils.log;

public class SpankyWindow extends JFrame {

    public static final List<SpankyWindow> winList = new ArrayList<>();
    private static final String spankoddsicon = "spanky.jpg";
    private static final AtomicInteger counter = new AtomicInteger(1);
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
            URL imgResource = Utils.getMediaResource(spankoddsicon);
            Image spankyimage = ImageIO.read(imgResource);
            instance.setIconImage(spankyimage);
        } catch (Exception ex) {
            log(ex);
        }
        instance.performLayout();
        instance.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        instance.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                instance.unBindAlertsComp();
                AppController.getUserPrefsProducer().sendUserPrefs();
                log("closing SpankyWindow instance "+instance.getName());
                AppController.removeFrame(instance);

            }
        });
        if ( null == gameClockUpdater) {
            gameClockUpdater = new GameClockUpdater();
            gameClockUpdater.start();
        }
        return instance;
    }
    public static void applyToAllWindows(Consumer<SportsTabPane> method) {
        for(int i=0;i<SpankyWindow.openWindowCount();i++){
            SportsTabPane stb = SpankyWindow.getSpankyWindow(i).getSportsTabPane();
            method.accept(stb);
        }
    }
    public static int openWindowCount() {
        return winList.size();
    }
    public static SpankyWindow getSpankyWindow(int index) {
        return winList.get(index);
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

        JPanel mainpanel = new JPanel();
        mainpanel.setLayout(new BorderLayout(1,1));
        mainpanel.add(tv, BorderLayout.PAGE_START);
        mainpanel.add(stp, BorderLayout.CENTER);
        setContentPane(mainpanel);
        setSize(600, 600);
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
}
