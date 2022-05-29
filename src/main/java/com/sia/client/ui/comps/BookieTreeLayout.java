package com.sia.client.ui.comps;

import javax.swing.*;
import java.awt.*;

public class BookieTreeLayout implements EditableLayout{

    private final BookieTree bookieTree;
    private final JLabel editStatusLabel = new JLabel("");
    private JPanel sportsbooktreePanel;
    private JScrollPane jScrollPane;
    private Dimension preferredSize;
    private final SimpleValueWraper<String> bookies;
    private UICompValueBinder uiCompValueBinder;

    public BookieTreeLayout(BookieTree bookieTree,SimpleValueWraper<String> bookies) {
        this.bookieTree = bookieTree;
        this.bookies = bookies;
        uiCompValueBinder =  UICompValueBinder.register(this,bookies,getOnValueChangedEventFunction()).bind("value",bookieTree);
    }
    public void setPreferredSize(Dimension preferredSize) {
        this.preferredSize = preferredSize;
        if ( null != sportsbooktreePanel) {
            setPreferredSize();
        }
    }
    @Override
    public JComponent getLayoutPane() {
        if ( null == sportsbooktreePanel) {
            sportsbooktreePanel = new JPanel();
            JPanel editStatusPanel = new JPanel();
            editStatusPanel.setLayout(new BorderLayout());
            editStatusLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,6));
            JLabel titleLabel = new JLabel("Select Bookies");
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            editStatusPanel.add(titleLabel,BorderLayout.CENTER);
            editStatusPanel.add(editStatusLabel,BorderLayout.EAST);
            sportsbooktreePanel.setLayout(new BorderLayout());
            jScrollPane = new JScrollPane(bookieTree.getSportTree());
            sportsbooktreePanel.add(jScrollPane,BorderLayout.CENTER);
            sportsbooktreePanel.add(editStatusPanel,BorderLayout.NORTH);
            if ( null != preferredSize) {
                setPreferredSize();
            }
        }
        return sportsbooktreePanel;
    }
    private void setPreferredSize() {
        jScrollPane.setPreferredSize(preferredSize);
        sportsbooktreePanel.setPreferredSize(preferredSize);
    }
    @Override
    public JLabel getEditStatusLabel() {
        return editStatusLabel;
    }

    @Override
    public UICompValueBinder getJComponentBinder() {
        if ( null == uiCompValueBinder) {
            uiCompValueBinder = UICompValueBinder.register(this,bookies,getOnValueChangedEventFunction()).bind("value",bookieTree);
        }
        return uiCompValueBinder;
    }
}
