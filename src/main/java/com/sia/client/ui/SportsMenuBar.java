package com.sia.client.ui;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class SportsMenuBar extends JMenuBar
{
	 
	 SportsTabPane stb;
	TopView tv;
	JMenu filemenu = new JMenu("File");
	JMenu bookiemenu = new JMenu("Columns");
	JMenu linealertsmenu = new JMenu("Line Alerts");
	JMenu gamealertsmenu = new JMenu("Game Alerts");
	JMenu tabsmenu = new JMenu("Tabs");
	JMenu windowmenu = new JMenu("Window");
	
	public SportsMenuBar()
	{
		super();
		this.init();
	}
	public SportsMenuBar(SportsTabPane stb, TopView tv)
	{
		super();
		this.stb = stb;
		this.tv = tv;
		this.init();
		AppController.addMenuBar(this);
	}


    public void init()
	{
		
		add(filemenu);
		
					JMenuItem logout = new JMenuItem("Exit...");
					logout.addActionListener(new ActionListener() {
				  public void actionPerformed(ActionEvent ev) {
					  // need to store user prefs
					System.exit(0);
				  }
				});
			filemenu.add(logout);
			
		add(bookiemenu);
		
			JMenuItem bookiecolumn = new JMenuItem("Manage");
			bookiecolumn.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					 // AudioClip clipfinal = new AudioClip("c:\\spankoddsclient\\final.wav");
					//  clipfinal.play();
						   SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
										   BookieColumnController2 bcc2 =  new BookieColumnController2();
												
							
						}
					});        

			   
					  

				}
			});
			JMenuItem bookiecolumn1 = new JMenuItem("Chart");
			bookiecolumn1.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					 // AudioClip clipfinal = new AudioClip("c:\\spankoddsclient\\final.wav");
					//  clipfinal.play();
						   SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
										   new ChartHome().setVisible(true);
												
							
						}
					});        

			   
					  

				}
			});
			bookiemenu.add(bookiecolumn);
			bookiemenu.add(bookiecolumn1);
		
		add(linealertsmenu);
		
		
			JMenuItem generallinealert = new JMenuItem("Line Moves");
						generallinealert.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  LineAlert la = new LineAlert("Started");
				}
				});
			JMenuItem majorlinemove = new JMenuItem("Line Seekers");
			JMenuItem openers = new JMenuItem("Openers");
			openers.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  LineAlertOpeners la = new LineAlertOpeners();
				}
				});
			linealertsmenu.add(generallinealert);	
			linealertsmenu.add(majorlinemove);	
			linealertsmenu.add(openers);	
		
		
		
		add(gamealertsmenu);
		
			JMenuItem started = new JMenuItem("Started");	
			started.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  GameAlert ga = new GameAlert("Started");
				}
				});
			JMenuItem finals = new JMenuItem("Finals");
			finals.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  GameAlert ga = new GameAlert("Final");
				}
				});							
			JMenuItem halftimes = new JMenuItem("Halftimes");
			halftimes.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  GameAlert ga = new GameAlert("Halftime");
				}
				});			
				
			JMenuItem lineups = new JMenuItem("Lineups");
			lineups.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  GameAlert ga = new GameAlert("Lineup");
				}
				});			
			JMenuItem officials = new JMenuItem("Officials");
			officials.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  GameAlert ga = new GameAlert("Official");
				}
				});		
			JMenuItem injuries = new JMenuItem("Injuries");	
			injuries.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  GameAlert ga = new GameAlert("Injury");
				}
				});					
			JMenuItem timechange = new JMenuItem("Time Changes");		
			timechange.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  GameAlert ga = new GameAlert("Time Change");
				}
				});			
			JMenuItem limitchange = new JMenuItem("Limit Changes");					
			limitchange.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  GameAlert ga = new GameAlert("Limit Change");
				}
				});						
			
			
			JMenuItem test = new JMenuItem("Test");
			test.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!");
				}
				});		
				
			JMenuItem test2 = new JMenuItem("Test NW");
			test2.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!",10000,SwingConstants.NORTH_WEST,AppController.getMainTabPane());
				}
				});								
			JMenuItem test3 = new JMenuItem("Test SW");
			test3.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!",10000,SwingConstants.SOUTH_WEST,AppController.getMainTabPane());
				}
				});				
			JMenuItem test4 = new JMenuItem("Test SE");
			test4.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					  UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!",10000,SwingConstants.SOUTH_EAST, AppController.getMainTabPane());
				}
				});						
				gamealertsmenu.add(started);	
				gamealertsmenu.add(finals);	
				gamealertsmenu.add(halftimes);				
				gamealertsmenu.add(lineups);				
				gamealertsmenu.add(officials);				
				gamealertsmenu.add(injuries);				
				gamealertsmenu.add(timechange);				
				gamealertsmenu.add(limitchange);				
				gamealertsmenu.add(test);				
				//gamealertsmenu.add(test2);				
				//gamealertsmenu.add(test3);				
				//gamealertsmenu.add(test4);				
			
		
		
		add(tabsmenu);
			populateTabsMenu();
			
		
		
		add(windowmenu);
			JMenuItem newwindow = new JMenuItem("New..");
			newwindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10,0)); // 0 means no modifiers
			newwindow.addActionListener(AppController.getNewWindowAction());
			windowmenu.add(newwindow);		

	
		
	

		

		
		
		
		
	}

  public void populateTabsMenu()
  {
			tabsmenu.removeAll();
			//SportsTabPane stb=new SportsTabPane();
			//com.sia.client.ui.AppController.addTabPane(stb);
		//********************Football*******************	
			JMenu football = new JMenu("Football");		
			tabsmenu.add(football);
			football.addMenuListener(new MenuListener() {

       public void menuSelected(MenuEvent e) {
		   football.removeAll();
			JMenuItem go=new JMenuItem("Goto");
			JMenuItem manage=new JMenuItem("Manage");
			JMenuItem hide=new JMenuItem("Hide");
			JMenuItem unhide=new JMenuItem("unHide");
			int tc=stb.getTabCount();
			int tp=0;
			int j;
			Vector tabpanes = AppController.getTabPanes();
			
				
			for(j=0;j<tc;j++)
			{
				if(stb.getTitleAt(j).equalsIgnoreCase("Football"))
				{
					tp=1;
					break;
				}
			}
			football.add(go);
			go.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						stb.setSelectedIndex(stb.indexOfTab("Football"));
					}
				});
			football.add(manage);
			manage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						new SportCustomTab("Football",stb.indexOfTab("Football"));
					}
				});
			if(tp==1)
			{
				football.add(hide); 
					hide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						
						int idx=stb.indexOfTab("Football");
						
							AppController.SpotsTabPaneVector.remove(0);
														
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							
						tp.remove(idx);
							
							tp.setSelectedIndex(0);
							
						}	
						go.setEnabled(false);
					}
				});
			}
			else{
				football.add(unhide);
				unhide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						AppController.SpotsTabPaneVector.put(0,"Football");
							Vector currentvec=AppController.getMainTabVec();
							int idx=currentvec.indexOf("Football");
						
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
														
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.insertTab("Football",new ImageIcon("football.png"),new MainScreen("Football"),"Football",idx);
							tp.setSelectedIndex(tp.indexOfTab("Football"));
									
						}
						
						go.setEnabled(true);
					}
				});
				go.setEnabled(false);
			}
			}	
	   
	     public void menuDeselected(MenuEvent e) {}
		   public void menuCanceled(MenuEvent e) {}
			});
				
			//*******Basketball	**************
			JMenu basketball = new JMenu("Basketball");					
			tabsmenu.add(basketball);
			basketball.addMenuListener(new MenuListener() {

       public void menuSelected(MenuEvent e) {
		   basketball.removeAll();
			JMenuItem go=new JMenuItem("Goto");
			JMenuItem manage=new JMenuItem("Manage");
			JMenuItem hide=new JMenuItem("Hide");
			JMenuItem unhide=new JMenuItem("unHide");
			int tc=stb.getTabCount();
			int tp=0;
			int j;
			for(j=0;j<tc;j++)
			{
				if(stb.getTitleAt(j).equalsIgnoreCase("Basketball"))
				{
					tp=1;
					break;
				}
			}
			basketball.add(go);
			go.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						stb.setSelectedIndex(stb.indexOfTab("Basketball"));
					}
				});
			basketball.add(manage);
			manage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						new SportCustomTab("Basketball",stb.indexOfTab("Basketball"));
					}
				});
			if(tp==1)
			{
				basketball.add(hide); 
					hide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						int idx=stb.indexOfTab("Basketball");
						AppController.SpotsTabPaneVector.remove(1);
														
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.setSelectedIndex(0);
							tp.remove(idx);
						}	
						go.setEnabled(false);
					}
				});
			}
			else{
				basketball.add(unhide);
				unhide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						
						AppController.SpotsTabPaneVector.put(1,"Basketball");
						Vector currentvec=AppController.getMainTabVec();
						int idx=currentvec.indexOf("Basketball");							
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.insertTab("Basketball",new ImageIcon("Basketball.png"),new MainScreen("Basketball"),"Basketball",idx);
							tp.setSelectedIndex(tp.indexOfTab("Basketball"));
									
						}
						
						go.setEnabled(true);
					}
				});
				go.setEnabled(false);
			}
			
	   }
	     public void menuDeselected(MenuEvent e) {}
		   public void menuCanceled(MenuEvent e) {}
			});	
			//***********Baseball***********************
			JMenu baseball = new JMenu("Baseball");					
			tabsmenu.add(baseball);
			baseball.addMenuListener(new MenuListener() {

       public void menuSelected(MenuEvent e) {
		   baseball.removeAll();
			JMenuItem go=new JMenuItem("Goto");
			JMenuItem manage=new JMenuItem("Manage");
			JMenuItem hide=new JMenuItem("Hide");
			JMenuItem unhide=new JMenuItem("unHide");
			int tc=stb.getTabCount();
			int tp=0;
			int j;
			for(j=0;j<tc;j++)
			{
				if(stb.getTitleAt(j).equalsIgnoreCase("Baseball"))
				{
					tp=1;
					break;
				}
			}
			baseball.add(go);
			go.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						stb.setSelectedIndex(stb.indexOfTab("Baseball"));
					}
				});
			baseball.add(manage);
			manage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						new SportCustomTab("Baseball",stb.indexOfTab("Baseball"));
					}
				});
			if(tp==1)
			{
				baseball.add(hide); 
					hide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						int idx=stb.indexOfTab("Baseball");
						AppController.SpotsTabPaneVector.remove(2);
														
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.setSelectedIndex(0);
							tp.remove(idx);
						}	
						go.setEnabled(false);
					}
				});
			}
			else{
				baseball.add(unhide);
				unhide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						AppController.SpotsTabPaneVector.put(2,"Baseball");
						Vector currentvec=AppController.getMainTabVec();
						int idx=currentvec.indexOf("Baseball");								
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.insertTab("Baseball",new ImageIcon("Baseball.png"),new MainScreen("Baseball"),"Baseball",idx);
							tp.setSelectedIndex(tp.indexOfTab("Baseball"));
									
						}
						
						go.setEnabled(true);
					}
				});
				go.setEnabled(false);
			}
			
	   }
	     public void menuDeselected(MenuEvent e) {}
		   public void menuCanceled(MenuEvent e) {}
			});	
			
			
			//***********************hockey*********************
			JMenu hockey = new JMenu("Hockey");					
			tabsmenu.add(hockey);
			hockey.addMenuListener(new MenuListener() {

       public void menuSelected(MenuEvent e) {
		   hockey.removeAll();
			JMenuItem go=new JMenuItem("Goto");
			JMenuItem manage=new JMenuItem("Manage");
			JMenuItem hide=new JMenuItem("Hide");
			JMenuItem unhide=new JMenuItem("unHide");
			int tc=stb.getTabCount();
			int tp=0;
			int j;
			for(j=0;j<tc;j++)
			{
				if(stb.getTitleAt(j).equalsIgnoreCase("Hockey"))
				{
					tp=1;
					break;
				}
			}
			hockey.add(go);
			go.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						stb.setSelectedIndex(stb.indexOfTab("Hockey"));
					}
				});
			hockey.add(manage);
			manage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						new SportCustomTab("Hockey",stb.indexOfTab("Hockey"));
					}
				});
			if(tp==1)
			{
				hockey.add(hide); 
					hide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						int idx=stb.indexOfTab("Hockey");
						AppController.SpotsTabPaneVector.remove(3);
														
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.setSelectedIndex(0);
							tp.remove(idx);
						}	
						go.setEnabled(false);
					}
				});
			}
			else{
				hockey.add(unhide);
				unhide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						AppController.SpotsTabPaneVector.put(3,"Hockey");
						Vector currentvec=AppController.getMainTabVec();
						int idx=currentvec.indexOf("Hockey");	
						
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.insertTab("Hockey",new ImageIcon("Hockey.png"),new MainScreen("Hockey"),"Hockey",idx);
							tp.setSelectedIndex(tp.indexOfTab("Hockey"));
									
						}
						go.setEnabled(true);
					}
				});
				go.setEnabled(false);
			}
			
	   }
	     public void menuDeselected(MenuEvent e) {}
		   public void menuCanceled(MenuEvent e) {}
			});
				
			//***********************fighting****************************
			JMenu fighting = new JMenu("Fighting");					
			tabsmenu.add(fighting);
			fighting.addMenuListener(new MenuListener() {

       public void menuSelected(MenuEvent e) {
		   fighting.removeAll();
			JMenuItem go=new JMenuItem("Goto");
			JMenuItem manage=new JMenuItem("Manage");
			JMenuItem hide=new JMenuItem("Hide");
			JMenuItem unhide=new JMenuItem("unHide");
			int tc=stb.getTabCount();
			int tp=0;
			int j;
			for(j=0;j<tc;j++)
			{
				if(stb.getTitleAt(j).equalsIgnoreCase("Fighting"))
				{
					tp=1;
					break;
				}
			}
			fighting.add(go);
			go.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						stb.setSelectedIndex(stb.indexOfTab("Fighting"));
					}
				});
			fighting.add(manage);
			manage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						new SportCustomTab("Fighting",stb.indexOfTab("Fighting"));
					}
				});
			if(tp==1)
			{
				fighting.add(hide); 
					hide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						int idx=stb.indexOfTab("Fighting");
						AppController.SpotsTabPaneVector.remove(4);
														
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.setSelectedIndex(0);
							tp.remove(idx);
						}	
						go.setEnabled(false);
					}
				});
			}
			else{
				fighting.add(unhide);
				unhide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						AppController.SpotsTabPaneVector.put(4,"Fighting");
						Vector currentvec=AppController.getMainTabVec();
						int idx=currentvec.indexOf("Fighting");
						
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.insertTab("Fighting",new ImageIcon("boxing.png"),new MainScreen("Fighting"),"Fighting",idx);
							tp.setSelectedIndex(tp.indexOfTab("Fighting"));
									
						}
						go.setEnabled(true);
					}
				});
				go.setEnabled(false);
			}
			
	   }
	     public void menuDeselected(MenuEvent e) {}
		   public void menuCanceled(MenuEvent e) {}
			});	
			
			
			//*****************soccer*************************
			JMenu soccer = new JMenu("Soccer");					
			tabsmenu.add(soccer);
			soccer.addMenuListener(new MenuListener() {

       public void menuSelected(MenuEvent e) {
		   soccer.removeAll();
			JMenuItem go=new JMenuItem("Goto");
			JMenuItem manage=new JMenuItem("Manage");
			JMenuItem hide=new JMenuItem("Hide");
			JMenuItem unhide=new JMenuItem("unHide");
			int tc=stb.getTabCount();
			int tp=0;
			int j;
			for(j=0;j<tc;j++)
			{
				if(stb.getTitleAt(j).equalsIgnoreCase("Soccer"))
				{
					tp=1;
					break;
				}
			}
			soccer.add(go);
			go.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						stb.setSelectedIndex(stb.indexOfTab("Soccer"));
					}
				});
			soccer.add(manage);
			manage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						new SportCustomTab("Soccer",stb.indexOfTab("Soccer"));
					}
				});
			if(tp==1)
			{
				soccer.add(hide); 
					hide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						int idx=stb.indexOfTab("Soccer");
						AppController.SpotsTabPaneVector.remove(5);
														
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.setSelectedIndex(0);
							tp.remove(idx);
						}	
						go.setEnabled(false);
					}
				});
			}
			else{
				soccer.add(unhide);
				unhide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						AppController.SpotsTabPaneVector.put(5,"Soccer");
						Vector currentvec=AppController.getMainTabVec();
						int idx=currentvec.indexOf("Soccer");	
						
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.insertTab("Soccer",new ImageIcon("soccer.png"),new MainScreen("Soccer"),"Soccer",idx);
							tp.setSelectedIndex(tp.indexOfTab("Soccer"));
									
						}
						
						go.setEnabled(true);
					}
				});
				go.setEnabled(false);
			}
			
	   }
	     public void menuDeselected(MenuEvent e) {}
		   public void menuCanceled(MenuEvent e) {}
			});	
			
			
			//************************golf*********************
			JMenu golf = new JMenu("Golf");					
			tabsmenu.add(golf);
			golf.addMenuListener(new MenuListener() {

       public void menuSelected(MenuEvent e) {
		   golf.removeAll();
			JMenuItem go=new JMenuItem("Goto");
			JMenuItem manage=new JMenuItem("Manage");
			JMenuItem hide=new JMenuItem("Hide");
			JMenuItem unhide=new JMenuItem("unHide");
			int tc=stb.getTabCount();
			int tp=0;
			int j;
			for(j=0;j<tc;j++)
			{
				if(stb.getTitleAt(j).equalsIgnoreCase("Golf"))
				{
					tp=1;
					break;
				}
			}
			golf.add(go);
			go.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						stb.setSelectedIndex(stb.indexOfTab("Golf"));
					}
				});
			golf.add(manage);
			manage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						new SportCustomTab("Golf",stb.indexOfTab("Golf"));
					}
				});
			if(tp==1)
			{
				golf.add(hide); 
					hide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						int idx=stb.indexOfTab("Golf");
						AppController.SpotsTabPaneVector.remove(7);
														
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.setSelectedIndex(0);
							tp.remove(idx);
						}	
						go.setEnabled(false);
					}
				});
			}
			else{
				golf.add(unhide);
				unhide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						AppController.SpotsTabPaneVector.put(7,"Golf");
						Vector currentvec=AppController.getMainTabVec();
						int idx=currentvec.indexOf("Golf");	
						
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.insertTab("Golf",new ImageIcon("golf.png"),new MainScreen("Golf"),"Golf",idx);
							tp.setSelectedIndex(tp.indexOfTab("Golf"));
									
						}
						go.setEnabled(true);
					}
				});
				go.setEnabled(false);
			}
			
	   }
	     public void menuDeselected(MenuEvent e) {}
		   public void menuCanceled(MenuEvent e) {}
			});	
			
			
			//**********************autoracing****************************
			JMenu autoracing = new JMenu("Auto Racing");					
			tabsmenu.add(autoracing);
			autoracing.addMenuListener(new MenuListener() {

       public void menuSelected(MenuEvent e) {
		   autoracing.removeAll();
			JMenuItem go=new JMenuItem("Goto");
			JMenuItem manage=new JMenuItem("Manage");
			JMenuItem hide=new JMenuItem("Hide");
			JMenuItem unhide=new JMenuItem("unHide");
			int tc=stb.getTabCount();
			int tp=0;
			int j;
			for(j=0;j<tc;j++)
			{
				if(stb.getTitleAt(j).equalsIgnoreCase("Auto Racing"))
				{
					tp=1;
					break;
				}
			}
			autoracing.add(go);
			go.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						stb.setSelectedIndex(stb.indexOfTab("Auto Racing"));
					}
				});
			autoracing.add(manage);
			manage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						new SportCustomTab("Auto Racing",stb.indexOfTab("Auto Racing"));
					}
				});
			if(tp==1)
			{
				autoracing.add(hide); 
					hide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						int idx=stb.indexOfTab("Auto Racing");
						AppController.SpotsTabPaneVector.remove(6);
														
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.setSelectedIndex(0);
							tp.remove(idx);
						}	
						go.setEnabled(false);
					}
				});
			}
			else{
				autoracing.add(unhide);
				unhide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						AppController.SpotsTabPaneVector.put(6,"Auto Racing");
						Vector currentvec=AppController.getMainTabVec();
						int idx=currentvec.indexOf("Auto Racing");	 
						
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.insertTab("Auto Racing",new ImageIcon("flag.png"),new MainScreen("Auto Racing"),"Auto Racing",idx);
							tp.setSelectedIndex(tp.indexOfTab("Auto Racing"));
									
						}
						
						go.setEnabled(true);
					}
				});
				go.setEnabled(false);
			}
			
	   }
	     public void menuDeselected(MenuEvent e) {}
		   public void menuCanceled(MenuEvent e) {}
			});
			
			//*******Tennis*****************
			JMenu tennis = new JMenu("Tennis");					
			tabsmenu.add(tennis);
			tennis.addMenuListener(new MenuListener() {

       public void menuSelected(MenuEvent e) {
		   tennis.removeAll();
			JMenuItem go=new JMenuItem("Goto");
			JMenuItem manage=new JMenuItem("Manage");
			JMenuItem hide=new JMenuItem("Hide");
			JMenuItem unhide=new JMenuItem("unHide");
			int tc=stb.getTabCount();
			int tp=0;
			int j;
			for(j=0;j<tc;j++)
			{
				if(stb.getTitleAt(j).equalsIgnoreCase("Tennis"))
				{
					tp=1;
					break;
				}
			}
			tennis.add(go);
			go.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						stb.setSelectedIndex(stb.indexOfTab("Tennis"));
					}
				});
			tennis.add(manage);
			manage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						new SportCustomTab("Tennis",stb.indexOfTab("Tennis"));
					}
				});
			if(tp==1)
			{
				tennis.add(hide); 
					hide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						int idx=stb.indexOfTab("Tennis");
						AppController.SpotsTabPaneVector.remove(8);					
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.setSelectedIndex(0);
							tp.remove(idx);
						}	
						go.setEnabled(false);
					}
				});
			}
			else{
				tennis.add(unhide);
				unhide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						AppController.SpotsTabPaneVector.put(8,"Tennis");
						Vector currentvec=AppController.getMainTabVec();
						int idx=currentvec.indexOf("Tennis");	 
						
						Vector tabpanes = AppController.getTabPanes();
						System.out.println("tabpanes size= "+tabpanes.size());
						for(int i=0; i< tabpanes.size(); i++)
						{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
							tp.insertTab("Tennis",new ImageIcon("tennis.png"),new MainScreen("Tennis"),"Tennis",idx);
							tp.setSelectedIndex(tp.indexOfTab("Tennis"));
									
						}
						go.setEnabled(true);
					}
				});
				go.setEnabled(false);
			}
			
	   }
	     public void menuDeselected(MenuEvent e) {}
		   public void menuCanceled(MenuEvent e) {}
			});
		//***********************end of Tab Menus******************************
			
			for(int i=0; i <AppController.customTabsVec.size(); i++)
			{
				String tabinfo = (String)AppController.customTabsVec.elementAt(i);
				String[] tabarr = tabinfo.split("\\*");
				String tabname = tabarr[1];
				JMenu temp = new JMenu(tabname);
				tabsmenu.add(temp);
				JMenuItem go=new JMenuItem("Goto");
				JMenuItem manage=new JMenuItem("Edit");
				JMenuItem hide=new JMenuItem("Remove");
			
			
				temp.add(go);
				go.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						stb.setSelectedIndex(stb.indexOfTab(temp.getText()));
					}
				});
				temp.add(manage);
				manage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	

						SwingUtilities.invokeLater(new Runnable()
							{
							public void run()
							{	
								int idx=stb.indexOfTab(temp.getText());
								new CustomTab2(stb.getTitleAt(idx),idx);
														
								}
								});								

					}
				});
				
				temp.add(hide);
				hide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	

						//stb.remove(idx);
						SwingUtilities.invokeLater(new Runnable()
									{
									public void run()
									{					
										int idx=stb.indexOfTab(temp.getText());
										AppController.removeCustomTab(stb.getTitleAt(idx));
										Vector tabpanes = AppController.getTabPanes();
										System.out.println("tabpanes size= "+tabpanes.size());
										for(int j=0; j< tabpanes.size(); j++)
										{
											SportsTabPane tp = (SportsTabPane)tabpanes.get(j);
											tp.setSelectedIndex(0);
											tp.remove(idx);
										}		
										go.setEnabled(false);
										hide.setEnabled(false);
									}
									});							
						
						
						
						
					}
				});
				
				
					
				
				
				
			/*
				temp.addMenuListener(new MenuListener() {
				
       public void menuSelected(MenuEvent e) {
		   temp.removeAll();
		   String menuname=temp.getText();
			JMenuItem go=new JMenuItem("Goto");
			JMenuItem manage=new JMenuItem("Edit");
			JMenuItem hide=new JMenuItem("remove");
			JMenuItem unhide=new JMenuItem("unHide");
			int tc=stb.getTabCount();
			int tp=0;
			int j;
			for(j=0;j<tc;j++)
			{
				if(stb.getTitleAt(j).equalsIgnoreCase(menuname))
				{
					tp=1;
					break;
				}
			}
			tennis.add(go);
			go.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						stb.setSelectedIndex(stb.indexOfTab(menuname));
					}
				});
			tennis.add(manage);
			manage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						new CustomTab2();
					}
				});
			if(tp==1)
			{
				tennis.add(hide); 
					hide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{	
						int idx=stb.indexOfTab(menuname);
						stb.remove(idx);
						go.setEnabled(false);
					}
				});
			}
			else{
				tennis.add(unhide);
				unhide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						stb.insertTab(menuname,null,null,menuname,8);
						stb.setSelectedIndex(stb.indexOfTab(menuname));
						go.setEnabled(true);
					}
				});
				go.setEnabled(false);
			}
			
	   }
	     public void menuCanceled(MenuEvent me){}
		 public void menuDeselected(MenuEvent me){}
				});	*/	
				





/*

										editItem.addActionListener(new ActionListener() {

											@Override
											public void actionPerformed(ActionEvent e) 
											{
													SwingUtilities.invokeLater(new Runnable()
													{
													public void run()
													{					
														new CustomTab2(thispane.getTitleAt(tabindex),tabindex);
														
													}
													});								
												
												
												

											}
										});
										removeItem.addActionListener(new ActionListener() {

											@Override
											public void actionPerformed(ActionEvent e) 
											{
													SwingUtilities.invokeLater(new Runnable()
													{
													public void run()
													{					
													    com.sia.client.ui.AppController.removeCustomTab(thispane.getTitleAt(tabindex));
														Vector tabpanes = com.sia.client.ui.AppController.getTabPanes();
														System.out.println("tabpanes size= "+tabpanes.size());
													    for(int i=0; i< tabpanes.size(); i++)
														{
															SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
															tp.setSelectedIndex(0);
															tp.remove(tabindex);
														}		
														
													}
													});														
												

											}
										});

				*/
				
				
			}	


			JMenuItem addnew = new JMenuItem("Add New...");					
			tabsmenu.add(addnew);
			addnew.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent ae)
				{
					SwingUtilities.invokeLater(new Runnable()
					{
					public void run()
					{					
						new CustomTab2();
						
					}
					});								  
				}
				});						
		


			
	  
  }
 /* public static void papulateSubMenus(){
/*	i=0;
	j=0;
  for(i=0;i<9;i++)
			{
			tabsmenu.getItem(i).removeAll();	
			JMenuItem go=new JMenuItem("Goto");
			JMenuItem manage=new JMenuItem("Manage");
			JMenuItem hide=new JMenuItem("Hide");
			JMenuItem unhide=new JMenuItem("unHide");
			int tc=stb.getTabCount();
			int tp=0;
			
			for(j=0;j<tc;j++)
			{
				if(stb.getTitleAt(j).equalsIgnoreCase(tabsmenu.getItem(i).getLabel()))
				{
					tp=1;
					break;
				}
			}
			tabsmenu.getItem(i).add(go);
			tabsmenu.getItem(i).add(manage);
			if(tp==1)
			{
				tabsmenu.getItem(i).add(hide);
				hide.addActionListener(new ActionListener(){
					int j1=j;
				public void	actionPerformed(ActionEvent ae){
						SwingUtilities.invokeLater(new Runnable()
					{
					public void run()
					{					
						stb.remove(j1);
					
					}
					});		
						
				
			}
			}
			);
			}
			else{
				tabsmenu.getItem(i).add(unhide);
				go.setEnabled(false);
				unhide.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						int i1=i;
						SwingUtilities.invokeLater(new Runnable()
					{
					public void run()
					{	
						if(tabsmenu.getItem(i1).getLabel().equalsIgnoreCase("Football"))
						{		
							stb.insertTab("Football",new ImageIcon("football.png"),new MainScreen("Football"),"Football",0);
						
						}
						else if(tabsmenu.getItem(i1).getLabel().equalsIgnoreCase("Basketball"))
						{		
							stb.insertTab("Basketball",new ImageIcon("basketball.png"),new MainScreen("Basketball"),"Basketball",0);
						
						}
						else if(tabsmenu.getItem(i1).getLabel().equalsIgnoreCase("Baseball"))
						{		
							stb.insertTab("Baseball",new ImageIcon("baseball.png"),new MainScreen("Baseball"),"Baseball",0);
						
						}
						else if(tabsmenu.getItem(i1).getLabel().equalsIgnoreCase("Hockey"))
						{		
							stb.insertTab("Hockey",new ImageIcon("hockey.png"),new MainScreen("Hockey"),"Hockey",0);
						
						}
						else if(tabsmenu.getItem(i1).getLabel().equalsIgnoreCase("Fighting"))
						{		
							stb.insertTab("Fighting",new ImageIcon("boxing.png"),new MainScreen("Fighting"),"Fighting",0);
						
						}
						else if(tabsmenu.getItem(i1).getLabel().equalsIgnoreCase("Soccer"))
						{		
							stb.insertTab("Soccer",new ImageIcon("soccer.png"),new MainScreen("Soccer"),"Soccer",0);
						
						}
						else if(tabsmenu.getItem(i1).getLabel().equalsIgnoreCase("Auto Racing"))
						{		
							stb.insertTab("Auto Racing",new ImageIcon("flag.png"),new MainScreen("Auto Racing"),"Auto Racing",0);
						
						}
						else if(tabsmenu.getItem(i1).getLabel().equalsIgnoreCase("Golf"))
						{		
							stb.insertTab("Golf",new ImageIcon("golf.png"),new MainScreen("Golf"),"Golf",0);
						
						}
						else
						{		
							stb.insertTab("Tennis",new ImageIcon("tennis.png"),new MainScreen("Tennis"),"Tennis",0);
						
						}
						go.setEnabled(true);
						
					}
					});		
			}
			
			
			});
			
  }
  
			}
  }*/
 public void refresh()
 {
	 
	 
 }


}