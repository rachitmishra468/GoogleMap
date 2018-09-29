package com.media_mosaic.httpwww.doubloons.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.IndoorLevel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.DB.SavePref;
import com.media_mosaic.httpwww.doubloons.Fragments.Aboutus_Fragment;
import com.media_mosaic.httpwww.doubloons.Fragments.Advertise_eith_us;
import com.media_mosaic.httpwww.doubloons.Fragments.BuyPackage_fragment;
import com.media_mosaic.httpwww.doubloons.Fragments.Featured_doubloonFragment;
import com.media_mosaic.httpwww.doubloons.Fragments.ListFragment;
import com.media_mosaic.httpwww.doubloons.Fragments.Progressive_doubloons;
import com.media_mosaic.httpwww.doubloons.Fragments.Sponsored_doubloon;
import com.media_mosaic.httpwww.doubloons.Fragments.FaqFragment;
import com.media_mosaic.httpwww.doubloons.Fragments.Mydoubloons;
import com.media_mosaic.httpwww.doubloons.Fragments.Notification_fragment;
import com.media_mosaic.httpwww.doubloons.Fragments.Profilr_edit;
import com.media_mosaic.httpwww.doubloons.MyApplication;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {
    private GoogleMap mMap;
    private View navHeader;
    public static double latitude;
    public static double longitude;
    Intent intentThatCalled;
    private GoogleApiClient googleApiClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    public LocationManager locationManager;
    public Criteria criteria;
    String voice2text;
    private ProgressDialog pDialog;
    public String bestProvider;
    TextView toolbar_list, select_text, notification_count, my_doubloons, sponser, sponsored_text, adverties_us, featured_text, progress_text, notification_text, packages_text, about_us, faq, profile_text, logout;
    SupportMapFragment mapFragment;
    DrawerLayout drawer;
    Location location;
    private boolean showLevelPicker = true;
    LinearLayout packages_layout, add_with_us, notification_icon, exclusive_items, show_hide_data, home;
    ImageView show_hide;
    boolean flag = true;
    SavePref savePref;
    ReadPref readPref;
    boolean show = true;
    private DataAPICALL groceryAPICALL;
    Spinner distance_spinner;
    ImageView search_icon;
    private DataAppResponse groceryAppResponse;
    EditText txt_search_name;
    String DateToStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateToStr = format.format(curDate);
        savePref = new SavePref();
        readPref = new ReadPref(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.app_name);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        TextView hadder_name = (TextView) navHeader.findViewById(R.id.hadder_name);
        TextView hadder_email = (TextView) navHeader.findViewById(R.id.hadder_email);
        hadder_name.setText(readPref.getLoggedname());
        hadder_email.setText(readPref.getLoggedemail());
        navigationView.setNavigationItemSelectedListener(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        intentThatCalled = getIntent();
        voice2text = intentThatCalled.getStringExtra("v2txt");
        init();
        my_doubloons.setOnClickListener(this);
        sponsored_text.setOnClickListener(this);
        adverties_us.setOnClickListener(this);
        featured_text.setOnClickListener(this);
        progress_text.setOnClickListener(this);
        notification_text.setOnClickListener(this);
        packages_text.setOnClickListener(this);
        about_us.setOnClickListener(this);
        faq.setOnClickListener(this);
        profile_text.setOnClickListener(this);
        logout.setOnClickListener(this);
        home.setOnClickListener(this);
        show_hide_data.setOnClickListener(this);
        notification_icon.setOnClickListener(this);
        search_icon.setOnClickListener(this);
        toolbar_list.setOnClickListener(this);
        if (flag) {
            exclusive_items.setVisibility(View.GONE);
        }
    }
    public void onFocusedBuildingInfo() {
        IndoorBuilding building = mMap.getFocusedBuilding();
        if (building != null) {
            String s = "";
            for (IndoorLevel level : (List<IndoorLevel>) building.getLevels()) {
                s = s + level.getName() + " ";
            }
            if (building.isUnderground()) {
                s += "is underground";
            }
            setText(s);
        } else {
            setText("No visible building");
        }
    }


    public void onVisibleLevelInfo() {
        IndoorBuilding building = mMap.getFocusedBuilding();
        if (building != null) {
            IndoorLevel level = (IndoorLevel) building.getLevels().get(building.getActiveLevelIndex());
            if (level != null) {
                setText(level.getName());
            } else {
                setText("No visible level");
            }
        } else {
            setText("No visible building");
        }
    }


    public void onHigherLevel() {
        IndoorBuilding building = mMap.getFocusedBuilding();
        if (building != null) {
            List<IndoorLevel> levels = building.getLevels();
            if (!levels.isEmpty()) {
                int currentLevel = building.getActiveLevelIndex();
                int newLevel = currentLevel - 1;
                if (newLevel == -1) {
                    newLevel = levels.size() - 1;
                }
                IndoorLevel level = levels.get(newLevel);
                setText("Activiating level " + level.getName());
                level.activate();
            } else {
                setText("No levels in building");
            }
        } else {
            setText("No visible building");
        }
    }

    private void setText(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        show = true;
        toolbar_list.setText("List");
        mapFragment.getMapAsync(this);
    }

    public void init() {
        packages_layout = (LinearLayout) findViewById(R.id.packages_layout);
        add_with_us = (LinearLayout) findViewById(R.id.add_with_us);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar_list = (TextView) findViewById(R.id.toolbar_list);
        select_text = (TextView) findViewById(R.id.select_text);
        my_doubloons = (TextView) findViewById(R.id.my_doubloons);
        sponser = (TextView) findViewById(R.id.sponser);
        search_icon = (ImageView) findViewById(R.id.search_icon);
        txt_search_name = (EditText) findViewById(R.id.txt_search_name);
        sponsored_text = (TextView) findViewById(R.id.sponsored_text);
        adverties_us = (TextView) findViewById(R.id.adverties_us);
        featured_text = (TextView) findViewById(R.id.featured_text);
        progress_text = (TextView) findViewById(R.id.progress_text);
        notification_text = (TextView) findViewById(R.id.notification_text);
        packages_text = (TextView) findViewById(R.id.packages_text);
        about_us = (TextView) findViewById(R.id.about_us);
        faq = (TextView) findViewById(R.id.faq);
        profile_text = (TextView) findViewById(R.id.profile_text);
        logout = (TextView) findViewById(R.id.logout);
        exclusive_items = (LinearLayout) findViewById(R.id.exclusive_items);
        show_hide_data = (LinearLayout) findViewById(R.id.show_hide_data);
        home = (LinearLayout) findViewById(R.id.home);
        notification_icon = (LinearLayout) findViewById(R.id.notification_icon);
        show_hide = (ImageView) findViewById(R.id.show_hide);
        notification_count = (TextView) findViewById(R.id.notification_count);
        distance_spinner = (Spinner) findViewById(R.id.distance_spinner);
        ArrayAdapter<CharSequence> stepadapter = ArrayAdapter.createFromResource(this,
                R.array.distance, R.layout.spinner_item);
        distance_spinner.setAdapter(stepadapter);
        distance_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                select_text.setText(distance_spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        if (readPref.getusertype().equals("player")) {
            add_with_us.setVisibility(View.GONE);
            packages_layout.setVisibility(View.GONE);
        } else {
            add_with_us.setVisibility(View.VISIBLE);
            packages_layout.setVisibility(View.VISIBLE);
        }
        GetResponse();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", readPref.getuserId());
        params.put("type", readPref.getusertype());
        groceryAPICALL = new DataAPICALL(this, groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/getNotificationCount", params, "Count");
    }

    public void popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.app_name);
        builder.setCancelable(false);
        builder.setMessage("Are you Sure you want to close this app?");
        builder.setIcon(R.drawable.logout);
        builder.setPositiveButton("Yes", null);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePref.saveLoggedIn(MapsActivity.this, "No");
                        savePref.savepartnerLoggedIn(MapsActivity.this, "No");
                        finish();
                    }
                });
        builder.setNegativeButton("No", null);
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.home:
                drawer.closeDrawer(GravityCompat.START);
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.toolbar_list:
                if (show) {
                    show = false;
                    toolbar_list.setText("Map");
                    ListFragment mydoubloon = new ListFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.map, mydoubloon, mydoubloon.getClass().getSimpleName()).addToBackStack(null).commit();
                } else {
                    show = true;
                    toolbar_list.setText("List");
                    FragmentManager fmg = getSupportFragmentManager();
                    fmg.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                break;
            case R.id.my_doubloons:
                drawer.closeDrawer(GravityCompat.START);
                show = false;
                toolbar_list.setText("Map");
                Mydoubloons mydoubloons = new Mydoubloons();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, mydoubloons, mydoubloons.getClass().getSimpleName()).addToBackStack(null).commit();
                break;
            case R.id.sponser:
                drawer.closeDrawer(GravityCompat.START);
                show = false;
                toolbar_list.setText("Map");
                Sponsored_doubloon create_sponsored_doubloon = new Sponsored_doubloon();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, create_sponsored_doubloon, create_sponsored_doubloon.getClass().getSimpleName()).addToBackStack(null).commit();
                break;
            case R.id.sponsored_text:
                drawer.closeDrawer(GravityCompat.START);
                show = false;
                toolbar_list.setText("Map");
                Sponsored_doubloon sponsored_doubloon = new Sponsored_doubloon();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, sponsored_doubloon, sponsored_doubloon.getClass().getSimpleName()).addToBackStack(null).commit();
                break;
            case R.id.adverties_us:
                drawer.closeDrawer(GravityCompat.START);
                show = false;
                toolbar_list.setText("Map");
                Advertise_eith_us adverties_us = new Advertise_eith_us();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, adverties_us, adverties_us.getClass().getSimpleName()).addToBackStack(null).commit();
                break;
            case R.id.featured_text:
                drawer.closeDrawer(GravityCompat.START);
                show = false;
                toolbar_list.setText("Map");
                Featured_doubloonFragment featured_doubloonFragment = new Featured_doubloonFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, featured_doubloonFragment, featured_doubloonFragment.getClass().getSimpleName()).addToBackStack(null).commit();
                break;
            case R.id.progress_text:
                drawer.closeDrawer(GravityCompat.START);
                show = false;
                toolbar_list.setText("Map");
                Progressive_doubloons progressive_doubloons = new Progressive_doubloons();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, progressive_doubloons, progressive_doubloons.getClass().getSimpleName()).addToBackStack(null).commit();
                break;
            case R.id.notification_text:
                drawer.closeDrawer(GravityCompat.START);
                show = false;
                toolbar_list.setText("Map");
                Notification_fragment notification_fragment = new Notification_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, notification_fragment, notification_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                break;
            case R.id.notification_icon:
                drawer.closeDrawer(GravityCompat.START);
                Notification_fragment notification_fragmente = new Notification_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, notification_fragmente, notification_fragmente.getClass().getSimpleName()).addToBackStack(null).commit();
                break;
            case R.id.packages_text:
                drawer.closeDrawer(GravityCompat.START);
                show = false;
                toolbar_list.setText("Map");
                BuyPackage_fragment buyPackageFragment = new BuyPackage_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, buyPackageFragment, buyPackageFragment.getClass().getSimpleName()).addToBackStack(null).commit();
                break;
            case R.id.about_us:
                drawer.closeDrawer(GravityCompat.START);
                Aboutus_Fragment aboutus_fragment = new Aboutus_Fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, aboutus_fragment, aboutus_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                break;
            case R.id.faq:
                drawer.closeDrawer(GravityCompat.START);
                show = false;
                toolbar_list.setText("Map");
                FaqFragment faqFragment = new FaqFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, faqFragment, faqFragment.getClass().getSimpleName()).addToBackStack(null).commit();
                break;
            case R.id.profile_text:
                drawer.closeDrawer(GravityCompat.START);
                GetResponse();
                HashMap<String, String> params = new HashMap<>();

                groceryAPICALL = new DataAPICALL(this, groceryAppResponse);
                String url = "";
                if (readPref.getusertype().equals("patrner")) {
                    url = "http://www.doubloon.media-mosaic.in/apis/partnerDetail";
                    params.put("partner_id", readPref.getuserId());
                } else {
                    url = "http://www.doubloon.media-mosaic.in/apis/userDetail";
                    params.put("user_id", readPref.getuserId());
                }
                groceryAPICALL.sendData(Request.Method.POST, url, params, "Profilr_edit");
             /*   Profilr_edit profilr_edit1 = new Profilr_edit();
                getSupportFragmentManager().beginTransaction().replace(R.id.map, profilr_edit1, profilr_edit1.getClass().getSimpleName()).addToBackStack(null).commit();*/
                break;
            case R.id.logout:
                drawer.closeDrawer(GravityCompat.START);
                popup();
                break;
            case R.id.search_icon:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Loading...");
                pDialog.show();
                GetResponse();
                HashMap<String, String> paramssearch = new HashMap<>();
                paramssearch.put("lat", String.valueOf(latitude));
                paramssearch.put("lng", String.valueOf(longitude));
                paramssearch.put("keyword", txt_search_name.getText().toString().trim());
                paramssearch.put("distance", distance_spinner.getSelectedItem().toString().replace("km", ""));
                Log.d("TAG@123", paramssearch.toString());
                groceryAPICALL = new DataAPICALL(this, groceryAppResponse);
                groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/searchDoubloons", paramssearch, "Search_data");
                break;
            case R.id.show_hide_data:
                if (flag) {
                    flag = false;
                    show_hide.setImageResource(R.drawable.drop_up);
                    exclusive_items.setVisibility(View.VISIBLE);
                } else {
                    flag = true;
                    show_hide.setImageResource(R.drawable.drop_down);
                    exclusive_items.setVisibility(View.GONE);
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {

        show = true;
        toolbar_list.setText("List");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        checkLocationandAddToMap();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    checkLocationandAddToMap();
                } else
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void checkLocationandAddToMap() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng sydney = new LatLng(latitude, longitude);
            mMap.clear();
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.green_nav);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Daily Doubloons").snippet("").icon(icon));
            CameraPosition position = CameraPosition.builder()
                    .target(new LatLng(location.getLatitude(),
                            location.getLongitude()))
                    .zoom(10f)
                    .bearing(0)
                    .tilt(0)
                    .build();
            mMap.setMinZoomPreference(2.0f);
            mMap.setMaxZoomPreference(14.0f);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(position), null);
            mMap.setTrafficEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setBuildingsEnabled(true);
            mMap.setMapType(MAP_TYPE_NORMAL);
            mMap.setIndoorEnabled(true);
           // drawCircle(sydney);
            mMap.getUiSettings().setMapToolbarEnabled(false);
           // mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
            GetResponse();
            HashMap<String, String> paramssearch = new HashMap<>();
            paramssearch.put("lat", String.valueOf(latitude));
            paramssearch.put("lng", String.valueOf(longitude));
            Log.d("TAG@123", paramssearch.toString());
            groceryAPICALL = new DataAPICALL(this, groceryAppResponse);
            groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/searchDoubloons", paramssearch, "First_open");
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    show = false;
                    toolbar_list.setText("Map");
                    ListFragment mydoubloon = new ListFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.map, mydoubloon, mydoubloon.getClass().getSimpleName()).addToBackStack(null).commit();
                    return false;
                }
            });

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    show = false;
                    toolbar_list.setText("Map");
                    ListFragment mydoubloon = new ListFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.map, mydoubloon, mydoubloon.getClass().getSimpleName()).addToBackStack(null).commit();
                }
            });




          /*  mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {


                }
            });
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(final Marker marker) {

                    View view = getLayoutInflater().inflate(R.layout.map_custom_infowindow, null);
                    TextView name_tv = view.findViewById(R.id.name);
                    TextView details_tv = view.findViewById(R.id.details);
                    //name_tv.setText(marker.getTitle());
                    // details_tv.setText(marker.getSnippet());
                    // name_tv.setText(R.string.app_name);
                    //details_tv.setText(R.string.app_name);

                    return view;
                   *//* LinearLayout info = new LinearLayout(MapsActivity.this);
                    info.setOrientation(LinearLayout.VERTICAL);
                    final TextView title = new TextView(MapsActivity.this);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());
                    TextView snippet = new TextView(MapsActivity.this);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());
                    Button button=new Button(MapsActivity.this);
                    button.setTextColor(Color.GRAY);
                    button.setBackgroundColor(Color.YELLOW);

                    info.addView(title);
                    info.addView(snippet);
                    info.addView(button);
                    info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    return info;*//*
                }
            });*/

        } else {

            getLocation();

        }

    }
    private void drawCircle(LatLng point){




        Circle circle = mMap.addCircle(new CircleOptions()
                .center(point)
                .radius(100)
                .strokeColor(Color.MAGENTA)
                .fillColor(Color.TRANSPARENT));


    }

    public void onToggleLevelPicker(View view) {
        showLevelPicker = !showLevelPicker;
        mMap.getUiSettings().setIndoorLevelPickerEnabled(showLevelPicker);
    }

    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

    protected void getLocation() {
        if (isLocationEnabled(MapsActivity.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                return;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LatLng sydney = new LatLng(latitude, longitude);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.green_nav);
                //mMap.addMarker(new MarkerOptions().position(sydney).title("Daily Doubloons").snippet("").icon(icon)).showInfoWindow();
                mMap.addMarker(new MarkerOptions().position(sydney).title("Daily Doubloons").snippet("").icon(icon));
                CameraPosition position = CameraPosition.builder()
                        .target(new LatLng(location.getLatitude(),
                                location.getLongitude()))
                        .zoom(10f)
                        .bearing(0)
                        .tilt(0)
                        .build();
                mMap.setMinZoomPreference(2.0f);
                mMap.setMaxZoomPreference(14.0f);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.setBuildingsEnabled(true);
                mMap.setMapType(MAP_TYPE_NORMAL);
                mMap.setIndoorEnabled(true);
               // drawCircle(sydney);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                GetResponse();
                HashMap<String, String> paramssearch = new HashMap<>();
                paramssearch.put("lat", String.valueOf(latitude));
                paramssearch.put("lng", String.valueOf(longitude));
                Log.d("TAG@123", paramssearch.toString());
                groceryAPICALL = new DataAPICALL(this, groceryAppResponse);
                groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/searchDoubloons", paramssearch, "First_open");
              mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                  @Override
                  public boolean onMarkerClick(Marker marker) {
                      show = false;
                      toolbar_list.setText("Map");
                      ListFragment mydoubloon = new ListFragment();
                      getSupportFragmentManager().beginTransaction().replace(R.id.map, mydoubloon, mydoubloon.getClass().getSimpleName()).addToBackStack(null).commit();
                      return false;
                  }
              });
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        show = false;
                        toolbar_list.setText("Map");
                        ListFragment mydoubloon = new ListFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.map, mydoubloon, mydoubloon.getClass().getSimpleName()).addToBackStack(null).commit();
                    }
                });


               /* mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {


                    }
                });
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(final Marker marker) {

                        View view = getLayoutInflater().inflate(R.layout.map_custom_infowindow, null);
                        TextView name_tv = view.findViewById(R.id.name);
                        TextView details_tv = view.findViewById(R.id.details);
                        //name_tv.setText(marker.getTitle());
                        // details_tv.setText(marker.getSnippet());
                        // name_tv.setText(R.string.app_name);
                        //details_tv.setText(R.string.app_name);

                        return view;
                   *//* LinearLayout info = new LinearLayout(MapsActivity.this);
                    info.setOrientation(LinearLayout.VERTICAL);
                    final TextView title = new TextView(MapsActivity.this);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());
                    TextView snippet = new TextView(MapsActivity.this);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());
                    Button button=new Button(MapsActivity.this);
                    button.setTextColor(Color.GRAY);
                    button.setBackgroundColor(Color.YELLOW);

                    info.addView(title);
                    info.addView(snippet);
                    info.addView(button);
                    info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    return info;*//*
                    }
                });*/

            } else {
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        } else {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        // Toast.makeText(MapsActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
        searchNearestPlace(voice2text);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void searchNearestPlace(String v2txt) {
        //.....
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (Exception e) {

        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void GetResponse() {
        groceryAppResponse = new DataAppResponse() {
            @Override
            public void onResponse(String response, String responseType) {
                String s = response.toString();
                Log.d("TAG@123", "Item Response:" + response);
                if (responseType.equals("Profilr_edit")) {
                    show = false;
                    toolbar_list.setText("Map");
                    Profilr_edit profilr_edit1 = new Profilr_edit();
                    Bundle bundle = new Bundle();
                    bundle.putString("Update_info", response.toString());
                    profilr_edit1.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.map, profilr_edit1, profilr_edit1.getClass().getSimpleName()).addToBackStack(null).commit();
                } else if (responseType.equals("Search_data")) {
                    pDialog.dismiss();
                    mMap.clear();
                    try {
                        JSONObject jsonObjectdata = new JSONObject(response);
                        //settoast(jsonObjectdata.getString("res_msg"));
                        JSONArray jsonArray = jsonObjectdata.getJSONArray("Doubloon_app");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (!jsonObject.isNull("lat")) {
                                createMarker(Double.parseDouble(jsonObject.getString("lat")), Double.parseDouble(jsonObject.getString("long")), jsonObject.getString("name"), jsonObject.getString("name"), jsonObject.getString("time_limit"));
                            }
                        }
                    } catch (JSONException h) {
                    }


                } else if (responseType.equals("First_open")) {

                    try {
                        JSONObject jsonObjectdata = new JSONObject(response);
                        //settoast(jsonObjectdata.getString("res_msg"));
                        JSONArray jsonArray = jsonObjectdata.getJSONArray("Doubloon_app");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (!jsonObject.isNull("lat")) {

                                createMarker(Double.parseDouble(jsonObject.getString("lat")), Double.parseDouble(jsonObject.getString("long")), jsonObject.getString("name"), jsonObject.getString("name"), jsonObject.getString("time_limit"));

                            }
                        }


                    } catch (JSONException h) {
                    }


                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("res_msg").equals("Record Found Successfully")) {
                            notification_count.setText(jsonObject.getString("count"));
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(String error, String responseType) {


            }
        };


    }

    protected Marker createMarker(double latitude, double longitude, String title, String snippet, String expir_date) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.green_nav);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       // drawCircle(new LatLng(latitude, longitude));
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(DateToStr);
            d2 = format.parse(expir_date);
            long diff = d2.getTime() - d1.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (diffDays > 0.0) {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.green_nav);
            } else {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.red_nav);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet).icon(icon));
    }

    public void settoast(String title) {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text_toast);
        text.setText(title);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setView(layout);
        toast.show();
    }


}
