package com.sia.client.ui;

import com.sia.client.config.SiaConst;

import java.awt.Color;
import java.awt.Font;

public class GameGropHeaderManager{

    public static final Color DefaultTitleColor = new Color(0, 0, 128);
    public static final Font DefaultTitleFont = new Font("Verdana", Font.BOLD, 11);
    private final MainGameTable mainGameTable;
    private TableColumnHeaderManager tableColumnHeaderManager;
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
        if ( null == tableColumnHeaderManager) {
            tableColumnHeaderManager = new TableColumnHeaderManager(mainGameTable,mainGameTable.getColumnHeaderProvider());
        }
        tableColumnHeaderManager.installListeners();
    }
}
