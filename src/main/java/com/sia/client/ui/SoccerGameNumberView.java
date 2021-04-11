package com.sia.client.ui;

import com.sia.client.model.Game;
import com.sia.client.model.LineData;

import javax.swing.*;
import java.awt.Color;

public class SoccerGameNumberView 
{

	public static ImageIcon ICON_UP = new ImageIcon("ArrUp.gif");
    public static ImageIcon ICON_DOWN = new ImageIcon("ArrDown.gif");
    public static ImageIcon ICON_BLANK = null;//new ImageIcon("blank.gif");
	Spreadline sl;
	Totalline tl;
	LineData topbox;
	LineData bottombox;
	LineData drawbox;
	LineData totalbox;
	LineData[] boxes = new LineData[4]; 
	LineData ld1 = new LineData(ICON_BLANK,"",Color.WHITE);
	LineData ld2 = new LineData(ICON_BLANK,"",Color.WHITE);
	LineData ld3 = new LineData(ICON_BLANK,"",Color.WHITE);
	LineData ld4 = new LineData(ICON_BLANK,"",Color.WHITE);
	int bid;
	int gid;
	
	Game g;
	

	public SoccerGameNumberView(int gid)
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
		boxes[2].setBackgroundColor(Color.WHITE);
		boxes[3].setBackgroundColor(Color.WHITE);
		
	}	
	public LineData gettopbox()
	{
		return topbox;
	}
	public LineData getbottombox()
	{
		return bottombox;
	}
	public LineData getdrawbox()
	{
		return drawbox;
	}
	public LineData gettotalbox()
	{
		return totalbox;
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
		String drawboxS = "";
		String totalboxS = "";
		Color spreadcolor = Color.WHITE;
		Color totalcolor = Color.WHITE;
		Color topcolor = Color.WHITE;
		Color bottomcolor = Color.WHITE;
		ImageIcon topicon = ICON_BLANK;
		ImageIcon bottomicon = ICON_BLANK;	

		Color drawcolor = Color.WHITE;
		//Color totalcolor = Color.WHITE;
		ImageIcon drawicon = ICON_BLANK;
		ImageIcon totalicon = ICON_BLANK;			

		ld1.setIcon(topicon);
		ld2.setIcon(bottomicon);
		
		ld3.setIcon(drawicon);
		ld4.setIcon(totalicon);
		
		ld1.setData(""+g.getVisitorgamenumber());
		ld2.setData(""+g.getHomegamenumber());
		ld3.setData(""+(g.getHomegamenumber()+1));
		ld4.setData(""+(g.getHomegamenumber()+2));
		ld1.setBackgroundColor(topcolor);
		ld2.setBackgroundColor(bottomcolor);
		ld3.setBackgroundColor(drawcolor);
		ld4.setBackgroundColor(totalcolor);
		boxes[0] = ld1;
		boxes[1] = ld2;
		boxes[2] = ld3;
		boxes[3] = ld4;
		//return boxes;
	}
	


}
