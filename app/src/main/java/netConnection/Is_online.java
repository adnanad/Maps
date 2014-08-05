package netConnection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Is_online   // 
{


	public static boolean isOnline(Context context) {
	    ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        try {
	            
	        	URL url = new URL("http://www.google.com");
	            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	            urlc.setConnectTimeout(8000);
	            urlc.connect();
	            if (urlc.getResponseCode() == 200) {
	                return true;
	            }
	        } catch (MalformedURLException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        return false;
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        return false;
	        }
	    }
	    return false;
	}
}
