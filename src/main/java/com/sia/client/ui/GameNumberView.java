package com.sia.client.ui;

import javax.swing.*;
import java.awt.Color;

public class GameNumberView 
{

	public static ImageIcon ICON_UP = new ImageIcon("ArrUp.gif");
    public static ImageIcon ICON_DOWN = new ImageIcon("ArrDown.gif");
    public static ImageIcon ICON_BLANK = null;//new ImageIcon("blank.gif");
	Spreadline sl;
	Totalline tl;
	LineData topbox;
	LineData bottombox;
	LineData[] boxes = new LineData[2]; 
	LineData ld1 = new LineData(ICON_BLANK,"",Color.WHITE);
	LineData ld2 = new LineData(ICON_BLANK,"",Color.WHITE);
	int bid;
	int gid;
	Game g;
	

	public GameNumberView(int gid)
	{
		
		
		this.gid = gid;
		g = AppController.getGame(gid);
		//this.setAndGetPriorBoxes(bid,gid);
		//this.setAndGetOpenerBoxes(bid,gid);
		
		
	}
	public void clearColors()
	{
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
		setCurrentBoxes();
		return boxes;
	}
	
	public void setCurrentBoxes()
	{
		String topboxS = "";
		String bottomboxS = "";
		Color spreadcolor = Color.WHITE;
		Color totalcolor = Color.WHITE;
		Color topcolor = Color.WHITE;
		Color bottomcolor = Color.WHITE;
		ImageIcon topicon = ICON_BLANK;
		ImageIcon bottomicon = ICON_BLANK;		

		ld1.setIcon(topicon);
		ld2.setIcon(bottomicon);
		ld1.setData(""+g.getVisitorgamenumber());
		ld2.setData(""+g.getHomegamenumber());
		ld1.setBackgroundColor(topcolor);
		ld2.setBackgroundColor(bottomcolor);		
		boxes[0] = ld1;
		boxes[1] = ld2;
		//return boxes;
	}
	


}
