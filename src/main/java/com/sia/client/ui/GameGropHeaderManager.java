package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.MainGameTableModel.BlankGameStruct;
import com.sia.client.ui.TableRowHeaderManager.ColumnHeaderStruct;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;

public class GameGropHeaderManager{

    public static final Color DefaultTitleColor = new Color(0, 0, 128);
    public static final Font DefaultTitleFont = new Font("Verdana", Font.BOLD, 11);
    private final MainGameTable mainGameTable;
    private TableRowHeaderManager tableRowHeaderManager;
    private final Color titleColor;
    private final Font titleFont;
    private final int headerHeight;

    public GameGropHeaderManager(MainGameTable mainGameTable) {
        this(mainGameTable, DefaultTitleColor, DefaultTitleFont, SiaConst.GameGroupHeaderHeight);
    }

    public GameGropHeaderManager(MainGameTable mainGameTable, Color titleColor, Font titleFont, int headerHeight) {
        this.mainGameTable = mainGameTable;
        this.titleColor = titleColor;
        this.titleFont = titleFont;
        this.headerHeight = headerHeight;
    }
    public void installListeners() {
        if ( null == tableRowHeaderManager) {
            tableRowHeaderManager = new TableRowHeaderManager(mainGameTable,titleColor,titleFont,headerHeight);
            tableRowHeaderManager.setColumnHeaderList(getColumnHeaderList());
        }
        tableRowHeaderManager.installListeners();
    }
    private List<ColumnHeaderStruct> getColumnHeaderList() {
        MainGameTableModel model = mainGameTable.getModel();
        List<BlankGameStruct> blankGameIndex = model.getBlankGameIdIndex();
        return blankGameIndex.stream().map(struct-> new ColumnHeaderStruct(struct.linesTableData.getGameGroupHeader(),struct.tableRowModelIndex)).collect(Collectors.toList());
    }
}
