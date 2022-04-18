package com.sia.client.ui.lineseeker;


import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class SectionLayout {

    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    private static final Dimension DefaultSpacingWidth = new Dimension(240,20);
    private final ColumnComponents leftColumn;
    private final ColumnComponents rightColumn;
    private final JCheckBox useEquivalent;
    private JPanel layoutPane;

    public SectionLayout(SectionComponents spreadFieldGrp) {

        this.leftColumn = spreadFieldGrp.leftColumn;
        this.rightColumn = spreadFieldGrp.rightColumn;
        this.useEquivalent = spreadFieldGrp.useEquivalent;
    }

    public JComponent getLayoutPane() {
        if (null == layoutPane) {
            layoutPane = new JPanel();
            layoutPane.setLayout(new GridBagLayout());

            GridBagConstraints c = createDefaultGridBagConstraints();
            Insets defualtInsets = c.insets;

            //row column title
            c.gridy = 0;

            c.gridx = 0;
            c.gridwidth = 3;
            c.insets = new Insets(defualtInsets.top+5,defualtInsets.left,defualtInsets.bottom,defualtInsets.right);
            c.fill = GridBagConstraints.HORIZONTAL;
            formatColumnTitle(leftColumn.titleLabel);
            layoutPane.add(leftColumn.titleLabel, c);

            c.gridx = 4;
            c.gridy = 0;
            formatColumnTitle(rightColumn.titleLabel);
            layoutPane.add(rightColumn.titleLabel, c);

            //new row -- input field title
            c.insets = defualtInsets;
            c.fill = GridBagConstraints.NONE;
            c.gridy++;

            c.gridx = 0;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.SOUTH;
            layoutPane.add(makeInputTitleLabel(leftColumn.lineInput), c);

            c.gridx = 1;
            layoutPane.add(makeInputTitleLabel(leftColumn.juiceInput), c);

            //spacing
            c.gridx = 3;
            JLabel spacingComp = new JLabel();
            spacingComp.setPreferredSize(DefaultSpacingWidth);
            layoutPane.add(spacingComp, c);

            c.gridx = 4;
            layoutPane.add(makeInputTitleLabel(rightColumn.lineInput), c);

            c.gridx = 5;
            layoutPane.add(makeInputTitleLabel(rightColumn.juiceInput), c);

            //new row -- input fields
            c.anchor = GridBagConstraints.NORTH;
            c.gridy++;
            c.gridx = 0;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.CENTER;
            addComponent(leftColumn.lineInput,c);

            c.gridx = 1;
            layoutPane.add(leftColumn.juiceInput, c);

            c.gridx = 2;
            layoutPane.add(leftColumn.clearBtn, c);

            c.gridx = 4;
            addComponent(rightColumn.lineInput,c);

            c.gridx = 5;
            layoutPane.add(rightColumn.juiceInput, c);

            c.gridx = 6;
            layoutPane.add(rightColumn.clearBtn, c);

            //new row -- radio buttons
            JComponent radioGrpLeft = makeRadioGroup(leftColumn);
            c.gridy++;

            c.gridx = 0;
            c.gridwidth = 3;
            layoutPane.add(radioGrpLeft, c);

            JComponent radioGrpRight = makeRadioGroup(rightColumn);
            c.gridx = 4;
            layoutPane.add(radioGrpRight, c);

//            //new row -- check box
//            c.gridy++;
//
//            c.gridx = 0;
//            c.gridwidth = 7;
//            c.anchor = GridBagConstraints.CENTER;
//            if(null == useEquivalent.getLabel() || "".equals(useEquivalent.getLabel().trim())) {
//                useEquivalent.setLabel(useEquivalent.getName());
//            }
//            layoutPane.add(useEquivalent, c);
        }
        return layoutPane;
    }
    private void addComponent(JComponent comp,GridBagConstraints c) {
        if ( comp.isVisible()) {
            layoutPane.add(comp, c);
        } else {
            JLabel dummyComp = new JLabel();
            dummyComp.setPreferredSize(comp.getPreferredSize());
            dummyComp.setMinimumSize(comp.getPreferredSize());
            layoutPane.add(dummyComp, c);
        }
    }
    private JLabel makeInputTitleLabel(JComponent input) {
        JLabel fieldTitleLabel;
        if ( input.isVisible()) {
            fieldTitleLabel = new JLabel(input.getName());
        } else {
            fieldTitleLabel = new JLabel();
        }
        fieldTitleLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        fieldTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return fieldTitleLabel;
    }
    private JComponent makeRadioGroup(ColumnComponents columnComponents) {

        JPanel grpPanel = new JPanel();
        grpPanel.setLayout(new FlowLayout());
        grpPanel.add(columnComponents.getAlertStateButton(AlertState.Good));
        grpPanel.add(columnComponents.getAlertStateButton(AlertState.Bad));
        grpPanel.add(columnComponents.getAlertStateButton(AlertState.Neutral));

        return grpPanel;
    }
    private static GridBagConstraints createDefaultGridBagConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.ipady = 2;
        c.fill = GridBagConstraints.NONE;
        c.insets = EMPTY_INSETS;

        return c;
    }
    private static void formatColumnTitle(JLabel label) {
        label.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.LIGHT_GRAY));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }
}
