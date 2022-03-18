package com.sia.client.model;

import com.sia.client.config.SiaConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class BookieManager {

    private static final String key_value_separator = "=";
    private final Map<Integer,String> changedBookieMap = new HashMap<>();
    private final Map<Integer, Bookie> bookieCache = new ConcurrentHashMap<>();
    private Map<String, Integer> bookieshortnameids = new ConcurrentHashMap<>();
    private List<Bookie> openerbookiesVec = new Vector<>();
    private List<Bookie> bookiesVec = new ArrayList<>();
    private Vector<Bookie> hiddenCols = new Vector<>();
    private Vector<Bookie> shownCols = new Vector<>();
    private Vector<Bookie> fixedCols = new Vector<>();
    private int numfixedcols;

    public static BookieManager instance() {
        return LazyInitHolder.instance;
    }
    private BookieManager() {

    }
    public int getNumFixedCols() {
        return numfixedcols;
    }

    public void setNumFixedCols(int x) {
        numfixedcols = x;
    }
    public void addBookieColumnChanged(int bookieId, String bookieName) {
        Bookie bookie = bookieCache.get(bookieId);
        if ( null != bookie ) {
            bookie.setShortname(bookieName);
        }
        changedBookieMap.put(bookieId,bookieName);
    }
    public String getChangedBookieStr() {
        StringBuilder sb = new StringBuilder();
        Set<Integer> keys = changedBookieMap.keySet();
        for(int bookieId: keys) {
            if ( 0 <sb.length()) {
                sb.append(SiaConst.ColumnDelimiter);
            }
            sb.append(bookieId).append(key_value_separator).append(changedBookieMap.get(bookieId));
        }
        return sb.toString();
    }
    public void setChangedBookieStr(String bookiesStr) {
        changedBookieMap.clear();
        String [] bookies = bookiesStr.split(SiaConst.ColumnDelimiter);
        for(String bookie: bookies) {
            String [] pair = bookie.split(key_value_separator);
            if ( 2 == pair.length) {
                addBookieColumnChanged(Integer.parseInt(pair[0]),pair[1]);
            }
        }
    }
    public Bookie getBookie(int bookieId) {
        return bookieCache.get(bookieId);
    }
    public List<Bookie> getBookiesVec() {
        //log("BEFORE bookiesvec size="+bookiesVec.size());
        reorderBookiesVec();
        //log("AFTER bookiesvec size="+bookiesVec.size());
        return bookiesVec;
    }
    public Iterator<Bookie> iterator() {
        return bookiesVec.iterator();
    }
    public Integer getBookieId(String sn) {
        return bookieshortnameids.get(sn);
    }
    public int reorderBookiesVec() {
        User u = User.instance();
        int fixedcolsint = 0;
        List<Bookie> newVec = new ArrayList<>();
        fixedCols.clear();
        shownCols.clear();
        hiddenCols.clear();
        String[] fixedcols = u.getFixedColumnPrefs().split(",");
        String[] cols = u.getBookieColumnPrefs().split(",");
        for (String id : fixedcols) {
            Bookie b = getBookie(Integer.parseInt(id));
            if (b != null) {
                newVec.add(b);
                fixedCols.add(b);
                fixedcolsint++;
            }

        }
        //cols can be in the format of ["17=Pincl", "620=Sp411", "271=Circa", "880=Bax", "42=Hiltn", +46 more] or ["17,620,271,880,42...."]
        //if column name is rename, id has former format. -- 2021-01-24
        for (String colStr : cols) {
            String id = colStr.split("=")[0];
            Bookie b = getBookie(Integer.parseInt(id));
            if (b != null) {
                shownCols.add(b);
                newVec.add(b);

            }
        }
        for (Bookie b : bookiesVec) {
            if (!newVec.contains(b)) {
                newVec.add(b);
                hiddenCols.add(b);
            }

        }
        bookiesVec = newVec;
        numfixedcols = fixedcolsint;
        return fixedcolsint;

    }
    public void addBookie(Bookie b) {
        String newColName = changedBookieMap.get(b.getBookie_id());
        if ( null != newColName) {
            b.setShortname(newColName);
        }
        bookieCache.put(b.getBookie_id(), b);
        bookieshortnameids.put(b.getShortname(), b.getBookie_id());
        bookiesVec.add(b);
    }
    public void changeBookieShortName(String oldShortName,String newShortName) {
        Integer bookieId = bookieshortnameids.get(oldShortName);
        if ( null != bookieId) {
            Bookie b = bookieCache.get(bookieId);
            b.setShortname(newShortName);
            bookieshortnameids.put(newShortName,bookieId);
        }
    }
    public void addOpenerBookie(Bookie b) {

        bookieCache.put(b.getBookie_id(), b);
        openerbookiesVec.add(b);
    }
    public List<Bookie> getHiddenCols() {
        return hiddenCols;
    }

    public List<Bookie> getShownCols() {
        if (shownCols == null || shownCols.size() == 0) {
            reorderBookiesVec();
        }
        return shownCols;
    }
    public List<Bookie> getFixedCols() {
        return fixedCols;
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    private static abstract class LazyInitHolder {
        private static final BookieManager instance = new BookieManager();
    }
}
