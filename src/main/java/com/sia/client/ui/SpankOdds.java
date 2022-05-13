package com.sia.client.ui;

import com.sia.client.config.CheckThreadViolationRepaintManager;
import com.sia.client.config.LocalConfig;
import com.sia.client.config.Logger;
import com.sia.client.config.SiaConst;
import com.sia.client.config.SiaConst.UIProperties;
import com.sia.client.config.Utils;
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
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;


public class SpankOdds {

    public static boolean getMessagesFromLog = false;
    private SpankyWindow frame;
    private String userName;

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
        System.setProperty("javax.net.ssl.keyStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ts");

        InitialGameMessages.initMsgLoggingProps();

        AppController.createLineOpenerAlertNodeList();
        AppController.initializSpotsTabPaneVector();
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(5000);
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
        LoginClient client = new LoginClient();
        LocalPwdStore localPwdStore = new LocalPwdStore(LocalConfig.instance());
        LocalUserStore localUserStore = new LocalUserStore(LocalConfig.instance());
        final JXLoginPane loginPane = new JXLoginPane(null, localPwdStore, localUserStore) {
            @Override
            public String getUserName() {
                String userName = super.getUserName();
                return null==userName?"":userName;
            }
        };
        loginPane.setBannerText("Spank Odds");

        LoginListener loginListener = new LoginAdapter() {
            @Override
            public void loginSucceeded(LoginEvent source) {
                frame = SpankyWindow.create("Spank Odds (" + SiaConst.Version + ")");
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

                try {
                    //Platform.runLater(new Runnable() { @Override public void run() {lbllogin.setText("Processing...");}});
                    //
                    client.login(name, String.valueOf(password));
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
        } catch (Exception e) {
            log(e);
            JOptionPane.showConfirmDialog(frame, "Error encountered, please contact customer service<br>\n" + e.getMessage(), "System Error", JOptionPane.YES_NO_OPTION);
            System.exit(-1);
        }
    }
}