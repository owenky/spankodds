package com.sia.client.model;

import com.sia.client.ui.AppController;
import com.sia.client.ui.TeamTotalline;
import com.sia.client.ui.Totalline;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import static com.sia.client.config.Utils.log;


public class BestLines {

	static int bestbookieid = 996;
	static int consensusbookieid = 997;

	static double upanddownforalt = 1.0;


	public static void calculateconsensusspread(int gameid, int period)
	{
		double defaulthold = .0454;
		if(AppController.getUserDisplaySettings().getConsensusnovig())
		{
			defaulthold = 0;
		}
		Game g = AppController.getGame(gameid);
		int leagueid = g.getLeague_id();
		double[] pusharray = LinesMoves.getleagueidArray(leagueid,period,"SPREAD");
		if(pusharray == null) return;
		int bookloop;
		double althigh;
		double altlow;
		double bestalthigh;
		double bestaltlow;
		ConsensusMakerSettings cms = AppController.getConsensusMakerSettingsForThisGame(gameid);
		if(cms == null)
		{
			return;
		}
		Vector<Bookie> bookiesvec = cms.getBookiesVec();

		if(bookiesvec.size() == 0) return;
		Vector<Spreadline> spreadlinevec = new Vector();
		Vector spreadlineweightsvec = new Vector();
		Iterator iter = bookiesvec.iterator();
		double deadpercentages = 0.0;
		Vector visitorssvec = new Vector();
		while(iter.hasNext())
		{
			Bookie b = (Bookie)iter.next();
			Spreadline sl = AppController.getSpreadline(b.getBookie_id(),gameid,period);
			if(sl == null || sl.getCurrentvisitjuice() == 0 || sl.getCurrenthomejuice() == 0)
			{
				int thisbmpercent = cms.getValue(b.getBookie_id());
				deadpercentages = deadpercentages + thisbmpercent;

				//adjust every percentage by percentage/(1-deadpercentage)
				//make sure this line is not factored in
			}
			else
			{
				spreadlinevec.add(sl);
				visitorssvec.add(sl.getCurrentvisitspread());
				spreadlineweightsvec.add((double)cms.getValue(b.getBookie_id()));
			}
		}
		if(visitorssvec.size() == 0) return;
		double baseindex = mode(visitorssvec);
		double bestbaseindex = baseindex;
		if(deadpercentages != 0)
		{
			double adjustment = 100-deadpercentages;
			if(gameid == 25)
			{
				log("adjustment="+adjustment);
			}
			Vector spreadlineweightsvecadjusted = new Vector();
			Iterator iter2 = spreadlineweightsvec.iterator();
			while(iter2.hasNext())
			{
				double weight = (double)iter2.next();
				weight = 100*weight/adjustment;
				spreadlineweightsvecadjusted.add(weight);
			}
			spreadlineweightsvec = spreadlineweightsvecadjusted;
		}

		double convertedwinrates[] = new double[spreadlineweightsvec.size()];
		double bookwinrates[] = new double[spreadlineweightsvec.size()];
		double spankywinrate = 0;
		for(int i =0; i < spreadlineweightsvec.size(); i++)
		{
			//spank left off
			Spreadline sl = spreadlinevec.elementAt(i);
			double[] doublearr = {sl.getCurrentvisitjuice(),sl.getCurrenthomejuice()};
			bookwinrates[i] = marketToWinRate(doublearr);
			convertedwinrates[i] = newWinRate(sl.getCurrentvisitspread(),bookwinrates[i],baseindex,pusharray);
			if(gameid == 25)
			{
				//log(i+"conv win rate="+convertedwinrates[i]+"..weight="+totallineweightsvec.elementAt(i));
			}
		}
		for(int i =0; i < spreadlineweightsvec.size(); i++)
		{
			spankywinrate = spankywinrate + convertedwinrates[i]*(double)spreadlineweightsvec.elementAt(i)/100.00;
		}

		for(double i= .5; i <= upanddownforalt; i = i+.5)
		{
			althigh = newWinRate(baseindex,spankywinrate,baseindex+upanddownforalt,pusharray);
			altlow = newWinRate(baseindex,spankywinrate,baseindex-upanddownforalt,pusharray);
			if(Math.abs(.5-althigh) < Math.abs(.5-spankywinrate))
			{
				bestbaseindex = baseindex+upanddownforalt;
				spankywinrate = althigh;
			}
			if(Math.abs(.5-altlow) < Math.abs(.5-spankywinrate))
			{
				bestbaseindex = baseindex-upanddownforalt;
				spankywinrate = altlow;
			}
		}




		//double[] spankymoneyline = winRateToMoneyLine(spankywinrate,defaulthold);
		double[] spankymoneyline = winRateToMoneyLine(spankywinrate,defaulthold);
		Spreadline consensussl = AppController.getSpreadline(consensusbookieid, gameid, period);
		double visitspread = bestbaseindex;
		double homespread = bestbaseindex*-1;
		if(consensussl == null)
		{
			AppController.addSpreadline(new Spreadline(gameid,consensusbookieid,visitspread,spankymoneyline[0],homespread,spankymoneyline[1],0,period));
		}
		else
		{
			consensussl.setCurrentvisitspread(visitspread);
			consensussl.setCurrentvisitjuice(spankymoneyline[0]);
			consensussl.setCurrenthomespread(homespread);
			consensussl.setCurrenthomejuice(spankymoneyline[1]);
		}
		if(gameid >= 901 && gameid <= 999)
		{
			//log(gameid+"..cms="+bestbaseindex+"o"+spankymoneyline[0]+"/u"+spankymoneyline[1]);
		}



	}
	public static void calculateconsensustotal(int gameid, int period)
	{
		double defaulthold = .0454;
		if(AppController.getUserDisplaySettings().getConsensusnovig())
		{
			defaulthold = 0;
		}
		Game g = AppController.getGame(gameid);
		int leagueid = g.getLeague_id();
		double[] pusharray = LinesMoves.getleagueidArray(leagueid,period,"TOTAL");
		if(pusharray == null) return;
		int bookloop;
		double althigh;
		double altlow;
		double bestalthigh;
		double bestaltlow;

		ConsensusMakerSettings cms = AppController.getConsensusMakerSettingsForThisGame(gameid);
		if(cms == null)
		{
			return;
		}
		Vector<Bookie> bookiesvec = cms.getBookiesVec();
		if(bookiesvec.size() == 0) return;
		Vector<Totalline>totallinevec = new Vector();
		Vector totallineweightsvec = new Vector();
		Iterator iter = bookiesvec.iterator();
		double deadpercentages = 0.0;
		Vector oversvec = new Vector();
		while(iter.hasNext())
		{
			Bookie b = (Bookie)iter.next();
			Totalline tl = AppController.getTotalline(b.getBookie_id(),gameid,period);
			if(tl == null || tl.getCurrentoverjuice() == 0 || tl.getCurrentunderjuice() == 0)
			{
				int thisbmpercent = cms.getValue(b.getBookie_id());
				deadpercentages = deadpercentages + thisbmpercent;

				//adjust every percentage by percentage/(1-deadpercentage)
				//make sure this line is not factored in
			}
			else
			{
				totallinevec.add(tl);
				oversvec.add(tl.getCurrentover());
				totallineweightsvec.add((double)cms.getValue(b.getBookie_id()));
			}
		}
		if(oversvec.size() == 0) return;
		double baseindex = mode(oversvec);
		double bestbaseindex = baseindex;
		if(deadpercentages != 0)
		{
			double adjustment = 100-deadpercentages;
			if(gameid == 25)
			{
				log("adjustment="+adjustment);
			}
			Vector totallineweightsvecadjusted = new Vector();
			Iterator iter2 = totallineweightsvec.iterator();
			while(iter2.hasNext())
			{
				double weight = (double)iter2.next();
				weight = 100*weight/adjustment;
				totallineweightsvecadjusted.add(weight);
			}
			totallineweightsvec = totallineweightsvecadjusted;
		}

		double convertedwinrates[] = new double[totallineweightsvec.size()];
		double bookwinrates[] = new double[totallineweightsvec.size()];
		double spankywinrate = 0;
		for(int i =0; i < totallineweightsvec.size(); i++)
		{
			//spank left off
			Totalline tl = totallinevec.elementAt(i);
			double[] doublearr = {tl.getCurrentoverjuice(),tl.getCurrentunderjuice()};
			bookwinrates[i] = marketToWinRate(doublearr);
			convertedwinrates[i] = newWinRate(tl.getCurrentover(),bookwinrates[i],baseindex,pusharray);
			if(gameid == 25)
			{
				//log(i+"conv win rate="+convertedwinrates[i]+"..weight="+totallineweightsvec.elementAt(i));
			}
		}
		for(int i =0; i < totallineweightsvec.size(); i++)
		{
			spankywinrate = spankywinrate + convertedwinrates[i]*(double)totallineweightsvec.elementAt(i)/100.00;
		}

		for(double i= .5; i <= upanddownforalt; i = i+.5)
		{
			althigh = newWinRate(baseindex,spankywinrate,baseindex+upanddownforalt,pusharray);
			altlow = newWinRate(baseindex,spankywinrate,baseindex-upanddownforalt,pusharray);
			if(Math.abs(.5-althigh) < Math.abs(.5-spankywinrate))
			{
				bestbaseindex = baseindex+upanddownforalt;
				spankywinrate = althigh;
			}
			if(Math.abs(.5-altlow) < Math.abs(.5-spankywinrate))
			{
				bestbaseindex = baseindex-upanddownforalt;
				spankywinrate = altlow;
			}
		}




		//double[] spankymoneyline = winRateToMoneyLine(spankywinrate,defaulthold);
		double[] spankymoneyline = winRateToMoneyLine(spankywinrate,defaulthold);
		Totalline consensustl = AppController.getTotalline(consensusbookieid, gameid, period);
		if(consensustl == null)
		{
			AppController.addTotalline(new Totalline(gameid,consensusbookieid,bestbaseindex,spankymoneyline[0],bestbaseindex,spankymoneyline[1],0,period));
		}
		else
		{
			consensustl.setCurrentover(bestbaseindex);
			consensustl.setCurrentunder(bestbaseindex);
			consensustl.setCurrentoverjuice(spankymoneyline[0]);
			consensustl.setCurrentunderjuice(spankymoneyline[1]);

		}
		if(gameid >= 901 && gameid <= 999)
		{
			//log(gameid+"..cms="+bestbaseindex+"o"+spankymoneyline[0]+"/u"+spankymoneyline[1]);
		}

	}
	public static void calculateconsensusmoney(int gameid, int period)
	{
		double defaulthold = .0454;
		if(AppController.getUserDisplaySettings().getConsensusnovig())
		{
			defaulthold = 0;
		}
		ConsensusMakerSettings cms = AppController.getConsensusMakerSettingsForThisGame(gameid);

		if(cms == null)
		{
			return;
		}
		Vector<Bookie> bookiesvec = cms.getBookiesVec();
		if(bookiesvec.size() == 0) return;
		Vector<Moneyline>moneylinevec = new Vector();
		Vector moneylineweightsvec = new Vector();
		Iterator iter = bookiesvec.iterator();
		double deadpercentages = 0.0;
		while(iter.hasNext())
		{
			Bookie b = (Bookie)iter.next();
			Moneyline ml = AppController.getMoneyline(b.getBookie_id(),gameid,period);
			if(ml == null || ml.getCurrentvisitjuice() == 0 || ml.getCurrenthomejuice() == 0)
			{
				int thisbmpercent = cms.getValue(b.getBookie_id());
				deadpercentages = deadpercentages + thisbmpercent;

				//adjust every percentage by percentage/(1-deadpercentage)
				//make sure this line is not factored in
			}
			else
			{
				moneylinevec.add(ml);
				moneylineweightsvec.add((double)cms.getValue(b.getBookie_id()));
			}
		}
		if(moneylinevec.size() == 0) return;
		if(gameid == 25)
		{
			log("deadpercentages="+deadpercentages);
		}
		if(deadpercentages != 0)
		{
			double adjustment = 100-deadpercentages;
			if(gameid == 25)
			{
				log("adjustment="+adjustment);
			}
			Vector moneylineweightsvecadjusted = new Vector();
			Iterator iter2 = moneylineweightsvec.iterator();
			while(iter2.hasNext())
			{
				double weight = (double)iter2.next();
				weight = 100*weight/adjustment;
				moneylineweightsvecadjusted.add(weight);
			}
			moneylineweightsvec = moneylineweightsvecadjusted;
		}

		double convertedwinrates[] = new double[moneylineweightsvec.size()];
		double spankywinrate = 0;
	 for(int i =0; i < moneylineweightsvec.size(); i++)
	 {
	 	Moneyline ml = moneylinevec.elementAt(i);
		 double[] doublearr = {ml.getCurrentvisitjuice(),ml.getCurrenthomejuice()};
	 	convertedwinrates[i] = marketToWinRate(doublearr);
		 if(gameid == 25)
		 {
			 log(i+"conv win rate="+convertedwinrates[i]+"..weight="+moneylineweightsvec.elementAt(i));
		 }
	 }
		for(int i =0; i < moneylineweightsvec.size(); i++)
		{
			spankywinrate = spankywinrate + convertedwinrates[i]*(double)moneylineweightsvec.elementAt(i)/100.00;
		}
		//double[] spankymoneyline = winRateToMoneyLine(spankywinrate,defaulthold);
		double[] spankymoneyline = winRateToMoneyLine(spankywinrate,defaulthold);
		Moneyline consensusml = AppController.getMoneyline(consensusbookieid, gameid, period);
		if(consensusml == null)
		{
			AppController.addMoneyline(new Moneyline(gameid,consensusbookieid,spankymoneyline[0],spankymoneyline[1],-99999,0,period));
		}
		else
		{
			consensusml.setCurrentvisitjuice(spankymoneyline[0]);
			consensusml.setCurrenthomejuice(spankymoneyline[1]);

		}
		if(gameid == 25)
		{
			log("cms="+spankymoneyline[0]+"/"+spankymoneyline[1]);
		}

	}
	public static void calculateconsensusteamtotal(int gameid, int period)
	{
		double defaulthold = .0454;
		if(AppController.getUserDisplaySettings().getConsensusnovig())
		{
			defaulthold = 0;
		}
		Game g = AppController.getGame(gameid);
		int leagueid = g.getLeague_id();
		double[] pusharray = LinesMoves.getleagueidArray(leagueid,period,"TEAMTOTAL");
		if(pusharray == null) return;
		int bookloop;
		double althighvisit;
		double altlowvisit;
		double bestalthighvisit;
		double bestaltlowvisit;
		double althighhome;
		double altlowhome;
		double bestalthighhome;
		double bestaltlowhome;

		ConsensusMakerSettings cms = AppController.getConsensusMakerSettingsForThisGame(gameid);
		if(cms == null)
		{
			return;
		}
		Vector<Bookie> bookiesvec = cms.getBookiesVec();
		if(bookiesvec.size() == 0) return;
		Vector<TeamTotalline>teamtotallinevec = new Vector();
		Vector teamtotallineweightsvec = new Vector();
		Iterator iter = bookiesvec.iterator();
		double deadpercentages = 0.0;
		Vector visitoversvec = new Vector();
		Vector homeoversvec = new Vector();
		while(iter.hasNext())
		{
			Bookie b = (Bookie)iter.next();
			TeamTotalline tl = AppController.getTeamTotalline(b.getBookie_id(),gameid,period);
			if(tl == null || tl.getCurrentvisitoverjuice() == 0 || tl.getCurrentvisitunderjuice() == 0
			|| tl.getCurrenthomeoverjuice() == 0 || tl.getCurrenthomeunderjuice() == 0)
			{
				int thisbmpercent = cms.getValue(b.getBookie_id());
				deadpercentages = deadpercentages + thisbmpercent;

				//adjust every percentage by percentage/(1-deadpercentage)
				//make sure this line is not factored in
			}
			else
			{
				teamtotallinevec.add(tl);
				visitoversvec.add(tl.getCurrentvisitover());
				homeoversvec.add(tl.getCurrenthomeover());
				teamtotallineweightsvec.add((double)cms.getValue(b.getBookie_id()));
			}
		}
		if(visitoversvec.size() == 0) return;
		if(homeoversvec.size() == 0) return;
		double baseindexvisit = mode(visitoversvec);
		double bestbaseindexvisit = baseindexvisit;
		double baseindexhome = mode(homeoversvec);
		double bestbaseindexhome = baseindexhome;
		if(deadpercentages != 0)
		{
			double adjustment = 100-deadpercentages;

			Vector teamtotallineweightsvecadjusted = new Vector();
			Iterator iter2 = teamtotallineweightsvec.iterator();
			while(iter2.hasNext())
			{
				double weight = (double)iter2.next();
				weight = 100*weight/adjustment;
				teamtotallineweightsvecadjusted.add(weight);
			}
			teamtotallineweightsvec = teamtotallineweightsvecadjusted;
		}

		double convertedwinratesvisit[] = new double[teamtotallineweightsvec.size()];
		double bookwinratesvisit[] = new double[teamtotallineweightsvec.size()];
		double spankywinratevisit = 0;
		double convertedwinrateshome[] = new double[teamtotallineweightsvec.size()];
		double bookwinrateshome[] = new double[teamtotallineweightsvec.size()];
		double spankywinratehome = 0;
		for(int i =0; i < teamtotallineweightsvec.size(); i++)
		{
			//spank left off
			TeamTotalline tl = teamtotallinevec.elementAt(i);
			double[] doublearrvisit = {tl.getCurrentvisitoverjuice(),tl.getCurrentvisitunderjuice()};
			bookwinratesvisit[i] = marketToWinRate(doublearrvisit);
			convertedwinratesvisit[i] = newWinRate(tl.getCurrentvisitover(),bookwinratesvisit[i],baseindexvisit,pusharray);
			double[] doublearrhome = {tl.getCurrenthomeoverjuice(),tl.getCurrenthomeunderjuice()};
			bookwinrateshome[i] = marketToWinRate(doublearrhome);
			convertedwinrateshome[i] = newWinRate(tl.getCurrenthomeover(),bookwinrateshome[i],baseindexhome,pusharray);

		}
		for(int i =0; i < teamtotallineweightsvec.size(); i++)
		{
			spankywinratevisit = spankywinratevisit + convertedwinratesvisit[i]*(double)teamtotallineweightsvec.elementAt(i)/100.00;
			spankywinratehome = spankywinratehome + convertedwinrateshome[i]*(double)teamtotallineweightsvec.elementAt(i)/100.00;
		}

		for(double i= .5; i <= upanddownforalt; i = i+.5)
		{
			althighvisit = newWinRate(baseindexvisit,spankywinratevisit,baseindexvisit+upanddownforalt,pusharray);
			altlowvisit = newWinRate(baseindexvisit,spankywinratevisit,baseindexvisit-upanddownforalt,pusharray);
			if(Math.abs(.5-althighvisit) < Math.abs(.5-spankywinratevisit))
			{
				bestbaseindexvisit = baseindexvisit+upanddownforalt;
				spankywinratevisit = althighvisit;
			}
			if(Math.abs(.5-altlowvisit) < Math.abs(.5-spankywinratevisit))
			{
				bestbaseindexvisit = baseindexvisit-upanddownforalt;
				spankywinratevisit = altlowvisit;
			}
			althighhome = newWinRate(baseindexhome,spankywinratehome,baseindexhome+upanddownforalt,pusharray);
			altlowhome = newWinRate(baseindexhome,spankywinratehome,baseindexhome-upanddownforalt,pusharray);
			if(Math.abs(.5-althighhome) < Math.abs(.5-spankywinratehome))
			{
				bestbaseindexhome = baseindexhome+upanddownforalt;
				spankywinratehome = althighhome;
			}
			if(Math.abs(.5-altlowhome) < Math.abs(.5-spankywinratehome))
			{
				bestbaseindexhome = baseindexhome-upanddownforalt;
				spankywinratehome = altlowhome;
			}

		}




		//double[] spankymoneyline = winRateToMoneyLine(spankywinrate,defaulthold);
		double[] spankymoneylinevisit = winRateToMoneyLine(spankywinratevisit,defaulthold);
		double[] spankymoneylinehome = winRateToMoneyLine(spankywinratehome,defaulthold);
		TeamTotalline consensustl = AppController.getTeamTotalline(consensusbookieid, gameid, period);
		if(consensustl == null)
		{
			AppController.addTeamTotalline(new TeamTotalline(gameid,consensusbookieid,bestbaseindexvisit,spankymoneylinevisit[0],bestbaseindexvisit,spankymoneylinevisit[1],bestbaseindexhome,spankymoneylinehome[0],bestbaseindexhome,spankymoneylinehome[1],0,period));
		}
		else
		{
			consensustl.setCurrentvisitover(bestbaseindexvisit);
			consensustl.setCurrentvisitunder(bestbaseindexvisit);
			consensustl.setCurrentvisitoverjuice(spankymoneylinevisit[0]);
			consensustl.setCurrentvisitunderjuice(spankymoneylinevisit[1]);
			consensustl.setCurrenthomeover(bestbaseindexhome);
			consensustl.setCurrenthomeunder(bestbaseindexhome);
			consensustl.setCurrenthomeoverjuice(spankymoneylinehome[0]);
			consensustl.setCurrenthomeunderjuice(spankymoneylinehome[1]);

		}
		if(gameid >= 901 && gameid <= 999)
		{
			//log(gameid+"..cms="+bestbaseindex+"o"+spankymoneyline[0]+"/u"+spankymoneyline[1]);
		}


		// still need to implement
	}


    public static void calculatebestspread(int gameid, int period)
	{
		List<Bookie> shownbookies = AppController.getShownCols();
		List<Bookie> fixedbookies = AppController.getFixedCols();

		List<Bookie> allcols = new ArrayList<>(shownbookies.size()+fixedbookies.size());
		allcols.addAll(fixedbookies);
        allcols.addAll(shownbookies);
        Bookie bestBookie = AppController.getBookie(bestbookieid);

        Spreadline bvsl = null;
        Spreadline bhsl = null;




		for (Object allcol : allcols) {
			Bookie b = (Bookie) allcol;
			if (b.getBookie_id() >= 990) {
				continue;
			}

///////////////////////////////////spreadlines
			try {
				Spreadline sl = AppController.getSpreadline(b.getBookie_id(), gameid, period);
				if (null != sl && sl.getCurrentvisitjuice()!= 0 && sl.getCurrenthomejuice()!= 0 )
				{
					double visitjuice =  sl.getCurrentvisitjuice();
					double visitspread =  sl.getCurrentvisitspread();
					double homejuice =  sl.getCurrenthomejuice();
					double homespread =  sl.getCurrenthomespread();
					int leagueid = sl.getLeague_id();
					double[] arr = LinesMoves.getleagueidArray(leagueid,period,"SPREAD");
					sl.setBestVisitSpread(false);
					sl.setBestHomeSpread(false);
					//if (bvsl == null && sl.getCurrentvisitjuice() != 0)
					if (bvsl == null)
					{
						bvsl = sl;
					}
					else // here we know bvsl is not null;
					{
						double bestvisitjuice = bvsl.getCurrentvisitjuice();
						double bestvisitspread = bvsl.getCurrentvisitspread();
						if (arr != null) // i have push chart lets use it for smart highlighting
						{


							if (LinesMoves.isLine1BetterThanLine2(visitspread, visitjuice, bestvisitspread, bestvisitjuice, leagueid, period, "SPREAD",gameid,false)) {
								bvsl = sl;
							}

						}
						else { // given i don't have push chart ill just highlight extreme
							if (sl.getCurrentvisitjuice() != 0) {
								if (null == bvsl || sl.getCurrentvisitspread() > bvsl.getCurrentvisitspread()) {
									bvsl = sl;
								} else if (sl.getCurrentvisitspread() == bvsl.getCurrentvisitspread()) {
									if (sl.getCurrentvisitjuice() > bvsl.getCurrentvisitjuice()) {
										bvsl = sl;
									}
								}
							}
						}
					}
					//if (bhsl == null && sl.getCurrenthomejuice() != 0)
					if (bhsl == null)
					{
						bhsl = sl;
					}
					else // here we know bhsl is not null;
					{
						double besthomejuice = bhsl.getCurrenthomejuice();
						double besthomespread = bhsl.getCurrenthomespread();
						if (arr != null) // i have push chart lets use it for smart highlighting
						{
							if (LinesMoves.isLine1BetterThanLine2(homespread, homejuice, besthomespread, besthomejuice, leagueid, period, "SPREAD",gameid,true)) {
								bhsl = sl;
							}

						} else { // given i don't have push chart ill just highlight extreme
							if (sl.getCurrenthomejuice() != 0) {
								if (null == bhsl || sl.getCurrenthomespread() > bhsl.getCurrenthomespread()) {
									bhsl = sl;
								} else if (sl.getCurrenthomespread() == bhsl.getCurrenthomespread()) {
									if (sl.getCurrenthomejuice() > bhsl.getCurrenthomejuice()) {
										bhsl = sl;
									}
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				log(ex);
			}
		}
		if (bvsl != null) {
			bvsl.setBestVisitSpread(true);

			Spreadline bestsl = AppController.getSpreadline(bestbookieid, gameid, period);
			if(bestsl == null)
			{
				AppController.addSpreadline(new Spreadline(gameid,bestbookieid,bvsl.getCurrentvisitspread(),bvsl.getCurrentvisitjuice(),bvsl.getCurrenthomespread(),bvsl.getCurrenthomejuice(),0,period));
			}
			else
			{
				bestsl.setCurrentvisitspread(bvsl.getCurrentvisitspread());
				bestsl.setCurrentvisitjuice(bvsl.getCurrentvisitjuice());
			}
		}
		if (bhsl != null) {
			bhsl.setBestHomeSpread(true);

			Spreadline bestsl = AppController.getSpreadline(bestbookieid, gameid, period);
			if(bestsl == null)
			{
				AppController.addSpreadline(new Spreadline(gameid,bestbookieid,bhsl.getCurrentvisitspread(),bhsl.getCurrentvisitjuice(),bhsl.getCurrenthomespread(),bhsl.getCurrenthomejuice(),0,period));
			}
			else
			{
				bestsl.setCurrenthomespread(bhsl.getCurrenthomespread());
				bestsl.setCurrenthomejuice(bhsl.getCurrenthomejuice());
			}
		}


    }

    public static void calculatebesttotal(int gameid, int period) {

		List<Bookie> shownbookies = AppController.getShownCols();
		List<Bookie> fixedbookies = AppController.getFixedCols();

		List<Bookie> allcols = new ArrayList<>(shownbookies.size()+fixedbookies.size());
		allcols.addAll(fixedbookies);
        allcols.addAll(shownbookies);


        Totalline bo = null;
        Totalline bu = null;

		for (Object allcol : allcols) {
			Bookie b = (Bookie) allcol;
			if (b.getBookie_id() >= 990) {
				continue;
			}
			try {
				Totalline tl = AppController.getTotalline(b.getBookie_id(), gameid, period);
				if (null != tl && tl.getCurrentoverjuice() != 0 && tl.getCurrentunderjuice() != 0)
				{
					double overjuice =  tl.getCurrentoverjuice();
					double over =  tl.getCurrentover();
					double underjuice =  tl.getCurrentunderjuice();
					double under =  tl.getCurrentunder();
					int leagueid = tl.getLeague_id();
					double[] arr = LinesMoves.getleagueidArray(leagueid,period,"TOTAL");
					tl.setBestOver(false);
					tl.setBestUnder(false);

					//if (bo == null && tl.getCurrentoverjuice() != 0)
					if (bo == null)
					{
						bo = tl;
					}
					else
					{

						double bestoverjuice = bo.getCurrentoverjuice();
						double bestover = bo.getCurrentover();
						if (arr != null) // i have push chart lets use it for smart highlighting
						{
							if(leagueid == 5 && period ==0 && gameid == 901)
							{
							//	System.out.println("BEFORE OVER."+over+".."+overjuice+".."+bestover+".."+bestoverjuice);
							}
							if (LinesMoves.isLine1BetterThanLine2(over, overjuice, bestover, bestoverjuice, leagueid, period, "OVER",gameid)) {
								bo = tl;
							}

						} else { // given i don't have push chart ill just highlight extreme
							if (tl.getCurrentoverjuice() != 0) {
								if (null == bo || tl.getCurrentover() < bo.getCurrentover()) {
									bo = tl;
								} else if (tl.getCurrentover() == bo.getCurrentover()) {
									if (tl.getCurrentoverjuice() > bo.getCurrentoverjuice()) {
										bo = tl;
									}
								}
							}
						}
					}

					//if (bu == null && tl.getCurrentunderjuice() != 0)
					if (bu == null)
					{
						bu = tl;
					}
					else {

						double bestunderjuice = bu.getCurrentunderjuice();
						double bestunder = bu.getCurrentunder();
						if (arr != null) // i have push chart lets use it for smart highlighting
						{
							if(leagueid == 5 && period ==0 && gameid == 901)
							{
							//	System.out.println("BEFORE UNDER."+under+".."+underjuice+".."+bestunder+".."+bestunderjuice);
							}
							if (LinesMoves.isLine1BetterThanLine2(under, underjuice, bestunder, bestunderjuice, leagueid, period, "UNDER",gameid)) {
								bu = tl;
							}

						} else { // given i don't have push chart ill just highlight extreme

							if (tl.getCurrentunderjuice() != 0) {
								if (null == bu || tl.getCurrentunder() > bu.getCurrentunder()) {
									bu = tl;
								} else if (tl.getCurrentunder() == bu.getCurrentunder()) {
									if (tl.getCurrentunderjuice() > bu.getCurrentunderjuice()) {
										bu = tl;
									}
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				log(ex);
			}

		}

		if (bo != null) {
			bo.setBestOver(true);

			Totalline besttl = AppController.getTotalline(bestbookieid, gameid, period);
			if(besttl == null)
			{
				AppController.addTotalline(new Totalline(gameid,bestbookieid,bo.getCurrentover(),bo.getCurrentoverjuice(),bo.getCurrentunder(),bo.getCurrentunderjuice(),0,period));
			}
			else
			{
				besttl.setCurrentover(bo.getCurrentover());
				besttl.setCurrentoverjuice(bo.getCurrentoverjuice());
			}
		}
		if (bu != null) {
			bu.setBestUnder(true);

			Totalline besttl = AppController.getTotalline(bestbookieid, gameid, period);
			if(besttl == null)
			{
				AppController.addTotalline(new Totalline(gameid,bestbookieid,bu.getCurrentover(),bu.getCurrentoverjuice(),bu.getCurrentunder(),bu.getCurrentunderjuice(),0,period));
			}
			else
			{
				besttl.setCurrentunder(bu.getCurrentunder());
				besttl.setCurrentunderjuice(bu.getCurrentunderjuice());
			}
		}


    }


    public static void calculatebestteamtotal(int gameid, int period) {
		List<Bookie> shownbookies = AppController.getShownCols();
		List<Bookie> fixedbookies = AppController.getFixedCols();

		List<Bookie> allcols = new ArrayList<>(shownbookies.size()+fixedbookies.size());
		allcols.addAll(fixedbookies);
        allcols.addAll(shownbookies);



        TeamTotalline bvo = null;
        TeamTotalline bvu = null;
        TeamTotalline bho = null;
        TeamTotalline bhu = null;

		for (Object allcol : allcols) {
			Bookie b = (Bookie) allcol;
			if (b.getBookie_id() >= 990) {
				continue;
			}
			try {
				TeamTotalline ttl = AppController.getTeamTotalline(b.getBookie_id(), gameid, period);
				if (null != ttl) {
					ttl.setBestVisitOver(false);
					ttl.setBestVisitUnder(false);
					ttl.setBestHomeOver(false);
					ttl.setBestHomeUnder(false);
					if (bvo == null && ttl.getCurrentvisitoverjuice() != 0) {
						bvo = ttl;
					} else if (ttl.getCurrentvisitoverjuice() != 0) {
						if (null == bvo || ttl.getCurrentvisitover() < bvo.getCurrentvisitover()) {
							bvo = ttl;
						} else if (ttl.getCurrentvisitover() == bvo.getCurrentvisitover()) {
							if (ttl.getCurrentvisitoverjuice() > bvo.getCurrentvisitoverjuice()) {
								bvo = ttl;
							}
						}
					}


					if (bvu == null && ttl.getCurrentvisitunderjuice() != 0) {
						bvu = ttl;
					} else if (ttl.getCurrentvisitunderjuice() != 0) {
						if (null == bvu || ttl.getCurrentvisitunder() > bvu.getCurrentvisitunder()) {
							bvu = ttl;
						} else if (ttl.getCurrentvisitunder() == bvu.getCurrentvisitunder()) {
							if (ttl.getCurrentvisitunderjuice() > bvu.getCurrentvisitunderjuice()) {
								bvu = ttl;
							}
						}
					}

					if (bho == null && ttl.getCurrenthomeoverjuice() != 0) {
						bho = ttl;
					} else if (ttl.getCurrenthomeoverjuice() != 0) {
						if (null == bho || ttl.getCurrenthomeover() < bho.getCurrenthomeover()) {
							bho = ttl;
						} else if (ttl.getCurrenthomeover() == bho.getCurrenthomeover()) {
							if (ttl.getCurrenthomeoverjuice() > bho.getCurrenthomeoverjuice()) {
								bho = ttl;
							}
						}
					}


					if (bhu == null && ttl.getCurrenthomeunderjuice() != 0) {
						bhu = ttl;
					} else if (ttl.getCurrenthomeunderjuice() != 0) {
						if (null == bhu || ttl.getCurrenthomeunder() > bhu.getCurrenthomeunder()) {
							bhu = ttl;
						} else if (ttl.getCurrenthomeunder() == bhu.getCurrenthomeunder()) {
							if (ttl.getCurrenthomeunderjuice() > bhu.getCurrenthomeunderjuice()) {
								bhu = ttl;
							}
						}
					}
				}

			} catch (Exception ex) {
				log(ex);
			}
		}


		if (bvo != null) {
			bvo.setBestVisitOver(true);
			TeamTotalline bestttl = AppController.getTeamTotalline(bestbookieid, gameid, period);
			if(bestttl == null)
			{
				AppController.addTeamTotalline(new TeamTotalline(gameid,bestbookieid,bvo.getCurrentvisitover(),bvo.getCurrentvisitoverjuice(),bvo.getCurrentvisitunder(),bvo.getCurrentvisitunderjuice(),bvo.getCurrenthomeover(),bvo.getCurrenthomeoverjuice(),bvo.getCurrenthomeunder(),bvo.getCurrenthomeunderjuice(),0,period));
			}
			else
			{
				bestttl.setCurrentvisitover(bvo.getCurrentvisitover());
				bestttl.setCurrentvisitoverjuice(bvo.getCurrentvisitoverjuice());
			}

		}
		if (bvu != null) {
			bvu.setBestVisitUnder(true);
			TeamTotalline bestttl = AppController.getTeamTotalline(bestbookieid, gameid, period);
			if(bestttl == null)
			{
				AppController.addTeamTotalline(new TeamTotalline(gameid,bestbookieid,bvu.getCurrentvisitover(),bvo.getCurrentvisitoverjuice(),bvu.getCurrentvisitunder(),bvu.getCurrentvisitunderjuice(),bvu.getCurrenthomeover(),bvu.getCurrenthomeoverjuice(),bvu.getCurrenthomeunder(),bvu.getCurrenthomeunderjuice(),0,period));
			}
			else
			{
				bestttl.setCurrentvisitunder(bvu.getCurrentvisitunder());
				bestttl.setCurrentvisitunderjuice(bvu.getCurrentvisitunderjuice());
			}
		}
		if (bho != null) {
			bho.setBestHomeOver(true);
			TeamTotalline bestttl = AppController.getTeamTotalline(bestbookieid, gameid, period);
			if(bestttl == null)
			{
				AppController.addTeamTotalline(new TeamTotalline(gameid,bestbookieid,bho.getCurrentvisitover(),bho.getCurrentvisitoverjuice(),bho.getCurrentvisitunder(),bho.getCurrentvisitunderjuice(),bho.getCurrenthomeover(),bho.getCurrenthomeoverjuice(),bho.getCurrenthomeunder(),bho.getCurrenthomeunderjuice(),0,period));
			}
			else
			{
				bestttl.setCurrenthomeover(bho.getCurrenthomeover());
				bestttl.setCurrenthomeoverjuice(bho.getCurrenthomeoverjuice());
			}
		}
		if (bhu != null) {
			bhu.setBestHomeUnder(true);
			TeamTotalline bestttl = AppController.getTeamTotalline(bestbookieid, gameid, period);
			if(bestttl == null)
			{
				AppController.addTeamTotalline(new TeamTotalline(gameid,bestbookieid,bhu.getCurrentvisitover(),bhu.getCurrentvisitoverjuice(),bhu.getCurrentvisitunder(),bhu.getCurrentvisitunderjuice(),bhu.getCurrenthomeover(),bhu.getCurrenthomeoverjuice(),bhu.getCurrenthomeunder(),bhu.getCurrenthomeunderjuice(),0,period));
			}
			else
			{
				bestttl.setCurrenthomeunder(bhu.getCurrenthomeunder());
				bestttl.setCurrenthomeunderjuice(bhu.getCurrenthomeunderjuice());
			}
		}

    }

    public static void calculatebestmoney(int gameid, int period) {
		List<Bookie> shownbookies = AppController.getShownCols();
		List<Bookie> fixedbookies = AppController.getFixedCols();

		List<Bookie> allcols = new ArrayList<>(shownbookies.size()+fixedbookies.size());
		allcols.addAll(fixedbookies);
        allcols.addAll(shownbookies);


        Moneyline bvml = null;
        Moneyline bhml = null;
        Moneyline bdml = null;
		for (Object allcol : allcols) {
			Bookie b = (Bookie) allcol;
			if (b.getBookie_id() >= 990) {
				continue;
			}
			try {
				Moneyline ml = AppController.getMoneyline(b.getBookie_id(), gameid, period);
				if (null != ml) {
					ml.setBestVisitMoney(false);
					ml.setBestHomeMoney(false);
					if (bvml == null && ml.getCurrentvisitjuice() != 0) {
						bvml = ml;
					} else if (ml.getCurrentvisitjuice() != 0) {
						if (null == bvml || ml.getCurrentvisitjuice() > bvml.getCurrentvisitjuice()) {
							bvml = ml;
						}
					}


					if (bhml == null && ml.getCurrenthomejuice() != 0) {
						bhml = ml;
					} else if (ml.getCurrenthomejuice() != 0) {
						if (null == bhml || ml.getCurrenthomejuice() > bhml.getCurrenthomejuice()) {
							bhml = ml;
						}
					}

					if (bdml == null && ml.getCurrentdrawjuice() != 0) {
						bdml = ml;
					} else if (ml.getCurrentdrawjuice() != 0) {
						if (null == bdml || ml.getCurrentdrawjuice() > bdml.getCurrentdrawjuice()) {
							bdml = ml;
						}
					}
				}

			} catch (Exception ex) {
				log(ex);
			}
		}
		if (bvml != null) {
			bvml.setBestVisitMoney(true);

			Moneyline bestml = AppController.getMoneyline(bestbookieid, gameid, period);
			if(bestml == null)
			{
				AppController.addMoneyline(new Moneyline(gameid,bestbookieid,bvml.getCurrentvisitjuice(),bvml.getCurrenthomejuice(),bvml.getCurrentdrawjuice(),0,period));
			}
			else
			{
				bestml.setCurrentvisitjuice(bvml.getCurrentvisitjuice());
			}


		}
		if (bhml != null) {
			bhml.setBestHomeMoney(true);

			Moneyline bestml = AppController.getMoneyline(bestbookieid, gameid, period);
			if(bestml == null)
			{
				AppController.addMoneyline(new Moneyline(gameid,bestbookieid,bhml.getCurrentvisitjuice(),bhml.getCurrenthomejuice(),bhml.getCurrentdrawjuice(),0,period));
			}
			else
			{
				bestml.setCurrenthomejuice(bhml.getCurrenthomejuice());
			}
		}
		if (bdml != null) {
			bdml.setBestDrawMoney(true);
			Moneyline bestml = AppController.getMoneyline(bestbookieid, gameid, period);
			if(bestml == null)
			{
				AppController.addMoneyline(new Moneyline(gameid,bestbookieid,bdml.getCurrentvisitjuice(),bdml.getCurrenthomejuice(),bdml.getCurrentdrawjuice(),0,period));
			}
			else
			{
				bestml.setCurrentdrawjuice(bdml.getCurrentdrawjuice());
			}
		}

    }


    public static void calculatebestall(int gameid, int period) {

		List<Bookie> shownbookies = AppController.getShownCols();
		List<Bookie> fixedbookies = AppController.getFixedCols();

		List<Bookie> allcols = new ArrayList<>(shownbookies.size()+fixedbookies.size());
		allcols.addAll(fixedbookies);
        allcols.addAll(shownbookies);


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


		for (Object allcol : allcols) {
			Bookie b = (Bookie) allcol;
			if (b.getBookie_id() >= 990) {
				continue;
			}
///////////////////////////////////spreadlines
			try {
				Spreadline sl = AppController.getSpreadline(b.getBookie_id(), gameid, period);
				if (null != sl && sl.getCurrentvisitjuice() != 0 && sl.getCurrenthomejuice() != 0)
				{
					double visitjuice =  sl.getCurrentvisitjuice();
					double visitspread =  sl.getCurrentvisitspread();
					double homejuice =  sl.getCurrenthomejuice();
					double homespread =  sl.getCurrenthomespread();
					int leagueid = sl.getLeague_id();
					double[] arr = LinesMoves.getleagueidArray(leagueid,period,"SPREAD");
					sl.setBestVisitSpread(false);
					sl.setBestHomeSpread(false);
					//if (bvsl == null && sl.getCurrentvisitjuice() != 0)
					if (bvsl == null)
					{
						bvsl = sl;
					}
					else // here we know bvsl is not null;
					{
						double bestvisitjuice = bvsl.getCurrentvisitjuice();
						double bestvisitspread = bvsl.getCurrentvisitspread();
						if (arr != null) // i have push chart lets use it for smart highlighting
						{
							if (LinesMoves.isLine1BetterThanLine2(visitspread, visitjuice, bestvisitspread, bestvisitjuice, leagueid, period, "SPREAD",gameid,false)) {
								bvsl = sl;
							}

						} else { // given i don't have push chart ill just highlight extreme
							if (sl.getCurrentvisitjuice() != 0) {
								if (null == bvsl || sl.getCurrentvisitspread() > bvsl.getCurrentvisitspread()) {
									bvsl = sl;
								} else if (sl.getCurrentvisitspread() == bvsl.getCurrentvisitspread()) {
									if (sl.getCurrentvisitjuice() > bvsl.getCurrentvisitjuice()) {
										bvsl = sl;
									}
								}
							}
						}
					}
					//if (bhsl == null && sl.getCurrenthomejuice() != 0)
					if (bhsl == null)
					{
						bhsl = sl;
					}
					else // here we know bhsl is not null;
					{
						double besthomejuice = bhsl.getCurrenthomejuice();
						double besthomespread = bhsl.getCurrenthomespread();
						if (arr != null) // i have push chart lets use it for smart highlighting
						{
							if (LinesMoves.isLine1BetterThanLine2(homespread, homejuice, besthomespread, besthomejuice, leagueid, period, "SPREAD",gameid,true)) {
								bhsl = sl;
							}

						} else { // given i don't have push chart ill just highlight extreme
							if (sl.getCurrenthomejuice() != 0) {
								if (null == bhsl || sl.getCurrenthomespread() > bhsl.getCurrenthomespread()) {
									bhsl = sl;
								} else if (sl.getCurrenthomespread() == bhsl.getCurrenthomespread()) {
									if (sl.getCurrenthomejuice() > bhsl.getCurrenthomejuice()) {
										bhsl = sl;
									}
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				log(ex);
			}

	/*
			try {
				Spreadline sl = AppController.getSpreadline(b.getBookie_id(), gameid, period);
				if (null != sl) {
					sl.setBestVisitSpread(false);
					sl.setBestHomeSpread(false);
					if (bvsl == null && sl.getCurrentvisitjuice() != 0) {
						bvsl = sl;
					} else if (sl.getCurrentvisitjuice() != 0) {
						if (null == bvsl || sl.getCurrentvisitspread() > bvsl.getCurrentvisitspread()) {
							bvsl = sl;
						} else if (sl.getCurrentvisitspread() == bvsl.getCurrentvisitspread()) {
							if (sl.getCurrentvisitjuice() > bvsl.getCurrentvisitjuice()) {
								bvsl = sl;
							}
						}
					}


					if (bhsl == null && sl.getCurrenthomejuice() != 0) {
						bhsl = sl;
					} else if (sl.getCurrenthomejuice() != 0) {
						if (null == bhsl || sl.getCurrenthomespread() > bhsl.getCurrenthomespread()) {
							bhsl = sl;
						} else if (sl.getCurrenthomespread() == bhsl.getCurrenthomespread()) {
							if (sl.getCurrenthomejuice() > bhsl.getCurrenthomejuice()) {
								bhsl = sl;
							}
						}
					}
				}
			} catch (Exception ex) {
				log(ex);
			}
*/

///////////////////////////////////moneylines

			try {
				Moneyline ml = AppController.getMoneyline(b.getBookie_id(), gameid, period);
				if (null != ml) {
					ml.setBestVisitMoney(false);
					ml.setBestHomeMoney(false);
					if (bvml == null && ml.getCurrentvisitjuice() != 0) {
						bvml = ml;
					} else if (ml.getCurrentvisitjuice() != 0) {
						if (null == bvml || ml.getCurrentvisitjuice() > bvml.getCurrentvisitjuice()) {
							bvml = ml;
						}
					}


					if (bhml == null && ml.getCurrenthomejuice() != 0) {
						bhml = ml;
					} else if (ml.getCurrenthomejuice() != 0) {
						if (null == bhml || ml.getCurrenthomejuice() > bhml.getCurrenthomejuice()) {
							bhml = ml;
						}
					}

					if (bdml == null && ml.getCurrentdrawjuice() != 0) {
						bdml = ml;
					} else if (ml.getCurrentdrawjuice() != 0) {
						if (null == bdml || ml.getCurrentdrawjuice() > bdml.getCurrentdrawjuice()) {
							bdml = ml;
						}
					}
				}

			} catch (Exception ex) {
				log(ex);
			}
///////////////////////////////////totalline
			try {
				Totalline tl = AppController.getTotalline(b.getBookie_id(), gameid, period);
				if (null != tl && tl.getCurrentoverjuice() != 0 && tl.getCurrentunderjuice() != 0)
				{
					double overjuice =  tl.getCurrentoverjuice();
					double over =  tl.getCurrentover();
					double underjuice =  tl.getCurrentunderjuice();
					double under =  tl.getCurrentunder();
					int leagueid = tl.getLeague_id();
					double[] arr = LinesMoves.getleagueidArray(leagueid,period,"TOTAL");
					tl.setBestOver(false);
					tl.setBestUnder(false);

					//if (bo == null && tl.getCurrentoverjuice() != 0)
					if (bo == null)
					{
						bo = tl;
					}
					else
					{

						double bestoverjuice = bo.getCurrentoverjuice();
						double bestover = bo.getCurrentover();
						if (arr != null) // i have push chart lets use it for smart highlighting
						{

							if (LinesMoves.isLine1BetterThanLine2(over, overjuice, bestover, bestoverjuice, leagueid, period, "OVER",gameid)) {
								bo = tl;
							}

						} else { // given i don't have push chart ill just highlight extreme
							if (tl.getCurrentoverjuice() != 0) {
								if (null == bo || tl.getCurrentover() < bo.getCurrentover()) {
									bo = tl;
								} else if (tl.getCurrentover() == bo.getCurrentover()) {
									if (tl.getCurrentoverjuice() > bo.getCurrentoverjuice()) {
										bo = tl;
									}
								}
							}
						}
					}

					//if (bu == null && tl.getCurrentunderjuice() != 0)
					if (bu == null)
					{
						bu = tl;
					}
					else {

						double bestunderjuice = bu.getCurrentunderjuice();
						double bestunder = bu.getCurrentunder();
						if (arr != null) // i have push chart lets use it for smart highlighting
						{
							if (LinesMoves.isLine1BetterThanLine2(under, underjuice, bestunder, bestunderjuice, leagueid, period, "UNDER",gameid)) {
								bu = tl;
							}

						} else { // given i don't have push chart ill just highlight extreme

							if (tl.getCurrentunderjuice() != 0) {
								if (null == bu || tl.getCurrentunder() > bu.getCurrentunder()) {
									bu = tl;
								} else if (tl.getCurrentunder() == bu.getCurrentunder()) {
									if (tl.getCurrentunderjuice() > bu.getCurrentunderjuice()) {
										bu = tl;
									}
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				log(ex);
			}
/*
			try {
				Totalline tl = AppController.getTotalline(b.getBookie_id(), gameid, period);
				if (null != tl) {
					tl.setBestOver(false);
					tl.setBestUnder(false);
					if (bo == null && tl.getCurrentoverjuice() != 0) {
						bo = tl;
					} else if (tl.getCurrentoverjuice() != 0) {
						if (null == bo || tl.getCurrentover() < bo.getCurrentover()) {
							bo = tl;
						} else if (tl.getCurrentover() == bo.getCurrentover()) {
							if (tl.getCurrentoverjuice() > bo.getCurrentoverjuice()) {
								bo = tl;
							}
						}
					}


					if (bu == null && tl.getCurrentunderjuice() != 0) {
						bu = tl;
					} else if (tl.getCurrentunderjuice() != 0) {
						if (null == bu || tl.getCurrentunder() > bu.getCurrentunder()) {
							bu = tl;
						} else if (tl.getCurrentunder() == bu.getCurrentunder()) {
							if (tl.getCurrentunderjuice() > bu.getCurrentunderjuice()) {
								bu = tl;
							}
						}
					}
				}
			} catch (Exception ex) {
				log(ex);
			}
*/

			//teamtotal line
			try {
				TeamTotalline ttl = AppController.getTeamTotalline(b.getBookie_id(), gameid, period);
				if (null != ttl) {
					ttl.setBestVisitOver(false);
					ttl.setBestVisitUnder(false);
					ttl.setBestHomeOver(false);
					ttl.setBestHomeUnder(false);
					if (bvo == null && ttl.getCurrentvisitoverjuice() != 0) {
						bvo = ttl;
					} else if (ttl.getCurrentvisitoverjuice() != 0) {
						if (null == bvo || ttl.getCurrentvisitover() < bvo.getCurrentvisitover()) {
							bvo = ttl;
						} else if (ttl.getCurrentvisitover() == bvo.getCurrentvisitover()) {
							if (ttl.getCurrentvisitoverjuice() > bvo.getCurrentvisitoverjuice()) {
								bvo = ttl;
							}
						}
					}


					if (bvu == null && ttl.getCurrentvisitunderjuice() != 0) {
						bvu = ttl;
					} else if (ttl.getCurrentvisitunderjuice() != 0) {
						if (null == bvu || ttl.getCurrentvisitunder() > bvu.getCurrentvisitunder()) {
							bvu = ttl;
						} else if (ttl.getCurrentvisitunder() == bvu.getCurrentvisitunder()) {
							if (ttl.getCurrentvisitunderjuice() > bvu.getCurrentvisitunderjuice()) {
								bvu = ttl;
							}
						}
					}

					if (bho == null && ttl.getCurrenthomeoverjuice() != 0) {
						bho = ttl;
					} else if (ttl.getCurrenthomeoverjuice() != 0) {
						if (null == bho || ttl.getCurrenthomeover() < bho.getCurrenthomeover()) {
							bho = ttl;
						} else if (ttl.getCurrenthomeover() == bho.getCurrenthomeover()) {
							if (ttl.getCurrenthomeoverjuice() > bho.getCurrenthomeoverjuice()) {
								bho = ttl;
							}
						}
					}


					if (bhu == null && ttl.getCurrenthomeunderjuice() != 0) {
						bhu = ttl;
					} else if (ttl.getCurrenthomeunderjuice() != 0) {
						if (null == bhu || ttl.getCurrenthomeunder() > bhu.getCurrenthomeunder()) {
							bhu = ttl;
						} else if (ttl.getCurrenthomeunder() == bhu.getCurrenthomeunder()) {
							if (ttl.getCurrenthomeunderjuice() > bhu.getCurrenthomeunderjuice()) {
								bhu = ttl;
							}
						}
					}
				}

			} catch (Exception ex) {
				log(ex);
			}


		}
        //log("bestline "+gameid+".."+period+".."+bvsl);
		if (bvsl != null) {
			bvsl.setBestVisitSpread(true);
			Spreadline bestsl = AppController.getSpreadline(bestbookieid, gameid, period);
			if(bestsl == null)
			{
				AppController.addSpreadline(new Spreadline(gameid,bestbookieid,bvsl.getCurrentvisitspread(),bvsl.getCurrentvisitjuice(),bvsl.getCurrenthomespread(),bvsl.getCurrenthomejuice(),0,period));
			}
			else
			{
				bestsl.setCurrentvisitspread(bvsl.getCurrentvisitspread());
				bestsl.setCurrentvisitjuice(bvsl.getCurrentvisitjuice());
			}
		}
		if (bhsl != null) {
			bhsl.setBestHomeSpread(true);
			Spreadline bestsl = AppController.getSpreadline(bestbookieid, gameid, period);
			if(bestsl == null)
			{
				AppController.addSpreadline(new Spreadline(gameid,bestbookieid,bhsl.getCurrentvisitspread(),bhsl.getCurrentvisitjuice(),bhsl.getCurrenthomespread(),bhsl.getCurrenthomejuice(),0,period));
			}
			else
			{
				bestsl.setCurrenthomespread(bhsl.getCurrenthomespread());
				bestsl.setCurrenthomejuice(bhsl.getCurrenthomejuice());
			}
		}
		if (bo != null) {
			bo.setBestOver(true);
			Totalline besttl = AppController.getTotalline(bestbookieid, gameid, period);
			if(besttl == null)
			{
				AppController.addTotalline(new Totalline(gameid,bestbookieid,bo.getCurrentover(),bo.getCurrentoverjuice(),bo.getCurrentunder(),bo.getCurrentunderjuice(),0,period));
			}
			else
			{
				besttl.setCurrentover(bo.getCurrentover());
				besttl.setCurrentoverjuice(bo.getCurrentoverjuice());
			}
		}
		if (bu != null) {
			bu.setBestUnder(true);
			Totalline besttl = AppController.getTotalline(bestbookieid, gameid, period);
			if(besttl == null)
			{
				AppController.addTotalline(new Totalline(gameid,bestbookieid,bu.getCurrentover(),bu.getCurrentoverjuice(),bu.getCurrentunder(),bu.getCurrentunderjuice(),0,period));
			}
			else
			{
				besttl.setCurrentunder(bu.getCurrentunder());
				besttl.setCurrentunderjuice(bu.getCurrentunderjuice());
			}
		}
		if (bvml != null) {
			bvml.setBestVisitMoney(true);
			Moneyline bestml = AppController.getMoneyline(bestbookieid, gameid, period);
			if(bestml == null)
			{
				AppController.addMoneyline(new Moneyline(gameid,bestbookieid,bvml.getCurrentvisitjuice(),bvml.getCurrenthomejuice(),bvml.getCurrentdrawjuice(),0,period));
			}
			else
			{
				bestml.setCurrentvisitjuice(bvml.getCurrentvisitjuice());
			}


		}
		if (bhml != null) {
			bhml.setBestHomeMoney(true);
			Moneyline bestml = AppController.getMoneyline(bestbookieid, gameid, period);
			if(bestml == null)
			{
				AppController.addMoneyline(new Moneyline(gameid,bestbookieid,bhml.getCurrentvisitjuice(),bhml.getCurrenthomejuice(),bhml.getCurrentdrawjuice(),0,period));
			}
			else
			{
				bestml.setCurrenthomejuice(bhml.getCurrenthomejuice());
			}
		}
		if (bdml != null) {
			bdml.setBestDrawMoney(true);
			Moneyline bestml = AppController.getMoneyline(bestbookieid, gameid, period);
			if(bestml == null)
			{
				AppController.addMoneyline(new Moneyline(gameid,bestbookieid,bdml.getCurrentvisitjuice(),bdml.getCurrenthomejuice(),bdml.getCurrentdrawjuice(),0,period));
			}
			else
			{
				bestml.setCurrentdrawjuice(bdml.getCurrentdrawjuice());
			}
		}

		if (bvo != null) {
			bvo.setBestVisitOver(true);
			TeamTotalline bestttl = AppController.getTeamTotalline(bestbookieid, gameid, period);
			if(bestttl == null)
			{
				AppController.addTeamTotalline(new TeamTotalline(gameid,bestbookieid,bvo.getCurrentvisitover(),bvo.getCurrentvisitoverjuice(),bvo.getCurrentvisitunder(),bvo.getCurrentvisitunderjuice(),bvo.getCurrenthomeover(),bvo.getCurrenthomeoverjuice(),bvo.getCurrenthomeunder(),bvo.getCurrenthomeunderjuice(),0,period));
			}
			else
			{
				bestttl.setCurrentvisitover(bvo.getCurrentvisitover());
				bestttl.setCurrentvisitoverjuice(bvo.getCurrentvisitoverjuice());
			}

		}
		if (bvu != null) {
			bvu.setBestVisitUnder(true);
			TeamTotalline bestttl = AppController.getTeamTotalline(bestbookieid, gameid, period);
			if(bestttl == null)
			{
				AppController.addTeamTotalline(new TeamTotalline(gameid,bestbookieid,bvu.getCurrentvisitover(),bvo.getCurrentvisitoverjuice(),bvu.getCurrentvisitunder(),bvu.getCurrentvisitunderjuice(),bvu.getCurrenthomeover(),bvu.getCurrenthomeoverjuice(),bvu.getCurrenthomeunder(),bvu.getCurrenthomeunderjuice(),0,period));
			}
			else
			{
				bestttl.setCurrentvisitunder(bvu.getCurrentvisitunder());
				bestttl.setCurrentvisitunderjuice(bvu.getCurrentvisitunderjuice());
			}
		}
		if (bho != null) {
			bho.setBestHomeOver(true);
			TeamTotalline bestttl = AppController.getTeamTotalline(bestbookieid, gameid, period);
			if(bestttl == null)
			{
				AppController.addTeamTotalline(new TeamTotalline(gameid,bestbookieid,bho.getCurrentvisitover(),bho.getCurrentvisitoverjuice(),bho.getCurrentvisitunder(),bho.getCurrentvisitunderjuice(),bho.getCurrenthomeover(),bho.getCurrenthomeoverjuice(),bho.getCurrenthomeunder(),bho.getCurrenthomeunderjuice(),0,period));
			}
			else
			{
				bestttl.setCurrenthomeover(bho.getCurrenthomeover());
				bestttl.setCurrenthomeoverjuice(bho.getCurrenthomeoverjuice());
			}
		}
		if (bhu != null) {
			bhu.setBestHomeUnder(true);
			TeamTotalline bestttl = AppController.getTeamTotalline(bestbookieid, gameid, period);
			if(bestttl == null)
			{
				AppController.addTeamTotalline(new TeamTotalline(gameid,bestbookieid,bhu.getCurrentvisitover(),bhu.getCurrentvisitoverjuice(),bhu.getCurrentvisitunder(),bhu.getCurrentvisitunderjuice(),bhu.getCurrenthomeover(),bhu.getCurrenthomeoverjuice(),bhu.getCurrenthomeunder(),bhu.getCurrenthomeunderjuice(),0,period));
			}
			else
			{
				bestttl.setCurrenthomeunder(bhu.getCurrenthomeunder());
				bestttl.setCurrenthomeunderjuice(bhu.getCurrenthomeunderjuice());
			}
		}




//        if (bhsl != null) {
//            //log("gameid="+gameid+"..bhsl="+bhsl.getBookieid()+"..bhsl="+bhsl.getCurrenthomespread()+bhsl.getCurrenthomejuice());
//        }


		calculateconsensusspread(gameid,period);
		calculateconsensustotal(gameid,period);
		calculateconsensusmoney(gameid,period);
		calculateconsensusteamtotal(gameid,period);

    }

	public static double winRateToMoneyLine(double w)
	{
		if( w>= .5)
		{
			return Math.round(-100*w/(1-w));
		}
		else
		{
			return Math.round(100*(1-w)/w);
		}
	}

	public static double[] winRateToMoneyLine(double winp, double hold)
	{
		double[] retvalue = new double[2];
		double w1 = winp+hold/2.0;
		double w2 = (1-winp)+hold/2.0;
		retvalue[0] = winRateToMoneyLine(w1);
		retvalue[1] = winRateToMoneyLine(w2);

		return retvalue;
	}

	public static double moneyLineToWinRate(double ml)
	{
		if(ml >= 100)
		{
			return 100/(100+ml);
		}
		else if(ml <= -100)
		{
			return ml/(ml-100);
		}
		else
		{
			return 0;
		}
	}

	public static double marketToWinRate(double[] mls)
	{
		double w1 = moneyLineToWinRate(mls[0]);
		double w2 = moneyLineToWinRate(mls[1]);
		return w1/(w1+w2);
	}



	public static double newWinRate(double oldindex,double oldwinrate,double newindex,double[] pusharray)
	{

		int startpoint = (int)Math.round(oldindex*2);
		int lastpoint = (int)Math.round(newindex*2);
		int pusindex;
		int pushloop;
		int pushtarget=0;
		int stepdir;
		double gains = 0;


		if(startpoint < lastpoint)
		{
			stepdir = 1;
		}
		else if(startpoint > lastpoint)
		{
			stepdir = -1;
		}
		else // they're the same
		{
			return oldwinrate;
		}

		pushloop = startpoint;
		while(pushloop != lastpoint)
		{
			pushloop = pushloop +stepdir;
			if(pushloop/2 == ((double)pushloop)/2)
			{
				if(stepdir  == 1)
				{
					pushtarget = Math.abs(pushloop/2);
				}
				else if(stepdir == -1)
				{
					pushtarget = Math.abs(pushloop)/2;
				}
				gains = gains +pusharray[pushtarget]/2/(1-pusharray[pushtarget]);
			}
			else
			{
				pushtarget = Math.abs(pushloop)/2;
				gains = gains +pusharray[pushtarget]/2;
			}
		}
		if(newindex > oldindex)
		{
			return oldwinrate+gains;
		}
		else
		{
			return oldwinrate - gains;
		}
	}

	public static double newWinRate(double oldindex,Moneyline line,double newindex,double[] pusharray)
	{
		double[] doublearr = {line.getCurrentvisitjuice(),line.getCurrenthomejuice()};
		return newWinRate(oldindex, marketToWinRate(doublearr),newindex,pusharray);

	}
	public static double mode(Vector items) {

		double[] a = new double[items.size()];
		for (int i = 0; i < items.size(); ++i)
		{
			a[i] = (double)items.elementAt(i);
		}
		double maxValue = a[0];
		int maxCount = 0;

		for (int i = 0; i < a.length; ++i) {
			int count = 0;
			for (int j = 0; j < a.length; ++j) {
				if (a[j] == a[i]) ++count;
			}
			if (count > maxCount) {
				maxCount = count;
				maxValue = a[i];
			}
		}

		return maxValue;
	}


}