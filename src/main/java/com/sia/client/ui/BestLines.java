package com.sia.client.ui;

import com.sia.client.model.Bookie;
import com.sia.client.model.Moneyline;

import java.util.Vector;


public class BestLines
{



	public static void calculatebestspread(int gameid,int period)
	{
		Vector shownbookies = AppController.getShownCols();
		Vector fixedbookies = AppController.getFixedCols();
		
		Vector allcols = new Vector();
		allcols.addAll(shownbookies);
		allcols.addAll(fixedbookies);
		
		Spreadline bvsl = null;
		Spreadline bhsl = null; 	


		for(int i=0;i< allcols.size(); i++)
		{
			Bookie b = (Bookie)allcols.get(i);
			if(b.getBookie_id() >= 990) continue;
				
///////////////////////////////////spreadlines
			try
			{
					Spreadline sl = AppController.getSpreadline(b.getBookie_id(),gameid,period);
					sl.setBestVisitSpread(false);
					sl.setBestHomeSpread(false);					
					if(bvsl == null && sl.getCurrentvisitjuice() != 0)
					{
						bvsl = sl;
					}
					else if(sl.getCurrentvisitjuice() != 0)
					{
						if(sl.getCurrentvisitspread() > bvsl.getCurrentvisitspread() )
						{
							bvsl = sl;
						}
						else if(sl.getCurrentvisitspread() == bvsl.getCurrentvisitspread())
						{
							if(sl.getCurrentvisitjuice() > bvsl.getCurrentvisitjuice())
							{
								bvsl = sl;
							}
						}
					}
					
					
					
					if(bhsl == null && sl.getCurrenthomejuice() != 0)
					{
						bhsl = sl;
					}
					else if(sl.getCurrenthomejuice() != 0)
					{
						if(sl.getCurrenthomespread() > bhsl.getCurrenthomespread() )
						{
							bhsl = sl;
						}
						else if(sl.getCurrenthomespread() == bhsl.getCurrenthomespread())
						{
							if(sl.getCurrenthomejuice() > bhsl.getCurrenthomejuice())
							{
								bhsl = sl;
							}
						}
					}		
			
			}
			catch(Exception ex) {}		
		}
		if(bvsl != null) bvsl.setBestVisitSpread(true);
		if(bhsl != null) bhsl.setBestHomeSpread(true);

		
	}

	public static void calculatebesttotal(int gameid,int period)
	{
		Vector shownbookies = AppController.getShownCols();
		Vector fixedbookies = AppController.getFixedCols();
		
		Vector allcols = new Vector();
		allcols.addAll(shownbookies);
		allcols.addAll(fixedbookies);
		
		Totalline bo = null;
		Totalline bu = null; 	
 			
		for(int i=0;i< allcols.size(); i++)
		{
			Bookie b = (Bookie)allcols.get(i);
			if(b.getBookie_id() >= 990) continue;	
				try
				{
					Totalline tl = AppController.getTotalline(b.getBookie_id(),gameid,period);
					tl.setBestOver(false);
					tl.setBestUnder(false);	
					if(bo == null && tl.getCurrentoverjuice() != 0)
					{
						bo = tl;
					}
					else if(tl.getCurrentoverjuice() != 0)
					{
						if(tl.getCurrentover() < bo.getCurrentover() )
						{
							bo = tl;
						}
						else if(tl.getCurrentover() == bo.getCurrentover())
						{
							if(tl.getCurrentoverjuice() > bo.getCurrentoverjuice())
							{
								bo = tl;
							}
						}
					}
					
					
					if(bu == null && tl.getCurrentunderjuice() != 0)
					{
						bu = tl;
					}
					else if(tl.getCurrentunderjuice() != 0)
					{
						if(tl.getCurrentunder() > bu.getCurrentunder() )
						{
							bu = tl;
						}
						else if(tl.getCurrentunder() == bu.getCurrentunder())
						{
							if(tl.getCurrentunderjuice() > bu.getCurrentunderjuice())
							{
								bu = tl;
							}
						}
					}			
				}
				catch(Exception ex) {}

			}

		if(bo != null) bo.setBestOver(true);
		if(bu != null) bu.setBestUnder(true);
		
		
		
	}
	
	
	public static void calculatebestteamtotal(int gameid,int period)
	{
		Vector shownbookies = AppController.getShownCols();
		Vector fixedbookies = AppController.getFixedCols();
		
		Vector allcols = new Vector();
		allcols.addAll(shownbookies);
		allcols.addAll(fixedbookies);
		TeamTotalline bvo = null;
		TeamTotalline bvu = null; 	
		TeamTotalline bho = null;
		TeamTotalline bhu = null; 	
 			
		for(int i=0;i< allcols.size(); i++)
		{
			Bookie b = (Bookie)allcols.get(i);
			if(b.getBookie_id() >= 990) continue;
				try
				{
					TeamTotalline ttl = AppController.getTeamTotalline(b.getBookie_id(),gameid,period);
					ttl.setBestVisitOver(false);
					ttl.setBestVisitUnder(false);	
					ttl.setBestHomeOver(false);
					ttl.setBestHomeUnder(false);	
					if(bvo == null && ttl.getCurrentvisitoverjuice() != 0)
					{
						bvo = ttl;
					}
					else if(ttl.getCurrentvisitoverjuice() != 0)
					{
						if(ttl.getCurrentvisitover() < bvo.getCurrentvisitover() )
						{
							bvo = ttl;
						}
						else if(ttl.getCurrentvisitover() == bvo.getCurrentvisitover())
						{
							if(ttl.getCurrentvisitoverjuice() > bvo.getCurrentvisitoverjuice())
							{
								bvo = ttl;
							}
						}
					}
					
					
					if(bvu == null && ttl.getCurrentvisitunderjuice() != 0)
					{
						bvu = ttl;
					}
					else if(ttl.getCurrentvisitunderjuice() != 0)
					{
						if(ttl.getCurrentvisitunder() > bvu.getCurrentvisitunder() )
						{
							bvu = ttl;
						}
						else if(ttl.getCurrentvisitunder() == bvu.getCurrentvisitunder())
						{
							if(ttl.getCurrentvisitunderjuice() > bvu.getCurrentvisitunderjuice())
							{
								bvu = ttl;
							}
						}
					}	

					if(bho == null && ttl.getCurrenthomeoverjuice() != 0)
					{
						bho = ttl;
					}
					else if(ttl.getCurrenthomeoverjuice() != 0)
					{
						if(ttl.getCurrenthomeover() < bho.getCurrenthomeover() )
						{
							bho = ttl;
						}
						else if(ttl.getCurrenthomeover() == bho.getCurrenthomeover())
						{
							if(ttl.getCurrenthomeoverjuice() > bho.getCurrenthomeoverjuice())
							{
								bho = ttl;
							}
						}
					}
					
					
					if(bhu == null && ttl.getCurrenthomeunderjuice() != 0)
					{
						bhu = ttl;
					}
					else if(ttl.getCurrenthomeunderjuice() != 0)
					{
						if(ttl.getCurrenthomeunder() > bhu.getCurrenthomeunder() )
						{
							bhu = ttl;
						}
						else if(ttl.getCurrenthomeunder() == bhu.getCurrenthomeunder())
						{
							if(ttl.getCurrenthomeunderjuice() > bhu.getCurrenthomeunderjuice())
							{
								bhu = ttl;
							}
						}
					}			



					
				}
				catch(Exception ex) {}
			}	
				
		
		if(bvo != null) bvo.setBestVisitOver(true);
		if(bvu != null) bvu.setBestVisitUnder(true);
		if(bho != null) bho.setBestHomeOver(true);
		if(bhu != null) bhu.setBestHomeUnder(true);

	}	

	public static void calculatebestmoney(int gameid,int period)
	{
		Vector shownbookies = AppController.getShownCols();
		Vector fixedbookies = AppController.getFixedCols();
		
		Vector allcols = new Vector();
		allcols.addAll(shownbookies);
		allcols.addAll(fixedbookies);
		
		Moneyline bvml = null;
		Moneyline bhml = null; 	
		Moneyline bdml = null; 	
		for(int i=0;i< allcols.size(); i++)
		{
			Bookie b = (Bookie)allcols.get(i);
			if(b.getBookie_id() >= 990) continue;	
			try
			{
				Moneyline ml = AppController.getMoneyline(b.getBookie_id(),gameid,period);
				ml.setBestVisitMoney(false);
				ml.setBestHomeMoney(false);			
				if(bvml == null && ml.getCurrentvisitjuice() != 0)
				{
					bvml = ml;
				}
				else if(ml.getCurrentvisitjuice() != 0)
				{
					if(ml.getCurrentvisitjuice() > bvml.getCurrentvisitjuice() )
					{
						bvml = ml;
					}
				}
				
				
				if(bhml == null && ml.getCurrenthomejuice() != 0)
				{
					bhml = ml;
				}
				else if(ml.getCurrenthomejuice() != 0)
				{
					if(ml.getCurrenthomejuice() > bhml.getCurrenthomejuice() )
					{
						bhml = ml;
					}
				}				
				
				if(bdml == null && ml.getCurrentdrawjuice() != 0)
				{
					bdml = ml;
				}
				else  if(ml.getCurrentdrawjuice() != 0)
				{
					if(ml.getCurrentdrawjuice() > bdml.getCurrentdrawjuice() )
					{
						bdml = ml;
					}
				}						


			}
			catch(Exception ex) {}		
		}
		if(bvml != null) bvml.setBestVisitMoney(true);
		if(bhml != null) bhml.setBestHomeMoney(true);
		if(bdml != null) bdml.setBestDrawMoney(true);		
		
	}



	public static void calculatebestall(int gameid,int period)
	{
		Vector shownbookies = AppController.getShownCols();
		Vector fixedbookies = AppController.getFixedCols();
		
		Vector allcols = new Vector();
		allcols.addAll(shownbookies);
		allcols.addAll(fixedbookies);
		
		Spreadline bvsl = null;
		Spreadline bhsl = null; 	

		Totalline bo = null;
		Totalline bu = null; 	

		Moneyline bvml = null;
		Moneyline bhml = null; 	
		Moneyline bdml = null; 	
		
		
		TeamTotalline bvo = null;
		TeamTotalline bvu = null; 	
		TeamTotalline bho = null;
		TeamTotalline bhu = null; 	
		
		
		for(int i=0;i< allcols.size(); i++)
		{
			Bookie b = (Bookie)allcols.get(i);
			if(b.getBookie_id() >= 990) continue;
///////////////////////////////////spreadlines
			try
			{
					Spreadline sl = AppController.getSpreadline(b.getBookie_id(),gameid,period);
					sl.setBestVisitSpread(false);
					sl.setBestHomeSpread(false);					
					if(bvsl == null && sl.getCurrentvisitjuice() != 0)
					{
						bvsl = sl;
					}
					else if(sl.getCurrentvisitjuice() != 0)
					{
						if(sl.getCurrentvisitspread() > bvsl.getCurrentvisitspread() )
						{
							bvsl = sl;
						}
						else if(sl.getCurrentvisitspread() == bvsl.getCurrentvisitspread())
						{
							if(sl.getCurrentvisitjuice() > bvsl.getCurrentvisitjuice())
							{
								bvsl = sl;
							}
						}
					}
					
					
					
					if(bhsl == null && sl.getCurrenthomejuice() != 0)
					{
						bhsl = sl;
					}
					else if(sl.getCurrenthomejuice() != 0)
					{
						if(sl.getCurrenthomespread() > bhsl.getCurrenthomespread() )
						{
							bhsl = sl;
						}
						else if(sl.getCurrenthomespread() == bhsl.getCurrenthomespread())
						{
							if(sl.getCurrenthomejuice() > bhsl.getCurrenthomejuice())
							{
								bhsl = sl;
							}
						}
					}		
			
			}
			catch(Exception ex) {}
			
			
///////////////////////////////////moneylines

			try
			{
				Moneyline ml = AppController.getMoneyline(b.getBookie_id(),gameid,period);
				ml.setBestVisitMoney(false);
				ml.setBestHomeMoney(false);			
				if(bvml == null && ml.getCurrentvisitjuice() != 0)
				{
					bvml = ml;
				}
				else if(ml.getCurrentvisitjuice() != 0)
				{
					if(ml.getCurrentvisitjuice() > bvml.getCurrentvisitjuice() )
					{
						bvml = ml;
					}
				}
				
				
				if(bhml == null && ml.getCurrenthomejuice() != 0)
				{
					bhml = ml;
				}
				else if(ml.getCurrenthomejuice() != 0)
				{
					if(ml.getCurrenthomejuice() > bhml.getCurrenthomejuice() )
					{
						bhml = ml;
					}
				}				
				
				if(bdml == null && ml.getCurrentdrawjuice() != 0)
				{
					bdml = ml;
				}
				else  if(ml.getCurrentdrawjuice() != 0)
				{
					if(ml.getCurrentdrawjuice() > bdml.getCurrentdrawjuice() )
					{
						bdml = ml;
					}
				}						


			}
			catch(Exception ex) {}
///////////////////////////////////totalline

			
			try
			{
				Totalline tl = AppController.getTotalline(b.getBookie_id(),gameid,period);
				tl.setBestOver(false);
				tl.setBestUnder(false);	
				if(bo == null && tl.getCurrentoverjuice() != 0)
				{
					bo = tl;
				}
				else if(tl.getCurrentoverjuice() != 0)
				{
					if(tl.getCurrentover() < bo.getCurrentover() )
					{
						bo = tl;
					}
					else if(tl.getCurrentover() == bo.getCurrentover())
					{
						if(tl.getCurrentoverjuice() > bo.getCurrentoverjuice())
						{
							bo = tl;
						}
					}
				}
				
				
				if(bu == null && tl.getCurrentunderjuice() != 0)
				{
					bu = tl;
				}
				else if(tl.getCurrentunderjuice() != 0)
				{
					if(tl.getCurrentunder() > bu.getCurrentunder() )
					{
						bu = tl;
					}
					else if(tl.getCurrentunder() == bu.getCurrentunder())
					{
						if(tl.getCurrentunderjuice() > bu.getCurrentunderjuice())
						{
							bu = tl;
						}
					}
				}			
			}
			catch(Exception ex) {}
			
			
			

 			//teamtotal line
				try
				{
					TeamTotalline ttl = AppController.getTeamTotalline(b.getBookie_id(),gameid,period);
					ttl.setBestVisitOver(false);
					ttl.setBestVisitUnder(false);	
					ttl.setBestHomeOver(false);
					ttl.setBestHomeUnder(false);	
					if(bvo == null && ttl.getCurrentvisitoverjuice() != 0)
					{
						bvo = ttl;
					}
					else if(ttl.getCurrentvisitoverjuice() != 0)
					{
						if(ttl.getCurrentvisitover() < bvo.getCurrentvisitover() )
						{
							bvo = ttl;
						}
						else if(ttl.getCurrentvisitover() == bvo.getCurrentvisitover())
						{
							if(ttl.getCurrentvisitoverjuice() > bvo.getCurrentvisitoverjuice())
							{
								bvo = ttl;
							}
						}
					}
					
					
					if(bvu == null && ttl.getCurrentvisitunderjuice() != 0)
					{
						bvu = ttl;
					}
					else if(ttl.getCurrentvisitunderjuice() != 0)
					{
						if(ttl.getCurrentvisitunder() > bvu.getCurrentvisitunder() )
						{
							bvu = ttl;
						}
						else if(ttl.getCurrentvisitunder() == bvu.getCurrentvisitunder())
						{
							if(ttl.getCurrentvisitunderjuice() > bvu.getCurrentvisitunderjuice())
							{
								bvu = ttl;
							}
						}
					}	

					if(bho == null && ttl.getCurrenthomeoverjuice() != 0)
					{
						bho = ttl;
					}
					else if(ttl.getCurrenthomeoverjuice() != 0)
					{
						if(ttl.getCurrenthomeover() < bho.getCurrenthomeover() )
						{
							bho = ttl;
						}
						else if(ttl.getCurrenthomeover() == bho.getCurrenthomeover())
						{
							if(ttl.getCurrenthomeoverjuice() > bho.getCurrenthomeoverjuice())
							{
								bho = ttl;
							}
						}
					}
					
					
					if(bhu == null && ttl.getCurrenthomeunderjuice() != 0)
					{
						bhu = ttl;
					}
					else if(ttl.getCurrenthomeunderjuice() != 0)
					{
						if(ttl.getCurrenthomeunder() > bhu.getCurrenthomeunder() )
						{
							bhu = ttl;
						}
						else if(ttl.getCurrenthomeunder() == bhu.getCurrenthomeunder())
						{
							if(ttl.getCurrenthomeunderjuice() > bhu.getCurrenthomeunderjuice())
							{
								bhu = ttl;
							}
						}
					}			



					
				}
				catch(Exception ex) {}
			

		}
		//System.out.println("bestline "+gameid+".."+period+".."+bvsl);
		if(bvsl != null) bvsl.setBestVisitSpread(true);
		if(bhsl != null) bhsl.setBestHomeSpread(true);
		if(bo != null) bo.setBestOver(true);
		if(bu != null) bu.setBestUnder(true);
		if(bvml != null) bvml.setBestVisitMoney(true);
		if(bhml != null) bhml.setBestHomeMoney(true);
		if(bdml != null) bdml.setBestDrawMoney(true);
		if(bvo != null) bvo.setBestVisitOver(true);
		if(bvu != null) bvu.setBestVisitUnder(true);
		if(bho != null) bho.setBestHomeOver(true);
		if(bhu != null) bhu.setBestHomeUnder(true);

	if(bhsl != null)
	{
		//System.out.println("gameid="+gameid+"..bhsl="+bhsl.getBookieid()+"..bhsl="+bhsl.getCurrenthomespread()+bhsl.getCurrenthomejuice());
	}	

	}



}