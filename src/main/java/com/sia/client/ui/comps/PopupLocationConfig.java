package com.sia.client.ui.comps;

import com.jidesoft.swing.JideToggleButton;
import com.sia.client.config.Utils;
import com.sia.client.ui.lineseeker.LineSeekerAlertMethodAttr;
import com.sia.client.ui.lineseeker.LineSeekerAlertMethodAttr.PopupLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PopupLocationConfig extends JPanel implements ActionableOnChanged{

    private PopupLocation selectedPopupLocation = PopupLocation.TOP_RIGHT;
    private final Map<JideToggleButton,PopupLocation> button2LocaionMap = new HashMap<>();
    private final Map<CompValueChangedListener,ItemListener> compoundListenerMap = new HashMap<>();
    private final JideToggleButton upperright;
    private final JideToggleButton upperleft;
    private final JideToggleButton lowerright;
    private final JideToggleButton lowerleft;

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
        ItemListener compoundListener = (e) -> {
            if ( e.getStateChange()== ItemEvent.SELECTED) {
                selectedPopupLocation = button2LocaionMap.get(e.getSource());
                itemListener.itemStateChanged(e);
            }
        };
        compoundListenerMap.put(itemListener,compoundListener);
        upperright.addItemListener(compoundListener);
        upperleft.addItemListener(compoundListener);
        lowerright.addItemListener(compoundListener);
        lowerleft.addItemListener(compoundListener);
    }
    @Override
    public void rmListener(CompValueChangedListener itemListener) {
        ItemListener compoundListener = compoundListenerMap.remove(itemListener);
        if ( null != compoundListener) {
            upperright.removeItemListener(compoundListener);
            upperleft.removeItemListener(compoundListener);
            lowerright.removeItemListener(compoundListener);
            lowerleft.removeItemListener(compoundListener);
        }
    }
    @Override
    public void setValue(Object obj) {
        LineSeekerAlertMethodAttr.PopupLocation popupLocation;
        if (null == obj) {
            popupLocation = PopupLocation.TOP_RIGHT;
        } else if ( obj instanceof LineSeekerAlertMethodAttr.PopupLocation) {
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
    public void setSelectedPopupLocation(int location) {
        setSelectedPopupLocation(PopupLocation.findPopupLocation(location));
    }
    public void setSelectedPopupLocation(PopupLocation popupLocation) {
        if ( null == popupLocation || popupLocation==PopupLocation.CENTER) {
            popupLocation = PopupLocation.TOP_RIGHT;
        }
        selectedPopupLocation = popupLocation;
        Optional<Map.Entry<JideToggleButton,PopupLocation>> selectedEntry
                = button2LocaionMap.entrySet().stream().filter(e->e.getValue()==selectedPopupLocation).findAny();
        if ( selectedEntry.isPresent()) {
            selectedEntry.get().getKey().setSelected(true);
        }
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
        button2LocaionMap.put(button,popupLocation);
        return button;
    }
}
