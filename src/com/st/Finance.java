package com.st;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import org.json.*;

public class Finance {
	String[] curSetString=new String[]{"AUD", "GBP", "EUR", "JPY", "CHF", "USD", "AFN", "ALL", "DZD", "AOA", "ARS", "AMD", "AWG", "AUD", "AZN", "BSD", "BHD", "BDT", "BBD", "BYR", "BZD", "BMD", "BTN", "BOB", "BAM", "BWP", "BRL", "GBP", "BND", "BGN", "BIF", "XOF", "XAF", "XPF", "KHR", "CAD", "CVE", "KYD", "CLP", "CNY", "COP", "KMF", "CDF", "CRC", "HRK", "CUC", "CUP", "CYP", "CZK", "DKK", "DJF", "DOP", "XCD", "EGP", "SVC", "EEK", "ETB", "EUR", "FKP", "FJD", "GMD", "GEL", "GHS", "GIP", "XAU", "GTQ", "GNF", "GYD", "HTG", "HNL", "HKD", "HUF", "ISK", "INR", "IDR", "IRR", "IQD", "ILS", "JMD", "JPY", "JOD", "KZT", "KES", "KWD", "KGS", "LAK", "LVL", "LBP", "LSL", "LRD", "LYD", "LTL", "MOP", "MKD", "MGA", "MWK", "MYR", "MVR", "MTL", "MRO", "MUR", "MXN", "MDL", "MNT", "MAD", "MZN", "MMK", "ANG", "NAD", "NPR", "NZD", "NIO", "NGN", "KPW", "NOK", "OMR", "PKR", "PAB", "PGK", "PYG", "PEN", "PHP", "PLN", "QAR", "RON", "RUB", "RWF", "WST", "STD", "SAR", "RSD", "SCR", "SLL", "XAG", "SGD", "SKK", "SIT", "SBD", "SOS", "ZAR", "KRW", "LKR", "SHP", "SDG", "SRD", "SZL", "SEK", "CHF", "SYP", "TWD", "TZS", "THB", "TOP", "TTD", "TND", "TRY", "TMM", "USD", "UGX", "UAH", "UYU", "AED", "VUV", "VEB", "VND", "YER", "ZMK", "ZWD"};
	HashSet<String> curSet=new HashSet<String>();
	
	Finance(){
		for(int i=0;i<curSetString.length;i++) curSet.add(curSetString[i]);
	}
	
	private JSONObject json;
	/**
	 * 输入url,输出url的查询结果
	 * 
	 * @param urlPath
	 * @return
	 * @throws Exception
	 */
	private String getJsonString(String urlPath) throws Exception {
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		InputStream inputStream = connection.getInputStream();
		// 对应的字符编码转换
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String str = null;
		StringBuffer sb = new StringBuffer();
		while ((str = bufferedReader.readLine()) != null) {
			sb.append(str);
		}
		reader.close();
		connection.disconnect();
		return sb.toString();
	}

	/**
	 * 输入货币种类1和2 调用private方法getJsonString
	 * 
	 * @param cur1
	 *            货币source
	 * @param cur2
	 *            货币to
	 * @return json格式的字符串数据
	 * @throws Exception
	 */
	public String getQueryResultInJasonForm(String cur1 , String cur2) throws Exception{
		if( !(curSet.contains(cur1) || curSet.contains(cur2)) ) return "货币种类输入错误";
		String querystr = "http://api.k780.com:88/?app=finance.rate&scur="+cur1+"&tcur="+cur2+"&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4";
		return getJsonString(querystr);
	}
	
	/**初始化，初始化后才能使用get方法查询
	 * @param cur1
	 * @param cur2
	 * @throws JSONException
	 * @throws Exception
	 */
	private void init(String cur1, String cur2) throws JSONException, Exception{
		json=new JSONObject(getQueryResultInJasonForm(cur1, cur2));
	}
	
	/**是否需要更新jason这个对象：1.当jason为null时 2.当jason的货币信息与查询请求不符时
	 * @param cur1
	 * @param cur2
	 * @return
	 */
	private boolean needInit(String cur1,String cur2){
		return json==null || 
			!(json.getJSONObject("result").get("scur").equals(cur1) 
					&& json.getJSONObject("result").get("tcur").equals(cur2));
	}
	/**获取更新时间
	 * @param cur1
	 * @param cur2
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public String getUpdateTime(String cur1,String cur2) throws JSONException, Exception{
		if( !(curSet.contains(cur1) || curSet.contains(cur2)) ) return "货币种类输入错误";
		if(needInit(cur1,cur2)) init(cur1,cur2);
		return json.getJSONObject("result").getString("update");
	}
	/**获取汇率
	 * @param cur1
	 * @param cur2
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public String getRate(String cur1,String cur2) throws JSONException, Exception{
		if( !(curSet.contains(cur1) || curSet.contains(cur2)) ) return "货币种类输入错误";
		if(needInit(cur1,cur2)) init(cur1,cur2);
		return json.getJSONObject("result").getString("rate");
	}
	/**获取以人民币为基准的汇率
	 * @param cur1
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public String getRateBasedOnCNY(String cur1) throws JSONException, Exception{
		if( !(curSet.contains(cur1)) ) return "货币种类输入错误";
		if(needInit("CNY",cur1)) init("CNY",cur1);
		return json.getJSONObject("result").getString("rate");
	}
	/**获取转换的两个币种的描述
	 * @param cur1
	 * @param cur2
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public String getDescription(String cur1,String cur2) throws JSONException, Exception{
		if( !(curSet.contains(cur1) || curSet.contains(cur2)) ) return "货币种类输入错误";
		if(needInit(cur1,cur2)) init(cur1,cur2);
		return json.getJSONObject("result").getString("ratenm");
	}
	
	/**使用样例
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception{
		Finance f = new Finance();
		//System.out.println(f.getQueryResultInJasonForm("CNY", "CAD"));
		System.out.println(f.getRate("CAD", "CNY"));
		System.out.println(f.getRateBasedOnCNY("CAD"));
		//System.out.println(f.getDescription("CAD", "CNY"));
		
		//System.out.println(f.getUpdateTime("CAD", "CNY"));
	}
}
