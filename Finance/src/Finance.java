import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Finance {
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
	public String getQueryResult(String cur1 , String cur2) throws Exception{
		String querystr = "http://api.k780.com:88/?app=finance.rate&scur="+cur1+"&tcur="+cur2+"&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4";
		return getJsonString(querystr);
	}
	
	public static void main(String args[]) throws Exception{
		Finance f = new Finance();
		System.out.println(f.getQueryResult("CNY", "CAD"));
	}
}
