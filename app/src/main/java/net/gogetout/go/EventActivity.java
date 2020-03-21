package net.gogetout.go;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import net.gogetout.go.library.DatabaseHandler;
import net.gogetout.go.library.EventFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Edwin on 15/02/2015.
 */
public class EventActivity extends ActionBarActivity implements OnMapReadyCallback {

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Details","Stickers","Guests"};
    int Numboftabs =3;


    private Dialog dialogMap;
    private MapFragment mapFragment;
    private GoogleMap map;
    public static android.app.FragmentManager fragmentManager;

    // private static String ADD_KEY_EMAIL = "email";
    private static String ADD_KEY_EID = "eid";
    private static String ADD_KEY_TITLE = "title";
    private static String ADD_KEY_LOCATION = "location";
    private static String ADD_KEY_DATE = "date";
    private static String ADD_KEY_TIME = "time";
    private static String ADD_KEY_DESCRIPTION = "description";
    private static String ADD_KEY_USER = "user";
    private static String ADD_UID = "uid";

    // private static String ADD_KEY_IMGURL = "imageURL";
    // private static String ADD_KEY_CREATED_AT = "created_at";

    String uid, eid;
    String guid;

    TextView titleView;
    ImageView iv;
    Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.app_bar_trans);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        titleView = (TextView) findViewById(R.id.ev_title);
        iv = (ImageView) findViewById(R.id.header_image);

        try {
            JSONObject json_event = new JSONObject(getIntent().getStringExtra(
                    "eventz"));

            String title = json_event.getString(ADD_KEY_TITLE);
            String date = json_event.getString(ADD_KEY_DATE);
            String time = json_event.getString(ADD_KEY_TIME);
            String location = json_event.getString(ADD_KEY_LOCATION);
            String description = json_event.getString(ADD_KEY_DESCRIPTION);
            String user = json_event.getString(ADD_KEY_USER);
            eid = json_event.getString(ADD_KEY_EID);
            uid = json_event.getString(ADD_UID);

            titleView.setText(title);
            /**dateView.setText(date);
            timeView.setText(time);
            locationView.setText(location);
            descriptionView.setText(description);
            uploaderView.setText(user);**/
            Toast.makeText(getApplicationContext(),
                    date, Toast.LENGTH_LONG)
                    .show();
            Toast.makeText(getApplicationContext(),
                    time, Toast.LENGTH_LONG)
                    .show();
            Toast.makeText(getApplicationContext(),
                    location, Toast.LENGTH_LONG)
                    .show();
            Toast.makeText(getApplicationContext(),
                    description, Toast.LENGTH_LONG)
                    .show();
            Toast.makeText(getApplicationContext(),
                    user, Toast.LENGTH_LONG)
                    .show();

            Picasso.with(getApplicationContext())
                    .load(new GetImageURL().execute(eid).get().toString())
                    .resize(100, 100).into(iv);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*goButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new going().execute(eid, uid);
            }
        });*/

        // tmp post upload start

        new uploadPostTask().execute();
        new uploadQuestionTask().execute();
        new uploadAnswerTask().execute();

        // tmp post upload end

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void popMap(View view) {

        dialogMap = new Dialog(this,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        dialogMap.setContentView(R.layout.map_dialog);
        fragmentManager = getFragmentManager();


        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_dialog);
        mapFragment.getMapAsync(EventActivity.this);

        map = mapFragment.getMap();

        //events lat lng
        LatLng latLng = new LatLng(30, 30);
        map.addMarker(new MarkerOptions().position(latLng));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, /*17*/5);
        map.animateCamera(cameraUpdate);

        dialogMap.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public void userOnClick(View view) {
        // send uid

        Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
        profile.putExtra("uid", uid);
        startActivity(profile);
    }

    private class going extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            guid = db.getUID();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            EventFunctions eventFunction = new EventFunctions();
            JSONObject json = eventFunction.going(eid, guid);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString("success") != null) {
                    goButton.setText("tab eshta");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class uploadPostTask extends AsyncTask<String, String, JSONObject> {
        String tmpeid, tmpuid, tmppost;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
			/* read data from view */

            tmpeid = "18";
            tmpuid = "2";
            tmppost = "post office, this event sucks!!";

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            EventFunctions eventFunction = new EventFunctions();
            JSONObject json = eventFunction.uploadPost(tmpuid, tmpeid, tmppost);

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString("success") != null) {
                    String addsuccess = json.getString("success");
                    if (Integer.parseInt(addsuccess) == 1) {
                        Toast.makeText(getApplicationContext(),
                                "mbrook postak etrafa3", Toast.LENGTH_LONG)
                                .show();
                    } else {

                        Toast.makeText(getApplicationContext(),
                                "7sal moshkla wna brfa3.. el post",
                                Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    private class uploadQuestionTask extends
            AsyncTask<String, String, JSONObject> {
        String tmpeid, tmpuid, tmpquestion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
			/* read data from view */

            tmpeid = "18";
            tmpuid = "2";
            tmpquestion = "post office, this event sucks!!";

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            EventFunctions eventFunction = new EventFunctions();
            JSONObject json = eventFunction.uploadQuestion(tmpuid, tmpeid,
                    tmpquestion);

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString("success") != null) {
                    String addsuccess = json.getString("success");
                    if (Integer.parseInt(addsuccess) == 1) {
                        Toast.makeText(getApplicationContext(),
                                "mbrook questionak etrafa3", Toast.LENGTH_LONG)
                                .show();
                    } else {

                        Toast.makeText(getApplicationContext(),
                                "7sal moshkla wna brfa3.. el question",
                                Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    private class uploadAnswerTask extends
            AsyncTask<String, String, JSONObject> {
        String tmpqid, tmpuid, tmpanswer;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
			/* read data from view */

            tmpqid = "18";
            tmpuid = "2";
            tmpanswer = "post office, this event sucks!!";

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            EventFunctions eventFunction = new EventFunctions();
            JSONObject json = eventFunction.uploadAnswer(tmpuid, tmpqid,
                    tmpanswer);

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString("success") != null) {
                    String addsuccess = json.getString("success");
                    if (Integer.parseInt(addsuccess) == 1) {
                        Toast.makeText(getApplicationContext(),
                                "mbrook answerak etrafa3", Toast.LENGTH_LONG)
                                .show();
                    } else {

                        Toast.makeText(getApplicationContext(),
                                "7sal moshkla wna brfa3.. el answer",
                                Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

}