package com.sia.client.ui;

import com.sia.client.config.Config;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.*;
import com.sia.client.ui.lineseeker.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class LineSeekerNode {
    private final int gameid;
    private final AlertPeriod period;
    private boolean spreadcheck;
    private boolean usespreadmatheq;
    private double visitorspread;
    private double visitorjuice;
    private String visitorspreadalerttype;
    private double homespread;
    private double homejuice;
    private String homespreadalerttype;
    private boolean totalcheck;
    private boolean usetotalmatheq;
    private double over;
    private double overjuice;
    private String overalerttype;
    private double under;
    private double underjuice;
    private String underalerttype;
    private boolean moneylinecheck;
    private boolean usemoneylinematheq;
    private double visitorml;
    private String visitormlalerttype;
    private double homeml;
    private String homemlalerttype;
    private boolean awayttcheck;
    private boolean useawayttmatheq;
    private double awayttover;
    private double awayttoverjuice;
    private String awayttoveralerttype;
    private double awayttunder;
    private double awayttunderjuice;
    private String awayttunderalerttype;
    private boolean homettcheck;
    private boolean usehomettmatheq;
    private double homettover;
    private double homettoverjuice;
    private String homettoveralerttype;
    private double homettunder;
    private double homettunderjuice;
    private String homettunderalerttype;

    private long lastspreadnotify = 0;
    private long lasttotalnotify = 0;
    private long lastmoneylinenotify = 0;
    private long lastawayttnotify = 0;
    private long lasthomettnotify = 0;
    private float lineseekerwaitmin = 0;
    private Game g;
    private Sport s;
    private Bookie b;
    private static Map<String,LineSeekerNode> liveNodes = new HashMap<>();

    public static List<LineSeekerNode> getAllNodes() {
        return Config.instance().getAlertAttrMap().keySet().stream().map(LineSeekerNode::of).collect(Collectors.toList());
    }
    public static LineSeekerNode of (String key) {
        String[] strs = key.split(Config.KeyJointer);
        int gameid = Integer.parseInt(strs[0]);
        AlertPeriod period = AlertPeriod.valueOf(strs[1]);
        LineSeekerNode lineSeekerNode = liveNodes.computeIfAbsent(key, (k)->new LineSeekerNode(gameid,period));
        updateWithAlertConfig(lineSeekerNode);
        return lineSeekerNode;
    }
    private static void updateWithAlertConfig(LineSeekerNode lineSeekerNode) {
        int gameid = lineSeekerNode.gameid;
        AlertPeriod period = lineSeekerNode.period;
        boolean spreadcheck=false;
        boolean usespreadmatheq=false;
        double visitorspread=0d;
        double visitorjuice=0d;
        String visitorspreadalerttype="";
        double homespread=0d;
        double homejuice=0d;
        String homespreadalerttype="";
        boolean totalcheck=false;
        boolean usetotalmatheq=false;
        double over=0d;
        double overjuice=0d;
        String overalerttype="";
        double under=0d;
        double underjuice=0d;
        String underalerttype="";
        boolean moneylinecheck=false;
        boolean usemoneylinematheq=false;
        double visitorml=0d;
        String visitormlalerttype="";
        double homeml=0d;
        String homemlalerttype="";
        boolean awayttcheck=false;
        boolean useawayttmatheq=false;
        double awayttover=0d;
        double awayttoverjuice=0d;
        String awayttoveralerttype="";
        double awayttunder=0d;
        double awayttunderjuice=0d;
        String awayttunderalerttype="";
        boolean homettcheck=false;
        boolean usehomettmatheq=false;
        double homettover=0d;
        double homettoverjuice=0d;
        String homettoveralerttype="";
        double homettunder=0d;
        double homettunderjuice=0d;
        String homettunderalerttype="";

        AlertConfig alertConfig = AlertAttrManager.getAlertAttr(String.valueOf(gameid),period);
        if ( null != alertConfig) {
            LineSeekerAttribute spreadAttr = alertConfig.getSectionAtrribute(AlertSectionName.spreadName);
            spreadcheck = spreadAttr.isActivateStatus();
            usespreadmatheq = spreadAttr.isUseEquivalent();
            visitorspread = Double.parseDouble(spreadAttr.getVisitorColumn().getLineInput());
            visitorjuice = Double.parseDouble(spreadAttr.getVisitorColumn().getJuiceInput());
            visitorspreadalerttype = spreadAttr.getVisitorColumn().getAlertState().name();
            homespread = Double.parseDouble(spreadAttr.getHomeColumn().getLineInput());
            homejuice = Double.parseDouble(spreadAttr.getHomeColumn().getJuiceInput());
            homespreadalerttype = spreadAttr.getHomeColumn().getAlertState().name();

            LineSeekerAttribute totalAttr = alertConfig.getSectionAtrribute(AlertSectionName.totalsName);
            totalcheck = totalAttr.isActivateStatus();
            usetotalmatheq = totalAttr.isUseEquivalent();
            over = Double.parseDouble(totalAttr.getVisitorColumn().getLineInput());
            overjuice = Double.parseDouble(totalAttr.getVisitorColumn().getJuiceInput());
            overalerttype = totalAttr.getVisitorColumn().getAlertState().name();
            under = Double.parseDouble(totalAttr.getHomeColumn().getLineInput());
            underjuice = Double.parseDouble(totalAttr.getHomeColumn().getJuiceInput());
            underalerttype = totalAttr.getHomeColumn().getAlertState().name();

            LineSeekerAttribute mlAttr = alertConfig.getSectionAtrribute(AlertSectionName.mLineName);
            moneylinecheck = mlAttr.isActivateStatus();
            usemoneylinematheq = mlAttr.isUseEquivalent();
            visitorml = Double.parseDouble(mlAttr.getVisitorColumn().getJuiceInput());
            visitormlalerttype = mlAttr.getVisitorColumn().getAlertState().name();
            homeml = Double.parseDouble(mlAttr.getHomeColumn().getJuiceInput());
            homemlalerttype = mlAttr.getHomeColumn().getAlertState().name();

            LineSeekerAttribute awayAttr = alertConfig.getSectionAtrribute(AlertSectionName.awayName);
            awayttcheck = awayAttr.isActivateStatus();
            useawayttmatheq = awayAttr.isUseEquivalent();
            awayttover = Double.parseDouble(awayAttr.getVisitorColumn().getLineInput());
            awayttoverjuice = Double.parseDouble(awayAttr.getVisitorColumn().getJuiceInput());
            awayttoveralerttype = awayAttr.getVisitorColumn().getAlertState().name();
            awayttunder = Double.parseDouble(awayAttr.getHomeColumn().getLineInput());
            awayttunderjuice = Double.parseDouble(awayAttr.getHomeColumn().getJuiceInput());
            awayttunderalerttype = awayAttr.getHomeColumn().getAlertState().name();

            LineSeekerAttribute homettAttr = alertConfig.getSectionAtrribute(AlertSectionName.homeTTName);
            homettcheck = homettAttr.isActivateStatus();
            usehomettmatheq = homettAttr.isUseEquivalent();
            homettover = Double.parseDouble(homettAttr.getVisitorColumn().getLineInput());
            homettoverjuice = Double.parseDouble(homettAttr.getVisitorColumn().getJuiceInput());
            homettoveralerttype = homettAttr.getVisitorColumn().getAlertState().name();
            homettunder = Double.parseDouble(homettAttr.getHomeColumn().getLineInput());
            homettunderjuice = Double.parseDouble(homettAttr.getHomeColumn().getJuiceInput());
            homettunderalerttype = homettAttr.getHomeColumn().getAlertState().name();
        }
        lineSeekerNode.updateWithAlertConfig(
                spreadcheck,   usespreadmatheq,    visitorspread, visitorjuice,      visitorspreadalerttype,homespread,        homejuice,       homespreadalerttype
                ,totalcheck,    usetotalmatheq,     over,          overjuice,          overalerttype,         under,            underjuice,      underalerttype
                ,moneylinecheck,usemoneylinematheq, visitorml,     visitormlalerttype, homeml,                homemlalerttype
                ,awayttcheck,   useawayttmatheq,    awayttover,    awayttoverjuice,    awayttoveralerttype,   awayttunder,      awayttunderjuice, awayttunderalerttype
                ,homettcheck,   usehomettmatheq,    homettover,    homettoverjuice,    homettoveralerttype,   homettunder,      homettunderjuice, homettunderalerttype);
    }
    private LineSeekerNode(int gameid, AlertPeriod period) {
        this.gameid = gameid;
        this.period = period;
    }
    private void updateWithAlertConfig(boolean spreadcheck, boolean usespreadmatheq, double visitorspread, double visitorjuice,
                           String visitorspreadalerttype, double homespread, double homejuice, String homespreadalerttype, boolean totalcheck,
                           boolean usetotalmatheq, double over, double overjuice, String overalerttype, double under,
                           double underjuice, String underalerttype, boolean moneylinecheck, boolean usemoneylinematheq,
                           double visitorml, String visitormlalerttype, double homeml, String homemlalerttype,
                           boolean awayttcheck, boolean useawayttmatheq, double awayttover, double awayttoverjuice,
                           String awayttoveralerttype, double awayttunder, double awayttunderjuice, String awayttunderalerttype,
                           boolean homettcheck, boolean usehomettmatheq, double homettover, double homettoverjuice,
                           String homettoveralerttype, double homettunder, double homettunderjuice, String homettunderalerttype) {
        this.spreadcheck = spreadcheck;
        this.usespreadmatheq = usespreadmatheq;
        this.visitorspread = visitorspread;
        this.visitorjuice = visitorjuice;
        this.visitorspreadalerttype = visitorspreadalerttype;
        this.homespread = homespread;
        this.homejuice = homejuice;
        this.homespreadalerttype = homespreadalerttype;
        this.totalcheck = totalcheck;
        this.usetotalmatheq = usetotalmatheq;
        this.over = over;
        this.overjuice = overjuice;
        this.overalerttype = overalerttype;
        this.under = under;
        this.underjuice = underjuice;
        this.underalerttype = underalerttype;
        this.moneylinecheck = moneylinecheck;
        this.usemoneylinematheq = usemoneylinematheq;
        this.visitorml = visitorml;
        this.visitormlalerttype = visitormlalerttype;
        this.homeml = homeml;
        this.homemlalerttype = homemlalerttype;
        this.awayttcheck = awayttcheck;
        this.useawayttmatheq = useawayttmatheq;
        this.awayttover = awayttover;
        this.awayttoverjuice = awayttoverjuice;
        this.awayttoveralerttype = awayttoveralerttype;
        this.awayttunder = awayttunder;
        this.awayttunderjuice = awayttunderjuice;
        this.awayttunderalerttype = awayttunderalerttype;
        this.homettcheck = homettcheck;
        this.usehomettmatheq = usehomettmatheq;
        this.homettover = homettover;
        this.homettoverjuice = homettoverjuice;
        this.homettoveralerttype = homettoveralerttype;
        this.homettunder = homettunder;
        this.homettunderjuice = homettunderjuice;
        this.homettunderalerttype = homettunderalerttype;
    }


    public boolean doesthislinequalify(Line line) {


        String html = "";
        g = AppController.getGame(line.getGameid());
        s = AppController.getSportByLeagueId(g.getLeague_id());
        int leagueid = g.getLeague_id();
        int bookieid = line.getBookieid();
        b = AppController.getBookie(bookieid);
        int periodid = line.getPeriod();
        int sportid = s.getSport_id();
        int gameid = g.getGame_id();

//      FRANK VARIABLES FROM CONFIG
        Vector bookiecodes = new Vector();
        // load bookiecodes from LineSeekerConfig
        String bookies = AlertAttrManager.getBookies(); // bookie names ( NOT booke id) separated by ","
        AlertSeekerMethods  alertSeekerMethods = Config.instance().getAlertSeekerMethods();
        lineseekerwaitmin = Float.parseFloat(alertSeekerMethods.getRenotifyInMinutes());

        LineSeekerAlertMethodAttr goodAttr = alertSeekerMethods.getAlertMethodMap().get(AlertState.Good.name());
        boolean playgoodaudio = goodAttr.getAudioEnabled();
        String goodaudiofile = goodAttr.getSoundFile();
        boolean showgoodpopup = goodAttr.getPopupEnabled();
        int goodpopuplocation = goodAttr.getPopupLocation().getLocation();
        int goodpopupseconds = Integer.parseInt(goodAttr.getPopupSeconds());

        LineSeekerAlertMethodAttr badAttr = alertSeekerMethods.getAlertMethodMap().get(AlertState.Bad.name());
        boolean playbadaudio = badAttr.getAudioEnabled();
        String badaudiofile = badAttr.getSoundFile();
        boolean showbadpopup = badAttr.getPopupEnabled();
        int badpopuplocation = badAttr.getPopupLocation().getLocation();
        int badpopupseconds = Integer.parseInt(badAttr.getPopupSeconds());

        LineSeekerAlertMethodAttr neutralAttr = alertSeekerMethods.getAlertMethodMap().get(AlertState.Neutral.name());
        boolean playneutralaudio = neutralAttr.getAudioEnabled();
        String neutralaudiofile = neutralAttr.getSoundFile();
        boolean showneutralpopup = neutralAttr.getPopupEnabled();
        int neutralpopuplocation = neutralAttr.getPopupLocation().getLocation();
        int neutralpopupseconds = Integer.parseInt(neutralAttr.getPopupSeconds());

        if(this.gameid == gameid && this.getPeriod().getOrder() == periodid && b != null) {

            //  if (bookiecodes.contains("" + bookieid)) {

            // this prints out all bookies, circa... why is circa always printing out?
            System.out.println("bookies string in lineseekernode=" + bookies);
            if (bookies.indexOf(AppController.getBookie(bookieid).getName()) != -1 || bookies.indexOf("All Bookies") != -1) {

                System.out.println(line.getClass()+".."+line.toString()+".."+totalcheck+".."+AppController.getBookie(bookieid).getName());
                if (line instanceof Spreadline && spreadcheck) {
                    html = shouldIAlertSpreadline((Spreadline) line);
                } else if (line instanceof Totalline && totalcheck) {
                    html = shouldIAlertTotalline((Totalline) line);
                } else if (line instanceof Moneyline && moneylinecheck) {
                    html = shouldIAlertMoneyline((Moneyline) line);
                } else if (line instanceof TeamTotalline && awayttcheck) {
                    html = shouldIAlertAwayTeamTotalline((TeamTotalline) line);
                } else if (line instanceof TeamTotalline && homettcheck) {
                    html = shouldIAlertHomeTeamTotalline((TeamTotalline) line);
                } else {

                    html = "";
                }

            } else {
                html = "";
            }

            if (!html.equals("")) {
              //  System.out.println("lineseekerflags...lineseekerwaitmin="+lineseekerwaitmin);
              //  System.out.println("lineseekerflags...playgoodaudio="+playgoodaudio);
              //  System.out.println("lineseekerflags...showgoodpopup="+showgoodpopup);
              //  System.out.println("lineseekerflags...goodpopuplocation="+goodpopuplocation);
             //   System.out.println("lineseekerflags...goodpopupseconds="+goodpopupseconds);
              //  System.out.println("lineseekerflags...playbadaudio="+playbadaudio);
              //  System.out.println("lineseekerflags...showbaddpopup="+showbadpopup);
              //  System.out.println("lineseekerflags...badpopuplocation="+badpopuplocation);
              //  System.out.println("lineseekerflags...badpopupseconds="+badpopupseconds);
              //  System.out.println("lineseekerflags...playneutralaudio="+playneutralaudio);
              //  System.out.println("lineseekerflags...showneutralpopup="+showneutralpopup);
              //  System.out.println("lineseekerflags...neutralpopuplocation="+neutralpopuplocation);
              //  System.out.println("lineseekerflags...neutralpopupseconds="+neutralpopupseconds);
                String mesg = html;
                mesg = mesg.replaceAll("<H2>","<table><tr><td>");
                mesg = mesg.replaceAll("</H2>","</td></tr></table>");
                mesg = mesg.replaceAll("<br>","");
                String hrmin = AppController.getCurrentHoursMinutes();
                AppController.addAlert(hrmin,mesg);
                if (html.indexOf("GOOD") != -1) {
                    if (playgoodaudio) {
                        new SoundPlayer(goodaudiofile);
                    }
                    if (showgoodpopup) {
                        new UrgentMessage(html, goodpopupseconds * 1000, goodpopuplocation, AppController.getMainTabPane());
                    }

                } else if (html.indexOf("BAD") != -1) {
                    if (playbadaudio) {
                        new SoundPlayer(badaudiofile);
                    }
                    if (showgoodpopup) {
                        new UrgentMessage(html, badpopupseconds * 1000, badpopuplocation, AppController.getMainTabPane());
                    }
                } else if (html.indexOf("NEUTRAL") != -1) {
                    if (playneutralaudio) {
                        new SoundPlayer(neutralaudiofile);
                    }
                    if (showneutralpopup) {
                        new UrgentMessage(html, neutralpopupseconds * 1000, neutralpopuplocation, AppController.getMainTabPane());
                    }
                }
                return true;
            } else {
                return false;
            }
        }
        else
        {
            return false;

        }

    }


    public String shouldIAlertSpreadline(Spreadline line) {
        if(visitorspread == 0 && homespread == 0 && visitorjuice == -110 && homejuice == -110) // default
        {
            return "";
        }
        long nowms = System.currentTimeMillis();
        double thisvisitspread = line.getCurrentvisitspread();
        double thisvisitjuice = line.getCurrentvisitjuice();
        double thishomespread = line.getCurrenthomespread();
        double thishomejuice = line.getCurrenthomejuice();
        String htmlvisit = "<html><body><H2>" + getVisitorspreadalerttype().toUpperCase() + " " + "LINE SEEKER&nbsp;</H2>" +
                "<br><table><tr><td>" + s.getLeaguename() + "</td><td colspan=2>" + g.getGameString() + "</td></tr></table><table>";
        htmlvisit = htmlvisit + "<tr><td colspan=2>" + g.getVisitorgamenumber() + "&nbsp;" + g.getVisitorteam() + "&nbsp;" + thisvisitspread + thisvisitjuice + "</td></tr><tr><td>"+b.getName()+"</td></tr>";
        String htmlhome = "<html><body><H2>" + getHomespreadalerttype().toUpperCase() + " " + "LINE SEEKER&nbsp;</H2>" +
                "<br><table><tr><td>" + s.getLeaguename() + "</td><td colspan=2>" + g.getGameString() + "</td></tr></table><table>";
        htmlhome = htmlhome + "<tr><td colspan=2>" + g.getHomegamenumber() + "&nbsp;" + g.getHometeam() + "&nbsp;" + thishomespread + thishomejuice + "</td></tr><tr><td>"+b.getName()+"</td></tr>";


        if (isUsespreadmatheq()) {
            if (LinesMoves.isLine1BetterThanLine2(thisvisitspread, thisvisitjuice, visitorspread, visitorjuice, line.getLeague_id(), line.getPeriod(), "SPREAD", line.getGameid())) {
                if (nowms - lastspreadnotify > lineseekerwaitmin * 60 * 1000) {
                    lastspreadnotify = nowms;
                    return htmlvisit;
                }
            } else if (LinesMoves.isLine1BetterThanLine2(thishomespread, thishomejuice, homespread, homejuice, line.getLeague_id(), line.getPeriod(), "SPREAD", line.getGameid())) {
                if (nowms - lastspreadnotify > lineseekerwaitmin * 60 * 1000) {
                    lastspreadnotify = nowms;
                    return htmlhome;
                }
            }

        } else {
            if (LinesMoves.isLine1BetterThanLine2NoMath(thisvisitspread, thisvisitjuice, visitorspread, visitorjuice, line.getLeague_id(), line.getPeriod(), "SPREAD", line.getGameid())) {
                if (nowms - lastspreadnotify > lineseekerwaitmin * 60 * 1000) {
                    lastspreadnotify = nowms;
                    return htmlvisit;
                }
            } else if (LinesMoves.isLine1BetterThanLine2NoMath(thishomespread, thishomejuice, homespread, homejuice, line.getLeague_id(), line.getPeriod(), "SPREAD", line.getGameid())) {
                if (nowms - lastspreadnotify > lineseekerwaitmin * 60 * 1000) {
                    lastspreadnotify = nowms;
                    return htmlhome;
                }
            }

        }
        return "";
    }

    public String shouldIAlertMoneyline(Moneyline line) {
        if(visitorml == -110 && homeml == -110) // default
        {
            return "";
        }
        long nowms = System.currentTimeMillis();

        double thisvisitml = line.getCurrentvisitjuice();
        double thishomeml = line.getCurrenthomejuice();

        String htmlvisit = "<html><body><H2>" + getVisitormlalerttype().toUpperCase() + " " + "LINE SEEKER&nbsp;</H2>" +
                "<br><table><tr><td>" + s.getLeaguename() + "</td><td colspan=2>" + g.getGameString() + "</td></tr></table><table>";
        htmlvisit = htmlvisit + "<tr><td colspan=2>" + g.getVisitorgamenumber() + "&nbsp;" + g.getVisitorteam() + "&nbsp;" + thisvisitml + "</td></tr><tr><td>"+b.getName()+"</td></tr>";
        String htmlhome = "<html><body><H2>" + getHomemlalerttype().toUpperCase() + " " + "LINE SEEKER&nbsp;</H2>" +
                "<br><table><tr><td>" + s.getLeaguename() + "</td><td colspan=2>" + g.getGameString() + "</td></tr></table><table>";
        htmlhome = htmlhome + "<tr><td colspan=2>" + g.getHomegamenumber() + "&nbsp;" + g.getHometeam() + "&nbsp;" + thishomeml + "</td></tr><tr><td>"+b.getName()+"</td></tr>";

        //System.out.println("VISITML.."+thisvisitml+"..is better than?"+visitorml+"..leagueid="+line.getLeague_id()+"periodid="+line.getPeriod()+"gameid="+line.getGameid());
        //System.out.println("HOMEML.."+thishomeml+"..is better than?"+homeml+"..leagueid="+line.getLeague_id()+"periodid="+line.getPeriod()+"gameid="+line.getGameid());
        if (isUsespreadmatheq()) {
            if (LinesMoves.isLine1BetterThanLine2(0, thisvisitml, 0, visitorml, line.getLeague_id(), line.getPeriod(), "MONEYLINE", line.getGameid())) {
                if (nowms - lastmoneylinenotify > lineseekerwaitmin * 60 * 1000) {
                    lastmoneylinenotify = nowms;
                    return htmlvisit;
                }
            } else if (LinesMoves.isLine1BetterThanLine2(0, thishomeml, 0, homeml, line.getLeague_id(), line.getPeriod(), "MONEYLINE", line.getGameid())) {
                if (nowms - lastmoneylinenotify > lineseekerwaitmin * 60 * 1000) {
                    lastmoneylinenotify = nowms;
                    return htmlhome;
                }
            }

        } else {
            if (LinesMoves.isLine1BetterThanLine2NoMath(0, thisvisitml, 0, visitorml, line.getLeague_id(), line.getPeriod(), "MONEYLINE", line.getGameid())) {
                if (nowms - lastmoneylinenotify > lineseekerwaitmin * 60 * 1000) {
                    lastmoneylinenotify = nowms;
                    return htmlvisit;
                }
            } else if (LinesMoves.isLine1BetterThanLine2NoMath(0, thishomeml, 0, homeml, line.getLeague_id(), line.getPeriod(), "MONEYLINE", line.getGameid())) {
                if (nowms - lastmoneylinenotify > lineseekerwaitmin * 60 * 1000) {
                    lastmoneylinenotify = nowms;
                    return htmlhome;
                }
            }
        }
        return "";
    }

    public String shouldIAlertTotalline(Totalline line) {

        if(over == 0 && under == 0) // default
        {
            return "";
        }

        long nowms = System.currentTimeMillis();
        double thisover = line.getCurrentover();
        double thisoverjuice = line.getCurrentoverjuice();
        double thisunder = line.getCurrentunder();
        double thisunderjuice = line.getCurrentunderjuice();

        String htmlover = "<html><body><H2>" + getOveralerttype().toUpperCase() + " " + "LINE SEEKER&nbsp;</H2>" +
                "<br><table><tr><td>" + s.getLeaguename() + "</td><td colspan=2>" + g.getGameString() + "</td></tr></table><table>";
        htmlover = htmlover + "<tr><td colspan=2>" + g.getVisitorgamenumber() + "&nbsp;OVER&nbsp;" + thisover + thisoverjuice + "</td></tr><tr><td>"+b.getName()+"</td></tr>";
        String htmlunder = "<html><body><H2>" + getUnderalerttype().toUpperCase() + " " + "LINE SEEKER&nbsp;</H2>" +
                "<br><table><tr><td>" + s.getLeaguename() + "</td><td colspan=2>" + g.getGameString() + "</td></tr></table><table>";
        htmlunder = htmlunder + "<tr><td colspan=2>" + g.getHomegamenumber() + "&nbsp;UNDER&nbsp;" + thisunder + thisunderjuice + "</td></tr><tr><td>"+b.getName()+"</td></tr>";


        if (isUsetotalmatheq()) {
            System.out.println("using total math.."+line.getLeague_id()+".."+line.getPeriod()+"..."+thisover+thisoverjuice+".vs."+over+overjuice);
            if (LinesMoves.isLine1BetterThanLine2(thisover, thisoverjuice, over, overjuice, line.getLeague_id(), line.getPeriod(), "OVER", line.getGameid())) {
                System.out.println("line1 is better now check.."+nowms+".."+lasttotalnotify+"..vs."+lineseekerwaitmin);
                if (nowms - lasttotalnotify > lineseekerwaitmin * 60 * 1000)
                {
                    lasttotalnotify = nowms;
                    System.out.println("RETURNING="+htmlover);
                    return htmlover;
                }
            } else if (LinesMoves.isLine1BetterThanLine2(thisunder, thisunderjuice, under, underjuice, line.getLeague_id(), line.getPeriod(), "UNDER", line.getGameid())) {
                if (nowms - lasttotalnotify > lineseekerwaitmin * 60 * 1000) {
                    lasttotalnotify = nowms;
                    return htmlunder;
                }
            }
        } else {
            if (LinesMoves.isLine1BetterThanLine2NoMath(thisover, thisoverjuice, over, overjuice, line.getLeague_id(), line.getPeriod(), "OVER", line.getGameid())) {
                if (nowms - lasttotalnotify > lineseekerwaitmin * 60 * 1000) {
                    lasttotalnotify = nowms;
                    return htmlover;
                }
            } else if (LinesMoves.isLine1BetterThanLine2NoMath(thisunder, thisunderjuice, under, underjuice, line.getLeague_id(), line.getPeriod(), "UNDER", line.getGameid())) {
                if (nowms - lasttotalnotify > lineseekerwaitmin * 60 * 1000) {
                    lasttotalnotify = nowms;
                    return htmlunder;
                }
            }
        }
        return "";
    }

    public String shouldIAlertAwayTeamTotalline(TeamTotalline line) {
        if(awayttover == 0 && awayttunder == 0) // default
        {
            return "";
        }
        long nowms = System.currentTimeMillis();
        double thisawayover = line.getCurrentvisitover();
        double thisawayoverjuice = line.getCurrentvisitoverjuice();
        double thisawayunder = line.getCurrentvisitunder();
        double thisawayunderjuice = line.getCurrentvisitunderjuice();

        String htmlover = "<html><body><H2>" + getAwayttoveralerttype().toUpperCase() + " " + "LINE SEEKER&nbsp;</H2>" +
                "<br><table><tr><td>" + s.getLeaguename() + "</td><td colspan=2>" + g.getGameString() + "</td></tr></table><table>";
        htmlover = htmlover + "<tr><td colspan=2>" + g.getVisitorgamenumber() + "&nbsp;" + g.getVisitorteam() + "&nbsp;OVER&nbsp;" + thisawayover + thisawayoverjuice + "</td></tr><tr><td>"+b.getName()+"</td></tr>";
        String htmlunder = "<html><body><H2>" + getAwayttunderalerttype().toUpperCase() + " " + "LINE SEEKER&nbsp;</H2>" +
                "<br><table><tr><td>" + s.getLeaguename() + "</td><td colspan=2>" + g.getGameString() + "</td></tr></table><table>";
        htmlunder = htmlunder + "<tr><td colspan=2>" + g.getVisitorgamenumber() + "&nbsp;" + g.getVisitorteam() + "&nbsp;UNDER&nbsp;" + thisawayunder + thisawayunderjuice + "</td></tr><tr><td>"+b.getName()+"</td></tr>";


        if (isUsetotalmatheq()) {
            if (LinesMoves.isLine1BetterThanLine2(thisawayover, thisawayoverjuice, awayttover, awayttoverjuice, line.getLeague_id(), line.getPeriod(), "TTOVER", line.getGameid())) {
                if (nowms - lastawayttnotify > lineseekerwaitmin * 60 * 1000) {
                    lastawayttnotify = nowms;
                    return htmlover;
                }
            } else if (LinesMoves.isLine1BetterThanLine2(thisawayunder, thisawayunderjuice, awayttunder, awayttunderjuice, line.getLeague_id(), line.getPeriod(), "TTUNDER", line.getGameid())) {
                if (nowms - lastawayttnotify > lineseekerwaitmin * 60 * 1000) {
                    lastawayttnotify = nowms;
                    return htmlunder;
                }
            }
        } else {
            if (LinesMoves.isLine1BetterThanLine2NoMath(thisawayover, thisawayoverjuice, awayttover, awayttoverjuice, line.getLeague_id(), line.getPeriod(), "TTOVER", line.getGameid())) {
                if (nowms - lastawayttnotify > lineseekerwaitmin * 60 * 1000) {
                    lastawayttnotify = nowms;
                    return htmlover;
                }
            } else if (LinesMoves.isLine1BetterThanLine2NoMath(thisawayunder, thisawayunderjuice, awayttunder, awayttunderjuice, line.getLeague_id(), line.getPeriod(), "TTUNDER", line.getGameid())) {
                if (nowms - lastawayttnotify > lineseekerwaitmin * 60 * 1000) {
                    lastawayttnotify = nowms;
                    return htmlunder;
                }
            }
        }
        return "";
    }

    public String shouldIAlertHomeTeamTotalline(TeamTotalline line) {
        if(homettover == 0 && homettunder == 0) // default
        {
            return "";
        }
        long nowms = System.currentTimeMillis();
        double thishomeover = line.getCurrenthomeover();
        double thishomeoverjuice = line.getCurrenthomeoverjuice();
        double thishomeunder = line.getCurrenthomeunder();
        double thishomeunderjuice = line.getCurrenthomeunderjuice();

        String htmlover = "<html><body><H2>" + getHomettoveralerttype().toUpperCase() + " " + "LINE SEEKER&nbsp;</H2>" +
                "<br><table><tr><td>" + s.getLeaguename() + "</td><td colspan=2>" + g.getGameString() + "</td></tr></table><table>";
        htmlover = htmlover + "<tr><td colspan=2>" + g.getHomegamenumber() + "&nbsp;" + g.getHometeam() + "&nbsp;OVER&nbsp;" + thishomeover + thishomeoverjuice + "</td></tr><tr><td>"+b.getName()+"</td></tr>";
        String htmlunder = "<html><body><H2>" + getHomettunderalerttype().toUpperCase() + " " + "LINE SEEKER&nbsp;</H2>" +
                "<br><table><tr><td>" + s.getLeaguename() + "</td><td colspan=2>" + g.getGameString() + "</td></tr></table><table>";
        htmlunder = htmlunder + "<tr><td colspan=2>" + g.getHomegamenumber() + "&nbsp;" + g.getHometeam() + "&nbsp;UNDER&nbsp;" + thishomeunder + thishomeunderjuice + "</td></tr><tr><td>"+b.getName()+"</td></tr>";

        if (isUsetotalmatheq()) {
            if (LinesMoves.isLine1BetterThanLine2(thishomeover, thishomeoverjuice, homettover, homettoverjuice, line.getLeague_id(), line.getPeriod(), "TTOVER", line.getGameid())) {
                if (nowms - lasthomettnotify > lineseekerwaitmin * 60 * 1000) {
                    lasthomettnotify = nowms;
                    return htmlover;
                }
            } else if (LinesMoves.isLine1BetterThanLine2(thishomeunder, thishomeunderjuice, homettunder, homettunderjuice, line.getLeague_id(), line.getPeriod(), "TTUNDER", line.getGameid())) {
                if (nowms - lasthomettnotify > lineseekerwaitmin * 60 * 1000) {
                    lasthomettnotify = nowms;
                    return htmlunder;
                }
            }
        } else {
            if (LinesMoves.isLine1BetterThanLine2NoMath(thishomeover, thishomeoverjuice, homettover, homettoverjuice, line.getLeague_id(), line.getPeriod(), "TTOVER", line.getGameid())) {
                if (nowms - lasthomettnotify > lineseekerwaitmin * 60 * 1000) {
                    lasthomettnotify = nowms;
                    return htmlover;
                }
            } else if (LinesMoves.isLine1BetterThanLine2NoMath(thishomeunder, thishomeunderjuice, homettunder, homettunderjuice, line.getLeague_id(), line.getPeriod(), "TTUNDER", line.getGameid())) {
                if (nowms - lasthomettnotify > lineseekerwaitmin * 60 * 1000) {
                    lasthomettnotify = nowms;
                    return htmlunder;
                }
            }
        }
        return "";
    }


    public int getGameid() {
        return gameid;
    }
    public AlertPeriod getPeriod() {
        return period;
    }
    public boolean isSpreadcheck() {
        return spreadcheck;
    }

    public void setIsspreadcheck(boolean spreadcheck) {
        this.spreadcheck = spreadcheck;
    }

    public boolean isUsespreadmatheq() {
        return usespreadmatheq;
    }

    public void setUsespreadmatheq(boolean usespreadmatheq) {
        this.usespreadmatheq = usespreadmatheq;
    }

    public double getVisitorspread() {
        return visitorspread;
    }

    public void setVisitorspread(double visitorspread) {
        this.visitorspread = visitorspread;
    }

    public double getVisitorjuice() {
        return visitorjuice;
    }

    public void setVisitorjuice(double visitorjuice) {
        this.visitorjuice = visitorjuice;
    }

    public String getVisitorspreadalerttype() {
        return visitorspreadalerttype;
    }

    public void setVisitorspreadalerttype(String visitorspreadalerttype) {
        this.visitorspreadalerttype = visitorspreadalerttype;
    }

    public double getHomespread() {
        return homespread;
    }

    public void setHomespread(double homespread) {
        this.homespread = homespread;
    }

    public double getHomejuice() {
        return homejuice;
    }

    public void setHomejuice(double homejuice) {
        this.homejuice = homejuice;
    }

    public String getHomespreadalerttype() {
        return homespreadalerttype;
    }

    public void setHomespreadalerttype(String homespreadalerttype) {
        this.homespreadalerttype = homespreadalerttype;
    }

    public boolean isTotalcheck() {
        return totalcheck;
    }

    public void setIstotalcheck(boolean totalcheck) {
        this.totalcheck = totalcheck;
    }

    public boolean isUsetotalmatheq() {
        return usetotalmatheq;
    }

    public void setUsetotalmatheq(boolean usetotalmatheq) {
        this.usetotalmatheq = usetotalmatheq;
    }

    public double getOver() {
        return over;
    }

    public void setOver(double over) {
        this.over = over;
    }

    public double getOverjuice() {
        return overjuice;
    }

    public void setOverjuice(double overjuice) {
        this.overjuice = overjuice;
    }

    public String getOveralerttype() {
        return overalerttype;
    }

    public void setOveralerttype(String overalerttype) {
        this.overalerttype = overalerttype;
    }

    public double getUnder() {
        return under;
    }

    public void setUnder(double under) {
        this.under = under;
    }

    public double getUnderjuice() {
        return underjuice;
    }

    public void setUnderjuice(double underjuice) {
        this.underjuice = underjuice;
    }

    public String getUnderalerttype() {
        return underalerttype;
    }

    public void setUnderalerttype(String underalerttype) {
        this.underalerttype = underalerttype;
    }

    public boolean isMoneylinecheck() {
        return moneylinecheck;
    }

    public void setIsmoneylinecheck(boolean moneylinecheck) {
        this.moneylinecheck = moneylinecheck;
    }

    public boolean isUsemoneylinematheq() {
        return usemoneylinematheq;
    }

    public void setUsemoneylinematheq(boolean usemoneylinematheq) {
        this.usemoneylinematheq = usemoneylinematheq;
    }

    public double getVisitorml() {
        return visitorml;
    }

    public void setVisitorml(double visitorml) {
        this.visitorml = visitorml;
    }

    public String getVisitormlalerttype() {
        return visitormlalerttype;
    }

    public void setVisitormlalerttype(String visitormlalerttype) {
        this.visitormlalerttype = visitormlalerttype;
    }

    public double getHomeml() {
        return homeml;
    }

    public void setHomeml(double homeml) {
        this.homeml = homeml;
    }

    public String getHomemlalerttype() {
        return homemlalerttype;
    }

    public void setHomemlalerttype(String homemlalerttype) {
        this.homemlalerttype = homemlalerttype;
    }

    public boolean isAwayttcheck() {
        return awayttcheck;
    }

    public void setIsawayttcheck(boolean awayttcheck) {
        this.awayttcheck = awayttcheck;
    }

    public boolean isUseawayttmatheq() {
        return useawayttmatheq;
    }

    public void setUseawayttmatheq(boolean useawayttmatheq) {
        this.useawayttmatheq = useawayttmatheq;
    }

    public double getAwayttover() {
        return awayttover;
    }

    public void setAwayttover(double awayttover) {
        this.awayttover = awayttover;
    }

    public double getAwayttoverjuice() {
        return awayttoverjuice;
    }

    public void setAwayttoverjuice(double awayttoverjuice) {
        this.awayttoverjuice = awayttoverjuice;
    }

    public String getAwayttoveralerttype() {
        return awayttoveralerttype;
    }

    public void setAwayttoveralerttype(String awayttoveralerttype) {
        this.awayttoveralerttype = awayttoveralerttype;
    }

    public double getAwayttunder() {
        return awayttunder;
    }

    public void setAwayttunder(double awayttunder) {
        this.awayttunder = awayttunder;
    }

    public double getAwayttunderjuice() {
        return awayttunderjuice;
    }

    public void setAwayttunderjuice(double awayttunderjuice) {
        this.awayttunderjuice = awayttunderjuice;
    }

    public String getAwayttunderalerttype() {
        return awayttunderalerttype;
    }

    public void setAwayttunderalerttype(String awayttunderalerttype) {
        this.awayttunderalerttype = awayttunderalerttype;
    }

    public boolean isHomettcheck() {
        return homettcheck;
    }

    public void setIshomettcheck(boolean homettcheck) {
        this.homettcheck = homettcheck;
    }

    public boolean isUsehomettmatheq() {
        return usehomettmatheq;
    }

    public void setUsehomettmatheq(boolean usehomettmatheq) {
        this.usehomettmatheq = usehomettmatheq;
    }

    public double getHomettover() {
        return homettover;
    }

    public void setHomettover(double homettover) {
        this.homettover = homettover;
    }

    public double getHomettoverjuice() {
        return homettoverjuice;
    }

    public void setHomettoverjuice(double homettoverjuice) {
        this.homettoverjuice = homettoverjuice;
    }

    public String getHomettoveralerttype() {
        return homettoveralerttype;
    }

    public void setHomettoveralerttype(String homettoveralerttype) {
        this.homettoveralerttype = homettoveralerttype;
    }

    public double getHomettunder() {
        return homettunder;
    }

    public void setHomettunder(double homettunder) {
        this.homettunder = homettunder;
    }

    public double getHomettunderjuice() {
        return homettunderjuice;
    }

    public void setHomettunderjuice(double homettunderjuice) {
        this.homettunderjuice = homettunderjuice;
    }

    public String getHomettunderalerttype() {
        return homettunderalerttype;
    }

    public void setHomettunderalerttype(String homettunderalerttype) {
        this.homettunderalerttype = homettunderalerttype;
    }
}