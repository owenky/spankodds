package com.sia.client.ui;

import com.sia.client.model.Game;
import com.sia.client.model.Sport;

import java.util.*;
import javax.swing.*;
import java.awt.Color;

public class SoccerChartView
{
	
	public static ImageIcon ICON_UP = new ImageIcon("moveup.png");
    public static ImageIcon ICON_DOWN = new ImageIcon("movedown.png");
    public static ImageIcon ICON_BLANK = null;//new ImageIcon("blank.gif");
	Spreadline sl;
	Totalline tl;
	/*static Color spreadcolor = Color.WHITE;
	static Color totalcolor = Color.WHITE;
	static Color moneycolor = Color.WHITE;
	static Color awaycolor = Color.WHITE;
	static Color homecolor = Color.WHITE;
	static ImageIcon spreadicon = ICON_BLANK;
	static ImageIcon totalicon = ICON_BLANK;
	static ImageIcon moneyicon = ICON_BLANK;
	static ImageIcon awayicon = ICON_BLANK;
	static ImageIcon homeicon = ICON_BLANK;*/
	LineData topbox;
	LineData bottombox;
	public static LineData[] boxes = new LineData[4]; 
	public static LineData ld1 = new LineData(ICON_BLANK,"",Color.WHITE);
	public static LineData ld2 = new LineData(ICON_BLANK,"",Color.WHITE);
	public static LineData ld3 = new LineData(ICON_BLANK,"",Color.WHITE);
	public static LineData ld4 = new LineData(ICON_BLANK,"",Color.WHITE);
	
	int bid;
	int gid;
	Game g;
	Sport sp;
	 int period;
	 String item="spreadtotal";
	List li=ChartChecker.cl;
	LinesTableData ltd;
	
	
	
		public String getDisplayType()
	{
		return item;
	}
	public void setDisplayType(String d)
	{
		item = d;
		boxes[0].setBackgroundColor(Color.WHITE);
		boxes[1].setBackgroundColor(Color.WHITE);
	}
	public int getPeriodType()
	{
		return period;
	}
	public void setPeriodType(int d)
	{
		period = d;
		boxes[0].setBackgroundColor(Color.WHITE);
		boxes[1].setBackgroundColor(Color.WHITE);		
		boxes[2].setBackgroundColor(Color.WHITE);		
		boxes[3].setBackgroundColor(Color.WHITE);		
	}	
	

	public SoccerChartView(int gid)
	{
		
		
		this.gid = gid;
		
		g = AppController.getGame(gid);
		sp = AppController.getSport(g.getLeague_id());
		this.getCurrentBoxes();
		//this.setAndGetPriorBoxes(bid,gid);
		//this.setAndGetOpenerBoxes(bid,gid);
		
		
	}
	/*
	public static void setItem(String itm){
		item=itm;
	}
	public static void setPeriod(int per){
		period=per;
	}
	*/
	public static void clearColors()
	{
		for(int i=0;i<ChartChecker.cl1.size();i++)
		{
			
				ChartChecker.cl1.get(i).spreadcolor=Color.WHITE;
			
			
				ChartChecker.cl1.get(i).totalcolor=Color.WHITE;
			
			
				ChartChecker.cl1.get(i).moneycolor=Color.WHITE;
			
			
				ChartChecker.cl1.get(i).awaycolor=Color.WHITE;
			
			
				ChartChecker.cl1.get(i).homecolor=Color.WHITE;
		}
		boxes[0].setBackgroundColor(Color.WHITE);
		boxes[1].setBackgroundColor(Color.WHITE);
		
	}	
	public LineData gettopbox()
	{
		return topbox;
	}
	public LineData getbottombox()
	{
		return bottombox;
	}
	
	
	public LineData[] getCurrentBoxes()
	{
		if(g.getStatus().equalsIgnoreCase("Time") && period ==0)
			{
				period = 2;
			}	
		setCurrentBoxes();
		return boxes;
	}
	
	public void setCurrentBoxes()
	{
		String topboxS = "";
		String bottomboxS = "";
		
				
        ld1.setData("");
		ld2.setData("");
		ld3.setData("");
		ld4.setData("");
		
		for(int i=0;i<ChartChecker.cl1.size();i++)
		{
			if(ChartChecker.cl1.get(i).gn==gid&&ChartChecker.cl1.get(i).p==period)
			{
				if(!ChartChecker.cl1.get(i).dataexists)
				{
					break;
				}	
				if(item.equals("default"))
				{
				
					if(sp.getSport_id() == 3 ) // baseball
					{
						item = "totalbothmoney";
					}
					else if(sp.getParentleague_id() == 9) // soccer 
					{
						item = "totalmoney";
					}
					else if(sp.getMoneylinedefault())
					{
						item = "justmoney";
					}
					else
					{
						item = "spreadtotal";
					}
				}
				
				if(item.equals("spreadtotal"))
				{
					ld1.setIcon(ChartChecker.cl1.get(i).spreadicon);
					ld4.setIcon(ChartChecker.cl1.get(i).totalicon);
					
					ld1.setBackgroundColor(ChartChecker.cl1.get(i).spreadcolor);
					ld2.setBackgroundColor(Color.WHITE);
					ld3.setBackgroundColor(Color.WHITE);
					ld4.setBackgroundColor(ChartChecker.cl1.get(i).totalcolor);
					
					ld1.setData(ChartChecker.cl1.get(i).NDS);
					ld2.setData("");
					ld3.setData(ChartChecker.cl.get(i).drawMamt+"");
					ld4.setData(ChartChecker.cl1.get(i).NDT);
					
					//boxes[0] = ld1;
					//boxes[1] = ld2;				
				}
				else if(item.equals("totalmoney")||item.equals("totalbothmoney"))
				{
					ld1.setIcon(ChartChecker.cl1.get(i).moneyicon);
					ld4.setIcon(ChartChecker.cl1.get(i).totalicon);
					ld1.setBackgroundColor(ChartChecker.cl1.get(i).moneycolor);
					ld2.setBackgroundColor(Color.WHITE);
					ld3.setBackgroundColor(Color.WHITE);
					ld4.setBackgroundColor(ChartChecker.cl1.get(i).totalcolor);
					
					ld1.setData(ChartChecker.cl1.get(i).NDM);
					ld2.setData("");
					ld3.setData(ChartChecker.cl.get(i).drawMamt+"");
					ld4.setData(ChartChecker.cl1.get(i).NDT);
					
					//boxes[0] = ld1;
					//boxes[1] = ld2;
				}
				else if(item.equals("justspread"))
				{
					ld1.setIcon(ChartChecker.cl1.get(i).spreadicon);
					ld1.setBackgroundColor(ChartChecker.cl1.get(i).spreadcolor);
					ld2.setBackgroundColor(Color.WHITE);
					ld3.setBackgroundColor(Color.WHITE);
					ld4.setBackgroundColor(Color.WHITE);
					
					ld1.setData(ChartChecker.cl1.get(i).NDS);
					ld2.setData("");
					ld3.setData(ChartChecker.cl.get(i).drawMamt+"");
					ld4.setData("");
					
					//boxes[0] = ld1;
					//boxes[1] = ld2;
				}
				else if(item.equals("justtotal"))
				{
					ld4.setIcon(ChartChecker.cl1.get(i).totalicon);
					ld1.setBackgroundColor(Color.WHITE);
					ld2.setBackgroundColor(Color.WHITE);
					ld3.setBackgroundColor(Color.WHITE);
					ld4.setBackgroundColor(ChartChecker.cl1.get(i).totalcolor);
					
					ld1.setData("");
					ld2.setData("");
					ld3.setData(ChartChecker.cl.get(i).drawMamt+"");
					ld4.setData(ChartChecker.cl1.get(i).NDT);
					
					//boxes[0] = ld1;
					//boxes[1] = ld2;
				}
				else if(item.equals("justmoney"))
				{
					ld1.setIcon(ChartChecker.cl1.get(i).moneyicon);
					ld1.setBackgroundColor(ChartChecker.cl1.get(i).moneycolor);
					ld2.setBackgroundColor(Color.WHITE);
					ld3.setBackgroundColor(Color.WHITE);
					ld4.setBackgroundColor(Color.WHITE);
					
					ld1.setData(ChartChecker.cl1.get(i).NDM);
					ld2.setData("");
					ld3.setData(ChartChecker.cl.get(i).drawMamt+"");
					ld4.setData("");
					
				//	boxes[0] = ld1;
				//	boxes[1] = ld2;
				}
				else if(item.equals("awayteamtotal"))
				{
					ld4.setIcon(ChartChecker.cl1.get(i).awayicon);
					ld1.setBackgroundColor(Color.WHITE);
					ld2.setBackgroundColor(Color.WHITE);
					ld3.setBackgroundColor(Color.WHITE);
					ld4.setBackgroundColor(ChartChecker.cl1.get(i).awaycolor);
					
					ld1.setData("");
					ld2.setData("");
					ld3.setData(ChartChecker.cl.get(i).drawMamt+"");
					ld4.setData(ChartChecker.cl1.get(i).NDA);
					
				}
				else if(item.equals("hometeamtotal"))
				
				{
					ld4.setIcon(ChartChecker.cl1.get(i).homeicon);
					ld1.setBackgroundColor(Color.WHITE);
					ld2.setBackgroundColor(Color.WHITE);
					ld3.setBackgroundColor(Color.WHITE);
					ld4.setBackgroundColor(ChartChecker.cl1.get(i).homecolor);
					
					ld1.setData("");
					ld2.setData("");
					ld3.setData(ChartChecker.cl.get(i).drawMamt+"");
					ld4.setData(ChartChecker.cl1.get(i).NDH);
					
				}
				
				
			}
		}
		boxes[0] = ld1;
	   boxes[1] = ld2;
	   boxes[2] = ld3;
	   boxes[3] = ld4;
	   
	//	ld1.setBackgroundColor(topcolor);
		//ld2.setBackgroundColor(bottomcolor);		
		
		//return boxes;
	}
}