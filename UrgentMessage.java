
import com.jidesoft.alert.Alert;
import com.jidesoft.alert.AlertGroup;
import com.jidesoft.animation.CustomAnimation;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;
import com.jidesoft.utils.PortingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.scene.media.*;

public class UrgentMessage
{


static AlertGroup ag = new AlertGroup();
// need to wrap this in a javafx swing component and invoke later

	public UrgentMessage(String urgentmessage)
	{
		
               
		this(urgentmessage,10000,SwingConstants.NORTH_EAST,AppController.getMainTabPane());
	}


	public UrgentMessage(String urgentmessage,int ms,int where,Component c)
	{
				
				String alerttext = "<HTML><H1>URGENT MESSAGE</H1><FONT COLOR=BLUE>" +
                "Alert Demo<BR>" +
                "Hello,<BR>" +
                "This is a sample alert demo.</FONT></HTML>";
				
				alerttext = urgentmessage;
				

				
				
				final JLabel message = new JLabel(alerttext);
			        PaintPanel panel = new PaintPanel(new BorderLayout(6, 6));
				panel.setBorder(BorderFactory.createEmptyBorder(6, 7, 7, 7));
				panel.add(message, BorderLayout.CENTER);
				panel.setOpaque(true);
				panel.setBackgroundPaint(new GradientPaint(0, 0, new Color(231, 229, 224), 0, panel.getPreferredSize().height, new Color(212, 208, 200)));
	              Alert alert = new Alert();
				  
				  
				JPanel rightPanel = new JPanel(new GridLayout(1, 2, 0, 0));
                JideButton closeButton = createButton(IconsFactory.getImageIcon(UrgentMessage.class, "close.png"));
				closeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        alert.hidePopupImmediately();
                    }
                });
				rightPanel.add(closeButton);				  
				  
				JPanel topPanel = JideSwingUtilities.createTopPanel(rightPanel);
				panel.add(topPanel, BorderLayout.AFTER_LINE_ENDS);				  
				  
				  
				ag.add(alert);
                alert.getContentPane().setLayout(new BorderLayout());
				alert.getContentPane().add(panel);
                /*alert.getContentPane().add(createSampleAlert(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        alert.hidePopupImmediately();
                    }
                }));
				*/
				alert.setOwner(c);
                alert.setResizable(true);
                alert.setMovable(true);
                alert.setTransient(false);
                alert.setTimeout(ms);
                alert.setPopupBorder(BorderFactory.createLineBorder(new Color(10, 30, 106)));

/*
                CustomAnimation showAnimation = new CustomAnimation();
				showAnimation.setEffect(CustomAnimation.EFFECT_FLY);
				showAnimation.setSpeed(CustomAnimation.SPEED_VERY_FAST);
				showAnimation.setSmoothness(CustomAnimation.SMOOTHNESS_MEDIUM);
				showAnimation.setFunctionX(CustomAnimation.FUNC_LINEAR);
				showAnimation.setFunctionY(CustomAnimation.FUNC_LINEAR);
				showAnimation.setDirection(CustomAnimation.TOP_LEFT);
                showAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
                alert.setShowAnimation(showAnimation);
*/
                CustomAnimation hideAnimation = new CustomAnimation();
                hideAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
			//	hideAnimation.setFunctionFade(CustomAnimation.FUNC_LINEAR);
				hideAnimation.setFunctionFade(CustomAnimation.FUNC_POW_HALF);
			
                alert.setHideAnimation(hideAnimation);
    //   SwingUtilities.invokeLater(new Runnable() {
      //      public void run() {
                alert.showPopup(where);	
		//	  }
			//});		
		
		
		
		
	}

  private static JideButton createButton(Icon icon) {
        return new JideButton(icon);
    }

}