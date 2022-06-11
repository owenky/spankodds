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
import java.awt.*;

public class UserDisplayGui extends AbstractLayerFrame {

    private static final String ximage = "blocking.jpg";
    private static final String checkimage = "unblocking.jpg";
    private static final int maxSecs = 120;
    private static final String[] secsArr = new String[maxSecs];

    private Box box1 = Box.createVerticalBox();
    private Box box2 = Box.createVerticalBox();
    private Box box3 = Box.createVerticalBox();
    private ColorChooserButton firstcolorChooserButton;
    private ColorChooserButton secondcolorChooserButton;
    private ColorChooserButton thirdcolorChooserButton;

    private SbtStringComboBox firstmovesecs;
    private SbtStringComboBox secondmovesecs;

    private PropItemComboBox footballcb;
    private PropItemComboBox basketballcb;
    private PropItemComboBox baseballcb;
    private PropItemComboBox hockeycb;
    private PropItemComboBox fightingcb;
    private PropItemComboBox soccercb;
    private PropItemComboBox autoracingcb;
    private PropItemComboBox golfcb;
    private PropItemComboBox tenniscb;

    private final JButton savebutton = new JButton("Save");

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


    private JToggleButton autofitcolumnsbutton = new JToggleButton("autofitcolumns", checkimage, "Click to stop columns from autofitting", ximage, "Click to enable columns to auto fit");
    private JToggleButton showcpotooltipbutton = new JToggleButton("showcpotooltip", checkimage, "Click to disbale C/P/O line history", ximage, "Click to enabale C/P/O line history");
    private JToggleButton showlinedirectionmovebuttom = new JToggleButton("showlinedirectionmove", checkimage, "Click to disbale line direction move", ximage, "Click to enable line direction move");
    private JToggleButton borderbestbutton = new JToggleButton("borderbest", checkimage, "Click to disbale bordering best line", ximage, "Click to enable bordering best line");

    private final UserDisplaySettings userDisplaySettings = Config.instance().getUserDisplaySettings();
    private final JLabel editStatusLabel = new JLabel();

    static {
        for ( int i=1;i<=maxSecs;i++) {
            secsArr[i-1]=String.valueOf(i);
        }
    }

    public UserDisplayGui(SportsTabPane stp) {
        super(stp, "Display Settings");
        init();
    }
    private void init() {
        savebutton.addActionListener(this::save);
        firstcolorChooserButton = new ColorChooserButton(Color.RED);
        secondcolorChooserButton = new ColorChooserButton(Color.RED);
        thirdcolorChooserButton = new ColorChooserButton(Color.RED);

        firstmovesecs = new SbtStringComboBox(secsArr);
        secondmovesecs = new SbtStringComboBox(secsArr);

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
        for (int v = 1; v <= 120; v++) {
            secsArr[v - 1] = v + "";
        }
        LookAndFeelFactory.installJideExtension();
        // Create a new JFrame container.
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
            public void colorChanged(Color newColor) {
                System.out.println("new first color is " + newColor);
            }
        });


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
        .bindCompProp("secondmoveseconds",secondmovesecs);
    }
}
