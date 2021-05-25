package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.LineData;
import com.sia.client.model.LinesTableDataSupplier;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import static com.sia.client.config.Utils.log;

public class LinePanel extends JPanel {
    private static final MatteBorder bestvisitborder = new MatteBorder(1, 1, 1, 1, new Color(103, 52, 235));
    private static final MatteBorder besthomeborder = new MatteBorder(1, 1, 1, 1, new Color(103, 52, 235));
    private static final MatteBorder bestoverborder = new MatteBorder(1, 1, 1, 1, new Color(103, 52, 235));
    private static final MatteBorder bestunderborder = new MatteBorder(1, 1, 1, 1, new Color(103, 52, 235));
    private static final MatteBorder bestdrawborder = new MatteBorder(1, 1, 1, 1, new Color(183, 52, 235));
    private static final MatteBorder bestallborder = new MatteBorder(1, 1, 1, 1, new Color(222, 235, 52));
    private static final Color altcolor = new Color(204, 255, 229);
    private static final Color openercolor = Color.LIGHT_GRAY;
    public static ImageIcon ICON_BLANK = new ImageIcon(Utils.getMediaResource("blank2.gif"));
    protected JTable table;
    boolean testprint = false;
    private final JLabel top;
    private final JLabel bottom;
    private final JLabel draw;
    private final JLabel total;

    public LinePanel() {
        this(new GridLayout(2, 0),SwingConstants.RIGHT);
        add(top);
        add(bottom);
    }

    public LinePanel(String name) {
        this(new GridLayout(4, 0),SwingConstants.RIGHT);
        setName(name);
        add(top);
        add(bottom);
        add(draw);
        add(total);
    }
    protected LinePanel(LayoutManager layoutManager,int textAlignment) {
        super(layoutManager);
        top = new JLabel("", ICON_BLANK, SwingConstants.LEADING);
        bottom = new JLabel("", ICON_BLANK, SwingConstants.LEADING);
        draw = new JLabel("", ICON_BLANK, SwingConstants.LEADING);
        total = new JLabel("", ICON_BLANK, SwingConstants.LEADING);

        Font myfont = new Font("Arial", Font.PLAIN, 12);
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

            LinesTableData ltd = ((LinesTableDataSupplier) table).getLinesTableData(row);

            stv.setDisplayType(ltd.getDisplayType());
            stv.setPeriodType(ltd.getPeriodType());

            if (ltd.isShowingPrior()) {
                boxes = stv.getPriorBoxes();
                testprint = stv.gid == 6829 && stv.bid == 204;
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

        if (ld.getBorder().contains("bestvisit")) {
            if (ld.getBorder().contains("besthome")) {
                top.setBorder(bestallborder);
            } else {
                top.setBorder(bestvisitborder);

            }
        } else if (ld.getBorder().contains("besthome")) {
            top.setBorder(besthomeborder);

        } else if (ld.getBorder().contains("bestover")) {
            if (ld.getBorder().contains("bestunder")) {
                top.setBorder(bestallborder);
            } else {
                top.setBorder(bestoverborder);
            }

        } else if (ld.getBorder().contains("bestunder")) {
            top.setBorder(bestunderborder);

        } else {
            top.setBorder(null);
        }


    }

    public void setBottom(LineData ld, int row, int col) {

        boolean blackorred = false;
        bottom.setText(ld.getData());

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

        if (ld.getBorder().contains("bestvisit")) {
            if (ld.getBorder().contains("besthome")) {
                bottom.setBorder(bestallborder);
            } else {
                bottom.setBorder(bestvisitborder);
            }
        } else if (ld.getBorder().contains("besthome")) {
            bottom.setBorder(besthomeborder);
        } else if (ld.getBorder().contains("bestover")) {
            if (ld.getBorder().contains("bestunder")) {
                bottom.setBorder(bestallborder);
            } else {
                bottom.setBorder(bestoverborder);
            }

        } else if (ld.getBorder().contains("bestunder")) {
            bottom.setBorder(bestunderborder);

        } else {
            bottom.setBorder(null);
        }


    }

    private void setDraw(LineData ld, int row, int col) {

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

        if (ld.getBorder().contains("bestvisit")) {
            if (ld.getBorder().contains("besthome")) {
                draw.setBorder(bestallborder);
            } else {
                draw.setBorder(bestvisitborder);
            }
        } else if (ld.getBorder().contains("besthome")) {
            draw.setBorder(besthomeborder);
        } else if (ld.getBorder().contains("bestover")) {
            if (ld.getBorder().contains("bestunder")) {
                draw.setBorder(bestallborder);
            } else {
                draw.setBorder(bestoverborder);
            }

        } else if (ld.getBorder().contains("bestunder")) {
            draw.setBorder(bestunderborder);

        } else if (ld.getBorder().contains("bestdraw")) {
            draw.setBorder(bestdrawborder);

        } else {
            draw.setBorder(null);
        }
    }

    private void setTotal(LineData ld, int row, int col) {

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

        if (ld.getBorder().contains("bestvisit")) {
            if (ld.getBorder().contains("besthome")) {
                total.setBorder(bestallborder);
            } else {
                total.setBorder(bestvisitborder);


            }
        } else if (ld.getBorder().contains("besthome")) {
            total.setBorder(besthomeborder);
        } else if (ld.getBorder().contains("bestover")) {
            if (ld.getBorder().contains("bestunder")) {
                total.setBorder(bestallborder);
            } else {
                total.setBorder(bestoverborder);
            }

        } else if (ld.getBorder().contains("bestunder")) {
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

            LinesTableData ltd = ((LinesTableDataSupplier) table).getLinesTableData(row);

            stv.setDisplayType(ltd.getDisplayType());
            stv.setPeriodType(ltd.getPeriodType());

            if (ltd.isShowingPrior()) {
                boxes = stv.getPriorBoxes();
                testprint = stv.gid == 6829 && stv.bid == 204;
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
        if (isSoccer()) {
            try {
                LinesTableData ltd = ((LinesTableDataSupplier) table).getLinesTableData(row);
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

    private boolean isSoccer() {
        return SiaConst.SoccerStr.equalsIgnoreCase(getName());
    }

    public void setTime(TimeView stv, int row, int col) {

        top.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.setHorizontalAlignment(SwingConstants.CENTER);
        LineData[] boxes = stv.getCurrentBoxes();
        setTop(boxes[0], row, col);
        setBottom(boxes[1], row, col);
        if (isSoccer()) {
            LineData blank = new LineData(ICON_BLANK, "", Color.WHITE);
            setDraw(blank, row, col);
            setTotal(blank, row, col);
        }
    }

    public void setChart(ChartView stv, int row, int col) {

        top.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            LinesTableData ltd = ((LinesTableDataSupplier) table).getLinesTableData(row);

            stv.setDisplayType(ltd.getDisplayType());
            stv.setPeriodType(ltd.getPeriodType());
        } catch (Exception ex) {
            log(ex);
        }


        LineData[] boxes = stv.getCurrentBoxes();
        setTop(boxes[0], row, col);
        setBottom(boxes[1], row, col);
        if (isSoccer()) {
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
            LinesTableData ltd = ((LinesTableDataSupplier) table).getLinesTableData(row);
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
        if (isSoccer()) {
            LineData blank = new LineData(ICON_BLANK, "", Color.WHITE);
            setDraw(blank, row, col);
            setTotal(blank, row, col);
        }

    }
}