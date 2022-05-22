package com.sia.client.ui;

import com.sia.client.config.SiaConst.LayedPaneIndex;
import com.sia.client.config.Utils;
import com.sia.client.ui.comps.LinkButton;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;


public class AnchoredLayeredPane implements ComponentListener {

    private final Integer layer_index;
	private final JComponent anchoredParentComp;
    private final JLayeredPane layeredPane;
	private final MouseAdapter mouseListener;
    private JScrollPane userComponentScrollPane;
    private boolean toHideOnMouseOut;
    private boolean isOpened = false;
    private Supplier<Point> anchorLocSupplier;
    private final SportsTabPane stp;
    private String title;
    private Callable<Boolean> closeValidor;
    private JComponent titlePanelLeftComp;
    private JComponent titlePaneRightComp;
    private final JPanel westPanel;
    private final JPanel eastPanel;
    private final JButton closeBtn;
    private boolean isCloseBtnAdded;
    private final java.util.List<Runnable> closeActions = new ArrayList<>();
    private static final Map<String,AnchoredLayeredPane> activeLayeredPaneMap =  new HashMap<>();

    public AnchoredLayeredPane(SportsTabPane stp,JComponent anchoredParentComp,int layer_index) {
        this.stp = stp;
        this.layer_index = layer_index;
        this.anchoredParentComp = anchoredParentComp;
        mouseListener = new HideOnMouseOutListener();
        layeredPane = getJLayeredPane();
        westPanel = new JPanel();
        eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.LINE_AXIS));
        westPanel.setLayout(new BorderLayout());
        closeBtn = new LinkButton("X");
        isCloseBtnAdded = false;
    }
    public AnchoredLayeredPane(SportsTabPane stp,int layer_index) {
        this(stp, stp,  layer_index);
    }
    public AnchoredLayeredPane(SportsTabPane stp) {
        this(stp, stp,  LayedPaneIndex.SportConfigIndex);
    }
    public synchronized void addCloseAction(Runnable r) {
        closeActions.add(r);
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public SportsTabPane getSportsTabPane() {
        return stp;
    }
    public void setCloseValidor(Callable<Boolean> closeValidor) {
        this.closeValidor = closeValidor;
    }
    private void prepareListening() {
		anchoredParentComp.addComponentListener(this);
    }
    public void openAndCenter(JComponent userComponent, Dimension totalSize,boolean toHideOnMouseOut) {

        Dimension selectedPaneDim = stp.getSelectedComponent().getSize();
        Dimension actualSize = calculateActualSize(selectedPaneDim,totalSize);
        Supplier<Point> anchorLocSupplier = ()->{
            Rectangle r = stp.getUI().getTabBounds(stp,0);
            Point tabPaneAnchor = stp.getLocationOnScreen();
            return new Point( tabPaneAnchor.x+ (int)((selectedPaneDim.getWidth()-actualSize.getWidth())/2), tabPaneAnchor.y+ (int)r.getHeight()+30);

        };
        openAndAnchoredAt(userComponent, actualSize,toHideOnMouseOut,anchorLocSupplier);
    }
    private Dimension calculateActualSize(Dimension selectedPaneDim,Dimension userDefinedSize) {
        int width = userDefinedSize.getWidth()<=0?Integer.MAX_VALUE:(int)userDefinedSize.getWidth();
        int height = userDefinedSize.getHeight()<=0?Integer.MAX_VALUE:(int)userDefinedSize.getHeight();
        int maxUserCompWidth = (int)selectedPaneDim.getWidth()-30;
        int maxUserCompHeight = (int)selectedPaneDim.getHeight()-30;

        return new Dimension(Math.min(maxUserCompWidth, width),Math.min(maxUserCompHeight, height));
    }
    public void openAndAnchoredAt(JComponent userComponent, Dimension size,boolean toHideOnMouseOut,Supplier<Point> anchorLocSupplier) {
        updateActiveLayerPaneMap();
        if (null == userComponent) {
            return;
        }
        this.toHideOnMouseOut = toHideOnMouseOut;
        this.userComponentScrollPane = makeUserComponetScrollPane(userComponent);
        if ( null != size) {
            userComponentScrollPane.setSize(size);
        }
        this.anchorLocSupplier = anchorLocSupplier;
        showLayeredPane();
    }

    private void updateActiveLayerPaneMap() {
        //one AnchoredLayerPane per SpankyWindow and layer_index
        String key = getActiveMapKey();
        AnchoredLayeredPane oldPane = activeLayeredPaneMap.get(key);
        if ( null != oldPane) {
            oldPane.close();
        }
        activeLayeredPaneMap.put(key,this);
    }
    private String getActiveMapKey(){
        return stp.getWindowIndex() +"_"+this.layer_index;
    }
    private void showLayeredPane() {
        if (null == userComponentScrollPane) {
            return;
        }
        userComponentScrollPane.removeMouseListener(mouseListener);
        if ( toHideOnMouseOut) {
            userComponentScrollPane.addMouseListener(mouseListener);
        }
        userComponentScrollPane.setVisible(true);
        layeredPane.add(userComponentScrollPane, layer_index);

        Point layeredPaneLoc = layeredPane.getLocationOnScreen();

        Point anchor_loc_ = anchorLocSupplier.get();
        final int x_ = anchor_loc_.x - layeredPaneLoc.x;
        final int y_ = anchor_loc_.y - layeredPaneLoc.y;
        userComponentScrollPane.setBounds(x_, y_, userComponentScrollPane.getWidth(), userComponentScrollPane.getHeight());
        isOpened = true;
        prepareListening();
    }
    private JScrollPane makeUserComponetScrollPane(JComponent userComponent) {
        JComponent containingComp;
        if ( null == title) {
            containingComp = userComponent;
        } else {
            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new BorderLayout());
            JLabel titleLabel = new JLabel(title);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            Font defaultFont = FontConfig.instance().getSelectedFont();
            Font titleFont = new Font(defaultFont.getFontName(),Font.BOLD,defaultFont.getSize()+4);
            titleLabel.setFont(titleFont);

            closeBtn.setFont(new Font(defaultFont.getFontName(),Font.BOLD,defaultFont.getSize()));
//            closeBtn.setOpaque(false);
//            closeBtn.setBorder(BorderFactory.createEmptyBorder());
//            closeBtn.setContentAreaFilled(false);
//            JPanel btnPanel = new JPanel();
//            btnPanel.setLayout(new BorderLayout());
//            btnPanel.add(closeBtn,BorderLayout.EAST);
            if ( ! isCloseBtnAdded) {
                eastPanel.add(closeBtn);
                isCloseBtnAdded = true;
            }

            titlePanel.add(westPanel,BorderLayout.WEST);
            titlePanel.add(eastPanel,BorderLayout.EAST);
            titlePanel.add(titleLabel,BorderLayout.CENTER);
            if ( null == eastPanel.getPreferredSize() && null != titlePanelLeftComp) {
                Dimension preSize = titlePanelLeftComp.getPreferredSize();
                eastPanel.setPreferredSize(preSize);
            }

            closeBtn.addActionListener(event-> close());
            titlePanel.setBorder(BorderFactory.createEmptyBorder(7, 5, 1, 7));

            containingComp = new JPanel();
            containingComp.setLayout(new BorderLayout());
            containingComp.add(titlePanel,BorderLayout.NORTH);
            containingComp.add(userComponent,BorderLayout.CENTER);
        }
        JScrollPane jScrollPane = new JScrollPane(containingComp);
        jScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
        return jScrollPane;
    }
    public void setTitlePanelLeftComp(JComponent titlePanelLeftComp) {
        this.titlePanelLeftComp = titlePanelLeftComp;
        westPanel.removeAll();
        westPanel.add(titlePanelLeftComp,BorderLayout.CENTER);
    }
    public void setTitlePanelRightComp(JComponent titlePanelLeftComp) {
        this.titlePanelLeftComp = titlePanelLeftComp;
        eastPanel.removeAll();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.LINE_AXIS));
        eastPanel.add(titlePanelLeftComp);
        eastPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        eastPanel.add(closeBtn);
    }
    public void close() {
        boolean toClose;
        try {
            toClose = (null == closeValidor || closeValidor.call());
        }catch ( Exception e) {
            Utils.log(e);
            toClose = true;
        }
        if ( toClose ) {
            hide();
            isOpened = false;
            anchoredParentComp.removeComponentListener(this);
            activeLayeredPaneMap.remove(this.getActiveMapKey(), this);
        }
        for(Runnable r: closeActions) {
            r.run();
        }
    }
    protected final void hide() {
        if ( null == userComponentScrollPane) {
            return;
        }
        userComponentScrollPane.removeMouseListener(mouseListener);
        userComponentScrollPane.setVisible(false);
        layeredPane.remove(userComponentScrollPane);
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
                showLayeredPane();
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
