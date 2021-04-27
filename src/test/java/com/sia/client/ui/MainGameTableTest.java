package com.sia.client.ui;

import com.sia.client.model.GameRegion;
import org.junit.Test;

import java.util.Vector;

import static junit.framework.Assert.assertEquals;

public class MainGameTableTest {

    @Test
    public void testgetValueAt() {
        MainGameTable table = new MainGameTable();
        table.addRegion( makeGameRegion(10));
        table.addRegion( makeGameRegion(20));
        table.addRegion( makeGameRegion(30));
        table.addRegion( makeGameRegion(40));

        assertEquals(10,table.getValueAt(0,0));
        assertEquals(100,table.getValueAt(1,0));

        assertEquals(20,table.getValueAt(3,0));

        assertEquals(40,table.getValueAt(9,0));
        assertEquals(4000,table.getValueAt(11,0));
    }
    private GameRegion makeGameRegion(int seed) {
        GameRegion gr = new GameRegion();
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
