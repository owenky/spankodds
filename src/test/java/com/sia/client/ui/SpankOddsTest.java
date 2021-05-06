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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.Vector;

public class SpankOddsTest {

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
        HListener listener = new HListener(jtable);
        jtable.addHierarchyListener(listener);
        jtable.getColumnModel().addColumnModelListener(listener);


        jFrame.getContentPane().add(new JScrollPane(jtable));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jtable.getParent().addComponentListener(listener);

        int columnCount=100;
        Vector<String> colIden = new Vector<>();
        for(int i=0;i<columnCount;i++) {
            colIden.add("col"+i);
        }

        Vector<Vector<Integer>> dataVector = new Vector<>();
        for(int i=1;i<100;i++) {
            dataVector.add(makeRow(i,columnCount));
        }

        ((DefaultTableModel)jtable.getModel()).setDataVector(dataVector, colIden);

        jFrame.setSize(new Dimension(100,100));
        jFrame.pack();
        jFrame.show();

    }
    private static Vector<Integer> makeRow(int seed,int colCount) {
        Vector<Integer> row = new Vector<>();
        for(int i=0;i<colCount;i++) {
            row.add(seed+i);
        }

        return row;
    }
 ////////////////////////////////////////////////////////////////////////////////
   private static class HListener implements HierarchyListener, TableColumnModelListener, ComponentListener {

        private final JTable mainGameTable;
        private boolean isMainTableFirstShown;
        JPanel title = new JPanel();
        public HListener(JTable mainGameTable) {
            this.mainGameTable = mainGameTable;

            title.setBackground(Color.BLACK);
            JLabel titleLabel = new JLabel("TEST");
            titleLabel.setOpaque(false);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            titleLabel.setForeground(Color.WHITE);
            title.add(BorderLayout.CENTER, titleLabel);
            mainGameTable.add(title);
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
             Rectangle r1 = mainGameTable.getCellRect(10, 0, true);
             Rectangle r2 = mainGameTable.getCellRect(10, mainGameTable.getColumnCount()-1, true);
             JComponent tableParent = (JComponent)mainGameTable.getParent();
             int x1 = 0;
             int y1 = (int)r1.getY();
             int tableWidth = mainGameTable.getWidth();
             int tableParentWidth = tableParent.getWidth();
             int width = Math.min(tableWidth, tableParentWidth);

             int height = (int)r2.getHeight();
             title.setBounds(x1, y1,width,height);
             System.out.println("x1="+x1+", y1="+y1+" width="+width+", height="+height);
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
 }
}
