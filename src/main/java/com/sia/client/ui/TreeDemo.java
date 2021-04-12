package com.sia.client.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.BorderLayout;

public class TreeDemo {

    TreeDemo() {
        // Create a new JFrame container.
        JFrame jfrm = new JFrame("Tree Demo");

        // Use the default border layout manager.

        // Give the frame an initial size.
        jfrm.setSize(200, 200);

        // Terminate the program when the user closes the application.
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Begin creating the tree by defining the
        // structure and relationship of its nodes.

        // First, create root node of tree.
        DefaultMutableTreeNode root =
                new DefaultMutableTreeNode("Food");

        // Next, create two subtrees.  One contains
        // fruit, the other vegetables.

        // Create the root of the Fruit subtree.
        DefaultMutableTreeNode fruit =
                new DefaultMutableTreeNode("Fruit");
        root.add(fruit); // add the Fruit node to the tree.

        // The Fruit subtree has two subtrees of its own.
        // The first is Apples. The second is Pears.

        // Create an Apples subtree.
        DefaultMutableTreeNode apples =
                new DefaultMutableTreeNode("Apples");
        fruit.add(apples); // add the Apples node to Fruit

        // Populate the Apples subtree by adding
        // apple varieties to the Apples subtree.
        apples.add(new DefaultMutableTreeNode("Jonathan"));
        apples.add(new DefaultMutableTreeNode("Winesap"));

        // Create a Pears subtree.
        DefaultMutableTreeNode pears =
                new DefaultMutableTreeNode("Pears");
        fruit.add(pears); // add the Pears node to fruit

        // Populate the Pears subtree by adding
        // pear varieties to the Pears subtree.
        pears.add(new DefaultMutableTreeNode("Bartlett"));

        // Create the root of the Vegetable subtree.
        DefaultMutableTreeNode veg =
                new DefaultMutableTreeNode("Vegetables");
        root.add(veg); // add the Vegetable node to the tree

        // Populate Vegetables.
        veg.add(new DefaultMutableTreeNode("Beans"));
        veg.add(new DefaultMutableTreeNode("Corn"));
        veg.add(new DefaultMutableTreeNode("Potatoes"));
        veg.add(new DefaultMutableTreeNode("Rice"));

        // Now, create a JTree that uses the structure
        // defined by the preceding statements.
        JTree jtree = new JTree(root);

        // Finally, wrap the tree in a scroll pane.
        JScrollPane jscrlp = new JScrollPane(jtree);

        // Add the tree and label to the content pane.
        jfrm.getContentPane().add(jscrlp, BorderLayout.CENTER);

        // Display the frame.
        jfrm.setVisible(true);
    }
}