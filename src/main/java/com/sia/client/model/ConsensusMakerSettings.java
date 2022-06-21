package com.sia.client.model;

import com.sia.client.ui.AppController;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import static com.sia.client.config.Utils.log;

public class ConsensusMakerSettings
{

    String name = "";



    String sportsselected = "";
    Vector sportsVec =  new Vector();
    Hashtable bookievalues = new Hashtable();


    public ConsensusMakerSettings()
    {


    }
    public ConsensusMakerSettings(String name)
    {
        this.name = name;
    }
    public ConsensusMakerSettings(String name,String sportslist,String bookiespercentages)
    {
        this.name = name;
        System.out.println("sportslist="+sportslist);
        sportslist = sportslist.replaceAll(",,",",");
        this.sportsselected = sportslist;
        sportsVec.clear();
        String[] sports = sportslist.split(",");
        for (String s : sports) {
            try {
                if(!s.equals("")) {
                    sportsVec.add("" + s);
                }
            } catch (Exception ex) {
                log(ex);
            }
        }

        String[] bookieswithpercentages = bookiespercentages.split(",");
        for (String s : bookieswithpercentages)
        {
            try {
                String[] vals = s.split("=");
                setValue(vals[0],Integer.parseInt(vals[1]));
            } catch (Exception ex) {
                log(ex);
            }
        }

    }

    public String getDelimitedString()
    {
        Enumeration enum99 = bookievalues.keys();
        String bookievaluestr = "";
        String bookieid = "";
        while(enum99.hasMoreElements()) {
            try {
                bookieid = (String) enum99.nextElement();
                if (bookievalues.get(bookieid) != null) {
                    int percentage = (int) bookievalues.get(bookieid);
                    if (percentage > 0) {
                        bookievaluestr = bookievaluestr + bookieid + "=" + percentage + ",";
                    }
                }
            } catch (Exception ex) {
                System.out.println("exception cms bookieid=" + bookieid + ".." + ex);
            }
        }

        String ret = getName()+"|"+getSportsselected()+"|"+bookievaluestr;
            return ret;

    }
    public  int getValue(int bookieid)
    {

        if(bookievalues.get(""+bookieid) != null)
        {
            return (int)bookievalues.get(""+bookieid);
        }
        else
        {
            return 0;
        }
    }
    public  void setValue(String bookieid,int value)
    {

        bookievalues.put(bookieid,value) ;
    }

    public Vector<Bookie> getBookiesVec()
    {
        Vector bookiesvec = new Vector();
        Enumeration enum99 = bookievalues.keys();
        String bookieid = "";
        while(enum99.hasMoreElements()) {
            try {
                bookieid = (String) enum99.nextElement();
                Bookie b = AppController.getBookie(Integer.parseInt(bookieid));
                bookiesvec.add(b);
               }
            catch(Exception ex) {
            System.out.println("exception cms bookieid="+bookieid+".."+ex);
            }
        }
        return bookiesvec;
    }

    public boolean isLeagueIncluded(String leagueid)
    {
        String[] leagues = getSportsselected().split(",");
        for(int i=0;i < leagues.length; i++)
        {
            if(leagueid.equals(leagues[i]))
            {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSportsselected() {
        return sportsselected;
    }

    public void setSportsselected(String sportsselected) {
        this.sportsselected = sportsselected;
    }

    public Vector getSportsVec() {
        return sportsVec;
    }

    public void setSportsVec(Vector sportsVec) {
        this.sportsVec = sportsVec;
    }

    public Hashtable getBookievalues() {
        return bookievalues;
    }

    public String gethtmlbreakdown()
    {
        String html = "<table border=1>";
        Enumeration enum99 = bookievalues.keys();
        String bookieid = "";
        while(enum99.hasMoreElements())
        {
            try {
                bookieid = (String)enum99.nextElement();
                String bookmaker = (AppController.getBookie(Integer.parseInt(bookieid))).getShortname();
                int percentage = (int)bookievalues.get(bookieid);
                if(percentage > 0)
                html = html + "<tr><td>" + bookmaker + "</td><td>" + percentage + "%</td></tr>";
            }
            catch(Exception ex)
            {
                System.out.println("error getting bookieid "+bookieid+".."+ex);
            }
        }
        html = html+"</table>";
        return html;
    }

    public String getsportsselectedhtmlbreakdown()
    {
        String html = "<table border=1><th bgcolor=yellow><u>"+this.toString()+"</u></th>";

        String[] sports = getSportsselected().split(",");

        //Enumeration enum99 = sportsVec.elements();
        String leagueid = "";
        //while(enum99.hasMoreElements())
        for(int i= 0; i < sports.length; i++)
        {
            try {
                //leagueid = (String)enum99.nextElement();
                leagueid = sports[i];
                Sport s = AppController.getSportByLeagueId(Integer.parseInt(leagueid));
                html = html + "<tr><td>" + s.getLeaguename()+ "</td></tr>";
            }
            catch(Exception ex)
            {
                System.out.println("error getting leagueid "+leagueid+".."+ex);
            }
        }
        html = html+"</table>";
        return html;
    }
    public void setBookievalues(Hashtable bookievalues) {
        this.bookievalues = bookievalues;
    }

    @Override
    public String toString()
    {
        return name;
    }

}
