package com.sia.client.ui;

import com.sia.client.config.SiaConst.LayedPaneIndex;
import com.sia.client.config.Utils;
import org.codehaus.plexus.util.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.function.Supplier;

public class RenameColumnPopupMenu {

    private final JTable table;
    private final AnchoredLayeredPane anchoredLayeredPane;
    private static final Dimension inputPanelSize = new Dimension(200,100);
    private static final Dimension inputFieldSize = new Dimension(180,30);
    private static final Insets inputPanelInsets = new Insets(5,5,5,5);
    private static final Insets buttonInsets = new Insets(3,2,3,2);
    private JFormattedTextField inputField;
    private JComponent title;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton cancelButton;
    private int tableColumnIndex;
    private JPanel inputPanel;
    private ActionListener cancelAction;
    private static RenameColumnPopupMenu oldTableColumnPopupMenu=null;

    public static RenameColumnPopupMenu of(JTable table,ActionListener cancelAction) {
        if ( null != oldTableColumnPopupMenu) {
            oldTableColumnPopupMenu.hideMenu();
        }
        oldTableColumnPopupMenu = new RenameColumnPopupMenu(table);
        oldTableColumnPopupMenu.cancelAction = cancelAction;
        return oldTableColumnPopupMenu;
    }
    private RenameColumnPopupMenu(JTable table) {
        this.table = table;
        anchoredLayeredPane = new AnchoredLayeredPane(table, LayedPaneIndex.TableColumnMenuIndex);
    }
    public void showMenu(int tableColumnIndex) {
        this.tableColumnIndex = tableColumnIndex;
        inputPanel = new JPanel() {
            @Override
            public Insets getInsets() {
                return inputPanelInsets;
            }
        };
        inputPanel.setSize(inputPanelSize);
        inputPanel.setBorder(BorderFactory.createEtchedBorder());
        GridLayout layoutMng =  new GridLayout(0,1);
        inputPanel.setLayout(layoutMng);
        inputPanel.add(getTitle());
        inputPanel.add(getInputField());
        inputPanel.add(getButtonPanel());

        anchoredLayeredPane.setUserPane(inputPanel);
        Supplier<Point> anchorPointSupl = ()-> {
            JTableHeader header = table.getTableHeader();
            Point headerAtScreen = header.getLocationOnScreen();
            Rectangle r = header.getHeaderRect(tableColumnIndex);
            return new Point((int)(r.getX()+headerAtScreen.getX()),(int)(r.getHeight()+headerAtScreen.getY()));
        };

        anchoredLayeredPane.openAndAnchoredAt(anchorPointSupl);
    }
    private JComponent getTitle() {
        if ( null == title) {
            title = new JLabel("Enter new name:  ");
            title.setBorder(BorderFactory.createEmptyBorder());
        }
        return title;
    }
    private JFormattedTextField getInputField() {
        if ( null == inputField) {
            inputField = new JFormattedTextField();
            inputField.setPreferredSize(inputFieldSize);
        }
        return inputField;
    }
    private JPanel getButtonPanel() {
        if ( null == buttonPanel) {
            buttonPanel = new JPanel();
            okButton = new JButton("Ok");
            okButton.setMargin(buttonInsets);
            okButton.addActionListener((evt-> {
                String err = validateInput();
                if ( null == err) {
                    hideMenu();
                    saveColumnName();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid column name: "+err, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }));


            cancelButton = new JButton("Cancel");
            cancelButton.setMargin(buttonInsets);
            cancelButton.addActionListener((evt-> {
                hideMenu();
                cancelAction.actionPerformed(evt);
            }));
            okButton.setPreferredSize(new Dimension(40,20));
            cancelButton.setPreferredSize(new Dimension(50,20));
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder());
        }
        return buttonPanel;
    }
    public void hideMenu() {
        Utils.removeItemListeners(okButton);
        Utils.removeItemListeners(cancelButton);
        if ( null != anchoredLayeredPane) {
            anchoredLayeredPane.close();
        }
    }
    private String validateInput() {
        String text = inputField.getText();
        String err;
        if ( StringUtils.isEmpty(text)) {
            err = "Column name can't be blank.";
        } else if ( ! Utils.containsOnlyAlphanumeric(text) ) {
            err = "Column name allows only alphanumeric characters.";
        } else {
            err = null;
        }
        return err;
    }
    private void saveColumnName() {
        String text = inputField.getText().trim();
        TableColumn tc = table.getColumnModel().getColumn(tableColumnIndex);
        tc.setHeaderValue(text);
        TableColumn [] allCols = getAllColumns();
        StringBuilder sb = new StringBuilder();
        for(TableColumn tblCol: allCols) {
            sb.append(tblCol.getIdentifier()).append("=").append(tblCol.getHeaderValue()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        AppController.getUser().setBookieColumnPrefs(sb.toString());
        UserPrefsProducer userPrefs = AppController.getUserPrefsProducer();
        userPrefs.sendUserPrefs();
    }
    private TableColumn [] getAllColumns() {
        JTable bindedTable;
        if ( table instanceof RowHeaderTable) {
            bindedTable = ((RowHeaderTable<?>)table).getMainTable();
        } else if ( table instanceof ColumnCustomizableTable) {
            bindedTable = ((ColumnCustomizableTable)table).getRowHeaderTable();
        } else {
            bindedTable = null;
        }

        int colCount;
        if ( null == bindedTable) {
            colCount = table.getColumnCount();
        } else {
            colCount = table.getColumnCount() + bindedTable.getColumnCount();
        }

        TableColumn [] result = new TableColumn [colCount];
        int index=0;
        for(int i=0;i<table.getColumnCount();i++) {
            result[index++]=table.getColumnModel().getColumn(i);
        }

        if ( null != bindedTable) {
            for(int i=0;i<bindedTable.getColumnCount();i++) {
                result[index++]=bindedTable.getColumnModel().getColumn(i);
            }
        }
        return result;
    }
}
