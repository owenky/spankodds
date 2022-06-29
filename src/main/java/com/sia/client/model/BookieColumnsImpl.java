package com.sia.client.model;

import com.sia.client.config.ColumnSettings;
import com.sia.client.config.Config;
import com.sia.client.config.Utils;
import com.sia.client.ui.AppController;
import com.sia.client.ui.LineRenderer;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookieColumnsImpl implements BookieColumnModel{

    private List<Bookie> allBookiesVec;
    private List<Bookie> hiddenBookies;
    private List<Bookie> shownBookies;
    private List<TableColumn> shownColumns;
    private final AtomicBoolean initStatus = new AtomicBoolean(false);

    public static BookieColumnsImpl instance() {
        return LazyInitHolder.instance;
    }
    private BookieColumnsImpl() {
    }
    public void reset() {
        initStatus.set(false);
    }
    private void init() {
        allBookiesVec = AppController.getBookiesVec();
        hiddenBookies = AppController.getHiddenCols();
        shownColumns = new ArrayList<>(allBookiesVec.size()-hiddenBookies.size());
        shownBookies = new ArrayList<>(allBookiesVec.size()-hiddenBookies.size());
        for (Bookie b : allBookiesVec) {
            if (hiddenBookies.contains(b)) {
                continue;
            }
            shownBookies.add(b);
        }
        shownColumns = null;
        initStatus.set(true);
    }
    @Override
    public synchronized int size() {
        if ( initStatus.compareAndSet(false,true)) {
            init();
        }
        return shownBookies.size();
    }
    @Override
    public synchronized TableColumn get(int index) {
        if ( initStatus.compareAndSet(false,true)) {
            init();
        }
        return getShownColumns().get(index);
    }
    private List<TableColumn> getShownColumns() {
        if ( null == shownColumns) {
            shownColumns = createAllColumns();
        }
        return shownColumns;
    }
    private List<TableColumn> createAllColumns() {
        List<TableColumn> result = new ArrayList<>(shownBookies.size());

        for (int k = 0; k < shownBookies.size(); k++) {
            Bookie b = shownBookies.get(k);
            TableColumn column;

            column = new TableColumn(k, 30, null, null) {
                @Override
                public TableCellRenderer getCellRenderer() {
                    //deferred construction of TableCellRenderer to avoid EDT vialation when building model in worker thread -- 2021-12-12
                    if (null == cellRenderer) {
                        cellRenderer = LineRenderer.instance();
                    }
                    return cellRenderer;
                }
            };

            column.setHeaderValue(b.getShortname());
            column.setIdentifier(b.getBookie_id());
            ColumnSettings columnSettings = Config.instance().getColumnSettings();
            column.setPreferredWidth(columnSettings.getColumnWidth(column.getHeaderValue()));

            TableCellEditor tableCellEditor;
            Class<? extends TableCellEditor> editorClass = BookieManager.getTableCellEditor(b.getBookie_id());
            if ( null != editorClass) {
                try {
                    tableCellEditor = editorClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    Utils.log(e);
                    tableCellEditor = null;
                }
            } else {
                tableCellEditor = null;
            }
            column.setCellEditor(tableCellEditor);
            result.add(column);

        }

        return result;
    }
    ////////////////////////////////////////////////////////////////////////////
    private static abstract class LazyInitHolder {
        private static final BookieColumnsImpl instance = new BookieColumnsImpl();
    }
}
