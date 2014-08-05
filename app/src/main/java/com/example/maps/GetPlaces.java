package com.example.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class GetPlaces extends AsyncTask<String, Void, String> implements OnInfoWindowClickListener {

	String data=null;
	public Marker[] placeMarkers;
	private MarkerOptions[] places;
	Context context; 
	private int  foodIcon, drinkIcon,movieicon,trainicon,shopIcon, otherIcon,bankicon,schoolicon,cafeicon;
	private int airporticon,hospitalicon,firecon,gasicon,parkingicon,policeicon;
	private GoogleMap map;
	static ArrayList<String> items;
	private Map<String,String> ref ;
	String reference1;
	String refer=null;
	
	String thisType;
	static ProgressDialog mDialog1;
	public GetPlaces(Context context,Marker[] plaMarker,GoogleMap map)

	{
		placeMarkers=plaMarker;
		this.context=context;
		foodIcon = R.drawable.restaurant;
		drinkIcon = R.drawable.bar;
		shopIcon = R.drawable.supermarket;
		bankicon=R.drawable.bank;
		schoolicon=R.drawable.school;
		otherIcon = R.drawable.pin2;
		cafeicon=R.drawable.coffee;
		
		movieicon=R.drawable.theater;
		trainicon=R.drawable.train;
		airporticon=R.drawable.airport;
		policeicon=R.drawable.shooting;
		hospitalicon=R.drawable.firstaid;
		firecon=R.drawable.fire;
		gasicon=R.drawable.fillingstation;
		parkingicon=R.drawable.parking;
		this.map=map;
	}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub

		try {
			data=downloadUrl(arg0[0]);
			
			
			items =new ArrayList<String>();

			} catch (Exception e) {
			// TODO: handle exception
		} 

		return data;
	}

	protected void onPostExecute(String result) 
	{
		if(data!=null)
		{
			if(placeMarkers!=null){
			for(int pm=0; pm<placeMarkers.length; pm++){
				if(placeMarkers[pm]!=null)
					placeMarkers[pm].remove();
			}
				
		}
		try {
			
			ref=new HashMap<String,String>();
			//parse JSON
			//create JSONObject, pass string returned from doInBackground
			JSONObject resultObject = new JSONObject(result);
			//get "results" array
			JSONArray placesArray = resultObject.getJSONArray("results");
			//marker options for each place returned
			places = new MarkerOptions[placesArray.length()];
			
			//loop through places
			for (int p=0; p<placesArray.length(); p++) {
				//parse each place
				//if any values are missing we won't show the marker
				boolean missingValue=false;
				LatLng placeLL=null;
				String placeName="";
				String vicinity="";
				int currIcon = otherIcon;
				
				try{
					//attempt to retrieve place data values
					missingValue=false;
					//get place at this index
					JSONObject placeObject = placesArray.getJSONObject(p);
					//get location section
					JSONObject loc = placeObject.getJSONObject("geometry")
							.getJSONObject("location");
					//read lat lng
					placeLL = new LatLng(Double.valueOf(loc.getString("lat")), 
							Double.valueOf(loc.getString("lng")));	
					//get types
					JSONArray types = placeObject.getJSONArray("types");
					//loop through types
					for(int t=0; t<types.length(); t++){
						//what type is it
						thisType=types.get(t).toString();
						//check for particular types - set icons
						if(thisType.contains("cafe")){
							currIcon = cafeicon;
							break;
						}
						else if(thisType.contains("bar")){
							currIcon = drinkIcon;
							break;
						}
						else if(thisType.contains("store")){
							currIcon = shopIcon;
							break;
						}else if(thisType.contains("bank"))
						{
							currIcon=bankicon;
							break;
						}else if(thisType.contains("school"))
						{
							currIcon=schoolicon;
							break;
						}else if(thisType.contains("food"))
						{
							currIcon=foodIcon;
							break;
						}else if(thisType.contains("movie_theater"))
						{
							currIcon=movieicon;
							break;
						}else if(thisType.contains("train_station"))
						{
							currIcon=trainicon;
							break;
						}else if(thisType.contains("airport"))
						{
							currIcon=airporticon;
							break;
						}else if(thisType.contains("police"))
						{
							currIcon=policeicon;
							break;
						}else if(thisType.contains("hospital"))
						{
							currIcon=hospitalicon;
							break;
						}else if(thisType.contains("fire_station"))
						{
							currIcon=firecon;
							break;
						}else if(thisType.contains("gas_station"))
						{
							currIcon=gasicon;
							break;
						}else if(thisType.contains("parking"))
						{
							currIcon=parkingicon;
							break;
						}
						
						else
						{
							currIcon=otherIcon;
						
						}
					}
					//vicinity
					if (placeObject.has("vicinity"))
					{
						vicinity = placeObject.getString("vicinity");
					}else
					{
						vicinity=placeObject.getString("formatted_address");
					}
						//name
					placeName = placeObject.getString("name");
					reference1=placeObject.getString("reference");
					
					
				} 
				catch(JSONException jse){
					Log.v("PLACES", "missing value");
					missingValue=true;
					jse.printStackTrace();
				}
			
				//if values missing we don't display
				if(missingValue)	places[p]=null;
				else
					
				items.add(placeName);
				ref.put(placeName,reference1);
				
				Log.e("placesss", items+"");
				
				places[p]=new MarkerOptions()
				.position(placeLL)
				.title(placeName)
				.icon(BitmapDescriptorFactory.fromResource(currIcon))
				.snippet(vicinity);
				Log.e("refer", ref.get(places[p].getTitle()));
				Log.e("refer1", ref.size()+"");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
//		Alertdialog ad=new Alertdialog(context, items,placeMarkers,map);
		
		if((items.size()<1))
		{
			items=new ArrayList<String>();
			items.add("No such Place found");
		}
		
		MainActivity.menu_m.getItem(0).setVisible(true);
		
			if(places!=null && placeMarkers!=null){
			
			for(int p=0; p<places.length && p<placeMarkers.length; p++){
				//will be null if a value was missing
				if(places[p]!=null)
					placeMarkers[p]=map.addMarker(places[p]);
			}
			map.setOnInfoWindowClickListener(this);
		}
		
		}else
		{
			Toast.makeText(context, "Check your internet Connection",0).show();
		}
		MainActivity.mDialog.dismiss();
		
	}
	private String downloadUrl(String strUrl) throws IOException
	{
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try{
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(5000);
			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			if(urlConnection.getResponseCode()==200)
			{
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
		{
			if(iStream!=null)
			{
				iStream.close();
			}
			urlConnection.disconnect();
		}
		Log.e("data---------", ""+data);
		return data;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
			
		    
			refer=ref.get(arg0.getTitle());
	  


				if(Networking.isNetworkAvailable(context))
				{
					String detailPlaces="https://maps.googleapis.com/maps/api/place/details/json?reference="+refer+"&sensor=false&key=AIzaSyA-0RFP4wGOWNar9RkUKcbEDOtNknrT-aY";
				
					mDialog1=new ProgressDialog(context,ProgressDialog.THEME_HOLO_DARK);
					mDialog1.setCancelable(false);
					mDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					mDialog1.setMessage("Getting info...");
					mDialog1.show();
					DetailSearch detailSearch=new DetailSearch(context,thisType,arg0);
					detailSearch.execute(detailPlaces);
				
				}
				else{
				
					Toast.makeText(context, "Check your Internet connectivity", 1).show();
					

			
			}
			

			
			
			
	}  	
}