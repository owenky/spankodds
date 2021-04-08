package com.sia.client.ui;

import javax.swing.ImageIcon;

public class StockData {

  public static ImageIcon ICON_UP = new ImageIcon("ArrUp.gif");
  public static ImageIcon ICON_DOWN = new ImageIcon("ArrDown.gif");
  public static ImageIcon ICON_BLANK = new ImageIcon("blank.gif");

  public IconData m_symbol;
  public String    m_name;
  public Double    m_last;
  public Double    m_open;
  public ColorData m_change;
  public ColorData m_changePr;
  public Long      m_volume;

  public StockData(String symbol, String name, double last,
   double open, double change, double changePr, long volume) {
    m_symbol = new IconData(getIcon(change), symbol);
    m_name = name;
    m_last = new Double(last);
    m_open = new Double(open);
    m_change = new ColorData(new Double(change));
    m_changePr = new ColorData(new Double(changePr));
    m_volume = new Long(volume);
  }

  public static ImageIcon getIcon(double change) {
    return (change>0 ? ICON_UP : (change<0 ? ICON_DOWN :
      ICON_BLANK));
  }
}