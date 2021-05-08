package com.sia.client.ui;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class SpankOddsTest {

    private static final int [] barRowIndex = new int [] {1,3,5};
    public static void main(String [] argv) {

        JFrame jFrame = new JFrame();
        JTable jtable = new JTable() {
            @Override
            public TableCellRenderer getCellRenderer(int row, int col) {
                TableCellRenderer rtn = super.getCellRenderer(row, col);
                if ( row == 10) {
                    ((JComponent)rtn).setOpaque(true);
                }
                return rtn;
            }
        };
        jtable.setRowHeight(60);

        jFrame.getContentPane().add(new JScrollPane(jtable));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        installListeners(jtable);

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

        jFrame.setSize(new Dimension(100,100));
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
    private static void installListeners(JTable mainGameTable) {
        HListener listener = new HListener(mainGameTable);
        mainGameTable.addHierarchyListener(listener);
        mainGameTable.getColumnModel().addColumnModelListener(listener);
        mainGameTable.getParent().addComponentListener(listener);
        mainGameTable.getModel().addTableModelListener(listener);
    }
 ////////////////////////////////////////////////////////////////////////////////
   private static class HListener implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener {

        private final JTable mainGameTable;
        private boolean isMainTableFirstShown;
        private Map<Integer,JComponent> titleMap = new HashMap<>();


        public HListener(JTable mainGameTable) {
            this.mainGameTable = mainGameTable;
        }
         @Override
         public void hierarchyChanged(final HierarchyEvent e) {
             if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                 Object source = e.getSource();
                 if ( source == mainGameTable && ! isMainTableFirstShown && mainGameTable.isShowing()) {
                     isMainTableFirstShown = true;
                     drawGameLineTitles();
                 }
             }
         }
         private void drawGameLineTitles() {
            for(int row:barRowIndex) {
                GameGropHeaderManager.layOutGameGroupHeader(row, mainGameTable, getTitleComponent(row), 50);
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
     private JComponent getTitleComponent(int rowIndex) {
         return titleMap.computeIfAbsent(rowIndex,row-> {
             JPanel title = new JPanel();
             title.setBackground(Color.BLACK);
             JLabel titleLabel = new JLabel("TEST"+rowIndex);
             titleLabel.setOpaque(false);
             titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
             titleLabel.setForeground(Color.WHITE);
             title.add(BorderLayout.CENTER, titleLabel);
             mainGameTable.add(title);
             return title;
         });
     }
 }
}
