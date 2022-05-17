package com.sia.client.ui;

import com.jidesoft.plaf.LookAndFeelFactory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class LineSeekerAlertMethodStateLayout {

    static int idx;
    private JComboBox<String> renotifyComboBox;
    private final JButton testsound = new JButton("Test Sound");
    private final JButton testpopup = new JButton("Test Popup");
    private JComboBox<String> popupsecsComboBox;
    private final JPanel panel = new JPanel();
    private final JButton usedefaultsound = new JButton("Use Default Sound");
    private final JButton usecustomsound = new JButton("Use Custom Sound");
    private final JLabel soundlabel = new JLabel("DEFAULT");
    private final String[] secslist = new String[60];
    private final String[] minslist = new String[20];
    private final String[] audiolist = new String[8];
    private final int popupsecs = 5;
    private final int popuplocationint = 0;
    private final String alerttype = "";
    private final String name;

    public LineSeekerAlertMethodStateLayout(String name) {
        this.name = name;
    }
    public JComponent getLayoutPane(SpankyWindow spankyWindow) {

        audiolist[0] = "select sound";
        audiolist[1] = "openers";
        audiolist[2] = "Custom Sounds";
        for (int v = 1; v <= 60; v++) {
            secslist[v - 1] = v + "";
        }

        for(int i=0;i<20;i++) {
            minslist[i]=String.valueOf(0.5+0.5*i);
        }

        LookAndFeelFactory.installJideExtension();
        AppController.LineOpenerAlertNodeList.get(LineSeekerAlertMethodStateLayout.idx).popuplocationint = popuplocationint;

        JPanel radioPanel = new JPanel(new GridLayout(2, 2, 0, 0));

        JCheckBox audiocheckbox = new JCheckBox("Play Audio");
        audiocheckbox.addItemListener(e -> {
            int i = LineSeekerAlertMethodStateLayout.idx;
            if (e.getStateChange() == ItemEvent.SELECTED) {
                AppController.LineOpenerAlertNodeList.get(i).isAudioChecks = true;
            } else {
                AppController.LineOpenerAlertNodeList.get(i).isAudioChecks = true;
            }
        });


        JCheckBox popupcheckbox = new JCheckBox("Show Popup");
        popupcheckbox.addItemListener(e -> {
            int i = LineSeekerAlertMethodStateLayout.idx;
            if (e.getStateChange() == ItemEvent.SELECTED) {
                AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks = true;
            } else {
                AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks = true;
            }
        });
        popupsecsComboBox = new JComboBox<>(secslist);
        popupsecsComboBox.addItemListener(e -> {
            int i = LineSeekerAlertMethodStateLayout.idx;
            AppController.LineOpenerAlertNodeList.get(i).showpopvalue = Integer.parseInt((String) popupsecsComboBox.getSelectedItem());
        });


        renotifyComboBox = new JComboBox<>(minslist);
        renotifyComboBox.addItemListener(e -> {
            int i = LineSeekerAlertMethodStateLayout.idx;
            AppController.LineOpenerAlertNodeList.get(i).renotifyvalue = Double.parseDouble((String) renotifyComboBox.getSelectedItem());
        });


        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setLayout(new GridBagLayout());


        JLabel renotifyme = new JLabel("Renotify me on same Sport only after");

        usedefaultsound.addActionListener(ae -> {
            int i = LineSeekerAlertMethodStateLayout.idx;
            AppController.LineOpenerAlertNodeList.get(i).soundfile = "openers.wav";
            AppController.LineOpenerAlertNodeList.get(i).soundlabel = "DEFAULT";
            soundlabel.setText("DEFAULT");
        });
        usecustomsound.addActionListener(ae -> {
            int i = LineSeekerAlertMethodStateLayout.idx;
            JFileChooser jfc = new JFileChooser();
            log("hai iam from filechooser");
            jfc.showOpenDialog(spankyWindow);
            File f1 = jfc.getSelectedFile();

            AppController.LineOpenerAlertNodeList.get(i).soundfile = f1.getPath();
            AppController.LineOpenerAlertNodeList.get(i).soundlabel = f1.getPath();
            soundlabel.setText(f1.getPath());
        });


        testsound.addActionListener(ae -> {
            try {
                playSound("openers.wav");
            } catch (Exception ex) {
                showMessageDialog(null, "Error Playing File!");
                log(ex);
            }
        });

        testpopup.addActionListener(ae -> {
            new UrgentMessage("<HTML><H1>" + alerttype.toUpperCase() + "</H1><FONT COLOR=BLUE>" +
                    "NFL<BR><TABLE cellspacing=5 cellpadding=5>" +

                    "<TR><TD COLSPAN=3>TEST MESSAGE A1</TD></TR>" +
                    "<TR><TD COLSPAN=3>TEST MESSAGE A2</TD></TR>" +
                    "</TABLE></FONT></HTML>", popupsecs * 1000, popuplocationint, spankyWindow);
            log("popupsecs=" + popupsecs);

        });

        JComboBox<String> soundSrc = new JComboBox<>();
        soundSrc.addItem("Default Sound");
        soundSrc.addItem("Upload Sound File");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3,3,3,3);
        //audio row
        c.gridy = 0;

        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor =  GridBagConstraints.WEST;
        panel.add(audiocheckbox, c);

        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 1;
        panel.add(soundSrc, c);

        c.gridx = 1;
        c.gridwidth = 1;
        panel.add(testsound, c);

        //popup row
        c.gridy = 2;

        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(popupcheckbox, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        panel.add(makePopupTimeComp(), c);

        c.gridx = 1;
        c.gridwidth = 1;
        panel.add(testpopup, c);

        //renotify row
        c.gridy = 4;

        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(renotifyme, c);

        c.gridy = 5;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(renotify2(), c);

        return panel;
    }
    private JComponent makePopupTimeComp() {
        JPanel comp = new JPanel();
        BoxLayout boxLayout = new BoxLayout(comp,BoxLayout.X_AXIS);
        comp.setLayout(boxLayout);
        comp.add(new JLabel(" for "));
        comp.add(popupsecsComboBox);
        comp.add(new JLabel(" seconds"));
        return comp;
    }
    private JComponent renotify2() {
        JPanel comp = new JPanel();
        BoxLayout boxLayout = new BoxLayout(comp,BoxLayout.X_AXIS);
        comp.setLayout(boxLayout);
        comp.add(renotifyComboBox);
        comp.add(new JLabel(" minutes have elapsed"));
        return comp;
    }
    public static void playSound(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error Playing File! Check file path. Only AIFF,AU and WAV are supported!");
            log(ex);
        }
    }
}

