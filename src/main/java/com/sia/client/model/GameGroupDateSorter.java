package com.sia.client.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class GameGroupDateSorter implements GameGroupHeaderSorter {
	@Override
    public int compare(GameGroupHeader g1, GameGroupHeader g2) {

	    int result = compareAnchorPos(g1,g2);
	    if ( 0 != result) {
	        return result;
        }
		if ( null == g1 && null == g2) {
			return 0;
		} else if ( null == g1) {
			return -1;
		} else if ( null == g2) {
			return 1;
		}
	    return g1.getGameDate().compareTo(g2.getGameDate());
    }
    public static void main(String [] argv) {
		LocalDateTime test = LocalDateTime.now();
		LocalDate date = LocalDate.of(test.getYear(), test.getMonth(),test.getDayOfMonth());

		System.out.println(date);
		System.out.println(test);

		System.out.println(new Date(1651716600000L));
	}

}