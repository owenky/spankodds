import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import com.jidesoft.swing.TitledSeparator;

  public class RegularTableView extends JTable 
  {
	  
	
	
         public TableCellRenderer getCellRenderer(int row, int column)  
			
			{
			return new LineRenderer();
			}
  
	
  }