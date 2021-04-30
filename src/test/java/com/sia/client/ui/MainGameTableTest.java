package com.sia.client.ui;


import org.junit.Test;

import javax.swing.table.TableColumn;
import java.util.Vector;

import static junit.framework.Assert.assertEquals;

public class MainGameTableTest {

    @Test
    public void testgetValueAt() {
        MainGameTable table = new MainGameTable();
        table.addColumn(new TableColumn());
        table.addGameLine( makeGameRegion(10));
        table.addGameLine( makeGameRegion(20));
        table.addGameLine( makeGameRegion(30));
        table.addGameLine( makeGameRegion(40));

        assertEquals(10,table.getValueAt(0,0));
        assertEquals(100,table.getValueAt(1,0));

        assertEquals(20,table.getValueAt(3,0));

        assertEquals(40,table.getValueAt(9,0));
        assertEquals(4000,table.getValueAt(11,0));
    }
    private LinesTableData makeGameRegion(int seed) {
        LinesTableData gr = new LinesTableData();
        Vector dataVector = new Vector();
        Vector columnIdentifier = new Vector();
        columnIdentifier.add("testColumn");
        Vector row = new Vector();
        row.add(seed);
        dataVector.add(row);

        row = new Vector();
        row.add(seed*10);
        dataVector.add(row);

        row = new Vector();
        row.add(seed*100);
        dataVector.add(row);

        gr.setDataVector(dataVector,columnIdentifier);
        return gr;
    }
}
