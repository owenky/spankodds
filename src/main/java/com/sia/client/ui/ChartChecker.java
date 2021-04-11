package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.ChartData2;
import com.sia.client.model.ChartData3;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
public class ChartChecker
 {
	javax.swing.Timer timer;
	
	boolean firsttime = true;
	String url = "";
	String uname = "";
	String password = "";
	int numprocessed = 0;
	
	Scanner sc;
	public ChartChecker()
	{
		startOver();
	}
	
	
	static List <ChartData2> cl=new ArrayList<ChartData2>();
	static List <ChartData3> cl1=new ArrayList<ChartData3>();
	
	int gn,p,asa,hsa,oa,ua,ama,hma,dma,aTToa,aTTua,hTToa,hTTua,r1,r2;
	static int amt = 1;
	int count1;
	int count2;
	static String filename="No File Selected";
	static int updatetime=5;
	static String errormsg;
	
	public void startOver()
	{
		System.out.println("Starting over chart checker..");
		try
		{
			amt = AppController.getUser().getChartMinAmtNotify();
			if(timer != null && timer.isRunning()) timer.stop();
			timer = new javax.swing.Timer(AppController.getUser().getChartSecsRefresh()*1000, new ActionListener() {
				public void actionPerformed(ActionEvent evt) 
					{
						if(numprocessed++ % 50 == 0)
						{
							cl.clear();
							cl1.clear();
							firsttime = true;
							System.out.println("Processing chartchecker file="+AppController.getUser().getChartFileName()+"..secs="+AppController.getUser().getChartSecsRefresh()+"..amt="+AppController.getUser().getChartMinAmtNotify()+".."+numprocessed+" at "+new java.util.Date());
						}
						init();
						//processData();
					}    
				});	
			timer.start();	
		}
		catch(Exception ex)
		{
			System.out.println("exception starting timer "+ex);
			//startOver();
		}
		
	}
	
	
	
	
	
	public void init()
	{
		try{
			
				//*******************************Url Data Reading********************************		
				
				if(!AppController.getUser().getChartFileName().endsWith("csv"))
				{
					try
					{
						 filename=AppController.getUser().getChartFileName();
						 // TODO temp
						filename = filename.substring(3);
						// end of TODO
						URL resource = Utils.getConfigResource(filename);
						File f=new File(resource.toURI());
						sc = new Scanner(f);    
						 sc.useDelimiter("\n");  
						 url=sc.next();
						 uname=sc.next();
						 password=sc.next();
						 cl.clear();
						// cl1.clear();
							prepareURLData(url,uname,password);	
						 sc.close();	
							//System.out.println("**************************************iam from Url Now**********************");
					}
					catch(Exception e)
					{
						System.out.println("ERROR OPENING FILE..."+AppController.getUser().getChartFileName());
						errormsg="Please check your URL/USER NAME/PASSWORD";
						//new ShowError().setVisible(true);
						cl.clear();
					}
				}				
//*******************************Local file Reading********************************				
				else
				{
					cl.clear();
					//cl1.clear();
			//	System.out.println("**************************************iam from Local file Now**********************");
				//amt=com.sia.client.ui.ChartHome.warnamount;
				filename=AppController.getUser().getChartFileName();
				File f=new File(filename);
				sc = new Scanner(f);  
				sc.useDelimiter("\n");  
				count1=0;
	
					while(sc.hasNext())
					{
						count1++;
						String s=sc.next();
						int ln=(int)s.charAt(0);
						if(ln==13){
							break;
						}
						String[] s1=s.split(",",13) ;
						ChartData2 cd=new ChartData2();
						if(s1.length<13)
						{
							errormsg="Data not proper at line-"+count1+" please check your Local File";
							//new ShowError().setVisible(true);
							cl.clear();
							//Thread.currentThread().stop();
							return;
							}
						gn=Integer.parseInt(s1[0]);
						p=Integer.parseInt(s1[1]);
						cd.gn=gn;
						cd.p=p;
						int found=0;
						Iterator itr=cl.iterator();
						while(itr.hasNext())
						{
							ChartData2 cd2=(ChartData2)itr.next();
							if(cd2.gn==gn&&cd2.p==p)
							{
								found=1;
								break;
							}
							else{
								continue;
							}
						}
						if(found==1)
						{
							continue;
						}
						else{
								cl.add(cd);
							}
					}
					sc.close();
					File f1=new File(filename);
					Scanner sc1 = new Scanner(f1);  
					sc1.useDelimiter("\n"); 
					count2=0;
					while(sc1.hasNext())
					{
						count2++;
						String s=sc1.next();
						int idx=s.length()-1;
						int asc=s.charAt(idx);
						int ln=(int)s.charAt(0);
						if(ln==13)
						{
							break;
						}
						if(asc==13)
						{
							StringBuffer sb=new StringBuffer(s);
							sb.insert(s.length()-1,',');
							s=(String)sb.toString();
						}
						String[] s1=s.split(",",14) ;
						gn=Integer.parseInt(s1[0]);
						p=Integer.parseInt(s1[1]);
						asa=Integer.parseInt(s1[2]);
						hsa=Integer.parseInt(s1[3]);
						oa=Integer.parseInt(s1[4]);
						ua=Integer.parseInt(s1[5]);
						ama=Integer.parseInt(s1[6]);
						hma=Integer.parseInt(s1[7]);
						dma=Integer.parseInt(s1[8]);
						aTToa=Integer.parseInt(s1[9]);
						aTTua=Integer.parseInt(s1[10]);
						hTToa=Integer.parseInt(s1[11]);
						hTTua=Integer.parseInt(s1[12]);
						for(int i=0;i<cl.size();i++)
						{
							if(cl.get(i).gn==gn&&cl.get(i).p==p)
							{
								cl.get(i).asaT=cl.get(i).asaT+asa;
								cl.get(i).hsaT=cl.get(i).hsaT+hsa;
								cl.get(i).oaT=cl.get(i).oaT+oa;
								cl.get(i).uaT=cl.get(i).uaT+ua;
								cl.get(i).amaT=cl.get(i).amaT+ama;
								cl.get(i).hmaT=cl.get(i).hmaT+hma;
								cl.get(i).drawMamt=dma;
								cl.get(i).aoaT=cl.get(i).aoaT+aTToa;
								cl.get(i).auaT=cl.get(i).auaT+aTTua;
								cl.get(i).hoaT=cl.get(i).hoaT+hTToa;
								cl.get(i).huaT=cl.get(i).huaT+hTTua;
							}
						}
					
					}
							
					sc1.close();
				} // end local file reading
	/***************************END READING FROM URL AND FILE*********************/		
			for(int i=0;i<cl.size();i++)
				{
					//System.out.println(i+"CL="+cl.get(i));
					int found=0;
					int gn=cl.get(i).gn;
					int p=cl.get(i).p;
					ChartData3 cd3=new ChartData3();
					cd3.gn=gn;
					cd3.p=p;
					Iterator itr1=cl1.iterator();
					while(itr1.hasNext())
					{
						ChartData3 cd4=(ChartData3)itr1.next();
						if(cd4.gn==gn&&cd4.p==p)
						{
							found=1;
							break;
						}
						else{
							continue;
						}
					}
					if(found==1)
					{
						continue;
					}
					else{
							cl1.add(cd3);
						}
				}
			
			
				//for(int i=0;i<cl.size();i++)
				for(int j=0;j<cl1.size();j++)
					{
						//System.out.println(j+"CL1="+cl1.get(j));
						int lamt=0;
				//		int j;
				//		for(j=0;j<cl1.size();j++)
				int i;
				cl1.get(j).dataexists = false;
						for(i=0;i<cl.size();i++)
						{
							if(cl.get(i).gn==cl1.get(j).gn&&cl.get(i).p==cl1.get(j).p)
							{
								cl1.get(j).dataexists = true;
								break;
							}
						}
						if(!cl1.get(j).dataexists)
						{
							
							break;
						}
					//**********Side deff***************
						int lds=java.lang.Math.abs(cl.get(i).asaT-cl.get(i).hsaT);
						if(lds>cl1.get(j).DS&&!firsttime)
						{
							cl1.get(j).spreadicon=ChartView.ICON_UP;
							
						}
						if(lds<cl1.get(j).DS&&!firsttime){
							cl1.get(j).spreadicon=ChartView.ICON_DOWN;
							
						}
						
						lamt=java.lang.Math.abs(lds-cl1.get(j).DS);
						if(lamt>=amt&&!firsttime)
						{
							cl1.get(j).isGreat1=true;
							cl1.get(j).spreadcolor=Color.RED;
							
						}
						else if(cl1.get(j).isGreat1)
						{
							cl1.get(j).spreadcolor=Color.BLACK;
						}
										
						if(cl.get(i).asaT>cl.get(i).hsaT)
						{
							
							cl.get(i).DS=(cl.get(i).asaT-cl.get(i).hsaT)+"a";
						}
						else if(cl.get(i).hsaT>cl.get(i).asaT){
							
							cl.get(i).DS=(cl.get(i).hsaT-cl.get(i).asaT)+"h";
						}
						else{
						cl.get(i).DS="";
						}
						cl1.get(j).NDS=cl.get(i).DS;
						cl1.get(j).DS=lds;
						
					//**********Total deff***************
					int ldt=java.lang.Math.abs(cl.get(i).oaT-cl.get(i).uaT);
						if(ldt>cl1.get(j).DT&&!firsttime)
						{
							cl1.get(j).totalicon=ChartView.ICON_UP;
							
						}
						if(ldt<cl1.get(j).DT&&!firsttime){
							cl1.get(j).totalicon=ChartView.ICON_DOWN;
							
						}
						lamt=java.lang.Math.abs(ldt-cl1.get(j).DT);
						if(lamt>=amt&&!firsttime)
						{
							cl1.get(j).isGreat2=true;
							cl1.get(j).totalcolor=Color.RED;
						}
						else if(cl1.get(j).isGreat2)
						{
							cl1.get(j).totalcolor=Color.BLACK;
						}
										
						if(cl.get(i).oaT>cl.get(i).uaT)
						{
							
							cl.get(i).DT=(cl.get(i).oaT-cl.get(i).uaT)+"o";
						}
						else if(cl.get(i).uaT>cl.get(i).oaT){
							
							cl.get(i).DT=(cl.get(i).uaT-cl.get(i).oaT)+"u";
						}
						else{
						cl.get(i).DT="";
						}
						cl1.get(j).NDT=cl.get(i).DT;
						cl1.get(j).DT=ldt;
						//**********MoneyLine deff***************
						int ldm=java.lang.Math.abs(cl.get(i).amaT-cl.get(i).hmaT);
						if(ldm>cl1.get(j).DM&&!firsttime)
						{
							cl1.get(j).moneyicon=ChartView.ICON_UP;
							
						}
						if(ldm<cl1.get(j).DM&&!firsttime){
							cl1.get(j).moneyicon=ChartView.ICON_DOWN;
							
						}
						lamt=java.lang.Math.abs(ldm-cl1.get(j).DM);
						if(lamt>=amt&&!firsttime)
						{
							cl1.get(j).isGreat3=true;
							cl1.get(j).moneycolor=Color.RED;
						}
						else if(cl1.get(j).isGreat3)
						{
							cl1.get(j).moneycolor=Color.BLACK;
						}
						
						if(cl.get(i).amaT>cl.get(i).hmaT)
						{
							
							cl.get(i).DM=(cl.get(i).amaT-cl.get(i).hmaT)+"a";
						}
						else if(cl.get(i).hmaT>cl.get(i).amaT){
							
							cl.get(i).DM=(cl.get(i).hmaT-cl.get(i).amaT)+"h";
						}
						else
						{
						cl.get(i).DM="";
						}
						cl1.get(j).NDM=cl.get(i).DM;
						cl1.get(j).DM=ldm;
						//**********Away over and under deff***************
						int lda=java.lang.Math.abs(cl.get(i).aoaT-cl.get(i).auaT);
						if(lda>cl1.get(j).DA&&!firsttime)
						{
							cl1.get(j).awayicon=ChartView.ICON_UP;
							
						}
						if(lda<cl1.get(j).DA&&!firsttime){
							cl1.get(j).awayicon=ChartView.ICON_DOWN;
							
							
						}
						lamt=java.lang.Math.abs(lda-cl1.get(j).DA);
						if(lamt>=amt&&!firsttime)
						{
							cl1.get(j).isGreat4=true;
							cl1.get(j).awaycolor=Color.RED;
						}
						else if(cl1.get(j).isGreat4)
						{
							cl1.get(j).awaycolor=Color.BLACK;
						}
						
						if(cl.get(i).aoaT>cl.get(i).auaT)
						{
							
							cl.get(i).DA=(cl.get(i).aoaT-cl.get(i).auaT)+"o";
						}
						else if(cl.get(i).auaT>cl.get(i).aoaT){
							
							cl.get(i).DA=(cl.get(i).auaT-cl.get(i).aoaT)+"u";
						}
						else
						{
						cl.get(i).DA="";
						}
						cl1.get(j).NDA=cl.get(i).DA;
						cl1.get(j).DA=lda;
						//**********Home over and under deff***************
						int ldh=java.lang.Math.abs(cl.get(i).hoaT-cl.get(i).huaT);
						if(ldh>cl1.get(j).DH&&!firsttime)
						{
							cl1.get(j).homeicon=ChartView.ICON_UP;
							
							
						}
						if(ldh<cl1.get(j).DH&&!firsttime){
							cl1.get(j).homeicon=ChartView.ICON_DOWN;	
						}
						lamt=java.lang.Math.abs(ldh-cl1.get(j).DH);
						if(lamt>=amt&&!firsttime)
						{
							cl1.get(j).isGreat5=true;
							cl1.get(j).homecolor=Color.RED;			
						}
						else if(cl1.get(j).isGreat5)
						{
							cl1.get(j).homecolor=Color.BLACK;
						}						
						
						if(cl.get(i).hoaT>cl.get(i).huaT)
						{
							
							cl.get(i).DH=(cl.get(i).hoaT-cl.get(i).huaT)+"o";
						}
						else if(cl.get(i).huaT>cl.get(i).hoaT){
							
							cl.get(i).DH=(cl.get(i).huaT-cl.get(i).hoaT)+"u";
						}
						else{
						cl.get(i).DH="";
						}
						cl1.get(j).NDH=cl.get(i).DH;
						cl1.get(j).DH=ldh;
					}
				if(firsttime)
				{
					firsttime = false;
				}



			
	
		}
	catch(Exception ex)
	{
		System.out.println("exception init chart data "+ex);
	}
  }		
				

	
	//Method for preparing the url data into chart
	public void prepareURLData(String urlStr, String user, String pass)
	{
		
			try {
        // URL url = new URL ("http://69.125.22.204:9452/tcscsvdbfeed.txt");
        URL url = new URL(urlStr);
        String authStr = user + ":" + pass;
        String authEncoded = Base64.getEncoder().encodeToString(authStr.getBytes());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + authEncoded);

      
        InputStream in = (InputStream) connection.getInputStream();
      
	   String URLData="";
        for (int b; (b = in.read()) != -1;) {
			char ch=(char)b;
			
			 URLData=URLData+ch;
			         
        }
		 in.close();
        String[] line = URLData.split("\n");
		//System.out.println(line[0]);
       for(int i=0;i<line.length;i++)
		{
			
			String s=line[i];
			//System.out.println("processing line="+s);
			int ln=(int)s.charAt(0);
			if(ln==13){
				break;
			}
			String[] s1=s.split(",",13) ;
			ChartData2 cd=new ChartData2();
			if(s1.length<13)
			{
				errormsg="Data not proper at line please check your Url File";
				//new ShowError().setVisible(true);
				cl.clear();
				cl1.clear();
				//Thread.currentThread().stop();
				return;
			}
			int gn=Integer.parseInt(s1[0]);
			int p=Integer.parseInt(s1[1]);
			cd.gn=gn;
			cd.p=p;
			int found=0;
			Iterator itr=cl.iterator();
			while(itr.hasNext())
			{
				ChartData2 cd2=(ChartData2)itr.next();
				if(cd2.gn==gn&&cd2.p==p)
				{
					found=1;
					break;
				}
				else{
					continue;
				}
			}
			if(found==1)
			{
				continue;
			}
			else{
					cl.add(cd);
				}
		}
	
	for(int i1=0;i1<line.length;i1++)
		{
			
			String s=line[i1];
			int idx=s.length()-1;
			int asc=s.charAt(idx);
			int ln=(int)s.charAt(0);
			if(ln==13){
				break;
			}
			if(asc==13)
			{
				StringBuffer sb=new StringBuffer(s);
				sb.insert(s.length()-1,',');
				s=(String)sb.toString();
			}
			String[] s1=s.split(",",14) ;
			int	gn=Integer.parseInt(s1[0]);
			int	p=Integer.parseInt(s1[1]);
			int	asa=Integer.parseInt(s1[2]);
			int	hsa=Integer.parseInt(s1[3]);
			int	oa=Integer.parseInt(s1[4]);
			int	ua=Integer.parseInt(s1[5]);
			int	ama=Integer.parseInt(s1[6]);
			int	hma=Integer.parseInt(s1[7]);
			int	dma=Integer.parseInt(s1[8]);
			int	aTToa=Integer.parseInt(s1[9]);
			int	aTTua=Integer.parseInt(s1[10]);
			int	hTToa=Integer.parseInt(s1[11]);
			int	hTTua=Integer.parseInt(s1[12]);
				for(int i=0;i<cl.size();i++)
				{
					if(cl.get(i).gn==gn&&cl.get(i).p==p)
					{
						
						cl.get(i).asaT=cl.get(i).asaT+asa;
						cl.get(i).hsaT=cl.get(i).hsaT+hsa;
						cl.get(i).oaT=cl.get(i).oaT+oa;
						cl.get(i).uaT=cl.get(i).uaT+ua;
						cl.get(i).amaT=cl.get(i).amaT+ama;
						cl.get(i).hmaT=cl.get(i).hmaT+hma;
						cl.get(i).drawMamt=dma; // owen why is this not added like the other ones??
						cl.get(i).aoaT=cl.get(i).aoaT+aTToa;
						cl.get(i).auaT=cl.get(i).auaT+aTTua;
						cl.get(i).hoaT=cl.get(i).hoaT+hTToa;
						cl.get(i).huaT=cl.get(i).huaT+hTTua;
					}
				}
		}
	
    }
    catch (Exception e) 
	{
		System.out.println("URL BAD! "+e+" "+new java.util.Date());
		cl.clear();
		cl1.clear();
		//errormsg="Plese check your URL/USER NAME/PASSWORD";
      //new ShowError().setVisible(true);
    }
		
 }		
		
		
		
}
	
	
