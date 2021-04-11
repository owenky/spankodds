package com.sia.client.ui;

import com.sia.client.model.Game;

import java.util.Comparator;
import java.text.SimpleDateFormat;
public class GameDateSorter implements Comparator<Game>
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public int compare(Game g1, Game g2) 
    {
		if(g1 == null || g2 == null || g1.getGamedate() == null || g2.getGamedate() == null)
		{
			return 0;
		}
		String gamedate1 = sdf.format(g1.getGamedate());
		String gamedate2 = sdf.format(g2.getGamedate());
        return gamedate1.compareTo(gamedate2);
    }
}
 
