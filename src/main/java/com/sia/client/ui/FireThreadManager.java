package com.sia.client.ui;

import java.util.Hashtable;

public class FireThreadManager
{

	static Hashtable threadsHash = new Hashtable();
	
	
	public static void fire(String id, LinesTableData ltd)
	{
	
		Long time = (Long)threadsHash.get(id);
		long now = new java.util.Date().getTime();

		if(time == null)
		{
			threadsHash.put(id,new Long(now));
			new FireThread(ltd).start();
		}
		else
		{
			if(now - time.longValue() > 30000)
			{
				threadsHash.put(id,new Long(now));
				new FireThread(ltd).start();
			}
			else
			{
				
			}
		}

	}
	
	public static void remove(String id)
	{
		if(threadsHash.get(id) != null)
		{
			threadsHash.remove(id);
		}
		
	}
	public static void emptyIt()
	{
		threadsHash.clear();
		
	}
}
