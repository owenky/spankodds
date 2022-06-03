package com.sia.client.ui;

import com.sia.client.config.Config;
import com.sia.client.ui.lineseeker.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LineSeekerNodeTest {

    private AlertPeriod period = AlertPeriod.Full;
    private int gameId = 1;
    private String key = gameId + Config.KeyJointer+period.name();

    @Before
    public void init() {
        AlertConfig alertConfig = AlertAttrManager.of(gameId,period);
        LineSeekerAttribute lineSeekerAttribute = alertConfig.getSectionAtrribute(AlertSectionName.totalsName);
        lineSeekerAttribute.getVisitorColumn().setLineInput("1");
        lineSeekerAttribute.getVisitorColumn().setJuiceInput("2");

    }
    @Test
    public void testOf() {
        List<LineSeekerNode> nodes = LineSeekerNode.getAllNodes();

        assertEquals(1,nodes.size());

        LineSeekerNode node = nodes.get(0);
        assertEquals(1,new Double(node.getOver()).intValue());
        assertEquals(2,new Double(node.getOverjuice()).intValue());
    }
}
