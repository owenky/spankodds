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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;

public class UrgentMesgHistBox extends TableComboBox {

    private final DefaultTableModel tableModel;
    private final static AlertStruct promptSecection = new AlertStruct("", "RECENT ALERTS");
    private final static int mesgColumnIndex = 0;
    private final static int mesgColWidth = 400;
    private final static int timeColWidth = 60;
    private final static int tableWidthPadding = 5;
    private final static int tableHeightPadding = 5;

    public UrgentMesgHistBox(DefaultTableModel tm) {
        super(tm,AlertStruct.class,TableComboBox.DROPDOWN);
        tableModel = tm;
        setTableModel(tableModel);
        tableModel.setColumnCount(promptSecection.convert().length);
        configEditor();
        setBorder(BorderFactory.createEmptyBorder());
        insertItemAt(promptSecection.convert(),0);
        super.setSelectedItem(promptSecection.getMesg());
        testData();
    }
    private void testData() {
        pushItem(new AlertStruct("20:19","<html>DEMO Message1<br>A long line A long line A long line A long line A long line A long line A long line A long line A long line A long line<br>short line2<br>short line3<br>LAST short line 4</html>"));
        pushItem(new AlertStruct("21:19","<html>DEMO Message 2<br>short line1</html>"));
    }
    public void pushItem(AlertStruct item) {
        insertItemAt(item.convert(),1);
    }
    public void insertItemAt(Object [] rowData,int rowModelIndex) {
        tableModel.insertRow(rowModelIndex, rowData);
    }
    @Override
    public void setSelectedIndex(int index) {
        //don't change selection ( editor should show prompt only)
        super.setSelectedIndex(0);
    }
    @Override
    public void setSelectedItem(Object item) {
       //don't change selection ( editor should show prompt only)
        super.setSelectedItem(promptSecection.getMesg());

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
                Dimension containerPrefDim = new Dimension(mesgColWidth + timeColWidth + tableWidthPadding, tablePrefHeight + getHeaderHeight()+tableHeightPadding);
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
            return scrollPane;
        }
    }
    private static class TimeCellRenderer implements TableCellRenderer {

        private final JLabel timeLabel;
        public TimeCellRenderer() {
            timeLabel = new JLabel();
            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
            timeLabel.setText((String)value);
            return timeLabel;
        }
    }
}
