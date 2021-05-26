package com.sia.client.ui;

import javax.swing.JTable;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ColumnAdjustPreparer {

    private final JTable table;
    private int max_calculate_row = -1;
    private int max_calculate_col = -1;


    public ColumnAdjustPreparer(JTable table) {
        this.table = table;
    }
    public void clear() {
        max_calculate_row = -1;
        max_calculate_col = -1;
    }
    public List<AdjustRegion> getAdjustRegions() {
        Rectangle visibleRect = table.getVisibleRect();
        int x = (int) visibleRect.getX();
        int width = (int) visibleRect.getWidth();
        int y = (int) visibleRect.getY();
        int height = (int) visibleRect.getHeight();

        Point p0 = new Point(x, y);
        Point p1 = new Point(x + width, y + height);
        int firstRow = nonNegative(table.rowAtPoint(p0));
        int lastRow = nonNegative(table.rowAtPoint(p1));
//        firstCol = table.columnAtPoint(p0);
//        lastCol = table.columnAtPoint(p1);
        int firstCol = nonNegative(table.getColumnModel().getColumnIndexAtX(x));
        int lastCol = table.getColumnModel().getColumnIndexAtX(x + width);
        if (lastCol < 0) {
            lastCol = table.getColumnCount() - 1;
        }
        if (0 == firstRow && 0 == lastRow) {
            firstCol = 0;
            lastCol = table.getColumnCount() - 1;
        }

        return calNewRegions(firstRow,lastRow,firstCol,lastCol);
    }
    private List<AdjustRegion> calNewRegions(int firstRow,int lastRow, int firstCol, int lastCol) {
        List<AdjustRegion> rtn = new ArrayList<>();
        if ( max_calculate_row < 0) {
            AdjustRegion region = new AdjustRegion(firstRow,lastRow,firstCol,lastCol);
            rtn.add(region);
            max_calculate_row = lastRow;
            max_calculate_col = lastCol;
        } else if (lastCol > max_calculate_col ){
            int regionLastRow = Math.max(lastRow,max_calculate_row);
            AdjustRegion region1 = new AdjustRegion(firstRow,regionLastRow,max_calculate_col,lastCol);
            rtn.add(region1);
            if ( lastRow > max_calculate_row) {
                AdjustRegion region2 = new AdjustRegion(max_calculate_row,lastRow,firstCol,max_calculate_col);
                rtn.add(region2);
                max_calculate_row = lastRow;
            }
            max_calculate_col = lastCol;
        } else if ( lastRow > max_calculate_row) {
            AdjustRegion region1 = new AdjustRegion(max_calculate_row,lastRow,firstCol,lastCol);
            rtn.add(region1);
            max_calculate_row = lastRow;
        }

        return rtn;
    }
    private static int nonNegative(int number) {
        return Math.max(0, number);
    }
///////////////////////////////////////////////////////////////////////////////
    public static class AdjustRegion {
        public final int firstRow;
        public final int lastRow;
        public final int firstColumn;
        public final int lastColumn;

        public AdjustRegion(int firstRow,int lastRow,int firstColumn,int lastColumn) {
            this.firstRow = firstRow;
            this.lastRow = lastRow;
            this.firstColumn = firstColumn;
            this.lastColumn = lastColumn;
        }
        @Override
        public String toString() {
            return firstRow+"-"+lastRow+" rows, "+firstColumn+"-"+lastColumn+" columns ";
        }
    }
}