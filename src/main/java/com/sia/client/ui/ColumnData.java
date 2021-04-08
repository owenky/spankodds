package com.sia.client.ui;

public class ColumnData {

  public String  m_title;
  public int     m_width;
  public int     m_alignment;
  public int     bookie_id;

  public ColumnData(int bid,String title, int width, int alignment) {
	bookie_id = bid;
    m_title = title;
    m_width = width;
    m_alignment = alignment;
  }
  
  
  public ColumnData(String title, int width, int alignment) {
	bookie_id = 0;
    m_title = title;
    m_width = width;
    m_alignment = alignment;
  }
  
  public String toString()
  {
	  return "columndata bookieid="+bookie_id;
  }
  
}