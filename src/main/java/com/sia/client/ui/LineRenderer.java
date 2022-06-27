package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.GameGroupHeader;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static com.sia.client.config.Utils.log;

public class LineRenderer implements TableCellRenderer {

    private final LinePanel teamLinePanel;
    private final LinePanel spreadTotalPanel;
    private final LinePanel gameNumbersPanel;
    private final LinePanel timePanel;
    private final LinePanel chartPanel;
    private final LinePanel infoPanel;
    private final LinePanel info2Panel;
    private final LinePanel headerPanel;
    private final JLabel headerCellRenderComp = new JLabel();
    private final boolean toIncludeDrawAndTotal;

    public static LineRenderer instance() {
        return LazyInitHolder.instance;
    }
    public static LineRenderer soccerInstance() {
        return LazyInitHolder.soccerInstance;
    }
    private LineRenderer(boolean toIncludeDrawAndTotal) {
        this.toIncludeDrawAndTotal = toIncludeDrawAndTotal;
        teamLinePanel = LinePanel.instance(toIncludeDrawAndTotal);
        spreadTotalPanel = LinePanel.instance(toIncludeDrawAndTotal);
        gameNumbersPanel = LinePanel.instance(toIncludeDrawAndTotal);
        timePanel = LinePanel.instance(toIncludeDrawAndTotal);
        chartPanel = LinePanel.instance(toIncludeDrawAndTotal);
        infoPanel = LinePanel.instance(toIncludeDrawAndTotal);
        info2Panel = LinePanel.instance(toIncludeDrawAndTotal);
        headerPanel = LinePanel.instance(toIncludeDrawAndTotal);

        initTeamLinePanel();
        initSpreadTotalPanel();
        initGameNumbersPanel();
        initTimePanel();
        initChartPanel();
        initInfoPanel();
        initInfo2Panel();
        initHeaderPanel();
        initHeaderCellRenderComp();
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
    private void initInfo2Panel() {
        info2Panel.top.setHorizontalAlignment(SwingConstants.LEFT);
        info2Panel.topPanel.setBorder(BorderFactory.createMatteBorder(-1, -1, 1, -1, Color.BLACK));
        info2Panel.bottom.setHorizontalAlignment(SwingConstants.LEFT);
    }
    private void initChartPanel() {
        if ( toIncludeDrawAndTotal ) {
            chartPanel.top.setHorizontalAlignment(SwingConstants.CENTER);
            chartPanel.bottom.setHorizontalAlignment(SwingConstants.CENTER);
            chartPanel.draw.setHorizontalAlignment(SwingConstants.CENTER);
            chartPanel.total.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            chartPanel.top.setHorizontalAlignment(SwingConstants.CENTER);
            chartPanel.bottom.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
    private void initTimePanel() {
        timePanel.top.setHorizontalAlignment(SwingConstants.CENTER);
        timePanel.bottom.setHorizontalAlignment(SwingConstants.CENTER);
    }
    private void initGameNumbersPanel() {
        if ( toIncludeDrawAndTotal ) {
            gameNumbersPanel.top.setHorizontalAlignment(SwingConstants.CENTER);
            gameNumbersPanel.bottom.setHorizontalAlignment(SwingConstants.CENTER);
            gameNumbersPanel.draw.setHorizontalAlignment(SwingConstants.CENTER);
            gameNumbersPanel.total.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            gameNumbersPanel.top.setHorizontalAlignment(SwingConstants.CENTER);
            gameNumbersPanel.bottom.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
    private void initSpreadTotalPanel() {
        if ( toIncludeDrawAndTotal) {
            spreadTotalPanel.top.setIconTextGap(1);
            spreadTotalPanel.bottom.setIconTextGap(1);
            spreadTotalPanel.draw.setIconTextGap(1);
            spreadTotalPanel.total.setIconTextGap(1);
            spreadTotalPanel.top.setHorizontalAlignment(SwingConstants.RIGHT);
            spreadTotalPanel.bottom.setHorizontalAlignment(SwingConstants.RIGHT);
            spreadTotalPanel.draw.setHorizontalAlignment(SwingConstants.RIGHT);
            spreadTotalPanel.total.setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            spreadTotalPanel.top.setIconTextGap(1);
            spreadTotalPanel.bottom.setIconTextGap(1);
            spreadTotalPanel.top.setHorizontalAlignment(SwingConstants.RIGHT);
            spreadTotalPanel.bottom.setHorizontalAlignment(SwingConstants.RIGHT);
        }
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
            spreadTotalPanel.setSoccerLines(table,(SoccerSpreadTotalView) value, row, column);
            rtn = spreadTotalPanel;
        } else if (value instanceof TeamView) {
            teamLinePanel.setTeams(table,(TeamView) value, row, column);
            rtn = teamLinePanel;
        } else if (value instanceof GameNumberView) {
            gameNumbersPanel.setGamenumbers(table,(GameNumberView) value, row, column);
            rtn = gameNumbersPanel;
        } else if (value instanceof SoccerGameNumberView) {
            gameNumbersPanel.setSoccerGamenumbers(table,(SoccerGameNumberView) value, row, column);
            rtn = gameNumbersPanel;
        } else if (value instanceof TimeView) {
            timePanel.setTime(table,(TimeView) value, row, column);
            rtn = timePanel;
        } else if (value instanceof ChartView) {
            chartPanel.setChart(table,(ChartView) value, row, column);
            rtn = chartPanel;
        } else if (value instanceof SoccerChartView) {
            chartPanel.setSoccerChart(table,(SoccerChartView) value, row, column);
            rtn = chartPanel;
        } else if (value instanceof InfoView) {
            infoPanel.setInfo(table,(InfoView) value, row, column);
            rtn = infoPanel;
        }else if (value instanceof InfoView2) {
            info2Panel.setInfo2(table,(InfoView2) value, row, column);
            rtn = info2Panel;
        }
        else if (value instanceof HeaderView) {
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
        private static final LineRenderer instance = new LineRenderer(false);
        private static final LineRenderer soccerInstance = new LineRenderer(true);
    }
}