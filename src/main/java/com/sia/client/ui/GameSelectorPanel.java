package com.sia.client.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class GameSelectorPanel extends JPanel {

    private static final int width=180;
    private static final int rowHeight=30;
    private boolean initStatus = false;
    private HintTextField hintTextField;
    private HintTextField teams;
    private JButton addBtn;
    public GameSelectorPanel() {
        this.setPreferredSize(new Dimension(width,rowHeight*3));
    }
    public int getPreferredWidth() {
        return (int)getPreferredSize().getWidth();
    }
    @Override
    public void doLayout() {
        if ( ! initStatus) {
            initStatus = true;
            initComponent();
        }
        super.doLayout();
    }
    private void initComponent() {

        this.setBorder(BorderFactory.createEtchedBorder());
        hintTextField = new HintTextField("Enter Game #");
        hintTextField.setPreferredSize(new Dimension(width,rowHeight));
        teams = new HintTextField("teams");
        teams.setEditable(false);
        addBtn = new JButton("Add");

        this.setLayout(new GridLayout(0,1));
        this.add(hintTextField);
        this.add(teams);
        this.add(createBtnPanel());
    }
    private JPanel createBtnPanel() {
        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(BorderFactory.createEmptyBorder());
        GridBagLayout gridBagLayout = new GridBagLayout();
        btnPanel.setLayout(gridBagLayout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 0;

        gbc.gridx = 0;
        btnPanel.add(new JPanel() ,gbc);

        gbc.gridx = 1;
        btnPanel.add(addBtn ,gbc);

        gbc.gridx = 2;
        btnPanel.add(new JPanel() ,gbc);

        return btnPanel;
    }
}
