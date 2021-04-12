package com.sia.client.ui;

import com.sia.client.config.CheckThreadViolationRepaintManager;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.auth.LoginAdapter;
import org.jdesktop.swingx.auth.LoginEvent;
import org.jdesktop.swingx.auth.LoginListener;
import org.jdesktop.swingx.auth.LoginService;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sia.client.config.Utils.log;


public class SpankOdds {

    SportsTabPane stb;
    TopView tv;
    SportsMenuBar smb;
    OddsFrame of;
    JPanel mainpanel;
    private JFrame frame;
    private String userName;
    private int failedAttemptsCount = 0;

    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.keyStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ts");
	/*	try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(com.sia.client.ui.ChartHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(com.sia.client.ui.ChartHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(com.sia.client.ui.ChartHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(com.sia.client.ui.ChartHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
    /*  java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SpankOdds().showLoginDialog();
            }
        });*/
        AppController.createLineOpenerAlertNodeList();
        AppController.initializSpotsTabPaneVector();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SpankOdds().showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {

        RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager(true));
        frame = new JFrame("Spank Odds");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoginClient client = new LoginClient();


        final JXLoginPane loginPane = new JXLoginPane();
        loginPane.setBannerText("Spank Odds");

        LoginListener loginListener = new LoginAdapter() {
            @Override
            public void loginSucceeded(LoginEvent source) {
                SpankOdds.this.userName = loginPane.getUserName();

                //SpankOdds.this.createAndShowGui();
                SpankOdds.this.showGui();
            }

            @Override
            public void loginFailed(LoginEvent source) {
                failedAttemptsCount++;
                String message;
                message = "Invalid Credentials!";
                loginPane.setErrorMessage(message);
            }
        };

        LoginService loginService = new LoginService() {
            @Override
            public boolean authenticate(String name, char[] password, String server) throws Exception {


                System.out.println(new java.util.Date());
                try {
                    //Platform.runLater(new Runnable() { @Override public void run() {lbllogin.setText("Processing...");}});

                    client.login(name, String.valueOf(password));
                    Thread.sleep(1000);

                } catch (Exception ex) {
                    System.out.println("error loggin in " + ex);
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
                System.out.println("out of the while loop " + client.loginresultback);
                System.out.println("result " + client.isloggedin());
                System.out.println(new java.util.Date());
                boolean loggedin = client.isloggedin();
                if (loggedin) {
                    createGui();


                }
                return loggedin;

            }
        };

        loginService.addLoginListener(loginListener);
        loginPane.setLoginService(loginService);

        JXLoginPane.JXLoginDialog dialog = new JXLoginPane.JXLoginDialog(frame, loginPane);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        // if loginPane was cancelled or closed then its status is CANCELLED
        // and still need to dispose main JFrame to exiting application
        if (loginPane.getStatus() == JXLoginPane.Status.CANCELLED) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void showGui() {
        //frame = new OddsFrame(stb,tv);
        //frame.setVisible(true);
        SwingUtilities.invokeLater(
                () -> {
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            System.out.println("Window Closing! ");
                            AppController.removeFrame(frame);
                            System.exit(0);

                        }
                    });
                    frame.setLayout(new BorderLayout(1, 1));
                    frame.getContentPane().add(tv, BorderLayout.PAGE_START);
                    frame.getContentPane().add(stb, BorderLayout.CENTER);
                    frame.setJMenuBar(smb);
                    AppController.addFrame(frame, stb);

                    frame.setSize(950, 800);

                    frame.setVisible(true);

                }
        );


    }

    private void createGui() {

        SwingUtilities.invokeLater(() -> {

            // owen took out 7/11/2020
            log("creating gui");
            stb = new SportsTabPane();
            tv = new TopView(stb);
            smb = new SportsMenuBar(stb, tv);
        });
    }

    private void createAndShowGui() {
        System.out.println("creating and showing gui");
        createGui();
        showGui();
		/*



            }
        });
		*/

    }


}