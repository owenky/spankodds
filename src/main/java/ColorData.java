import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class ColorData {

  public Color  m_color;
  public Object m_data;
  public static Color GREEN = new Color(0, 128, 0);
  public static Color RED = Color.red;

  public ColorData(Color color, Object data) {
    m_color = color;
    m_data  = data;
  }

  public ColorData(Double data) {
    m_color = data.doubleValue() >= 0 ? GREEN : RED;
    m_data  = data;
  }

  public String toString() {
    return m_data.toString();
  }
}