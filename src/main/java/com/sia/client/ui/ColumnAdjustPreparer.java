package com.sia.client.ui;

import javax.swing.JTable;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ColumnAdjustPreparer {

    private final JTable table;
    private int max_calculate_row = -1;
    private int min_calculate_row = -1;
    private int max_calculate_col = -1;
    private int min_calculate_col = -1;

    public ColumnAdjustPreparer(JTable table) {
        this.table = table;
    }
    public void clear() {
        max_calculate_row = -1;
        max_calculate_col = -1;
        min_calculate_row = -1;
        min_calculate_col = -1;
    }
    public List<AdjustRegion> getAdjustRegions() {
        Rectangle visibleRect = table.getVisibleRect();
        int x = (int) visibleRect.getX();
        int width = (int) visibleRect.getWidth();
        int y = (int) visibleRect.getY();
        int height = (int) visibleRect.getHeight();

        List<AdjustRegion> rtn = new ArrayList<>();

        Point p0 = new Point(x, y);
        Point p1 = new Point(x + width, y + height);
        int firstRow = table.rowAtPoint(p0);
        int lastRow = table.rowAtPoint(p1);
        int firstCol = table.getColumnModel().getColumnIndexAtX(x);
        int lastCol = table.getColumnModel().getColumnIndexAtX(x + width);

        if ( 0 == width || 0 == height) {
            //adjust header
            AdjustRegion region = new AdjustRegion(-1,-1,0,table.getColumnCount()-1);
            rtn.add(region);
            return rtn;
        }
        if ( lastCol < 0 && 0 < width ) {
            lastCol = table.getColumnCount()-1;
        }

        if ( 0>  max_calculate_row ) {
            AdjustRegion region = new AdjustRegion(firstRow,lastRow,firstCol,lastCol);
            rtn.add(region);
            max_calculate_row = lastRow;
            min_calculate_row = firstRow;
            max_calculate_col = lastCol;
            min_calculate_col = firstCol;
        } else {
            AdjustRegion region = new AdjustRegion(firstRow,min_calculate_row,firstCol,lastCol);
            rtn.add(region);
            region = new AdjustRegion(Math.max(min_calculate_row,firstRow),Math.min(max_calculate_row,lastRow),firstCol,min_calculate_col);
            rtn.add(region);
            region = new AdjustRegion(Math.max(min_calculate_row,firstRow),Math.min(max_calculate_row,lastRow),Math.max(firstCol,max_calculate_col),lastCol);
            rtn.add(region);
            region = new AdjustRegion(Math.max(max_calculate_row,firstRow),lastRow,firstCol,lastCol);
            rtn.add(region);

            rtn.removeIf(r-> r.lastRow < r.firstRow || r.lastColumn < r.firstColumn);
            if ( firstCol ==min_calculate_col && lastCol == max_calculate_col) {
                max_calculate_row = Math.max(max_calculate_row,lastRow);
                min_calculate_row = Math.min(min_calculate_row,firstRow);
            }
            if ( firstRow == min_calculate_row && lastRow == max_calculate_row) {
                max_calculate_col = Math.max(max_calculate_col,lastCol);
                min_calculate_col = Math.min(min_calculate_col,firstCol);
            }
        }
        max_calculate_row = Math.max(max_calculate_row,lastRow);
        max_calculate_col = Math.max(max_calculate_col,lastCol);
        return rtn;
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
