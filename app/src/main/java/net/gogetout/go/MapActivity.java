package net.gogetout.go;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.software.shell.fab.ActionButton;

import net.gogetout.go.library.EventFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends Activity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    ActionButton actionButton;

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private MapFragment mapFragment;
    private GoogleMap map;
    private static String LIST_KEY_SUCCESS = "success";
    private static String LIST_KEY_TITLE = "title";
    private static String LIST_KEY_EID = "eid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        actionButton = (ActionButton) findViewById(R.id.map_action_button);
        actionButton.bringToFront();
        actionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng latLng = new LatLng(30, 30);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, /*17*/5);
                        map.animateCamera(cameraUpdate);

                    }
                }

        );
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        map = mapFragment.getMap();
        actionButton.bringToFront();
        actionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng latLng = new LatLng(30, 30);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, /*17*/5);
                        map.animateCamera(cameraUpdate);

                    }
                }

        );
        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        //displayLocation();


    }

    private void navigateToLocation() {


        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            //lblLocation.setText(latitude + ", " + longitude);
            Toast.makeText(this, latitude + ", " + longitude, Toast.LENGTH_LONG).show();
            LatLng latLng = new LatLng(latitude, longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, /*17*/5);
            map.animateCamera(cameraUpdate);
            new ProcessNearbyEvents().execute(Double.toString(latitude), Double.toString(longitude));
        } else {

            Toast.makeText(this, "(Couldn't get the location. Make sure location is enabled on the device)", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {


        //map.setMyLocationEnabled(true);

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        //for(int i = 0 ; i<10;i++){
        /*LatLng thirty = new LatLng(30.867, 30.206);
            thirty = new LatLng(30.0*//*+Float.parseFloat(String.valueOf(i))/1000*//*, 30.0*//*+Float.parseFloat(String.valueOf(i))/1000*//*);
            map.addMarker(new MarkerOptions()
                    .title("Marker"*//* + i*//*)
                    .snippet("Snippet MOFO!")
                    .position(thirty))*//*.draggable(true)*//*;*/
        /*map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                //Toast.makeText(MapActivity.this,"onMarkerDragStart",Toast.LENGTH_LONG).show();
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);

                map.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                //Toast.makeText(MapActivity.this,"onMarkerDragEnd",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
                //Toast.makeText(MapActivity.this,"onMarkerDrag",Toast.LENGTH_LONG).show();
            }
        });*/
        //}

        /*MarkerOptions marker= new MarkerOptions()
                .title("Dropped").snippet("drag to position").position(thirty).draggable(true);
                //GoogleMap.OnMarkerDragListener

        Marker m = map.addMarker( marker );
        String eidKey = m.getId();*/

        /*map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(41.889, -87.622), 16));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.xmo))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(41.889, -87.622)));*/


        //Toast.makeText(this,GP,Toast.LENGTH_LONG).show();
        //displayLocation();
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        //displayLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
        //displayLocation();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        navigateToLocation();

        /*Location location = mLocationClient.getLastLocation();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        map.animateCamera(cameraUpdate);*/
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    private class ProcessNearbyEvents extends
            AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            /*String lat = "30";
            String lng = "30";*/
            String lat = args[0];
            String lng = args[1];

            EventFunctions eventFunction = new EventFunctions();
            JSONObject json = eventFunction.grabNearbyEvents(lat, lng);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            String suc;
            try {
                suc = json.getString(LIST_KEY_SUCCESS);
                if (suc != null) {
                    if (Integer.parseInt(suc) == 1) {

                        JSONArray jarray = json.getJSONArray("events");
                        LatLng latlng;
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject object = jarray.getJSONObject(i);

                            latlng = new LatLng(Float.parseFloat(object.getString("lat")), Float.parseFloat(object.getString("lng")));

                            map.addMarker(new MarkerOptions()
                                    .title(object.getString("title"))
                                    .snippet(object.getString("distance") + " Km")
                                    .position(latlng)/*.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))*/);
                        }

                    } else {
                        // print error msg
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}