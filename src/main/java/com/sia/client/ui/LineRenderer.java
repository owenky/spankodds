package com.sia.client.ui;

import com.sia.client.config.SiaConst;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

import static com.sia.client.config.Utils.log;

public class LineRenderer extends LinePanel implements TableCellRenderer {

    private static final JLabel BlankCell;
    static {
        BlankCell = new JLabel();
        BlankCell.setOpaque(false);
        BlankCell.setBorder(BorderFactory.createEmptyBorder());
    }
    public LineRenderer() {
    }

    public LineRenderer(String name) {
        super(name);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // here value will be infoview,teamview,gmnumview,timeview,or spreadtotalview....
        // when it gets instantiated in LinesTableData it will be set with a border color and size
        // whch will be used as top border here to seperate sports and dates

        if (value == null) {
            if (row == 0 && column == 0) {
                log("value is null" + row + ".." + column);
            }
            return null;
        } else if (SiaConst.BlankGameId.equals(value)) {
            return BlankCell;
        }

        this.table = (MainGameTable)table;
        if (value instanceof SpreadTotalView) {
            setLines((SpreadTotalView) value, row, column);
        } else if (value instanceof SoccerSpreadTotalView) {
            setSoccerLines((SoccerSpreadTotalView) value, row, column);
        } else if (value instanceof TeamView) {
            setTeams((TeamView) value, row, column);
        } else if (value instanceof GameNumberView) {
            setGamenumbers((GameNumberView) value, row, column);
        } else if (value instanceof SoccerGameNumberView) {

            setSoccerGamenumbers((SoccerGameNumberView) value, row, column);

        } else if (value instanceof TimeView) {
            setTime((TimeView) value, row, column);
        } else if (value instanceof ChartView) {
            setChart((ChartView) value, row, column);
        } else if (value instanceof SoccerChartView) {
            setSoccerChart((SoccerChartView) value, row, column);
        } else if (value instanceof InfoView) {
            setInfo((InfoView) value, row, column);
        } else if (value instanceof HeaderView) {
            setHeader((HeaderView) value, row, column);
        }
        return this;
    }

}