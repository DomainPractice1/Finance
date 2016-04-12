import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Finance {
	/**����url,���url�Ĳ�ѯ���
	 * @param urlPath
	 * @return
	 * @throws Exception
	 */
	private String getJsonString(String urlPath) throws Exception {  
        URL url = new URL(urlPath);  
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
        connection.connect();  
        InputStream inputStream = connection.getInputStream();  
        //��Ӧ���ַ�����ת��  
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
	/**�����������1��2
	 * ����private����getJsonString
	 * @param cur1 ����source
	 * @param cur2 ����to
	 * @return json��ʽ���ַ�������
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
