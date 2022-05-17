package com.sia.client.ui;

import javax.swing.*;
import java.io.*;

// CURRENTLY NOT USED BUT FEEL FREE TO USE FRANK IF YOU LIKE
public class DisplayWebPage extends JFrame
{

    public void displayLineHistroy(String url)
    {
        JEditorPane editor = new JEditorPane();
        editor.setEditable(false);
        try {
            editor.setPage(url);
        }catch (IOException e) {
            editor.setContentType("text/html");
            editor.setText("Page could not be loaded");
        }

        JScrollPane scrollPane = new JScrollPane(editor);
        JFrame f = new JFrame("Line History");
        f.getContentPane().add(scrollPane);
        f.setSize(700,400);
        f.setVisible(true);
       // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}