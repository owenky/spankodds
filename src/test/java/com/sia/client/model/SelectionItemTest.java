package com.sia.client.model;

import com.sia.client.ui.simulator.TestGame;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SelectionItemTest {

    @Test
    public void testEquals() {
        PropItem item = new PropItem("abc","123");
        PropItem item2 = new PropItem("abc1","123");
        PropItem item3 = new PropItem("abc","1234");
        SelectionItem<String> item4 = new SelectionItem<>("abc").withDisplay("1234");

        assertNotEquals(item,item2);
        assertNotEquals(item,null);
        assertNotEquals(item,"a");
        assertNotEquals(item,"123");

        assertEquals(item,item3);
        assertEquals(item,item4);
        assertEquals(item,"abc");
    }
}
