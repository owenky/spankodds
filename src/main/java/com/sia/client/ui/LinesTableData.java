package com.sia.client.ui;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.Container;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

//public class LinesTableData extends AbstractTableModel implements TableColumnModelListener {
	public class LinesTableData extends DefaultTableModel implements TableColumnModelListener {
	//	public class LinesTableData extends AbstractSpanTableModel implements TableColumnModelListener {

   public Vector<ColumnData> m_columns = new Vector();
public String display = AppController.getDisplayType();
public int period = 0;
public Hashtable<String,TableColumn> tablecolumnhash = new Hashtable();
//Vector<String> gamesIdVec = com.sia.client.ui.AppController.getGamesIdVec();
Vector<Game> gamesVec;
Hashtable<String,Bookie> bookies = AppController.getBookies();
Vector<Bookie> bookiesVec = AppController.getBookiesVec();

boolean timesort = false;
boolean shortteam = false;
boolean inview = false;
Vector gamesIdVec;

  protected SimpleDateFormat m_frm;
  protected NumberFormat m_volumeFormat;	// NEW
  public String sport = "";
  protected Date   m_date;
	Object[][] stv;
 boolean showingOpener = false;	
 boolean showingPrior = false;	
	int lastbookieremoved = 0;
	long cleartime = 100;
	JTable thistable;
  public LinesTableData() {
    m_frm = new SimpleDateFormat("MM/dd/yyyy");

    setInitialData();
  }
 public LinesTableData(long cleartime,Vector gamesVec) {
    m_frm = new SimpleDateFormat("MM/dd/yyyy");
	this.cleartime = cleartime;
	this.gamesVec = gamesVec;
	
    setInitialData();
  }
 public LinesTableData(String display,int period,long cleartime,Vector gamesVec,JTable thetable,boolean timesort,boolean shortteam,boolean opener,boolean last) {
    m_frm = new SimpleDateFormat("MM/dd/yyyy");
	this.cleartime = cleartime;
	this.gamesVec = gamesVec;
	this.timesort = timesort;
	this.shortteam = shortteam;
	this.display = display;
	this.period = period;
	thistable = thetable;
    setInitialData();
	if(opener)
	{
		showOpener();
	}
	else if(last)
	{
		showPrior();
	}
  }

public void setInView(boolean b)
{
	inview = b;
}
public boolean isInView()
{
	return inview;
}

/*
public boolean isCellSpanOn() {
return true;
}


public CellSpan getCellSpanAt(int rowIndex, int columnIndex)
 {

if (rowIndex % 5 == 0 ) 
{
return new CellSpan(rowIndex, 0, 1, getColumnCount());
}
else
{
	return null;
}

//return null;
}
*/


/*
public TableColumn addColumn(int bookieid)
{
	Bookie b = getBookie(bookieid);
	System.out.println("OLD COLUMN COUNT IS "+getColumnCount());
	System.out.println("OLD COLUMN COUNT IS "+getColumnCount());
	TableColumn column = new TableColumn(getColumnCount(),78, new LineRenderer(), null);
	m_columns.add(new com.sia.client.ui.ColumnData(b.getBookie_id(), b.toString(),78, JLabel.RIGHT) );
	tablecolumnhash.put(""+bookieid,column);
	printStv(3);
		for(int i=0; i< gamesIdVec.size(); i++)
		{
				String gameid = gamesIdVec.get(i);
				if(bookieid == 990)
					{

						stv[i][getColumnCount()-1] = new InfoView(Integer.parseInt(gameid));
						
					}
					else if(bookieid == 991)
					{
						stv[i][getColumnCount()-1] = new TimeView(Integer.parseInt(gameid));
					}
					else if(bookieid == 992)
					{
						stv[i][getColumnCount()-1] = new GameNumberView(Integer.parseInt(gameid));
					}
					else if(bookieid == 993)
					{
						stv[i][getColumnCount()-1] = new TeamView(Integer.parseInt(gameid));
					}
					
					else
					{
					 stv[i][getColumnCount()-1] = new SpreadTotalView(bookieid,Integer.parseInt(gameid));
					}
		
				
		}
	printStv(3);	
	return column;
	
}  
	
public TableColumn removeColumn(int bookieid)
{
	System.out.println("OLD COLUMN COUNT BEFORE REMOVAL "+getColumnCount());
	printStv(3);
	for(int cnt =0; cnt <m_columns.size();cnt++)
	{
		com.sia.client.ui.ColumnData cd = m_columns.get(cnt);
		if(cd.bookie_id == bookieid)
		{
			
			// now i need to change the data and move it down
			for(int cnt2 = cnt; cnt2 <m_columns.size();cnt2++)
			{
				for(int row=0;row<getRowCount();row++)
				{
					stv[row][cnt2] = stv[row][cnt2+1];
				}
			}
			
			m_columns.remove(cnt);
			break;
		}
		
	}
	TableColumn column = tablecolumnhash.get(""+bookieid);
	lastbookieremoved = bookieid;
	System.out.println("NEW COLUMN COUNT AFTER REMOVAL "+getColumnCount());
	printStv(3);
	return column;
	
	
}  	
*/


public long getClearTime()
{
	return cleartime;
}
public void printStv(int gameid)
{
	for(int j=0;j <getColumnCount();j++)
	{
		//System.out.println("game="+gameid+".."+stv[gameid][j]+"..."+m_columns.get(j));
		System.out.println("j="+j+"...."+m_columns.get(j));
		
	}
	
}

public void columnAdded(TableColumnModelEvent e)
{
	System.out.println("COLUMN ADDED!!! ");	
	printStv(3);
	/*
	int bookieid = m_columns.lastElement().bookie_id;
	System.out.println("bookie i will put data in for "+bookieid);
	System.out.println("COLUMN COUNT IS "+getColumnCount());
	System.out.println("COLUMN ADDED!!! "+bookieid);
	for(int i=0; i< gamesIdVec.size(); i++)
	{
			String gameid = gamesIdVec.get(i);
			if(bookieid == 990)
				{

					stv[i][getColumnCount()-1] = new InfoView(Integer.parseInt(gameid));
					
				}
				else if(bookieid == 991)
				{
					stv[i][getColumnCount()-1] = new TimeView(Integer.parseInt(gameid));
				}
				else if(bookieid == 992)
				{
					stv[i][getColumnCount()-1] = new GameNumberView(Integer.parseInt(gameid));
				}
				else if(bookieid == 993)
				{
					stv[i][getColumnCount()-1] = new TeamView(Integer.parseInt(gameid));
				}
				
				else
				{
				 stv[i][getColumnCount()-1] = new SpreadTotalView(bookieid,Integer.parseInt(gameid));
				}
	
			
	}
	*/
}

public void columnRemoved(TableColumnModelEvent e)
{
	System.out.println("COLUMN Removed!!! "+lastbookieremoved);
	printStv(3);
	//tablecolumnhash.remove(""+lastbookieremoved);
}

public void columnSelectionChanged(ListSelectionEvent e)
{
	System.out.println("Column SelectionChanged!!!");
}


public void columnMarginChanged(ChangeEvent e)
{
	System.out.println("Column MarginChanged!!!");
	
}

public void columnMoved(TableColumnModelEvent e)
{
	System.out.println("Column Moved!!!");
	
}

public Bookie getBookie(int bookieid)
{
	return bookies.get(""+bookieid);
	
}

public boolean isShowingPrior()
{
	return showingPrior;
}
public boolean isShowingOpener()
{
	return showingOpener;
}

public void fire()
{
	//m_table.selectAll();
	
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					fireTableDataChanged();
				}
			});						
	
	
	//System.out.println("fired change inside ltd "+new java.util.Date());
}

public void showOpener()
{
	showingOpener = true;
	showingPrior = false;
	SwingUtilities.invokeLater(new Runnable()
	{
		public void run()
		{
			fireTableDataChanged();
		}
	});	
		
}
public void showPrior()
{
	showingOpener = false;
	showingPrior = true;
	SwingUtilities.invokeLater(new Runnable()
	{
		public void run()
		{
			fireTableDataChanged();
		}
	});	
}
public void showCurrent()
{
	showingOpener = false;
	showingPrior = false;
	SwingUtilities.invokeLater(new Runnable()
	{
		public void run()
		{
			fireTableDataChanged();
		}
	});	
}

public void setDisplayType(String d)
{
	display = d;
	SwingUtilities.invokeLater(new Runnable()
	{
		public void run()
		{
			fireTableDataChanged();
		}
	});	
}

public void setPeriodType(int d)
{
	period = d;
	SwingUtilities.invokeLater(new Runnable()
	{
		public void run()
		{
			fireTableDataChanged();
		}
	});	
}
public String getDisplayType()
{
	return display;
	
}
public int getPeriodType()
{
	return period;
	
}
public void clearColors()
	{
		//printStv(451);
		//System.out.println("rowcount="+getRowCount()+"..colcount="+getColumnCount());
		System.out.println("=============cleared at "+new java.util.Date()+"..cols="+getColumnCount());
		cleartime = new java.util.Date().getTime();
			for(int i=0; i< getRowCount(); i++)
			{

				for(int j =0; j <getColumnCount();j++)
				{
					Object obj = getValueAt(i,j);
					if(obj instanceof SpreadTotalView)
					{
						SpreadTotalView stv = (SpreadTotalView)obj;
						try
						{
							stv.clearColors(cleartime);	
						}
						catch(Exception ex)		
						{
							System.out.println("clear exception row="+i+"..col="+j+ "src/main" +ex);
						}
					}
					if(obj instanceof SoccerSpreadTotalView)
					{
						SoccerSpreadTotalView stv = (SoccerSpreadTotalView)obj;
						try
						{
							stv.clearColors(cleartime);	
						}
						catch(Exception ex)		
						{
							System.out.println("clear exception row="+i+"..col="+j+ "src/main" +ex);
						}
					}
					if(obj instanceof ChartView)
					{
						ChartView stv = (ChartView)obj;
						try
						{
							stv.clearColors();	
							//System.out.println("iam chart");
						}
						catch(Exception ex)		
						{
							System.out.println("clear exception row="+i+"..col="+j+ "src/main" +ex);
						}
					}
					if(obj instanceof SoccerChartView)
					{
						SoccerChartView stv = (SoccerChartView)obj;
						try
						{
							stv.clearColors();	
							//System.out.println("iam chart");
						}
						catch(Exception ex)		
						{
							System.out.println("clear exception row="+i+"..col="+j+ "src/main" +ex);
						}
					}
					
				}
			}
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					fireTableDataChanged();
				}
			});		
		
	}
	
	public void timesort()
	{
		timesort = true;
		setInitialData();
		JViewport parent = (JViewport)thistable.getParent();
		JScrollPane scrollpane = (JScrollPane)parent.getParent();
		scrollpane.setPreferredSize(new Dimension(700,thistable.getRowHeight()*gamesVec.size()));
		thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
		Container comp = scrollpane.getParent();
		comp.revalidate();
		SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					fireTableDataChanged();
				}
			});	
		
	}
	
	public void gmnumsort()
	{
		timesort = false;
		setInitialData();
		JViewport parent = (JViewport)thistable.getParent();
		JScrollPane scrollpane = (JScrollPane)parent.getParent();
		scrollpane.setPreferredSize(new Dimension(700,thistable.getRowHeight()*gamesVec.size()));
		thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
		Container comp = scrollpane.getParent();
		comp.revalidate();
		SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					fireTableDataChanged();
				}
			});			
	}


  public void checktofire(String gameid)
  {
	  
	  if(gamesIdVec.contains(gameid))
	  {
		  fire();
	  }
	  
  }

  public void setInitialData() {
    try {
      m_date = m_frm.parse("12/18/2004");
    }
    catch (java.text.ParseException ex) {
      m_date = null;
    }
	//System.out.println("bbba");
	User user = AppController.getUser();
	String bookiecolumnprefs = user.getBookieColumnPrefs();
	//String bookiefixedcolumnprefs = u.getBookieFixedColumnPrefs();
	
	Vector<String> bookieids = new Vector();
	int fixedcols = 5;
	/*
	bookieids.add(""+990); //details
	bookieids.add(""+991); // time
	bookieids.add(""+992); //gmnum
	bookieids.add(""+993); //team
	*/
	//System.out.println("ccc");
	//System.out.println("bookiecolumnprefs="+bookiecolumnprefs);
	
	stv = new Object[gamesVec.size()+1][bookies.size()+1];
	bookiecolumnprefs = bookiecolumnprefs.trim();
	bookiecolumnprefs = ","+bookiecolumnprefs+",";
	
	//System.out.println("bookiecolumnprefsafter="+bookiecolumnprefs);

	m_columns = new Vector();
	

		
	//	m_columns.add(new com.sia.client.ui.ColumnData(990, "Details", 40, JLabel.RIGHT) );
	//	m_columns.add(new com.sia.client.ui.ColumnData(991, "Time", 40, JLabel.RIGHT) );
	//	m_columns.add(new com.sia.client.ui.ColumnData(992, "Gm#", 30, JLabel.LEFT) );
	//	m_columns.add(new com.sia.client.ui.ColumnData(993, "Team", 160, JLabel.LEFT) );
		
	
	//System.out.println("LTDbookiesvec size="+bookiesVec.size());
	//System.out.println("mcolumns size="+m_columns.size());
	HeaderView hv = new HeaderView();
	for(int j=0;j<bookiesVec.size();j++)	
	{
		
		
		Bookie b = bookiesVec.get(j);
		
		int bookieid = b.getBookie_id();
		
		
		
		if(bookieid == 990)
		{
			
			m_columns.add(new ColumnData(bookieid, "Details", 30, JLabel.RIGHT) );
			
		}
		else if(bookieid == 991)
		{
			m_columns.add(new ColumnData(bookieid, "Time", 40, JLabel.RIGHT) );
			
		}
		else if(bookieid == 992)
		{
			m_columns.add(new ColumnData(bookieid, "Gm#", 40, JLabel.LEFT) );
			
		}
		else if(bookieid == 993)
		{
			m_columns.add(new ColumnData(bookieid, "Team", 100, JLabel.LEFT) );
			
		}
		else if(bookieid == 994)
		{
			m_columns.add(new ColumnData(bookieid, "Chart", 80, JLabel.CENTER) );
			
		}
		else // 81 is precise for 225.5o-07 icon border thickness of 2
		{
		 m_columns.add(new ColumnData(bookieid, b.toString(),50, JLabel.RIGHT) );
		 
		}
	}	
		//stv[0][j] = hv;
		//for(int i=1; i<= gamesIdVec.size(); i++)
		//System.out.println("timesort?="+timesort);
		if(timesort)
		{
		    Collections.sort(gamesVec, new GameDateSorter().thenComparing(new GameTimeSorter()).thenComparing(new GameNumSorter()));
		}						
		else
		{
		    Collections.sort(gamesVec, new GameDateSorter().thenComparing(new GameNumSorter()));			
		}
		
		gamesIdVec = new Vector();
		for(int i=0; i< gamesVec.size(); i++)
		{
			Game g = (Game)gamesVec.get(i);
			
			//System.out.println("game="+gameid);
			String gameid = ""+g.getGame_id();
			
			gamesIdVec.add(gameid);
			Sport s = AppController.getSport(g.getLeague_id());
			int leagueID = g.getLeague_id();

			//System.out.println("LEAGUE ID "+g.getLeague_id());
			//System.out.println("Sportname "+s.getSportname());
			//System.out.println("this Sport "+sport);
			if(!s.getSportname().equalsIgnoreCase(sport))
			{
				//continue;
			}
			
			for(int j=0;j<bookiesVec.size();j++)	
			{
				Bookie b = bookiesVec.get(j);
				try
				{
					
				
					int bookieid = b.getBookie_id();
					if(bookieid == 990)
						{
							stv[i][j] = new InfoView(Integer.parseInt(gameid));
						}
						else if(bookieid == 991)
						{
							stv[i][j] = new TimeView(Integer.parseInt(gameid));
						}
						else if(bookieid == 992)
						{
							if(leagueID == 9)
							{
								stv[i][j] = new SoccerGameNumberView(Integer.parseInt(gameid));
							}
							else{
								stv[i][j] = new GameNumberView(Integer.parseInt(gameid));
							}
							
						}
						else if(bookieid == 993)
						{
							stv[i][j] = new TeamView(Integer.parseInt(gameid),shortteam);
						}
						else if(bookieid == 994)
						{
							if(leagueID == 9)
							{
								stv[i][j] = new SoccerChartView(Integer.parseInt(gameid));
							}
							else{
							stv[i][j] = new ChartView(Integer.parseInt(gameid));
							}
							
						}
						
						else
						{
							
							if(leagueID == 9)
							{
								//System.out.println("soccerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
								stv[i][j] = new SoccerSpreadTotalView(bookieid,Integer.parseInt(gameid),cleartime,this);
							}
							else{
								//System.out.println("otherrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
								 stv[i][j] = new SpreadTotalView(bookieid,Integer.parseInt(gameid),cleartime,this);
							}

						
						}
				}
				catch(Exception ex)	
				{
				//	System.out.println("ltd exception "+ex);
				//	System.out.println("i="+i+"..j="+j);
				//	System.out.println("gamesvecsize="+gamesVec.size());
				//	System.out.println("bookie="+b+"....game="+gameid);
					//System.exit(0);
					ex.printStackTrace();
				}
			}

			try
				{
					
					BestLines.calculatebestall(g.getGame_id(),0);
					BestLines.calculatebestall(g.getGame_id(),1);
					BestLines.calculatebestall(g.getGame_id(),2);
					BestLines.calculatebestall(g.getGame_id(),5);
					BestLines.calculatebestall(g.getGame_id(),6);
					BestLines.calculatebestall(g.getGame_id(),7);
					BestLines.calculatebestall(g.getGame_id(),8);
					BestLines.calculatebestall(g.getGame_id(),9);
				}
				catch(Exception ex)
				{
					System.out.println("error calculating best all "+ex);
				}

		
		}
	


	//System.out.println("done with games! "+getRowCount());
	
  }

  public int getRowCount() {
    return gamesVec==null ? 0 : gamesVec.size();
	//return gamesIdVec==null ? 0 : gamesIdVec.size()+1;
  }

  public int getColumnCount() {
    return m_columns.size();
  }

  public String getColumnName(int column) 
  
  {
	 // System.out.println("colname="+m_columns.get(column).m_title);
    return (m_columns.get(column)).m_title;
  }

  public boolean isCellEditable(int nRow, int nCol) {
    return false;
  }


 public Game removeGameId(String gameidtoremove)
 {
	 
		for(int i=0; i< gamesVec.size(); i++)
		{
			Game g = gamesVec.get(i);
			String gameid = ""+g.getGame_id();
			if(gameid.equals(gameidtoremove))
			{
			//	System.out.println("will delete it at row "+i);
			//	System.out.println("games size is "+gamesVec.size());
				
				try
				{
				gamesVec.remove(i);
				}
				catch(Exception ex)
				{
					System.out.println("error removing from vector!");
				}
		
					setInitialData();
					JViewport parent = (JViewport)thistable.getParent();
					JScrollPane scrollpane = (JScrollPane)parent.getParent();
				//	System.out.println("games size after remove "+gamesVec.size());
					scrollpane.setPreferredSize(new Dimension(700,thistable.getRowHeight()*gamesVec.size()));
					scrollpane.revalidate();
					thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
					//System.out.println("removerow d");
					Container comp = scrollpane.getParent();
					comp.revalidate();
		
				return g;
			}
		}	
	return null; // didn't find it
 }

 public void removeGameIds(String[] gameidstoremove)
 {
	 if(gameidstoremove.length == 1 && gameidstoremove[0].equals("-1"))
	 {
		 removeYesterdaysGames();
	 }
	else
	{

			AppController.disableTabs();
			
			java.util.List<String> list = Arrays.asList(gameidstoremove);
			Vector gameidstoremovevec = new Vector(list);

			for (Iterator<Game> iterator = gamesVec.iterator(); iterator.hasNext();) 
			{

					Game g = iterator.next();
					String gameid = ""+g.getGame_id();
					
					if(gameidstoremovevec.contains(gameid))
					{
						try
						{
						iterator.remove();
						}
						catch(Exception ex)
						{
							System.out.println("error removing from vector!");
						}
					}
		
			}	
		//	System.out.println("games size end is "+gamesVec.size());	

						setInitialData();
						JViewport parent = (JViewport)thistable.getParent();
						JScrollPane scrollpane = (JScrollPane)parent.getParent();
						
						scrollpane.setPreferredSize(new Dimension(700,thistable.getRowHeight()*gamesVec.size()));
						scrollpane.revalidate();
						thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
						Container comp = scrollpane.getParent();
						comp.revalidate();
						
			
			AppController.enableTabs();
		
	}
 }

 public void removeYesterdaysGames()
 {
		java.util.Date today = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String d1 = sdf.format(today);
		boolean removal = false;
			AppController.disableTabs();
			//System.out.println("games size start is "+gamesVec.size());	
			for (Iterator<Game> iterator = gamesVec.iterator(); iterator.hasNext();) 
			{

					Game g = iterator.next();
					String d2 = sdf.format(g.getGamedate());

					if(d1.compareTo(d2) > 0) 
					{
						try
						{
						iterator.remove();
						removal = true;
						}
						catch(Exception ex)
						{
							System.out.println("error removing from vector!");
						}
					}
			}	
		//	System.out.println("games size end is "+gamesVec.size()+"..removal="+removal);	
		if(removal)
		{
						setInitialData();
						JViewport parent = (JViewport)thistable.getParent();
						JScrollPane scrollpane = (JScrollPane)parent.getParent();
						
						scrollpane.setPreferredSize(new Dimension(700,thistable.getRowHeight()*gamesVec.size()));
						scrollpane.revalidate();
						thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
						Container comp = scrollpane.getParent();
						comp.revalidate();
		}		
			
			AppController.enableTabs();
			
	
 }

 public void addGame(Game g,boolean repaint)
 {
		Hashtable<String,Game> games = AppController.getGames(); 	
		Game g2 = games.get(""+g.getGame_id());
		
		//if(!gamesVec.contains(g))
		if(g2 != null)
		{
				System.out.println("games size before add "+gamesVec.size());
				
	
				gamesVec.add(g);
				
				if(repaint)
				{
					setInitialData();
					JViewport parent = (JViewport)thistable.getParent();
					JScrollPane scrollpane = (JScrollPane)parent.getParent();
					//System.out.println("games size after add "+gamesVec.size());
					scrollpane.setPreferredSize(new Dimension(700,thistable.getRowHeight()*gamesVec.size()));
					scrollpane.revalidate();
					thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
					Container comp = scrollpane.getParent();
					comp.revalidate();
				}
		
	
		}

 }


 public void rebuild()
 {
	setInitialData();
 }
  public Object getValueAt(int nRow, int nCol) {
	//  System.out.println("getting value at row="+nRow+"..column="+nCol+"===="+stv[nRow][nCol]);
	try
	{
		if (nRow < 0 || nRow>=getRowCount())
		{
		  return "";
		}
		else
		{
			return stv[nRow][nCol];
		}
	}
	catch(Exception ex)
	{
		System.out.println("exception getvalueat row="+nRow+"..col="+nCol+ "src/main" +ex);
		//try { Thread.sleep(10000);} catch(Exception e) {}
	}
	return "";
  }


public void destroyMe()
{
	for(int i = 0; i < gamesVec.size(); i++)
	{
		for(int j = 0; j < bookies.size(); j++)
		{
				stv[i][j] = null;
			
		}
	}	
	
	
}


public void setValueAt(Object value, int rowIndex, int colIndex) {
    stv[rowIndex][colIndex] = value;
		
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					fireTableDataChanged();
				}
			});	
			
    }

  public String getTitle() {
    if (m_date==null)
      return "Stock Quotes";
    return "Stock Quotes at "+m_frm.format(m_date);
  }
}
