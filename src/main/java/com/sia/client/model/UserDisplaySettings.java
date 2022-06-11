package com.sia.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.*;

public class UserDisplaySettings {

    private String footballdefault;
    private String basketballdefault;
    private String baseballdefault;
    private String hockeydefault;
    private String fightingdefault;
    private String soccerdefault;
    private String autoracingdefault;
    private String golfdefault;
    private String tennisdefault;

    private int firstmoveseconds ;
    private int secondmoveseconds;

    private Color firstcolor;
    private Color secondcolor;
    private Color thirdcolor;

    private Boolean autofitdata;
    private Boolean showcpo;
    private Boolean showborderbestline;
    private Boolean showdirectionicons;

    private Boolean showaltcolor;
    private Color altcolor;

    private Color openercolor;
    private Color lastcolor;

    public Boolean getShowaltcolor() {
        if ( null == showaltcolor) {
            showaltcolor = true;
        }
        return showaltcolor;
    }

    public void setShowaltcolor(Boolean showaltcolor) {
        this.showaltcolor = showaltcolor;
    }

    public Color getAltcolor() {
        if ( null == altcolor ) {
            altcolor = new Color(215, 215, 215);
        }
        return altcolor;
    }

    public void setAltcolor(Color altcolor) {
        this.altcolor = altcolor;
    }

    public Color getOpenercolor() {
        if( null == openercolor) {
            openercolor = Color.GRAY;
        }
        return openercolor;
    }

    public void setOpenercolor(Color openercolor) {
        this.openercolor = openercolor;
    }

    public Color getLastcolor() {
        if ( null == lastcolor) {
            lastcolor = Color.BLUE;
        }
        return lastcolor;
    }

    public void setLastcolor(Color lastcolor) {
        this.lastcolor = lastcolor;
    }

    @JsonProperty
    public String getFootballdefault() {
        if ( null == footballdefault) {
            footballdefault = "spreadtotal";
        }
        return footballdefault;
    }
    @JsonProperty
    public void setFootballdefault(String footballdefault) {
        this.footballdefault = footballdefault;
    }
    @JsonProperty
    public String getBasketballdefault() {
        if ( null == basketballdefault) {
            basketballdefault = "spreadtotal";
        }
        return basketballdefault;
    }
    @JsonProperty
    public void setBasketballdefault(String basketballdefault) {
        this.basketballdefault = basketballdefault;
    }
    @JsonProperty
    public String getBaseballdefault() {
        if ( null == baseballdefault) {
            baseballdefault = "totalbothmoney";
        }
        return baseballdefault;
    }
    @JsonProperty
    public void setBaseballdefault(String baseballdefault) {
        this.baseballdefault = baseballdefault;
    }
    @JsonProperty
    public String getHockeydefault() {
        if ( null == hockeydefault) {
            hockeydefault = "totalbothmoney";
        }
        return hockeydefault;
    }
    @JsonProperty
    public void setHockeydefault(String hockeydefault) {
        this.hockeydefault = hockeydefault;
    }
    @JsonProperty
    public String getFightingdefault() {
        if ( null == fightingdefault) {
            fightingdefault = "totalbothmoney";
        }
        return fightingdefault;
    }
    @JsonIgnore
    public void setFightingdefault(String fightingdefault) {
        this.fightingdefault = fightingdefault;
    }
    @JsonProperty
    public String getSoccerdefault() {
        if ( null == soccerdefault) {
            soccerdefault = "spreadtotal";
        }
        return soccerdefault;
    }
    @JsonProperty
    public void setSoccerdefault(String soccerdefault) {
        this.soccerdefault = soccerdefault;
    }
    @JsonProperty
    public String getAutoracingdefault() {
        if ( null == autoracingdefault) {
            autoracingdefault = "justmoney";
        }
        return autoracingdefault;
    }
    @JsonProperty
    public void setAutoracingdefault(String autoracingdefault) {
        this.autoracingdefault = autoracingdefault;
    }
    @JsonProperty
    public String getGolfdefault() {
        if ( null == golfdefault) {
            golfdefault = "justmoney";
        }
        return golfdefault;
    }
    @JsonProperty
    public void setGolfdefault(String golfdefault) {
        this.golfdefault = golfdefault;
    }
    @JsonProperty
    public String getTennisdefault() {
        if ( null == tennisdefault) {
            tennisdefault = "justmoney";
        }
        return tennisdefault;
    }
    @JsonProperty
    public void setTennisdefault(String tennisdefault) {
        this.tennisdefault = tennisdefault;
    }
    @JsonProperty
    public int getFirstmoveseconds() {
        if ( 0 == firstmoveseconds) {
            firstmoveseconds = 30;
        }
        return firstmoveseconds;
    }
    @JsonProperty
    public void setFirstmoveseconds(int firstmoveseconds) {
        this.firstmoveseconds = firstmoveseconds;
    }
    @JsonProperty
    public int getSecondmoveseconds() {
        if ( 0 == secondmoveseconds) {
            secondmoveseconds = 5;
        }
        return secondmoveseconds;
    }
    @JsonProperty
    public void setSecondmoveseconds(int secondmoveseconds) {
        this.secondmoveseconds = secondmoveseconds;
    }
    @JsonProperty
    public Color getFirstcolor() {
        if ( null == firstcolor) {
            firstcolor = Color.RED;
        }
        return firstcolor;
    }
    @JsonProperty
    public void setFirstcolor(Color firstcolor) {
        this.firstcolor = firstcolor;
    }
    @JsonProperty
    public Color getSecondcolor() {
        if ( null == secondcolor ) {
            secondcolor =  Color.BLACK;
        }
        return secondcolor;
    }
    @JsonProperty
    public void setSecondcolor(Color secondcolor) {
        this.secondcolor = secondcolor;
    }
    @JsonProperty
    public Color getThirdcolor() {
        if ( null == thirdcolor ) {
            thirdcolor =  Color.BLACK;
        }
        return thirdcolor;
    }
    @JsonProperty
    public void setThirdcolor(Color thirdcolor) {
        this.thirdcolor = thirdcolor;
    }
    @JsonProperty
    public boolean getAutofitdata() {
        if ( null == autofitdata) {
            autofitdata = true;
        }
        return autofitdata;
    }
    @JsonProperty
    public void setAutofitdata(Boolean autofitdata) {
        this.autofitdata = autofitdata;
    }
    @JsonProperty
    public boolean getShowcpo() {
        if ( null == showcpo) {
            showcpo = true;
        }
        return showcpo;
    }
    @JsonProperty
    public void setShowcpo(Boolean showcpo) {
        this.showcpo = showcpo;
    }
    @JsonProperty
    public boolean getShowborderbestline() {
        if ( null == showborderbestline) {
            showborderbestline = true;
        }
        return showborderbestline;
    }
    @JsonProperty
    public void setShowborderbestline(Boolean showborderbestline) {
        this.showborderbestline = showborderbestline;
    }
    @JsonProperty
    public boolean getShowdirectionicons() {
        if ( null == showdirectionicons) {
            showdirectionicons = true;
        }
        return showdirectionicons;
    }
    @JsonProperty
    public void setShowdirectionicons(Boolean showdirectionicons) {
        this.showdirectionicons = showdirectionicons;
    }

}
