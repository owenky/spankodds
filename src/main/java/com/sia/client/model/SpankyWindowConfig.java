package com.sia.client.model;

import com.sia.client.ui.GameTimeSorter;

import java.util.Comparator;

public class SpankyWindowConfig {

    private final int windowIndex;
    private String display = "default";
    private int period = 0;
    private Boolean timesort;
    private boolean shortteam = false;
    private boolean opener;
    private boolean last;
    private boolean showingOpener;
    private boolean showingPrior;
    private Comparator<Game> gameComparator;

    public SpankyWindowConfig(int windowIndex,boolean last, boolean opener) {
        this.windowIndex = windowIndex;
        this.last = last;
        setOpener(opener);
    }
    public Comparator<Game> getGameComparator() {
        return gameComparator;
    }
    public int getWindowIndex() {
        return windowIndex;
    }
    public void showCurrent() {
        showingOpener = false;
        showingPrior = false;
    }
    public void showPrior() {
        showingOpener = false;
        showingPrior = true;
    }
    public void showOpener() {
        showingOpener = true;
        showingPrior = false;
    }
    public String getDisplay() {
        return display;
    }

    public void setDisplay(final String display) {
        this.display = display;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(final int period) {
        this.period = period;
    }

    public Boolean isTimesort() {
        return timesort;
    }

    public void setTimesort(final boolean timesort) {
        this.timesort = timesort;
        if ( timesort) {
            gameComparator = new GameTimeSorter();
        } else {
            gameComparator = new GameIdSorter();
        }
    }

    public boolean isShortteam() {
        return shortteam;
    }

    public void setShortteam(final boolean shortteam) {
        this.shortteam = shortteam;
    }

    public boolean isOpener() {
        return opener;
    }
    public void setOpener(boolean opener) {
        this.opener = opener;
        if (opener) {
            showOpener();
        } else if (last) {
            showPrior();
        }
    }
    public boolean isLast() {
        return last;
    }

    public void setLast(final boolean last) {
        this.last = last;
    }

    public boolean isShowingOpener() {
        return showingOpener;
    }

    public void setShowingOpener(final boolean showingOpener) {
        this.showingOpener = showingOpener;
    }

    public boolean isShowingPrior() {
        return showingPrior;
    }

    public void setShowingPrior(final boolean showingPrior) {
        this.showingPrior = showingPrior;
    }
}
