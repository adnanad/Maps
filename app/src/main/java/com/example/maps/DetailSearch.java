package com.example.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.weexcel.features.Alternate;

public class DetailSearch extends AsyncTask<String, Void, String>{

	String data=null;
	
	HashMap<Integer, List<String>> review;
	Context context;
	String type,name;
	
	Marker ag;
	static Alternate alter;
	public DetailSearch(Context context,String type,Marker ag0) {
		// TODO Auto-generated constructor stub

		this.context=context;
		this.type=type;
		this.ag=ag0;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			data=downloadUrl(params[0]);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.e("detail data",""+data);
		return data;
	}


	@Override
	protected void onPostExecute(String result) 
	{
		// TODO Auto-generated method stub
		name="";
		String vicinity="";
		String formatted_phone_number="";
		String icon="";
		String price_level="-";
		String rating="missing";
		String website="";
		String Status="";
		review=new HashMap<Integer, List<String>>();

		if(data!=null)
		{
			try {
				JSONObject resultobj=new JSONObject(result);
				JSONObject res=resultobj.getJSONObject("result");

				name=res.getString("name");
				icon=res.getString("icon");
				vicinity=res.getString("vicinity");

				if(res.has("opening_hours"))
				{	

					String open_now=res.getJSONObject("opening_hours").getString("open_now");
					if(open_now.equalsIgnoreCase("true"))
					{
						open_now="Open";
					}else
					{open_now="Not Open";}

					//					details.add( "Status :"+open_now);
					Status=open_now;
				}else
				{
					Status="-";
				}


				if(res.has("price_level"))
				{
					price_level=res.getString("price_level");

					if(price_level.contains("1"))
					{
						price_level="Very Cheap";

					}else if(price_level.contains("2"))
					{
						price_level="Cheap";

					}else if(price_level.contains("3"))
					{
						price_level="Moderate";
					}else if(price_level.contains("4"))
					{
						price_level="Expensive";
					}
					else if(price_level.contains("5"))
					{
						price_level="Very Expensive";
					}

				}else
				{
					price_level="missing";
				}

				if(res.has("formatted_phone_number"))
				{
					formatted_phone_number=res.getString("formatted_phone_number").replace(" ", "-");
				
				}

				if(res.has("website"))
				{
					website=res.getString("website");
				
				}

				if(res.has("rating"))
				{
					rating=res.getString("rating");
				}

				if(res.has("reviews"))
				{


					JSONArray rev=res.getJSONArray("reviews");

					for(int i=0;i<rev.length();i++)
					{
						JSONObject emptyname=rev.getJSONObject(i);
						String author_name="";
						String text="";
						List<String> item=new ArrayList<String>();

						author_name=emptyname.getString("author_name");
						Log.v("authorname", author_name);


						text=emptyname.getString("text");

						item.add(author_name);
						item.add(text);
						review.put(i, item);
						review.get(i).get(0);
						Log.v("authorname----", review.get(i).get(0));
					}
				}


			} catch (JSONException e) {

				e.printStackTrace();
			}


			GetPlaces.mDialog1.dismiss();

			//			Alertdialog a=new Alertdialog(context,details, type,contacts,name,ag);
			alter=new Alternate( type, name, ag,vicinity,price_level,rating,formatted_phone_number,website,Status,review);

			Intent in =new Intent(context,Detail.class);
			context.startActivity(in);
		

		}else
		{	GetPlaces.mDialog1.dismiss();
		Toast.makeText(context, "Check your internet Connection",0).show();

		}

	}
	private String downloadUrl(String strUrl) throws IOException
	{
		String data =null;
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try{
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(5000);
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

				br.close();
			}
		}catch(Exception e){
			Log.d("Exception while downloading url", e.toString());
		}
		finally
		{	if(iStream!=null)
		{
			iStream.close();
		}	
		urlConnection.disconnect();
		}

		return data;
	}
}
