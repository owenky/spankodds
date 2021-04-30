package com.sia.client.model;

public class ColumnData {

  public final String  m_title;
  public final int     m_width;
  public final int     m_alignment;
  public final int     bookie_id;

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
  @Override
  public String toString()
  {
	  return m_title;
  }
  
}