package com.example.cs61huehueapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	
	class PostIt extends AsyncTask<Void, Void, Void> {
		
		int mode;
		HttpClient httpclient;
		
		public PostIt(int modein)
		{
			httpclient = new DefaultHttpClient();
			mode = modein;
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			HttpPost post = new HttpPost(url);
			try {
				//post.setEntity(new StringEntity("command=" + mode)); // such jank
				List<NameValuePair> params = new ArrayList<NameValuePair>(1);
				params.add(new BasicNameValuePair("command", ""+mode));
				post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				HttpResponse r = httpclient.execute(post);
				System.out.println(r.getStatusLine().getStatusCode());
				//System.out.println(new BufferedReader(new InputStreamReader(r.getEntity().getContent())).readLine());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		
		
	}

	private String url = "<url>/commands.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClick_Stop(View view)
	{
		new PostIt(0).execute();
	}
	
	public void onClick_Red(View view)
	{
		new PostIt(1).execute();
	}
	
	public void onClick_Green(View view)
	{
		new PostIt(2).execute();
	}
	
	public void onClick_Blue(View view)
	{
		new PostIt(3).execute();
	}
	
	public void onClick_Cancel(View view)
	{
		new PostIt(4).execute();
	}
	
	public void onClick_Spazz(View view)
	{
		new PostIt(5).execute();
	}

}
