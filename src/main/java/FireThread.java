import javax.swing.SwingUtilities;

public class FireThread extends Thread
{
	
LinesTableData ltd;	
public FireThread(LinesTableData ltd)	
{
	this.ltd = ltd;
	
}
	
public void run()
{
	try
	{
			
		Thread.sleep(30000);




		try
		{
			if(ltd!= null)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{				
					ltd.fireTableDataChanged();
					}
				});				
			}
		}
		catch(Exception ex){}
			
			
		
	
	//System.out.println("fired table!");
	}
	catch(Exception ex)
	{
		System.out.println("exception firethread!");
	}
}



}
