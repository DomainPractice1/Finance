package com.st;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*;

public class Finance {
	private JSONObject json;
	/**输入url,输出url的查询结果
	 * @param urlPath
	 * @return
	 * @throws Exception
	 */
	private String getJsonString(String urlPath) throws Exception {  
        URL url = new URL(urlPath);  
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
        connection.connect();  
        InputStream inputStream = connection.getInputStream();  
        //对应的字符编码转换  
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
	/**输入货币种类1和2
	 * 调用private方法getJsonString
	 * @param cur1 货币source
	 * @param cur2 货币to
	 * @return json格式的字符串数据
	 * @throws Exception 
	 */
	public String getQueryResultInJasonForm(String cur1 , String cur2) throws Exception{
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
		if(needInit(cur1,cur2)) init(cur1,cur2);
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
		if(needInit(cur1,cur2)) init(cur1,cur2);
		return json.getJSONObject("result").getString("ratenm");
	}
	
	/**使用样例
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception{
		Finance f = new Finance();
		System.out.println(f.getDescription("CAD", "CNY"));
		System.out.println(f.getRate("CAD", "CNY"));
		System.out.println(f.getUpdateTime("CAD", "CNY"));
	}
}
 