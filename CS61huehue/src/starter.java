import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class starter {
	
	private static int numthreads = 50;
	private static char mode = ' ';
	private static String url = "<url>commands.txt"; // will read command from this file on the net

	private static class PostThread extends Thread{
		
		
		Bloop b;
		public PostThread()
		{
			try {
				b = new Bloop();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run()
		{
			try
			{
				while(mode != 'q')
				{
					if(mode == 's')
					{
						spazz(1000);
					}
					else if(mode == 'r')
					{
						b.red();
					}
					else if(mode == 'g')
					{
						b.green();
					}
					else if(mode == 'b')
					{
						b.blue();
					}
					else if(mode == 'c')
					{
						b.cancel();
					}
					else
					{
						Thread.sleep(100);
					}
					
				}
				b.cancel();
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
		
		private void spazz(long time)
		{
			try
			{
				long endtime = System.currentTimeMillis() + time;
				while(System.currentTimeMillis() < endtime)
				{
					int color = (int)(Math.random()*3);
					if(color == 0)
					{
						b.red();
					}
					else if(color == 1)
					{
						b.green();
					}
					else
					{
						b.blue();
					}
					
					Thread.sleep(150);
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	
	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		
		// init
		boolean networkmode;
		if(args.length < 1)
		{
			System.out.println("network or console");
			return;
		}
		
		if(args[0].equals("network"))
		{
			networkmode = true;
		}
		else if(args[0].equals("console"))
		{
			networkmode = false;
		}
		else
		{
			return;
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(numthreads);
		for(int temp = 0; temp < numthreads; temp++)
		{
			executor.execute(new PostThread());
		}
		
		
		// network mode and console mode. because bazookas
		CloseableHttpClient httpClient = HttpClients.createDefault();
		EasyReader console = new EasyReader();

		// not sure why command and mode are different but they are. 
		System.out.println("READY");
		String command = "";
		while(!command.equals("quit"))
		{
			if(networkmode)
			{
				CloseableHttpResponse r = httpClient.execute(new HttpGet(url));
				command = new BufferedReader(new InputStreamReader(r.getEntity().getContent())).readLine();
				System.out.println("COMMAND ENTERED: " + command);
				r.close();
			}
			else
			{
				command = console.readLine();
			}
			
			if(command.equals("stop"))
			{
				mode = ' ';
			}
			else if(command.equals("red"))
			{
				mode = 'r';
			}
			else if(command.equals("green"))
			{
				mode = 'g';
			}
			else if(command.equals("blue"))
			{
				mode = 'b';
			}
			else if(command.equals("cancel"))
			{
				mode = 'c';
			}
			else if(command.equals("spazz"))
			{
				mode = 's';
			}
			
			Thread.sleep(1000);
		}
		mode = 'q';
		
		
		
		executor.shutdown();
		try {
			executor.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
