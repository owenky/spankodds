package com.sia.client.model;

import com.sia.client.ui.ChartView;

import javax.swing.ImageIcon;
import java.awt.Color;
class ChartData2
{
	 int gn;
	 int p;
     int asaT;
     int hsaT;
	 String DS;
	 int oaT;
	 int uaT;
     String DT;
	 int amaT;
	 int hmaT;
	 String DM;
	 int drawMamt;
	 int aoaT;
	 int auaT;
	 String DA;
	 int hoaT;
	 int huaT;
	 String DH;
	
	public String toString()
	{
		return gn+","
		+p+","
		+asaT+","
		+hsaT+","
		+DS+","
		+oaT+","
		+uaT+","
		+DT+","
		+amaT+","
		+hmaT+","
		+DM;
		
		
	}
   	
}
class ChartData3
{
	 Color spreadcolor = Color.WHITE;
	 Color totalcolor = Color.WHITE;
	 Color moneycolor = Color.WHITE;
	 Color awaycolor = Color.WHITE;
	 Color homecolor = Color.WHITE;
	 ImageIcon spreadicon = ChartView.ICON_BLANK;
	 ImageIcon totalicon =ChartView.ICON_BLANK;
	 ImageIcon moneyicon = ChartView.ICON_BLANK;
	 ImageIcon awayicon = ChartView.ICON_BLANK;
	 ImageIcon homeicon = ChartView.ICON_BLANK;
	int gn;
	int p;
	int DS;
	int DT;
	int DM;
	int DA;
	int DH;
	String NDS;
	String NDT;
	String NDM;
	String NDA;
	String NDH;
	 boolean isGreat1;
	 boolean isGreat2;
	 boolean isGreat3;
	 boolean isGreat4;
	 boolean isGreat5;
	 boolean dataexists;
	 
	 public String toString()
	{
		return gn+","
		+p+","
		+DS+","
		+DM+","
		+NDS+","
		+NDT+","
		+NDM+","
		+isGreat1+","
		+isGreat2+","
		+isGreat3;
	
		
		
	}
	
}