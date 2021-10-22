package com.sia.client.model;

import java.awt.Color;
import java.awt.Font;

public class ColumnHeaderProperty {

    private final Color headerBackground;
    private final Color headerForeground;
    private final Font headerFont;
    private final int columnHeaderHeight;

    public ColumnHeaderProperty(Color headerBackground, Color headerForeground, Font headerFont, int columnHeaderHeight) {
        this.headerBackground = headerBackground;
        this.headerForeground = headerForeground;
        this.headerFont = headerFont;
        this.columnHeaderHeight = columnHeaderHeight;
    }
    public Color getHeaderBackground() {
        return headerBackground;
    }
    public Color getHeaderForeground() {
        return headerForeground;
    }

    public Font getHeaderFont() {
        return headerFont;
    }

    public int getColumnHeaderHeight() {
        return columnHeaderHeight;
    }
//    @Override
//    public boolean equals(final Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        final ColumnHeaderProperty that = (ColumnHeaderProperty) o;
//        boolean status = columnHeaderHeight == that.columnHeaderHeight && Objects.equals(headerBackground, that.headerBackground) && Objects.equals(headerForeground, that.headerForeground)
//                && Objects.equals(headerFont, that.headerFont);
//
//        if ( status ) {
//            status = isSetsSame(rowIndexToHeadValueMap.keySet(),that.rowIndexToHeadValueMap.keySet());
//            if ( status ) {
//                status = isSetsSame(rowIndexToHeadValueMap.values(),that.rowIndexToHeadValueMap.values());
//            }
//        }
//        return status;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(headerBackground, headerForeground, headerFont, columnHeaderHeight, rowIndexToHeadValueMap);
//    }
//    public static boolean isSetsSame(Collection<?> set1, Collection<?> set2) {
//        boolean status = set1.size()==set2.size();
//        if ( status ) {
//            status = set1.containsAll(set2);
//        }
//        return status;
//    }
}
