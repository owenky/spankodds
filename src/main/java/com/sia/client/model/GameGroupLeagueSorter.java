package com.sia.client.model;

public class GameGroupLeagueSorter implements GameGroupHeaderSorter {
	@Override
    public int compare(GameGroupHeader g1, GameGroupHeader g2) {

	    int result = compareAnchorPos(g1,g2);
	    if ( 0 != result) {
	        return result;
        }
	    result = g1.getSubLeagueId()-g2.getSubLeagueId();
	    if ( 0 != result ) {
	    	return result;
		}
	    return g1.getLeagueName().compareTo(g2.getLeagueName());
    }
}