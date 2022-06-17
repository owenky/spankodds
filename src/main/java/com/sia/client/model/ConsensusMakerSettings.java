package com.sia.client.model;

import com.sia.client.ui.AppController;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

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

   public boolean isLeagueIncluded(int leagueid)
   {
       if(sportsVec.contains(leagueid+""))
       {
           return true;
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
        String html = "<table>";
        Enumeration enum99 = bookievalues.keys();
        String bookieid = "";
        while(enum99.hasMoreElements())
        {
            try {
                bookieid = (String)enum99.nextElement();
                String bookmaker = AppController.getBookie(Integer.parseInt(bookieid)).getShortname();
                String percentage = (String)bookievalues.get(bookieid);
                html = html + "<tr><td>" + bookmaker + "</td><td>" + percentage + "</td></tr>";
            }
            catch(Exception ex)
            {
                System.out.println("error getting bookieid "+bookieid+".."+ex);
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