package com.sia.client.ui;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.sia.client.ui.comps.LightComboBox;
import com.sia.client.ui.comps.LinkButton;
import com.sia.client.ui.lineseeker.LineSeekerAlertMethodAttr;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class LineSeekerAlertMethodStateLayout {

    static int idx;
    private static final Dimension TestButtonSpacing = new Dimension(10,0);
    private static final Dimension ComboBoxPrefSize = new Dimension(60,15);
    private JComboBox<String> renotifyComboBox;
    private final JButton testsound = new LinkButton("Test Sound");
    private final JButton testpopup = new LinkButton("Test Popup");
    private JComboBox<String> popupsecsComboBox;
    private final JPanel panel = new JPanel();
    private final JButton usedefaultsound = new JButton("Use Default Sound");
    private final JButton usecustomsound = new JButton("Use Custom Sound");
    private final JLabel soundlabel = new JLabel("DEFAULT");
    private final String[] secslist = new String[60];
    private final String[] minslist = new String[20];
    private final int popupsecs = 5;
    private final int popuplocationint = 0;
    private final String alerttype = "";
    private final String name;
    private final JCheckBox audiocheckbox = new JCheckBox("Audio");
    private final JCheckBox popupcheckbox = new JCheckBox("Popup");
    private final JComboBox<String> soundSrc = new LightComboBox<>();
    private static final String SoundSrcDefault = "Default Sound";
    private static final String SoundSrcUpload = "Upload Sound File";

    public LineSeekerAlertMethodStateLayout(String name) {
        this.name = name;
    }
    public JComponent getLayoutPane(SpankyWindow spankyWindow) {

        initComponents(spankyWindow);
        JLabel renotifyme = new JLabel("Renotify me on same Sport only after");
        JLabel titleLabel = new JLabel(name);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,1,20,1);

        //0. title row
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor =  GridBagConstraints.CENTER;
        panel.add(titleLabel, c);

        //1. audio row
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor =  GridBagConstraints.WEST;
        panel.add(makeAudioRow(), c);

        //2. popup row
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(makePopupRow(), c);

        //3. renotify row
        c.insets = new Insets(0,1,10,1);
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(renotifyme, c);

        //6.renotify row 2
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(renotify2(), c);

        setComponentFont(panel, FontConfig.instance().getSelectedFont().deriveFont(Font.PLAIN));
        titleLabel.setFont(FontConfig.instance().getSelectedFont().deriveFont(Font.BOLD));
        return panel;
    }
    private JComponent makeAudioRow() {
        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel,BoxLayout.X_AXIS);
        panel.setLayout(boxLayout);
        panel.add(audiocheckbox);
        panel.add(soundSrc);
        panel.add(Box.createRigidArea(TestButtonSpacing));
        panel.add(testsound);
        return panel;
    }
    private JComponent makePopupRow() {
        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel,BoxLayout.X_AXIS);
        panel.setLayout(boxLayout);
        panel.add(popupcheckbox);
        panel.add(new JLabel(" for "));
        panel.add(popupsecsComboBox);
        panel.add(new JLabel(" seconds"));
        panel.add(Box.createRigidArea(TestButtonSpacing));
        panel.add(testpopup);
        return panel;
    }
    private void initComponents(SpankyWindow spankyWindow) {
        for (int v = 1; v <= 60; v++) {
            secslist[v - 1] = v + "";
        }

        for(int i=0;i<20;i++) {
            minslist[i]=String.valueOf(0.5+0.5*i);
        }

        LookAndFeelFactory.installJideExtension();
        AppController.LineOpenerAlertNodeList.get(LineSeekerAlertMethodStateLayout.idx).popuplocationint = popuplocationint;

        popupsecsComboBox = new LightComboBox<>(secslist);
        popupsecsComboBox.setPreferredSize(ComboBoxPrefSize);
        popupsecsComboBox.addItemListener(e -> {
            int i = LineSeekerAlertMethodStateLayout.idx;
            AppController.LineOpenerAlertNodeList.get(i).showpopvalue = Integer.parseInt((String) popupsecsComboBox.getSelectedItem());
        });


        renotifyComboBox = new LightComboBox<>(minslist);
        renotifyComboBox.setPreferredSize(ComboBoxPrefSize);
        renotifyComboBox.addItemListener(e -> {
            int i = LineSeekerAlertMethodStateLayout.idx;
            AppController.LineOpenerAlertNodeList.get(i).renotifyvalue = Double.parseDouble((String) renotifyComboBox.getSelectedItem());
        });


        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setLayout(new GridBagLayout());

        usedefaultsound.addActionListener(ae -> {
            int i = LineSeekerAlertMethodStateLayout.idx;
            AppController.LineOpenerAlertNodeList.get(i).soundfile = "openers.wav";
            AppController.LineOpenerAlertNodeList.get(i).soundlabel = "DEFAULT";
            soundlabel.setText("DEFAULT");
        });
        usecustomsound.addActionListener(ae -> {

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

        soundSrc.addItem(SoundSrcDefault);
        soundSrc.addItem(SoundSrcUpload);
        soundSrc.addItemListener(this::onSoundFileChanged);
    }
    private void onSoundFileChanged(ItemEvent ie) {

        if (ie.getStateChange() == ItemEvent.SELECTED) {
            Object item = ie.getItem();
            if ( SoundSrcUpload.equals(item)) {
                JFileChooser jfc = new JFileChooser();
                jfc.showOpenDialog(SpankyWindow.getFirstSpankyWindow());
                File f1 = jfc.getSelectedFile();
                String uploaded = f1.getPath();
                System.out.println("uploaded="+uploaded);
            }
        }
    }
    private static void setComponentFont(JComponent parent, Font font ) {
        java.util.List<JComponent> result = new ArrayList<>();
        findChildCompDeep(parent,result);
        for(JComponent c: result) {
            c.setFont(font);
        }
    }
    private static void findChildCompDeep(JComponent parent,java.util.List<JComponent> result) {
        for( Component c : parent.getComponents()) {
            if ( c instanceof JComponent) {
                JComponent jc = (JComponent)c;
                if ( jc instanceof JComboBox || jc instanceof JCheckBox ) {
                    result.add(jc);
                }  else if( jc.getComponents().length > 0) {
                    findChildCompDeep(jc,result);
                } else {
                    result.add(jc);
                }
            }
        }
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
    public void updateAlertMethodAttr(LineSeekerAlertMethodAttr attr) {
        audiocheckbox.setSelected(attr.getAudioEnabled());
        soundSrc.setSelectedItem(attr.getSoundFile());
        popupcheckbox.setSelected(attr.getPopupEnabled());
        popupsecsComboBox.setSelectedItem(attr.getPopupSeconds());
        renotifyComboBox.setSelectedItem(attr.getRenotifyInMinutes());
    }
    public void saveMethodAttr(LineSeekerAlertMethodAttr attr) {
        attr.setAudioEnabled(audiocheckbox.isSelected());
        attr.setSoundFile(String.valueOf(soundSrc.getSelectedItem()));
        attr.setPopupEnabled(popupcheckbox.isSelected());
        attr.setPopupSeconds(String.valueOf(popupsecsComboBox.getSelectedItem()));
        attr.setRenotifyInMinutes(String.valueOf(renotifyComboBox.getSelectedItem()));
    }
}

