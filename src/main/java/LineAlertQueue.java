import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;


public class LineAlertQueue extends ArrayBlockingQueue
{

	Game g;
	long lastalerted;
	public LineAlertQueue(int numbookies)
	{
		super(numbookies,true);
		lastalerted = 0;
	}
	

	public void setlastalerted(long la)
	{
		lastalerted = la;
	}
	public long getlastalerted()
	{
		return lastalerted;
	}
}