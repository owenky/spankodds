package com.sia.client.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GameGroupDateSorter implements GameGroupHeaderSorter {
	@Override
    public int compare(GameGroupHeader g1, GameGroupHeader g2) {

	    int result = compareAnchorPos(g1,g2);
	    if ( 0 != result) {
	        return result;
        }
	    return g1.getGameDate().compareTo(g2.getGameDate());
    }
    public static void main(String [] argv) {
		LocalDateTime test = LocalDateTime.now();
		LocalDate date = LocalDate.of(test.getYear(), test.getMonth(),test.getDayOfMonth());

		System.out.println(date);
		System.out.println(test);
	}
}