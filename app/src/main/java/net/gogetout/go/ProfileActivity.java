package net.gogetout.go;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;
import com.squareup.picasso.Picasso;

import net.gogetout.go.library.ProfileFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends ActionBarActivity {

    private RecyclerView recyclerView;
    private View containerView;
    private profAdapter adapter;
    private ActionButton actionButton;
    private Toolbar toolbar;

    private static String FETCH_KEY_SUCCESS = "success";
    private static String FETCH_KEY_USERNAME = "username";
    private static String FETCH_KEY_USERLOCATION = "location";
    private static String FETCH_KEY_ABOUT = "about";
    private static String FETCH_KEY_PHONE = "contact_phone";
    private static String FETCH_KEY_EMAIL = "contact_email";
    private static String FETCH_KEY_INTERESTS = "interests";
    private static String FETCH_KEY_BIRTHDATE = "personal_birth";
    private static String FETCH_KEY_EDUCATION = "personal_education";
    private static String FETCH_KEY_WORK = "personal_work";
    private static String FETCH_KEY_PICTUREURL = "pictureURL";

    ImageView profilePicture;
    TextView username;
    TextView userLocation;
    TextView about;
    TextView phone;
    TextView email;
    TextView interests;
    TextView birthDate;
    TextView education;
    TextView work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.app_bar_trans);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();

        recyclerView = (RecyclerView) findViewById(R.id.userInfo);
        adapter = new profAdapter(ProfileActivity.this, getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        actionButton = (ActionButton) findViewById(R.id.action_button);
        actionButton.bringToFront();

        /*profilePicture = (ImageView) findViewById(R.id.profilePicture);
        username = (TextView) findViewById(R.id.username);
        userLocation = (TextView) findViewById(R.id.userLocation);
        about = (TextView) findViewById(R.id.about);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        interests = (TextView) findViewById(R.id.intrests);
        birthDate = (TextView) findViewById(R.id.birthDate);*/
        String uid = getIntent().getStringExtra(
                "uid");
        new ProcessGetProfileData().execute(uid);

    }


    private class ProcessGetProfileData extends
            AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // notification:Loading Profile Data..
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            ProfileFunctions profileFunction = new ProfileFunctions();
            JSONObject json = profileFunction.fetchProfile(args[0]);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            String suc;
            try {
                suc = json.getString(FETCH_KEY_SUCCESS);
                if (suc != null) {
                    if (Integer.parseInt(suc) == 1) {

                        JSONObject profile = json.getJSONObject("profile");
                        username.setText(profile.getString(FETCH_KEY_USERNAME));
                        userLocation.setText(profile.getString(FETCH_KEY_USERLOCATION));
                        about.setText(profile.getString(FETCH_KEY_ABOUT));
                        phone.setText(profile.getString(FETCH_KEY_PHONE));
                        email.setText(profile.getString(FETCH_KEY_EMAIL));
                        interests.setText(profile.getString(FETCH_KEY_INTERESTS));
                        birthDate.setText(profile.getString(FETCH_KEY_BIRTHDATE));
                        education.setText(profile.getString(FETCH_KEY_EDUCATION));
                        work.setText(profile.getString(FETCH_KEY_WORK));

                        Picasso.with(getApplicationContext())
                                .load(profile.getString(FETCH_KEY_PICTUREURL))
                                .resize(100, 100).into(profilePicture);

/*				startEvID = json.getString("lastEvID");

				JSONArray jarray = json.getJSONArray("events");

				for (int i = 0; i < jarray.length(); i++) {
					JSONObject object = jarray.getJSONObject(i);

					Events event = new Events();

					event.setTitle(object.getString(LIST_KEY_TITLE));
					event.setLocation(object
							.getString(LIST_KEY_LOCATION));
					event.setDate(object.getString(LIST_KEY_DATE));
					event.setTime(object.getString(LIST_KEY_TIME));
					event.setUniqueID(object.getString(LIST_KEY_EID));

					String s = new GetImageURL()
							.execute(event.getUniqueID()).get()
							.toString();
					Picasso.with(getBaseContext()).load(s).into(target);
					event.setImageBM(tmpBM);

					eventsList.add(event);

				}*/



                    } else {
                        // print error msg:couldn't fetch profile
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_profile, menu);
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

    public static List<profElements> getData() {

        List<profElements> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_location_city_black_24dp, R.drawable.ic_email_black_24dp, R.drawable.ic_phone_black_24dp, R.drawable.ic_cake_black_24dp};
        String[] values = {"Alexandria, Egypt", "amgad.mah2@gmail.com", "01067205702", "May-28-93"};
        String[] infos = {"Location City", "E-mail", "Phone", "Birthday"};
        for (int i = 0; i < values.length && i < icons.length && i < infos.length; i++) {
            profElements current = new profElements();
            current.iconID = icons[i];
            current.value = values[i];
            current.info = infos[i];
            data.add(current);
        }
        return data;
    }

}
