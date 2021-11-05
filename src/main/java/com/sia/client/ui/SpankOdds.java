package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.simulator.InitialGameMessages;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.auth.LoginAdapter;
import org.jdesktop.swingx.auth.LoginEvent;
import org.jdesktop.swingx.auth.LoginListener;
import org.jdesktop.swingx.auth.LoginService;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;


public class SpankOdds {

    private static final String version = "20210601001";
//    private SportsTabPane stb;
//    private TopView tv;
//    private SportsMenuBar smb;
//    private OddsFrame of;
//    private JPanel mainpanel;
    private SpankyWindow frame;
    private String userName;
//    private int failedAttemptsCount = 0;

    public static void main(String[] args) {
        initSystemProperties();
        com.jidesoft.utils.Lm.verifyLicense("Spank Odds", "Spank Odds",
                "gJGsTI2f4lYzPcskZ7OHWXN7iPvWAbO2");
        System.setProperty("javax.net.ssl.keyStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ts");
        log("CHANGE04242021 ");

        InitialGameMessages.initMsgLoggingProps();

        AppController.createLineOpenerAlertNodeList();
        AppController.initializSpotsTabPaneVector();

        checkAndRunInEDT(() -> new SpankOdds().showLoginDialog());
    }
    private static void initSystemProperties() {
        if ( Boolean.parseBoolean(System.getProperty("LogToFile")) ) {
            try {
                Utils.logPs = new PrintStream(new FileOutputStream(SiaConst.logFileName));
                Utils.errPs = new PrintStream(new FileOutputStream(SiaConst.errFileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Utils.logPs = System.out;
            Utils.errPs = System.err;
        }
    }
    private void showLoginDialog() {

//        RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager(true));
        frame = SpankyWindow.create("Spank Odds )" + version + ")");

        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoginClient client = new LoginClient();


        final JXLoginPane loginPane = new JXLoginPane();
        loginPane.setBannerText("Spank Odds");

        LoginListener loginListener = new LoginAdapter() {
            @Override
            public void loginSucceeded(LoginEvent source) {
                SpankOdds.this.userName = loginPane.getUserName();
                InitialGameMessages.postDataLoading();
                SpankOdds.this.showGui();
            }

            @Override
            public void loginFailed(LoginEvent source) {
//                failedAttemptsCount++;
                String message;
                message = "Invalid Credentials!";
                loginPane.setErrorMessage(message);
            }
        };

        LoginService loginService = new LoginService() {
            @Override
            public boolean authenticate(String name, char[] password, String server) throws Exception {


                log("data:"+new java.util.Date());
                try {
                    //Platform.runLater(new Runnable() { @Override public void run() {lbllogin.setText("Processing...");}});
                    //
                    client.login(name, String.valueOf(password));
                    Thread.sleep(1000);

                } catch (Exception ex) {
                    log( ex);
                }
                int i = 0;
                while (!client.getLoginResultBack()) //wait for login
                {
                    i++;
                    if (i % 10000000 == 0) {
                        System.out.print(" ");
                        i = 1;
                    }

                }
                log("out of the while loop " + client.loginresultback);
                log("result " + client.isloggedin());
                log("date:"+new java.util.Date());
                boolean loggedin = client.isloggedin();
                if (loggedin) {
                    userName = name;
                }
                return loggedin;

            }
        };

        loginService.addLoginListener(loginListener);
        loginPane.setLoginService(loginService);
        loginPane.addPropertyChangeListener(evt -> {
            if ("status".equals(evt.getPropertyName()) && JXLoginPane.Status.CANCELLED.equals(evt.getNewValue())) {
                System.exit(0);
            }
        });

        JXLoginPane.JXLoginDialog dialog = new JXLoginPane.JXLoginDialog(frame, loginPane);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        Utils.computeEffectiveScreenSize(dialog);
        dialog.setVisible(true);

        // if loginPane was cancelled or closed then its status is CANCELLED
        // and still need to dispose main JFrame to exiting application
        if (loginPane.getStatus() == JXLoginPane.Status.CANCELLED) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        }
    }

    private void showGui() {
        //frame = new OddsFrame(stb,tv);
        //frame.setVisible(true);
        checkAndRunInEDT(
                () -> {
                    try {
                        frame.setTitle(userName + " Logged In "+ SiaConst.Version);
                        frame.populateTabPane();
//                        createGui();
//                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                        frame.addWindowListener(new WindowAdapter() {
//                            @Override
//                            public void windowClosing(WindowEvent e) {
//                                log("Window Closing! ");
//                                AppController.getUserPrefsProducer().sendUserPrefs();
//                                AppController.removeFrame(frame);
//
//                            }
//                        });
//                        tv.initComponents();
//                        frame.setLayout(new BorderLayout(1, 1));
//                        frame.getContentPane().add(tv, BorderLayout.PAGE_START);
//                        frame.getContentPane().add(stb, BorderLayout.CENTER);
//                        frame.setJMenuBar(smb);
                        AppController.addFrame(frame);

                        frame.setSize(950, 800);

                        frame.setVisible(true);

                    } catch (Exception e) {
                        log(e);
                        JOptionPane.showConfirmDialog(frame, "Error encountered, please contact customer service<br>\n" + e.getMessage(), "System Error", JOptionPane.YES_NO_OPTION);
                        System.exit(-1);
                    }

                }
        );


    }
//
//    private void createGui() {
//
//        checkAndRunInEDT(() -> {
//
//            // owen took out 7/11/2020
//            log("creating gui");
//            stb = new SportsTabPane();
//            tv = new TopView(stb);
//            smb = new SportsMenuBar(stb, tv);
//        });
//    }
}