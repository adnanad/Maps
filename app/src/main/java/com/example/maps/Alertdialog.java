package com.example.maps;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

@SuppressLint("NewApi")
public class Alertdialog {

	Context context;
	String[] items;
	String pictureurl="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=reference&sensor=true&key=AIzaSyA-0RFP4wGOWNar9RkUKcbEDOtNknrT-aY";
	ArrayList<Integer> selected;
	ArrayList<String> emptylist;
	ImageView im,img;
	TextView  txt,txt1,txt2,txt3;
	Marker placemarker[],ag;
	StringBuffer message=new StringBuffer();

	String link=null;

	@SuppressLint("NewApi")

	public Alertdialog(Context context,ArrayList<String> itemss,final Marker [] marker,final GoogleMap map) 
	{
		this.context = context;
		placemarker=marker;

		selected=new ArrayList<Integer>();
		items=new String[itemss.size()];
		AlertDialog.Builder alertbox = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_DARK);

		LayoutInflater lf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=lf.inflate(R.layout.alert,null);

		ListView lv=(ListView)view.findViewById(R.id.listView_alertbox);
		
		
		ArrayAdapter<String> adapter=new  ArrayAdapter<String>(context, R.layout.spinner_item,itemss);
		lv.setAdapter(adapter);
		alertbox.setView(view);
		
		if(itemss.size()>0)
		{
			MainActivity.menu_m.getItem(0).setIcon(context.getResources().getDrawable(R.drawable.list));
		}
		
		final AlertDialog ad=alertbox.create();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				placemarker[arg2].showInfoWindow();
				LatLng lm=placemarker[arg2].getPosition();
				map.moveCamera(CameraUpdateFactory.newLatLng(lm));
				ad.dismiss();

			}});
		ad.show();

	}









}



