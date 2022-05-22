package com.sia.client.ui;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.sia.client.config.SiaConst;
import com.sia.client.media.SoundPlayer;
import com.sia.client.ui.comps.LightComboBox;
import com.sia.client.ui.comps.LinkButton;
import com.sia.client.ui.comps.PopupLocationConfig;
import com.sia.client.ui.lineseeker.LineSeekerAlertMethodAttr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class LineSeekerAlertMethodStateLayout {

    static int idx;
    private static final String DefaultSoundFileDisp = "Default Sound";
    private static final Dimension TestButtonSpacing = new Dimension(10,0);
    private static final Dimension ComboBoxPrefSize = new Dimension(60,15);
    private JComboBox<String> renotifyComboBox;
    private final JButton testsound = new LinkButton("Test Sound");
    private final JButton testpopup = new LinkButton("Test Popup");
    private JComboBox<String> popupsecsComboBox;
    private final JPanel panel = new JPanel();
    private final String[] secslist = new String[60];
    private final String[] minslist = new String[20];
    private final int popupsecs = 5;
    private final String alerttype = "";
    private final JCheckBox audiocheckbox = new JCheckBox("Audio");
    private final JCheckBox popupcheckbox = new JCheckBox("Popup");
    private final JComboBox<SoundBoxItem> soundSrc = new LightComboBox<>();
    private final SoundBoxItem customerSoundItem = new SoundBoxItem("","");
    private final SoundBoxItem defaultSoundItem = new SoundBoxItem(DefaultSoundFileDisp, LineSeekerAlertMethodAttr.DefaultSoundFilePath);
    private final SoundBoxItem uploadSoundItem = new SoundBoxItem("Upload Sound File","");
    private final JLabel editStatusLabel = new JLabel();
    private final LineSeekerAlertMethodAttr attr;
    private final PopupLocationConfig popupLocationConfig = new PopupLocationConfig();

    public LineSeekerAlertMethodStateLayout(LineSeekerAlertMethodAttr attr) {
        this.attr = attr;
    }
    public JComponent getLayoutPane(SpankyWindow spankyWindow) {

        initComponents(spankyWindow);
        JLabel renotifyme = new JLabel("Renotify me on same Game only after");
        JLabel titleLabel = new JLabel(attr.getAlertState());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(editStatusLabel,BorderLayout.EAST);
        titlePanel.add(titleLabel,BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,1,20,1);

        //0. title row
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor =  GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(titlePanel, c);

        //1. audio row
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor =  GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
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
        panel.add(new JLabel(" at "));
        panel.add(popupLocationConfig.getUserComponent());
        panel.add(new JLabel(" for "));
        panel.add(popupsecsComboBox);
        panel.add(new JLabel(" seconds"));
        panel.add(Box.createRigidArea(TestButtonSpacing));
        panel.add(testpopup);
        return panel;
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

        for(int i=0;i<20;i++) {
            minslist[i]=String.valueOf(0.5+0.5*i);
        }

        LookAndFeelFactory.installJideExtension();

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

        testsound.addActionListener(ae -> {
            try {
                SoundPlayer.playSound(((SoundBoxItem) Objects.requireNonNull(soundSrc.getSelectedItem())).path);
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

        soundSrc.addItem(defaultSoundItem);
        soundSrc.addItem(customerSoundItem);
        soundSrc.addItem(uploadSoundItem);
        soundSrc.addItemListener(this::onSoundFileChanged);

        // add listeners
        audiocheckbox.addItemListener(this::onItemChanged);
        soundSrc.addItemListener(this::onItemChanged);
        popupcheckbox.addItemListener(this::onItemChanged);
        popupsecsComboBox.addItemListener(this::onItemChanged);
        renotifyComboBox.addItemListener(this::onItemChanged);
        popupLocationConfig.setPopupLocationListener(this::onPopupLocationChanged);
        //
    }
    private void onPopupLocationChanged(LineSeekerAlertMethodAttr.PopupLocation popupLocation) {
        checkAndSetEditStatus();
    }
    private void onItemChanged(ItemEvent ie) {
        checkAndSetEditStatus();
    }
    private void onSoundFileChanged(ItemEvent ie) {

        if (ie.getStateChange() == ItemEvent.SELECTED) {
            Object item = ie.getItem();
            if ( uploadSoundItem.equals(item)) {
                JFileChooser jfc = new JFileChooser();
                jfc.showOpenDialog(SpankyWindow.getFirstSpankyWindow());
                File f1 = jfc.getSelectedFile();
                if ( null != f1) {
                    String uploadedFilePath = f1.getPath();
                    customerSoundItem.setPath(uploadedFilePath);
                    soundSrc.setSelectedItem(customerSoundItem);
                }
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
    public void updateAlertMethodAttr() {
        audiocheckbox.setSelected(attr.getAudioEnabled());
        SoundBoxItem selectedItem;
        if ( defaultSoundItem.path.equals(attr.getSoundFile()) ||  uploadSoundItem.path.equals(attr.getSoundFile())) {
            selectedItem = defaultSoundItem;
        } else {
            selectedItem = customerSoundItem;
            selectedItem.setPath(attr.getSoundFile());
        }
        soundSrc.setSelectedItem(selectedItem);
        popupcheckbox.setSelected(attr.getPopupEnabled());
        popupsecsComboBox.setSelectedItem(attr.getPopupSeconds());
        renotifyComboBox.setSelectedItem(attr.getRenotifyInMinutes());
        popupLocationConfig.setSelectedPopupLocation(attr.getPopupLocation());
        setEdited(false);
    }
    public void saveMethodAttr() {
        attr.setAudioEnabled(audiocheckbox.isSelected());
        SoundBoxItem selectedSoundItem = (SoundBoxItem)soundSrc.getSelectedItem();
        attr.setSoundFile(selectedSoundItem.path);
        attr.setPopupEnabled(popupcheckbox.isSelected());
        attr.setPopupSeconds(String.valueOf(popupsecsComboBox.getSelectedItem()));
        attr.setRenotifyInMinutes(String.valueOf(renotifyComboBox.getSelectedItem()));
        attr.setPopupLocation(popupLocationConfig.getSelectedPopupLocation());
        setEdited(false);
    }
    private void checkAndSetEditStatus() {
        SoundBoxItem selectedSoundItem = (SoundBoxItem)soundSrc.getSelectedItem();
        boolean isEdited = attr.getAudioEnabled() != audiocheckbox.isSelected()
                || ! attr.getSoundFile().equals(selectedSoundItem.path)
                || attr.getPopupEnabled() != popupcheckbox.isSelected()
                || ! attr.getPopupSeconds().equals(popupsecsComboBox.getSelectedItem())
                || ! attr.getRenotifyInMinutes().equals(renotifyComboBox.getSelectedItem())
                || attr.getPopupLocation() != popupLocationConfig.getSelectedPopupLocation();

        setEdited(isEdited);
    }
    public boolean isEdited() {
        return SiaConst.EditedIndicator.equals(editStatusLabel.getText());
    }
    public void setEdited(boolean edited) {
        if ( edited) {
            editStatusLabel.setText(SiaConst.EditedIndicator);
        } else {
            editStatusLabel.setText("  ");
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    private static class SoundBoxItem {
        private String path;
        private String display;
        public SoundBoxItem(String display,String path) {
            this.path = path;
            this.display = display;
        }
        @Override
        public String toString() {
            return display;
        }
        public void setPath(String path) {
            if ( ! DefaultSoundFileDisp.equals(path)) {
                this.path = path;
                String[] parts = path.split("\\\\|/");
                this.display = parts[parts.length - 1];
            } else {
                this.display = DefaultSoundFileDisp;
                this.path = LineSeekerAlertMethodAttr.DefaultSoundFilePath;
            }
        }
    }
    public static void main(String [] argv) {
        String path = "/abc/123/456/efg/test.txt";
        SoundBoxItem test= new SoundBoxItem("TEST",path);
        System.out.println(test);
    }
}

