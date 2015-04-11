package edu.wereawesome.calebkevin.pleasetakemehome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    // persistent data
    public static final String PREFS_NAME = "MyPrefsFile";

    Geocoder addressToLatLng;
    float lat;
    float lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Restore preferences
        SharedPreferences myHome = getSharedPreferences(PREFS_NAME, 0);
        setUpMapIfNeeded();
        askForCurrentLocation();
//
//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        String address = settings.getString("myHome", "");
//        Log.d("hi", address);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d("is it called?", "hello");
//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        float savedLat = settings.getFloat("lat",0);
//        float savedLong = settings.getFloat("lng",0);
//        if (savedLat != 0 && savedLong != 0) {
//            try {
//                mMap.addMarker(new MarkerOptions().position(new LatLng(savedLat, savedLong)));
//
//            } catch (Exception e){
//                Log.d("Error", "unable to convert address to long lat coords " + e.toString());
//            }
//        } else {
//            Log.d("Error", "address was empty");
//        }
//
//    }
    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    // ask for current location
    private void askForCurrentLocation() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Home Address");
        alert.setMessage("Please put in your home address");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                 String value = input.getText().toString();
                // Do something with value!
                // saving the stuff
                Log.d("value", value);
                SharedPreferences myHome = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = myHome.edit();
                editor.putFloat("lat", lat);
                editor.putFloat("lng", lng);
                editor.commit();


            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").snippet("Snippet"));

        //Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service Location_Service
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria,true);

        Location myLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // get latitude of the current location
        //lat = //(float) myLocation.getLatitude();

        // longitude
        //lng = (float) myLocation.getLongitude();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        lat = settings.getFloat("lat",0);
        lng = settings.getFloat("lng",0);

        //create a latLng object for the current location
        LatLng curLocation = new LatLng(lat, lng);

        // show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curLocation));

        // update the camera view
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(curLocation,12);

        // Zoom
        mMap.animateCamera(yourLocation);
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("You are here!").snippet("Consider yourself located"));

    }
}
