package com.sia.client.ui.comps;

import com.jidesoft.swing.JideToggleButton;
import com.sia.client.config.Utils;
import com.sia.client.ui.lineseeker.LineSeekerAlertMethodAttr;
import com.sia.client.ui.lineseeker.LineSeekerAlertMethodAttr.PopupLocation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PopupLocationConfig extends JPanel implements ActionableOnChanged{

    private PopupLocation selectedPopupLocation = PopupLocation.TOP_RIGHT;
    private final Map<JideToggleButton,PopupLocation> button2LocaionMap = new HashMap<>();
    private final JideToggleButton upperright;
    private final JideToggleButton upperleft;
    private final JideToggleButton lowerright;
    private final JideToggleButton lowerleft;
    private PopupLocationListener popupLocationListener;

    public PopupLocationConfig() {
        upperright = makeJideToggleButton("upperright.png",PopupLocation.TOP_RIGHT);
        upperleft = makeJideToggleButton("upperleft.png",PopupLocation.TOP_LEFT);
        lowerright = makeJideToggleButton("lowerright.png",PopupLocation.BOTTOM_RIGHT);
        lowerleft = makeJideToggleButton("lowerleft.png",PopupLocation.BOTTOM_LEFT);
        ButtonGroup group = new ButtonGroup();
        group.add(upperright);
        group.add(upperleft);
        group.add(lowerright);
        group.add(lowerleft);
        initLayout();
    }
    @Override
    public void addListener(CompValueChangedListener itemListener) {
        upperright.addItemListener(itemListener);
        upperleft.addItemListener(itemListener);
        lowerright.addItemListener(itemListener);
        lowerleft.addItemListener(itemListener);
    }
    @Override
    public void setValue(Object obj) {
        LineSeekerAlertMethodAttr.PopupLocation popupLocation;
        if ( obj instanceof LineSeekerAlertMethodAttr.PopupLocation) {
            popupLocation = (LineSeekerAlertMethodAttr.PopupLocation)obj;
        } else {
            popupLocation = LineSeekerAlertMethodAttr.PopupLocation.valueOf(String.valueOf(obj));
        }
        setSelectedPopupLocation(popupLocation);
    }
    @Override
    public LineSeekerAlertMethodAttr.PopupLocation getValue() {
        return selectedPopupLocation;
    }
    public void setPopupLocationListener(PopupLocationListener popupLocationListener) {
        this.popupLocationListener = popupLocationListener;
    }
    public void setSelectedPopupLocation(int location) {
        setSelectedPopupLocation(PopupLocation.findPopupLocation(location));
    }
    public void setSelectedPopupLocation(PopupLocation popupLocation) {
        selectedPopupLocation = popupLocation;
    }
    public PopupLocation getSelectedPopupLocation() {
        return selectedPopupLocation;
    }
    private void initLayout() {

        setLayout(new GridLayout(2, 2, 0, 0));
        add(upperleft);
        add(upperright);
        add(lowerleft);
        add(lowerright);

    }
    private JideToggleButton makeJideToggleButton(String icon,PopupLocation popupLocation) {
        JideToggleButton button = new JideToggleButton(new ImageIcon(Utils.getMediaResource(icon)));
        button.addItemListener(e -> {
            selectedPopupLocation = popupLocation;
            if ( null != popupLocationListener) {
                popupLocationListener.process(popupLocation);
            }
        });
        button2LocaionMap.put(button,popupLocation);
        return button;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public interface PopupLocationListener {
        void process(PopupLocation popupLocation);
    }
}
