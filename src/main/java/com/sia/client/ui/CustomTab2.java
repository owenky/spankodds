package com.sia.client.ui;

import com.sia.client.model.Game;
import com.sia.client.model.GameDateSorter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import java.util.Collections;
import javax.swing.tree.*;
import javax.swing.JTree;
import javax.swing.event.*;
import java.text.SimpleDateFormat; 
import javax.swing.plaf.IconUIResource;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JOptionPane;

import javax.swing.SwingUtilities;

public class CustomTab2 extends JPanel {
	boolean editing = false;
	Vector customheaders = new Vector();
	Vector<String> illegalnames = new Vector<String>();
	Hashtable pathhash;
	Hashtable nodehash = new Hashtable();
	JTree jtree;
			Vector gamegroupvec = new Vector();
		Vector gamegroupheadervec = new Vector();
		Vector currentvec = new Vector();
		Vector gamesVec = new Vector();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");	
  private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
JScrollPane jscrlp = new JScrollPane();
	private static final String UP_BUTTON_LABEL = "^";
	private static final String DOWN_BUTTON_LABEL = "v";
private InvisibleNode root =  new InvisibleNode("Games"); 

  private static final String ADD_BUTTON_LABEL = "Add >";
  private static final String ADD_ALL_BUTTON_LABEL = "Add All >>";

  private static final String REMOVE_BUTTON_LABEL = "< Remove";
  private static final String REMOVE_ALL_BUTTON_LABEL = "<< Remove All";

  private static final String DEFAULT_SOURCE_CHOICE_LABEL = "Available Sports";

  private static final String DEFAULT_DEST_CHOICE_LABEL = "Selected Sports";
  
  private static final String SAVE_BUTTON_LABEL = "Save";
	private Hashtable sporthash = new Hashtable();
	private Hashtable mainhash = new Hashtable();
  private JLabel sourceLabel;

  private JList sourceList;

  private MyListModel2 sourceListModel;

  private JList destList;

  private MyListModel2 destListModel;

  private JLabel destLabel;

  private JButton addButton;

  private JButton removeButton;
  
  private JButton addAllButton;

  private JButton removeAllButton;
  
  private JButton upButton;

  private JButton downButton;
  
  private JButton saveButton;
  
  private JLabel jlab;
  
  private JCheckBox includeheaders;
  private JCheckBox includeseries;
  private JCheckBox includeingame;
  private JCheckBox includeadded;
  private JCheckBox includeextra;
  private JCheckBox includeprops;
 
  private JTextField tabname;
  
  
  private JFrame f = new JFrame("Custom Tab");
  int tabindex = -1;
  public CustomTab2()
  {
	  tabname = new JTextField(10);
		tabname.setDocument(new JTextFieldLimit(10));
		TextPrompt tp7 = new TextPrompt("Name Your Tab", tabname);
		tp7.setForeground( Color.RED );
		tp7.setShow(TextPrompt.Show.FOCUS_LOST);
		//tp7.changeAlpha(0.5f);
		tp7.changeStyle(Font.BOLD + Font.ITALIC);
	  
	tabname.setMinimumSize(new Dimension(100,20));
  includeheaders = new JCheckBox("Include Headers",true);
  includeseries = new JCheckBox("Include Series Prices",true);
  includeingame = new JCheckBox("Include In Game",true);
  includeadded = new JCheckBox("Include Added Games",true);
  includeextra = new JCheckBox("Include Extra Games",true);
  includeprops = new JCheckBox("Include Props",true);
    destListModel = new MyListModel2();
    destList = new JList(destListModel);
	destList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	
	 init(); 
	initScreen();
	  
	  
	  
	  
	      f.getContentPane().add(this, BorderLayout.CENTER);
    f.setSize(1000, 400);
	//f.pack();
    f.setVisible(true);
    
	
  }
  public CustomTab2(String tabnamestr,int tabindex)
  {
	  this.tabindex = tabindex;
	  
	  	  tabname = new JTextField(tabnamestr,10);
		  tabname.setDocument(new JTextFieldLimit(10));
		TextPrompt tp7 = new TextPrompt("Name Your Tab", tabname);
		tp7.setForeground( Color.RED );
		tp7.setShow(TextPrompt.Show.FOCUS_LOST);
		//tp7.changeAlpha(0.5f);
		tp7.changeStyle(Font.BOLD + Font.ITALIC);
	tabname.setMinimumSize(new Dimension(100,20));
  includeheaders = new JCheckBox("Include Headers",true);
  includeseries = new JCheckBox("Include Series Prices",true);
  includeingame = new JCheckBox("Include In Game",true);
  includeadded = new JCheckBox("Include Added Games",true);
  includeextra = new JCheckBox("Include Extra Games",true);
  includeprops = new JCheckBox("Include Props",true);
      String msinfo = AppController.getTabInfo(tabnamestr);
	if(msinfo == null)
	{
		msinfo = "";
		editing = false;
	}
	else
	{
		editing = true;
		String[] items = msinfo.split("\\*");
		
		for(int j = 0; j <items.length;j++)
		{
			System.out.println(j+" item="+items[j]);
			if(j == 0)
			{
				String[] headers = items[j].split("\\|");
				for(int k=0; k < headers.length; k++)
				{
					String header = headers[k];
					if(header.equals(""))
					{
						continue;
					}
					else
					{
					customheaders.add(header);
					System.out.println("adding header="+header);
					}
				}
			}
			else if(j == 1)
			{
				tabname.setText(items[j]);
				//tabname.setEditable(false);
				tabname.setEnabled(false);
			}
			else if(j == 2)
			{
				includeheaders.setSelected(Boolean.parseBoolean(items[j]));
			}
			else if(j == 3)
			{
				includeseries.setSelected(Boolean.parseBoolean(items[j]));
			}
			else if(j == 4)
			{
				includeingame.setSelected(Boolean.parseBoolean(items[j]));
			}
			else if(j == 5)
			{
				includeadded.setSelected(Boolean.parseBoolean(items[j]));
			}
			else if(j == 6)
			{
				includeextra.setSelected(Boolean.parseBoolean(items[j]));
			}
			else if(j == 7)
			{
				includeprops.setSelected(Boolean.parseBoolean(items[j]));
			}	
			
		}

	}
  
  
  
  

    destListModel = new MyListModel2();
    destList = new JList(destListModel);
	destList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	
	  
	init(tabnamestr);
	initScreen(tabnamestr);	  
	  
	  
	  
	  
	      f.getContentPane().add(this, BorderLayout.CENTER);
    f.setSize(1000, 400);
	//f.pack();
    f.setVisible(true);
	//	revalidate();
	//	repaint();
	
  }

    private void setNodeVisible(final JTree tree, boolean isVisible) {
      DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
      TreePath[] path = tree.getSelectionPaths();
      InvisibleNode node = null;
      for (int i = 0; i < path.length; i++) {
        node = (InvisibleNode) path[i].getLastPathComponent();
        if (!(node == model.getRoot())) {
          node.setVisible(isVisible);
        } else {
          System.out.println("refused: root node");
        }
      }
      if (path.length == 1) {
        model.nodeChanged(node);
      } else {
        model.reload();
      }
    }



  public String getSourceChoicesTitle() {
    return sourceLabel.getText();
  }

  public void setSourceChoicesTitle(String newValue) {
    sourceLabel.setText(newValue);
  }

  public String getDestinationChoicesTitle() {
    return destLabel.getText();
  }

  public void setDestinationChoicesTitle(String newValue) {
    destLabel.setText(newValue);
  }

  public void clearSourceListModel() {
    sourceListModel.clear();
  }

  public void clearDestinationListModel() {
    destListModel.clear();
  }

  public void addSourceElements(ListModel newValue) {
    fillListModel(sourceListModel, newValue);
  }

  public void setSourceElements(ListModel newValue) {
    clearSourceListModel();
    addSourceElements(newValue);
  }

  public void addDestinationElements(ListModel newValue) {
    fillListModel(destListModel, newValue);
  }

  private void fillListModel(MyListModel2 model, ListModel newValues) {
    int size = newValues.getSize();
    for (int i = 0; i < size; i++) {
      model.add(newValues.getElementAt(i));
	  System.out.println("adding .."+newValues.getElementAt(i));
    }
  }

  public void addSourceElements(Object newValue[]) {
    fillListModel(sourceListModel, newValue);
  }

  public void setSourceElements(Object newValue[]) {
    clearSourceListModel();
    addSourceElements(newValue);
  }


  public void addDestinationElements(Object newValue[]) {
    fillListModel(destListModel, newValue);
  }




  private void fillListModel(MyListModel2 model, Object newValues[]) {
    model.addAll(newValues);
  }

  public Iterator sourceIterator() {
    return sourceListModel.iterator();
  }

  public Iterator destinationIterator() {
    return destListModel.iterator();
  }

  public void setSourceCellRenderer(ListCellRenderer newValue) {
    sourceList.setCellRenderer(newValue);
  }

  public ListCellRenderer getSourceCellRenderer() {
    return sourceList.getCellRenderer();
  }

  public void setDestinationCellRenderer(ListCellRenderer newValue) {
    destList.setCellRenderer(newValue);
  }

  public ListCellRenderer getDestinationCellRenderer() {
    return destList.getCellRenderer();
  }

  public void setVisibleRowCount(int newValue) {
    sourceList.setVisibleRowCount(newValue);
    destList.setVisibleRowCount(newValue);
  }

  public int getVisibleRowCount() {
    return sourceList.getVisibleRowCount();
  }

  public void setSelectionBackground(Color newValue) {
    sourceList.setSelectionBackground(newValue);
    destList.setSelectionBackground(newValue);
  }

  public Color getSelectionBackground() {
    return sourceList.getSelectionBackground();
  }

  public void setSelectionForeground(Color newValue) {
    sourceList.setSelectionForeground(newValue);
    destList.setSelectionForeground(newValue);
  }

  public Color getSelectionForeground() {
    return sourceList.getSelectionForeground();
  }

  private void clearSourceSelected() {
    Object selected[] = sourceList.getSelectedValues();
    for (int i = selected.length - 1; i >= 0; --i) {
      sourceListModel.removeElement(selected[i]);
    }
    sourceList.getSelectionModel().clearSelection();
  }

  private void clearDestinationSelected() {
    Object selected[] = destList.getSelectedValues();
    for (int i = selected.length - 1; i >= 0; --i) {
      destListModel.removeElement(selected[i]);
    }
    destList.getSelectionModel().clearSelection();
  }
  
    private void clearSourceAll() {
    Object selected[] = ((MyListModel2)sourceList.getModel()).toArray();
    for (int i = selected.length - 1; i >= 0; --i) {
      sourceListModel.removeElement(selected[i]);
    }
    sourceList.getSelectionModel().clearSelection();
  }

  private void clearDestinationAll() {
    Object selected[] = ((MyListModel2)destList.getModel()).toArray();
    for (int i = selected.length - 1; i >= 0; --i) {
      destListModel.removeElement(selected[i]);
    }
    destList.getSelectionModel().clearSelection();
  }
  
  
  
private void initScreen()
{
	initScreen("");
}
private void init()
{
	init("");
}
  private void initScreen(String tabnamestr) {
	  
    setBorder(BorderFactory.createEtchedBorder());
	

    setLayout(new GridBagLayout());
    sourceLabel = new JLabel(DEFAULT_SOURCE_CHOICE_LABEL);
    sourceListModel = new MyListModel2();
    sourceList = new JList(sourceListModel);
	
	

    add(sourceLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    add(jscrlp, new GridBagConstraints(0, 1, 1, 4, .5,
        1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        EMPTY_INSETS, 0, 0));

    addButton = new JButton(ADD_BUTTON_LABEL);
    add(addButton, new GridBagConstraints(1, 1, 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    addButton.addActionListener(new AddListener());
	
	
	addAllButton = new JButton(ADD_ALL_BUTTON_LABEL);
	/*
    add(addAllButton, new GridBagConstraints(1, 2, 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    addAllButton.addActionListener(new AddAllListener());
	*/
	
	
    removeButton = new JButton(REMOVE_BUTTON_LABEL);
    add(removeButton, new GridBagConstraints(1, 2, 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 5, 0, 5), 0, 0));
    removeButton.addActionListener(new RemoveListener());
	
	removeAllButton = new JButton(REMOVE_ALL_BUTTON_LABEL);
    add(removeAllButton, new GridBagConstraints(1, 3, 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 5, 0, 5), 0, 0));
    removeAllButton.addActionListener(new RemoveAllListener());
	
	
	JLabel lab = new JLabel("Tab Name");
	
	add(lab, new GridBagConstraints(1, 4, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
	add(tabname, new GridBagConstraints(1, 5, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));	

    destLabel = new JLabel(DEFAULT_DEST_CHOICE_LABEL);

	
		destList.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent me) {
            if (me.getClickCount() == 2) 
			{
 					 System.out.println("Double clicked on.." + destList.getSelectedValue()+ "src/main");
					 TreePath tp = (TreePath)pathhash.get(destList.getSelectedValue()+"");
					 System.out.println("treepath="+tp);
					  InvisibleNode node = (InvisibleNode)tp.getLastPathComponent();
					 node.setVisible(true);
					 ((DefaultTreeModel) jtree.getModel()).nodeChanged(node);
					  pathhash.remove(destList.getSelectedValue()+"");
					 clearDestinationSelected();
					
            }
         }
      });	  	
		
		

	
    add(destLabel, new GridBagConstraints(2, 0, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    add(new JScrollPane(destList), new GridBagConstraints(2, 1, 1, 4, .5,
        1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        EMPTY_INSETS, 0, 0));
		
		
		
	
		
	upButton = new JButton(UP_BUTTON_LABEL)	;
	upButton.addActionListener(new MoveUpListener());
	downButton = new JButton(DOWN_BUTTON_LABEL)	;
	downButton.addActionListener(new MoveDownListener());
	add(upButton, new GridBagConstraints(3, 1, 1, 1, 0, 0,
        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));		
	add(downButton, new GridBagConstraints(3, 2, 1, 1, 0, 0,
        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));				
	

  	saveButton = new JButton(SAVE_BUTTON_LABEL);
  
    saveButton.addActionListener(new SaveListener());	
  	add(includeheaders, new GridBagConstraints(3, 4, 1, 1, 0, 0,
        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));		
	add(includeseries, new GridBagConstraints(3, 5, 1, 1, 0, 0,
			GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
			EMPTY_INSETS, 0, 0));		
	add(includeingame, new GridBagConstraints(3, 6, 1, 1, 0, 0,
			GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
			EMPTY_INSETS, 0, 0));		
	add(includeadded, new GridBagConstraints(3, 7, 1, 1, 0, 0,
			GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
			EMPTY_INSETS, 0, 0));		
	add(includeextra, new GridBagConstraints(3, 8, 1, 1, 0, 0,
			GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
			EMPTY_INSETS, 0, 0));		
	add(includeprops, new GridBagConstraints(3, 9, 1, 1, 0, 0,
			GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
			EMPTY_INSETS, 0, 0));					

	  add(saveButton, new GridBagConstraints(3, 10 , 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 5, 0, 5), 0, 0));	

	
		
		
  }

  public void init(String tabnamestr) {
    
	
	
    pathhash = new Hashtable();
    jlab = new JLabel();   




    illegalnames.add("football");
	illegalnames.add("basketball");
	illegalnames.add("baseball");
	illegalnames.add("hockey");
	illegalnames.add("fighting");
	illegalnames.add("soccer");
	illegalnames.add("auto racing");
	illegalnames.add("golf");
	illegalnames.add("tennis");
	

    InvisibleNode football = new InvisibleNode("Football"); 
	InvisibleNode basketball = new InvisibleNode("Basketball"); 
	InvisibleNode baseball = new InvisibleNode("Baseball"); 
	InvisibleNode hockey = new InvisibleNode("Hockey"); 
	InvisibleNode fighting = new InvisibleNode("Fighting"); 
	InvisibleNode soccer = new InvisibleNode("Soccer"); 
	InvisibleNode autoracing = new InvisibleNode("Auto Racing"); 
	InvisibleNode golf = new InvisibleNode("Golf"); 
	InvisibleNode tennis = new InvisibleNode("Tennis"); 
	
	
	
	if(tabnamestr.equalsIgnoreCase("football"))
	{
		root.add(football); 
	}
	else if(tabnamestr.equalsIgnoreCase("Basketball"))
	{
		root.add(basketball); 
	}
	else if(tabnamestr.equalsIgnoreCase("Baseball"))
	{
		root.add(baseball); 
	}
	else if(tabnamestr.equalsIgnoreCase("hockey"))
	{
		root.add(hockey); 
	}
	else if(tabnamestr.equalsIgnoreCase("fighting"))
	{
		root.add(fighting); 
	}
	else if(tabnamestr.equalsIgnoreCase("soccer"))
	{
		root.add(soccer); 
	}
	else if(tabnamestr.equalsIgnoreCase("Auto racing"))
	{
		root.add(autoracing); 
	}
	else if(tabnamestr.equalsIgnoreCase("golf"))
	{
		root.add(golf); 
	}
	else if(tabnamestr.equalsIgnoreCase("Tennis"))
	{
		root.add(tennis); 
	}
	else
	{
		 root.add(football); 
	root.add(basketball); 
	root.add(baseball); 
	root.add(hockey); 
	root.add(fighting);
	root.add(soccer);
	root.add(autoracing); 
	root.add(golf); 
	root.add(tennis); 
	}
    
    
	
	InvisibleNode currenttreenode = null;
		Vector allgames = AppController.getGamesVec();
		Vector allsports = AppController.getSportsVec();
		java.util.Date today = new java.util.Date();
		
		String lastdate = "";
		int lastleagueid = 0;
		
		
		try
		{

			Collections.sort(allgames, new GameLeagueSorter().thenComparing(new GameDateSorter().thenComparing(new GameNumSorter())));
			//Collections.sort(allgames, new GameLeagueSorter());	
			//System.out.println("sorted by league");
			Collections.sort(allgames, new GameDateSorter());			
			System.out.println("sorted by gm date");
			//Collections.sort(allgames, new GameNumSorter());			
			System.out.println("sorted by gm num");
			
		}
		catch(Exception ex)
		{
			System.out.println("exception sorting "+ex);
			ex.printStackTrace();
		}
		
		for(int k =0;k< allgames.size(); k++)
		{
		
			String gameid = "";
			Game g = (Game)allgames.get(k);
				
			
			if(g == null)
			{
				System.out.println("skipping gameid="+gameid+"...cuz of null game");
				continue;
			}
			else
			{
				gameid = ""+g.getGame_id();
			}	
			if(g.getGamedate() == null)
			{
				System.out.println("skipping gameid="+gameid+"...cuz of null game date");
				continue;
			}
			
			String gamedate = sdf.format(g.getGamedate());
			int leagueid = g.getLeague_id();
			
			
			
			Sport s = AppController.getSport(""+leagueid);
			
			Sport s2;						
			
			
			
			//System.out.println(s.getSportname());
			if(s == null)
			{
				System.out.println("skipping "+leagueid+"...cuz of null sport");
				continue;
			}

			if(s.getSportname().equalsIgnoreCase("Football"))
			{
				currenttreenode = football;
			}
			else if(s.getSportname().equalsIgnoreCase("Basketball"))
			{
				currenttreenode = basketball;
			}
			else if(s.getSportname().equalsIgnoreCase("Baseball"))
			{
				currenttreenode = baseball;
			}
			else if(s.getSportname().equalsIgnoreCase("Hockey"))
			{
				currenttreenode = hockey;
			}
			else if(s.getSportname().equalsIgnoreCase("Fighting"))
			{
				currenttreenode = fighting;
			}
			else if(s.getSportname().equalsIgnoreCase("Soccer"))
			{
				currenttreenode = soccer;
			}
			else if(s.getSportname().equalsIgnoreCase("Auto Racing"))
			{
				currenttreenode = autoracing;
			}
			else if(s.getSportname().equalsIgnoreCase("Golf"))
			{
				currenttreenode = golf;
			}
			else if(s.getSportname().equalsIgnoreCase("Tennis"))
			{
				currenttreenode = tennis;
			}
			else
			{
				System.out.println("should never enter gameid="+g.getGame_id());
				currenttreenode = football;				
				
			}
			

				if(leagueid == 9) // soccer need to look at subleagueid
				{
					leagueid= g.getSubleague_id();

					s2 = AppController.getSport(""+leagueid);
					
				}
				else
				{
					s2 = s;
				}

				String description = g.getDescription();
				if (description == null || description.equalsIgnoreCase("null"))
				{
					description = "";
				}
				
				if(g.getStatus() == null)
				{
					g.setStatus("");
				}
				if(g.getTimeremaining() == null)
				{
					g.setTimeremaining("");
				}
				

				if(g.getStatus().equalsIgnoreCase("Tie") || g.getStatus().equalsIgnoreCase("Cncld") || g.getStatus().equalsIgnoreCase("Poned") || g.getStatus().equalsIgnoreCase("Final")
				|| 	g.getStatus().equalsIgnoreCase("Win") || (g.getTimeremaining().equalsIgnoreCase("Win"))
				
				)
				{
					
					//finalgames.add(g);
					//System.out.println("skipping "+g.getGame_id());
					continue;
					
				}
				// adding inprogress games to custyom tabs!
				//else if(!g.getStatus().equalsIgnoreCase("NULL") && !g.getStatus().equals(""))
				//{
				//	continue;
					//inprogressgames.add(g);
				//}
				else if(g.isSeriesprice())
				{
					continue;
					//seriesgames.add(g);
				}
				else if(g.isForprop())
				{
					continue;
					//seriesgames.add(g);
				}				
				else if(g.isIngame() || description.indexOf("In-Game") != -1)
				{
					continue;
					//ingamegames.add(g);
				}				
				
				

				else if(!lastdate.equals(gamedate) || lastleagueid != leagueid) // new date or new league!
				{
					if(lastleagueid <= 4 || leagueid <= 4)
					{
					//System.out.println("new!...lastdate="+lastdate+"..gamedate="+g.getGamedate()+".."+lastleagueid+"..new="+leagueid);
					}
					lastdate = gamedate;
					lastleagueid = leagueid;
					gamegroupheadervec.add(s2.getLeaguename()+" "+sdf2.format(g.getGamedate()));
					
				//	currenttreenode.add(new InvisibleNode(s2.getLeaguename()+" "+sdf2.format(g.getGamedate()))); 
					sporthash.put(s2.getLeaguename()+" "+sdf2.format(g.getGamedate()),currenttreenode);
					mainhash.put(s2.getLeaguename()+" "+sdf2.format(g.getGamedate()),leagueid+" "+sdf2.format(g.getGamedate()));
					Vector v2 = new Vector();
					//v2.add(gameid);
					v2.add(g);
					gamegroupvec.add(v2);
					currentvec = v2;								
				}
				else // same date
				{
					//currentvec.add(gameid);
					currentvec.add(g);
					
				}
				
			 
			
			
			}
			

	for(int j=0;j <gamegroupvec.size(); j++)
	{
		Vector thisvec = (Vector)gamegroupvec.elementAt(j);
		int numgames = thisvec.size();
		String title = 	""+gamegroupheadervec.elementAt(j);
		InvisibleNode currenttreenode2 = (InvisibleNode)sporthash.get(title);
		
		String value = (String)mainhash.get(title);

		
		
		if(numgames  == 1)
		{
			title = title+" ("+numgames+" Event)";
		}
		else if(numgames > 1)
		{
			title = title+" ("+numgames+" Events)";
		}
		
		InvisibleNode childnode = new InvisibleNode(title);
		
		
		if(customheaders.contains(value))
		{
			childnode.setVisible(false);
			nodehash.put(value,childnode);
			System.out.println("adding value="+value+"..."+childnode.toString());
			//addDestinationElements(new Object[] {childnode});
		}
		
		currenttreenode2.add(childnode);
		
	}
	
	for(int z=0; z < customheaders.size(); z++)
	{
		String key = (String)customheaders.elementAt(z);
		System.out.print("key="+key);
		InvisibleNode node = (InvisibleNode)nodehash.get(key);
		if(node != null)
		{
			System.out.println(" not null");
		pathhash.put(node.toString(),new TreePath(node.getPath()));
		node.setVisible(false);
		addDestinationElements(new Object[] {node});
		}
		else
		{
			System.out.println(" is null");
		}
	}
	

    InvisibleTreeModel ml = new InvisibleTreeModel(root);
    //ml.activateFilter(true);
 
 	//jtree = new JTree(root); 
	jtree = new JTree(ml); 
	  
	

	
	
	
	
	DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
      public Component getTreeCellRendererComponent(JTree tree,
          Object value, boolean sel, boolean expanded, boolean leaf,
          int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded,
            leaf, row, hasFocus);
        if (!((InvisibleNode) value).isVisible()) 
		{
          setForeground(Color.green);
        }
		else
		{
          setForeground(Color.black);
        }
        return this;
      }
    };

	renderer.setLeafIcon(null);
	renderer.setOpenIcon(new IconUIResource(new NodeIcon('-')));
	renderer.setClosedIcon(new IconUIResource(new NodeIcon('+')));
	
	jtree.setCellRenderer(renderer);

    jtree.setEditable(false); 
	jtree.setRootVisible(false); 
 

    TreeSelectionModel tsm = jtree.getSelectionModel(); 
    //tsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); 
 

    jscrlp = new JScrollPane(jtree); 
	
   // Listen for tree expansion events. 
    jtree.addTreeExpansionListener(new TreeExpansionListener() {    
      public void treeExpanded(TreeExpansionEvent tse) {  
        // Get the path to the expansion point. 
        TreePath tp = tse.getPath(); 
 
        // Display the node 
        jlab.setText("Expansion: " + 
                     tp.getLastPathComponent()); 
      }    
 
      public void treeCollapsed (TreeExpansionEvent tse) {  
        // Get the path to the expansion point. 
        TreePath tp = tse.getPath(); 
 
        // Display the node 
        jlab.setText("Collapse: " + 
                     tp.getLastPathComponent()); 
      }    
    });   
 
    // Listen for tree selection events. 
    jtree.addTreeSelectionListener(new TreeSelectionListener() {    
      public void valueChanged(TreeSelectionEvent tse) {  
        // Get the path to the selection. 
        TreePath tp = tse.getPath(); 
 
        // Display the selected node. 
        jlab.setText("Selection event: " + 
                     tp.getLastPathComponent()); 
      }    
    });   
 
    // Listen for tree model events. Notice that the 
    // listener is registered with the tree model. 
    jtree.getModel().addTreeModelListener(new TreeModelListener() {    
      public void treeNodesChanged(TreeModelEvent tse) {  
        // Get the path to the change. 
        TreePath tp = tse.getTreePath(); 
 
        // Display the path 
        jlab.setText("Model change path: " + tp); 
      } 
 
      // Empty implementations of the remaing TreeModelEvent 
      // methods. Implement these if your application 
      // needs to handle these actions. 
      public void treeNodesInserted(TreeModelEvent tse) {} 
      public void treeNodesRemoved(TreeModelEvent tse) {} 
      public void treeStructureChanged(TreeModelEvent tse) {} 
    });   	
	
	
	jtree.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent me) {
			         int selRow = jtree.getRowForLocation(me.getX(), me.getY());
					TreePath selPath = jtree.getPathForLocation(me.getX(), me.getY()); 
			if(selRow != -1)
			{	
		
		

				if(jtree.isCollapsed(selRow)) 
				{
					jtree.expandRow(selRow);
				}
				else 
				{
					jtree.collapseRow(selRow);
				}

		
		
		
		
		
				if (me.getClickCount() == 2 && selPath.getPathCount() == 3) 
				{
					System.out.println("treepath="+selPath);
					System.out.println("pathcount="+selPath.getPathCount());
					System.out.println("lastPathComponent="+selPath.getLastPathComponent());
					InvisibleNode node = (InvisibleNode)selPath.getLastPathComponent();
					
					if(pathhash.get(node.toString()) == null) // not there already
					{
					
					
						 addDestinationElements(new Object[] { selPath.getLastPathComponent() });
						 
						 System.out.println("node="+node.toString()+ "src/main");
						 pathhash.put(node.toString(),selPath);
						 node.setVisible(false);
						((DefaultTreeModel) jtree.getModel()).nodeChanged(node);
						if(selRow != 0)
						{
							jtree.setSelectionRow(0);
						}
						else
						{
							jtree.setSelectionRow(1);
						}
					}	
				}
			}	
         }
      });	    
    




	

	
  }

  private class MoveUpListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = destList.getSelectedValues();
	  int[] selectedindices = destList.getSelectedIndices();

	  
      
	  destListModel.moveUp(selected);
	  int[] newselectedindices = new int[selectedindices.length];
	  for(int i =0; i <selectedindices.length;i++)
	  {
		 int oldindex = selectedindices[i];
		 if(oldindex != 0)
		 {
			 newselectedindices[i] = oldindex-1;
		 }
	  }
	  destList.setSelectedIndices(newselectedindices);
	  destList.ensureIndexIsVisible(destList.getSelectedIndex());
	  
    }
  } 
  
  private class SaveListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
		
				String tab = tabname.getText().trim();
				String tablc = tab.toLowerCase();
				Vector selectedlist = ((MyListModel2)destList.getModel()).getModelVec();				
				if(!AppController.getMainTabPane().isTabNameAvailable(tab) && !editing )
				{
					JOptionPane.showMessageDialog(null, tab+" name is taken!");
					return;
				}
				else if(illegalnames.contains(tablc)) // dont think this will execute cus maintabpane handles already!
				{
					JOptionPane.showMessageDialog(null, tab+" name is taken by system! Try something else.");
					return;
				}
				else if(tab.indexOf("~") != -1 || tab.indexOf("|") != -1 || tab.indexOf("*") != -1 || tab.indexOf(",") != -1 || tab.indexOf("!") != -1 || tab.indexOf("?") != -1)
				{
					JOptionPane.showMessageDialog(null, "Illegal character(s) used!");
					return;
				}
				else if(tab.equals(""))
				{
					JOptionPane.showMessageDialog(null, "Please Name Your Tab!");
					return;
				}				
				else if(selectedlist.size() == 0)
				{
					JOptionPane.showMessageDialog(null, "Empty List!");
					return;
				}		
				
				boolean headersbool = includeheaders.isSelected();
				boolean seriesbool = includeseries.isSelected();
				boolean ingamebool = includeingame.isSelected();
				boolean addedbool = includeadded.isSelected();
				boolean extrabool = includeextra.isSelected();
				boolean propsbool = includeprops.isSelected();
  
				
				
			
				String fixedcols = "";
				String showncols = "";
				String hiddencols = "";
				boolean fixed = true;
				Vector customvec = new Vector();
				String msstring = "";
				for(int i=0; i < selectedlist.size();i++)
				{
					String s = ((InvisibleNode)selectedlist.get(i)).toString();
					s = s.substring(0,s.indexOf("("));
					s = s.trim();
					customvec.add(s);
					msstring = msstring+"|"+mainhash.get(s);
					
				}
				
				msstring = msstring+"*"+tab+"*"+includeheaders.isSelected()+"*"+includeseries.isSelected()+"*"+includeingame.isSelected()+
							"*"+includeadded.isSelected()+"*"+includeextra.isSelected()+"*"+includeprops.isSelected();
							
				System.out.println("adding="+msstring);
				AppController.addCustomTab(tab,msstring);
								
			if(!editing)
			{
				SwingUtilities.invokeLater(new Runnable()
					{
					public void run()
					{					
						
						 
						 Vector tabpanes = AppController.getTabPanes();
						 System.out.println("tabpanes size= "+tabpanes.size());
					  for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							int numtabs = tp.getTabCount();
							System.out.println("numtabs= "+numtabs);
							
								tp.removeTabAt(numtabs -1);
								MainScreen ms = new MainScreen(tab,customvec);
								ms.setShowHeaders(includeheaders.isSelected());
								ms.setShowSeries(includeseries.isSelected());
								ms.setShowIngame(includeingame.isSelected());
								ms.setShowAdded(includeadded.isSelected());
								ms.setShowExtra(includeextra.isSelected());
								ms.setShowProps(includeprops.isSelected());
								tp.addTab(ms.getName(),null,ms,ms.getName());
								
								/*
								JPanel pnlTab = new JPanel(new GridBagLayout());
								pnlTab.setOpaque(false);
								JLabel lblTitle = new JLabel(ms.getName());
								JButton btnClose = new JButton("x");

								GridBagConstraints gbc = new GridBagConstraints();
								gbc.gridx = 0;
								gbc.gridy = 0;
								gbc.weightx = 1;

								pnlTab.add(lblTitle, gbc);

								gbc.gridx++;
								gbc.weightx = 0;
								pnlTab.add(btnClose, gbc);

								tp.setTabComponentAt(tp.getTabCount()-1, pnlTab);

								//btnClose.addActionListener(myCloseActionHandler);
								
								*/

								tp.addTab("+",null,null,"+");
								
						

						}
						
						
					}
					});								
			}
			else
			{
				SwingUtilities.invokeLater(new Runnable()
					{
					public void run()
					{					

						 Vector tabpanes = AppController.getTabPanes();
						 System.out.println("tabpanes size= "+tabpanes.size());
					  for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							int numtabs = tp.getTabCount();
							System.out.println("numtabs= "+numtabs);
							
							MainScreen oldms = (MainScreen)tp.getComponentAt(tabindex);
							oldms.makeDataModelsVisible(false);
							oldms.destroyMe();
							
								MainScreen ms = new MainScreen(tab,customvec);
								ms.setShowHeaders(includeheaders.isSelected());
								ms.setShowSeries(includeseries.isSelected());
								ms.setShowIngame(includeingame.isSelected());
								ms.setShowAdded(includeadded.isSelected());
								ms.setShowExtra(includeextra.isSelected());
								ms.setShowProps(includeprops.isSelected());
								tp.setComponentAt(tabindex,ms);
								tp.refreshCurrentTab();
						}
						
						
					}
					});		
					
			}
				f.dispose();
	  
	  
    }
  } 
  

 private class MoveDownListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = destList.getSelectedValues();
 	  int[] selectedindices = destList.getSelectedIndices();

	  
      
	  destListModel.moveDown(selected);
	  int[] newselectedindices = new int[selectedindices.length];
	  for(int i =0; i <selectedindices.length;i++)
	  {
		 int oldindex = selectedindices[i];
		 if(oldindex != ((MyListModel2)destList.getModel()).getSize())
		 {
			 newselectedindices[i] = oldindex+1;
		 }
	  }
	  destList.setSelectedIndices(newselectedindices);
	  destList.ensureIndexIsVisible(destList.getMaxSelectionIndex());
    }
  } 

  private class AddListener implements ActionListener {
    public void actionPerformed(ActionEvent e) 
	{
		TreePath[] selPathArray = jtree.getSelectionPaths(); 
		
		for(int i =0;i <selPathArray.length;i++)
		{	
			TreePath selPath = selPathArray[i];
            if (selPath != null && selPath.getPathCount() == 3) 
			{
				System.out.println("treepath="+selPath);
				System.out.println("pathcount="+selPath.getPathCount());
				System.out.println("lastPathComponent="+selPath.getLastPathComponent());
				InvisibleNode node = (InvisibleNode)selPath.getLastPathComponent();
				if(pathhash.get(node.toString()) == null) // not there already
				{
				
				
					 addDestinationElements(new Object[] { selPath.getLastPathComponent() });
					 
					 System.out.println("node="+node.toString()+ "src/main");
					 pathhash.put(node.toString(),selPath);
					 node.setVisible(false);
					((DefaultTreeModel) jtree.getModel()).nodeChanged(node);
			
						jtree.setSelectionRow(0);
					
				}
            }	

		}


			
    }
  }
  
  
  

   private class AddAllListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = ((MyListModel2)sourceList.getModel()).toArray();
      addDestinationElements(selected);
      clearSourceAll();
    }
  }

  private class RemoveListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = destList.getSelectedValues();
	  
	  for(int i =0; i < selected.length; i++)
	  {

		 TreePath tp = (TreePath)pathhash.get(selected[i]+"");
		 System.out.println("treepath="+tp);
		  InvisibleNode node = (InvisibleNode)tp.getLastPathComponent();
		 node.setVisible(true);
		 ((DefaultTreeModel) jtree.getModel()).nodeChanged(node);
		  pathhash.remove(selected[i]+"");
	  }			  
					 
	  
      
      clearDestinationSelected();
    }
  }
  
  private class RemoveAllListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = ((MyListModel2)destList.getModel()).toArray();
      addSourceElements(selected);
	  for(int i =0; i < selected.length; i++)
	  {

		 TreePath tp = (TreePath)pathhash.get(selected[i]+"");
		 System.out.println("treepath="+tp);
		  InvisibleNode node = (InvisibleNode)tp.getLastPathComponent();
		 node.setVisible(true);
		 ((DefaultTreeModel) jtree.getModel()).nodeChanged(node);
		  pathhash.remove(selected[i]+"");
	  }			  	  
      clearDestinationAll();
    }
  }
  
  
}

class MyListModel2 extends AbstractListModel {

  Vector model;

	public Vector getModelVec()
	{
		return model;
	}

  public MyListModel2() {
    model = new Vector();
  }


  public void moveUp(Object[] values)
  {
	  
	  for(int i=0; i <values.length; i++)
	  {
		  int firstindex = model.indexOf(values[i]);
		  Object selected = values[i];
		  if(firstindex == 0) break;
		  else
		  {
			  Object tempobj = model.get(firstindex-1);
			  model.set(firstindex-1,selected);
			  model.set(firstindex,tempobj);
			  fireContentsChanged(this, 0, getSize());			  
		  }

	  }
	  
  }
 public void moveDown(Object[] values)
  {
	  
	//  for(int i=0; i <values.length; i++)
		for(int i=values.length-1; i >=0; i--)
	  {
		  int firstindex = model.indexOf(values[i]);
		  Object selected = values[i];
		  if(firstindex == model.size()-1 ) break;
		  else
		  {
			  Object tempobj = model.get(firstindex+1);
			  model.set(firstindex+1,selected);
			  model.set(firstindex,tempobj);
			  fireContentsChanged(this, 0, getSize());	
		  }


	  }
	  
  }

  public int getSize() {
    return model.size();
  }

  public Object getElementAt(int index) {
    return model.toArray()[index];
  }

  public void add(Object element) {
    if (model.add(element)) {
      fireContentsChanged(this, 0, getSize());
    }
  }

  public void addAll(Object elements[]) 
  {
    Collection c = Arrays.asList(elements);
    model.addAll(c);
    fireContentsChanged(this, 0, getSize());
  }

  public void clear() {
    model.clear();
    fireContentsChanged(this, 0, getSize());
  }

  public boolean contains(Object element) {
    return model.contains(element);
  }



  public Iterator iterator() {
    return model.iterator();
  }


  public Object[] toArray()
  {
	  return model.toArray();
  }

  public boolean removeElement(Object element) {
    boolean removed = model.remove(element);
    if (removed) {
      fireContentsChanged(this, 0, getSize());
    }
    return removed;
  }




}
  

class InvisibleNode extends DefaultMutableTreeNode {

  protected boolean isVisible;
  protected String key;

  public InvisibleNode() {
    this(null);
  }

  public InvisibleNode(Object userObject) {
    this(userObject, true, true);
  }

  public InvisibleNode(Object userObject, boolean allowsChildren,
      boolean isVisible) {
    super(userObject, allowsChildren);
    this.isVisible = isVisible;
  }

  public TreeNode getChildAt(int index, boolean filterIsActive) {
    if (!filterIsActive) {
      return super.getChildAt(index);
    }
    if (children == null) {
      throw new ArrayIndexOutOfBoundsException("node has no children");
    }

    int realIndex = -1;
    int visibleIndex = -1;
    Enumeration e = children.elements();
    while (e.hasMoreElements()) {
      InvisibleNode node = (InvisibleNode) e.nextElement();
      if (node.isVisible()) {
        visibleIndex++;
      }
      realIndex++;
      if (visibleIndex == index) {
        return (TreeNode) children.elementAt(realIndex);
      }
    }

    throw new ArrayIndexOutOfBoundsException("index unmatched");
    //return (TreeNode)children.elementAt(index);
  }

  public int getChildCount(boolean filterIsActive) {
    if (!filterIsActive) {
      return super.getChildCount();
    }
    if (children == null) {
      return 0;
    }

    int count = 0;
    Enumeration e = children.elements();
    while (e.hasMoreElements()) {
      InvisibleNode node = (InvisibleNode) e.nextElement();
      if (node.isVisible()) {
        count++;
      }
    }

    return count;
  }

 public void setKey(String key) {
    this.key = key;
  }
  
  public String getKey()
  {
	  return key;
  }


  public void setVisible(boolean visible) {
    this.isVisible = visible;
  }

  public boolean isVisible() {
    return isVisible;
  }

}  


class InvisibleTreeModel extends DefaultTreeModel {

  protected boolean filterIsActive;

  public InvisibleTreeModel(TreeNode root) {
    this(root, false);
  }

  public InvisibleTreeModel(TreeNode root, boolean asksAllowsChildren) {
    this(root, false, false);
  }

  public InvisibleTreeModel(TreeNode root, boolean asksAllowsChildren,
      boolean filterIsActive) {
    super(root, asksAllowsChildren);
    this.filterIsActive = filterIsActive;
  }

  public void activateFilter(boolean newValue) {
    filterIsActive = newValue;
  }

  public boolean isActivatedFilter() {
    return filterIsActive;
  }

  public Object getChild(Object parent, int index) {
    if (filterIsActive) {
      if (parent instanceof InvisibleNode) {
        return ((InvisibleNode) parent).getChildAt(index,
            filterIsActive);
      }
    }
    return ((TreeNode) parent).getChildAt(index);
  }

  public int getChildCount(Object parent) {
    if (filterIsActive) {
      if (parent instanceof InvisibleNode) {
        return ((InvisibleNode) parent).getChildCount(filterIsActive);
      }
    }
    return ((TreeNode) parent).getChildCount();
  }

}