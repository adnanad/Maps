package com.Ads;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maps.R;
import com.squareup.picasso.Picasso;

/**
 * Created by krishan on 8/6/2014.
 */
public class AdActivty extends Activity implements View.OnClickListener {
    ImageView adView,img_cancel,img_small;
    TextView textView3;
    Myad myad;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_detail);

        myad = (Myad) getIntent().getExtras().getSerializable("com.naksha.ad");
        adView = (ImageView) findViewById(R.id.adView);
        img_cancel = (ImageView) findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(this);
        img_small = (ImageView) findViewById(R.id.img_small);
        img_small = (ImageView) findViewById(R.id.img_small);
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
        textView3=(TextView)findViewById(R.id.textView3);
        Log.v("tag",myad.getAdTitle()+"");

        Picasso.with(getApplicationContext()).load(myad.getImageURL()).into(adView);
        Picasso.with(getApplicationContext()).load(myad.getImageURL()).into(img_small);
        textView3.setText(myad.getAdDesc());
    }


    @Override
    public void onClick(View view) {
        if(view==img_cancel)
        {
            finish();
        }else if(view==button)
        {
            Uri marketUri = Uri.parse("market://details?id=" + myad.getAdPackage());
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }

    }
}
