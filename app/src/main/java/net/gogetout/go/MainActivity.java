package net.gogetout.go;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.gogetout.go.library.DatabaseHandler;
import net.gogetout.go.library.EventFunctions;
import net.gogetout.go.library.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements EventsAdapter.ClickListener {

    private static String LIST_KEY_SUCCESS = "success";
    private static String LIST_KEY_TITLE = "title";
    private static String LIST_KEY_LOCATION = "location";
    private static String LIST_KEY_DATE = "date";
    private static String LIST_KEY_TIME = "time";
    private static String LIST_KEY_EID = "eid";
    private static String LIST_KEY_IMAGEURL = "imageURL";

    private String searchQuery = "";

    private Toolbar toolbar;
    private RecyclerView RV;
    private EventsAdapter eventsAdapter;

    private String loadNumber = "0";

    private List<Events> eventsList;
    LinearLayoutManager mLayoutManager;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static String DISPLAY_KEY_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        eventsList = new ArrayList<>();

        RV = (RecyclerView) findViewById(R.id.eventsList);

        RV.addItemDecoration(new SlidingTabStrip.SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        eventsAdapter = new EventsAdapter(MainActivity.this, eventsList);
        eventsAdapter.setClickListener(this);
        RV.setAdapter(eventsAdapter);


        mLayoutManager = new LinearLayoutManager(this);


        RV.setLayoutManager(mLayoutManager);


        new ProcessListEvents().execute(loadNumber);
        RV.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1)) {
                    new ProcessListEvents().execute(loadNumber);
                } else if (dy < 0) {
                    //onScrolledUp();
                } else if (dy > 0) {
                    //onScrolledDown();
                }

            }
        });

        if (getIntent().getStringExtra("previousActivity") != null) {
            if (getIntent().getStringExtra("previousActivity").equals("register")) {

                Toast.makeText(getApplicationContext(),
                        "Welcome to the GO comunity!",
                        Toast.LENGTH_LONG).show();

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
                .getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget;
        // expand it by default

        // searchView.setQueryRefinementEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                Toast.makeText(getBaseContext(), "Ouery : " + query,
                        Toast.LENGTH_SHORT).show();

                //new EventSearchTask().execute();

                return true;
            }

          @Override
            public boolean onQueryTextChange(String s) {
                // Toast.makeText(getBaseContext(),"Our word : "+s,Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id
                = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_logout:
                UserFunctions uf = new UserFunctions();
                uf.logoutUser(getApplicationContext());

                Intent LI = new Intent(
                        getApplicationContext(), LoginActivity.class);
                LI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(LI);
                break;
            case R.id.menu_search:
                Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_map:
                //startActivity(new Intent(this, MapFragment.class));
                Toast.makeText(getApplicationContext(), "Map", Toast.LENGTH_SHORT).show();
                break;
           /* case R.id.menu_notifications:
                //startActivity(new Intent(this, SubActivity.class));
                Toast.makeText(getApplicationContext(), "Notifications", Toast.LENGTH_SHORT).show();
                break;*/
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public void itemClicked(View view, int position) {
        Toast.makeText(this, "Omak 2ar3a", Toast.LENGTH_LONG).show();


        Events ev = eventsList.get(position);
        new ProcessDisplayEvent().execute(ev.getEID());

    }

    private class ProcessDisplayEvent extends
            AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {

            EventFunctions eventFunction = new EventFunctions();
            JSONObject json = eventFunction.displayEvent(args[0]);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            String suc;
            try {
                suc = json.getString(DISPLAY_KEY_SUCCESS);
                if (suc != null) {
                    if (Integer.parseInt(suc) == 1) {

                        Intent postItemClick = new Intent(
                                getApplicationContext(), EventActivity.class);

                        postItemClick.putExtra("eventz",
                                json.getString("event"));

                        startActivity(postItemClick);

                    }
                } else {
                    // print error msg
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    private class ProcessListEvents extends
            AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // notification:Loading Events list
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            // int istart = Integer.parseInt(args[0]);
            loadNumber = args[0];


            //getUserInterests from SQLite DB
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            //db.addUser("tmpEmail","tmpUnae","tmpID","7-8-9"); default data for columns

            String userInterests = db.getUserInterests();
            //String userInterests = "7-8-9";
            EventFunctions eventFunction = new EventFunctions();
            JSONObject json = eventFunction.listSortedEvents(loadNumber, userInterests);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            String suc;
            try {
                suc = json.getString(LIST_KEY_SUCCESS);
                if (suc != null) {
                    if (Integer.parseInt(suc) == 1) {

                        loadNumber = Integer.toString(Integer.parseInt(loadNumber) + 1); //json.getString("lastEvID");

                        JSONArray jarray = json.getJSONArray("events");
                        int i;

                        /*String[] tmpTitles = {"Movie Night Saturday!","Contemporary dance workshop","Longest Line of Tables","رمضان كريم مع دستة الوان","CS 70 Graduation Ceremony","Summer Courses Registeration","Study in The USA"};
                        String[] tmpLocations = {"Jesuit Cultural Center","Alexandria Bibliotheca","San Stefano to Stanely Bridge","Fouad Street","Faculty of Science","Smouha","Ma3mal"};
                        String[] tmpDate = {"2015-JUN-27","2015-JUN-28","2015-JUN-26","2015-JUN-28","2015-JUN-29","2015-JUL-03","2015-JUL-04"};
                        String[] tmpTime = {"8:30 PM","3:00 PM","7:00 PM", "9:30 PM","11:20 AM","10:00 AM","6:15 PM"};
                        String[] tmpImage = {"https://scontent-ams2-1.xx.fbcdn.net/hphotos-xft1/v/t1.0-9/10167972_798152110232912_1397073488117519188_n.jpg?oh=94e89729aff66180eea3c59a50341d90&oe=561CB0E9","https://pixabay.com/static/uploads/photo/2015/02/18/14/35/self-portrait-640754_640.jpg","https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-xaf1/v/t1.0-9/11120071_10204033549618769_2642736532869495961_n.jpg?oh=ca1b1464e27f1416c8cb36547d69b1a6&oe=56255D2B&__gda__=1445335455_9317a04bf54a311dde80dba9441797d7","http://img.qwled.com/i/4fb6f13959af4f5c67907957ab1b0594.jpg","https://scontent-ams2-1.xx.fbcdn.net/hphotos-xfp1/v/t1.0-9/10991192_10205611199174850_5121141984558898918_n.jpg?oh=7c5faa23bf9637edefd27a098559e00c&oe=562BDC08","http://samdoesthings.com/wp-content/uploads/2015/04/studying.jpg","http://www.worldatlas.com/webimage/flags/usa/usalarge.gif"};*/
                        for (i = 2; i < jarray.length(); i++) {
                            JSONObject object = jarray.getJSONObject(i);
                            if (object == null) {
                                i = 0;
                                break;
                            }
                            Events event = new Events();

                            event.setEID(object.getString(LIST_KEY_EID));
                            event.setTitle(object.getString(LIST_KEY_TITLE));
                            event.setLocation(object
                                    .getString(LIST_KEY_LOCATION));
                            event.setDate(object.getString(LIST_KEY_DATE));
                            event.setTime(object.getString(LIST_KEY_TIME));
                            event.setImageURL(object.getString(LIST_KEY_IMAGEURL));

                            /*event.setTitle(tmpTitles[i%7]);
                            event.setLocation(tmpLocations[i%7]);
                            event.setDate(tmpDate[i%7]);
                            event.setTime(tmpTime[i%7]);
                            event.setImageURL(tmpImage[i%7]);*/


                            eventsList.add(event);

                        }

                        // get listview current position - used to maintain
                        // scroll position
                        // int currentPosition = list.getFirstVisiblePosition();

          /*              EventsAdapter adapter = new EventsAdapter(
                                getApplicationContext(), R.layout.custom_row,
                                eventsList);
                        list.setAdapter(adapter);

                        list.setSelectionFromTop(currentPosition, 0);
*/
        /*                eventsAdapter = new EventsAdapter(MainActivity.this, eventsList);
                        //eventsAdapter.setClickListener(MainActivity.this);
                        RV.setAdapter(eventsAdapter);
                        RV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
*/

                        eventsAdapter.notifyItemRangeInserted(Integer.parseInt(loadNumber) * i, i);

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