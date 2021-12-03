package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.LinesTableData;

import javax.swing.table.TableColumn;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static com.sia.client.config.Utils.log;
import static java.lang.Boolean.parseBoolean;

public class ScreenGameModel {

    private final ScreenProperty screenProperty;
    private final SportType sportType;
    private final Vector<TableColumn> allColumns;
    private final Map<GameGroupHeader, LinesTableData> headerMap = new HashMap<>();
    private int maxlength;

    public ScreenGameModel(ScreenProperty screenProperty,SportType sportType,final Vector<TableColumn> allColumns) {
        this.screenProperty = screenProperty;
        this.sportType = sportType;
        this.allColumns = allColumns;
    }
    public void build() {


    }
    public Collection<LinesTableData> getTableSections() {
        return headerMap.values();
    }
    private void enrichSportType() {
        String[] prefs = sportType.getUserPerf().split("\\|");
        String[] tmp = {};
        boolean all = false;
        sportType.setComingDays(Integer.parseInt(prefs[1]));
        if (parseBoolean(prefs[0])) {
            sportType.setTimeSort(true);
//            this.timesort = true;
        }

        try {
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase(sportType.getSportName())) {
                    all = true;
                }
                setShowProperties(prefs);
            }

        } catch (Exception ex) {
            log(ex);
        }
        sportType.setLeagueFilter(new LeagueFilter(tmp, all));
    }
    private void setShowProperties(String[] prefs) {
        screenProperty.setShowheaders(parseBoolean(prefs[3]));
        sportType.setShowseries(parseBoolean(prefs[4]));
        sportType.setShowingame(parseBoolean(prefs[5]));
        sportType.setShowAdded(parseBoolean(prefs[6]));
        sportType.setShowExtra(parseBoolean(prefs[7]));
        sportType.setShowProps(parseBoolean(prefs[8]));
    }
    public LinesTableData createLinesTableData(Vector<Game> newgamegroupvec, GameGroupHeader gameGroupHeader) {
        LinesTableData tableSection = new LinesTableData(sportType,screenProperty, newgamegroupvec, gameGroupHeader, allColumns);
        if (sportType.equals(SportType.Soccer)) {
            tableSection.setRowHeight(SiaConst.SoccerRowheight);
        }
        return tableSection;
    }
    public Map<GameGroupHeader, LinesTableData> getHeaderMap() {
        return this.headerMap;
    }
    public int getMaxlength() {
        return maxlength;
    }
    private int calcmaxlength(String s1, String s2) {
        return Math.max(s1.length(), s2.length());
    }
}
