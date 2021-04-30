package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.LineData;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import static com.sia.client.config.Utils.log;

public class LinePanel extends JPanel {
    public static ImageIcon ICON_BLANK = new ImageIcon(Utils.getMediaResource("blank2.gif"));
    protected MainGameTable table;
    Color altcolor = new Color(204, 255, 229);
    String name = "";
    Color openercolor = Color.LIGHT_GRAY;
    boolean testprint = false;
    MatteBorder bestvisitborder = new MatteBorder(1, 1, 1, 1, new Color(103, 52, 235));
    MatteBorder besthomeborder = new MatteBorder(1, 1, 1, 1, new Color(103, 52, 235));
    MatteBorder bestoverborder = new MatteBorder(1, 1, 1, 1, new Color(103, 52, 235));
    MatteBorder bestunderborder = new MatteBorder(1, 1, 1, 1, new Color(103, 52, 235));
    MatteBorder bestdrawborder = new MatteBorder(1, 1, 1, 1, new Color(183, 52, 235));
    MatteBorder bestallborder = new MatteBorder(1, 1, 1, 1, new Color(222, 235, 52));
    private JLabel top;
    private JLabel bottom;
    private JLabel draw;
    private JLabel total;

    public LinePanel() {
        super(new GridLayout(2, 0));
        top = new JLabel("", ICON_BLANK, SwingConstants.LEADING);
        top.setHorizontalTextPosition(SwingConstants.RIGHT);
        bottom = new JLabel("", ICON_BLANK, SwingConstants.LEADING);
        bottom.setHorizontalTextPosition(SwingConstants.RIGHT);
        draw = new JLabel("", ICON_BLANK, SwingConstants.LEADING);
        draw.setHorizontalTextPosition(SwingConstants.RIGHT);
        total = new JLabel("", ICON_BLANK, SwingConstants.LEADING);
        total.setHorizontalTextPosition(SwingConstants.RIGHT);
        Font myfont = new Font("Arial", Font.PLAIN, 12);
        top.setFont(myfont);
        bottom.setFont(myfont);
        draw.setFont(myfont);
        total.setFont(myfont);
        top.setOpaque(true);
        bottom.setOpaque(true);
        draw.setOpaque(true);
        total.setOpaque(true);
        this.setOpaque(true);
        add(top);
        add(bottom);


        ToolTipManager.sharedInstance().setInitialDelay(5000);
        ToolTipManager.sharedInstance().setDismissDelay(60000);
    }

    public LinePanel(String name) {
        super(new GridLayout(4, 0));
        this.name = name;
        top = new JLabel("", ICON_BLANK, SwingConstants.LEADING);
        top.setHorizontalTextPosition(SwingConstants.RIGHT);

        bottom = new JLabel("", ICON_BLANK, SwingConstants.LEADING);
        bottom.setHorizontalTextPosition(SwingConstants.RIGHT);

        draw = new JLabel("", ICON_BLANK, SwingConstants.LEADING);
        draw.setHorizontalTextPosition(SwingConstants.RIGHT);

        total = new JLabel("", ICON_BLANK, SwingConstants.LEADING);
        total.setHorizontalTextPosition(SwingConstants.RIGHT);

        Font myfont = new Font("Arial", Font.PLAIN, 12);
        top.setFont(myfont);
        bottom.setFont(myfont);
        draw.setFont(myfont);
        total.setFont(myfont);

        top.setOpaque(true);
        bottom.setOpaque(true);
        draw.setOpaque(true);
        total.setOpaque(true);
        this.setOpaque(true);
        add(top);
        add(bottom);

        add(draw);
        add(total);


        ToolTipManager.sharedInstance().setInitialDelay(5000);
        ToolTipManager.sharedInstance().setDismissDelay(60000);
    }
    public void setSoccerLines(SoccerSpreadTotalView stv, int row, int col) {

        top.setIconTextGap(1);
        bottom.setIconTextGap(1);
        draw.setIconTextGap(1);
        total.setIconTextGap(1);
        top.setHorizontalAlignment(SwingConstants.RIGHT);
        bottom.setHorizontalAlignment(SwingConstants.RIGHT);
        draw.setHorizontalAlignment(SwingConstants.RIGHT);
        total.setHorizontalAlignment(SwingConstants.RIGHT);
        LineData[] boxes;
        try {

            LinesTableData ltd = table.getLinesTableData(row);

            stv.setDisplayType(ltd.getDisplayType());
            stv.setPeriodType(ltd.getPeriodType());

            if (ltd.isShowingPrior()) {
                boxes = stv.getPriorBoxes();
                if (stv.gid == 6829 && stv.bid == 204) {
                    testprint = true;
                } else {
                    testprint = false;
                }
            } else if (ltd.isShowingOpener()) {
                boxes = stv.getOpenerBoxes();

            } else {

                boxes = stv.getCurrentBoxes();

            }
            String tooltip = stv.getToolTip();

            if (tooltip != null && !tooltip.equals("") && !tooltip.equalsIgnoreCase("null")) {
                this.setToolTipText(tooltip);
            } else {
                this.setToolTipText(null);
            }
            setTop(boxes[0], row, col);

            setBottom(boxes[1], row, col);

            setDraw(boxes[2], row, col);

            setTotal(boxes[3], row, col);

        } catch (Exception ex) {
            log("Error=" + "..." + stv + "..row=" + row + "...col=" + col);
            log(ex);
        }
    }

    public void setTop(LineData ld, int row, int col) {
        try {
            boolean blackorred = false;
            top.setText(ld.getData());
            String bookie = table.getColumnModel().getColumn(col).getHeaderValue().toString();

            String bookieid = AppController.getBookieId(bookie);
            Color colcolor = (Color) AppController.getBookieColors().get(bookieid);
            //System.out.println("bookie-color="+bookie+".."+bookieid+".."+colcolor);

            Color bgcolor = ld.getBackgroundColor();
            Color fgcolor = Color.BLACK;

            if (ld.getData().equals("") && bgcolor != openercolor) {
                bgcolor = Color.WHITE;
            }

            if (bgcolor != Color.WHITE && bgcolor != altcolor && bgcolor != openercolor) {
                blackorred = true;
                top.setIcon(ld.getIcon());
                fgcolor = Color.WHITE;
            } else {
                if (ld.getData().equals("")) {
                    top.setIcon(null);
                    top.setText("");
                } else {
                    top.setIcon(null);
                }

            }

            if (row % 2 == 1 && bgcolor == Color.WHITE) {
                bgcolor = altcolor;
            }
            if (colcolor != null && !blackorred) {
                bgcolor = colcolor;
            }

            top.setBackground(bgcolor);
            top.setForeground(fgcolor);

            if (testprint) {
                log("reached end of top..." + row + "src/main" + col + ".." + top.getText());
            }
        } catch (Exception ex) {
            log(ex);
        }

        if (ld.getBorder().indexOf("bestvisit") != -1) {
            if (ld.getBorder().indexOf("besthome") != -1) {
                top.setBorder(bestallborder);
            } else {
                top.setBorder(bestvisitborder);

            }
        } else if (ld.getBorder().indexOf("besthome") != -1) {
            top.setBorder(besthomeborder);

        } else if (ld.getBorder().indexOf("bestover") != -1) {
            if (ld.getBorder().indexOf("bestunder") != -1) {
                top.setBorder(bestallborder);
            } else {
                top.setBorder(bestoverborder);
            }

        } else if (ld.getBorder().indexOf("bestunder") != -1) {
            top.setBorder(bestunderborder);

        } else {
            top.setBorder(null);
        }


    }
    public void setBottom(LineData ld, int row, int col) {

        boolean blackorred = false;
        bottom.setText(ld.getData());

        String bookie = table.getColumnModel().getColumn(col).getHeaderValue().toString();

        String bookieid = (String) AppController.getBookieId(bookie);
        Color colcolor = (Color) AppController.getBookieColors().get(bookieid);
        //System.out.println("bookie-color="+bookie+".."+colcolor);

        Color bgcolor = ld.getBackgroundColor();
        Color fgcolor = Color.BLACK;

        if (ld.getData().equals("") && bgcolor != openercolor) {
            bgcolor = Color.WHITE;
        }

        if (bgcolor != Color.WHITE && bgcolor != altcolor && bgcolor != openercolor) {
            blackorred = true;
            fgcolor = Color.WHITE;
            bottom.setIcon(ld.getIcon());
        } else {
            if (ld.getData().equals("")) {
                bottom.setIcon(null);
                bottom.setText("");
            } else {
                bottom.setIcon(null);
            }

        }
        if (row % 2 == 1 && bgcolor == Color.WHITE) {
            bgcolor = altcolor;
        }

        if (colcolor != null && !blackorred) {
            bgcolor = colcolor;
        }

        bottom.setBackground(bgcolor);
        bottom.setForeground(fgcolor);

        if (ld.getBorder().indexOf("bestvisit") != -1) {
            if (ld.getBorder().indexOf("besthome") != -1) {
                bottom.setBorder(bestallborder);
            } else {
                bottom.setBorder(bestvisitborder);
            }
        } else if (ld.getBorder().indexOf("besthome") != -1) {
            bottom.setBorder(besthomeborder);
        } else if (ld.getBorder().indexOf("bestover") != -1) {
            if (ld.getBorder().indexOf("bestunder") != -1) {
                bottom.setBorder(bestallborder);
            } else {
                bottom.setBorder(bestoverborder);
            }

        } else if (ld.getBorder().indexOf("bestunder") != -1) {
            bottom.setBorder(bestunderborder);

        } else {
            bottom.setBorder(null);
        }


    }

    public void setDraw(LineData ld, int row, int col) {

        boolean blackorred = false;
        draw.setText(ld.getData());

        String bookie = table.getColumnModel().getColumn(col).getHeaderValue().toString();

        String bookieid = AppController.getBookieId(bookie);
        Color colcolor = (Color) AppController.getBookieColors().get(bookieid);

        Color bgcolor = ld.getBackgroundColor();
        Color fgcolor = Color.BLACK;

        if (ld.getData().equals("") && bgcolor != openercolor) {
            bgcolor = Color.WHITE;
        }

        if (bgcolor != Color.WHITE && bgcolor != altcolor && bgcolor != openercolor) {
            blackorred = true;
            fgcolor = Color.WHITE;
            draw.setIcon(ld.getIcon());
        } else {

            if (ld.getData().equals("")) {
                draw.setIcon(null);
                draw.setText("");
            } else {
                draw.setIcon(null);
            }

        }
        if (row % 2 == 1 && bgcolor == Color.WHITE) {
            bgcolor = altcolor;
        }

        if (colcolor != null && !blackorred) {
            bgcolor = colcolor;
        }

        draw.setBackground(bgcolor);
        draw.setForeground(fgcolor);

        if (ld.getBorder().indexOf("bestvisit") != -1) {
            if (ld.getBorder().indexOf("besthome") != -1) {
                draw.setBorder(bestallborder);
            } else {
                draw.setBorder(bestvisitborder);
            }
        } else if (ld.getBorder().indexOf("besthome") != -1) {
            draw.setBorder(besthomeborder);
        } else if (ld.getBorder().indexOf("bestover") != -1) {
            if (ld.getBorder().indexOf("bestunder") != -1) {
                draw.setBorder(bestallborder);
            } else {
                draw.setBorder(bestoverborder);
            }

        } else if (ld.getBorder().indexOf("bestunder") != -1) {
            draw.setBorder(bestunderborder);

        } else if (ld.getBorder().indexOf("bestdraw") != -1) {
            draw.setBorder(bestdrawborder);

        } else {
            draw.setBorder(null);
        }
    }

    public void setTotal(LineData ld, int row, int col) {

        boolean blackorred = false;
        total.setText(ld.getData());

        String bookie = table.getColumnModel().getColumn(col).getHeaderValue().toString();

        String bookieid = AppController.getBookieId(bookie);
        Color colcolor = (Color) AppController.getBookieColors().get(bookieid);

        Color bgcolor = ld.getBackgroundColor();
        Color fgcolor = Color.BLACK;

        if (ld.getData().equals("") && bgcolor != openercolor) {
            bgcolor = Color.WHITE;
        }

        if (bgcolor != Color.WHITE && bgcolor != altcolor && bgcolor != openercolor) {
            blackorred = true;
            fgcolor = Color.WHITE;
            total.setIcon(ld.getIcon());
        } else {
            if (ld.getData().equals("")) {
                total.setIcon(null);
                total.setText("");
            } else {
                total.setIcon(null);
            }

        }
        if (row % 2 == 1 && bgcolor == Color.WHITE) {
            bgcolor = altcolor;
        }

        if (colcolor != null && !blackorred) {
            bgcolor = colcolor;
        }

        total.setBackground(bgcolor);
        total.setForeground(fgcolor);

        if (ld.getBorder().indexOf("bestvisit") != -1) {
            if (ld.getBorder().indexOf("besthome") != -1) {
                total.setBorder(bestallborder);
            } else {
                total.setBorder(bestvisitborder);


            }
        } else if (ld.getBorder().indexOf("besthome") != -1) {
            total.setBorder(besthomeborder);
        } else if (ld.getBorder().indexOf("bestover") != -1) {
            if (ld.getBorder().indexOf("bestunder") != -1) {
                total.setBorder(bestallborder);
            } else {
                total.setBorder(bestoverborder);
            }

        } else if (ld.getBorder().indexOf("bestunder") != -1) {
            total.setBorder(bestunderborder);

        } else {
            total.setBorder(null);
        }


    }

    public void setLines(SpreadTotalView stv, int row, int col) {
        top.setIconTextGap(1);
        bottom.setIconTextGap(1);
        top.setHorizontalAlignment(SwingConstants.RIGHT);
        bottom.setHorizontalAlignment(SwingConstants.RIGHT);
        LineData[] boxes;
        try {

            LinesTableData ltd = table.getLinesTableData(row);

            stv.setDisplayType(ltd.getDisplayType());
            stv.setPeriodType(ltd.getPeriodType());

            if (ltd.isShowingPrior()) {
                boxes = stv.getPriorBoxes();
                if (stv.gid == 6829 && stv.bid == 204) {
                    testprint = true;
                } else {
                    testprint = false;
                }
            } else if (ltd.isShowingOpener()) {
                boxes = stv.getOpenerBoxes();
            } else {
                boxes = stv.getCurrentBoxes();
            }


            String tooltip = stv.getToolTip();
            if (tooltip != null && !tooltip.equals("") && !tooltip.equalsIgnoreCase("null")) {
                this.setToolTipText(tooltip);
            } else {
                this.setToolTipText(null);
            }

            setTop(boxes[0], row, col);
            setBottom(boxes[1], row, col);
        } catch (Exception ex) {
            log("ERROR=..." + stv + "..row=" + row + "...col=" + col);
            log(ex);
        }

    }

    public void setTeams(TeamView stv, int row, int col) {

        top.setHorizontalAlignment(SwingConstants.LEFT);
        bottom.setHorizontalAlignment(SwingConstants.LEFT);

        top.setHorizontalTextPosition(SwingConstants.RIGHT);
        bottom.setHorizontalTextPosition(SwingConstants.RIGHT);

        draw.setHorizontalAlignment(SwingConstants.LEFT);
        total.setHorizontalAlignment(SwingConstants.LEFT);
        draw.setHorizontalTextPosition(SwingConstants.RIGHT);
        total.setHorizontalTextPosition(SwingConstants.RIGHT);

        LineData[] boxes = stv.getCurrentBoxes();
        setTop(boxes[0], row, col);
        setBottom(boxes[1], row, col);
        String display = "";
        if ((this.name).equalsIgnoreCase(SiaConst.SoccerStr)) {
            try {
                LinesTableData ltd = table.getLinesTableData(row);
                display = ltd.getDisplayType();
            } catch (Exception ex) {
                log(ex);
            }
            if (display.equalsIgnoreCase("SpreadTotal")) {
                LineData draw = new LineData(ICON_BLANK, "Over", Color.WHITE);
                LineData total = new LineData(ICON_BLANK, "Under", Color.WHITE);
                setDraw(draw, row, col);
                setTotal(total, row, col);
            } else {
                LineData draw = new LineData(ICON_BLANK, "Draw", Color.WHITE);
                LineData total = new LineData(ICON_BLANK, "Total", Color.WHITE);
                setDraw(draw, row, col);
                setTotal(total, row, col);
            }


        }

        String tooltip = stv.getToolTip();
        if (tooltip != null && !tooltip.equals("") && !tooltip.equalsIgnoreCase("null")) {
            this.setToolTipText(tooltip);
        } else {
            this.setToolTipText(null);
        }
    }

    public void setTime(TimeView stv, int row, int col) {

        top.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.setHorizontalAlignment(SwingConstants.CENTER);
        LineData[] boxes = stv.getCurrentBoxes();
        setTop(boxes[0], row, col);
        setBottom(boxes[1], row, col);
        if ((this.name).equalsIgnoreCase(SiaConst.SoccerStr)) {
            LineData blank = new LineData(ICON_BLANK, "", Color.WHITE);
            setDraw(blank, row, col);
            setTotal(blank, row, col);
        }
    }

    public void setChart(ChartView stv, int row, int col) {

        top.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            LinesTableData ltd = table.getLinesTableData(row);

            stv.setDisplayType(ltd.getDisplayType());
            stv.setPeriodType(ltd.getPeriodType());
        } catch (Exception ex) {
            log(ex);
        }


        LineData[] boxes = stv.getCurrentBoxes();
        setTop(boxes[0], row, col);
        setBottom(boxes[1], row, col);
        if ((this.name).equalsIgnoreCase(SiaConst.SoccerStr)) {
            LineData blank = new LineData(ICON_BLANK, "", Color.WHITE);
            setDraw(blank, row, col);
            setTotal(blank, row, col);
        }
    }

    public void setSoccerChart(SoccerChartView stv, int row, int col) {
        top.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.setHorizontalAlignment(SwingConstants.CENTER);
        draw.setHorizontalAlignment(SwingConstants.CENTER);
        total.setHorizontalAlignment(SwingConstants.CENTER);


        try {
            LinesTableData ltd = table.getLinesTableData(row);
            stv.setDisplayType(ltd.getDisplayType());
            stv.setPeriodType(ltd.getPeriodType());
        } catch (Exception ex) {
            log(ex);
        }

        LineData[] boxes = stv.getCurrentBoxes();
        setTop(boxes[0], row, col);
        setBottom(boxes[1], row, col);
        setDraw(boxes[2], row, col);
        setTotal(boxes[3], row, col);
    }

    public void setHeader(HeaderView stv, int row, int col) {
        top.setHorizontalAlignment(SwingConstants.LEFT);
        bottom.setHorizontalAlignment(SwingConstants.LEFT);
        LineData[] boxes = stv.getCurrentBoxes();
        setTop(boxes[0], row, col);
        setBottom(boxes[1], row, col);
    }

    public void setGamenumbers(GameNumberView stv, int row, int col) {
        top.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.setHorizontalAlignment(SwingConstants.CENTER);
        LineData[] boxes = stv.getCurrentBoxes();
        setTop(boxes[0], row, col);
        setBottom(boxes[1], row, col);
    }

    public void setSoccerGamenumbers(SoccerGameNumberView stv, int row, int col) {
        top.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.setHorizontalAlignment(SwingConstants.CENTER);
        LineData[] boxes = stv.getCurrentBoxes();
        setTop(boxes[0], row, col);
        setBottom(boxes[1], row, col);
        setDraw(boxes[2], row, col);
        setTotal(boxes[3], row, col);
    }

    public void setInfo(InfoView stv, int row, int col) {


        top.setHorizontalAlignment(SwingConstants.LEFT);
        top.setBorder(BorderFactory.createMatteBorder(-1, -1, 1, -1, Color.BLACK));
        bottom.setHorizontalAlignment(SwingConstants.LEFT);
        LineData[] boxes = stv.getCurrentBoxes();
        setTop(boxes[0], row, col);
        setBottom(boxes[1], row, col);
        if ((this.name).equalsIgnoreCase(SiaConst.SoccerStr)) {
            LineData blank = new LineData(ICON_BLANK, "", Color.WHITE);
            setDraw(blank, row, col);
            setTotal(blank, row, col);
        }

    }

}