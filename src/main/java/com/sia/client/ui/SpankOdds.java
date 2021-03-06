package com.sia.client.ui;

import com.sia.client.config.*;
import com.sia.client.config.SiaConst.UIProperties;
import com.sia.client.simulator.InitialGameMessages;
import com.sia.client.simulator.LocalMessageLogger;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.auth.LoginAdapter;
import org.jdesktop.swingx.auth.LoginEvent;
import org.jdesktop.swingx.auth.LoginListener;
import org.jdesktop.swingx.auth.LoginService;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.RepaintManager;
import javax.swing.ToolTipManager;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;



import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;


public class SpankOdds {

    public static boolean getMessagesFromLog = false;
    private SpankyWindow frame;
    private String userName;
    private static String ip;

    public static void main(String[] args) {
        getMessagesFromLog = Boolean.parseBoolean(System.getProperty("GetMesgFromLog"));
        if ( getMessagesFromLog) {
            Utils.logger = new LocalMessageLogger();
        } else {
            Utils.logger = new Logger();
        }
        initSystemProperties();
        com.jidesoft.utils.Lm.verifyLicense("Spank Odds", "Spank Odds",
                "gJGsTI2f4lYzPcskZ7OHWXN7iPvWAbO2");

        URL client_ks = SpankOdds.class.getResource(("/config/client.ks"));
        URL client_ts = SpankOdds.class.getResource(("/config/client.ts"));
        /*
        System.setProperty("javax.net.ssl.keyStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ts");
*/
        System.setProperty("javax.net.ssl.keyStore", client_ks.getPath());
        System.setProperty("javax.net.ssl.keyStorePassword", "j0llyg00dm00d");
        System.setProperty("javax.net.ssl.trustStore", client_ts.getPath());

        InitialGameMessages.initMsgLoggingProps();

        //spanky added ip lookup to pass to loginclient
        try
        {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            ip = in.readLine(); //you get the IP as a String
        }
        catch(Exception ex)
        {
            log(ex);
        }

        AppController.createLineOpenerAlertNodeList();
        AppController.createLimitNodeList();
        AppController.initializSpotsTabPaneVector();
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(30000);
        checkAndRunInEDT(() -> new SpankOdds().showLoginDialog(),true);
    }

    private static void initSystemProperties() {
        Logger logger = Utils.logger;
        if (Boolean.parseBoolean(System.getProperty("LogToFile"))) {
            try {
                logger.setlogPs(new PrintStream(new FileOutputStream(SiaConst.logFileName)));
                logger.setErrPs(new PrintStream(new FileOutputStream(SiaConst.errFileName)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            logger.setlogPs(System.out);
            logger.setErrPs(System.err);
        }
    }

    private void showLoginDialog() {

        if ( InitialGameMessages.Debug) {
            RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager(true));
        }

        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoginClient client = new LoginClient(ip);
        LocalPwdStore localPwdStore = new LocalPwdStore(LocalConfig.instance());
        LocalUserStore localUserStore = new LocalUserStore(LocalConfig.instance());
        final JXLoginPane loginPane = new JXLoginPane(null, localPwdStore, localUserStore) {
            @Override
            public String getUserName() {
                String userName = super.getUserName();
                return null==userName?"":userName;
            }
        };
        loginPane.setBanner(Utils.getImage("spankoddstextonsoft.png"));
        // loginPane.setBannerText("Spank Odds");
        loginPane.setUserName(localUserStore.getLastUserName());
        LoginListener loginListener = new LoginAdapter() {
            @Override
            public void loginSucceeded(LoginEvent source) {
                frame = SpankyWindow.create("Spank Odds (" + SiaConst.Version + ")");
                SpankOdds.this.userName = loginPane.getUserName();
                InitialGameMessages.postDataLoading();
                Config.instance().setOutOfSync();
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

                try {
                    //Platform.runLater(new Runnable() { @Override public void run() {lbllogin.setText("Processing...");}});
                    //
                    client.login(name, String.valueOf(password));
                    localUserStore.removeUserName(name);
                    localPwdStore.set(name,"",password);
                    Thread.sleep(1000);

                } catch (Exception ex) {
                    log(ex);
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
        if (loginPane.getStatus() == JXLoginPane.Status.CANCELLED ) {
            if ( null != frame ) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
            System.exit(0);
        }
    }

    private void showGui() {

        try {
            frame.setTitle(userName + " Logged In " + SiaConst.Version);
            AppController.addFrame(frame);
            SpankyWindow.setLocationaAndSize(frame,UIProperties.screenXmargin,UIProperties.screenYmargin);
            frame.populateTabPane();
            frame.setVisible(true);

           // AnchoredLayeredPane anchoredLayeredPane = new AnchoredLayeredPane(AppController.getMainTabPane());
           // WelcomeImagePane wip = new WelcomeImagePane(anchoredLayeredPane);
           // wip.openAndCenter(new Dimension(700,700),false);
            Utils.checkAndRunInEDT(() -> new WelcomeMessage());


        } catch (Exception e) {
            log(e);
            JOptionPane.showConfirmDialog(frame, "Error encountered, please contact customer service<br>\n" + e.getMessage(), "System Error", JOptionPane.YES_NO_OPTION);
            System.exit(-1);
        }
    }
}