package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.GameGroupHeader;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static com.sia.client.config.Utils.log;

public class LineRenderer implements TableCellRenderer {

    private final LinePanel teamLinePanel = new LinePanel();
    private final LinePanel spreadTotalPanel = new LinePanel();
    private final LinePanel gameNumbersPanel = new LinePanel();
    private final LinePanel soccerNumbersPanel = new LinePanel();
    private final LinePanel soccerLinePanel = new LinePanel();
    private final LinePanel timePanel = new LinePanel();
    private final LinePanel chartPanel = new LinePanel();
    private final LinePanel soccerChartPanel = new LinePanel();
    private final LinePanel infoPanel = new LinePanel();
    private final LinePanel headerPanel = new LinePanel();
    private final JLabel headerCellRenderComp = new JLabel();

    public static LineRenderer instance() {
        return LazyInitHolder.instance;
    }
    public static LineRenderer soccerInstance() {
        return LazyInitHolder.soccerInstance;
    }
    private LineRenderer() {
        initTeamLinePanel();
        initSpreadTotalPanel();
        initGameNumbersPanel();
        initSoccerNumbersPanel();
        initSoccerLinePanel();
        initTimePanel();
        initChartPanel();
        initSoccerChartPanel();
        initInfoPanel();
        initHeaderPanel();
        initHeaderCellRenderComp();
    }
    private LineRenderer(String name) {
        this();
        setName(name);
    }
    private void setName(String name) {
        teamLinePanel.setName(name);
        spreadTotalPanel.setName(name);
        gameNumbersPanel.setName(name);
        soccerNumbersPanel.setName(name);
        soccerLinePanel.setName(name);
        timePanel.setName(name);
        chartPanel.setName(name);
        soccerChartPanel.setName(name);
        infoPanel.setName(name);
        headerPanel.setName(name);
        headerCellRenderComp.setName(name);
    }
    private void initSoccerLinePanel() {
        soccerLinePanel.top.setIconTextGap(1);
        soccerLinePanel.bottom.setIconTextGap(1);
        soccerLinePanel.draw.setIconTextGap(1);
        soccerLinePanel.total.setIconTextGap(1);
        soccerLinePanel.top.setHorizontalAlignment(SwingConstants.RIGHT);
        soccerLinePanel.bottom.setHorizontalAlignment(SwingConstants.RIGHT);
        soccerLinePanel.draw.setHorizontalAlignment(SwingConstants.RIGHT);
        soccerLinePanel.total.setHorizontalAlignment(SwingConstants.RIGHT);
    }
    private void initHeaderCellRenderComp() {
        headerCellRenderComp.setOpaque(true);
        headerCellRenderComp.setBackground(SiaConst.DefaultHeaderColor);
        headerCellRenderComp.setBorder(BorderFactory.createEmptyBorder());
    }
    private void initHeaderPanel() {
        headerPanel.top.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.bottom.setHorizontalAlignment(SwingConstants.LEFT);
    }
    private void initInfoPanel() {
        infoPanel.top.setHorizontalAlignment(SwingConstants.LEFT);
        infoPanel.topPanel.setBorder(BorderFactory.createMatteBorder(-1, -1, 1, -1, Color.BLACK));
        infoPanel.bottom.setHorizontalAlignment(SwingConstants.LEFT);
    }
    private void initSoccerChartPanel() {
        soccerChartPanel.top.setHorizontalAlignment(SwingConstants.CENTER);
        soccerChartPanel.bottom.setHorizontalAlignment(SwingConstants.CENTER);
        soccerChartPanel.draw.setHorizontalAlignment(SwingConstants.CENTER);
        soccerChartPanel.total.setHorizontalAlignment(SwingConstants.CENTER);
    }
    private void initChartPanel() {
        chartPanel.top.setHorizontalAlignment(SwingConstants.CENTER);
        chartPanel.bottom.setHorizontalAlignment(SwingConstants.CENTER);
    }
    private void initTimePanel() {
        timePanel.top.setHorizontalAlignment(SwingConstants.CENTER);
        timePanel.bottom.setHorizontalAlignment(SwingConstants.CENTER);
    }
    private void initSoccerNumbersPanel() {
        soccerNumbersPanel.top.setHorizontalAlignment(SwingConstants.CENTER);
        soccerNumbersPanel.bottom.setHorizontalAlignment(SwingConstants.CENTER);
        soccerNumbersPanel.draw.setHorizontalAlignment(SwingConstants.CENTER);
        soccerNumbersPanel.total.setHorizontalAlignment(SwingConstants.CENTER);
    }
    private void initGameNumbersPanel() {
        gameNumbersPanel.top.setHorizontalAlignment(SwingConstants.CENTER);
        gameNumbersPanel.bottom.setHorizontalAlignment(SwingConstants.CENTER);
    }
    private void initSpreadTotalPanel() {
        spreadTotalPanel.top.setIconTextGap(1);
        spreadTotalPanel.bottom.setIconTextGap(1);
        spreadTotalPanel.top.setHorizontalAlignment(SwingConstants.RIGHT);
        spreadTotalPanel.bottom.setHorizontalAlignment(SwingConstants.RIGHT);
    }
    private void initTeamLinePanel() {
        teamLinePanel.top.setHorizontalAlignment(SwingConstants.LEFT);
        teamLinePanel.bottom.setHorizontalAlignment(SwingConstants.LEFT);

        teamLinePanel.top.setHorizontalTextPosition(SwingConstants.RIGHT);
        teamLinePanel.bottom.setHorizontalTextPosition(SwingConstants.RIGHT);

        teamLinePanel.draw.setHorizontalAlignment(SwingConstants.LEFT);
        teamLinePanel.total.setHorizontalAlignment(SwingConstants.LEFT);
        teamLinePanel.draw.setHorizontalTextPosition(SwingConstants.RIGHT);
        teamLinePanel.total.setHorizontalTextPosition(SwingConstants.RIGHT);
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
        JComponent rtn;
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
        } else if (value instanceof GameGroupHeader) {
            rtn = headerCellRenderComp;
        }  else {
            log(new IllegalStateException("Illegal value:"+value));
            rtn = infoPanel;
        }
        return rtn;
    }
    private static class LazyInitHolder {
        private static final LineRenderer instance = new LineRenderer();
        private static final LineRenderer soccerInstance = new LineRenderer(SiaConst.SportName.Soccer);
    }
}