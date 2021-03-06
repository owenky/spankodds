package com.sia.client.ui.lineseeker;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.sia.client.config.Config;
import com.sia.client.media.SoundPlayer;
import com.sia.client.ui.SpankyWindow;
import com.sia.client.ui.UrgentMessage;
import com.sia.client.ui.comps.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class LineSeekerAlertMethodStatePane implements EditablePane {

    private final JButton testsound = new JButton("Test Sound");
    private final JButton testpopup = new JButton("Test Popup");
    private SbtStringComboBox popupsecsComboBox;
    private final JPanel panel = new JPanel();
    private final String[] secslist = new String[60];
    private final int popupsecs = 5;
    private final String alerttype = "";
    private final SbtCheckBox audiocheckbox = new SbtCheckBox("Audio");
    private final SbtCheckBox popupcheckbox = new SbtCheckBox("Popup");
    private final SbtStringComboBox soundSrc = new SbtStringComboBox();
    private final JLabel editStatusLabel = new JLabel();
    private final LineSeekerAlertMethodAttr attr;
    private final PopupLocationConfig popupLocationConfig = new PopupLocationConfig();
    private UICompValueBinder uiCompValueBinder;
    private final SpankyWindow spankyWindow;

    public LineSeekerAlertMethodStatePane(LineSeekerAlertMethodAttr attr, SpankyWindow spankyWindow) {
        this.attr = attr;
        this.spankyWindow = spankyWindow;
    }
    @Override
    public JComponent getPane() {

        initComponents(spankyWindow);
        JLabel titleLabel = new JLabel(attr.getAlertState());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(editStatusLabel,BorderLayout.EAST);
        titlePanel.add(titleLabel,BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,1,20,1);
        float middleWeightX = 1.5f;

        //0. title row
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor =  GridBagConstraints.CENTER;
//        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(titlePanel, c);

        //1. audio row
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 1;
//        c.fill = GridBagConstraints.NONE;
        panel.add(audiocheckbox, c);

        c.gridx = 1;
        c.weightx = middleWeightX;
        c.gridwidth = 1;
        c.anchor =  GridBagConstraints.WEST;
//        c.fill = GridBagConstraints.BOTH;
        panel.add(soundSrc,c);

        c.gridx = 2;
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
//        c.fill = GridBagConstraints.NONE;
        panel.add(testsound,c);


        //2. popup row
        c.gridy++;
        c.gridx = 0;
        c.weightx = 1.0;
        c.gridwidth = 1;
        c.anchor =  GridBagConstraints.CENTER;
//        c.fill = GridBagConstraints.NONE;
        panel.add(popupcheckbox, c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = middleWeightX;
        c.anchor =  GridBagConstraints.WEST;
//        c.fill = GridBagConstraints.BOTH;
        panel.add(makeLocationAndTimePane(),c);

        c.gridx = 2;
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
//        c.fill = GridBagConstraints.NONE;
        panel.add(testpopup,c);

        Font font = Config.instance().getFontConfig().getSelectedFont();
        setComponentFont(panel, font);
        titleLabel.setFont(font.deriveFont(Font.BOLD,(float)(font.getSize()+1)));
        return panel;
    }
    private JComponent makeLocationAndTimePane() {
        JPanel pane = new JPanel();
        JLabel atlabel = new JLabel(" at ");
        atlabel.setHorizontalAlignment(SwingConstants.LEFT);
        pane.add(atlabel);
        pane.add(popupLocationConfig);
        pane.add(new JLabel(" for "));
        pane.add(popupsecsComboBox);
        pane.add(new JLabel(" seconds"));
        return pane;
    }
    private void initComponents(SpankyWindow spankyWindow) {

        Dimension dim = new Dimension(20,20);
        editStatusLabel.setPreferredSize(dim);
        editStatusLabel.setSize(dim);
        editStatusLabel.setMinimumSize(dim);
        editStatusLabel.setMaximumSize(dim);

        for (int v = 1; v <= 60; v++) {
            secslist[v - 1] = v + "";
        }
        LookAndFeelFactory.installJideExtension();

        popupsecsComboBox = new SbtStringComboBox(secslist);
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setLayout(new GridBagLayout());

        testsound.addActionListener(ae -> {
            try {
                SoundPlayer.playSound(String.valueOf(soundSrc.getSelectedItem()));
            } catch (Exception ex) {
                showMessageDialog(null, "Error Playing File!");
                log(ex);
            }
        });

        testpopup.addActionListener(ae -> {
            int popuplocationint = popupLocationConfig.getSelectedPopupLocation().getLocation();
            new UrgentMessage("<HTML><H1>" + alerttype.toUpperCase() + "</H1><FONT COLOR=BLUE>" +
                    "NFL<BR><TABLE cellspacing=5 cellpadding=5>" +

                    "<TR><TD COLSPAN=3>TEST MESSAGE A1</TD></TR>" +
                    "<TR><TD COLSPAN=3>TEST MESSAGE A2</TD></TR>" +
                    "</TABLE></FONT></HTML>", popupsecs * 1000, popuplocationint, spankyWindow);
        });

        // sound selection
        soundSrc.addItem("beautifulbaby.wav");
        soundSrc.addItem("beep1.wav");
        soundSrc.addItem("beep2.wav");
        soundSrc.addItem("beep3.wav");
        soundSrc.addItem("bigearn.wav");
        soundSrc.addItem("doublehorn.wav");
        soundSrc.addItem("fuckyeah.wav");
        soundSrc.addItem("fuckyeah2.wav");
        soundSrc.addItem("horn.wav");
        soundSrc.addItem("ohshit.wav");
        soundSrc.addItem("onothissucks.wav");
        soundSrc.addItem("scream.wav");
        soundSrc.addItem("whatthefuck.wav");
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
    @Override
    public UICompValueBinder getJComponentBinder() {
        if ( null == uiCompValueBinder) {
            uiCompValueBinder = UICompValueBinder.register(this,getOnValueChangedEventFunction()).withPersistenceObject(attr)
                .bindCompProp("audioEnabled",audiocheckbox).bindCompProp("soundFile",soundSrc)
                    .bindCompProp("popupEnabled",popupcheckbox).bindCompProp("popupSeconds",popupsecsComboBox)
                    .bindCompProp("popupLocation",popupLocationConfig);
        }
        return uiCompValueBinder;
    }
    @Override
    public JLabel getEditStatusLabel() {
        return editStatusLabel;
    }
}

