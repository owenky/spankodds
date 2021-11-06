package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.TableSection;

import java.lang.reflect.InvocationTargetException;

public class EmptySection implements EventGenerator{

    private TableSection<TestGame> sourceSection;
    private TableSection<TestGame> targetSection;
    private boolean movingForward = true;
    @Override
    public void generatEvent(final TableProperties [] tblProps) throws InvocationTargetException, InterruptedException {

        TableProperties tblProp = tblProps[0];
        ColumnCustomizableDataModel<TestGame> model = tblProp.table.getModel();

        TestTableSection [] sections = tblProp.getSections();

        if ( movingForward ) {
            sourceSection = sections[0];
            targetSection = sections[1];
        } else {
            sourceSection = sections[1];
            targetSection = sections[0];
        }

        GameGroupHeader targetHeader = targetSection.getGameGroupHeader();
        TestGame game = sourceSection.getGame(1);
        model.moveGameToThisHeader(game, targetHeader);

        if ( 0 == sourceSection.getRowCount()) {
            movingForward = !movingForward;
        }
    }
}
