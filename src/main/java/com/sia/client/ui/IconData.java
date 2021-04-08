package com.sia.client.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class IconData {

  public ImageIcon  m_icon;
  public Object m_data;

  public IconData(ImageIcon icon, Object data) {
    m_icon = icon;
    m_data = data;
  }

  public String toString() {
    return m_data.toString();
  }
}