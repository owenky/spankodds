package com.sia.client.ui;

import com.sia.client.config.Config;
import com.sia.client.config.SiaConst.SportName;
import com.sia.client.config.Utils;
import com.sia.client.model.LineData;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.SpankyWindowConfig;
import com.sia.client.model.UserDisplaySettings;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

import static com.sia.client.config.Utils.log;

public class LinePanel extends JPanel {
    // 183 52 235
    private boolean useArrowIcons = true;
    private static int topthickness = 2;
    private static int bottomthickness = 2;
    private static int rightthickness = 1;
    private static int leftthickness = 1;
    private static final MatteBorder scalpbordertop = new MatteBorder(2, 2, 0, 2, new Color(0 ,102, 0));
    private static final MatteBorder scalpborderbottom = new MatteBorder(0, 2, 2, 2, new Color(0 ,102, 0));
    private static final MatteBorder scalpborder = new MatteBorder(2, 2, 2, 2, new Color(0 ,102, 0));
    private static final MatteBorder bestvisitborder = new MatteBorder(topthickness, leftthickness, bottomthickness, rightthickness, new Color(51,0, 0));
    private static final MatteBorder besthomeborder = new MatteBorder(topthickness, leftthickness, bottomthickness, rightthickness, new Color(51, 0, 0));
    private static final MatteBorder bestoverborder = new MatteBorder(topthickness, leftthickness, bottomthickness, rightthickness, new Color(51 ,0, 0));
    private static final MatteBorder bestunderborder = new MatteBorder(topthickness, leftthickness, bottomthickness, rightthickness, new Color(51 ,0, 0));
    private static final MatteBorder bestvisitborderblackorred = new MatteBorder(topthickness, leftthickness, bottomthickness, rightthickness, new Color(255,255, 0));
    private static final MatteBorder besthomeborderblackorred = new MatteBorder(topthickness, leftthickness, bottomthickness, rightthickness, new Color(255,255, 0));
    private static final MatteBorder bestoverborderblackorred = new MatteBorder(topthickness, leftthickness, bottomthickness, rightthickness, new Color(255,255, 0));
    private static final MatteBorder bestunderborderblackorred = new MatteBorder(topthickness, leftthickness, bottomthickness, rightthickness, new Color(255,255, 0));
    private static final MatteBorder bestdrawborder = new MatteBorder(2, 2, 2, 2, new Color(255, 255, 0));
    private static final MatteBorder bestallborder = new MatteBorder(2, 2, 2, 2, new Color(222, 235, 52));
  //  private static final Color altcolor = new Color(204, 255, 229);
  private static final Color altcolor = new Color(215, 215, 215);
    private static final Color openercolor = Color.LIGHT_GRAY;
    private final static int leftPaddingSpace = 0;
    private final static int rightPaddingSpace = 0;
    public static String ICON_BLANK = "blank2.gif";
    private boolean testprint = false;
    final JLabel top;
    final JLabel bottom;
    final JLabel draw;
    final JLabel total;
    final JComponent topPanel;
    final JComponent bottomPanel;
    final JComponent drawPanel;
    final JComponent totalPanel;
    private final UserDisplaySettings userDisplaySettings = Config.instance().getUserDisplaySettings();


    public LinePanel() {
        this(null);

    }
    public LinePanel(String name) {
        this(SwingConstants.RIGHT);
        if ( null == name) {
            setLayout(new GridLayout(2, 0));
            add(topPanel);
            add(bottomPanel);
        } else {
            setLayout(new GridLayout(4, 0));
            add(topPanel);
            add(bottomPanel);
            add(drawPanel);
            add(totalPanel);
        }
        setName(name);
    }
    private LinePanel(int textAlignment) {

        top = new JLabel("", Utils.getImageIcon(ICON_BLANK), SwingConstants.LEADING);
        bottom = new JLabel("", Utils.getImageIcon(ICON_BLANK), SwingConstants.LEADING);
        draw = new JLabel("", Utils.getImageIcon(ICON_BLANK), SwingConstants.LEADING);
        total = new JLabel("", Utils.getImageIcon(ICON_BLANK), SwingConstants.LEADING);

        topPanel = createContainingComponent(top,leftPaddingSpace,rightPaddingSpace);
        bottomPanel = createContainingComponent(bottom,leftPaddingSpace,rightPaddingSpace);
        drawPanel = createContainingComponent(draw,leftPaddingSpace,rightPaddingSpace);
        totalPanel = createContainingComponent(total,leftPaddingSpace,rightPaddingSpace);
        //setToolTipText("linepaneltooltiptext");
//        Font myfont = new Font("Arial", Font.PLAIN, 12);
        Font myfont = Config.instance().getFontConfig().getSelectedFont().deriveFont(Font.PLAIN);
        top.setFont(myfont);
        bottom.setFont(myfont);
        draw.setFont(myfont);
        total.setFont(myfont);

        top.setOpaque(true);
        bottom.setOpaque(true);
        draw.setOpaque(true);
        total.setOpaque(true);

        top.setHorizontalTextPosition(textAlignment);
        bottom.setHorizontalTextPosition(textAlignment);
        draw.setHorizontalTextPosition(textAlignment);
        total.setHorizontalTextPosition(textAlignment);

        this.setOpaque(true);










    }
    public void setSoccerLines(JTable table,SoccerSpreadTotalView stv, int row, int col) {

        SpankyWindowConfig spankyWindowConfig = ((MainGameTableModel)table.getModel()).getSpankyWindowConfig();
        LineData[] boxes;
        try {
            stv.setDisplayType(spankyWindowConfig.getDisplay());
            stv.setPeriodType(spankyWindowConfig.getPeriod());

            if (spankyWindowConfig.isShowingPrior()) {
                boxes = stv.getPriorBoxes();
                testprint = stv.gid == 6829 && stv.bid == 204;
            } else if (spankyWindowConfig.isShowingOpener()) {
                boxes = stv.getOpenerBoxes();

            } else {
                boxes = stv.getCurrentBoxes();
            }
            setTop(table,boxes[0], row, col);
            setBottom(table,boxes[1], row, col);
            setDraw(table,boxes[2], row, col);
            setTotal(table,boxes[3], row, col);

        } catch (Exception ex) {
            log("Error=" + "..." + stv + "..row=" + row + "...col=" + col);
            log(ex);
        }
    }

    public void setTop(JTable table,LineData ld, int row, int col) {
        boolean blackorred = false;
        try {

            top.setText(ld.getData());
            String bookie = table.getColumnModel().getColumn(col).getHeaderValue().toString();

            Integer bookieid = AppController.getBookieId(bookie);
            Color colcolor = AppController.getColor(bookieid);

            Color bgcolor = ld.getBackgroundColor();
            Color fgcolor = Color.BLACK;

            if (ld.getData().equals("") && bgcolor != openercolor) {
                bgcolor = Color.WHITE;
            }

            if (bgcolor != Color.WHITE && bgcolor != altcolor && bgcolor != openercolor) {
                blackorred = true;
                top.setIcon(Utils.getImageIcon(ld.getIconPath()));
                fgcolor = Color.WHITE;
            } else {
                if (ld.getData().equals("")) {
                    top.setIcon(null);
                    top.setText("");
                } else {
                    top.setIcon(null);
                }

            }

            if (row % 2 == 0 && bgcolor == Color.WHITE) {
                bgcolor = altcolor;
            }
            if (colcolor != null && !blackorred) {
                bgcolor = colcolor;
            }

            topPanel.setBackground(bgcolor);
            top.setBackground(bgcolor);
            top.setForeground(fgcolor);

            if (testprint) {
                log("reached end of top..." + row + "src/main" + col + ".." + top.getText());
            }
        } catch (Exception ex) {
            log(ex);
        }

        if (ld.getBorder().contains("bestvisit")) {
            if (ld.getBorder().contains("besthome")) {
                top.setBorder(bestallborder);
            } else {

                if(blackorred) {  top.setBorder(bestvisitborderblackorred);}
                else { top.setBorder(bestvisitborder); }


            }
        } else if (ld.getBorder().contains("besthome")) {
            top.setBorder(besthomeborder);

        } else if (ld.getBorder().contains("bestover")) {
            if (ld.getBorder().contains("bestunder")) {
                top.setBorder(bestallborder);
            } else {
                if(blackorred) {  top.setBorder(bestoverborderblackorred);}
                else { top.setBorder(bestoverborder); }
            }

        } else if (ld.getBorder().contains("bestunder")) {
            if(blackorred) {  top.setBorder(bestunderborderblackorred);}
            else { top.setBorder(bestunderborder); }

        }

        else {
            top.setBorder(null);
        }

        if(!userDisplaySettings.getShowborderbestline())
        {
            top.setBorder(null);
        }
        if (ld.getBorder().contains("scalp"))
        {
            top.setBorder(scalpborder);
        }

        top.setToolTipText(ld.getTooltip());
        if(!userDisplaySettings.getShowdirectionicons())
        {
            top.setIcon(null);
        }
    }

    public void setBottom(JTable table,LineData ld, int row, int col) {

        boolean blackorred = false;
        bottom.setText(ld.getData());

        String bookie = table.getColumnModel().getColumn(col).getHeaderValue().toString();

        Integer bookieid = AppController.getBookieId(bookie);
        Color colcolor = AppController.getColor(bookieid);

        Color bgcolor = ld.getBackgroundColor();
        Color fgcolor = Color.BLACK;

        if (ld.getData().equals("") && bgcolor != openercolor) {
            bgcolor = Color.WHITE;
        }

        if (bgcolor != Color.WHITE && bgcolor != altcolor && bgcolor != openercolor) {
            blackorred = true;
            fgcolor = Color.WHITE;
            bottom.setIcon(Utils.getImageIcon(ld.getIconPath()));
        } else {
            if (ld.getData().equals("")) {
                bottom.setIcon(null);
                bottom.setText("");
            } else {
                bottom.setIcon(null);
            }

        }
        if (row % 2 == 0 && bgcolor == Color.WHITE) {
            bgcolor = altcolor;
        }

        if (colcolor != null && !blackorred) {
            bgcolor = colcolor;
        }

        bottomPanel.setBackground(bgcolor);
        bottom.setBackground(bgcolor);
        bottom.setForeground(fgcolor);

        if (ld.getBorder().contains("bestvisit")) {
            if (ld.getBorder().contains("besthome")) {
                bottom.setBorder(bestallborder);
            } else {
                if(blackorred) {  bottom.setBorder(bestvisitborderblackorred);}
                else { bottom.setBorder(bestvisitborder); }
            }
        } else if (ld.getBorder().contains("besthome")) {
            if(blackorred) {  bottom.setBorder(besthomeborderblackorred);}
            else { bottom.setBorder(besthomeborder); }
        } else if (ld.getBorder().contains("bestover")) {
            if (ld.getBorder().contains("bestunder")) {
                bottom.setBorder(bestallborder);
            } else {
                if(blackorred) {  bottom.setBorder(bestoverborderblackorred);}
                else { bottom.setBorder(bestoverborder); }
            }

        } else if (ld.getBorder().contains("bestunder")) {
            if(blackorred) {  bottom.setBorder(bestunderborderblackorred);}
            else { bottom.setBorder(bestunderborder); }

        }
        else {
            bottom.setBorder(null);
        }

        if(!userDisplaySettings.getShowborderbestline())
        {
            bottom.setBorder(null);
        }
        if (ld.getBorder().contains("scalp"))
        {
            bottom.setBorder(scalpborder);
        }


        bottom.setToolTipText(ld.getTooltip());
        if(!userDisplaySettings.getShowdirectionicons())
        {
            bottom.setIcon(null);
        }
    }

    private void setDraw(JTable table,LineData ld, int row, int col) {

        boolean blackorred = false;
        draw.setText(ld.getData());

        String bookie = table.getColumnModel().getColumn(col).getHeaderValue().toString();

        Integer bookieid = AppController.getBookieId(bookie);
        Color colcolor = AppController.getColor(bookieid);

        Color bgcolor = ld.getBackgroundColor();
        Color fgcolor = Color.BLACK;

        if (ld.getData().equals("") && bgcolor != openercolor) {
            bgcolor = Color.WHITE;
        }

        if (bgcolor != Color.WHITE && bgcolor != altcolor && bgcolor != openercolor) {
            blackorred = true;
            fgcolor = Color.WHITE;
            draw.setIcon(Utils.getImageIcon(ld.getIconPath()));
        } else {

            if (ld.getData().equals("")) {
                draw.setIcon(null);
                draw.setText("");
            } else {
                draw.setIcon(null);
            }

        }
        if (row % 2 == 0 && bgcolor == Color.WHITE) {
            bgcolor = altcolor;
        }

        if (colcolor != null && !blackorred) {
            bgcolor = colcolor;
        }

        drawPanel.setBackground(bgcolor);
        draw.setBackground(bgcolor);
        draw.setForeground(fgcolor);

        if (ld.getBorder().contains("bestvisit")) {
            if (ld.getBorder().contains("besthome")) {
                draw.setBorder(bestallborder);
            } else {
                if(blackorred) {  draw.setBorder(bestvisitborderblackorred);}
                else { draw.setBorder(bestvisitborder); }
            }
        } else if (ld.getBorder().contains("besthome")) {
            if(blackorred) {  draw.setBorder(besthomeborderblackorred);}
            else { draw.setBorder(besthomeborder); }
        } else if (ld.getBorder().contains("bestover")) {
            if (ld.getBorder().contains("bestunder")) {
                draw.setBorder(bestallborder);
            } else {
                if(blackorred) {  draw.setBorder(bestoverborderblackorred);}
                else { draw.setBorder(bestoverborder); }
            }

        } else if (ld.getBorder().contains("bestunder")) {
            if(blackorred) {  draw.setBorder(bestunderborderblackorred);}
            else { draw.setBorder(bestunderborder); }

        } else if (ld.getBorder().contains("bestdraw")) {
            draw.setBorder(bestdrawborder);

        } else {
            draw.setBorder(null);
        }

        if(!userDisplaySettings.getShowborderbestline())
        {
            draw.setBorder(null);
        }



        if(!userDisplaySettings.getShowdirectionicons())
        {
            draw.setIcon(null);
        }

    }

    private void setTotal(JTable table,LineData ld, int row, int col) {

        boolean blackorred = false;
        total.setText(ld.getData());

        String bookie = table.getColumnModel().getColumn(col).getHeaderValue().toString();

        Integer bookieid = AppController.getBookieId(bookie);
        Color colcolor = AppController.getColor(bookieid);

        Color bgcolor = ld.getBackgroundColor();
        Color fgcolor = Color.BLACK;

        if (ld.getData().equals("") && bgcolor != openercolor) {
            bgcolor = Color.WHITE;
        }

        if (bgcolor != Color.WHITE && bgcolor != altcolor && bgcolor != openercolor) {
            blackorred = true;
            fgcolor = Color.WHITE;
            total.setIcon(Utils.getImageIcon(ld.getIconPath()));
        } else {
            if (ld.getData().equals("")) {
                total.setIcon(null);
                total.setText("");
            } else {
                total.setIcon(null);
            }

        }
        if (row % 2 == 0 && bgcolor == Color.WHITE) {
            bgcolor = altcolor;
        }

        if (colcolor != null && !blackorred) {
            bgcolor = colcolor;
        }

        totalPanel.setBackground(bgcolor);
        total.setBackground(bgcolor);
        total.setForeground(fgcolor);

        if (ld.getBorder().contains("bestvisit")) {
            if (ld.getBorder().contains("besthome")) {
                total.setBorder(bestallborder);
            } else {
                if(blackorred) {  total.setBorder(bestvisitborderblackorred);}
                else { total.setBorder(bestvisitborder); }


            }
        } else if (ld.getBorder().contains("besthome")) {
            if(blackorred) {  total.setBorder(besthomeborderblackorred);}
            else { total.setBorder(besthomeborder); }
        } else if (ld.getBorder().contains("bestover")) {
            if (ld.getBorder().contains("bestunder")) {
                total.setBorder(bestallborder);
            } else {
                if(blackorred) {  total.setBorder(bestoverborderblackorred);}
                else { total.setBorder(bestoverborder); }
            }

        } else if (ld.getBorder().contains("bestunder")) {
            if(blackorred) {  total.setBorder(bestunderborderblackorred);}
            else { total.setBorder(bestunderborder); }
        }
          else {
            total.setBorder(null);
        }


        if(!userDisplaySettings.getShowborderbestline())
        {
            total.setBorder(null);
        }

        if (ld.getBorder().contains("scalp"))
        {
            total.setBorder(scalpborder);
        }

        if(!userDisplaySettings.getShowdirectionicons())
        {
            total.setIcon(null);
        }

    }

    public void setLines(JTable table,SpreadTotalView stv, int row, int col) {
        LineData[] boxes;
        try {
            boolean bothscalps = false;
            SpankyWindowConfig spankyWindowConfig = ((MainGameTableModel)table.getModel()).getSpankyWindowConfig();
            stv.setDisplayType(spankyWindowConfig.getDisplay());
            stv.setPeriodType(spankyWindowConfig.getPeriod());

            if (spankyWindowConfig.isShowingPrior()) {
                boxes = stv.getPriorBoxes();
                testprint = stv.gid == 6829 && stv.bid == 204;
            } else if (spankyWindowConfig.isShowingOpener()) {
                boxes = stv.getOpenerBoxes();
            } else {
                boxes = stv.getCurrentBoxes();
            }
            if(boxes[0].getBorder().equals(boxes[1].getBorder()) && boxes[0].getBorder().equals("scalp"))
            {
                bothscalps = true;
                boxes[0].setBorder("");
                boxes[1].setBorder("");
            }
            setTop(table,boxes[0], row, col);
            setBottom(table,boxes[1], row, col);
            if(bothscalps)
            {
                top.setBorder(scalpbordertop);
                bottom.setBorder(scalpborderbottom);
            }

        } catch (Exception ex) {
            log("ERROR=..." + stv + "..row=" + row + "...col=" + col);
            log(ex);
            ex.printStackTrace();
        }

    }

    public void setTeams(JTable table,TeamView stv, int row, int col) {

        LineData[] boxes = stv.getCurrentBoxes();
        setTop(table,boxes[0], row, col);
        setBottom(table,boxes[1], row, col);
        String display = "";
        if (isSoccer()) {
            try {
                SpankyWindowConfig spankyWindowConfig = ((MainGameTableModel)table.getModel()).getSpankyWindowConfig();
                display = spankyWindowConfig.getDisplay();
            } catch (Exception ex) {
                log(ex);
            }
            if (display.equalsIgnoreCase("SpreadTotal")) {
                LineData draw = new LineData(ICON_BLANK, "Over", Color.WHITE);
                LineData total = new LineData(ICON_BLANK, "Under", Color.WHITE);
                setDraw(table,draw, row, col);
                setTotal(table,total, row, col);
            } else {
                LineData draw = new LineData(ICON_BLANK, "Draw", Color.WHITE);
                LineData total = new LineData(ICON_BLANK, "Total", Color.WHITE);
                setDraw(table,draw, row, col);
                setTotal(table,total, row, col);
            }


        }
//
//        String tooltip = stv.getToolTip();
//        if (tooltip != null && !tooltip.equals("") && !tooltip.equalsIgnoreCase("null")) {
//            this.setToolTipText(tooltip);
//        } else {
//            this.setToolTipText(null);
//        }
    }

    private boolean isSoccer() {
        return SportName.Soccer.equalsIgnoreCase(getName());
    }

    public void setTime(JTable table,TimeView stv, int row, int col) {
        LineData[] boxes = stv.getCurrentBoxes();
        setTop(table,boxes[0], row, col);
        setBottom(table,boxes[1], row, col);
        if (isSoccer()) {
            LineData blank = new LineData(ICON_BLANK, "", Color.WHITE);
            setDraw(table,blank, row, col);
            setTotal(table,blank, row, col);
        }
    }

    public void setChart(JTable table,ChartView stv, int row, int col) {
        try {
            SpankyWindowConfig spankyWindowConfig = ((MainGameTableModel)table.getModel()).getSpankyWindowConfig();
            stv.setDisplayType(spankyWindowConfig.getDisplay());
            stv.setPeriodType(spankyWindowConfig.getPeriod());
        } catch (Exception ex) {
            log(ex);
        }


        LineData[] boxes = stv.getCurrentBoxes();
        setTop(table,boxes[0], row, col);
        setBottom(table,boxes[1], row, col);
        if (isSoccer()) {
            LineData blank = new LineData(ICON_BLANK, "", Color.WHITE);
            setDraw(table,blank, row, col);
            setTotal(table,blank, row, col);
        }
    }

    public void setSoccerChart(JTable table,SoccerChartView stv, int row, int col) {
        try {
            SpankyWindowConfig spankyWindowConfig = ((MainGameTableModel)table.getModel()).getSpankyWindowConfig();
            stv.setDisplayType(spankyWindowConfig.getDisplay());
            stv.setPeriodType(spankyWindowConfig.getPeriod());
        } catch (Exception ex) {
            log(ex);
        }

        LineData[] boxes = stv.getCurrentBoxes();
        setTop(table,boxes[0], row, col);
        setBottom(table,boxes[1], row, col);
        setDraw(table,boxes[2], row, col);
        setTotal(table,boxes[3], row, col);
    }

    public void setHeader(JTable table,HeaderView stv, int row, int col) {
        LineData[] boxes = stv.getCurrentBoxes();
        setTop(table,boxes[0], row, col);
        setBottom(table,boxes[1], row, col);
    }

    public void setGamenumbers(JTable table,GameNumberView stv, int row, int col) {
        LineData[] boxes = stv.getCurrentBoxes();
        setTop(table,boxes[0], row, col);
        setBottom(table,boxes[1], row, col);
    }

    public void setSoccerGamenumbers(JTable table,SoccerGameNumberView stv, int row, int col) {
        LineData[] boxes = stv.getCurrentBoxes();
        setTop(table,boxes[0], row, col);
        setBottom(table,boxes[1], row, col);
        setDraw(table,boxes[2], row, col);
        setTotal(table,boxes[3], row, col);
    }

    public void setInfo(JTable table,InfoView stv, int row, int col) {
        LineData[] boxes = stv.getCurrentBoxes();
        setTop(table,boxes[0], row, col);
        setBottom(table,boxes[1], row, col);
        if (isSoccer()) {
            LineData blank = new LineData(ICON_BLANK, "", Color.WHITE);
            setDraw(table,blank, row, col);
            setTotal(table,blank, row, col);
        }

    }
    private static JComponent createContainingComponent(JComponent comp,int leftPadding, int rightPadding) {
        JPanel containingPanel = new JPanel();
        Dimension leftPaddingDim = new Dimension(leftPadding,0);
        Dimension rightPaddingDim = new Dimension(rightPadding,0);

        containingPanel.setLayout(new BorderLayout());
        JPanel leftPaddingPanel = new JPanel();
        JPanel rightPaddingPanel = new JPanel();
        leftPaddingPanel.setOpaque(false);
        rightPaddingPanel.setOpaque(false);

        leftPaddingPanel.setPreferredSize(leftPaddingDim);
        leftPaddingPanel.setMaximumSize(leftPaddingDim);
        rightPaddingPanel.setPreferredSize(rightPaddingDim);
        rightPaddingPanel.setMaximumSize(rightPaddingDim);

        containingPanel.add(leftPaddingPanel,BorderLayout.WEST);
        containingPanel.add(comp,BorderLayout.CENTER);
        containingPanel.add(rightPaddingPanel,BorderLayout.EAST);

        return containingPanel;
    }
}