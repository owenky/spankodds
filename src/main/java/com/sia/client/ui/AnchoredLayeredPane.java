package com.sia.client.ui;

import com.sia.client.config.SiaConst.LayedPaneIndex;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;


public class AnchoredLayeredPane implements ComponentListener {

    private final Integer layer_index;
	private final JComponent anchoredParentComp;
    private final JLayeredPane layeredPane;
	private final MouseAdapter mouseListener;
    private JComponent userComponent;
    private boolean toHideOnMouseOut;
    private boolean isOpened = false;
    private Supplier<Point> anchorLocSupplier;
    private final SportsTabPane stp;

    public AnchoredLayeredPane(SportsTabPane stp) {
        this(stp, (JComponent)stp.getSelectedComponent(), LayedPaneIndex.SportConfigIndex);
    }
    public AnchoredLayeredPane(SportsTabPane stp,JComponent anchoredParentComp,int layer_index_) {
        this.stp = stp;
        this.layer_index = layer_index_;
		this.anchoredParentComp = anchoredParentComp;
        mouseListener = new HideOnMouseOutListener();
        layeredPane = getJLayeredPane();
    }

    private void prepareListening() {
		anchoredParentComp.addComponentListener(this);
    }
    public JComponent getUserComponent() {
        return this.userComponent;
    }
    public void openAndCenter(JComponent userComponent,boolean toHideOnMouseOut) {
        Supplier<Point> anchorLocSupplier = ()->{
            Dimension selectedPaneDim = stp.getSelectedComponent().getSize();
            int maxUserCompWidth = (int)selectedPaneDim.getWidth()-30;
            int maxUserCompHeight = (int)selectedPaneDim.getHeight()-30;

            Dimension prefDim = userComponent.getPreferredSize();

            int preferredWidth = (int)prefDim.getWidth();
            preferredWidth = preferredWidth<=0?Integer.MAX_VALUE:preferredWidth;

            int preferredHeight = (int)prefDim.getHeight();
            preferredHeight = preferredHeight<=0?Integer.MAX_VALUE:preferredHeight;

            int userCompWidth= Math.min(maxUserCompWidth, preferredWidth);
            int userCompHeight= Math.min(maxUserCompHeight, preferredHeight);
            userComponent.setSize(userCompWidth,userCompHeight);

            Rectangle r = stp.getUI().getTabBounds(stp,0);
            Point tabPaneAnchor = stp.getLocationOnScreen();
            return new Point( tabPaneAnchor.x+ (int)((selectedPaneDim.getWidth()-userCompWidth)/2), tabPaneAnchor.y+ (int)r.getHeight()+30);

        };
        openAndAnchoredAt(userComponent, toHideOnMouseOut,anchorLocSupplier);
    }
    public void openAndAnchoredAt(JComponent userComponent,boolean toHideOnMouseOut,Supplier<Point> anchorLocSupplier) {
        if (null == userComponent) {
            return;
        }
        setUserPane(userComponent,toHideOnMouseOut);
        this.anchorLocSupplier = anchorLocSupplier;
        userComponent.setVisible(true);
        layeredPane.add(userComponent, layer_index);

        Point layeredPaneLoc = layeredPane.getLocationOnScreen();

        Point anchor_loc_ = anchorLocSupplier.get();
        final int x_ = anchor_loc_.x - layeredPaneLoc.x;
        final int y_ = anchor_loc_.y - layeredPaneLoc.y;
        userComponent.setBounds(x_, y_, userComponent.getWidth(), userComponent.getHeight());
        isOpened = true;
        prepareListening();
    }
    private void setUserPane(JComponent userComponent_,boolean toHideOnMouseOut) {
        if ( null != this.userComponent ) {
            userComponent.removeMouseListener(mouseListener);
        }
        this.toHideOnMouseOut = toHideOnMouseOut;
        this.userComponent = userComponent_;
        if ( toHideOnMouseOut) {
            userComponent.addMouseListener(mouseListener);
        }
    }
    public void close() {
        hide();
        isOpened = false;
		anchoredParentComp.removeComponentListener(this);
    }
    protected final void hide() {
        if ( null == userComponent) {
            return;
        }
        userComponent.removeMouseListener(mouseListener);
        userComponent.setVisible(false);
        layeredPane.remove(userComponent);
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private JLayeredPane getJLayeredPane() {
        Component root_comp_ = SwingUtilities.getRoot(anchoredParentComp);
        if (root_comp_ == null) {
            root_comp_ = getTopContainer();
        }
        JRootPane rootPane_ = SwingUtilities.getRootPane(root_comp_);
        return rootPane_.getLayeredPane();
    }
    private Component getTopContainer() {

        Component topContainer = anchoredParentComp.getParent();
        while (!(topContainer instanceof JFrame)) {
            topContainer = topContainer.getParent();
        }
        return topContainer;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentShown(ComponentEvent e) {

        Component comp_ = e.getComponent();
		if ( anchoredParentComp == comp_) {
            if (isOpened()) {
                openAndAnchoredAt(userComponent,toHideOnMouseOut,anchorLocSupplier);
            }
        }
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        Component comp_ = e.getComponent();
		if ( anchoredParentComp == comp_) {
            if (isOpened()) {
                hide();
            }
        }
    }

    protected boolean isOpened() {
        return isOpened;
    }
 /////////////////////////////////////////////////////////////////////////////////////////////////////
     private class HideOnMouseOutListener extends MouseAdapter{
         private boolean hasMouseEntered = false;
         @Override
         public void mouseExited(MouseEvent e) {
             JComponent source = (JComponent)e.getSource();
             source.setBorder(BorderFactory.createLineBorder(Color.RED));
//System.out.println("source="+ ((JComponent)e.getSource()).getName()+", rec="+source.getVisibleRect()+",e point="+e.getPoint()+", contains="+source.contains(e.getPoint()));
             if (  null != source && ! source.contains(e.getPoint()) ) {
                 if (hasMouseEntered) {
                     hide();
                 }
                 hasMouseEntered = false;
             }
         }
         @Override
         public void mouseEntered(MouseEvent e) {
             hasMouseEntered = true;
         }
     }
}
