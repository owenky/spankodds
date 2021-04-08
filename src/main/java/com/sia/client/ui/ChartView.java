package com.sia.client.ui;

import java.util.*;
import javax.swing.*;
import java.awt.Color;

public class ChartView 
{

	public static ImageIcon ICON_UP = new ImageIcon("moveup.png");
    public static ImageIcon ICON_DOWN = new ImageIcon("movedown.png");
    public static ImageIcon ICON_BLANK = new ImageIcon("blank2.gif");
	Spreadline sl;
	Totalline tl;
	LineData topbox;
	LineData bottombox;
/*	static Color spreadcolor = Color.WHITE;
	static Color totalcolor = Color.WHITE;
	static Color moneycolor = Color.WHITE;
	static Color awaycolor = Color.WHITE;
	static Color homecolor = Color.WHITE;
	static ImageIcon spreadicon = ICON_BLANK;
	static ImageIcon totalicon = ICON_BLANK;
	static ImageIcon moneyicon = ICON_BLANK;
	static ImageIcon awayicon = ICON_BLANK;
	static ImageIcon homeicon = ICON_BLANK;*/
	
	public static LineData[] boxes = new LineData[2]; 
	public static LineData ld1 = new LineData(ICON_BLANK,"",Color.WHITE);
	public static LineData ld2 = new LineData(ICON_BLANK,"",Color.WHITE);
	
	
	
	int bid;
	int gid;
	Sport sp;
	Game g;
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
	}	
	
	
	
	public ChartView(){}

	public ChartView(int gid)
	{
		
		
		this.gid = gid;
		
		g = AppController.getGame(gid);
		sp = AppController.getSport(g.getLeague_id());
		this.getCurrentBoxes();
		//this.setAndGetPriorBoxes(bid,gid);
		//this.setAndGetOpenerBoxes(bid,gid);
		
		
	}
	
	/*
	public static  void setItem(String itm){
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
				ChartChecker.cl1.get(i).isGreat1=false;
			
				ChartChecker.cl1.get(i).totalcolor=Color.WHITE;
				ChartChecker.cl1.get(i).isGreat2=false;
			
				ChartChecker.cl1.get(i).moneycolor=Color.WHITE;
				ChartChecker.cl1.get(i).isGreat3=false;
			
				ChartChecker.cl1.get(i).awaycolor=Color.WHITE;
				ChartChecker.cl1.get(i).isGreat4=false;
			
				ChartChecker.cl1.get(i).homecolor=Color.WHITE;
				ChartChecker.cl1.get(i).isGreat5=false;
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
				//System.out.println("setting period=2 for gameid="+g.getGame_id());
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
		ld1.setIcon(ICON_BLANK);
		ld2.setIcon(ICON_BLANK);
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
					ld2.setIcon(ChartChecker.cl1.get(i).totalicon);
					ld1.setBackgroundColor(ChartChecker.cl1.get(i).spreadcolor);
					ld2.setBackgroundColor(ChartChecker.cl1.get(i).totalcolor);
					ld1.setData(ChartChecker.cl1.get(i).NDS);
					ld2.setData(ChartChecker.cl1.get(i).NDT);
					
					//boxes[0] = ld1;
					//boxes[1] = ld2;				
				}
				else if(item.equals("totalmoney")||item.equals("totalbothmoney"))
				{
					ld1.setIcon(ChartChecker.cl1.get(i).totalicon);
					ld2.setIcon(ChartChecker.cl1.get(i).moneyicon);
					ld1.setBackgroundColor(ChartChecker.cl1.get(i).totalcolor);
					ld2.setBackgroundColor(ChartChecker.cl1.get(i).moneycolor);
					ld1.setData(ChartChecker.cl1.get(i).NDT);
					ld2.setData(ChartChecker.cl1.get(i).NDM);
					
					//boxes[0] = ld1;
					//boxes[1] = ld2;
				}
				else if(item.equals("justspread"))
				{
					ld1.setIcon(ChartChecker.cl1.get(i).spreadicon);
					ld1.setBackgroundColor(ChartChecker.cl1.get(i).spreadcolor);
					ld2.setBackgroundColor(Color.WHITE);
					ld1.setData(ChartChecker.cl1.get(i).NDS);
					ld2.setData("");
					
					//boxes[0] = ld1;
					//boxes[1] = ld2;
				}
				else if(item.equals("justtotal"))
				{
					ld2.setIcon(ChartChecker.cl1.get(i).totalicon);
					ld1.setBackgroundColor(Color.WHITE);
					ld2.setBackgroundColor(ChartChecker.cl1.get(i).totalcolor);
					ld1.setData("");
					ld2.setData(ChartChecker.cl1.get(i).NDT);
					
					//boxes[0] = ld1;
					//boxes[1] = ld2;
				}
				else if(item.equals("justmoney"))
				{
					ld1.setIcon(ChartChecker.cl1.get(i).moneyicon);
					ld1.setBackgroundColor(ChartChecker.cl1.get(i).moneycolor);
					ld2.setBackgroundColor(Color.WHITE);
					ld1.setData(ChartChecker.cl1.get(i).NDM);
					ld2.setData("");
					
				//	boxes[0] = ld1;
				//	boxes[1] = ld2;
				}
				else if(item.equals("awayteamtotal"))
				{
					ld1.setIcon(ChartChecker.cl1.get(i).awayicon);
					ld1.setBackgroundColor(ChartChecker.cl1.get(i).awaycolor);
					ld2.setBackgroundColor(Color.WHITE);
					ld1.setData(ChartChecker.cl1.get(i).NDA);
					ld2.setData("");
					
				}
				else if(item.equals("hometeamtotal"))
				{
					ld1.setIcon(ChartChecker.cl1.get(i).homeicon);
					ld1.setBackgroundColor(ChartChecker.cl1.get(i).homecolor);
					ld2.setBackgroundColor(Color.WHITE);
					ld1.setData(ChartChecker.cl1.get(i).NDH);
					ld2.setData("");
					
				}
				
				
			}
		}
		boxes[0] = ld1;
	   boxes[1] = ld2;
	//	ld1.setBackgroundColor(topcolor);
		//ld2.setBackgroundColor(bottomcolor);		
		
		//return boxes;
		
	}
	


}