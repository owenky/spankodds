import java.util.Comparator;
 
public class GameLeagueSorter implements Comparator<Game> 
{
    public int compare(Game g1, Game g2) 
    {
		//System.out.print(g1.getLeague_id()+".."+g2.getLeague_id()+"~~~");
		if(g1 == null || g2 == null)
		{
			return 0;
		}
        else if(g1.getLeague_id() < g2.getLeague_id())
		{
			return -1;
		}
		else if(g1.getLeague_id() > g2.getLeague_id())
		{
			return 1;
		}
		else
		{
			return 0;
		}
    }
}