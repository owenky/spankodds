package com.sia.client.ui;

import com.sia.client.model.Game;

import java.util.Comparator;
 
public class GameNumSorter implements Comparator<Game>
{
    public int compare(Game g1, Game g2) 
    {
		if(g1 == null || g2 == null)
		{
			return 0;
		}
        else if(g1.getGame_id() <= g2.getGame_id())
		{
			return -1;
		}
		else
		{
			return 1;
		}
    }
}
