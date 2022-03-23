/*
 * Created on 2005-6-17 by Y.M.S.
 */
package com.sia.client.ui.sbt;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;


public class SBTButton extends JButton{
	public static final String CELL_RENDER_OF_TABLE = "CELL_RENDER_OF_TABLE";
	private List<AbstractButton> clonedButtons;
	private boolean isTableCellEditor = false;

    public SBTButton(String btnName_) {
    	super(btnName_);
    	setActionCommand(btnName_);
    	init();
    }
    
    public SBTButton() {
        super();
        init();
    }
    
    private void init() {
		setFont(new java.awt.Font("����", 0,12));
		this.setCursor(SiaConst.Ui.CURSOR_HAND);
    }
    @Override
    public synchronized void removeMouseListener(MouseListener ml){
    	super.removeMouseListener(ml);
    }

    /**
     * �������ť��Ϊ���༭���Ĺ�������������ʾʱ����Ҫ����׼��������-- XFZ@2012-08-07
     */
    public void prepareForEditing() {
    	//doing nothing.�����า��
    }
    public boolean isTableCellEditor() {
		return isTableCellEditor;
	}
    @Override
    public void requestFocus() {
        super.requestFocus();
    }
    @Override
    public boolean requestDefaultFocus() {
		return super.requestDefaultFocus();

    }
	public void setTableCellEditor(boolean isTableCellEditor) {
		this.isTableCellEditor = isTableCellEditor;
	}
    @Override
	public final void addActionListener(ActionListener al_){
		super.addActionListener(al_);
	}
    @Override
    public String getName(){
    	String rtn_ = super.getName();
    	if ( Utils.isBlank(rtn_)){
    		rtn_ = this.getActionCommand();
    	}
    	return rtn_;
    }
	/**
	 * ����һ�����Ա�ʾ�����İ�ť��
	 * @return
	 */
	public static JButton createArrowButton(){
		JButton button = new BasicArrowButton(BasicArrowButton.SOUTH);
		button.setSize(10,10);
		button.setPreferredSize(new Dimension(50,50));
		return button;
	}
	/**
	 * ����һ�����Ա�ʾ�����İ�ť(image)��
	 * @return
	 */
	public static SBTButton createComboboxDropDownButton() {
		SBTButton button = new SBTButton();
		ImageIcon icon = Utils.getImageIcon("downbutton.jpg");
		button.setIcon(icon);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setUI(new MetalButtonUI());

		return button;
	}
	@Override
	public void setText(String text){
		super.setText(text);
		updateCloneButtonProperties();
	}
	@Override
	public void setEnabled(boolean b){
		super.setEnabled(b);
		updateCloneButtonProperties();
	}
	@Override
	public String toString(){
		String rtn = getName();
		if ( Utils.isBlank(rtn)){
			rtn = getText();
		}
		return rtn;
	}
	@Override
	public void setToolTipText(String tooltiptxt){
		super.setToolTipText(tooltiptxt);
		updateCloneButtonProperties();
	}
	/**
	 * �������ť�����ԣ�����ʾ��������״̬�ȸ��Ƶ�anotherButton������Щ���Ա��ˣ�anotherButtonҲҪ���ű䣩-- XFZ@2010-10-29
	 * @param anotherButton
	 */
	public void cloneProperties(AbstractButton anotherButton){
		
		if ( clonedButtons == null) {
			clonedButtons = new ArrayList<>();
		}
		
		clonedButtons.add(anotherButton); //�����б����Ƶİ�ť����clonedButtons�Ա����Ըı�����֪ͨ���� -- XFZ@2010-10-29
		
		setCloneProperties(anotherButton);
		
		/**
		 * anotherButton��ť�Ķ���Ӧ��ִ�������ť�Ķ��� -- XFZ@2010-10-29
		 */
		anotherButton.addActionListener(new ActionDelegateListener(this));
	}
	private void updateCloneButtonProperties(){
		if ( clonedButtons == null){
			return;
		}
		
		for ( int i=0;i<clonedButtons.size();i++){
			AbstractButton aButton = clonedButtons.get(i);
			setCloneProperties(aButton);
		}
	}
	private void setCloneProperties(AbstractButton anotherButton){
		anotherButton.setText(this.getText());
		anotherButton.setToolTipText(this.getToolTipText());
		boolean isEnabled_ = this.isEnabled();
		anotherButton.setEnabled(isEnabled_);
	}
//////////////////////�ڲ���ʵ�ְ�ť�������� -- XFZ@2011-01-25 //////////////////////////////////
	/**
	 * ��װ����������İ�ť��ִ�еĶ�����sourceButton��ִ�ж���
	 * @author xfzheng
	 *
	 */
	private static class ActionDelegateListener implements ActionListener{
		public ActionDelegateListener(SBTButton sourceButton_){
			this.sourceButton = sourceButton_;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			sourceButton.doClick();
		}
		private final SBTButton sourceButton;
	}
	
}
