import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
class Test 

{
	static ArrayList <ChartData2> cl=new ArrayList <ChartData2>();

public static void downloadFileWithAuth(String urlStr, String user, String pass) {
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
			int ln=(int)s.charAt(0);
			if(ln==13){
				break;
			}
			String[] s1=s.split(",",13) ;
			ChartData2 cd=new ChartData2();
			if(s1.length<13)
			{
				//errormsg="Data not proper at line please check your Url File";
				//new ShowError().setVisible(true);
				cl.clear();
				//Thread.currentThread().stop();
				//return;
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
					cl.get(i).drawMamt=dma;
					cl.get(i).aoaT=cl.get(i).aoaT+aTToa;
					cl.get(i).auaT=cl.get(i).auaT+aTTua;
					cl.get(i).hoaT=cl.get(i).hoaT+hTToa;
					cl.get(i).huaT=cl.get(i).huaT+hTTua;
			}
		}
		}
	for(int i=0;i<cl.size();i++)
	{
		System.out.println(cl.get(i).gn);
	}
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
  public static void main(String[] args) 
{

try
{

String ip = "71.172.25.164";

downloadFileWithAuth("http://"+ip+":9452/sia/spankoddschart.csv", "michelle", "gym");





Thread.sleep(2000);
}
catch(Exception ex)
{
System.out.println("exception "+ex);
}


}

}