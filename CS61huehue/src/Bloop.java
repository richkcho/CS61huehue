import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


public class Bloop {

	private String loginurl = "http://cs61-fall-2013.seas.harvard.edu:6169/cs61/login";
	private String stopurl = "http://cs61-fall-2013.seas.harvard.edu:6169/cs61/feedback/stop";
	private String gourl = "http://cs61-fall-2013.seas.harvard.edu:6169/cs61/feedback/ok";
	private String askurl = "http://cs61-fall-2013.seas.harvard.edu:6169/cs61/ask";
	private String cancelurl = "http://cs61-fall-2013.seas.harvard.edu:6169/cs61/feedback/cancel";
	private String table1 = "q=(%E2%95%AF%C2%B0%E2%96%A1%C2%B0%EF%BC%89%E2%95%AF%EF%B8%B5+%E2%94%BB%E2%94%81%E2%94%BB)";
	private String table2 = "q=%E2%94%AC%E2%94%80%E2%94%AC%E3%83%8E(+%C2%BA+_+%C2%BA%E3%83%8E)";
	
	private HttpContext cookie;
	
	CloseableHttpClient httpClient;
	
	public Bloop() throws ClientProtocolException, IOException
	{
		this(false);
	}
	
	public Bloop(boolean isfiddleron) throws ClientProtocolException, IOException
	{
		httpClient = HttpClients.createDefault();
		
		// run only when fiddler is on
		if(isfiddleron)
		{
			HttpHost proxy = new HttpHost("127.0.0.1", 8888);
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
			httpClient = HttpClients.custom().setRoutePlanner(routePlanner).build();
		}
		
		// first round to get cookie
		CloseableHttpResponse r1 = httpClient.execute(new HttpPost(loginurl));
		
		// lol were assuming the first one is teh cookie. 
		String scookie = r1.getAllHeaders()[0].getValue();
		scookie = scookie.substring(0, scookie.indexOf(';')); // trim dat
		r1.close();
		
		// set the cookie
		cookie = new BasicHttpContext();
		cookie.setAttribute("feedback61", scookie);
		
		// second round of login
		httpClient.execute(new HttpPost(loginurl), cookie).close();
	}
	
	public void red() throws ClientProtocolException, IOException
	{
		httpClient.execute(new HttpPost(stopurl), cookie).close();
	}
	
	public void green() throws ClientProtocolException, IOException
	{
		httpClient.execute(new HttpPost(gourl), cookie).close();
	}
	
	public void blue() throws ClientProtocolException, IOException
	{
		HttpPost q = new HttpPost(askurl);
		q.setEntity(new StringEntity((Math.random() < .5 ? table1 : table2)));
		httpClient.execute(q, cookie).close();
	}
	
	
	public void cancel() throws ClientProtocolException, IOException
	{
		httpClient.execute(new HttpPost(cancelurl), cookie).close();
	}
}
