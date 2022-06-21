package com.sia.client.ui;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;
import com.jidesoft.tree.TreeUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Bookie;
import com.sia.client.model.ConsensusMakerSettings;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;
import com.sia.client.ui.comps.PopupLocationConfig;
import com.sia.client.ui.control.SportsTabPane;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
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

public class ConsensusMakerGui extends AbstractLayeredDialog implements ActionListener {//ItemListener {

    ConsensusMakerSettings cms;

    String sport = "";

    JPanel panel = new JPanel();
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();

    JButton set = new JButton("Save");
    JButton deletebutton = new JButton("Delete");
    private boolean ignorenexttrigger = false;
    private CheckBoxTree _tree;
    private Vector checkedsports = new Vector();
    private Vector checkednodes = new Vector();
    private Vector illegalsports = new Vector();
    private Vector illegalnodes = new Vector();
    JPanel sportsbooktreePanel = new JPanel(new BorderLayout(2, 2));
    JPanel selectedPanel;
    ConsensusMakerSettings[] cmslist = new ConsensusMakerSettings[100];

    private JComboBox cmscombobox;

    private JLabel totallabel = new JLabel("");

    private JLabel errorlabel = new JLabel("            ");

    private JLabel linetype = new JLabel("LINE TYPE");
    private JLabel notify = new JLabel("NOTIFY");
    private JList selectedList = new JList();
    private Box box1 = Box.createVerticalBox();
    private Box box2 = Box.createVerticalBox();
    private Box box3 = Box.createVerticalBox();
    private Box horizontalBox = Box.createHorizontalBox();
    private int popupsecs = 5;
    private int popuplocationint = 8;



    private CheckBoxTree sportsbooktree;
    private Hashtable leaguenameidhash = new Hashtable();
    private Hashtable bookienameidhash = new Hashtable();
    private String alerttype = "";



    Hashtable spinnerhash = new Hashtable();
    JPanel percentagespanel = new JPanel();
    JButton createnewbutton = new JButton("Create New");


    JPanel userComponent = new JPanel();

    JList eventsList = new JList();
    DefaultListModel eventsModel = new DefaultListModel();

    public ConsensusMakerGui(SportsTabPane stp) {
        super(stp,"Consensus Line Setup", SiaConst.CONSENSUSHELPURL);
    }
    @Override
    protected JComponent getUserComponent() {
        totallabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        percentagespanel.setLayout(new GridBagLayout());
        percentagespanel.setBorder(BorderFactory.createEtchedBorder());
        int index=0;
        Vector cmsvec = new Vector();
        cmsvec.add(new ConsensusMakerSettings("Edit Existing"));

        Hashtable cmshash = AppController.getConsensusMakerSettings();
        Enumeration enum98 = cmshash.elements();
        String html = "<html><table><tr>";
        while(enum98.hasMoreElements())
        {
            html = html+"<td align=top valign=top>";
            ConsensusMakerSettings cmsi = (ConsensusMakerSettings)enum98.nextElement();
            html = html+cmsi.getsportsselectedhtmlbreakdown()+"</td>";


            cmsvec.add(cmsi);

        }
        html = html +"</tr></table>";
        JLabel htmllabel = new JLabel(html);
        //sportlist[9] = "Auto Racing";
        cmscombobox = new JComboBox(cmsvec);
        //cmscombobox.setMaximumRowCount(cmshash.size());

        LookAndFeelFactory.installJideExtension();
        // Create a new JFrame container.
        JFrame jfrm1 = SpankyWindow.findSpankyWindow(getAnchoredLayeredPane().getSportsTabPane().getWindowIndex());

        userComponent.setLayout(new FlowLayout());
        //userComponent.setLayout(new GridBagLayout());









        cmscombobox.setSelectedIndex(0);
        cmscombobox.addActionListener(this);



        panel1.setBorder(BorderFactory.createEtchedBorder());
        panel1.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setLayout(new GridBagLayout());
        panel2.setBorder(BorderFactory.createEtchedBorder());
        panel2.setLayout(new GridBagLayout());


















        selectedList.revalidate();
        sportsbooktreePanel.revalidate();

        //************************end of panel*******************

        //set Buttong actions
        set.addActionListener(ae -> {
            try {
                Vector leagueidsvec = new Vector();
                String sportselected = "";
                int[] treeRows = _tree.getCheckBoxTreeSelectionModel().getSelectionRows();
                log("treerows=" + treeRows);
                if (treeRows != null) {
                    java.util.Arrays.sort(treeRows);
                    for (final int treeRow : treeRows) {
                        TreePath path = _tree.getPathForRow(treeRow);
                        log(path.getLastPathComponent());
                        String id = "" + leaguenameidhash.get("" + path.getLastPathComponent());

                        if (id.equals("null"))
                        {
                            id = "" + path.getLastPathComponent();

                        }
                        sportselected = sportselected + id + ",";

                        leagueidsvec.add(id);


                    }
                    System.out.println("sportsselectedfromgui="+sportselected);
                }

                cms.setSportsVec(leagueidsvec);
                cms.setSportsselected(sportselected);


                Enumeration enum99 = spinnerhash.keys();
                while(enum99.hasMoreElements())
                {
                    String bookieid = (String)enum99.nextElement();
                    JSpinner spinner = (JSpinner)spinnerhash.get(bookieid);
                    int val = (int)spinner.getValue();
                    cms.setValue(bookieid,val);
                }

            AppController.addConsensusMakerSetting(cms);
                SpankyWindow.applyToAllWindows((tp)-> {
                    SpankyWindow sw = SpankyWindow.findSpankyWindow(tp.getWindowIndex());
                    sw.revalidate();
                    sw.repaint();
                    sw.getSportsTabPane().rebuildMainScreen();
                });


            } catch (Exception e) {

            }
            close();
        });

        deletebutton.addActionListener(ae -> {
            try {
                AppController.removeConsensusMakerSetting(cms);

                }
            catch (Exception e) {

            }
            close();
        });


        createnewbutton.addActionListener(this);

        // Create three vertical boxes.


        // Create invisible borders around the boxes.
        box1.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box2.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box3.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pan1 = new JPanel(new FlowLayout());
        JPanel pan2 = new JPanel(new FlowLayout());


        pan1.add(createnewbutton);
        pan1.add(new JLabel("           OR           "));
        pan1.add(cmscombobox);
        pan2.add(htmllabel);

        /*
        box1.add(createnewbutton);
        box2.add(new JLabel("           OR           "));
        box3.add(cmscombobox);
        box3.add(htmllabel);
*/
        box1.add(pan1);
        box1.add(pan2);


        selectedList.revalidate();

        selectedList.setEnabled(false);

        // Add the boxes to the content pane.
        userComponent.add(box1);
       // userComponent.add(box1);
       // userComponent.add(box2);
       // userComponent.add(box3);




        return userComponent;
//*******end of cons***********
    }















    public void actionPerformed(ActionEvent ev) {
        boolean showdelbutton = false;
       if (ev.getSource() == cmscombobox )
        {
            if(cmscombobox.getSelectedIndex() == 0)
            {
                return;
            }


            cms = (ConsensusMakerSettings) cmscombobox.getSelectedItem();
            showdelbutton = true;

        }
        else if(ev.getSource() == createnewbutton)
        {
            cms = new ConsensusMakerSettings();


        }

        String[] checkedsportsarr = cms.getSportsselected().split(",");
        for (final String s : checkedsportsarr) {
            try {
                checkedsports.add("" + s);
            } catch (Exception ex) {
                log(ex);
            }
        }

        Hashtable allcheckedhash = AppController.getConsensusMakerSettings();
        Enumeration enum10 = allcheckedhash.elements();
        while(enum10.hasMoreElements())
        {

            ConsensusMakerSettings thiscms = (ConsensusMakerSettings) enum10.nextElement();
            if(thiscms == cms)
            {
                continue;
            }
            else
            {
               // System.out.println("entering illegal...");

                String[] illegalsportsarr = thiscms.getSportsselected().split(",");
                for (final String s : illegalsportsarr) {
                    try {
                        illegalsports.add("" + s);
                    } catch (Exception ex) {
                        log(ex);
                    }
                }



            }
        }

        final TreeModel treeModel = createSportTreeModel();

        JPanel treePanel = new JPanel(new BorderLayout(2, 2));
        treePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), cms.getName(), JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));
        _tree = new CheckBoxTree(treeModel) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(300, 600);
            }
        };
        _tree.setRootVisible(true);
        _tree.getCheckBoxTreeSelectionModel().setDigIn(true);
        _tree.getCheckBoxTreeSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        _tree.setClickInCheckBoxOnly(false);

        _tree.setShowsRootHandles(true);
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) _tree.getActualCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);

        _tree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(e -> {

            TreePath[] paths = e.getPaths();
            String illegalfound = "";
            for (TreePath path : paths)
            {
                eventsModel.addElement((e.isAddedPath(path) ? "Added - " : "Removed - ") + path);
            }
            eventsModel.addElement("---------------");
            eventsList.ensureIndexIsVisible(eventsModel.size() - 1);
            DefaultListModel selectedModel = new DefaultListModel();
//            TreePath[] treePaths = _tree.getCheckBoxTreeSelectionModel().getSelectionPaths();

            int[] treeRows = _tree.getCheckBoxTreeSelectionModel().getSelectionRows();
            //log("treerows=" + treeRows);
            if (treeRows != null) {
                java.util.Arrays.sort(treeRows);
                for (final int treeRow : treeRows)
                {
                    TreePath path = _tree.getPathForRow(treeRow);
                   // log("lastcomponent="+path.getLastPathComponent()+"..");


                    if(illegalnodes.contains(path.getLastPathComponent()) )
                    {
                        errorlabel.setText(path.getLastPathComponent()+" used already");
                        errorlabel.setForeground(Color.RED);
                        log(path+"..FOUND illegal="+path.getLastPathComponent());


                        _tree.getCheckBoxTreeSelectionModel().removeSelectionPath(path);


                    }
                    //else if(arethereillegalchildren((TreePath)path.getLastPathComponent()))
                    else if(isThisParentIllegal((DefaultMutableTreeNode)path.getLastPathComponent()))
                    {
                        errorlabel.setText(path.getLastPathComponent()+" used already");
                        errorlabel.setForeground(Color.RED);
                        log(path+"..FOUND illegal CHILD="+path.getLastPathComponent());
                        _tree.getCheckBoxTreeSelectionModel().removeSelectionPath(path);


                    }
                    else
                    {
                        log(path+"..selected="+path.getLastPathComponent());
                        selectedModel.addElement(path.getLastPathComponent());
                       // errorlabel.setText("");
                       // ignorenexttrigger = false;
                    }


                }
            }
            log("-------------------------");
            selectedList.setModel(selectedModel);
        });
        eventsList.setModel(eventsModel);

        selectedList.setVisibleRowCount(30);
        eventsList.setVisibleRowCount(15);

        selectedPanel = new JPanel(new BorderLayout());
        selectedPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Selected Sports", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));


       // selectedPanel = new JPanel(new BoxLayout(selectedPanel,BoxLayout.PAGE_AXIS));
        JScrollPane sp = new JScrollPane(selectedList);
        sp.setPreferredSize(new Dimension(200,600));
        selectedPanel.add(sp);


        JPanel eventsPanel = new JPanel(new BorderLayout());
        eventsPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Event Fired", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));
        eventsPanel.add(new JScrollPane(eventsList));


        SearchableUtils.installSearchable(_tree);

        TreeUtils.expandAll(_tree, true);

        /*
        JScrollPane sp2 = new JScrollPane(_tree);
        sp2.setPreferredSize(new Dimension(300,550));
        treePanel.add(sp2);
        */
        treePanel.add(new JScrollPane(_tree));


        //initialize tree
        for (int j = 0; j < checkednodes.size(); j++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) checkednodes.elementAt(j);
            //log("its"+node);
            TreePath path = new TreePath(((DefaultMutableTreeNode) node).getPath());
            //log("its==========================="+checkednodes.get(j)+"psth"+path);
            _tree.getCheckBoxTreeSelectionModel().addSelectionPath(path);
        }

       // _tree.getCheckBoxTreeSelectionModel().addTreeSelectionListener();

        selectedList.revalidate();
        selectedPanel.revalidate();

        selectedList.setEnabled(false);



        String sportname = "";


                spinnerhash = new Hashtable();



                List<Bookie> newBookiesVec = AppController.getBookiesVec();
                List<Bookie> hiddencols = AppController.getHiddenCols();
                String allbookies = "";
                GridBagConstraints c = new GridBagConstraints();

                //periods  checkboxes

                c.anchor = GridBagConstraints.EAST;
                c.gridwidth = 3;
                c.gridheight = 1;

                int xcnt = 0;

                int booksadded = 0;
                c.gridx = 0;
                c.gridy = 0;
        Vector<Bookie> goodbookiesvec = new Vector();
        for (int cnt = 0; cnt < newBookiesVec.size(); cnt++) {
            Bookie b = newBookiesVec.get(cnt);


            if (b.getBookie_id() >= 990) {
                continue;
            } else {
                goodbookiesvec.add(b);
            }
        }
                int yreset = (int)(Math.ceil(goodbookiesvec.size()/4.0));
        System.out.println("yreset="+yreset);
                for (int cnt = 0; cnt < goodbookiesvec.size(); cnt++)
                {
                    Bookie b = goodbookiesvec.get(cnt);




                    JPanel bmpanel = new JPanel();
                    SpinnerModel spinnermodel = new SpinnerNumberModel(0,0, 100, 1);
                    JSpinner bmspinner = new JSpinner(spinnermodel);
                    bmspinner.setValue(cms.getValue(b.getBookie_id()));
                    JComponent comp = bmspinner.getEditor();
                    JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
                    DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
                    formatter.setCommitsOnValidEdit(true);
                    bmspinner.addChangeListener(new ChangeListener() {

                        @Override
                        public void stateChanged(ChangeEvent e)
                        {
                            sumAllSpinners(spinnerhash);
                            //selectedPanel.revalidate();

                        }
                    });


                    bmpanel.add(new JLabel(b.getShortname()));
                    bmpanel.add(bmspinner);
                    bmspinner.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    spinnerhash.put(""+b.getBookie_id(), bmspinner);
                    percentagespanel.add(bmpanel,c);
                   // System.out.println("adding panel to ("+c.gridx+","+c.gridy+")");
                    c.gridy++;
                    if(c.gridy % yreset ==0)
                    {
                        c.gridx= c.gridx+3;
                        c.gridy = 0;
                    }
                }



               // c.gridwidth = c.gridx;
               // c.gridx = 0;
               // c.gridy = 20;

               // c.anchor = GridBagConstraints.WEST;
               // percentagespanel.add(totallabel,c);






                //   box1.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));


                //treePanel.add(_tree);

                //box1.add(gameperiodComboBox);
                sumAllSpinners(spinnerhash);
                box1.removeAll();
                box2.removeAll();
                box3.removeAll();

                box3.add(selectedPanel);
        //box3.add(new JSeparator(SwingConstants.HORIZONTAL));
                //box3.add(totallabel);
        JPanel errorPanel = new JPanel();
       errorPanel.add(errorlabel);
       // box2.add(totallabel);
                box2.add(percentagespanel);
        box2.add(new JSeparator(SwingConstants.HORIZONTAL));
                //box2.add(totallabel);

                JPanel totalPanel = new JPanel();
                totalPanel.add(totallabel);

                JPanel buttonPanel = new JPanel();
                //buttonPanel.add(errorlabel);
                buttonPanel.add(set);
                if(showdelbutton)
                {
                    buttonPanel.add(deletebutton);
                }
                //horizontalBox.add(errorlabel);
                //horizontalBox.add(buttonPanel);
                JPanel bottomPanel = new JPanel(new GridLayout(0, 2));
                bottomPanel.add(totalPanel);
                 bottomPanel.add(buttonPanel);

             //   box2.add(buttonPanel);
                  box2.add(bottomPanel);



                //box1.add(linealertname);
                //box1.add(saveBut);


                box1.add(treePanel);
                box1.add(errorPanel);

                box1.revalidate();
                box1.repaint();



				/*	//box2.add(totalcheckbox);
					box2.add(totalPanel);
					//box2.add(moneylinecheckbox);
					box2.add(moneylinePanel);
					//box2.add(teamtotalcheckbox);
					box2.add(teamtotalPanel);*/


                box2.revalidate();
                box2.repaint();
        box3.revalidate();
        box3.repaint();
               // userComponent.removeAll();
                userComponent.add(box1);
                userComponent.add(box2);
                userComponent.add(box3);
               // userComponent.revalidate();







    }



private void sumAllSpinners(Hashtable sporthash)
{
    int total = 0;
    Enumeration enum99 = sporthash.elements();
    while(enum99.hasMoreElements())
    {
        JSpinner spinner = (JSpinner)enum99.nextElement();
        int val = (int)spinner.getValue();
        if(val > 0)
        {
            if(spinner.getParent() != null)
            {
                spinner.getParent().setBackground(Color.GREEN);
            }
        }
        else
        {
            if(spinner.getParent() != null)
            {
                spinner.getParent().setBackground(null);
            }
        }
        total = total+val;
    }

    totallabel.setText("Total %= "+total+"%");


    if(total == 100)
    {
        set.setEnabled(true);
    }
    else
    {
        set.setEnabled(false);
    }
    box1.revalidate();
    box2.revalidate();
    box3.revalidate();
}
Hashtable allsportsidforsportname = new Hashtable();

    public TreeModel createSportTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("All Sports");
        DefaultTreeModel treeModel = new DefaultTreeModel(root);

        SportType [] preDefinedSportTypes = SportType.getPreDefinedSports();
        for(SportType st: preDefinedSportTypes) {
            root.add(new DefaultMutableTreeNode(st.getSportName()));
            allsportsidforsportname.put(st.getSportName(),"");
            leaguenameidhash.put(st.getSportName(),"");
        }
        DefaultMutableTreeNode horse = new DefaultMutableTreeNode("Horse");
//        DefaultMutableTreeNode esport = new DefaultMutableTreeNode("E-Sport");
        DefaultMutableTreeNode other = new DefaultMutableTreeNode("Other");
        //root.add(horse);
        try {

            List<Sport> sportsVec = AppController.getSportsVec();
            for (Sport value : sportsVec)
            {
                DefaultMutableTreeNode tempnode = other;
                int count = root.getChildCount();
                for(int i=0;i<count;i++)
                {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(i);
                    if ( value.getSportname().equals(node.getUserObject())) {
                        tempnode = node;
                        break;
                    }
                }
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(value.getLeaguename());

                tempnode.add(child);

                leaguenameidhash.put(value.getLeaguename(), "" + value.getLeague_id());
                String leagueids = (String)leaguenameidhash.get(value.getSportname());
                log("before sport name="+value.getSportname()+"..leaguename="+value.getLeaguename()+".."+value.getLeague_id()+"..idstillnow="+leagueids);
                if(!leagueids.equals(""+value.getLeague_id())) // used for sports with one id
                {
                    leagueids = leagueids + value.getLeague_id() + ",";

                }

                leaguenameidhash.put(value.getSportname(),leagueids);

                log("after sport name="+value.getSportname()+"..leaguename="+value.getLeaguename()+".."+value.getLeague_id()+"..idstillnow="+leagueids);
                if (checkedsports.contains("" + value.getLeague_id()) || checkedsports.contains(value.getSportname())
                        || checkedsports.contains("All Sports"))
                {
                    checkednodes.add(child);

                }

                if (illegalsports.contains("" + value.getLeague_id()) || illegalsports.contains(value.getSportname())
                        || illegalsports.contains("All Sports"))
                {
                    log("adding to illegal="+child);
                    illegalnodes.add(child);

                }
            }


            return treeModel;
        } catch (Exception e) {
            log(e);
        }
        return null;
    }


public boolean arethereillegalchildren(TreePath tp)
{
    Iterator iter = illegalnodes.iterator();
    while(iter.hasNext())
    {
       TreePath childpath = (TreePath)iter.next();
       if(tp.isDescendant(childpath))
       {
           return true;
       }
    }
    return false;
}

public boolean isThisParentIllegal(DefaultMutableTreeNode parent)
{
    Iterator iter = illegalnodes.iterator();
    while(iter.hasNext())
    {
        DefaultMutableTreeNode child = (DefaultMutableTreeNode)iter.next();
        if(child.getParent() == parent || child.getParent().getParent() == parent)
       // if(child.getParent().equals(parent) || child.getParent().getParent().equals(parent))
        {
            return true;
        }
    }
    return false;

}
//*******end of class***********
}


