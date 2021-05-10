package com.sia.client.ui;

import com.sia.client.config.SiaConst;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.Vector;

public class SpankOddsTest {

    private static final int [] barRowIndex = new int [] {1,3,5};
    private static final Integer [] lockColumnIndex = new Integer[]{0,1,2};
    public static void main(String [] argv) {

        JFrame jFrame = new JFrame();
        ColumnLockableTable jtable = new ColumnLockableTable(true);
        jtable.setRowHeight(60);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int columnCount=46;
        Vector<String> colIden = new Vector<>();
        for(int i=0;i<columnCount;i++) {
            colIden.add("col"+i);
        }

        Vector<Vector<String>> dataVector = new Vector<>();
        for(int i=0;i<100;i++) {
            dataVector.add(makeRow(i,columnCount));
        }

        ((DefaultTableModel)jtable.getModel()).setDataVector(dataVector, colIden);
        jtable.removeLockedColumnIndex(lockColumnIndex);
        JComponent tableContainer = TableUtils.configTableLockColumns(jtable);
        installListeners(jtable);
        jFrame.getContentPane().add(tableContainer);


        jFrame.setSize(new Dimension(250,100));
        jFrame.pack();
        jFrame.show();
    }
    private static Vector<String> makeRow(int seed,int colCount) {
        Vector<String> row = new Vector<>();
        for(int i=0;i<colCount;i++) {
            row.add(""+seed+"_"+i);
        }

        return row;
    }
    private static void installListeners(ColumnLockableTable mainGameTable) {
        HListener listener = new HListener(mainGameTable);
        mainGameTable.addHierarchyListener(listener);
        mainGameTable.getColumnModel().addColumnModelListener(listener);
        mainGameTable.getParent().addComponentListener(listener);
        mainGameTable.getModel().addTableModelListener(listener);
    }
 ////////////////////////////////////////////////////////////////////////////////
   private static class HListener implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener {

        private final ColumnLockableTable mainTable;
        private boolean isMainTableFirstShown;

        public HListener(ColumnLockableTable mainTable) {
            this.mainTable = mainTable;
        }
         @Override
         public void hierarchyChanged(final HierarchyEvent e) {
             if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                 Object source = e.getSource();
                 if ( source == mainTable && ! isMainTableFirstShown && mainTable.isShowing()) {
                     isMainTableFirstShown = true;
                     drawGameLineTitles();
                 }
             }
         }
         private void drawGameLineTitles() {
            for(int row:barRowIndex) {
                GameGropHeaderManager.layOutGameGroupHeader(row, mainTable,  GameGropHeaderManager
                        .makeGameGroupHeaderComp(mainTable,"TEST"+row,GameGropHeaderManager.DefaultTitleColor,GameGropHeaderManager.DefaultTitleFont), SiaConst.GameGroupHeaderHeight+20);
            }
         }

     @Override
     public void columnAdded(final TableColumnModelEvent e) {

         drawGameLineTitles();
     }

     @Override
     public void columnRemoved(final TableColumnModelEvent e) {
         drawGameLineTitles();
     }

     @Override
     public void columnMoved(final TableColumnModelEvent e) {

     }

     @Override
     public void columnMarginChanged(final ChangeEvent e) {
         drawGameLineTitles();
     }

     @Override
     public void columnSelectionChanged(final ListSelectionEvent e) {

     }

     @Override
     public void componentResized(final ComponentEvent e) {
         drawGameLineTitles();
     }

     @Override
     public void componentMoved(final ComponentEvent e) {

     }

     @Override
     public void componentShown(final ComponentEvent e) {

     }

     @Override
     public void componentHidden(final ComponentEvent e) {

     }
     @Override
     public void tableChanged(final TableModelEvent e) {
         if ( e.getType() == TableModelEvent.INSERT ||  e.getType() == TableModelEvent.DELETE) {
             drawGameLineTitles();
         }
     }
 }
}
