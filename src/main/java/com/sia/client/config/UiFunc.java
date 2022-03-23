package com.sia.client.config;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

public abstract class UiFunc {

    private static final JComponent component_for_calculate_string_size = new JEditorPane() ;

    public static boolean isEnterKey(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch(keyCode) {
            case KeyEvent.VK_ENTER:
                return true;
        }
        return false;
    }
    public static boolean isArrowKey(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_DOWN:
                return true;
        }
        return false;
    }
    public static int showDialog(String title,String message){
        int rtn = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE, null);
        return rtn;
    }
    /**
     * 与showDialog相比，这个方法不提供选项，它与displayMessageInEDT()类似是用来显示信息的。但与displayMessageInEDT()
     * 不同的是，这个方法在用户按下按钮前，程序不会继续下去，而displayMessageInEDT()一弹出窗口，就继续执行下面的代码。-- XFZ@2010-11-18
     * @param message
     * @return
     */
    public static void showDialogAndWaitInEDT(final String message){
        if ( SwingUtilities.isEventDispatchThread()){
            showDialogAndWait("Info",message);
        }else{
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    showDialogAndWait("Info",message);
                }
            };
            try {
                SwingUtilities.invokeAndWait(r);
            } catch (Exception e) {
                Utils.log(e);
            }
        }
    }
    /**
     * 与showDialog相比，这个方法不提供选项，它与displayMessageInEDT()类似是用来显示信息的。但与displayMessageInEDT()
     * 不同的是，这个方法在用户按下按钮前，程序不会继续下去，而displayMessageInEDT()一弹出窗口，就继续执行下面的代码。-- XFZ@2010-11-18
     * @param message
     * @return
     */
    public static int showDialogAndWait(String message){
        return showDialogAndWait("Info",message);
    }
    /**
     * 与showDialog相比，这个方法不提供选项，它与displayMessageInEDT()类似是用来显示信息的。但与displayMessageInEDT()
     * 不同的是，这个方法在用户按下按钮前，程序不会继续下去，而displayMessageInEDT()一弹出窗口，就继续执行下面的代码。-- XFZ@2010-11-18
     * @param message
     * @return
     */
    public static int showDialogAndWait(String title,String message){
        return JOptionPane.showConfirmDialog(null,message,title, JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE,null);
    }
    public static void beep() {
        Toolkit.getDefaultToolkit().beep();
    }
    public static int getStringWidth(Font font, String str){
        return getStringWidth(font,str,component_for_calculate_string_size);
    }
    /**
     * 计算以font字体的字符串str的长度
     * @param font
     * @param str
     * @return
     */
    public static int getStringWidth(Font font, String str,JComponent jcomp ){
        FontMetrics fm = SwingUtilities2.getFontMetrics(jcomp, font);
        int string_width = fm.stringWidth(str);
        return string_width;
    }
    public static boolean isSpecialKeyDown(MouseEvent e){
        if ( e == null) {
            return false;
        }else if ( e.isControlDown() || e.isAltDown() || e.isShiftDown()){
            return true;
        }else{
            return false;
        }
    }
    private static void formatString(Font font, String str, int max_width, List<String> stringArray){
        if ( str.indexOf("<br>")>=0 || str.indexOf("<BR>")>=0){
            formatHtmlString(str,stringArray);
            return;
        }

        StringBuilder sb = new StringBuilder(str);
        StringBuilder string_over_length = new StringBuilder();
        int str_width = getStringWidth(font,str);
        while ( str_width > max_width) {
            //删除最后一个字符，并放入string_over_length
            int last_character_index = sb.length()-1;
            if(last_character_index == 0){
                break;
            }
            String last_string = sb.substring(last_character_index, last_character_index+1);
            sb.deleteCharAt(last_character_index);
            string_over_length.insert(0, last_string);

            str_width = getStringWidth(font,sb.toString());
        }

        //现在sb中的字符串满足条件（即小于max_width），放入stringArray
        stringArray.add(sb.toString());
        //再对剩下的字符串做同样的处理
        if ( string_over_length.length() > 0){
            formatString(font, string_over_length.toString(), max_width, stringArray);
        }
    }
    private static void formatHtmlString(String str, List<String>  stringArray){
        //将<BR>转换成<br>
        str = Utils.replaceStr(str, "<BR>", "<br>");
        String pointer = str;
        int index_of_br=-1;
        while (  (index_of_br = pointer.indexOf("<br>")) >=0 ){
            String sub_string = pointer.substring(0, index_of_br);
            stringArray.add(sub_string);
            pointer = pointer.substring(index_of_br+"<br>".length());
        }
    }
    public static int getScreenHeight(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.height;
    }
}
