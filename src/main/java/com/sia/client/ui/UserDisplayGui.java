package com.sia.client.ui;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.sia.client.config.Config;
import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.PropItem;
import com.sia.client.model.UserDisplaySettings;
import com.sia.client.ui.comps.PropItemComboBox;
import com.sia.client.ui.comps.SbtStringComboBox;
import com.sia.client.ui.comps.UICompValueBinder;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import static com.sia.client.config.Utils.log;

public class UserDisplayGui extends AbstractLayerFrame {

    private static final String ximage = "blocking.jpg";
    private static final String checkimage = "unblocking.jpg";
    private static final int maxSecs = 120;
    private static final String[] secArr = new String[maxSecs];

    private Box box1 = Box.createVerticalBox();
    private Box box2 = Box.createVerticalBox();
    private Box box3 = Box.createVerticalBox();
    private ColorChooserButton firstcolorChooserButton;
    private ColorChooserButton secondcolorChooserButton;
    private ColorChooserButton thirdcolorChooserButton;

    private ColorChooserButton altcolorChooserButton;
    private ColorChooserButton openercolorChooserButton;
    private ColorChooserButton lastcolorChooserButton;

    private ColorChooserButton rowhighlightcolorChooserButton;


    private SbtStringComboBox firstmovesecs;
    private SbtStringComboBox secondmovesecs;


    private PropItemComboBox footballcb;
    private PropItemComboBox  basketballcb;
    private PropItemComboBox  baseballcb;
    private PropItemComboBox  hockeycb;
    private PropItemComboBox  fightingcb;
    private PropItemComboBox  soccercb;
    private PropItemComboBox  autoracingcb;
    private PropItemComboBox  golfcb;
    private PropItemComboBox  tenniscb;

    private final JButton savebutton = new JButton("Save");

    private final JButton clearcolorbutton = new JButton("Clear Column Colors");


    private static final MatteBorder bestborder = new MatteBorder(2, 1, 2, 1, new Color(51, 0, 0));
    private static final PropItem[] items =  new PropItem[] {
            new PropItem( "spreadtotal","Sides & Totals"),
            new PropItem( "totalmoney","Totals & ML"),
            new PropItem( "totalbothmoney","Totals & Both ML"),
            new PropItem( "justspread","Just Sides"),
            new PropItem( "justtotal","Just Totals"),
            new PropItem( "justmoney","Just ML"),
            new PropItem( "awayteamtotal","Away TT"),
            new PropItem( "hometeamtotal","Home TT")
    };
    JToggleButton autofitcolumnsbutton = new JToggleButton("autofitcolumns", checkimage, "Click to stop columns from autofitting", ximage, "Click to enable columns to auto fit");
    JToggleButton showcpotooltipbutton = new JToggleButton("showcpotooltip", checkimage, "Click to disable C/P/O line history", ximage, "Click to enable C/P/O line history");
    JToggleButton showlinedirectionmovebuttom = new JToggleButton("showlinedirectionmove", checkimage, "Click to disable line direction move", ximage, "Click to enable line direction move");


    JToggleButton borderbestbutton = new JToggleButton("borderbest", checkimage, "Click to disable bordering best line", ximage, "Click to enable bordering best line");
    JToggleButton altcolorbutton = new JToggleButton("altcolor", checkimage, "Click to disable altering coloring rows", ximage, "Click to enable altering coloring rows");

    JToggleButton soccerquarterlinebutton = new JToggleButton("soccerquarterlines", checkimage, "Click to show .25 and .75 instead of \u00BC and \u00BE", ximage, "Click to show \u00BC and \u00BE instead of .25 and .75");

    private final UserDisplaySettings userDisplaySettings = Config.instance().getUserDisplaySettings();
    private final JLabel editStatusLabel = new JLabel();

    static {
        for ( int i=1;i<=maxSecs;i++) {
            secArr[i-1]=String.valueOf(i);
        }
    }

    public UserDisplayGui(SportsTabPane stp) {
        super(stp, "Display Settings");
        init();
    }
    private void init() {
        savebutton.addActionListener(this::save);

        clearcolorbutton.addActionListener(ae -> {
            try {
                int result = JOptionPane.showConfirmDialog(null,"Are You Sure You Would Like To Clear All Column Colors?","Column Colors",JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION)
                {
                    AppController.clearColumnColors();
                }


            } catch (Exception e)
            {
                log("Error clear colors.."+e);

            }

        });
        firstcolorChooserButton = new ColorChooserButton(Color.RED);
        secondcolorChooserButton = new ColorChooserButton(Color.RED);
        thirdcolorChooserButton = new ColorChooserButton(Color.RED);
        altcolorChooserButton= new ColorChooserButton(Color.RED);
        openercolorChooserButton= new ColorChooserButton(Color.RED);
        lastcolorChooserButton= new ColorChooserButton(Color.RED);
        rowhighlightcolorChooserButton= new ColorChooserButton(Color.RED);

        firstmovesecs = new SbtStringComboBox(secArr);
        secondmovesecs = new SbtStringComboBox(secArr);

        footballcb = new PropItemComboBox(items);
        basketballcb = new PropItemComboBox(items);
        baseballcb = new PropItemComboBox(items);
        hockeycb = new PropItemComboBox(items);
        fightingcb = new PropItemComboBox(items);
        soccercb = new PropItemComboBox(items);
        autoracingcb = new PropItemComboBox(items);
        golfcb = new PropItemComboBox(items);
        tenniscb = new PropItemComboBox(items);
    }
    @Override
    protected JComponent getUserComponent() {

        LookAndFeelFactory.installJideExtension();
        // Create a new JFrame container.
        JFrame jfrm1 = SpankyWindow.findSpankyWindow(getAnchoredLayeredPane().getSportsTabPane().getWindowIndex());
        JPanel userComponent = new JPanel();
        userComponent.setLayout(new FlowLayout());

        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panelempty = new JPanel();


        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setLayout(new GridBagLayout());

        panel1.setBorder(BorderFactory.createEtchedBorder());
        panel1.setLayout(new GridBagLayout());

        panel2.setBorder(BorderFactory.createEtchedBorder());
        panel2.setLayout(new GridBagLayout());


        panel3.setBorder(BorderFactory.createEtchedBorder());
        panel3.setLayout(new GridBagLayout());

        panel4.setBorder(BorderFactory.createEtchedBorder());
        panel4.setLayout(new GridBagLayout());

        panelempty.setLayout(new GridBagLayout());



        Border blackline = BorderFactory.createLineBorder(Color.black);
        GridBagConstraints c = new GridBagConstraints();

        //periods  checkboxes
        JLabel autofitcolumns = new JLabel("  Columns automatically fit data?");
        JLabel showcurrentpreviousopenertooltip = new JLabel("  Show Curr/Prev/Open on MouseOver?");
        JLabel showlinemovedirection = new JLabel("  Show Line Move Direction Icons?");
        JLabel borderbest = new JLabel("  Show Border for Best Line?");

        JLabel altcolorlabel = new JLabel("Have Rows Alternating Color? - ");

        JLabel soccerquarterlabel = new JLabel("Display \u00BC and \u00BE for Soccer");


        JLabel uplabel = new JLabel(Utils.getImageIcon(SiaConst.ImageFile.ICON_UP));
        JLabel downlabel = new JLabel(Utils.getImageIcon(SiaConst.ImageFile.ICON_DOWN));
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
        c1.gridwidth = 4;
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


        GridBagConstraints c8 = new GridBagConstraints();


        c8.gridheight = 2;


        c8.anchor = GridBagConstraints.CENTER;
        c8.gridy = 0;
        c8.gridx = 0;
        c8.gridwidth = 4;
        panel4.add(clearcolorbutton, c8);



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

        c.gridy = 8;
        c.gridx = 0;
        c.gridwidth = 2;
        panel2.add(altcolorbutton, c);
        c.gridx = 4;
        c.gridwidth = 3;
        panel2.add(altcolorlabel, c);
        c.gridx = 7;
        c.gridwidth = 1;
        panel2.add(altcolorChooserButton, c);

        c.gridy = 10;
        c.gridx = 0;
        c.gridwidth = 2;
        panel2.add(soccerquarterlinebutton, c);
        c.gridx = 4;
        c.gridwidth = 3;
        panel2.add(soccerquarterlabel, c);



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
        panel.add(secondcolorpart2, c2);
        c2.gridx = 9;
        c2.gridwidth = 1;
        panel.add(secondcolorChooserButton, c2);

        c2.gridy = 4;
        c2.gridx = 0;
        c2.gridwidth = 3;
        panel.add(thirdcolorpart1, c2);
        c2.gridx = 4;
        c2.gridwidth = 1;
        panel.add(secondmovesecs, c2);
        c2.gridx = 5;
        c2.gridwidth = 4;
        panel.add(thirdcolorpart2, c2);
        c2.gridx = 9;
        c2.gridwidth = 1;
        panel.add(thirdcolorChooserButton, c2);


        JLabel openercolorlabel = new JLabel("Opener Button Color - ");
        JLabel lastcolorlabel = new JLabel("Last Button Color - ");
        JLabel rowhighlightcolorlabel = new JLabel("Row Highlight Color - ");
        GridBagConstraints c3 = new GridBagConstraints();


        c3.gridheight = 2;

        // c2.anchor = GridBagConstraints.EAST;

        c3.gridy = 0;
        c3.gridx = 0;
        c3.gridwidth = 4;
        panel3.add(openercolorlabel, c3);
        c3.anchor = GridBagConstraints.WEST;
        c3.gridx = 5;
        c3.gridwidth = 1;
        panel3.add(openercolorChooserButton, c3);

        c3.gridy = 2;
        c3.gridx = 0;
        c3.gridwidth = 4;
        panel3.add(lastcolorlabel, c3);
        c3.anchor = GridBagConstraints.WEST;
        c3.gridx = 5;
        c3.gridwidth = 1;
        panel3.add(lastcolorChooserButton, c3);

        c3.gridy = 4;
        c3.gridx = 0;
        c3.gridwidth = 4;
        panel3.add(rowhighlightcolorlabel, c3);
        c3.anchor = GridBagConstraints.WEST;
        c3.gridx = 5;
        c3.gridwidth = 1;
        panel3.add(rowhighlightcolorChooserButton, c3);

        GridBagConstraints cempty = new GridBagConstraints();
        cempty.gridy = 0;
        cempty.gridx = 0;
        cempty.gridwidth = 4;
        cempty.gridheight = 15;
        panelempty.add(new JLabel("                                            "), cempty);
        cempty.gridy = 15;
        cempty.gridx = 0;
        panelempty.add(new JLabel("                                            "), cempty);


        //************************end of panel*******************


        // Create three vertical boxes.


        // Create invisible borders around the boxes.
        box1.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box2.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box3.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        box1.add(new JLabel("Default Line Display                               "));
        box1.add(panel1);
        box1.add(panelempty);
        box1.add(panelempty);
        box1.add(panelempty);
        box1.add(panelempty);
        box1.add(panelempty);

        box1.add(panel4);
        box2.add(new JLabel("Line Yes/No Display Options                                        "));
        box2.add(panel2);
        box2.add(new JLabel("Color Line Moves                                        "));
        box2.add(panel);
        box2.add(new JLabel("Colors                                                  "));
        box2.add(panel3);
        box2.add(savebutton);
        // Add the boxes to the content pane.
        userComponent.add(box1);
        userComponent.add(box2);

        return userComponent;
//*******end of cons***********
    }
    @Override
    public JLabel getEditStatusLabel() {
        return editStatusLabel;
    }
    @Override
    protected void bindProperties(UICompValueBinder uiCompValueBinder) {
        uiCompValueBinder.withPersistenceObject(userDisplaySettings)
                .bindCompProp("autofitdata",autofitcolumnsbutton)
                .bindCompProp("showcpo",showcpotooltipbutton)
                .bindCompProp("showdirectionicons",showlinedirectionmovebuttom)
                .bindCompProp("showborderbestline",borderbestbutton)
                .bindCompProp("showaltcolor",altcolorbutton)
                .bindCompProp("firstcolor",firstcolorChooserButton)
                .bindCompProp("secondcolor",secondcolorChooserButton)
                .bindCompProp("thirdcolor",thirdcolorChooserButton)
                .bindCompProp("footballdefault",footballcb)
                .bindCompProp("basketballdefault",basketballcb)
                .bindCompProp("baseballdefault",baseballcb)
                .bindCompProp("hockeydefault",hockeycb)
                .bindCompProp("fightingdefault",fightingcb)
                .bindCompProp("soccerdefault",soccercb)
                .bindCompProp("autoracingdefault",autoracingcb)
                .bindCompProp("golfdefault",golfcb)
                .bindCompProp("tennisdefault",tenniscb)
                .bindCompProp("firstmoveseconds",firstmovesecs)
                .bindCompProp("secondmoveseconds",secondmovesecs)
                .bindCompProp("altcolor",altcolorChooserButton)
                .bindCompProp("openercolor",openercolorChooserButton)
                .bindCompProp("rowhighlightcolor",rowhighlightcolorChooserButton)
                .bindCompProp("soccerquarter",soccerquarterlinebutton)
                .bindCompProp("lastcolor",lastcolorChooserButton);

    }

}

