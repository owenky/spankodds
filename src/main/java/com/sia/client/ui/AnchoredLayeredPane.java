package com.sia.client.ui;

import com.sia.client.config.Utils;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.function.Supplier;


public class AnchoredLayeredPane implements ComponentListener {

    private final Integer layer_index;
	private final JComponent current_screen;
    protected JComponent userComponent;
    private boolean isOpened = false;
    private Supplier<Point> anchorLocSupplier;

    public AnchoredLayeredPane(JComponent current_screen, int layer_index_) {
        this.layer_index = layer_index_;
		this.current_screen = current_screen;
    }

    private void setLocation(Component topContainer, int x, int y, Rectangle top_container_rec_) {

        int width_ = userComponent.getWidth();
        int height_ = userComponent.getHeight();

        Dimension topContainerDim = topContainer.getSize();
        if (height_ <= 0) {
            height_ = topContainerDim.height;
        }
        if (width_ <= 0) {
            width_ = topContainerDim.width;
        }
        //���userComponent���ұ��Ƿ��쵽�����ڵ������ˣ�����ǣ���ô����userComponent��ߵ�λ�á�
        int x_right_of_usercomponent_ = x + width_;
        int x_right_of_topcontainer_ = top_container_rec_.x + top_container_rec_.width;
        if (x_right_of_usercomponent_ > x_right_of_topcontainer_) {
            x = x_right_of_topcontainer_ - width_;
        }
        //���userComponent�ĵײ��Ƿ��쵽�����ڵ������ˣ�����ǣ���ô����userComponent�ϱߵ�λ�á�
        int y_bottom_of_usercomponent_ = y + height_;
        int y_bottom_of_topcontainer_ = top_container_rec_.y + top_container_rec_.height;
        if (y_bottom_of_usercomponent_ > y_bottom_of_topcontainer_) {
            y = y_bottom_of_usercomponent_ - height_;
        }
        userComponent.setBounds(x, y, width_, height_);
    }

    private void prepareListening() {
		current_screen.addComponentListener(this);
    }

    public void setUserPane(JComponent userComponent_) {
        this.userComponent = userComponent_;
    }
    public JComponent getUserComponent() {
        return this.userComponent;
    }

    public void openAndAnchoredAt(Supplier<Point> anchorLocSupplier) {

        this.anchorLocSupplier = anchorLocSupplier;
        if (null == userComponent) {
            return;
        }

        removeComponentsFromThisLayer();

        userComponent.setVisible(true);
        JLayeredPane layeredPane_ = getJLayeredPane();
        layeredPane_.add(userComponent, layer_index);
        Component topContainer = getTopContainer();
        Rectangle topContainer_rec_ = topContainer.getBounds();

        Point anchor_loc_ = anchorLocSupplier.get();
			final int x_ = anchor_loc_.x-topContainer_rec_.x;
        final int y_ = anchor_loc_.y - topContainer_rec_.y;
        setLocation(topContainer, x_, y_, topContainer_rec_);


        isOpened = true;
        prepareListening();
    }

    public void close() {
        hide();
        isOpened = false;
		current_screen.removeComponentListener(this);
    }

    /**
     * �رմ���һ����close
     */
    protected final void hide() {
        if (userComponent == null) {
            return;
        }
        userComponent.setVisible(false);
        Utils.fireVisibilityChangedEvent(userComponent, true);
        JLayeredPane layeredPane_ = getJLayeredPane();
        layeredPane_.remove(userComponent);

        //�����Ƴ�userComponent�ǲ����ģ���Ϊ����DyDataContainer����Ҳ��HistoryWindow�������Layer�����������ЩҲ�Ƴ�������ʾ�����������ʷ����-- XFZ@2015-01-08
        removeComponentsFromThisLayer();
    }

    private JLayeredPane getJLayeredPane() {
        Component root_comp_ = SwingUtilities.getRoot(current_screen);
        if (root_comp_ == null) {
            //���磬���ۿ۷������棬���ָ���ͻ���root_comp_���ǿ�-- XFZ@2015-02-11
            root_comp_ = getTopContainer();
        }
        JRootPane rootPane_ = SwingUtilities.getRootPane(root_comp_);
        return rootPane_.getLayeredPane();
    }

    private void removeComponentsFromThisLayer() {
        JLayeredPane layeredPane_ = getJLayeredPane();
        Component[] components_in_this_layer_ = layeredPane_.getComponentsInLayer(layer_index);
        if (components_in_this_layer_ != null) {
            for (int i = 0; i < components_in_this_layer_.length; i++) {
                Component comp_ = components_in_this_layer_[i];
                if (comp_ instanceof JComponent) {
                    comp_.setVisible(false);
                }
                layeredPane_.remove(components_in_this_layer_[i]);
            }
        }
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
