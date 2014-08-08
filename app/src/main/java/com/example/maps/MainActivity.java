package com.example.maps;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.Ads.AdActivty;
import com.Ads.EditNameDialog;
import com.Ads.Myad;
import com.appnext.appnextsdk.API.AppnextAPI;
import com.appnext.appnextsdk.API.AppnextAd;
import com.appnext.appnextsdk.API.AppnextAdRequest;
import com.appnext.appnextsdk.Appnext;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements LocationListener, OnMapLongClickListener, OnEditorActionListener, TextWatcher {
    //---------------------------------------------------------
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] drawer_places;
    private String type;
    boolean visible = true;
    private GoogleMap map;
    Location loc;
    static double lat, lon;
    LocationManager locman;
    String bestprovider;
    public static Marker userMarker;
    static public Marker[] placeMarkers;
    private final int MAX_PLACES = 20;//most returned from google
    static ProgressDialog mDialog;
    int img[] = {R.drawable.bank, R.drawable.bar, R.drawable.busstop, R.drawable.coffee, R.drawable.school, R.drawable.supermarket, R.drawable.restaurant, R.drawable.theater, R.drawable.train, R.drawable.airport, R.drawable.shooting, R.drawable.firstaid, R.drawable.fire, R.drawable.fillingstation, R.drawable.parking};
    String tx[] = {"Bank", "Bar", "Bus Stop", "Coffee", "School", "Store", "Food", "Movie Theater", "Train Station", "Airport", "Police", "Hospital", "Fire Station", "Gas Station", "Parking"};
    ImageButton mylocation;
    View vw;
    AutoCompleteTextView sv;
    ImageView cancel;
    ActionBar actionbar;
    static Menu menu_m;
    Appnext appnext;
    AppnextAPI appnextAPI;

    public ArrayList<AppnextAd> getAd_list() {
        return ad_list;
    }

    ArrayList<AppnextAd> ad_list;
    LinearLayout horizontalview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //--------------------------------------
        mTitle = mDrawerTitle = getTitle();
        drawer_places = getResources().getStringArray(R.array.place_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        horizontalview=(LinearLayout)findViewById(R.id.horizontalview);
        mylocation = (ImageButton) findViewById(R.id.imageView_myloc);

        //set a custom shadow that overlays the main content when the drawer opens

        LayoutInflater lv = getLayoutInflater();


        //-------------------------------------search----------------------------

        sv = (AutoCompleteTextView) findViewById(R.id.editText1);
        Button search = (Button) findViewById(R.id.btn_search);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, array);
        sv.setOnEditorActionListener(this);
        sv.addTextChangedListener(this);
        cancel = (ImageView) findViewById(R.id.imageView_clear_search);
        cancel.setVisibility(View.GONE);

        //----------------------- -------------------------------------------------

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener

        mDrawerList.setAdapter(new Customadapter(this, tx, img));


        mDrawerList.setFastScrollEnabled(true);


        actionbar = getActionBar();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bitmap_alertbackground));

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }
        };

        ad_list=new ArrayList<AppnextAd>();

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Log.e("tag2", "" + arg2);
                //Networking network= new Networking();

                if (Networking.isNetworkAvailable(getApplicationContext())) {
                    type = drawer_places[arg2];
                    mDrawerLayout.closeDrawer(mDrawerList);

                    mDialog = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_DARK);
                    mDialog.setCancelable(false);
                    mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mDialog.setMessage(progressbar_message(arg2));
                    mDialog.show();
                    updateplaces();
                } else {
                    mDrawerLayout.closeDrawer(mDrawerList);
                    Toast.makeText(getApplicationContext(), "Check your Internet connectivity", Toast.LENGTH_SHORT).show();

                }


            }

        });

        //-----------------------------

//        appnext = new Appnext(this);
//        appnext.setAppID("509d2dd8-7e0f-41cc-8b14-3900f49712a6"); // Set your AppID
//        appnext.showBubble(); // show the interstitial

        appnextAPI = new AppnextAPI(getApplicationContext(), "509d2dd8-7e0f-41cc-8b14-3900f49712a6");

//        appnextAPI.setAdListener(new AppnextAPI.AppnextAdListener() {
//                                     @Override
//                                     public void onError(String error)
//                                     {
//                                         Toast.makeText(getApplicationContext(), "Failed to load ad", Toast.LENGTH_SHORT).show();
//                                     }
//
//                                     @Override
//                                     public void onAdsLoaded(ArrayList<AppnextAd> ads) {
//
//                                         Toast.makeText(getApplicationContext(), "" + ads.size(), Toast.LENGTH_SHORT).show();
//                                         ad_list.addAll(ads);
//                                         for (int i=0;i<ads.size();i++)
//                                         {
//                                             ImageView img=new ImageView(getApplicationContext());
//                                             Picasso.with(getApplicationContext()).load(ads.get(i).getImageURL()).placeholder(android.R.drawable.ic_menu_gallery).into(img);
//                                             img.setTag(i);
//
//                                             final int finalI = i;
//                                             img.setOnClickListener(new OnClickListener() {
//                                                 @Override
//                                                 public void onClick(View view) {
//                                                     Myad ad=new Myad();
//                                                     AppnextAd addd=ad_list.get(finalI);
//                                                     ad.setImageURL(addd.getImageURL());
//                                                     ad.setAdTitle(addd.getAdTitle());
//
//                                                     Intent in=new Intent(MainActivity.this, AdActivty.class);
//                                                     in.putExtra("com.naksha.ad",ad);
//                                                     ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(view, 0, 0,view.getWidth(), view.getHeight());
//                                                     startActivity(in,opts.toBundle());
////                                                     startActivity(in);
////                                                     appnextAPI.adClicked(ad_list.get(finalI));
//                                                 }
//                                             });
//                                             img.setOnLongClickListener(new View.OnLongClickListener() {
//                                                 @Override
//                                                 public boolean onLongClick(View view) {
//                                                     appnextAPI.adClicked(ad_list.get(finalI));
//                                                     return true;
//                                                 }
//                                             });
//                                             horizontalview.addView(img);
//                                         }
//                                     }
//                                 }
//
//        );
        appnextAPI.setAdListener(new AppnextAPI.AppnextAdListener() {
            @Override
            public void onError(String error) { }
            @Override
            public void onAdsLoaded(ArrayList<AppnextAd> ads) {
                EditNameDialog dialog=new EditNameDialog(ads);
                dialog.show(getSupportFragmentManager(),"hello");
                ad_list=ads;

            }
        });

        AppnextAdRequest adRequest=new AppnextAdRequest();
        adRequest.setCount(5);
        appnextAPI.loadAds(adRequest);


        if (map == null) {
            //get the map
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();
            //check in case map/ Google Play services not available
            if (map != null) {
                Log.v("map!=null", "ok");
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                placeMarkers = new Marker[MAX_PLACES];

                locman = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                Criteria criteria = new Criteria();
                //	criteria.setAccuracy(Criteria.ACCURACY_FINE);			// application crashes if uncommented
                bestprovider = locman.getBestProvider(criteria, true);
                Location lastloc = locman.getLastKnownLocation(bestprovider);

                if (lastloc != null) {
                    lat = lastloc.getLatitude();
                    lon = lastloc.getLongitude();
                    onLocationChanged(lastloc);  // because of missing this, shows wrong current position in samsung note
                } else {
                    Toast.makeText(getApplicationContext(), "Location not Available:", Toast.LENGTH_SHORT).show();
                }
                locman.requestLocationUpdates(bestprovider, 10 * 1000, 1, this);


                map.setMyLocationEnabled(true);

                UiSettings u = map.getUiSettings();
                u.setCompassEnabled(false);
                u.setMyLocationButtonEnabled(false);
                u.setZoomControlsEnabled(false);

            }
        }


        //---------------------------------------------search button

        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                type = sv.getText().toString().trim().toLowerCase();
                mDrawerLayout.closeDrawer(mDrawerList);
                hidekeyboard();
                mDialog = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_DARK);
                mDialog.setCancelable(true);
                mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mDialog.setMessage("Searching " + type + "...");
                mDialog.show();
                textsearch(type);

            }
        });

        mylocation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
                map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
        });
        //clearing the search box
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sv.setText("");
            }
        });


    }

    //---------------------------------------------------------------------------

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        locman.removeUpdates(this);
    }

    private void updateplaces() {
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
        map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/search/json?location=" + lat + "," + lon + "&radius=8000&types=" + type + "&sensor=false&key=AIzaSyA-0RFP4wGOWNar9RkUKcbEDOtNknrT-aY";
        GetPlaces getPlaces = new GetPlaces(this, placeMarkers, map);
        getPlaces.execute(placesSearchStr);
        map.setOnMapLongClickListener(this);

    }

    private void textsearch(String ty) {
        String t = ty;
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
        map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + t + "&location=" + lat + "," + lon + "&radius=8000&sensor=false&key=AIzaSyA-0RFP4wGOWNar9RkUKcbEDOtNknrT-aY";
        GetPlaces getPlaces = new GetPlaces(this, placeMarkers, map);
        getPlaces.execute(placesSearchStr);
        map.setOnMapLongClickListener(this);


    }

    // this is called only once go to onprepareoptions item selected
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu_m = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //hide menu items here
    // runs every time drawer is opened or closed
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu_m = menu;
        if (GetPlaces.items != null) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.list));

        } else {
            menu.getItem(0).setVisible(false);
        }

        if (drawerOpen) {
            MenuItem lister = menu.findItem(R.id.list12);
            lister.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            //open close drawer

            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        if (item.getItemId() == android.R.id.home) {

            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {

                mDrawerLayout.closeDrawer(mDrawerList);

            } else {

                mDrawerLayout.openDrawer(mDrawerList);

            }
        } else if (item.getItemId() == R.id.list12 && GetPlaces.items != null) {
            Alertdialog ad = new Alertdialog(this, GetPlaces.items, placeMarkers, map);
        } else {

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        lat = location.getLatitude();
        lon = location.getLongitude();

    }


    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    String[] array = {"accounting", "airport", "amusement_park", "aquarium", "art_gallery", "atm", "bakery", "bank", "bar", "beauty_salon", "bicycle_store",
            "book_store", "bowling_alley", "bus_station", "cafe", "campground", "car_dealer", "car_rental", "car_repair", "car_wash", "casino", "cemetery",
            "church", "city_hall", "clothing_store", "convenience_store", "courthouse", "dentist", "department_store",
            "doctor", "electrician", "electronics_store", "embassy", "establishment", "finance", "fire_station", "florist", "food",
            "funeral_home", "furniture_store", "gas_station", "general_contractor", "grocery_or_supermarket", "gym",
            "hair_care", "hardware_store", "health", "hindu_temple", "home_goods_store", "hospital", "insurance_agency",
            "jewelry_store", "laundry", "lawyer", "library", "liquor_store", "local_government_office", "locksmith",
            "lodging", "meal_delivery", "meal_takeaway", "mosque", "movie_rental", "movie_theater", "moving_company",
            "museum", "night_club", "painter", "park", "parking", "pet_store", "pharmacy", "place_of_worship", "plumber", "police", "post_office",
            "real_estate_agency", "restaurant", "roofing_contractor", "rv_park", "school", "shoe_store", "shopping_mall",
            "spa", "stadium", "storage", "store", "subway_station", "synagogue", "taxi_stand", "train_station", "travel_agency",
            "university", "veterinary_care", "zoo",
    };


    @Override
    public void onMapLongClick(LatLng arg0) {
        // TODO Auto-generated method stub

        final LatLng pos = arg0;
        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        ab.setTitle("Directions").setMessage("Do you want to get Directions?").setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                String uri = "http://maps.google.com/maps?saddr=" + lat + "," + lon + "&daddr=" + pos.latitude
                        + "," + pos.longitude;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        ab.create();
        ab.show();
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try {
            locman.requestLocationUpdates(bestprovider, 10 * 1000, 1, this); // update after every 10 seconds or every 1 meter
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub

        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            type = sv.getText().toString().trim().toLowerCase();
            mDrawerLayout.closeDrawer(mDrawerList);
            hidekeyboard();
            mDialog = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_DARK);
            mDialog.setCancelable(true);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setMessage("Loading....");
            mDialog.show();

            textsearch(type);

        }
        return false;
    }

    private void hidekeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public String progressbar_message(int i) {
        String message = null;

        switch (i) {
            case 0:
                message = "Looking for Bank...";
                break;
            case 1:
                message = "lookin for Bar...";
                break;
            case 2:
                message = "Finding bustop...";
                break;
            case 3:
                message = "Goodtime for a cup of Coffee";
                break;
            case 4:
                message = "Looking for Schools...";
                break;
            case 5:
                message = "Searching...";
                break;
            case 6:
                message = "Looking for something to eat...";
                break;
            case 7:
                message = "Searching Movie theaters...";
                break;
            case 8:
                message = "Searching...";
                break;
            case 9:
                message = "Searching...";
                break;
            case 10:
                message = "Looking for Help...";
                break;
            case 11:
                message = "Looking for Help...";
                break;
            case 12:
                message = "Looking for Help...";
                break;
            case 13:
                message = "Looking...";
                break;
            case 14:
                message = "Finding a Parking spot..";
                break;

            default:
                message = "Searching...";
        }
        return message;
    }


    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        if (!cancel.isShown()) {
            cancel.setVisibility(View.VISIBLE);
        } else if (s.toString().trim().isEmpty())        // string is empty
        {
            cancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onBackPressed() {
//        if (appnext.isBubbleVisible()) {
//            appnext.hideBubble();
//        } else {
        super.onBackPressed();
//        }

    }
}
