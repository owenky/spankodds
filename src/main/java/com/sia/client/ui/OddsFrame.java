package com.sia.client.ui;

import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;

//this thing doesnt work so i stopped using it 7/11/2020

public class OddsFrame extends JFrame
{
JMenuBar jmb;
SportsTabPane stb;
TopView tv;
OddsFrame thisframe;


public OddsFrame(SportsTabPane stb , TopView tv)
{	
	super("Spank Odds");
	thisframe = this;

                    this.stb = stb;
	
					this.tv = tv;
				
     			
						
	    
		thisframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		thisframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window Closing! ");
				AppController.removeFrame(thisframe);
                
            }
        });
				
					JPanel mainpanel = new JPanel();
					mainpanel.setLayout(new BorderLayout());
					//mainpanel.add(tv,BorderLayout.CENTER);
					mainpanel.add(this.tv,BorderLayout.PAGE_START);
					mainpanel.add(this.stb,BorderLayout.CENTER);
					//frame.setLayout(new BorderLayout());

					thisframe.setContentPane(mainpanel);
				
				AppController.addFrame(thisframe,this.stb);
				thisframe.setSize(950, 800);
					
				//	while(com.sia.client.ui.AppController.isLoadingInitial())
					//{
						
				//	}
						
					try 
						{ 
							//System.out.println("sleeping 5 secs");
							//Thread.sleep(5000);
						}
						catch(Exception ex ) {}	
											
					//stb.refreshCurrentTab();
					thisframe.setVisible(true);		
				
}


public void show()
{

					

}







}