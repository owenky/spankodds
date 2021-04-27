package com.sia.client.ui;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class LineRenderer implements TableCellRenderer {

    private LinePanel linePanel;
    private String name;
    public LineRenderer() {
        super();
    }

    public LineRenderer(String name) {
        this.name = name;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

/*
        // here value will be infoview,teamview,gmnumview,timeview,or spreadtotalview....
        // when it gets instantiated in LinesTableData it will be set with a border color and size
        // whch will be used as top border here to seperate sports and dates

        if (value == null) {
            if (row == 0 && column == 0) {
                log("value is null" + row + ".." + column);
            }
            return null;
        }

        LinePanel lp = getLinePanel();
        lp.table = table;
        // int rowHeight = table.getRowHeight();
        // System.out.println(this.table);
        //(this.table).setRowHeight(60);
        // System.out.println("table printing---->"+this.table);

        // this col is the view col not the one in the table model

        //table.setRowHeight(30);
        if (value instanceof SpreadTotalView) {
            lp.setLines((SpreadTotalView) value, row, column);
        } else if (value instanceof SoccerSpreadTotalView) {
            lp.setSoccerLines((SoccerSpreadTotalView) value, row, column);

        } else if (value instanceof TeamView) {
            lp.setTeams((TeamView) value, row, column);
        } else if (value instanceof GameNumberView) {
            lp.setGamenumbers((GameNumberView) value, row, column);
        } else if (value instanceof SoccerGameNumberView) {
            lp.setSoccerGamenumbers((SoccerGameNumberView) value, row, column);

        } else if (value instanceof TimeView) {
            lp.setTime((TimeView) value, row, column);
        } else if (value instanceof ChartView) {
            lp.setChart((ChartView) value, row, column);
        } else if (value instanceof SoccerChartView) {
            lp.setSoccerChart((SoccerChartView) value, row, column);
        } else if (value instanceof InfoView) {
            lp.setInfo((InfoView) value, row, column);
        } else if (value instanceof HeaderView) {
            lp.setHeader((HeaderView) value, row, column);
        }

        lp.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        return lp;
        */

        return new JLabel("test-"+table.getName());
    }
    private LinePanel getLinePanel() {
        if ( null == linePanel) {
            linePanel = new LinePanel(name);
        }
        return linePanel;
    }
}