package com.sia.client.ui;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;

import static com.sia.client.config.Utils.log;

public class LineRenderer implements TableCellRenderer {

    private final LinePanel teamLinePanel;
    private final LinePanel spreadTotalPanel;
    private final LinePanel gameNumbersPanel;
    private final LinePanel soccerNumbersPanel;
    private final LinePanel soccerLinePanel;
    private final LinePanel timePanel;
    private final LinePanel chartPanel;
    private final LinePanel soccerChartPanel;
    private final LinePanel infoPanel;
    private final LinePanel headerPanel;
    private final String name;

    public LineRenderer() {
        this(null);
    }

    public LineRenderer(String name) {
        this.name = name;
        teamLinePanel = createTeamLinePanel(name);
        spreadTotalPanel = createSpreadTotalPanel(name);
        gameNumbersPanel = createGameNumbersPanel(name);
        soccerNumbersPanel = createSoccerNumbersPanel(name);
        soccerLinePanel = createSoccerLinePanel(name);
        timePanel = createTimePanel(name);
        chartPanel = createChartPanel(name);
        soccerChartPanel = createSoccerChartPanel(name);
        infoPanel = createInfoPanel(name);
        headerPanel = createHeaderPanel(name);
    }
    private LinePanel createSoccerLinePanel(String name) {
        LinePanel lp = new LinePanel(name);
        lp.top.setIconTextGap(1);
        lp.bottom.setIconTextGap(1);
        lp.draw.setIconTextGap(1);
        lp.total.setIconTextGap(1);
        lp.top.setHorizontalAlignment(SwingConstants.RIGHT);
        lp.bottom.setHorizontalAlignment(SwingConstants.RIGHT);
        lp.draw.setHorizontalAlignment(SwingConstants.RIGHT);
        lp.total.setHorizontalAlignment(SwingConstants.RIGHT);

        return lp;
    }
    private LinePanel createHeaderPanel(String name) {
        LinePanel lp = new LinePanel(name);
        lp.top.setHorizontalAlignment(SwingConstants.LEFT);
        lp.bottom.setHorizontalAlignment(SwingConstants.LEFT);

        return lp;
    }
    private LinePanel createInfoPanel(String name) {
        LinePanel lp = new LinePanel(name);
        lp.top.setHorizontalAlignment(SwingConstants.LEFT);
        lp.top.setBorder(BorderFactory.createMatteBorder(-1, -1, 1, -1, Color.BLACK));
        lp.bottom.setHorizontalAlignment(SwingConstants.LEFT);

        return lp;
    }
    private LinePanel createSoccerChartPanel(String name) {
        LinePanel lp = new LinePanel(name);
        lp.top.setHorizontalAlignment(SwingConstants.CENTER);
        lp.bottom.setHorizontalAlignment(SwingConstants.CENTER);
        lp.draw.setHorizontalAlignment(SwingConstants.CENTER);
        lp.total.setHorizontalAlignment(SwingConstants.CENTER);

        return lp;
    }
    private LinePanel createChartPanel(String name) {
        LinePanel lp = new LinePanel(name);
        lp.top.setHorizontalAlignment(SwingConstants.CENTER);
        lp.bottom.setHorizontalAlignment(SwingConstants.CENTER);

        return lp;
    }
    private LinePanel createTimePanel(String name) {
        LinePanel lp = new LinePanel(name);
        lp.top.setHorizontalAlignment(SwingConstants.CENTER);
        lp.bottom.setHorizontalAlignment(SwingConstants.CENTER);
        return lp;
    }
    private LinePanel createSoccerNumbersPanel(String name) {
        LinePanel lp = new LinePanel(name);
        lp.top.setHorizontalAlignment(SwingConstants.CENTER);
        lp.bottom.setHorizontalAlignment(SwingConstants.CENTER);
        return lp;
    }
    private LinePanel createGameNumbersPanel(String name) {
        LinePanel lp = new LinePanel(name);
        lp.top.setHorizontalAlignment(SwingConstants.CENTER);
        lp.bottom.setHorizontalAlignment(SwingConstants.CENTER);
        return lp;
    }
    private LinePanel createSpreadTotalPanel(String name) {
        LinePanel lp = new LinePanel(name);
        lp.top.setIconTextGap(1);
        lp.bottom.setIconTextGap(1);
        lp.top.setHorizontalAlignment(SwingConstants.RIGHT);
        lp.bottom.setHorizontalAlignment(SwingConstants.RIGHT);
        return lp;
    }
    private LinePanel createTeamLinePanel(String name) {
        LinePanel lp = new LinePanel(name);
        lp.top.setHorizontalAlignment(SwingConstants.LEFT);
        lp.bottom.setHorizontalAlignment(SwingConstants.LEFT);

        lp.top.setHorizontalTextPosition(SwingConstants.RIGHT);
        lp.bottom.setHorizontalTextPosition(SwingConstants.RIGHT);

        lp.draw.setHorizontalAlignment(SwingConstants.LEFT);
        lp.total.setHorizontalAlignment(SwingConstants.LEFT);
        lp.draw.setHorizontalTextPosition(SwingConstants.RIGHT);
        lp.total.setHorizontalTextPosition(SwingConstants.RIGHT);
        return lp;
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
        }
        LinePanel rtn;
        if (value instanceof SpreadTotalView) {
            spreadTotalPanel.setLines(table,(SpreadTotalView) value, row, column);
            rtn = spreadTotalPanel;
        } else if (value instanceof SoccerSpreadTotalView) {
            soccerLinePanel.setSoccerLines(table,(SoccerSpreadTotalView) value, row, column);
            rtn = soccerLinePanel;
        } else if (value instanceof TeamView) {
            teamLinePanel.setTeams(table,(TeamView) value, row, column);
            rtn = teamLinePanel;
        } else if (value instanceof GameNumberView) {
            gameNumbersPanel.setGamenumbers(table,(GameNumberView) value, row, column);
            rtn = gameNumbersPanel;
        } else if (value instanceof SoccerGameNumberView) {
            soccerNumbersPanel.setSoccerGamenumbers(table,(SoccerGameNumberView) value, row, column);
            rtn = soccerNumbersPanel;
        } else if (value instanceof TimeView) {
            timePanel.setTime(table,(TimeView) value, row, column);
            rtn = timePanel;
        } else if (value instanceof ChartView) {
            chartPanel.setChart(table,(ChartView) value, row, column);
            rtn = chartPanel;
        } else if (value instanceof SoccerChartView) {
            soccerChartPanel.setSoccerChart(table,(SoccerChartView) value, row, column);
            rtn = soccerChartPanel;
        } else if (value instanceof InfoView) {
            infoPanel.setInfo(table,(InfoView) value, row, column);
            rtn = infoPanel;
        } else if (value instanceof HeaderView) {
            headerPanel.setHeader(table,(HeaderView) value, row, column);
            rtn = headerPanel;
        } else {
            log(new IllegalStateException("Illegal value:"+value));
            rtn = createTeamLinePanel(name);
        }
        return rtn;
    }
}