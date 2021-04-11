package com.sia.client.ui;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

public class BookieColumnController2 extends JPanel {

  private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

	private static final String UP_BUTTON_LABEL = "^";
	private static final String DOWN_BUTTON_LABEL = "v";
	private static final String UP_UP_BUTTON_LABEL = "^^";
	private static final String DOWN_DOWN_BUTTON_LABEL = "vv";	


  private static final String ADD_BUTTON_LABEL = "Add >";
  private static final String ADD_ALL_BUTTON_LABEL = "Add All >>";

  private static final String REMOVE_BUTTON_LABEL = "< Remove";
  private static final String REMOVE_ALL_BUTTON_LABEL = "<< Remove All";

  private static final String DEFAULT_SOURCE_CHOICE_LABEL = "Available";

  private static final String DEFAULT_DEST_CHOICE_LABEL = "Selected";
  
  private static final String SAVE_BUTTON_LABEL = "Save";

  private JLabel sourceLabel;

  private JList sourceList;

  private MyListModel sourceListModel;

  private JList destList;

  private MyListModel destListModel;
  
  private JList openerList;
  private MyListModel openerListModel;

  private JLabel destLabel;

  private JButton addButton;

  private JButton removeButton;
  
  private JButton addAllButton;

  private JButton removeAllButton;
  
  private JButton upButton;

  private JButton downButton;
  
  private JButton upupButton;

  private JButton downdownButton;
  
  private JButton saveButton;
  private JCheckBox showopen;
  
  private JFrame f = new JFrame("Bookie Columns");

  public BookieColumnController2()
  {
    initScreen();
	init();
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

 public void clearOpenerListModel() {
    openerListModel.clear();
  }

  public void clearSourceListModel() {
    sourceListModel.clear();
  }

  public void clearDestinationListModel() {
    destListModel.clear();
  }

 public void addOpenerElements(ListModel newValue) {
    fillListModel(openerListModel, newValue);
  }

  public void addSourceElements(ListModel newValue) {
    fillListModel(sourceListModel, newValue);
	
  }

 public void setOpenerElements(ListModel newValue) {
    clearOpenerListModel();
    addOpenerElements(newValue);
  }

  public void setSourceElements(ListModel newValue) {
    clearSourceListModel();
    addSourceElements(newValue);
  }

  public void addDestinationElements(ListModel newValue) 
  {
	int destlistindex = destList.getSelectedIndex();
	System.out.println("DEST LIST INDEX IS "+destlistindex);
	if(destlistindex == -1)
	{
		fillListModel(destListModel, newValue);
	}
	else
	{
		fillListModel(destListModel, newValue,destlistindex);
		destList.setSelectedIndex(destlistindex+1);
	}
  }

  private void fillListModel(MyListModel model, ListModel newValues) {
    
	
    for (int i = 0; i < newValues.getSize(); i++) 
	{
	 
	 
      model.add(newValues.getElementAt(i));
	  System.out.println("adding .."+newValues.getElementAt(i));
    }
  }
private void fillListModel(MyListModel model, ListModel newValues,int atindex) {
    
	
    for (int i = 0; i < newValues.getSize(); i++) 
	{
	 // add the items at the dest selected 	 position if something is selected in dest list
	 
      model.add(newValues.getElementAt(i),atindex);
	  System.out.println("adding .."+newValues.getElementAt(i)+"..at index ="+atindex);
    }
  }

 public void addOpenerElements(Object newValue[]) {
    fillListModel(openerListModel, newValue);
  }

  public void setOpenerElements(Object newValue[]) {
    clearOpenerListModel();
    addOpenerElements(newValue);
  }


  public void addSourceElements(Object newValue[]) {
    fillListModel(sourceListModel, newValue);
  }

  public void setSourceElements(Object newValue[]) {
    clearSourceListModel();
    addSourceElements(newValue);
  }


  public void addDestinationElements(Object newValue[]) 
   
  {
	int destlistindex = destList.getSelectedIndex();
	System.out.println("DEST LIST INDEX IS "+destlistindex);
	if(destlistindex == -1)
	{
		fillListModel(destListModel, newValue);
	}
	else
	{
		fillListModel(destListModel, newValue,destlistindex);
		destList.setSelectedIndex(destlistindex+newValue.length);
	}  

  }




  private void fillListModel(MyListModel model, Object newValues[]) 
  {
    model.addAll(newValues);
  }
 private void fillListModel(MyListModel model, Object newValues[],int atindex) 
  {
    model.addAll(newValues,atindex);
  }
  public Iterator sourceIterator() {
    return sourceListModel.iterator();
  }

  public Iterator destinationIterator() {
    return destListModel.iterator();
  }

  public Iterator openerIterator() {
    return openerListModel.iterator();
  }


  public void setOpenerCellRenderer(ListCellRenderer newValue) {
    openerList.setCellRenderer(newValue);
  }

  public ListCellRenderer getOpenerCellRenderer() {
    return openerList.getCellRenderer();
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
    Object selected[] = ((MyListModel)sourceList.getModel()).toArray();
    for (int i = selected.length - 1; i >= 0; --i) {
      sourceListModel.removeElement(selected[i]);
    }
    sourceList.getSelectionModel().clearSelection();
  }

  private void clearDestinationAll() {
    Object selected[] = ((MyListModel)destList.getModel()).toArray();
    for (int i = selected.length - 1; i >= 0; --i) {
      destListModel.removeElement(selected[i]);
    }
    destList.getSelectionModel().clearSelection();
  }
  
  
  

  private void initScreen() {
    setBorder(BorderFactory.createEtchedBorder());
    setLayout(new GridBagLayout());
    sourceLabel = new JLabel(DEFAULT_SOURCE_CHOICE_LABEL);
	openerListModel = new MyListModel();
    openerList = new JList(openerListModel);
	
	
    sourceListModel = new MyListModel();
	
    sourceList = new JList(sourceListModel);
	sourceList.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent me) { 
            if (me.getClickCount() == 2) 
			{
					 addDestinationElements(new Object[] { sourceList.getSelectedValue() });
					 clearSourceSelected();
            }
         }
      });
    add(sourceLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    add(new JScrollPane(sourceList), new GridBagConstraints(0, 1, 1, 5, .5,
        1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        EMPTY_INSETS, 0, 0));
		
	showopen = new JCheckBox("Show Bookie Opener");
   add(showopen, new GridBagConstraints(0, 6, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));

	 showopen.addActionListener(new ShowOpenListener());	

    addButton = new JButton(ADD_BUTTON_LABEL);
    add(addButton, new GridBagConstraints(1, 1, 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    addButton.addActionListener(new AddListener());
	
	
	addAllButton = new JButton(ADD_ALL_BUTTON_LABEL);
    add(addAllButton, new GridBagConstraints(1, 2, 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    addAllButton.addActionListener(new AddAllListener());
	
	
	
    removeButton = new JButton(REMOVE_BUTTON_LABEL);
    add(removeButton, new GridBagConstraints(1, 4, 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 5, 0, 5), 0, 0));
    removeButton.addActionListener(new RemoveListener());
	
	removeAllButton = new JButton(REMOVE_ALL_BUTTON_LABEL);
    add(removeAllButton, new GridBagConstraints(1, 5, 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 5, 0, 5), 0, 0));
    removeAllButton.addActionListener(new RemoveAllListener());
	
	saveButton = new JButton(SAVE_BUTTON_LABEL);
    add(saveButton, new GridBagConstraints(1, 6, 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 5, 0, 5), 0, 0));
    saveButton.addActionListener(new SaveListener());	

    destLabel = new JLabel(DEFAULT_DEST_CHOICE_LABEL);
    destListModel = new MyListModel();
    destList = new JList(destListModel);
	destList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		destList.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent me) {
            if (me.getClickCount() == 2) 
			{
 					 System.out.println("Double clicked on " + destList.getSelectedValue());
					 addSourceElements(new Object[] { destList.getSelectedValue() });
					 clearDestinationSelected();
					 sourceListModel.sort(); // owen added 3/17/2021
					 sourceList.revalidate(); // owen added 3/17/2021
					 showopen.doClick();  
					 showopen.doClick();  
            }
         }
      });
	
	
    add(destLabel, new GridBagConstraints(2, 0, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    add(new JScrollPane(destList), new GridBagConstraints(2, 1, 1, 5, .5,
        1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        EMPTY_INSETS, 0, 0));
		
		
	upButton = new JButton(UP_BUTTON_LABEL)	;
	upButton.addActionListener(new MoveUpListener());
	downButton = new JButton(DOWN_BUTTON_LABEL)	;
	downButton.addActionListener(new MoveDownListener());
	add(upButton, new GridBagConstraints(3, 1, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));		
	add(downButton, new GridBagConstraints(3, 2, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));				
	upupButton = new JButton(UP_UP_BUTTON_LABEL)	;
	upupButton.addActionListener(new MoveUpUpListener());
	downdownButton = new JButton(DOWN_DOWN_BUTTON_LABEL)	;
	downdownButton.addActionListener(new MoveDownDownListener());
	add(upupButton, new GridBagConstraints(3, 3, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));		
	add(downdownButton, new GridBagConstraints(3, 4, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));					
		
		
		
  }

  public void init() {
    
    

		AppController.reorderBookiesVec();
		Vector hidden = AppController.getHiddenCols();
		Comparator<Bookie> byName= Comparator.comparing(Bookie::getName);
		Collections.sort(hidden,byName);
		Vector shown = AppController.getShownCols();
		Vector fixed = AppController.getFixedCols();
		
		
		for(int i=0; i < fixed.size();i++)
		{
			Bookie b = (Bookie)fixed.get(i);
			System.out.println("fixed adding "+b);
			this.addDestinationElements(new Object[] {b});
		}
		this.addDestinationElements(new Object[] {new Bookie(999,"<<<<FIXED COLUMN DIVIDER>>>>","<<<<FIXED COLUMN DIVIDER>>>>","","")});
		
		for(int i=0; i < shown.size();i++)
		{
			Bookie b = (Bookie)shown.get(i);
			System.out.println("shown adding "+b);
			this.addDestinationElements(new Object[] {b});
		}
		for(int i=0; i < hidden.size();i++)
		{
			Bookie b = (Bookie)hidden.get(i);
			System.out.println("hidden adding "+b);
			if(b.getName().indexOf("OPEN") != -1)
				{
					if(showopen.isSelected())
					{
						this.addSourceElements(new Object[] {b});						
					}
					else
					{
						this.addOpenerElements(new Object[] {b});
					}
				}
			else
			{
				this.addSourceElements(new Object[] {b});
				
				
			}
		}
		
	//owen kyrollos need to think about filtering opener bookie in list
    f.getContentPane().add(this, BorderLayout.CENTER);
    f.setSize(700, 700);
    f.setVisible(true);
  }

  public void moveItUp()
  {
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

  private class MoveUpListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {

	  moveItUp();
    }
  } 
  
  private class MoveUpUpListener implements ActionListener {
    public void actionPerformed(ActionEvent e) 
	{
      for(int i=0; i < 10; i++)
	  {
		 moveItUp();
	  }
	  
    }
  } 
  
  private class MoveDownDownListener implements ActionListener {
    public void actionPerformed(ActionEvent e) 
	{
      for(int i=0; i < 10; i++)
	  {
		  moveItDown();
	  }
	  
    }
  } 
  
  
  private class SaveListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
				Vector selectedlist = ((MyListModel)destList.getModel()).getModelVec();
				Vector availablelist = ((MyListModel)sourceList.getModel()).getModelVec();
				String fixedcols = "";
				String showncols = "";
				String hiddencols = "";
				boolean fixed = true;
				
				
				for(int i=0; i < selectedlist.size();i++)
				{
					Bookie b = (Bookie)selectedlist.get(i);
					int id = b.getBookie_id();
					
					if(id== 999) // fixed breaker
					{
						fixed = false;
						continue;
					}
					
					if(fixed)
						{
							fixedcols = fixedcols+","+id;
							
						}
					else
						{
							showncols = showncols+","+id;
						}						
				}
				for(int i=0; i < availablelist.size();i++)
				{
					Bookie b = (Bookie)availablelist.get(i);
					int id = b.getBookie_id();
					if(id== 999) // fixed breaker if this is here then no fixed cols 
					{
						continue;
					}
					hiddencols = ","+hiddencols;
					
				}		
				if(fixedcols.length() >1) {fixedcols = fixedcols.substring(1);}
				if(showncols.length() >1) {showncols = showncols.substring(1);}
				AppController.getUser().setBookieColumnPrefs(showncols);
				AppController.getUser().setFixedColumnPrefs(fixedcols);
				System.out.println(fixedcols);
				System.out.println(showncols);	  
				AppController.refreshTabs3();
				f.dispose();
	  
	  
	  
    }
  } 
  



   private class ShowOpenListener implements ActionListener {
   // public void itemStateChanged(ItemEvent e) {
	     public void actionPerformed(ActionEvent e) {
			 JCheckBox cb = (JCheckBox)e.getSource();
			 
		boolean showit = cb.isSelected();
				
		System.out.println("checkbox selected="+showit);
		if(showit)
		{
				for (Iterator<Bookie> iterator = openerListModel.iterator(); iterator.hasNext();) 
				{

					Bookie b = iterator.next();
					String name = ""+b.getName();
					if(name.indexOf("OPEN") != -1)
					{
						addSourceElements(new Object[] {b});
						iterator.remove();
					}
					
				}	
	
		}
		else
		{
				for (Iterator<Bookie> iterator = sourceListModel.iterator(); iterator.hasNext();) 
				{

					Bookie b = iterator.next();
					String name = ""+b.getName();
					if(name.indexOf("OPEN") != -1)
					{
						addOpenerElements(new Object[] {b});
						iterator.remove();
					}
					
				}				
		}
		
		sourceList.revalidate();
    }
   }

 private class MoveDownListener implements ActionListener {
    public void actionPerformed(ActionEvent e) 
	{
		moveItDown();
    }
  } 

  public void moveItDown()
  {
	  Object selected[] = destList.getSelectedValues();
 	  int[] selectedindices = destList.getSelectedIndices();

	  
      
	  destListModel.moveDown(selected);
	  int[] newselectedindices = new int[selectedindices.length];
	  for(int i =0; i <selectedindices.length;i++)
	  {
		 int oldindex = selectedindices[i];
		 if(oldindex != ((MyListModel)destList.getModel()).getSize())
		 {
			 newselectedindices[i] = oldindex+1;
		 }
	  }
	  destList.setSelectedIndices(newselectedindices);
	  destList.ensureIndexIsVisible(destList.getMaxSelectionIndex());
	  
	  
  }


  private class AddListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = sourceList.getSelectedValues();
	  System.out.println("JUST ADDED-"+sourceList.getSelectedValue().toString());
	  
	 /*
	 if(sourceList.getSelectedValue().toString().equals("Chart")){
		new com.sia.client.ui.ChartHome().setVisible(true);
	 }
	 */
      addDestinationElements(selected);
      clearSourceSelected();
    }
  }
  
  


   private class AddAllListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = ((MyListModel)sourceList.getModel()).toArray();
      addDestinationElements(selected);
      clearSourceAll();
    }
  }

  private class RemoveListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = destList.getSelectedValues();
      addSourceElements(selected);
      clearDestinationSelected();
	  
	  sourceListModel.sort(); // owen added 3/17/2021
	  sourceList.revalidate(); // owen added 3/17/2021
		showopen.doClick();  
		showopen.doClick();  
    }
  }
  
  private class RemoveAllListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = ((MyListModel)destList.getModel()).toArray();
      addSourceElements(selected);
      clearDestinationAll();
	  sourceListModel.sort(); // owen added 3/17/2021
	  sourceList.revalidate(); // owen added 3/17/2021
	showopen.doClick();  
	showopen.doClick();  
    }
  }
  
  
}

class MyListModel extends AbstractListModel {

  Vector model;

	public Vector getModelVec()
	{
		return model;
	}

  public MyListModel() {
    model = new Vector();
  }


  public void sort()
  {
	  
		Comparator<Bookie> byName= Comparator.comparing(Bookie::getName);
		Collections.sort(model,byName);
		fireContentsChanged(this, 0, getSize());
	    System.out.println("JUST SORTED THE MODEL!!!");
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
public void add(Object element,int atindex) 
  {
	  
	 
	 model.add(atindex,element);
     fireContentsChanged(this, 0, getSize());
    
  }
  public void addAll(Object elements[]) 
  {
    Collection c = Arrays.asList(elements);
    model.addAll(c);
    fireContentsChanged(this, 0, getSize());
  }
 public void addAll(Object elements[],int atindex) 
  {
    Collection c = Arrays.asList(elements);
    model.addAll(atindex,c);
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
           