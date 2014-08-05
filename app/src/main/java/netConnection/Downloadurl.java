package netConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class Downloadurl {
	
	public static String downloadUrl(String strUrl) throws IOException
	
	{
		String data =null;
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try{
			URL url = new URL(strUrl);

			//Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(9000);
			// Connecting to url
			urlConnection.connect();
			if(urlConnection.getResponseCode()==200)
			{
				// Reading data from url
			
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb  = new StringBuffer();

			String line = "";
			while( ( line = br.readLine())  != null){
				sb.append(line);
			}

			data = sb.toString();
			Log.v("data", data.toString());
			br.close();
			}else
			{
				Log.v("connenction error response code not ok","error");
			}
		}catch(Exception e){
			Log.d("Exception while downloading url", e.toString());
		}
		finally
		{	
			if(iStream!=null)
		{
			iStream.close();
		}	
			urlConnection.disconnect();
		}
		
		return data;
	}
}
