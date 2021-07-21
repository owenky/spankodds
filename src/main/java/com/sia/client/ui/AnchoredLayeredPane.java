package com.sia.client.ui;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;


public class AnchoredLayeredPane implements ComponentListener {

    private final Integer layer_index;
	private final JComponent current_screen;
    private final JLayeredPane layeredPane;
	private final MouseAdapter mouseListener;
    protected JComponent userComponent;
    private boolean isOpened = false;
    private Supplier<Point> anchorLocSupplier;

    public AnchoredLayeredPane(JComponent current_screen, int layer_index_) {
        this.layer_index = layer_index_;
		this.current_screen = current_screen;
        mouseListener = new MouseAdapter() {
            private boolean hasMouseEntered = false;
            @Override
            public void mouseExited(MouseEvent e) {
                JComponent source = (JComponent)e.getSource();
                if ( ! source.contains(e.getPoint()) ) {
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
        };
        layeredPane = getJLayeredPane();
    }

    private void prepareListening() {
		current_screen.addComponentListener(this);
    }

    public void setUserPane(JComponent userComponent_) {
        if ( null != this.userComponent ) {
            userComponent.removeMouseListener(mouseListener);
        }
        this.userComponent = userComponent_;
        userComponent.addMouseListener(mouseListener);
    }
    public JComponent getUserComponent() {
        return this.userComponent;
    }

    public void openAndAnchoredAt(Supplier<Point> anchorLocSupplier) {

        this.anchorLocSupplier = anchorLocSupplier;
        if (null == userComponent) {
            return;
        }
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

    public void close() {
        hide();
        isOpened = false;
		current_screen.removeComponentListener(this);
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
        Component root_comp_ = SwingUtilities.getRoot(current_screen);
        if (root_comp_ == null) {
            root_comp_ = getTopContainer();
        }
        JRootPane rootPane_ = SwingUtilities.getRootPane(root_comp_);
        return rootPane_.getLayeredPane();
    }
    private Component getTopContainer() {

        Component topContainer = current_screen.getParent();
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
		if ( current_screen == comp_) {
            if (isOpened()) {
                openAndAnchoredAt(anchorLocSupplier);
            }
        }
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        Component comp_ = e.getComponent();
		if ( current_screen == comp_) {
            if (isOpened()) {
                hide();
            }
        }
    }

    protected boolean isOpened() {
        return isOpened;
    }
}
