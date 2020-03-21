package net.gogetout.go;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.gogetout.go.library.DatabaseHandler;
import net.gogetout.go.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class RegisterActivity extends ActionBarActivity {

    private static String REGISTER_KEY_SUCCESS = "success";
    private static String REGISTER_KEY_UID = "uid";
    private static String REGISTER_KEY_USERNAME = "uname";
    private static String REGISTER_KEY_EMAIL = "email";
    private static String REGISTER_KEY_ERROR = "error";
    private Toolbar toolbar;

    private EditText registerEmail;
    private Button btnSignup;
    private EditText registerUsername;
    private EditText registerPassword;
    private TextView registerErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        registerEmail = (EditText) findViewById(R.id.email_et);
        registerUsername = (EditText) findViewById(R.id.username_et);
        registerPassword = (EditText) findViewById(R.id.password_et);
        btnSignup = (Button) findViewById(R.id.register_b);
        registerErrorMsg = (TextView) findViewById(R.id.error_tv);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((!registerUsername.getText().toString().equals(""))
                        && (!registerPassword.getText().toString().equals(""))
                        && (!registerEmail.getText().toString().equals(""))) {
                    if (registerUsername.getText().toString().length() > 4) {
                        NetAsyncRegister(view);

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Username should be minimum 5 characters",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    public void NetAsyncRegister(View view) {
        new NetCheckRegister().execute();
    }

    private class NetCheckRegister extends AsyncTask<String, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*nDialog = new ProgressDialog(RegisterActivity.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();*/
        }

        @Override
        protected Boolean doInBackground(String... args) {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.getoutgo.netne.net");
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
                //nDialog.dismiss();
                new ProcessRegister().execute();
            } else {
                //nDialog.dismiss();
                registerErrorMsg.setText("Couldn't Contact Server: Check your network settings");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

        /**
         * Defining Process dialog
         */
        private ProgressDialog pDialog; // C_5 start.

        String email, password, uname; // remove fn,ln

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email = registerEmail.getText().toString();
            uname = registerUsername.getText().toString();
            password = registerPassword.getText().toString();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.registerUser(email, uname, password);

            return json;

        } // C_5 end.

        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString(REGISTER_KEY_SUCCESS) != null) {
                    registerErrorMsg.setText("");
                    String res = json.getString(REGISTER_KEY_SUCCESS);

                    String red = json.getString(REGISTER_KEY_ERROR);

                    if (Integer.parseInt(res) == 1) {
                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");

                        DatabaseHandler db = new DatabaseHandler(
                                getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        /**
                         * Removes all the previous data in the SQlite database
                         **/

                        UserFunctions logout = new UserFunctions(); // C_6
                        // start.
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(REGISTER_KEY_EMAIL),
                                json_user.getString(REGISTER_KEY_USERNAME),
                                json_user.getString(REGISTER_KEY_UID),
                                null);
                        /**
                         * Stores registered data in SQlite Database Launch
                         * Registered screen
                         **/
                        // C_6 end.
                        // Intent registered = new
                        // Intent(getApplicationContext(), ListEvents.class);

                        /**
                         * Close all views before launching Registered screen
                         **/
                        // registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        // startActivity(registered);
                        /*Toast.makeText(getApplicationContext(),
                                "Welcome to the GO comunity!",
                                Toast.LENGTH_LONG).show();*/
                        Intent MA = new Intent(getApplicationContext(),
                                MainActivity.class);
                        MA.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        MA.putExtra("previousActivity","register");
                        finish();
                        startActivity(MA);
                    } else if (Integer.parseInt(red) == 2) {
                        pDialog.dismiss();
                        registerErrorMsg.setText("User already exists");
                    } else if (Integer.parseInt(red) == 3) {
                        pDialog.dismiss();
                        registerErrorMsg.setText("Invalid Email");
                    }

                } else {
                    pDialog.dismiss();

                    registerErrorMsg.setText("Error occured in registration");
                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
}
