package com.Ads;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appnext.appnextsdk.API.AppnextAPI;
import com.appnext.appnextsdk.API.AppnextAd;
import com.appnext.appnextsdk.API.AppnextAdRequest;
import com.example.maps.MainActivity;
import com.example.maps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EditNameDialog extends DialogFragment implements View.OnClickListener {

    private AppnextAd ad;
    ImageView adView,img_cancel,img_small,imageView2,send_mail;
    TextView textView3,textView5,button;
    AppnextAPI api;
    ArrayList<AppnextAd> ad_list1;
    int count=1;

    public EditNameDialog(ArrayList<AppnextAd> ad1) {
        // Empty constructor required for DialogFragment
        this.ad_list1=ad1;
        ad=ad1.get(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        api = new AppnextAPI(getActivity(), "509d2dd8-7e0f-41cc-8b14-3900f49712a6");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.ad_detail, container);
        adView = (ImageView)view.findViewById(R.id.adView);
        img_cancel = (ImageView)view.findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(this);
        img_small = (ImageView)view.findViewById(R.id.img_small);
        send_mail = (ImageView)view.findViewById(R.id.send_mail);
        send_mail.setOnClickListener(this);
        imageView2 = (ImageView)view.findViewById(R.id.imageView2);
        imageView2.setOnClickListener(this);

        button = (TextView)view.findViewById(R.id.button1);
        button.setOnClickListener(this);
        textView3=(TextView)view.findViewById(R.id.textView3);
        textView5=(TextView)view.findViewById(R.id.textView5);

        update_views();
        String possibleEmail="";

        try{
            possibleEmail += "************* Get Registered Gmail Account *************nn";
            Account[] accounts = AccountManager.get(getActivity()).getAccountsByType("com.google");

//            for (Account account : accounts) {
//
//                possibleEmail += " --> "+account.name+" : "+account.type+" , n";
//                possibleEmail += " nn";
//
//            }
            possibleEmail=accounts[0].name;
            textView5.setText(possibleEmail+"");
        }
        catch(Exception e)
        {
            Log.i("Exception", "Exception:"+e) ;
        }

        return view;

    }

    private void update_views() {
        Picasso.with(getActivity()).load(ad.getImageURL()).placeholder(R.drawable.transparent).into(adView);
        Picasso.with(getActivity()).load(ad.getImageURL()).placeholder(R.drawable.transparent).into(img_small);
        textView3.setText(ad.getAdTitle());
    }
    @Override
    public void onClick(View view) {
        if(view==img_cancel)
        {
            dismiss();
        }else if(view==button)
        {

            try {
                api.adClicked(ad);
            }catch (ActivityNotFoundException activity)
            {

            }
        }else if(view==imageView2)
        {
            //Todo refresh ad here
             if(count<ad_list1.size())
             {
                 ad=ad_list1.get(count);
                 update_views();
                 count++;
             }else
             {
                 count=0;
                 ad=ad_list1.get(count);
                 update_views();
             }
        }else if(view==send_mail)
        {
            api.sendEmail(ad);
            Toast.makeText(getActivity(),"Email sent",Toast.LENGTH_SHORT).show();
        }
    }
}