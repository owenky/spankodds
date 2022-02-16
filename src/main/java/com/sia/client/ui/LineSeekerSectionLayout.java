package com.sia.client.ui;


import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class LineSeekerSectionLayout {

    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    private static final Dimension DefaultSpacingWidth = new Dimension(240,20);
    private final LineSeekerColumnFieldGroup leftColumn;
    private final LineSeekerColumnFieldGroup rightColumn;
    private final Checkbox useEquivalent;
    private JPanel layoutPane;

    public LineSeekerSectionLayout(LineSeekerSectionFieldGroup spreadFieldGrp) {

        this.leftColumn = spreadFieldGrp.leftColumn;
        this.rightColumn = spreadFieldGrp.rightColumn;
        this.useEquivalent = spreadFieldGrp.useEquivalent;
    }

    public JComponent getLayoutPane() {
        if (null == layoutPane) {
            layoutPane = new JPanel();
            layoutPane.setLayout(new GridBagLayout());

            GridBagConstraints c = createDefaultGridBagConstraints();

            //row column title
            c.gridy = 0;

            c.gridx = 0;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            formatColumnTitle(leftColumn.titleLabel);
            layoutPane.add(leftColumn.titleLabel, c);

            c.gridx = 3;
            c.gridy = 0;
            formatColumnTitle(rightColumn.titleLabel);
            layoutPane.add(rightColumn.titleLabel, c);

            //new row -- input field title
            c.fill = GridBagConstraints.NONE;
            c.gridy++;

            c.gridx = 0;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.SOUTH;
            layoutPane.add(makeInputTitleLabel(leftColumn.input1), c);

            c.gridx = 1;
            layoutPane.add(makeInputTitleLabel(leftColumn.input2), c);

            //spacing
            c.gridx = 2;
            JLabel spacingComp = new JLabel();
            spacingComp.setPreferredSize(DefaultSpacingWidth);
            layoutPane.add(spacingComp, c);

            c.gridx = 3;
            layoutPane.add(makeInputTitleLabel(rightColumn.input1), c);

            c.gridx = 4;
            layoutPane.add(makeInputTitleLabel(rightColumn.input2), c);

            //new row -- input fields
            c.anchor = GridBagConstraints.NORTH;
            c.gridy++;
            c.gridx = 0;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.CENTER;
            layoutPane.add(leftColumn.input1, c);

            c.gridx = 1;
            layoutPane.add(leftColumn.input2, c);

            c.gridx = 3;
            layoutPane.add(rightColumn.input1, c);

            c.gridx = 4;
            layoutPane.add(rightColumn.input2, c);

            //new row -- radio buttons
            JComponent radioGrpLeft = makeRadioGroup(leftColumn);
            c.gridy++;

            c.gridx = 0;
            c.gridwidth = 2;
            layoutPane.add(radioGrpLeft, c);

            JComponent radioGrpRight = makeRadioGroup(rightColumn);
            c.gridx = 3;
            layoutPane.add(radioGrpRight, c);

            //new row -- check box
            c.gridy++;

            c.gridx = 0;
            c.gridwidth = 5;
            c.anchor = GridBagConstraints.CENTER;
            if(null == useEquivalent.getLabel() || "".equals(useEquivalent.getLabel().trim())) {
                useEquivalent.setLabel(useEquivalent.getName());
            }
            layoutPane.add(useEquivalent, c);
        }
        return layoutPane;
    }
    private JLabel makeInputTitleLabel(JComponent input) {
        JLabel fieldTitleLabel = new JLabel(input.getName());
        fieldTitleLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        fieldTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return fieldTitleLabel;
    }
    private JComponent makeRadioGroup(LineSeekerColumnFieldGroup columnFieldGroup) {

        JPanel grpPanel = new JPanel();
        grpPanel.setLayout(new FlowLayout());
        grpPanel.add(columnFieldGroup.good);
        grpPanel.add(columnFieldGroup.bad);
        grpPanel.add(columnFieldGroup.neutral);

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
