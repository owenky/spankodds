package com.sia.client.ui;

import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.Moneyline;
import com.sia.client.model.Sport;
import com.sia.client.model.UrgentsConsumer;
import com.sia.client.model.User;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Arrays;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class AppController {


    public static Vector alertsVector = new Vector();


    public static boolean loadinginitial = true;
    public static Hashtable customTabsHash = new Hashtable();
    public static Vector<String> customTabsVec = new Vector();
    public static Vector<LinesTableData> dataModels = new Vector();
    public static Vector<LineAlertNode> linealertnodes = new Vector();
    public static Vector<SportsTabPane> tabpanes = new Vector();
    public static Vector<SportsMenuBar> menubars = new Vector();

    public static Hashtable<String, Bookie> bookies = new Hashtable();
    public static Hashtable<String, String> bookieshortnameids = new Hashtable();
    public static Hashtable<String, Sport> sports = new Hashtable();
    public static Hashtable<String, Game> games = new Hashtable();

    public static Vector<Bookie> openerbookiesVec = new Vector();
    public static Vector<Bookie> bookiesVec = new Vector();
    public static Vector<Sport> sportsVec = new Vector();
    public static Vector<Bookie> hiddenCols = new Vector();
    public static Vector<Bookie> shownCols = new Vector();
    public static Vector<Bookie> fixedCols = new Vector();

    public static Vector<Game> gamesVec = new Vector();
    public static Hashtable<JFrame, SportsTabPane> frames = new Hashtable();
    public static Hashtable<String, Spreadline> spreads = new Hashtable();
    public static Hashtable<String, Totalline> totals = new Hashtable();
    public static Hashtable<String, Moneyline> moneylines = new Hashtable();
    public static Hashtable<String, TeamTotalline> teamtotals = new Hashtable();


    public static Hashtable<String, Spreadline> h1spreads = new Hashtable();
    public static Hashtable<String, Totalline> h1totals = new Hashtable();
    public static Hashtable<String, Moneyline> h1moneylines = new Hashtable();
    public static Hashtable<String, TeamTotalline> h1teamtotals = new Hashtable();

    public static Hashtable<String, Spreadline> h2spreads = new Hashtable();
    public static Hashtable<String, Totalline> h2totals = new Hashtable();
    public static Hashtable<String, Moneyline> h2moneylines = new Hashtable();
    public static Hashtable<String, TeamTotalline> h2teamtotals = new Hashtable();

    public static Hashtable<String, Spreadline> q1spreads = new Hashtable();
    public static Hashtable<String, Totalline> q1totals = new Hashtable();
    public static Hashtable<String, Moneyline> q1moneylines = new Hashtable();
    public static Hashtable<String, TeamTotalline> q1teamtotals = new Hashtable();

    public static Hashtable<String, Spreadline> q2spreads = new Hashtable();
    public static Hashtable<String, Totalline> q2totals = new Hashtable();
    public static Hashtable<String, Moneyline> q2moneylines = new Hashtable();
    public static Hashtable<String, TeamTotalline> q2teamtotals = new Hashtable();

    public static Hashtable<String, Spreadline> q3spreads = new Hashtable();
    public static Hashtable<String, Totalline> q3totals = new Hashtable();
    public static Hashtable<String, Moneyline> q3moneylines = new Hashtable();
    public static Hashtable<String, TeamTotalline> q3teamtotals = new Hashtable();

    public static Hashtable<String, Spreadline> q4spreads = new Hashtable();
    public static Hashtable<String, Totalline> q4totals = new Hashtable();
    public static Hashtable<String, Moneyline> q4moneylines = new Hashtable();
    public static Hashtable<String, TeamTotalline> q4teamtotals = new Hashtable();

    public static Hashtable<String, Spreadline> livespreads = new Hashtable();
    public static Hashtable<String, Totalline> livetotals = new Hashtable();
    public static Hashtable<String, Moneyline> livemoneylines = new Hashtable();
    public static Hashtable<String, TeamTotalline> liveteamtotals = new Hashtable();


    public static User u;
    public static String brokerURL = "failover:(tcp://71.172.25.164:61616)";
    //public static String brokerURL = "failover:(ssl://localhost:61617)";
//public static String brokerURL = "failover:(ssl://71.172.25.164:61617)";
    public static ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
    public static Connection guestConnection;
    public static Connection loggedInConnection;

    public static String loginQueue = "spankodds.LOGIN";
    public static String userPrefsQueue = "spankodds.USERPREFS";
    public static String linechangesQueue = "spankoddsin.LINECHANGE";
    public static String gamechangesQueue = "spankoddsin.GAMECHANGE";
    public static String scorechangesQueue = "spankoddsin.SCORECHANGE";
    public static String urgentQueue = "spankoddsin.URGENT";

    public static String displaytype = "default";

    public static LinesConsumer linesConsumer;
    public static GamesConsumer gamesConsumer;
    public static ScoresConsumer scoresConsumer;
    public static UrgentsConsumer urgentsConsumer;

    public static UserPrefsProducer userPrefsProducer;

    public static ChartChecker chartchecker;

    public static LinesTableData linestabledata;

//public static LinesTable2 linestable;

    public static Hashtable<String, Color> bookiecolors = new Hashtable();

    public static int numfixedcols;

    public static long clearalltime;

    public static DefaultTableColumnModel columnmodel;
    public static DefaultTableColumnModel fixedcolumnmodel;

    public static NewWindowAction nwa;

    public static LineOpenerAlertNode football = new LineOpenerAlertNode("Football");
    public static LineOpenerAlertNode basketball = new LineOpenerAlertNode("Basketball");
    public static LineOpenerAlertNode baseball = new LineOpenerAlertNode("Baseball");
    public static LineOpenerAlertNode hockey = new LineOpenerAlertNode("Hockey");
    public static LineOpenerAlertNode soccer = new LineOpenerAlertNode("Soccer");
    public static LineOpenerAlertNode fighting = new LineOpenerAlertNode("Fighting");
    public static LineOpenerAlertNode golf = new LineOpenerAlertNode("Golf");
    public static LineOpenerAlertNode tennis = new LineOpenerAlertNode("Tennis");
    public static LineOpenerAlertNode autoracing = new LineOpenerAlertNode("Auto Racing");
    public static ArrayList<LineOpenerAlertNode> LineOpenerAlertNodeList = new ArrayList<LineOpenerAlertNode>();

    public static SortedMap<Integer, String> SpotsTabPaneVector = new TreeMap<Integer, String>();
    public static Vector<String> SportsTabPaneVector = new Vector<String>();

    public static void initializeSportsTabPaneVectorFromUser()
    {
        String[] tabsindex = u.getTabsIndex().split(",");
        for(int i = 0; i < tabsindex.length; i++)
        {
            SpotsTabPaneVector.put(i, tabsindex[i]);
            // will be using this instead
            SportsTabPaneVector.add(tabsindex[i]);

        }
    }

    public static void initializeLineAlertVectorFromUser()
    {
        String[] linealerts = u.getLineAlerts().split("\\?");
        for(int i = 0; i < linealerts.length; i++)
        {
         try {
             String[] lanitems = linealerts[i].split("!");
             String[] sportselected = lanitems[3].split(",");
             String[] bookieselected = lanitems[4].split(",");
             for(int k=0; k< sportselected.length;k++)
             {
                 System.out.println("sport"+k+"="+sportselected[k]);
             }
             Vector sportselectedvec = new Vector(Arrays.asList(sportselected));
             Vector bookieselectedvec = new Vector(Arrays.asList(bookieselected));
             int j = 5;
             LineAlertNode lan2 = new LineAlertNode(
                     lanitems[0],
                     lanitems[1],
                     Integer.parseInt(lanitems[2]),
                     sportselectedvec,
                     bookieselectedvec,
                     Boolean.parseBoolean(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Double.parseDouble(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Double.parseDouble(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     lanitems[j++],
                     Boolean.parseBoolean(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Double.parseDouble(lanitems[j++]),

                     Boolean.parseBoolean(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Double.parseDouble(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Double.parseDouble(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     lanitems[j++],
                     Boolean.parseBoolean(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Double.parseDouble(lanitems[j++]),

                     Boolean.parseBoolean(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Double.parseDouble(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     lanitems[j++],
                     Boolean.parseBoolean(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Double.parseDouble(lanitems[j++]),


                     Boolean.parseBoolean(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     Double.parseDouble(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Double.parseDouble(lanitems[j++]),
                     Boolean.parseBoolean(lanitems[j++]),
                     lanitems[j++],
                     Boolean.parseBoolean(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Integer.parseInt(lanitems[j++]),
                     Double.parseDouble(lanitems[j++])
             );

             linealertnodes.add(lan2);
         }
         catch(Exception ex)
         {
             System.out.println("exception loading in line alert="+linealerts[i]);
             System.out.println(ex);
         }

        }
    }

    public static void initializSpotsTabPaneVector()
    {
/*
        SpotsTabPaneVector.put(0, "Football");
        SpotsTabPaneVector.put(1, "Basketball");
        SpotsTabPaneVector.put(2, "Baseball");
        SpotsTabPaneVector.put(3, "Hockey");
        SpotsTabPaneVector.put(4, "Fighting");
        SpotsTabPaneVector.put(5, "Soccer");
        SpotsTabPaneVector.put(6, "Auto Racing");
        SpotsTabPaneVector.put(7, "Golf");
        SpotsTabPaneVector.put(8, "Tennis");
        SpotsTabPaneVector.put(9, "Today");

 */
    }

    public static Vector getMainTabVec() {
        Collection<String> al = SpotsTabPaneVector.values();
        Iterator<String> itr = al.iterator();
        Vector<String> vec = new Vector();
        while (itr.hasNext()) {
            vec.add(itr.next());

        }
        return vec;


    }

    public static void createLineOpenerAlertNodeList() {
        LineOpenerAlertNodeList.add(football);
        LineOpenerAlertNodeList.add(basketball);
        LineOpenerAlertNodeList.add(baseball);
        LineOpenerAlertNodeList.add(hockey);
        LineOpenerAlertNodeList.add(soccer);
        LineOpenerAlertNodeList.add(fighting);
        LineOpenerAlertNodeList.add(golf);
        LineOpenerAlertNodeList.add(tennis);
        LineOpenerAlertNodeList.add(autoracing);
    }


    public static void addLineAlertNode(LineAlertNode lan) {

        checkAndRunInEDT(() -> linealertnodes.add(lan));
    }

    public static void removeLineAlertNode(LineAlertNode lan) {

        checkAndRunInEDT(() -> linealertnodes.remove(lan));
    }

    public static void replaceLineAlertNode(LineAlertNode oldlan, LineAlertNode newlan) {

        checkAndRunInEDT(() -> Collections.replaceAll(linealertnodes, oldlan, newlan));
    }

    public static boolean isLineAlertNameAvailable(String name) {
        for (int i = 0; i < linealertnodes.size(); i++) {
            String lanname = ((LineAlertNode) linealertnodes.elementAt(i)).getName();
            if (lanname.equalsIgnoreCase(name)) {
                return false;
            }

        }
        return true;
    }


    public static boolean isLoadingInitial() {
        return loadinginitial;
    }

    public static void doneloadinginitial() {
        loadinginitial = false;
    }

    public static TableColumnModel getColumnModel() {
        if (columnmodel == null) {
            createColumnModel();
        }
        return columnmodel;
    }

    public static void createColumnModel() {
        Vector newBookiesVec = getBookiesVec();
        columnmodel = new DefaultTableColumnModel();
        fixedcolumnmodel = new DefaultTableColumnModel();
        for (int k = 0; k < newBookiesVec.size(); k++) {
            Bookie b = (Bookie) newBookiesVec.get(k);
            LineRenderer lr = new LineRenderer();
            //renderers.add(lr);
            TableColumn column;

            column = new TableColumn(k, 30, lr, null);
            column.setHeaderValue(b.getShortname() + "");
            column.setIdentifier("" + b.getBookie_id());
            if (b.getBookie_id() == 990) {
                column.setPreferredWidth(54);
            } else if (b.getBookie_id() == 991) {
                column.setPreferredWidth(40);
            } else if (b.getBookie_id() == 992) {
                column.setPreferredWidth(45);
            } else if (b.getBookie_id() == 993) {

                column.setPreferredWidth(150);

            } else {
                //column.setPreferredWidth(50) ;
                column.setMinWidth(10);
                column.setPreferredWidth(10);
            }
            columnmodel.addColumn(column);

        }
        // here i'm adding a blank column
        TableColumn blankcol = new TableColumn(newBookiesVec.size(), 10, null, null);
        blankcol.setHeaderValue("");
        blankcol.setIdentifier("9999999");

        blankcol.setMaxWidth(30);
        blankcol.setPreferredWidth(30);
        columnmodel.addColumn(blankcol);


        for (int i = 0; i < AppController.getNumFixedCols(); i++) {
            TableColumn column = columnmodel.getColumn(0);
            columnmodel.removeColumn(column);
            fixedcolumnmodel.addColumn(column);
        }


    }

    public static Vector getBookiesVec() {
        //System.out.println("BEFORE bookiesvec size="+bookiesVec.size());
        reorderBookiesVec();
        //System.out.println("AFTER bookiesvec size="+bookiesVec.size());
        return bookiesVec;
    }

    public static int getNumFixedCols() {
        return numfixedcols;
    }

    public static void setNumFixedCols(int x) {
        numfixedcols = x;
    }

    public static int reorderBookiesVec() {
        int fixedcolsint = 0;
        Vector newVec = new Vector();
        fixedCols.clear();
        shownCols.clear();
        hiddenCols.clear();
        //System.out.println("FIXED="+u.getFixedColumnPrefs());
        //System.out.println("OTHERS"+u.getBookieColumnPrefs());
        String fixedcols[] = u.getFixedColumnPrefs().split(",");
        String cols[] = u.getBookieColumnPrefs().split(",");
        for (int i = 0; i < fixedcols.length; i++) {
            String id = fixedcols[i];
            Bookie b = bookies.get(id);
            if (b != null) {
                newVec.add(b);
                fixedCols.add(b);
                fixedcolsint++;
            }

        }
        for (int i = 0; i < cols.length; i++) {
            String id = cols[i];
            Bookie b = bookies.get(id);
            if (b != null) {
                shownCols.add(b);
                newVec.add(b);

            }
        }
        for (int i = 0; i < bookiesVec.size(); i++) {
            Bookie b = bookiesVec.get(i);
            if (!newVec.contains(b)) {
                newVec.add(b);
                hiddenCols.add(b);
            }

        }
        bookiesVec = newVec;
        numfixedcols = fixedcolsint;
        return fixedcolsint;

    }

    public static TableColumnModel getFixedColumnModel() {
        if (fixedcolumnmodel == null) {
            createColumnModel();
        }
        return fixedcolumnmodel;
    }

    public static NewWindowAction getNewWindowAction() {
        if (nwa == null) {
            nwa = new NewWindowAction();
        }
        return nwa;
    }

    public static String getDisplayType() {
        return displaytype;
    }

    public static void setDisplayType(String s) {
        displaytype = s;
    }

    public static Vector getCustomTabsVec() {
        return customTabsVec;
    }

    public static Hashtable getCustomTabsHash() {
        return customTabsHash;
    }

    public static Vector getLineAlertNodes() {
        if (linealertnodes.size() != 0) {
           // linealertnodes.add(0,new LineAlertNode("Please Select Line Alert"));
        }
        return linealertnodes;
    }

    public static Vector getDataModels() {
        return dataModels;
    }

    public static void addDataModels(Vector v) {
        for (int i = 0; i < v.size(); i++) {
            addDataModel((LinesTableData) v.get(i));
        }

    }

    public static void addDataModel(LinesTableData ltd) {
        if (!dataModels.contains(ltd)) {
            dataModels.add(ltd);
        }

    }

    public static void removeDataModels(Vector v) {
        if (v != null) {
            for (int i = 0; i < v.size(); i++) {
                removeDataModel((LinesTableData) v.get(i));
            }
        }

    }

    public static void removeDataModel(LinesTableData ltd) {
        if (ltd != null) {
            dataModels.remove(ltd);
            //ltd.destroyMe(); // owen took out 6/4
        }
        //ltd = null; // owen took out 6/4

    }

    public static void addTabPane(SportsTabPane stb) {
        tabpanes.add(stb);

    }

    public static SportsTabPane getMainTabPane() {

        return (SportsTabPane) tabpanes.get(0);
    }

    public static Vector getTabPanes() {

        return tabpanes;
    }

    public static SportsMenuBar getMainMenuBar() {

        return (SportsMenuBar) menubars.get(0);
    }

  /*
  public static void refreshTabs()
  {
	  for(int i=0; i< tabpanes.size(); i++)
	  {
		SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
		if(tp != null)
		{
			int j = tp.getSelectionModel().getSelectedIndex();
			//System.out.println("selected index is "+j);
			//tp.getSelectionModel().clearSelection(j);
			//tp.getSelectionModel().select(j);
			//tp.getSelectionModel().clearAndSelect(tp.getSelectionModel().getSelectedIndex());

			tp.getSelectionModel().selectFirst();
			tp.getSelectionModel().selectNext();
			tp.getSelectionModel().select(j);

		}
	  }
  }

    public static void refreshTabs2()
  {
	  for(int i=0; i< tabpanes.size(); i++)
	  {
		SportsTabPane tp = (SportsTabPane)tabpanes.get(i);
		if(tp != null)
		{
			SportsTab thistab = (SportsTab)tp.getSelectionModel().getSelectedItem();

			//System.out.println("destroying...");
			thistab.destroy();
			//System.out.println("creating...");
			thistab.create();
			//System.out.println("all done");


		}
	  }
  }
   */

    public static Vector getMenuBars() {

        return menubars;
    }

    public static void disableTabs() {
        for (int i = 0; i < tabpanes.size(); i++) {
            SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
            tp.disableTabs();
        }
    }

    public static void enableTabs() {
        for (int i = 0; i < tabpanes.size(); i++) {
            SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
            tp.enableTabs();
        }
    }

    public static void addFrame(JFrame f, SportsTabPane stb) {
        frames.put(f, stb);
        addMenuBar((SportsMenuBar) f.getJMenuBar());
    }

    public static void addMenuBar(SportsMenuBar smb) {
        menubars.add(smb);

    }

    public static void removeFrame(JFrame f) {
        removeMenuBar((SportsMenuBar) f.getJMenuBar());
        SportsTabPane stb = frames.get(f);
        stb.cleanup();
        removeTabPane(stb);
        frames.remove(f);
        if (frames.size() == 0) {
            System.out.println("exiting..");
            System.exit(0);
        }
    }

    public static void removeMenuBar(SportsMenuBar smb) {
        menubars.remove(smb);

    }

    public static void removeTabPane(SportsTabPane stb) {
        tabpanes.remove(stb);

    }

    public static void removeCustomTab(String key) {
        String val = (String) customTabsHash.get(key);
        if (val != null) {
            int index = customTabsVec.indexOf(val);
            if (index != -1) {
                customTabsVec.removeElementAt(index);
            }
        }


        customTabsHash.remove(key);
        repaintmenubars();
    }

    public static void repaintmenubars() {

        for (int i = 0; i < menubars.size(); i++) {
            SportsMenuBar smb = menubars.elementAt(i);
            System.out.println("smb=" + smb);
            smb.populateTabsMenu();

        }

    }

    public static String getTabInfo(String tabname) {
        return (String) customTabsHash.get(tabname);
    }

    public static void refreshTabs3() {
        for (int i = 0; i < tabpanes.size(); i++) {
            SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
            if (tp != null) {


                try {
                    tp.refreshCurrentTab();
                } catch (Exception ex) {
                }

			/*

			int numtabs = tp.getTabCount();
			if(currenttabindex == 0)
			{
				tp.setSelectedIndex(currenttabindex+1);
				tp.setSelectedIndex(currenttabindex);
			}
			else
			{
				tp.setSelectedIndex(currenttabindex-1);
				tp.setSelectedIndex(currenttabindex);
			}
			*/
            }
        }
    }
  /*
  public static void setUpStage(Stage stage)
  {
	  if(stage == null)
	  {
		  stage = new Stage();
	  }
	  SportsTabPane stb = new SportsTabPane();
	  TopView tv = new TopView();
	  BorderPane pane = new BorderPane(stb,tv,null,null,null);
	  Scene mainscene =  new Scene(pane, 820, 750);
	  stage.setScene(mainscene);


  }
*/

    public static void clearAll() {
        for (int k = 0; k < tabpanes.size(); k++) {

            SportsTabPane stb = (SportsTabPane) tabpanes.get(k);
            stb.clearAll();

        }
	  /*
	  for(int i=0;i <dataModels.size();i++)
	  {
		dataModels.get(i).clearColors();
	  }
	  */


    }

    public static void fireAllTableDataChanged(String gameid) {

        for (int k = 0; k < tabpanes.size(); k++) {

            SportsTabPane stb = (SportsTabPane) tabpanes.get(k);
            stb.fireAllTableDataChanged(gameid);

        }

	  /*
	  for(int i=0; i< dataModels.size(); i++)
	  {
		LinesTableData ltd = (LinesTableData)dataModels.get(i);

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					ltd.checktofire(gameid);
				}
			});

	  }
	  */
    }

    public static void fireAllTableStructureChanged() {
        for (int i = 0; i < dataModels.size(); i++) {
            LinesTableData ltd = dataModels.get(i);

            checkAndRunInEDT(() -> ltd.fireTableStructureChanged());


        }

    }

    public static LinesTableData getLinesTableData() {
        return linestabledata;
    }

    public static void setLinesTableData(LinesTableData tab) {
        linestabledata = tab;
    }

    /*
    public static void setLinesTable(LinesTable2 tab)
    {
        linestable = tab;
    }

    public static LinesTable2 getLinesTable()
    {
        return linestable;
    }
    */
    public static String getLoginQueue() {
        return loginQueue;
    }

    public static String getUserPrefsQueue() {
        return userPrefsQueue;
    }

    public static ActiveMQConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public static long getClearAllTime() {
        return clearalltime;
    }

    public static void setClearAllTime(long x) {
        clearalltime = x;
    }

    public static Connection createLoggedInConnection(String un, String pw) {
        try {
            if (loggedInConnection == null) {
                loggedInConnection = connectionFactory.createConnection(un, pw);
            }
        } catch (Exception e) {
            log("error establishing loggedin connection! ");
            log(e);
        }
        return loggedInConnection;
    }

    public static Connection getLoggedInConnection() {
        return loggedInConnection;
    }


    public static void createLinesConsumer() {
        try {
            linesConsumer = new LinesConsumer(connectionFactory, loggedInConnection, linechangesQueue);
        } catch (Exception e) {
            log("error establishing loggedin connection! lines consumer");
            log(e);
        }

    }

    public static void createScoresConsumer() {
        try {
            scoresConsumer = new ScoresConsumer(connectionFactory, loggedInConnection, scorechangesQueue);
        } catch (Exception e) {
            log("error establishing loggedin connection! scores consumer");
            log(e);
        }

    }

    public static void createUrgentsConsumer() {
        try {
            urgentsConsumer = new UrgentsConsumer(connectionFactory, loggedInConnection, urgentQueue);
        } catch (Exception e) {
            log("error establishing loggedin connection! urgents consumer");
            log(e);
        }

    }

    public static void createGamesConsumer() {
        try {
            gamesConsumer = new GamesConsumer(connectionFactory, loggedInConnection, gamechangesQueue);
        } catch (Exception e) {
            log("error establishing loggedin connection! games consumer ");
            log(e);
        }

    }

    public static void createChartChecker() {
        try {
            chartchecker = ChartChecker.instance();
        } catch (Exception e) {
            log("error creating chartchecker " + e);
            log(e);
        }

    }

    public static ChartChecker getChartChecker() {
        return chartchecker;
    }

    public static UserPrefsProducer getUserPrefsProducer() {
        if (userPrefsProducer == null) {
            System.out.println("creating userprefsproducer!!!");
            createUserPrefsProducer();
        }
        return userPrefsProducer;
    }

    public static void createUserPrefsProducer() {
        try {
            userPrefsProducer = new UserPrefsProducer();
        } catch (Exception e) {
            System.out.println("error creating userprefs producer! " + e);
        }

    }

    public static Connection getGuestConnection() {

        try {
            if (guestConnection == null) {
                guestConnection = connectionFactory.createConnection("guest", "spank0dds4ever");
            }
        } catch (Exception e) {
            log("error establishing guest connection! ");
            log(e);
        }
        return guestConnection;
    }

    public static String getBookieId(String sn) {
        return "" + bookieshortnameids.get(sn);
    }

    public static User getUser() {
        return u;
    }

    public static void setUser(User u2) {
        u = u2;

        //here i will load in all prefs
        String columncolors = u.getColumnColors();

        String[] colcolorsarray = columncolors.split(",");
        for (int i = 0; i < colcolorsarray.length; i++) {
            String colcolor = colcolorsarray[i];
            if (colcolor.indexOf("=") != -1) {
                String id = colcolor.substring(0, colcolor.indexOf("="));
                String color = colcolor.substring(colcolor.indexOf("=") + 1);
                try {
                    System.out.println("BOOKIECOLORS " + id + "=" + color);
                    System.out.println(Color.decode(color));
                    bookiecolors.put(id, Color.decode(color));
                } catch (Exception ex) {
                    log(ex);
                }

            }

        }

        String customtabs = u.getCustomTabs();

        String[] customtabsarray = customtabs.split("\\?");
        for (int i = 0; i < customtabsarray.length; i++) {
            try {
                String customtab = customtabsarray[i];
                String[] tabelements = customtab.split("\\*");
                addCustomTab(tabelements[1], customtab);
            } catch (Exception ex) {
                log(ex);
            }

        }


    }

    public static void addCustomTab(String key, String s) {
        String val = (String) customTabsHash.get(key);
        if (val != null) {
            int index = customTabsVec.indexOf(val);
            if (index != -1) {
                customTabsVec.set(index, s);
            }
        } else {
            customTabsVec.add(s);
        }


        customTabsHash.put(key, s);
        repaintmenubars();
    }

    public static void addBookie(Bookie b) {

        bookies.put(b.getBookie_id() + "", b);
        bookieshortnameids.put(b.getShortname(), "" + b.getBookie_id());
        bookiesVec.add(b);
    }

    public static void addOpenerBookie(Bookie b) {

        bookies.put(b.getBookie_id() + "", b);
        openerbookiesVec.add(b);
    }

    public static void addSport(Sport s) {

        sports.put(s.getLeague_id() + "", s);
        sportsVec.add(s);
    }

    public static void removeGame(int gameid) {
        checkAndRunInEDT(() -> {
            for (int k = 0; k < tabpanes.size(); k++) {

                SportsTabPane stb = (SportsTabPane) tabpanes.get(k);
                stb.removeGame("" + gameid);

            }
            Game g = null;
            if (games.get(gameid + "") != null) {
                g = games.get(gameid + "");
                gamesVec.remove(g);

            }
            games.remove(gameid + "");
            for (int j = 0; j < bookiesVec.size(); j++) {
                Bookie b = bookiesVec.get(j);
                int bid = b.getBookie_id();
                spreads.remove(bid + "-" + gameid);
                totals.remove(bid + "-" + gameid);
                moneylines.remove(bid + "-" + gameid);
                teamtotals.remove(bid + "-" + gameid);
                h1spreads.remove(bid + "-" + gameid);
                h1totals.remove(bid + "-" + gameid);
                h1moneylines.remove(bid + "-" + gameid);
                h1teamtotals.remove(bid + "-" + gameid);
                h2spreads.remove(bid + "-" + gameid);
                h2totals.remove(bid + "-" + gameid);
                h2moneylines.remove(bid + "-" + gameid);
                h2teamtotals.remove(bid + "-" + gameid);
                q1spreads.remove(bid + "-" + gameid);
                q1totals.remove(bid + "-" + gameid);
                q1moneylines.remove(bid + "-" + gameid);
                q1teamtotals.remove(bid + "-" + gameid);
                q2spreads.remove(bid + "-" + gameid);
                q2totals.remove(bid + "-" + gameid);
                q2moneylines.remove(bid + "-" + gameid);
                q2teamtotals.remove(bid + "-" + gameid);
                q3spreads.remove(bid + "-" + gameid);
                q3totals.remove(bid + "-" + gameid);
                q3moneylines.remove(bid + "-" + gameid);
                q3teamtotals.remove(bid + "-" + gameid);
                q4spreads.remove(bid + "-" + gameid);
                q4totals.remove(bid + "-" + gameid);
                q4moneylines.remove(bid + "-" + gameid);
                q4teamtotals.remove(bid + "-" + gameid);
                livespreads.remove(bid + "-" + gameid);
                livetotals.remove(bid + "-" + gameid);
                livemoneylines.remove(bid + "-" + gameid);
                liveteamtotals.remove(bid + "-" + gameid);

            }
            g = null;
        });
    }

    public static void removeGameDate(String date, String leagueid) {
        //here i will get all teh game ids for a given date and leagueid and
        // then make an array out of it and call removegames
        removeGames(getAllGamesForThisDateAndLeagueId(date, leagueid));

    }

    public static void removeGames(String[] gameidarr) {
        checkAndRunInEDT(() -> {
            for (int k = 0; k < tabpanes.size(); k++) {

                SportsTabPane stb = (SportsTabPane) tabpanes.get(k);
                stb.removeGames(gameidarr);


            }
            if (gameidarr.length == 1 && gameidarr[0].equals("-1")) {
                return;
            }

            for (int i = 0; i < gameidarr.length; i++) {
                try {
                    Game g = null;
                    String gameid = gameidarr[i];

                    if (games.get(gameid) != null) {
                        g = games.get(gameid);
                        gamesVec.remove(g);

                    }
                    games.remove(gameid + "");
                    for (int j = 0; j < bookiesVec.size(); j++) {
                        Bookie b = bookiesVec.get(j);
                        int bid = b.getBookie_id();
                        spreads.remove(bid + "-" + gameid);
                        totals.remove(bid + "-" + gameid);
                        moneylines.remove(bid + "-" + gameid);
                        teamtotals.remove(bid + "-" + gameid);
                        h1spreads.remove(bid + "-" + gameid);
                        h1totals.remove(bid + "-" + gameid);
                        h1moneylines.remove(bid + "-" + gameid);
                        h1teamtotals.remove(bid + "-" + gameid);
                        h2spreads.remove(bid + "-" + gameid);
                        h2totals.remove(bid + "-" + gameid);
                        h2moneylines.remove(bid + "-" + gameid);
                        h2teamtotals.remove(bid + "-" + gameid);
                        q1spreads.remove(bid + "-" + gameid);
                        q1totals.remove(bid + "-" + gameid);
                        q1moneylines.remove(bid + "-" + gameid);
                        q1teamtotals.remove(bid + "-" + gameid);
                        q2spreads.remove(bid + "-" + gameid);
                        q2totals.remove(bid + "-" + gameid);
                        q2moneylines.remove(bid + "-" + gameid);
                        q2teamtotals.remove(bid + "-" + gameid);
                        q3spreads.remove(bid + "-" + gameid);
                        q3totals.remove(bid + "-" + gameid);
                        q3moneylines.remove(bid + "-" + gameid);
                        q3teamtotals.remove(bid + "-" + gameid);
                        q4spreads.remove(bid + "-" + gameid);
                        q4totals.remove(bid + "-" + gameid);
                        q4moneylines.remove(bid + "-" + gameid);
                        q4teamtotals.remove(bid + "-" + gameid);
                        livespreads.remove(bid + "-" + gameid);
                        livetotals.remove(bid + "-" + gameid);
                        livemoneylines.remove(bid + "-" + gameid);
                        liveteamtotals.remove(bid + "-" + gameid);

                    }
                    g = null;
                } catch (Exception ex) {
                    log(ex);
                }
            }
        });
    }

    public static String[] getAllGamesForThisDateAndLeagueId(String date, String leagueid) {
        Iterator itr = gamesVec.iterator();
        Vector<String> gameidstodelete = new Vector<String>();
        while (itr.hasNext()) {
            Game g = (Game) itr.next();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String gamedate = sdf.format(g.getGamedate());
            if (date.equals(gamedate)) // we have a match on date
            {
                if (!leagueid.equals("")) {
                    int lid = g.getLeague_id();
                    int lid2 = Integer.parseInt(leagueid);
                    if (lid == lid2) {
                        gameidstodelete.add(g.getGame_id() + "");
                        System.out.println("will be deleting .." + g.getGame_id());
                    }
                } else {
                    gameidstodelete.add(g.getGame_id() + "");
                    System.out.println("will be deleting .." + g.getGame_id());
                }
            }
        }
        return (gameidstodelete.toArray(new String[gameidstodelete.size()]));

    }

    public static void rebuildModels() {

        for (int k = 0; k < dataModels.size(); k++) {
            LinesTableData ltd = (LinesTableData) dataModels.get(k);
            ltd.rebuild();
        }

    }

    public static void addGame(Game g) {
        addGame(g, true);

    }

    public static void addGame(Game g, boolean repaint) {
	/*
	if(!gamesIdVec.contains(g.getGame_id()+""))
	{
		System.out.println("adding new gmid="+g.getGame_id());
		gamesIdVec.add(g.getGame_id()+"");
	}
*/
        checkAndRunInEDT(() -> {
            if (games.get(g.getGame_id() + "") == null) {
                //System.out.println("adding new gmid="+g.getGame_id());
                gamesVec.add(g);
                games.put(g.getGame_id() + "", g);
                for (int k = 0; k < tabpanes.size(); k++) {

                    SportsTabPane stb = (SportsTabPane) tabpanes.get(k);
                    stb.addGame(g, repaint);

                }
            } else // just an update
            {
                games.put(g.getGame_id() + "", g);
            }

        });


        //owen here i need to put code to add this game to datamodel addGameId(string)
        // the problem is i dont know which model to add it to...

    }

    public static void moveGameToThisHeader(Game g, String header) {
        checkAndRunInEDT(() -> {
            for (int k = 0; k < tabpanes.size(); k++) {

                SportsTabPane stb = (SportsTabPane) tabpanes.get(k);
                stb.moveGameToThisHeader(g, header);

            }
        });
    }

    public static Bookie getBookie(int bid) {
        return bookies.get(bid + "");
    }

    public static Sport getSport(int sid) {
        return sports.get(sid + "");
    }

    public static Game getGame(int gid) {
        return games.get(gid + "");
    }

    public static Bookie getBookie(String bid) {
        return bookies.get(bid + "");
    }

    public static Sport getSport(String sid) {
        return sports.get(sid + "");
    }

    public static Game getGame(String gid) {
        return games.get(gid + "");
    }

    public static Hashtable getGames() {
        return games;
    }

    public static Hashtable getBookies() {
        return bookies;
    }

    public static Hashtable getSports() {
        return sports;
    }

    public static Hashtable getBookieColors() {
        return bookiecolors;
    }

    public static Vector getSportsVec() {
        //System.out.println("BEFORE sportssvec size="+sportsVec.size());
        //reorderBookiesVec();
        //System.out.println("AFTER sportsvec size="+sportsVec.size());
        return sportsVec;
    }

    public static Vector getHiddenCols() {
        return hiddenCols;
    }

    public static Vector getShownCols() {
        if (shownCols == null || shownCols.size() == 0) {
            reorderBookiesVec();
        }
        return shownCols;
    }

    public static Vector getFixedCols() {
        return fixedCols;
    }

    public static Vector getGamesVec() {
        return gamesVec;
    }

    public static void addSpreadline(Spreadline spread) {
        int period = spread.getPeriod();

        if (period == 0) {
            spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 1) {
            h1spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 2) {
            h2spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 5) {
            q1spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 6) {
            q2spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 7) {
            q3spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 8) {
            q4spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else {
            livespreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
            System.out.println("unknown spread period " + period + "...." + spread.getBookieid() + "-" + spread.getGameid());
        }
        //LineAlertOpeners.spreadOpenerAlert(spread);
    }

    public static void addTotalline(Totalline total) {


        int period = total.getPeriod();
        if (period == 0) {
            totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 1) {
            h1totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 2) {
            h2totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 5) {
            q1totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 6) {
            q2totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 7) {
            q3totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 8) {
            q4totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else {
            livetotals.put(total.getBookieid() + "-" + total.getGameid(), total);
            log("unknown total period " + period + "...." + total.getBookieid() + "-" + total.getGameid());
        }

        //LineAlertOpeners.totalOpenerAlert(total);

    }

    public static void addMoneyline(Moneyline ml) {


        int period = ml.getPeriod();
        if (period == 0) {
            moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 1) {
            h1moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 2) {
            h2moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 5) {
            q1moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 6) {
            q2moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 7) {
            q3moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 8) {
            q4moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else {
            livemoneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
            log("unknown money period " + period + "...." + ml.getBookieid() + "-" + ml.getGameid());
        }
//LineAlertOpeners.moneyOpenerAlert(ml);
    }

    public static void addTeamTotalline(TeamTotalline teamtotal) {

        int period = teamtotal.getPeriod();
        if (period == 0) {
            teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 1) {
            h1teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 2) {
            h2teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 5) {
            q1teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 6) {
            q2teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 7) {
            q3teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 8) {
            q4teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else {
            liveteamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
            System.out.println("unknown tt period " + period + "...." + teamtotal.getBookieid() + "-" + teamtotal.getGameid());
        }
//LineAlertOpeners.teamTotalOpenerAlert(teamtotal);
    }

    public static Spreadline getSpreadline(int b, int g, int period) {
        if (period == 0) {
            return spreads.get(b + "-" + g);
        } else if (period == 1) {
            return h1spreads.get(b + "-" + g);
        } else if (period == 2) {
            return h2spreads.get(b + "-" + g);
        } else if (period == 5) {
            return q1spreads.get(b + "-" + g);
        } else if (period == 6) {
            return q2spreads.get(b + "-" + g);
        } else if (period == 7) {
            return q3spreads.get(b + "-" + g);
        } else if (period == 8) {
            return q4spreads.get(b + "-" + g);
        } else {
            return livespreads.get(b + "-" + g);
        }
    }

    public static Totalline getTotalline(int b, int g, int period) {

        if (period == 0) {
            return totals.get(b + "-" + g);
        } else if (period == 1) {
            return h1totals.get(b + "-" + g);
        } else if (period == 2) {
            return h2totals.get(b + "-" + g);
        } else if (period == 5) {
            return q1totals.get(b + "-" + g);
        } else if (period == 6) {
            return q2totals.get(b + "-" + g);
        } else if (period == 7) {
            return q3totals.get(b + "-" + g);
        } else if (period == 8) {
            return q4totals.get(b + "-" + g);
        } else {
            return livetotals.get(b + "-" + g);
        }
    }

    public static Moneyline getMoneyline(int b, int g, int period) {

        if (period == 0) {
            return moneylines.get(b + "-" + g);
        } else if (period == 1) {
            return h1moneylines.get(b + "-" + g);
        } else if (period == 2) {
            return h2moneylines.get(b + "-" + g);
        } else if (period == 5) {
            return q1moneylines.get(b + "-" + g);
        } else if (period == 6) {
            return q2moneylines.get(b + "-" + g);
        } else if (period == 7) {
            return q3moneylines.get(b + "-" + g);
        } else if (period == 8) {
            return q4moneylines.get(b + "-" + g);
        } else {
            return livemoneylines.get(b + "-" + g);
        }
    }

    public static TeamTotalline getTeamTotalline(int b, int g, int period) {

        if (period == 0) {
            return teamtotals.get(b + "-" + g);
        } else if (period == 1) {
            return h1teamtotals.get(b + "-" + g);
        } else if (period == 2) {
            return h2teamtotals.get(b + "-" + g);
        } else if (period == 5) {
            return q1teamtotals.get(b + "-" + g);
        } else if (period == 6) {
            return q2teamtotals.get(b + "-" + g);
        } else if (period == 7) {
            return q3teamtotals.get(b + "-" + g);
        } else if (period == 8) {
            return q4teamtotals.get(b + "-" + g);
        } else {
            return liveteamtotals.get(b + "-" + g);
        }
    }


/*
public static Spreadline getSpreadline(Bookie b,Game g)
{
	return spreads.get(b.getBookie_id()+"-"+g.getGame_id());
}

public static Totalline getTotalline(Bookie b,Game g)
{
	return totals.get(b.getBookie_id()+"-"+g.getGame_id());
}
public static Moneyline getMoneyline(Bookie b,Game g)
{
	return moneylines.get(b.getBookie_id()+"-"+g.getGame_id());
}


public static TeamTotalline getTeamTotalline(Bookie b,Game g)
{
	return teamtotals.get(b.getBookie_id()+"-"+g.getGame_id());
}



public static Spreadline getSpreadline(int b,Game g)
{
	return spreads.get(b+"-"+g.getGame_id());
}

public static Totalline getTotalline(int b,Game g)
{
	return totals.get(b+"-"+g.getGame_id());
}
public static Moneyline getMoneyline(int b,Game g)
{
	return moneylines.get(b+"-"+g.getGame_id());
}
public static TeamTotalline getTeamTotalline(int b,Game g)
{
	return teamtotals.get(b+"-"+g.getGame_id());
}

public static Spreadline getSpreadline(Bookie b,int g)
{
	return spreads.get(b.getBookie_id()+"-"+g);
}

public static Totalline getTotalline(Bookie b,int g)
{
	return totals.get(b.getBookie_id()+"-"+g);
}
public static Moneyline getMoneyline(Bookie b,int g)
{
	return moneylines.get(b.getBookie_id()+"-"+g);
}
public static TeamTotalline getTeamTotalline(Bookie b,int g)
{
	return teamtotals.get(b.getBookie_id()+"-"+g);
}


*/

    public static String getCurrentHoursMinutes() {
        Calendar calendar = Calendar.getInstance();

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        return "" + hours + "hr," + minutes + "min";

    }

    public LinesConsumer getLinesConsumer() {
        return linesConsumer;
    }

    public GamesConsumer getGamesConsumer() {
        return gamesConsumer;
    }

    public ScoresConsumer getScoresConsumer() {
        return scoresConsumer;
    }

    public UrgentsConsumer getUrgentsConsumer() {
        return urgentsConsumer;
    }

}