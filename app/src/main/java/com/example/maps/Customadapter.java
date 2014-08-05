package com.example.maps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Customadapter extends ArrayAdapter<String>{

	private  Context context;
	private   String[] values;
	private  int[] color_name;
	public Customadapter(Context context, String[] values,int[] color_name) 
	{
		super(context, R.layout.custom_list, values);
		this.context = context;
		this.values = values;
		this.color_name=color_name;
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.custom_list, arg2, false);
		TextView textView = (TextView) rowView.findViewById(R.id.text);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
		textView.setText(values[arg0]);
		imageView.setImageResource(color_name[arg0]);
		return rowView;
	}
	
}
