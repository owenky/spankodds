package com.sia.client.ui;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;
import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Bookie;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;
import com.sia.client.model.UserDisplaySettings;
import com.sia.client.ui.comps.PopupLocationConfig;
import com.sia.client.ui.control.SportsTabPane;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.*;
import java.util.List;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class UserDisplayGui extends AbstractLayeredDialog implements ActionListener
{


    String footballdefault;
    String basketballdefault;
    String baseballdefault;
    String hockeydefault;
    String fightingdefault;
    String soccerdefault;
    String autoracingdefault;
    String golfdefault;
    String tennisdefault;
    int firstmoveseconds;
    int secondmoveseconds;
    Color firstcolor;
    Color secondcolor;
    Color thirdcolor;

    private static final String ximage="blocking.jpg";
    private static final String checkimage="unblocking.jpg";

    String[] secslist = new String[120];

    private Box box1 = Box.createVerticalBox();
    private Box box2 = Box.createVerticalBox();
    private Box box3 = Box.createVerticalBox();
    ColorChooserButton firstcolorChooserButton;
    ColorChooserButton secondcolorChooserButton;
    ColorChooserButton thirdcolorChooserButton;


    JComboBox firstmovesecs;
    JComboBox secondmovesecs;



    JComboBox footballcb;
    JComboBox basketballcb;
    JComboBox baseballcb;
    JComboBox hockeycb;
    JComboBox fightingcb;
    JComboBox soccercb;
    JComboBox autoracingcb;
    JComboBox golfcb;
    JComboBox tenniscb;

    JButton savebutton =  new JButton("Save");



    private static final MatteBorder bestborder = new MatteBorder(2, 1, 2, 1, new Color(51,0, 0));
    private String[] display = new String[8];
    private String[] display2 = new String[8];


    JToggleButton autofitcolumnsbutton = new JToggleButton("autofitcolumns",checkimage,"Click to stop columns from autofitting",ximage,"Click to enable columns to auto fit");
    JToggleButton showcpotooltipbutton = new JToggleButton("showcpotooltip",checkimage,"Click to disbale C/P/O line history",ximage,"Click to enabale C/P/O line history");
    JToggleButton showlinedirectionmovebuttom = new JToggleButton("showlinedirectionmove",checkimage,"Click to disbale line direction move",ximage,"Click to enable line direction move");
    JToggleButton borderbestbutton = new JToggleButton("borderbest",checkimage,"Click to disbale bordering best line",ximage,"Click to enable bordering best line");

    public UserDisplayGui(SportsTabPane stp) {
        super(stp,"Display Settings");
    }
    @Override
    protected JComponent getUserComponent() {
        savebutton.addActionListener(this);
        footballdefault = UserDisplaySettings.getFootballdefault();
        basketballdefault = UserDisplaySettings.getBasketballdefault();
        baseballdefault = UserDisplaySettings.getBaseballdefault();
        hockeydefault = UserDisplaySettings.getHockeydefault();
        fightingdefault = UserDisplaySettings.getFightingdefault();
        soccerdefault = UserDisplaySettings.getSoccerdefault();
        autoracingdefault = UserDisplaySettings.getAutoracingdefault();
        golfdefault = UserDisplaySettings.getGolfdefault();
        tennisdefault = UserDisplaySettings.getTennisdefault();
        firstmoveseconds = UserDisplaySettings.getFirstmoveseconds();
        secondmoveseconds = UserDisplaySettings.getSecondmoveseconds();
        firstcolor = UserDisplaySettings.getFirstcolor();
        secondcolor = UserDisplaySettings.getSecondcolor();
        thirdcolor = UserDisplaySettings.getThirdcolor();

        firstcolorChooserButton = new ColorChooserButton(firstcolor);
        secondcolorChooserButton = new ColorChooserButton(secondcolor);
        thirdcolorChooserButton = new ColorChooserButton(thirdcolor);

        autofitcolumnsbutton.setEnable(UserDisplaySettings.isAutofitdata());
        showcpotooltipbutton.setEnable(UserDisplaySettings.isShowcpo());
        showlinedirectionmovebuttom.setEnable(UserDisplaySettings.isShowdirectionicons());
        borderbestbutton.setEnable(UserDisplaySettings.isShowborderbestline());

        display[0] = "spreadtotal";
        display[1] = "totalmoney";
        display[2] = "totalbothmoney";
        display[3] = "justspread";
        display[4] = "justtotal";
        display[5] = "justmoney";
        display[6] = "awayteamtotal";
        display[7] = "hometeamtotal";


        display2[0] = "Sides & Totals";
        display2[1] = "Totals & ML";
        display2[2] = "Totals & Both ML";
        display2[3] = "Just Sides";
        display2[4] = "Just Totals";
        display2[5] = "Just ML";
        display2[6] = "Away TT";
        display2[7] = "Home TT";

        for (int v = 1; v <= 120; v++) {
            secslist[v - 1] = v + "";
        }




        firstmovesecs = new JComboBox(secslist);
        secondmovesecs = new JComboBox(secslist);

        footballcb = new JComboBox(display2);
        basketballcb = new JComboBox(display2);
        baseballcb = new JComboBox(display2);
        hockeycb = new JComboBox(display2);
        fightingcb = new JComboBox(display2);
        soccercb = new JComboBox(display2);
        autoracingcb = new JComboBox(display2);
        golfcb = new JComboBox(display2);
        tenniscb = new JComboBox(display2);

        firstmovesecs.setSelectedIndex(firstmoveseconds-1);
        secondmovesecs.setSelectedIndex(secondmoveseconds-1);


        List<String> abcd  = Arrays.asList(display);
        footballcb.setSelectedIndex(abcd.indexOf(footballdefault));
        basketballcb.setSelectedIndex(abcd.indexOf(basketballdefault));
        baseballcb.setSelectedIndex(abcd.indexOf(baseballdefault));
        hockeycb.setSelectedIndex(abcd.indexOf(hockeydefault));
        fightingcb.setSelectedIndex(abcd.indexOf(fightingdefault));
        soccercb.setSelectedIndex(abcd.indexOf(soccerdefault));
        autoracingcb.setSelectedIndex(abcd.indexOf(autoracingdefault));
        golfcb.setSelectedIndex(abcd.indexOf(golfdefault));
        tenniscb.setSelectedIndex(abcd.indexOf(tennisdefault));








        LookAndFeelFactory.installJideExtension();
        // Create a new JFrame container.
        JFrame jfrm1 = SpankyWindow.findSpankyWindow(getAnchoredLayeredPane().getSportsTabPane().getWindowIndex());
        JPanel userComponent = new JPanel();
        userComponent.setLayout(new FlowLayout());

        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();










        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setLayout(new GridBagLayout());

        panel1.setBorder(BorderFactory.createEtchedBorder());
        panel1.setLayout(new GridBagLayout());

        panel2.setBorder(BorderFactory.createEtchedBorder());
        panel2.setLayout(new GridBagLayout());







        Border blackline = BorderFactory.createLineBorder(Color.black);
        GridBagConstraints c = new GridBagConstraints();

        //periods  checkboxes
        JLabel autofitcolumns = new JLabel("  Columns automatically fit data?");
        JLabel showcurrentpreviousopenertooltip = new JLabel("  Show Curr/Prev/Open on MouseOver?");
        JLabel showlinemovedirection = new JLabel("  Show Line Move Direction Icons?");
        JLabel borderbest = new JLabel("  Show Border for Best Line?");





        JLabel uplabel = new JLabel( Utils.getImageIcon(SiaConst.ImageFile.ICON_UP));
        JLabel downlabel = new JLabel( Utils.getImageIcon(SiaConst.ImageFile.ICON_DOWN));
        JPanel updownpanel = new JPanel();
        updownpanel.setBackground(Color.BLACK);
        updownpanel.add(uplabel);
        updownpanel.add(downlabel);


        JLabel besttoplabel = new JLabel("41o05");
        JLabel bestbottomlabel = new JLabel("7");
        besttoplabel.setBorder(bestborder);
        bestbottomlabel.setBorder(bestborder);
        JPanel bestpanel = new JPanel();
        bestpanel.setBackground(Color.WHITE);
        bestpanel.setLayout(new GridLayout(2, 0));
        bestpanel.add(besttoplabel);
        bestpanel.add(bestbottomlabel);

        JLabel sportlabel = new JLabel("Sport");
        JLabel defaultvaluelabel = new JLabel("Default Value");
        JLabel football = new JLabel("Football");
        JLabel basketball = new JLabel("Basketball");
        JLabel baseball = new JLabel("Baseball");
        JLabel hockey = new JLabel("Hockey");
        JLabel fighting = new JLabel("Fighting");
        JLabel soccer = new JLabel("Soccer");
        JLabel autoracing = new JLabel("Auto Racing");
        JLabel golf = new JLabel("Golf");
        JLabel tennis = new JLabel("Tennis");


        JLabel firstcolorlabel = new JLabel("New Change Color - ");
        JLabel secondcolorpart1 = new JLabel("Then After ");
        JLabel secondcolorpart2 = new JLabel(" seconds, Use Color - ");
        JLabel thirdcolorpart1 = new JLabel("Finally, After ");
        JLabel thirdcolorpart2 = new JLabel("seconds, Use Color - ");


/*
        autofitcolumnsbutton.setBorder(blackline);
        showcpotooltipbutton.setBorder(blackline);
        showlinedirectionmovebuttom.setBorder(blackline);
        borderbestbutton.setBorder(blackline);
*/
        GridBagConstraints c1 = new GridBagConstraints();


        c1.gridheight = 2;


        c1.gridy = 0;
        c1.gridx = 0;
        c1.gridwidth = 4;
        panel1.add(sportlabel, c1);
        c1.gridx = 5;
        c1.gridwidth = 4;
        panel1.add(defaultvaluelabel, c1);

        c1.anchor = GridBagConstraints.WEST;

        c1.gridy = 2;
        c1.gridx = 0;
        c1.gridwidth = 4;
        panel1.add(football, c1);
        c1.gridx = 5;
        c1.gridwidth =4;
        panel1.add(footballcb, c1);


        c1.gridy = 4;
        c1.gridx = 0;
        c1.gridwidth = 4;
        panel1.add(basketball, c1);
        c1.gridx = 5;
        c1.gridwidth = 4;
        panel1.add(basketballcb, c1);

        c1.gridy = 6;
        c1.gridx = 0;
        c1.gridwidth = 4;
        panel1.add(baseball, c1);
        c1.gridx = 5;
        c1.gridwidth = 4;
        panel1.add(baseballcb, c1);

        c1.gridy = 8;
        c1.gridx = 0;
        c1.gridwidth = 4;
        panel1.add(hockey, c1);
        c1.gridx = 5;
        c1.gridwidth = 4;
        panel1.add(hockeycb, c1);

        c1.gridy = 10;
        c1.gridx = 0;
        c1.gridwidth = 4;
        panel1.add(fighting, c1);
        c1.gridx = 5;
        c1.gridwidth = 4;
        panel1.add(fightingcb, c1);

        c1.gridy = 12;
        c1.gridx = 0;
        c1.gridwidth = 4;
        panel1.add(soccer, c1);
        c1.gridx = 5;
        c1.gridwidth = 4;
        panel1.add(soccercb, c1);

        c1.gridy = 14;
        c1.gridx = 0;
        c1.gridwidth = 4;
        panel1.add(autoracing, c1);
        c1.gridx = 5;
        c1.gridwidth = 4;
        panel1.add(autoracingcb, c1);

        c1.gridy = 16;
        c1.gridx = 0;
        c1.gridwidth = 4;
        panel1.add(golf, c1);
        c1.gridx = 5;
        c1.gridwidth = 4;
        panel1.add(golfcb, c1);

        c1.gridy = 18;
        c1.gridx = 0;
        c1.gridwidth = 4;
        panel1.add(tennis, c1);
        c1.gridx = 5;
        c1.gridwidth = 4;
        panel1.add(tenniscb, c1);













        c.anchor = GridBagConstraints.WEST;
        c.gridheight = 2;
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 2;
        panel2.add(autofitcolumnsbutton, c);
        c.gridx = 4;
        c.gridwidth = 4;
        panel2.add(autofitcolumns, c);

        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 2;
        panel2.add(showcpotooltipbutton, c);
        c.gridx = 4;
        c.gridwidth = 4;
        panel2.add(showcurrentpreviousopenertooltip, c);




        c.gridy = 4;
        c.gridx = 0;
        c.gridwidth = 2;
        panel2.add(showlinedirectionmovebuttom, c);
        c.gridx = 4;
        c.gridwidth = 3;
        panel2.add(showlinemovedirection, c);
        c.gridx = 7;
        c.gridwidth = 1;
        panel2.add(updownpanel, c);

        c.gridy = 6;
        c.gridx = 0;
        c.gridwidth = 2;
        panel2.add(borderbestbutton, c);
        c.gridx = 4;
        c.gridwidth = 3;
        panel2.add(borderbest, c);
        c.gridx = 7;
        c.gridwidth = 1;
        panel2.add(bestpanel, c);

        GridBagConstraints c2 = new GridBagConstraints();


        c2.gridheight = 2;

        // c2.anchor = GridBagConstraints.EAST;

        c2.gridy = 0;
        c2.gridx = 5;
        c2.gridwidth = 4;
        panel.add(firstcolorlabel, c2);
        c2.anchor = GridBagConstraints.WEST;
        c2.gridx = 9;
        c2.gridwidth = 1;
        panel.add(firstcolorChooserButton, c2);

        c2.gridy = 2;
        c2.gridx = 0;
        c2.gridwidth = 3;
        panel.add(secondcolorpart1, c2);
        c2.gridx = 4;
        c2.gridwidth = 1;
        panel.add(firstmovesecs, c2);
        c2.gridx = 5;
        c2.gridwidth = 4;
        panel.add(secondcolorpart2 , c2);
        c2.gridx = 9;
        c2.gridwidth = 1;
        panel.add(secondcolorChooserButton  , c2);

        c2.gridy = 4;
        c2.gridx = 0;
        c2.gridwidth = 3;
        panel.add(thirdcolorpart1 , c2);
        c2.gridx = 4;
        c2.gridwidth = 1;
        panel.add(secondmovesecs , c2);
        c2.gridx = 5;
        c2.gridwidth = 4;
        panel.add(thirdcolorpart2  , c2);
        c2.gridx = 9;
        c2.gridwidth = 1;
        panel.add(thirdcolorChooserButton  , c2);




        //************************end of panel*******************


        // Create three vertical boxes.


        // Create invisible borders around the boxes.
        box1.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box2.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box3.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        autofitcolumnsbutton.addActionListener(this);
        showcpotooltipbutton.addActionListener(this);
        showlinedirectionmovebuttom.addActionListener(this);
        borderbestbutton.addActionListener(this);
        box1.add(new JLabel("Default Line Display                               "));
        box1.add(panel1);
        box2.add(new JLabel("Line Yes/No Display Options                                        "));
        box2.add(panel2);
        box2.add(new JLabel("Color Line Moves                                        "));
        box2.add(panel);
        box2.add(savebutton);
        // Add the boxes to the content pane.
        userComponent.add(box1);
        userComponent.add(box2);

        firstcolorChooserButton.addColorChangedListener(new ColorChangedListener() {
            @Override
            public void colorChanged(Color newColor)
            {
                System.out.println("new first color is "+newColor);
            }
        });




        return userComponent;
//*******end of cons***********
    }







    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == savebutton)
        {
            UserDisplaySettings.setFootballdefault(display[footballcb.getSelectedIndex()]);
            UserDisplaySettings.setBasketballdefault(display[basketballcb.getSelectedIndex()]);
            UserDisplaySettings.setBaseballdefault(display[baseballcb.getSelectedIndex()]);
            UserDisplaySettings.setHockeydefault(display[hockeycb.getSelectedIndex()]);
            UserDisplaySettings.setFightingdefault(display[fightingcb.getSelectedIndex()]);
            UserDisplaySettings.setSoccerdefault(display[soccercb.getSelectedIndex()]);
            UserDisplaySettings.setAutoracingdefault(display[autoracingcb.getSelectedIndex()]);
            UserDisplaySettings.setGolfdefault(display[golfcb.getSelectedIndex()]);
            UserDisplaySettings.setTennisdefault(display[tenniscb.getSelectedIndex()]);

            UserDisplaySettings.setFirstmoveseconds(Integer.parseInt(""+firstmovesecs.getSelectedItem()));
            UserDisplaySettings.setSecondmoveseconds(Integer.parseInt(""+secondmovesecs.getSelectedItem()));


            UserDisplaySettings.setFirstcolor(firstcolorChooserButton.getSelectedColor());
            UserDisplaySettings.setSecondcolor(secondcolorChooserButton.getSelectedColor());
            UserDisplaySettings.setThirdcolor(thirdcolorChooserButton.getSelectedColor());

            UserDisplaySettings.setAutofitdata(autofitcolumnsbutton.isEnabled());
            UserDisplaySettings.setShowcpo(showcpotooltipbutton.isEnabled());
            UserDisplaySettings.setShowdirectionicons(showlinedirectionmovebuttom.isEnabled());
            UserDisplaySettings.setShowborderbestline(borderbestbutton.isEnabled());



            close();


        }
        if(e.getSource() instanceof JToggleButton)
        {
            ((JToggleButton)e.getSource()).toggle();
        }
    }





}

