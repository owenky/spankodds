package com.sia.client.ui;

import java.util.Comparator;
import java.text.SimpleDateFormat;
public class GameTimeSorter implements Comparator<Game> 
{
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); 
    public int compare(Game g1, Game g2) 
    {
		//if(g1.getGame_id() == 483)
		//{
		//	System.out.println("gmtime for 483 ="+g1.getGametime();)
		//}
		if(g1 == null || g2 == null || g1.getGametime() == null || g2.getGametime() == null)
		{
			return 0;
		}		
		String gametime1 = sdf.format(g1.getGametime());
		String gametime2 = sdf.format(g2.getGametime());
        return gametime1.compareTo(gametime2);
    }
}
 
