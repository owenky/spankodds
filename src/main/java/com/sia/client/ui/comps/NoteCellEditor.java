package com.sia.client.ui.comps;

import com.sia.client.config.SiaConst;
import com.sia.client.model.AccessableToGame;
import com.sia.client.model.Game;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class NoteCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final EditorComponent editorComponent;
    private final Dimension editorComponentSize = new Dimension(500,150);
    private final JLabel editorIndicator;
    private JLayeredPane jLayeredPane;
    public NoteCellEditor() {
        super();
        editorIndicator = new JLabel();
        editorIndicator.setBorder(BorderFactory.createLineBorder(Color.RED));
        editorComponent = new EditorComponent();
        editorComponent.setPreferredSize(editorComponentSize);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Rectangle rectangle = table.getCellRect(row,column,true);
        Point tblLocation =table.getLocationOnScreen();
        Point popupLocation = new Point(tblLocation.x+rectangle.x, tblLocation.y+rectangle.y+table.getRowHeight(row));
        editorComponent.textEditor.setText(String.valueOf(value));
        JRootPane rootPane_ = SwingUtilities.getRootPane(table);
        jLayeredPane = rootPane_.getLayeredPane();
        Point layerPaneLoc= jLayeredPane.getLocationOnScreen();
        if ( jLayeredPane.getIndexOf(editorComponent) <  0) {
            jLayeredPane.add(editorComponent, SiaConst.LayedPaneIndex.NoteColumnEditorIndex);
        }
            editorComponent.setVisible(true);
        editorComponent.setBounds(popupLocation.x-layerPaneLoc.x,popupLocation.y-layerPaneLoc.y,editorComponentSize.width,editorComponentSize.height);

        String title;
        if ( table instanceof AccessableToGame) {
            int rowModelIndex = table.convertRowIndexToModel(row);
            Game game = (Game)((AccessableToGame)table).getGame(rowModelIndex);
            title = "Note of "+game.getGame_id()+" ("+game.getShortvisitorteam()+" / " +game.getShorthometeam()+")";
        } else {
            title = "Game Note";
        }
        editorComponent.setTitle(title);
        return editorIndicator;
    }
    @Override
    public String getCellEditorValue() {
        return editorComponent.textEditor.getText();
    }
    @Override
    public boolean stopCellEditing() {
        boolean status = super.stopCellEditing();
        hideEditorComponent();
        return status;
    }
    private void hideEditorComponent() {
        editorComponent.setVisible(false);
        if ( null != jLayeredPane) {
            jLayeredPane.remove(editorComponent);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
    private class EditorComponent extends JInternalFrame {
        private final TextComponent textEditor;
        private EditorComponent() {
            textEditor = new TextArea();
            setLayout(new BorderLayout());
            add(textEditor,BorderLayout.CENTER);
            add(createButtonPanel(),BorderLayout.SOUTH);
            setBorder(BorderFactory.createEtchedBorder());
        }
        private JComponent createButtonPanel() {
            JPanel buttonPanel = new JPanel();
            JButton saveBtn = new JButton("Save");
            JButton cancelBtn = new JButton("Cancel");
            BoxLayout boxLayout = new BoxLayout(buttonPanel,BoxLayout.X_AXIS);
            buttonPanel.setLayout(boxLayout);
            buttonPanel.add(Box.createGlue());
            buttonPanel.add(saveBtn);
            buttonPanel.add(cancelBtn);
            buttonPanel.add(Box.createGlue());

            saveBtn.addActionListener(e-> {
                stopCellEditing();
            });
            cancelBtn.addActionListener(e-> {
                cancelCellEditing();
                hideEditorComponent();
            });
            return buttonPanel;
        }
    }
}
