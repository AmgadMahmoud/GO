package net.gogetout.go;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.software.shell.fab.ActionButton;

import net.gogetout.go.library.DatabaseHandler;
import net.gogetout.go.library.EventFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class CreateEventActivity extends ActionBarActivity implements OnMapReadyCallback {

    private Toolbar toolbar;
    ActionButton actionButton;

    private Dialog dialogMap;
    private MapFragment mapFragment;
    private GoogleMap map;
    public static android.app.FragmentManager fragmentManager;
    private Marker marker;

    EditText eTitle;
    EditText eLocation;
    EditText eDate;
    EditText eTime;
    EditText eDescription;
    Button ePost;

    private EditText edtDate;
    private EditText edtTime;
    final Calendar c = Calendar.getInstance();

    private static String ADD_KEY_SUCCESS = "success";
    private static String ADD_KEY_ERROR = "error";
    private static String ADD_KEY_UEID = "eid";

    String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        actionButton = (ActionButton) findViewById(R.id.action_button);
        /*fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.tiny_map);
        mapFragment.getMapAsync(CreateEventActivity.this);

        map = mapFragment.getMap();

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                if (marker != null) marker.remove();
                marker = map.addMarker(new MarkerOptions().position(point));
            }
        });


        LatLng latLng = new LatLng(30, 30);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, *//*17*//*5);
        map.animateCamera(cameraUpdate);*/

        initializeView();



    }

    private class NetCheckAddEvent extends AsyncTask<String, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(CreateEventActivity.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {

            /**
             * Gets current device state and checks for working internet
             * connection by trying Google.
             **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url
                            .openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean th) {

            if (th == true) {
                nDialog.dismiss();
                new ProcessAddEvent().execute();
            } else {
                nDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Error in Network Connection", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private class ProcessAddEvent extends AsyncTask<String, String, JSONObject> {

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog; // C_5 start.

        String email, title, location, date, time, description; // remove fn,ln

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            eTitle = (EditText) findViewById(R.id.ev_title);
            eLocation = (EditText) findViewById(R.id.ev_location);
            eDate = (EditText) findViewById(R.id.ev_date);
            eTime = (EditText) findViewById(R.id.ev_time);
            eDescription = (EditText) findViewById(R.id.ev_description);
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            email = db.getUserEmail();
            title = eTitle.getText().toString();
            location = eLocation.getText().toString();
            date = eDate.getText().toString();
            time = eTime.getText().toString();
            description = eDescription.getText().toString();
            pDialog = new ProgressDialog(CreateEventActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Uploading Event ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            EventFunctions eventFunction = new EventFunctions();
            JSONObject json = eventFunction.addEvent(email, title, location,
                    date, time, description);

            return json;

        } // C_5 end.

        // onPostExecute
        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString(ADD_KEY_SUCCESS) != null) {
                    // registerErrorMsg.setText("");
                    String addsuccess = json.getString(ADD_KEY_SUCCESS);

                    String addfail = json.getString(ADD_KEY_ERROR);

                    if (Integer.parseInt(addsuccess) == 1) {

                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");

                        JSONObject json_event = json.getJSONObject("event");
                        new ImageUpload(filePath,
                                json_event.getString(ADD_KEY_UEID)).execute();
                        Intent postUpload = new Intent(getApplicationContext(),
                                EventActivity.class);

                        postUpload.putExtra("eventz", json_event.toString());
                        // postUpload.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(postUpload);

                        finish();
                    }

					/*
					 * fix error response number
					 */else if (Integer.parseInt(addfail) == 1) {
                        pDialog.dismiss();
						/*
						 * toast this.
						 */
                        Toast.makeText(getApplicationContext(),
                                "Couldn't Add Event", Toast.LENGTH_SHORT)
                                .show();
                    }

                }

                else {
                    pDialog.dismiss();

                    Toast.makeText(getApplicationContext(),
                            "Error occured while adding event",
                            Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    public void NetAsyncAddEvent(View view) {
        new NetCheckAddEvent().execute();
    }

    private void initializeView() {
        edtDate = (EditText) findViewById(R.id.ev_date);
        edtTime = (EditText) findViewById(R.id.ev_time);
        // setCurrentDateOnView();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setCurrentDateOnView();
        }
    };

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            setCurrentTimeOnView();
        }
    };

    public void dateOnClick(View view) {
        new DatePickerDialog(this, R.style.DialogTheme, date,c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void timeOnClick(View view) {
        new TimePickerDialog(this, R.style.DialogTheme, time, c.get(Calendar.HOUR),
                c.get(Calendar.MINUTE), false).show();
    }

    public void setCurrentDateOnView() {
        // String dateFormat = "dd-MMM-yyyy";
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        edtDate.setText(sdf.format(c.getTime()));
    }

    public void setCurrentTimeOnView() {
        String timeFormat = "hh:mm a";
        SimpleDateFormat stf = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
        edtTime.setText(stf.format(c.getTime()));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public void popMap(View view) {

        dialogMap = new Dialog(this,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        dialogMap.setContentView(R.layout.map_dialog);
        fragmentManager = getFragmentManager();


        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_dialog);
        mapFragment.getMapAsync(this);

        map = mapFragment.getMap();
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                if (marker != null) marker.remove();
                marker = map.addMarker(new MarkerOptions().position(point));
            }
        });
        //events lat lng
        LatLng latLng = new LatLng(30, 30);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, /*17*/5);
        map.animateCamera(cameraUpdate);

        dialogMap.show();
    }

    public void popCategoryBox(View view) {

        final Dialog dialogCategoryBox = new Dialog(this/*,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen*/);
        dialogCategoryBox.setContentView(R.layout.interests_box);
        dialogCategoryBox.setCanceledOnTouchOutside(true);
        dialogCategoryBox.setTitle("Choose Event Category:");

            /*Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });*/

        dialogCategoryBox.show();

    }


    public void imageOnClick(View view) {

        File mPath = new File(Environment.getExternalStorageDirectory()
                + "//DIR//");
        FileDialog fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".jpg");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                filePath = file.toString();
                Log.d(getClass().getName(), "selected file " + file.toString());
                Toast.makeText(getApplicationContext(),
                        "File Chosen: " + file.toString(), Toast.LENGTH_LONG)
                        .show();

                // ImageUpload imageup = new ImageUpload(file.toString());
                // imageup.uploadFile();
                // new ImageUpload(file.toString()).execute();
                Bitmap myBitmap = BitmapFactory.decodeFile(file
                        .getAbsolutePath());

                ImageView myImage = (ImageView) findViewById(R.id.ev_image);

                myImage.setImageBitmap(myBitmap);
            }
        });

        fileDialog.showDialog();

    }

}
