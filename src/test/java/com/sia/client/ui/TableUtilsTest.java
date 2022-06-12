package com.sia.client.ui;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TableUtilsTest {

    @Before
    public void init() {

    }
    @Test
    public void testGetChildren() {
        JComponent root = createJComponent();
        JComponent l1_child1 = createJComponent();
        JComponent l1_child2 = createJComponent();
        JComponent l1_child3 = createJComponent();

        root.add(createComponent());
        root.add(l1_child1);
        root.add(l1_child2);
        root.add(l1_child3);
        root.add(createComponent());

        JComponent l2_child1 = createJComponent();
        JComponent l2_child2 = createJComponent();
        JComponent l2_child3 = createJComponent();

        l1_child1.add(l2_child1);
        l1_child1.add(createComponent());
        l1_child1.add(l2_child2);

        l2_child1.add(l2_child3);
        l2_child1.add(createComponent());

        List<JComponent> children = TableUtils.getChildren(root);
        assertEquals(6,children.size());
        assertTrue(children.contains(l1_child1));
        assertTrue(children.contains(l1_child2));
        assertTrue(children.contains(l1_child3));
        assertTrue(children.contains(l2_child1));
        assertTrue(children.contains(l2_child2));
        assertTrue(children.contains(l2_child3));

    }
    private static JComponent createJComponent() {
        return new JComponent() {

        };
    }
    private static Component createComponent() {
        return new Component() {

        };
    }
}
