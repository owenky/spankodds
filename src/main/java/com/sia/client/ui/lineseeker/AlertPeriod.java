package com.sia.client.ui.lineseeker;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum AlertPeriod {
    Full("Full Game",0),
    FirstHalf("First Half",1),
    SecondHalf("Second Half",2);

    AlertPeriod(String display,int order) {
        this.display = display;
        this.order = order;
    }
    @Override
    public String toString() {
        return display;
    }
    public static List<AlertPeriod> getOrderedVec() {
        return Arrays.stream(AlertPeriod.values()).sorted(Comparator.comparingInt(p -> p.order)).collect(Collectors.toList());
    }
    private final String display;
    private final int order;
}
