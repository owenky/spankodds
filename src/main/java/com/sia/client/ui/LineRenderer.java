package com.sia.client.ui;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class LineRenderer extends LinePanel  implements TableCellRenderer {
	 

		public LineRenderer()
		{
			super();
			
			// System.out.println(table);
		}
		public LineRenderer(String name)
		{
			super(name);
		
			
			
		}

        @Override
         public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)  
			
			{
				

			// here value will be infoview,teamview,gmnumview,timeview,or spreadtotalview....
			// when it gets instantiated in LinesTableData it will be set with a border color and size
			// whch will be used as top border here to seperate sports and dates
			
			if(value == null)
			{
				if(row == 0 && column == 0)
				{
					System.out.println("value is null"+row+ "src/main" +column+", table name="+table.getName());
				}	
				return null;
			}
			else
			{
				if(row == 0 && column == 0)
				{
				//	System.out.println("value is "+value+"..."+row+".."+column);
				}					
				
			}

			if(row == 2)
			{
				//MatteBorder mb = new MatteBorder(15,-1,-1,-1, new Color(0,0,128));
				//this.setBorder(mb);
			/*
				if(value instanceof TeamView)
				{
					
					//TitledBorder tb = new TitledBorder(mb, "Some Long Text", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
					TitledBorder tb = new TitledBorder("Some Long Text");
					this.setBorder(tb);


				}
				else
				{
					MatteBorder mb2 = new MatteBorder(15,-1,-1,-1, new Color(0,0,128));
					this.setBorder(mb2);
					//this.setBorder(BorderFactory.createMatteBorder(15,-1,-1,-1, new Color(0,0,128)));
				}
			*/	
			}
			else
			{
				 // this.setBorder(null);
			//	this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			}			
				
					this.table = table;
					// int rowHeight = table.getRowHeight();
					// System.out.println(this.table);
					//(this.table).setRowHeight(60);
   // System.out.println("table printing---->"+this.table);

		// this col is the view col not the one in the table model
				
			//table.setRowHeight(30);	
					if(value instanceof SpreadTotalView)	
					{
						
						
						setLines((SpreadTotalView) value,row,column);
					}
					else if(value instanceof SoccerSpreadTotalView)	
					{
					
						
						setSoccerLines((SoccerSpreadTotalView) value,row,column);
						
					}
					else if(value instanceof TeamView)
					{
						setTeams((TeamView) value,row,column);
					}
					else if(value instanceof GameNumberView)
					{
						setGamenumbers((GameNumberView) value,row,column);
					}		
					else if(value instanceof SoccerGameNumberView)
					{
						
						setSoccerGamenumbers((SoccerGameNumberView) value,row,column);
						
					}						
					else if(value instanceof TimeView)
					{
						setTime((TimeView) value,row,column);
					}		
					else if(value instanceof ChartView)
					{
						setChart((ChartView) value,row,column);
					}	
					else if(value instanceof SoccerChartView)
					{
						setSoccerChart((SoccerChartView) value,row,column);
					}	
					else if(value instanceof InfoView)
					{
						setInfo((InfoView) value,row,column);
					}
					else if(value instanceof HeaderView)
					{
						setHeader((HeaderView) value,row,column);
					}				

			
            return this;
			}
		
    }