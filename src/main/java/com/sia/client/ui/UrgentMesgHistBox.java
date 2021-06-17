package com.sia.client.ui;

import com.jidesoft.combobox.TableComboBox;
import com.sia.client.model.AlertStruct;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;

public class UrgentMesgHistBox extends TableComboBox {

    private final DefaultTableModel tableModel;
    private final static AlertStruct promptSecection = new AlertStruct("", "RECENT ALERTS");
    private final static int mesgColumnIndex = 0;
    private final static int mesgColWidth = 400;
    private final static int timeColWidth = 80;
    private final static int tableWidthPadding = 5;
    private final static int tableHeightPadding = 5;
    private final static int mesgTableMaxHeight = 500;
    private final static Color tblAltColor1 = new Color(250,250,250);
    private final static Color tblAltColor2 = new Color(240,240,240);


    public UrgentMesgHistBox(DefaultTableModel tm) {
        super(tm,AlertStruct.class,TableComboBox.DROPDOWN);
        tableModel = tm;
        setTableModel(tableModel);
        tableModel.setColumnCount(promptSecection.convert().length);
        configEditor();
        setBorder(BorderFactory.createEmptyBorder());
        insertItemAt(promptSecection.convert(),0);
        super.setSelectedItem(promptSecection.getMesg());
//        testData();
    }
    private void testData() {
        pushItem(new AlertStruct("20hr 19min","<html>DEMO Message1<br>A long line A long line A long line A long line A long line A long line A long line A long line A long line A long line<br>short line2<br>short line3<br>LAST short line 4</html>"));
        pushItem(new AlertStruct("21hr 19min","<html>DEMO Message 2<br>short line1</html>"));
    }
    public void pushItem(AlertStruct item) {
        insertItemAt(item.convert(),1);
    }
    public void insertItemAt(Object [] rowData,int rowModelIndex) {
        tableModel.insertRow(rowModelIndex, rowData);
        setSelectedIndex(rowModelIndex);
    }
    @Override
    public void setSelectedIndex(int index) {
        //don't change selection ( editor should show prompt only)
        super.setSelectedIndex(getMostRecentMsgIndex());
    }
    @Override
    public void setSelectedItem(Object item) {
       //always shows most recent message.
        super.setSelectedItem(tableModel.getValueAt(getMostRecentMsgIndex(),mesgColumnIndex));

    }
    private int getMostRecentMsgIndex() {
        return tableModel.getRowCount()>1?1:0;
    }
    @Override
    protected JTable createTable(TableModel tm) {
        return new popUpTable(tm);
    }
    private void configEditor() {
        setEditable(false);
//        EditorComponent editorComp = (EditorComponent)_editor.getEditorComponent();
//        editorComp.setOpaque(false);
    }
    private static Color getBackgroundColor(int rowViewIndex) {
        if ( 0 == rowViewIndex%2 ) {
            return tblAltColor1;
        } else {
            return tblAltColor2;
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////
    private static class popUpTable extends JTable {
        public popUpTable(TableModel tm) {
            super(tm);
        }
        @Override
        public Object getValueAt(int rowViewIndex, int colViewIndex) {
            int rowModelIndex = rowViewIndex + 1;
            return getModel().getValueAt(rowModelIndex,colViewIndex);
        }
        @Override
        public int getRowCount() {
            return getModel().getRowCount()-1;
        }
        @Override
        public void createDefaultColumnsFromModel() {

            TableModel m = getModel();
            if (m != null) {

                TableColumnModel cm = getColumnModel();
                while (cm.getColumnCount() > 0) {
                    cm.removeColumn(cm.getColumn(0));
                }
                TableColumn mesgCol = new TableColumn(mesgColumnIndex,mesgColWidth);
                mesgCol.setHeaderValue("Message");
                TableColumn timeCol = new TableColumn(mesgColumnIndex+1,timeColWidth);
                timeCol.setHeaderValue("Time");
                mesgCol.setCellRenderer(new MessageCellRenderer());
                timeCol.setCellRenderer(new TimeCellRenderer());
                cm.addColumn(mesgCol);
                cm.addColumn(timeCol);
            }
        }
        @Override
        public void paint(Graphics g) {
            configRowHeight();
            super.paint(g);
        }
        private void configRowHeight() {
            int tablePrefHeight = 0;
            for(int rowViewIndex=0;rowViewIndex<getRowCount();rowViewIndex++) {
                TableCellRenderer renderer = getCellRenderer(rowViewIndex,mesgColumnIndex);
                Component c = prepareRenderer(renderer, rowViewIndex, mesgColumnIndex);
                int rowHeight = c.getPreferredSize().height;
                this.setRowHeight(rowViewIndex,rowHeight);
                tablePrefHeight += rowHeight;
            }

            JScrollPane scrollPane = null;
            Component me = this;
            while ( null != me.getParent() && me != me.getParent()) {
                me = me.getParent();
                if ( me instanceof JScrollPane) {
                    scrollPane = (JScrollPane)me;
                    break;
                }
            }
            if ( null != scrollPane) {
                int topContainerPrefHeight = Math.min(mesgTableMaxHeight,tablePrefHeight + getHeaderHeight()+tableHeightPadding);
                Dimension containerPrefDim = new Dimension(mesgColWidth + timeColWidth + tableWidthPadding,topContainerPrefHeight );
                Container topContainer = scrollPane.getParent().getParent();
                topContainer.setPreferredSize(containerPrefDim);
            }
        }
        private int getHeaderHeight() {
            TableColumn tc = getColumnModel().getColumn(0);
            Object value = tc.getHeaderValue();
            TableCellRenderer renderer = tc.getHeaderRenderer();
            if (renderer == null) {
                renderer = getTableHeader().getDefaultRenderer();
            }
            Component c = renderer.getTableCellRendererComponent(this, value, false, false, -1, 0);
            return c.getPreferredSize().height;
        }

    }
    private static class MessageCellRenderer implements TableCellRenderer {

        private final JEditorPane editorPane;
        private final JScrollPane scrollPane;
        public MessageCellRenderer() {
            HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
            editorPane = new JEditorPane();
            editorPane.setEditorKit(htmlEditorKit);
            editorPane.setEditable(false);
            editorPane.setAutoscrolls(true);
            scrollPane = new JScrollPane(editorPane);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
        }
        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
            editorPane.setText((String)value);
            editorPane.setBackground(getBackgroundColor(row));
            return scrollPane;
        }
    }
    private static class TimeCellRenderer implements TableCellRenderer {

        private final JLabel timeLabel;
        public TimeCellRenderer() {
            timeLabel = new JLabel();
            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            timeLabel.setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
            timeLabel.setText((String)value);
            timeLabel.setBackground(getBackgroundColor(row));
            return timeLabel;
        }
    }
}
