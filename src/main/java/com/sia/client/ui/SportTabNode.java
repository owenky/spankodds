package com.sia.client.ui;

import com.sia.client.ui.control.MainScreen;

import javax.swing.ImageIcon;

class SportTabNode
{
String	sportname1;
String	sportname2;

ImageIcon icon;

MainScreen ms;

public SportTabNode(String sportname1,ImageIcon icon,MainScreen ms,String sportname2)
{
	this.sportname1=sportname1;
	this.icon=icon;
	this.ms=ms;
	this.sportname2=sportname2;
	
	
}

//addTab("Baseball",new ImageIcon("baseball.png"),new MainScreen("Baseball"),"Baseball");	

	
	
	
}