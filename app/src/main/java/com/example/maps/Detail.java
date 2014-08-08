package com.example.maps;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.appnext.appnextsdk.Appnext;
import com.appnext.appnextsdk.PopupClickedInterface;
import com.appnext.appnextsdk.PopupClosedInterface;
import com.appnext.appnextsdk.PopupOpenedInterface;
import com.google.android.gms.internal.ap;
import com.google.android.gms.maps.model.Marker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Detail extends Activity implements OnClickListener{

	TextView name,address,pricelevel,rating,website,status;
	ImageView call,share;
	String phoneno="",url=" ";
	LinearLayout websit,revter;
	HashMap<Integer, List<String>> review;
	Marker mark;
	String msg1="";
    Appnext appnext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
        init();

        appnext = new Appnext(this);
        appnext.setAppID("509d2dd8-7e0f-41cc-8b14-3900f49712a6"); // Set your AppID
        appnext.addMoreAppsRight("e546fb48-af38-45f2-baa9-827f4b66ed21");
        appnext.showBubble(); // show the interstitial
        appnext.addAppWallBottom("e546fb48-af38-45f2-baa9-827f4b66ed21");

        appnext.setPopupClosedCallback(new PopupClosedInterface() {
            @Override
            public void popupClosed() {
                Log.v("appnext", "popup closed");

            }
        });

        appnext.setPopupClickedCallback(new PopupClickedInterface() {
            @Override
            public void popupClicked() {
                Log.v("appnext", "popup clicked");

            }
        });
        appnext.setPopupOpenedInterface(new PopupOpenedInterface() {
            @Override
            public void popupOpened() {

            }
        });


	}

    private void init() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bitmap_alertbackground));

        if(DetailSearch.alter.getType().isEmpty())
        {
            getActionBar().setTitle("Naksha");
        }else
        {
            getActionBar().setTitle(DetailSearch.alter.getType().toUpperCase(Locale.ENGLISH).replace("_", " "));
        }

        mark=DetailSearch.alter.getMarker();
        name=(TextView)findViewById(R.id.textView_name);
        name.setText(DetailSearch.alter.getName());
        address=(TextView)findViewById(R.id.textView_address);
        address.setText(DetailSearch.alter.getVicinity());
        pricelevel=(TextView)findViewById(R.id.textView_pricelevel);
        pricelevel.setText("PriceLevel :"+DetailSearch.alter.getPricelevel());
        rating=(TextView)findViewById(R.id.textView_rating);
        rating.setText("Rating :"+DetailSearch.alter.getRating());
        website=(TextView)findViewById(R.id.textView_website);
        website.setText(DetailSearch.alter.getWebsite());
        call=(ImageView)findViewById(R.id.button_call);
        share=(ImageView)findViewById(R.id.button_share);
        phoneno=DetailSearch.alter.getPhoneno();
        call.setOnClickListener(this);
        share.setOnClickListener(this);
        websit=(LinearLayout)findViewById(R.id.LinearLayout1_webiste);
        websit.setOnClickListener(this);
        url=DetailSearch.alter.getWebsite();

        status=(TextView)findViewById(R.id.text_status);
        status.setText(DetailSearch.alter.getStatus());

        msg1=DetailSearch.alter.getName()+"\n"+DetailSearch.alter.getVicinity();

        review=new HashMap<Integer, List<String>>();
        review=DetailSearch.alter.getReviews();

        if(review.size()>0)
            Log.v("size of review", review.get(0).get(1) + " ");
        revter=(LinearLayout)findViewById(R.id.linearlayout_addreviews);


        if(review.size()==0)
        {
            TextView ed1=new TextView(this);   		// for name
            ed1.setGravity(Gravity.CENTER);
            ed1.setText("No reviews to show");
            ed1.setPadding(0, 5, 0, 2);
            ed1.setTextColor(Color.parseColor("#ff58585b"));
            ed1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            revter.addView(ed1);
        }
        else
        {
            for(int i=0;i<review.size();i++)
            {
                LinearLayout lll=new LinearLayout(this);
                lll.setOrientation(LinearLayout.VERTICAL);
                lll.setPadding(8, 5, 5, 5);

                LinearLayout l=new LinearLayout(this);
                l.setOrientation(LinearLayout.HORIZONTAL);

                ImageView img=new ImageView(this);
                img.setImageResource(R.drawable.user);


                TextView ed1=new TextView(this);   		// for name
                ed1.setGravity(Gravity.CENTER_VERTICAL);
                ed1.setText(review.get(i).get(0)+" : ");
                ed1.setPadding(3, 0, 1, 0);
                ed1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                //			ed1.setTextColor(Color.parseColor("#ff58585b"));
                l.addView(img);
                l.addView(ed1);


                TextView ed2=new TextView(this);		// for review
                ed2.setPadding(5, 2, 5, 5);
                ed2.setText(review.get(i).get(1));
                ed2.setTextColor(Color.parseColor("#ff58585b"));
                lll.addView(l);
                lll.addView(ed2);
                revter.addView(lll);
            }}
    }

    @Override
	public void onBackPressed() {
		if(appnext.isBubbleVisible())
        {
            appnext.hideBubble();
            return;
        }
        super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.upp,R.anim.down);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			finish();
			overridePendingTransition(R.anim.upp,R.anim.down);
		}
		else if(item.getItemId()==R.id.navigation)
		{
			String uri = "http://maps.google.com/maps?saddr=" +MainActivity.lat+","+MainActivity.lon+"&daddr="+mark.getPosition().latitude
					+","+mark.getPosition().longitude;

			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
			intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
			startActivity(intent);

		}

		return super.onOptionsItemSelected(item);

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public void onClick(View v) 
	{
		long id=v.getId();
		if(id==R.id.button_call)
		{
			if(!phoneno.isEmpty()) 
			{	
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+phoneno));
				startActivity(callIntent);
			}else
			{
				Toast.makeText(getApplicationContext(), "Phoneno. missing", Toast.LENGTH_SHORT).show();
			}
		}else if(id==R.id.button_share)
		{
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, msg1);
			sendIntent.setType("text/plain");
			startActivity(Intent.createChooser(sendIntent,"Share using"));
		}
		else if(id==R.id.LinearLayout1_webiste)
		{

			if(!url.isEmpty())
			{Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);	}
		}

	}

}
