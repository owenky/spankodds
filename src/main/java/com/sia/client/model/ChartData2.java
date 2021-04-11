package com.sia.client.model;

public class ChartData2 {
	public int gn;
	public int p;
	public int asaT;
     public int hsaT;
	 public String DS;
	public int oaT;
	public int uaT;
	public String DT;
	public int amaT;
	public int hmaT;
	public String DM;
	public int drawMamt;
	public int aoaT;
	public int auaT;
	public String DA;
	public int hoaT;
	public int huaT;
	public String DH;

	 @Override
	public String toString()
	{
		return gn+","
		+p+","
		+asaT+","
		+hsaT+","
		+DS+","
		+oaT+","
		+uaT+","
		+DT+","
		+amaT+","
		+hmaT+","
		+DM;
	}
}
