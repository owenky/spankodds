package com.sia.client.ui.comps;

import com.sia.client.config.SiaConst;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class NodeCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final TextComponent editorComponent;
    private final Dimension editorComponentSize = new Dimension(100,100);
    private final JTextField editorIndicator;
    public NodeCellEditor() {
        super();
        editorIndicator = new JTextField();
        editorIndicator.setEditable(false);
        editorIndicator.setBorder(BorderFactory.createLineBorder(Color.RED));
        editorComponent = new TextArea();
        editorComponent.setPreferredSize(editorComponentSize);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Rectangle rectangle = table.getCellRect(row,column,true);
        editorComponent.setText(String.valueOf(value));
        JRootPane rootPane_ = SwingUtilities.getRootPane(table);
        JLayeredPane jLayeredPane = rootPane_.getLayeredPane();
        if ( jLayeredPane.getIndexOf(editorComponent) <  0) {
            jLayeredPane.add(editorComponent, SiaConst.LayedPaneIndex.NoteColumnEditorIndex);
        }
        editorComponent.setVisible(true);
        editorComponent.setBounds(rectangle.x,rectangle.y+rectangle.height,editorComponentSize.width,editorComponentSize.height);
        return editorIndicator;
    }
    @Override
    public String getCellEditorValue() {
        return editorComponent.getText();
    }
    @Override
    public boolean stopCellEditing() {
        boolean status = super.stopCellEditing();
        editorComponent.setVisible(false);
        return status;
    }
}
