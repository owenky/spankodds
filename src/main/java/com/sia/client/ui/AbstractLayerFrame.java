package com.sia.client.ui;

import com.sia.client.ui.comps.EditablePane;
import com.sia.client.ui.comps.UICompValueBinder;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLayerFrame extends AbstractLayerEditablePanes {

    private List<EditablePane> editablePanes;
    private UICompValueBinder uiCompValueBinder;

    protected abstract Object getBindingObject();
    protected abstract JLabel getEditStatusLabel();
    protected abstract void bindProperties(UICompValueBinder binder);

    public AbstractLayerFrame(SportsTabPane stp, String title, int layer_index) {
        super(stp,title,layer_index);
        init();
    }
    public AbstractLayerFrame(SportsTabPane stp,String title) {
        super(stp,title);
        init();
    }
    public AbstractLayerFrame(SportsTabPane stp,int layer_index) {
       super(stp,layer_index);
       init();
    }
    @Override
    public List<EditablePane> getEditablePanes() {
        if ( null== editablePanes) {
            editablePanes = new ArrayList<>(1);
            EditablePane editablePane = new EditablePane() {

                @Override
                public JLabel getEditStatusLabel() {
                    return AbstractLayerFrame.this.getEditStatusLabel();
                }

                @Override
                public UICompValueBinder getJComponentBinder() {
                    if ( null == uiCompValueBinder) {
                        uiCompValueBinder = UICompValueBinder.register(AbstractLayerFrame.this,getBindingObject(),getOnValueChangedEventFunction());
                        bindProperties(uiCompValueBinder);
                    }
                    return uiCompValueBinder;
                }

                @Override
                public JComponent getPane() {
                    return getUserComponent();
                }
            };
            editablePanes.add(editablePane);
        }
        return editablePanes;
    }
    private void init() {
        setIsFloating(true);
    }
}
