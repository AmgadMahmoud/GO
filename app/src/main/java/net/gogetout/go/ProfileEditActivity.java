package net.gogetout.go;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.software.shell.fab.ActionButton;

import net.gogetout.go.library.DatabaseHandler;
import net.gogetout.go.library.ProfileFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProfileEditActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ActionButton actionButtonEdit;
    private ActionButton actionButtonDone;

    ViewSwitcher switcher;

    private RecyclerView RV;
    private PeopleAdapter peopleAdapter;
    private List<PeopleElements> peopleList;
    private List<Events> eventsList;
    private EventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        toolbar = (Toolbar) findViewById(R.id.app_bar_trans);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();


        switcher = (ViewSwitcher) findViewById(R.id.switwit);
        actionButtonEdit = (ActionButton) findViewById(R.id.action_button_edit);
        actionButtonDone = (ActionButton) findViewById(R.id.action_button_done);
        actionButtonEdit.show();
        actionButtonEdit.bringToFront();

        actionButtonEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        actionButtonEdit.hide();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                actionButtonDone.show();
                                switcher.showNext();
                                actionButtonDone.bringToFront();
                            }
                        }, 600);

                        //actionButtonDone.show();
                    }
                }

        );


        actionButtonDone.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new UploadProfileInfo().execute();


                        actionButtonDone.hide();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                actionButtonEdit.show();
                                switcher.showNext();
                                actionButtonEdit.bringToFront();
                            }
                        }, 600);

                        //actionButtonEdit.show();
                    }
                }

        );
        for (int i = 0; i < Interests.length; i++) {
            Interests[i] = "";
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        //boolean checked = ((CheckBox) view).isChecked();

        LayoutInflater inflater = getLayoutInflater();

        boolean checked = ((CheckBox) inflater.inflate(R.layout.interests_box, (ViewGroup) findViewById(R.id.activity_profile_edit), false)).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.art:
                if (checked)
                    Interests[0] = "Art";
                else
                    Interests[0] = "";
                break;
            case R.id.daytime:
                if (checked)
                    Interests[1] = "Day Time";
                else
                    Interests[1] = "";
                break;
            case R.id.education_learning:
                if (checked)
                    Interests[2] = "Education and Learning";
                else
                    Interests[2] = "";
                break;
            case R.id.freebies:
                if (checked)
                    Interests[3] = "Freebies";
                else
                    Interests[3] = "";
                break;
            case R.id.hangouts:
                if (checked)
                    Interests[4] = "Hangouts";
                else
                    Interests[4] = "";
                break;
            case R.id.movies:
                if (checked)
                    Interests[5] = "Movies";
                else
                    Interests[5] = "";
                break;
            case R.id.music_concerts:
                if (checked)
                    Interests[6] = "Music and Concerts";
                else
                    Interests[6] = "";
                break;
            case R.id.nighttime:
                if (checked)
                    Interests[7] = "Night Time";
                else
                    Interests[7] = "";
                break;
            case R.id.sports:
                if (checked)
                    Interests[8] = "Sports";
                else
                    Interests[8] = "";
                break;
            case R.id.vacation_travel:
                if (checked)
                    Interests[9] = "Vacation and Travels";
                else
                    Interests[9] = "";
                break;

        }
    }

    private static final String KEY_SUCCESS = "success";
    EditText About, Phone, Email, BirthDate, City;
    private static String[] Interests = new String[10];
    private class UploadProfileInfo extends
            AsyncTask<String, String, JSONObject> {

        /**Defining Process dialog**/
		/** private ProgressDialog pDialog; // C_5 start.		 */
        String interestsList, uid, about, phone,email,birthDate, city;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            About = (EditText) findViewById(R.id.about1);
            Phone = (EditText) findViewById(R.id.edit_phone);
            Email = (EditText) findViewById(R.id.edit_email);
            BirthDate = (EditText) findViewById(R.id.edit_birth);
            City = (EditText) findViewById(R.id.edit_city);
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            uid = db.getUID();
            about = About.getText().toString();
            phone = Phone.getText().toString();
            email = Email.getText().toString();
            birthDate = BirthDate.getText().toString();
            city = City.getText().toString();

            interestsList = "";
            for (int i = 0; i < Interests.length; i++) {
                if (Interests[i] != "")
                    interestsList = interestsList + Interests[i] + "-";
            }

            // Toast.makeText(getApplicationContext(), interestsList,
            // Toast.LENGTH_LONG).show();

        }



        @Override
        protected JSONObject doInBackground(String... args) {

            ProfileFunctions profileFunction = new ProfileFunctions();
            JSONObject json = profileFunction.editProfile(uid, about, phone,
                    interestsList, birthDate, city,email, /* lat */
                    "30.01",
					/* lng */"32.21");

            return json;

        } // C_5 end.

        // onPostExecute
        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String addsuccess = json.getString(KEY_SUCCESS);
                    String returnedUid = json.getString("uid");
                    if (Integer.parseInt(addsuccess) == 1) {

                        /*Intent profile = new Intent(getApplicationContext(),
                                Profile.class);
                        profile.putExtra("uid", returnedUid);
                        startActivity(profile);*/
                    } else {

                        Toast.makeText(getApplicationContext(),
                                "Couldn't Edit Profile", Toast.LENGTH_SHORT)
                                .show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
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

    public void popInterestsBox(View view) {

        final Dialog dialogInterestsBox = new Dialog(ProfileEditActivity.this/*,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen*/);
        dialogInterestsBox.setContentView(R.layout.interests_box);
        dialogInterestsBox.setCanceledOnTouchOutside(true);
        dialogInterestsBox.setTitle("Pick Your Interests:");

            /*Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });*/

        dialogInterestsBox.show();

    }


    public void popFriendsBox(View view) {

        final Dialog dialogFriendsBox = new Dialog(ProfileEditActivity.this);


        peopleList = new ArrayList<>();
        peopleList = getPeopleData();

        LayoutInflater inflater = getLayoutInflater();

        RV = (RecyclerView) inflater.inflate(R.layout.friends_box, (ViewGroup) findViewById(R.id.activity_profile_edit), false);

        dialogFriendsBox.setContentView(RV/*R.layout.friends_box*/);
        dialogFriendsBox.setCanceledOnTouchOutside(true);
        dialogFriendsBox.setTitle("Community");
        RV.addItemDecoration(new SlidingTabStrip.SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        peopleAdapter = new PeopleAdapter(this, peopleList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV.setLayoutManager(layoutManager);
        RV.setAdapter(peopleAdapter);

        dialogFriendsBox.show();


    }


    public void popEventsBox(View view) {

        final Dialog dialogEventsBox = new Dialog(ProfileEditActivity.this);


        eventsList = new ArrayList<>();
        eventsList = getEventsData();

        LayoutInflater inflater = getLayoutInflater();

        RV = (RecyclerView) inflater.inflate(R.layout.events_box, (ViewGroup) findViewById(R.id.activity_profile_edit), false);

        dialogEventsBox.setContentView(RV/*R.layout.friends_box*/);
        dialogEventsBox.setCanceledOnTouchOutside(true);
        dialogEventsBox.setTitle("Community");
        RV.addItemDecoration(new SlidingTabStrip.SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        eventsAdapter = new EventsAdapter(this, eventsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV.setLayoutManager(layoutManager);
        RV.setAdapter(eventsAdapter);

        dialogEventsBox.show();


    }


    public static List<PeopleElements> getPeopleData() {

        List<PeopleElements> data = new ArrayList<>();
        String[] names = {"Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man"};
        String[] images = {"Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man"};
        for (int i = 0; i < names.length && i < images.length; i++) {
            PeopleElements current = new PeopleElements();
            current.name = names[i];
            current.imageURL = images[i];
            data.add(current);
        }
        return data;
    }

    public static List<Events> getEventsData() {

        List<Events> data = new ArrayList<>();
        String[] titles = {"Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man"};
        String[] images = {"Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man"};
        String[] dates = {"Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man"};
        String[] times = {"Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man"};
        String[] locations = {"Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man", "Girl", "Boy", "Guy", "Man"};
        for (int i = 0; i < titles.length && i < images.length && i < dates.length && i < times.length && i < locations.length; i++) {
            Events current = new Events();
            current.setTitle(titles[i]);
            current.setLocation(locations[i]);
            current.setDate(dates[i]);
            current.setTime(times[i]);
            current.setImageURL(images[i]);
            data.add(current);
        }
        return data;
    }


}
