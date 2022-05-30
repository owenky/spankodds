package com.sia.client.model;

import com.sia.client.ui.AppController;
import com.sia.client.ui.TeamTotalline;
import com.sia.client.ui.Totalline;

import java.util.ArrayList;
import java.util.List;

import static com.sia.client.config.Utils.log;


public class BestLines {

	static int bestbookieid = 996;
    public static void calculatebestspread(int gameid, int period) {
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


							if (LinesMoves.isLine1BetterThanLine2(visitspread, visitjuice, bestvisitspread, bestvisitjuice, leagueid, period, "SPREAD",gameid)) {
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
							if (LinesMoves.isLine1BetterThanLine2(homespread, homejuice, besthomespread, besthomejuice, leagueid, period, "SPREAD",gameid)) {
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
							if (LinesMoves.isLine1BetterThanLine2(visitspread, visitjuice, bestvisitspread, bestvisitjuice, leagueid, period, "SPREAD",gameid)) {
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
							if (LinesMoves.isLine1BetterThanLine2(homespread, homejuice, besthomespread, besthomejuice, leagueid, period, "SPREAD",gameid)) {
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

    }


}