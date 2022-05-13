package com.sia.client.ui.lineseeker;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum AlertSectionName {

    spreadName("Spreads",0,true,"",""),
    totalsName("Totals",1,true,"Over","Under"),
    mLineName ("Money Lines",2,false,"",""),
    awayName ("Away TT",3, true,"Over","Under"),
    homeTTName ("Home TT",4, true,"Over","Under");

    private final String display;
    private final int displayOrder;
    private final String leftColTitle;
    private final String rightColTitle;
    private final boolean toShowLineInput;
    AlertSectionName(String display,int displayOrder,boolean toShowLineInput,String leftColTitle,String rightColTitle) {
        this.display = display;
        this.displayOrder = displayOrder;
        this.leftColTitle = leftColTitle;
        this.rightColTitle = rightColTitle;
        this.toShowLineInput = toShowLineInput;
    }
    public String getDisplay() {
        return display;
    }
    public int getDisplayOrder() {
        return this.displayOrder;
    }
    public String getLeftColTitle() {
        return leftColTitle;
    }
    public String getRightColTitle() {
        return rightColTitle;
    }
    public boolean toShowLineInput() {
        return toShowLineInput;
    }
    public static List<AlertSectionName> getSortedSectionNames() {
        return Arrays.stream(AlertSectionName.values()).sorted(Comparator.comparingInt(a -> a.displayOrder))
                .collect(Collectors.toList());

    }
}
