package com.sia.client.ui;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import static com.sia.client.config.Utils.checkAndRunInEDT;

public class TopView extends JPanel implements ItemListener, Cloneable {
    JButton clearBut;
    JButton clearAllBut;
    JButton lastBut;
    JButton openerBut;
    JButton sortBut;
    JButton addBookieBut;
    JButton shrinkTeamBut;
    JButton newWindowBut;
    JButton alertBut;
    JButton adjustcolsBut;
    JButton chartBut;

    //buttoms not visible
    JButton firsthalfBut;


    JComboBox alertsCombo;
    JComboBox cb;
    JComboBox periodcb;
    String[] display = new String[9];
    String[] display2 = new String[9];
    int[] perioddisplay = new int[8];
    String[] perioddisplay2 = new String[8];
    SportsTabPane stb;


    public TopView(SportsTabPane stb) {

        //super(1);
        super(new FlowLayout(FlowLayout.LEFT));

        this.stb = stb;
        display[0] = "default";
        display[1] = "spreadtotal";
        display[2] = "totalmoney";
        display[3] = "totalbothmoney";
        display[4] = "justspread";
        display[5] = "justtotal";
        display[6] = "justmoney";
        display[7] = "awayteamtotal";
        display[8] = "hometeamtotal";


        display2[0] = "Default View";
        display2[1] = "Sides & Totals";
        display2[2] = "Totals & ML";
        display2[3] = "Totals & Both ML";
        display2[4] = "Just Sides";
        display2[5] = "Just Totals";
        display2[6] = "Just ML";
        display2[7] = "Away TT";
        display2[8] = "Home TT";

        perioddisplay[0] = 0;
        perioddisplay[1] = 1;
        perioddisplay[2] = 2;
        perioddisplay[3] = 5;
        perioddisplay[4] = 6;
        perioddisplay[5] = 7;
        perioddisplay[6] = 8;
        perioddisplay[7] = 9;


        perioddisplay2[0] = "Full Game";
        perioddisplay2[1] = "1st Half";
        perioddisplay2[2] = "2nd Half";
        perioddisplay2[3] = "1st Q/P";
        perioddisplay2[4] = "2nd Q/P";
        perioddisplay2[5] = "3rd Q/P";
        perioddisplay2[6] = "4th Q/P";
        perioddisplay2[7] = "Live";


        cb = new JComboBox(display2);

        cb.setSelectedIndex(0);
        cb.addItemListener(this);

        periodcb = new JComboBox(perioddisplay2);

        periodcb.setSelectedIndex(0);
        periodcb.addItemListener(this);


        sortBut = new JButton("Time sort");

        addBookieBut = new JButton("Add Bookie");
        shrinkTeamBut = new JButton("Short Team");
        newWindowBut = new JButton("New Window");
        alertBut = new JButton("Alert");
        adjustcolsBut = new JButton("Adj Cols");
        chartBut = new JButton("Chart");
        chartBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ChartHome().setVisible(true);

            }
        });
        alertsCombo = new JComboBox();
        //alertsCombo.addItem("RECENT ALERTS LIST             ");


        alertsCombo.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                alertsCombo.removeAllItems();
                alertsCombo.addItem("RECENT ALERTS LIST             ");
                for (int i = (AppController.alertsVector.size()); i > 0; i--) {
                    alertsCombo.addItem((String) AppController.alertsVector.get(i - 1));
                }
                System.out.println("inside" + AppController.alertsVector.size());


            }
        });


        // this.getChildren().addAll(cb,clearBut,clearAllBut,lastBut,openerBut,addBookieBut,remBookieBut,newWindowBut,alertBut);
        checkAndRunInEDT(() -> {
            initEvents();
            add(cb);
            add(periodcb);
            add(clearBut);
            add(clearAllBut);
            add(lastBut);
            add(openerBut);
            add(sortBut);
            // add(addBookieBut);
            add(shrinkTeamBut);
            // add(newWindowBut);
            // add(alertBut);
            add(adjustcolsBut);
            add(chartBut);

            add(alertsCombo);

        });

    }
/*	
	public LinesTableData getDataModel()
	{
		
			BorderPane bp = (BorderPane)getParent();
			System.out.println("bp="+bp);
			SportsTabPane stb = (SportsTabPane)bp.getCenter();
			System.out.println("stb="+stb);
			LinesTableData ltd = ((LinesTableData)stb.getCurrentTable().getModel());
			return ltd;
	}
*/

    public void initEvents() {

        //nwa = new NewWindowAction();
        newWindowBut.addActionListener(AppController.getNewWindowAction());

			/*
		newWindowBut.addActionListener(new ActionListener() {



			 public void actionPerformed(ActionEvent ae)
			{
				// owen still need!


					SportsTabPane stbnew = new SportsTabPane();
					TopView tv = new TopView(stbnew);

					//new OddsFrame(stbnew,tv);

					JFrame frame = new JFrame("Spank Odds");


				//	SportsTabPane stbnew = (SportsTabPane)((SportsTabPane)stb).clone();
				//	TopView tv = (TopView)this.clone();
					//owen gotta find a way to deep clone stb

					JPanel mainpanel = new JPanel();
					mainpanel.setLayout(new BorderLayout());
					//mainpanel.add(tv,BorderLayout.CENTER);
					mainpanel.add(tv,BorderLayout.PAGE_START);
					mainpanel.add(stbnew,BorderLayout.CENTER);
					//frame.setLayout(new BorderLayout());

					frame.setContentPane(mainpanel);
					frame.setSize(600, 600);

					com.sia.client.ui.AppController.addFrame(frame,stbnew);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							System.out.println("Window Closing2! ");
							com.sia.client.ui.AppController.removeFrame(frame);

						}
					});



					//frame.setLocationRelativeTo(null);
					frame.setVisible(true);

			}
		});
		*/


// 1st HALF NOT VISIBLE USED TO SELECT 1st HALF FROM COMBO BOX!

        Action firsthalfaction = new AbstractAction("1st half") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("1st half button pressed");
                periodcb.setSelectedIndex(1);


            }
        };
        firsthalfaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_2, 0));

        this.getActionMap().put("1sthalfAction", firsthalfaction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) firsthalfaction.getValue(Action.ACCELERATOR_KEY), "1sthalfAction");

// FULL GAME NOT VISIBLE USED TO SELECT FULL GAME FROM COMBO BOX!

        Action fullgameaction = new AbstractAction("Full Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("full game button pressed");
                periodcb.setSelectedIndex(0);


            }
        };
        fullgameaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, 0));

        this.getActionMap().put("fullgameAction", fullgameaction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) fullgameaction.getValue(Action.ACCELERATOR_KEY), "fullgameAction");

// USED TO SELECT DEFAULT VIEW FROM COMBO BOX!

        Action defaultviewaction = new AbstractAction("DefaultView") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("defaultview button pressed");
                cb.setSelectedIndex(0);


            }
        };
        defaultviewaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_1, 0));

        this.getActionMap().put("defaultviewaction", defaultviewaction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) defaultviewaction.getValue(Action.ACCELERATOR_KEY), "defaultviewaction");

// USED TO SELECT SIDES ONLY FROM COMBO BOX!

        Action sidesonlyaction = new AbstractAction("SidesOnly") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("sides only button pressed");
                cb.setSelectedIndex(4);


            }
        };
        sidesonlyaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_3, 0));

        this.getActionMap().put("sidesonlyaction", sidesonlyaction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) sidesonlyaction.getValue(Action.ACCELERATOR_KEY), "sidesonlyaction");

// USED TO SELECT TOTALS ONLY FROM COMBO BOX!

        Action totalsonlyaction = new AbstractAction("TotalsOnly") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("totals only button pressed");
                cb.setSelectedIndex(5);


            }
        };
        totalsonlyaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_4, 0));

        this.getActionMap().put("totalsonlyaction", totalsonlyaction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) totalsonlyaction.getValue(Action.ACCELERATOR_KEY), "totalsonlyaction");


// CLEAR BUTTON

        Action clearaction = new AbstractAction("Clear") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clear button pressed");
                //com.sia.client.ui.AppController.getLinesTableData().clearColors();
                MainScreen ms = (MainScreen) stb.getSelectedComponent();
                ms.setClearTime(new java.util.Date().getTime());


            }
        };
        clearaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, 0));
        clearBut = new JButton(clearaction);
        clearBut.getActionMap().put("clearAction", clearaction);
        clearBut.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) clearaction.getValue(Action.ACCELERATOR_KEY), "clearAction");


// CLEAR ALL BUTTON

        Action clearallaction = new AbstractAction("Clear All") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clear all button pressedAAA");
                //com.sia.client.ui.AppController.getLinesTableData().clearColors();
                long ct = (new java.util.Date()).getTime();
                MainScreen ms = (MainScreen) stb.getSelectedComponent();
                ms.setClearTime(ct);
                AppController.setClearAllTime(ct);
                AppController.clearAll();
                FireThreadManager.emptyIt();


            }
        };
        clearallaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, 0));
        clearAllBut = new JButton(clearallaction);
        clearAllBut.getActionMap().put("clearAllAction", clearallaction);
        clearAllBut.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) clearallaction.getValue(Action.ACCELERATOR_KEY), "clearAllAction");


        adjustcolsBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.out.println("ajustcols button pressed");
                //com.sia.client.ui.AppController.getLinesTableData().clearColors();

                MainScreen ms = (MainScreen) stb.getSelectedComponent();
                ms.adjustcols(true);
                //getDataModel().clearColors();

            }
        });


		/*
		clearAllBut.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent ae)
			{
				System.out.println("clear all button pressed");
				//com.sia.client.ui.AppController.getLinesTableData().clearColors();
				long ct = (new java.util.Date()).getTime();
				com.sia.client.ui.AppController.setClearAllTime(ct);
				com.sia.client.ui.AppController.clearAll();
				FireThreadManager.emptyIt();

			}
		});
		*/


// LAST BUTTON

        Action lastaction = new AbstractAction("Last") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("last button pressed");
                if (lastBut.getText().equals("Current")) {
                    lastBut.setText("Last");
                    //com.sia.client.ui.AppController.getLinesTableData().showCurrent();
                    //getDataModel().showCurrent();
                    stb.setLast(false);
                    Vector v = getAllDataModels();
                    for (int j = 0; j < v.size(); j++) {
                        ((LinesTableData) v.get(j)).showCurrent();
                    }

                } else {
                    lastBut.setText("Current");
                    openerBut.setText("Opener");
                    //com.sia.client.ui.AppController.getLinesTableData().showPrior();
                    //getDataModel().showPrior();
                    stb.setLast(true);

                    Vector v = getAllDataModels();
                    for (int j = 0; j < v.size(); j++) {
                        ((LinesTableData) v.get(j)).showPrior();
                    }

                }


            }
        };
        lastBut = new JButton(lastaction);

        lastaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0));

        lastBut.getActionMap().put("lastAction", lastaction);
        lastBut.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) lastaction.getValue(Action.ACCELERATOR_KEY), "lastAction");
// END LAST BUTTON



/*
		lastBut.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent ae)
			{
				System.out.println("last button pressed");
				if(lastBut.getText().equals("Current"))
				{
					lastBut.setText("Last");
					//com.sia.client.ui.AppController.getLinesTableData().showCurrent();
					//getDataModel().showCurrent();
						stb.setLast(false);
						Vector v = getAllDataModels();
						for(int j=0; j < v.size(); j++)
						{
							((LinesTableData)v.get(j)).showCurrent();
						}

				}
				else
				{
					lastBut.setText("Current");
					openerBut.setText("Opener");
					//com.sia.client.ui.AppController.getLinesTableData().showPrior();
					//getDataModel().showPrior();
						stb.setLast(true);

						Vector v = getAllDataModels();
						for(int j=0; j < v.size(); j++)
						{
							((LinesTableData)v.get(j)).showPrior();
						}

				}
			}
		});
*/
// OPENER BUTTON

        Action openeraction = new AbstractAction("Opener") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("opener button pressed");
                if (openerBut.getText().equals("Current")) {
                    openerBut.setText("Opener");
                    //com.sia.client.ui.AppController.getLinesTableData().showCurrent();
                    //getDataModel().showCurrent();
                    stb.setOpener(false);

                    Vector v = getAllDataModels();
                    for (int j = 0; j < v.size(); j++) {
                        ((LinesTableData) v.get(j)).showCurrent();
                    }
                } else {
                    openerBut.setText("Current");
                    lastBut.setText("Last");
                    //com.sia.client.ui.AppController.getLinesTableData().showOpener();

                    //getDataModel().showOpener();
                    stb.setOpener(true);
                    Vector v = getAllDataModels();
                    for (int j = 0; j < v.size(); j++) {
                        ((LinesTableData) v.get(j)).showOpener();
                    }

                }


            }
        };
        openerBut = new JButton(openeraction);
        openeraction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, 0));

        openerBut.getActionMap().put("openerAction", openeraction);
        openerBut.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) openeraction.getValue(Action.ACCELERATOR_KEY), "openerAction");
// END OPENER BUTTON
/*
		openerBut.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent ae)
			{
				System.out.println("opener button pressed");
				if(openerBut.getText().equals("Current"))
				{
					openerBut.setText("Opener");
					//com.sia.client.ui.AppController.getLinesTableData().showCurrent();
					//getDataModel().showCurrent();
						stb.setOpener(false);

						Vector v = getAllDataModels();
						for(int j=0; j < v.size(); j++)
						{
							((LinesTableData)v.get(j)).showCurrent();
						}
				}
				else
				{
					openerBut.setText("Current");
					lastBut.setText("Last");
					//com.sia.client.ui.AppController.getLinesTableData().showOpener();

					//getDataModel().showOpener();
						stb.setOpener(true);
						Vector v = getAllDataModels();
						for(int j=0; j < v.size(); j++)
						{
							((LinesTableData)v.get(j)).showOpener();
						}

				}
			}
		});
*/
        sortBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.out.println("sort button pressed");
                if (sortBut.getText().equals("Time sort")) {
                    stb.setSort(true);
                    sortBut.setText("Gm# sort");
                    //com.sia.client.ui.AppController.getLinesTableData().showCurrent();
                    //getDataModel().showCurrent();
                    Vector v = getAllDataModels();
                    for (int j = 0; j < v.size(); j++) {
                        ((LinesTableData) v.get(j)).timesort();
                    }
                } else {
                    stb.setSort(false);
                    sortBut.setText("Time sort");

                    //com.sia.client.ui.AppController.getLinesTableData().showOpener();

                    //getDataModel().showOpener();
                    Vector v = getAllDataModels();
                    for (int j = 0; j < v.size(); j++) {
                        ((LinesTableData) v.get(j)).gmnumsort();
                    }

                }
            }
        });

        addBookieBut.addActionListener(ae -> {
			// AudioClip clipfinal = new AudioClip("c:\\spankoddsclient\\final.wav");
			//  clipfinal.play();
			checkAndRunInEDT(() -> {
				BookieColumnController2 bcc2 = new BookieColumnController2();
			});


		});

        //owen this one we will have to repaint somehow
        shrinkTeamBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.out.println("shrinkTeamBut button pressed");
                if (shrinkTeamBut.getText().equals("Short Team")) {
                    stb.setShort(true);
                    shrinkTeamBut.setText("Long Team");
                    //stb.refreshTabs();
                    stb.refreshCurrentTab();
                } else {
                    stb.setShort(false);
                    shrinkTeamBut.setText("Short Team");
                    //stb.refreshTabs();
                    stb.refreshCurrentTab();

                }
            }
        });


        alertBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!");
            }
        });

    }

    public Vector getAllDataModels() {
        Vector ltd = stb.getAllDataModels();
        return ltd;

    }

    public void itemStateChanged(ItemEvent e) {
        // if the state combobox is changed
        if (e.getSource() == cb) {
            Vector v = getAllDataModels();
            for (int j = 0; j < v.size(); j++) {
                ((LinesTableData) v.get(j)).setDisplayType(display[cb.getSelectedIndex()]);
            }
            stb.setDisplay(display[cb.getSelectedIndex()]);
            //cb.transferFocus();
            stb.requestFocusInWindow();
            //	com.sia.client.ui.AppController.setDisplayType(display[cb.getSelectedIndex()]);
            //owen took out getting period and siaplay from ltd
            //com.sia.client.ui.ChartView.setItem(display[cb.getSelectedIndex()]);
            //SoccerChartView.setItem(display[cb.getSelectedIndex()]);

        } else if (e.getSource() == periodcb) {
            Vector v = getAllDataModels();
            for (int j = 0; j < v.size(); j++) {
                ((LinesTableData) v.get(j)).setPeriodType(perioddisplay[periodcb.getSelectedIndex()]);
            }
            stb.setPeriod(perioddisplay[periodcb.getSelectedIndex()]);
            //periodcb.transferFocus();
            stb.requestFocusInWindow();
            //owen took out getting period and siaplay from ltd
            //	com.sia.client.ui.ChartView.setPeriod(perioddisplay[periodcb.getSelectedIndex()]);
            //	SoccerChartView.setPeriod(perioddisplay[periodcb.getSelectedIndex()]);
            System.out.println("JUST SET PERIOD=" + perioddisplay[periodcb.getSelectedIndex()]);
        }
    }
		/*
	public LinesTable2 getScrollPane()
	{
		
			BorderPane bp = (BorderPane)getParent();
			System.out.println("bp="+bp);
			SportsTabPane stb = (SportsTabPane)bp.getCenter();
			System.out.println("stb="+stb);
			LinesTable2 lt2 = stb.getCurrentScrollPane();
			return lt2;
	}
*/

    public Vector getCurrentDataModels() {
        Vector ltd = stb.getCurrentDataModels();
        return ltd;

    }

}