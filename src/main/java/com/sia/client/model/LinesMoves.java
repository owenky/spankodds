package com.sia.client.model;

public class LinesMoves
{
    static boolean initialized = false;
    static double[] cfsides = new double[100];
    static double[] cftotals = new double[100];
    static double[] cfh1sides = new double[100];
    static double[] cfh1totals = new double[100];

    static double[] nflsides = new double[100];
    static double[] nfltotals = new double[100];
    static double[] nflh1sides = new double[100];
    static double[] nflh1totals = new double[100];

    static double[] nbasides = new double[100];
    static double[] nbatotals = new double[300];
    static double[] nbah1sides = new double[100];
    static double[] nbah1totals = new double[200];

    static double[] cbsides = new double[100];
    static double[] cbtotals = new double[200];
    static double[] cbh1sides = new double[100];
    static double[] cbh1totals = new double[200];

    static double[] mlbsides = new double[100];
    static double[] mlbtotals = new double[100];
    static double[] mlbh1sides = new double[100];
    static double[] mlbh1totals = new double[100];

    public static void initi()
    {



        mlbsides[1] =  .13;

        mlbh1sides[0] = .15;

        mlbtotals[7] = .124;
        mlbtotals[8] = .072;
        mlbtotals[9] = .103;
        mlbtotals[10] = .065;
        mlbtotals[11] = .089;

        mlbh1totals[4] = .14;
        mlbh1totals[5] = .12;






        cfsides[0] = 0.000000001;
        cfsides[1] = 0.011621378845722;
        cfsides[2] = 0.0144623092737655;
        cfsides[3] = 0.0624749798658274;
        cfsides[4] = 0.0279822966353472;
        cfsides[5] = 0.02023034;
        cfsides[6] = 0.0257999435607947;
        cfsides[7] = 0.0560011908768176;
        cfsides[8] = 0.0253264257546907;
        cfsides[9] = 0.008089529;
        cfsides[10] = 0.03212498542126;
        cfsides[11] = 0.01838371;
        cfsides[12] = 0.01799197;
        cfsides[13] = 0.01298664;
        cfsides[14] = 0.0350862863612674;
        cfsides[15] = 0.0158987587130829;
        cfsides[16] = 0.008840203;
        cfsides[17] = 0.0470155900733601;
        cfsides[18] = 0.02830226;
        cfsides[19] = 0.0200926;
        cfsides[20] = 0.02895494;
        cfsides[21] = 0.03213106;
        cfsides[22] = 0.0209777854652919;
        cfsides[23] = 0.01812545;
        cfsides[24] = 0.02781408;
        cfsides[25] = 0.03767449;
        cfsides[26] = 0.01455748;
        cfsides[27] = 0.01901732;
        cfsides[28] = 0.03343775;
        cfsides[29] = 0.01228099;
        cfsides[30] = 0.01695779;
        cfsides[31] = 0.03105969;
        cfsides[32] = 0.03078489;
        cfsides[33] = 0.007203111;
        cfsides[34] = 0.02923645;
        cfsides[35] = 0.02538885;
        cfsides[36] = 0.02989876;

        cftotals[37] = 0.04786586;
        cftotals[38] = 0.02114695;
        cftotals[39] = 0.0090667;
        cftotals[40] = 0.02490099;
        cftotals[41] = 0.04507582;
        cftotals[42] = 0.02167235;
        cftotals[43] = 0.02052872;
        cftotals[44] = 0.03356997;
        cftotals[45] = 0.0421018;
        cftotals[46] = 0.01310982;
        cftotals[47] = 0.02511861;
        cftotals[48] = 0.03873815;
        cftotals[49] = 0.02433593;
        cftotals[50] = 0.0163593;
        cftotals[51] = 0.02753631;
        cftotals[52] = 0.02766985;
        cftotals[53] = 0.01672774;
        cftotals[54] = 0.0191587;
        cftotals[55] = 0.03454205;
        cftotals[56] = 0.015254;
        cftotals[57] = 0.01737252;
        cftotals[58] = 0.03208366;
        cftotals[59] = 0.0369341;
        cftotals[60] = 0.009752569;
        cftotals[61] = 0.02018568;
        cftotals[62] = 0.02664655;
        cftotals[63] = 0.02026209;
        cftotals[64] = 0.00805621;
        cftotals[65] = 0.03422643;
        cftotals[66] = 0.03122996;
        cftotals[67] = 0.01417745;
        cftotals[68] = 0.02063627;
        cftotals[69] = 0.03158314;
        cftotals[70] = 0.011;
        cftotals[71] = 0.02075;
        cftotals[72] = 0.01847676;
        cftotals[73] = 0.03048235;
        cftotals[74] = 0.00914;
        cftotals[75] = 0.01425982;
        cftotals[76] = 0.01;
        cftotals[77] = 0.01;
        cftotals[78] = 0.01;
        cftotals[79] = 0.01;
        cftotals[80] = 0.01;
        cftotals[81] = 0.01;
        cftotals[82] = 0.01;
        cftotals[83] = 0.01;
        cftotals[84] = 0.01;

        cfh1sides[0] = 0.08992806;
        cfh1sides[1] = 0.0199024;
        cfh1sides[2] = 0.008901466;
        cfh1sides[3] = 0.05330992;
        cfh1sides[4] = 0.03861156;
        cfh1sides[5] = 0.008912127;
        cfh1sides[6] = 0.02866867;
        cfh1sides[7] = 0.08048021;
        cfh1sides[8] = 0.01718189;
        cfh1sides[9] = 0.01194627;
        cfh1sides[10] = 0.06501116;
        cfh1sides[11] = 0.02964565;
        cfh1sides[12] = 0.006277447;
        cfh1sides[13] = 0.02644793;
        cfh1sides[14] = 0.06031458;
        cfh1sides[15] = 0.01224976;
        cfh1sides[16] = 0.01172183;
        cfh1sides[17] = 0.01764947;
        cfh1sides[18] = 0.03403852;

        cfh1totals[19] = 0.01378503;
        cfh1totals[20] = 0.06126799;
        cfh1totals[21] = 0.04729656;
        cfh1totals[22] = 0.008774244;
        cfh1totals[23] = 0.03832212;
        cfh1totals[24] = 0.07617937;
        cfh1totals[25] = 0.003195849;
        cfh1totals[26] = 0.02056042;
        cfh1totals[27] = 0.05446596;
        cfh1totals[28] = 0.05028389;
        cfh1totals[29] = 0.01084761;
        cfh1totals[30] = 0.03400891;
        cfh1totals[31] = 0.06669513;
        cfh1totals[32] = 0.004581115;
        cfh1totals[33] = 0.01279715;
        cfh1totals[34] = 0.05896401;


        nflsides[0] = 0.00001;
        nflsides[1] = 0.02124964;
        nflsides[2] = 0.0238847;
        nflsides[3] = 0.08849608;
        nflsides[4] = 0.03975331; // recently changed
        nflsides[5] = 0.02106881;
        nflsides[6] = 0.02991309;
        nflsides[7] = 0.0570887;
        nflsides[8] = 0.01926965;
        nflsides[9] = 0.007745019;
        nflsides[10] = 0.05683194;
        nflsides[11] = 0.01746612;
        nflsides[12] = 0.01303521;
        nflsides[13] = 0.01542709;
        nflsides[14] = 0.04934072;
        nflsides[15] = 0.00994;
        nflsides[16] = 0.0168;
        nflsides[17] = 0.0533;
        nflsides[18] = 0.03287787;
        nflsides[19] = 0.009969;
        nflsides[20] = 0.01014823;
        nflsides[21] = 0.05621165;
        nflsides[22] = 0.0068;

        nfltotals[30] = 0.0432;
        nfltotals[31] = 0.02438;
        nfltotals[32] = 0.01917;
        nfltotals[33] = 0.0267;
        nfltotals[34] = 0.0298;
        nfltotals[35] = 0.01743;
        nfltotals[36] = 0.01712094;
        nfltotals[37] = 0.0523003;
        nfltotals[38] = 0.02279654;
        nfltotals[39] = 0.01808474;
        nfltotals[40] = 0.03343519;
        nfltotals[41] = 0.04561202;
        nfltotals[42] = 0.01660581;
        nfltotals[43] = 0.03210515;
        nfltotals[44] = 0.03640421;
        nfltotals[45] = 0.03022984;
        nfltotals[46] = 0.02212059;
        nfltotals[47] = 0.03758024;
        nfltotals[48] = 0.03096898;
        nfltotals[49] = 0.02009631;
        nfltotals[50] = 0.03486414;
        nfltotals[51] = 0.03608;
        nfltotals[52] = 0.01818;
        nfltotals[53] = 0.01424;
        nfltotals[54] = 0.01855;
        nfltotals[55] = 0.04026;
        nfltotals[56] = 0.00676;
        nfltotals[57] = 0.02026;
        nfltotals[58] = 0.0211;
        nfltotals[59] = 0.06246;
        nfltotals[60] = 0.01188;

        nflh1sides[0] = 0.08909772;
        nflh1sides[1] = 0.02318335;
        nflh1sides[2] = 0.01147084;
        nflh1sides[3] = 0.05452536;
        nflh1sides[4] = 0.05314563;
        nflh1sides[5] = 0.003540544;
        nflh1sides[6] = 0.02247088;
        nflh1sides[7] = 0.07796355;
        nflh1sides[8] = 0.02091726;
        nflh1sides[9] = 0.008217738;
        nflh1sides[10] = 0.06586434;
        nflh1sides[11] = 0.03997552;

        nflh1totals[15] = 0.005491573;
        nflh1totals[16] = 0.05721391;
        nflh1totals[17] = 0.08151678;
        nflh1totals[18] = 0.0009953587;
        nflh1totals[19] = 0.01813249;
        nflh1totals[20] = 0.08999876;
        nflh1totals[21] = 0.03812249;
        nflh1totals[22] = 0.006731327;
        nflh1totals[23] = 0.05717292;
        nflh1totals[24] = 0.09566068;
        nflh1totals[25] = 0.001389917;
        nflh1totals[26] = 0.02264101;
        nflh1totals[27] = 0.0805133;
        nflh1totals[28] = 0.03871448;
        nflh1totals[29] = 0.002212525;
        nflh1totals[30] = 0.0383532;



        cbsides[0] = 0.000000000001;
        nbasides[0] = 0.000000000001;
        cbsides[1] = .026178;
        nbasides[1] = .0237;

        for(int cnt = 2; cnt<= 60;cnt++)
        {
            cbsides[cnt] = .0415;

        }
        for(int cnt = 0; cnt<= 60;cnt++)
        {
            cbh1sides[cnt] = .0415;

        }
        for(int cnt = 2; cnt<= 60;cnt++)
        {
            nbasides[cnt] = .04;

        }
        for(int cnt = 0; cnt<= 60;cnt++)
        {
            nbah1sides[cnt] = .04;

        }
        for(int cnt = 150; cnt<= 270;cnt++)
        {
            nbatotals[cnt] = .0175; // was .02475
        }
        for(int cnt = 110; cnt<= 190;cnt++)
        {
            cbtotals[cnt] = .025;
        }
        for(int cnt = 85; cnt<= 140;cnt++)
        {
            nbah1totals[cnt] = .03; // was .0385
        }
        for(int cnt = 40; cnt<= 100;cnt++)
        {
            cbh1totals[cnt] = .039;
        }


    }

    public static double[] getleagueidArray(int leagueid,int period,String type)
    {
        if(!initialized)
        {
            initi();
        }

        double[] arr = null;
        if(leagueid == 2)
        {
            if(type.equals("SPREAD"))
            {
                if(period == 0)
                {
                    arr = cfsides;
                }
                else if(period == 1 || period == 2)
                {
                    arr = cfh1sides;
                }
                else
                {
                    arr = null;
                }
            }
            else if(type.equals("TOTAL") || type.equals("OVER") || type.equals("UNDER"))
            {
                if(period == 0)
                {
                    arr = cftotals;
                }
                else if(period == 1 || period == 2)
                {
                    arr = cfh1totals;
                }
                else
                {
                    arr = null;
                }
            }
        }
        else if(leagueid == 1)
        {
            if(type.equals("SPREAD"))
            {
                if(period == 0)
                {
                    arr = nflsides;
                }
                else if(period == 1 || period == 2)
                {
                    arr = nflh1sides;
                }
                else
                {
                    arr = null;
                }
            }
            else if(type.equals("TOTAL") || type.equals("OVER") || type.equals("UNDER"))
            {
                if(period == 0)
                {
                    arr = nfltotals;
                }
                else if(period == 1 || period == 2)
                {
                    arr = nflh1totals;
                }
                else
                {
                    arr = null;
                }
            }
        }

        else if(leagueid == 3)
        {
            if(type.equals("SPREAD"))
            {
                if(period == 0)
                {
                    arr = nbasides;
                }
                else if(period == 1 || period == 2)
                {
                    arr = nbah1sides;
                }
                else
                {
                    arr = null;
                }
            }
            else if(type.equals("TOTAL") || type.equals("OVER") || type.equals("UNDER"))
            {
                if(period == 0)
                {
                    arr = nbatotals;
                }
                else if(period == 1 || period == 2)
                {
                    arr = nbah1totals;
                }
                else
                {
                    arr = null;
                }
            }
        }

        else if(leagueid == 4  )
        {
            if(type.equals("SPREAD"))
            {
                if(period == 0)
                {
                    arr = cbsides;
                }
                else if(period == 1 || period == 2)
                {
                    arr = cbh1sides;
                }
                else
                {
                    arr = null;
                }
            }
            else if(type.equals("TOTAL") || type.equals("OVER") || type.equals("UNDER"))
            {
                if(period == 0)
                {
                    arr = cbtotals;
                }
                else if(period == 1 || period == 2)
                {
                    arr = cbh1totals;
                }
                else
                {
                    arr = null;
                }
            }
        }

        else if(leagueid == 5)
        {
            if(type.equals("SPREAD"))
            {
                if(period == 0)
                {
                    arr = mlbsides;
                }
                else if(period == 1)
                {
                    arr = mlbh1sides;
                }
                else
                {
                    arr = null;
                }
            }
            else if(type.equals("TOTAL") || type.equals("OVER") || type.equals("UNDER"))
            {
                if(period == 0)
                {
                    arr = mlbtotals;
                }
                else if(period == 1)
                {
                    arr = mlbh1totals;
                }
                else
                {
                    arr = null;
                }
            }
        }
        return arr;
    }

    public static double percentageofjuicemove(double oldjuice,double newjuice)
    {
        // newjuice is closing line
        //System.out.println("JUICE INPUTS..oj="+oldjuice+"..nj="+newjuice);
        double percentmove = 0;
        if(newjuice < 0 && oldjuice < 0)
        {
            double newj = (newjuice/(newjuice-100));
            double oldj = oldjuice/(oldjuice-100);
            //System.out.println("newj="+newj);
            //System.out.println("oldj="+oldj);
            percentmove = (newj-oldj)/oldj;
            //percentmove = ( newjuice/(newjuice-100) - oldjuice/(oldjuice-100))/((oldjuice/(oldjuice-100)));

        }
        else if(newjuice > 0 && oldjuice > 0)
        {
            double newj = 1 - (newjuice/(newjuice+100));
            double oldj = 1- (oldjuice/(oldjuice+100));
            //System.out.println("newj="+newj);
            //System.out.println("oldj="+oldj);
            percentmove = (newj-oldj)/oldj;
            //percentmove = ( ((1 - newjuice/(newjuice+100)) - (1 - oldjuice/(oldjuice+100)))/(1 - (oldjuice/(oldjuice+100))));

        }
        else if(newjuice > 0 && oldjuice < 0)
        {
            double newj = 1 - (newjuice/(newjuice+100));
            double oldj = oldjuice/(oldjuice-100);
            //System.out.println("newj="+newj);
            //System.out.println("oldj="+oldj);
            percentmove = (newj-oldj)/oldj;

            //percentmove = ( ((1 - newjuice/(newjuice+100)) - (oldjuice/(oldjuice-100)))/((oldjuice/(oldjuice-100))));

        }
        else if(newjuice < 0 && oldjuice > 0)
        {
            double newj = (newjuice/(newjuice-100));
            double oldj = 1- (oldjuice/(oldjuice+100));
            //System.out.println("newj="+newj);
            //System.out.println("oldj="+oldj);
            percentmove = (newj-oldj)/oldj;
            //percentmove = ( ((newjuice/(newjuice-100)) - (1 - oldjuice/(oldjuice+100)))/(1 - (oldjuice/(oldjuice+100))));

        }
        return percentmove;
    }
    public static double percentOfMove(double oldline,double oldjuice,double newline,double newjuice,int leagueid,int period,String type)
    {

        //System.out.println("oldline="+oldline+".."+oldjuice+"..newline="+newline+".."+newjuice);
        double[] arr = new double[100];

        arr = getleagueidArray(leagueid,period,type);



        double juicepercent = 0;
        double negativefactor = 1;

        // if( ( oldline+newline == 0 && type.equals("SPREAD")) || ( oldline-newline == 0 && type.equals("TOTAL"))) // difference == 0
        if( type.equals("MONEYLINE") || ( oldline== newline && type.equals("SPREAD")) || ( oldline-newline == 0 &&   (  type.equals("TOTAL") || type.equals("OVER") || type.equals("UNDER")          )  )) // difference == 0
        {
            juicepercent = percentageofjuicemove(oldjuice,newjuice);
            //System.out.println("juicepercent="+juicepercent);
            return juicepercent;
        }

        // need to handle case where i don't have push chart data
        if(arr == null)
        {

            return 0;
        }

        if(oldline <0 && newline <0)
        {
            //	oldline = oldline*-1.0;
            //	newline = newline*-1.0;
        }
        else if(oldline <0 && newline >0)
        {

        }

        else if(oldline >0 && newline <0)
        {

        }
				/*
			if(oldline > newline)
				{
					negativefactor = -1;

					double temp = oldline;
					oldline = newline;
					newline = temp;
					double tempjuice = oldjuice;
					oldjuice = newjuice;
					newjuice = tempjuice;


				}
				*/
        double prob = .5;
        //System.out.println("oldline="+oldline+".."+oldjuice+"..newline="+newline+".."+newjuice);
        juicepercent = percentageofjuicemove(oldjuice,newjuice);


        // used to handle extreme cases where we have no data
        try
        {
            if(oldline % 1 == 0 && arr[(int)oldline] == 0)
            {
                arr[(int)oldline] = 0.0001;
            }
            if(oldline % 1 == 0 && arr[(int)newline] == 0)
            {
                arr[(int)newline] = 0.0001;
            }
        }
        catch(Exception ex) {}

        if(type.equals("OVER"))
        {
            if(oldline > newline)
            {

                negativefactor = -1;
            }
        }
        else
        {
            if(oldline < newline)
            {

                negativefactor = -1;
            }
        }
        double st = oldline;
        double en = newline;

        if(st > en)
        {
            double temp = st;
            st = en;
            en = temp;
        }

        for (double startline = st; startline < en; startline=startline+.5)
        {
            // add half of push probability every time we cross whole number
            int index = (int)Math.round(startline);
            int positiveindex = index;
            if(index < 0)
            {
                positiveindex = index*-1;
            }
            prob = prob+arr[positiveindex]/2.0*negativefactor;
            //System.out.println(index+".."+arr[positiveindex]/2.0*negativefactor);

        }

        //prob = prob *negativefactor;
        double linepercent = (prob-.5)/.5; // use 50% and no vig
        double totalpercent = juicepercent+linepercent;
       // System.out.println("juicepercent="+juicepercent+"\tlinepercent="+linepercent);
       // System.out.println("totalpercent="+totalpercent);
        return totalpercent;

    }

    public static boolean isLine1BetterThanLine2(double oldline,double oldjuice,double newline,double newjuice,int leagueid,int period,String type)
    {
        if(oldjuice == 0)
        {
            return false;
        }
        double movePercent = percentOfMove(oldline,oldjuice,newline,newjuice,leagueid,period,type);
        if(movePercent > 0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    public static boolean didNewLineMoveEnough(double desiredPercent, double oldline,double oldjuice,double newline,double newjuice,int leagueid,int period,String type)
    {
        double movePercent = percentOfMove(oldline,oldjuice,newline,newjuice,leagueid,period,type);
        //System.out.println("MOVE ENOUGH? "+movePercent+">="+desiredPercent+".."+oldline+".."+oldjuice+".."+newline+".."+newjuice+".."+leagueid+".."+type);
        if(Math.abs(movePercent*100.00) >= desiredPercent)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}