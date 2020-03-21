package net.gogetout.go;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.gogetout.go.library.DatabaseHandler;
import net.gogetout.go.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class LoginActivity extends ActionBarActivity {

    Button btnLogin;

    private AutoCompleteTextView loginEmail;
    private EditText loginPassword;
    private TextView loginErrorMsg;



    String[] arrayToStoreEmails;
    SharedPreferences SP;
    ArrayAdapter<String> suggestionsAdapter;


    private static String LOGIN_KEY_SUCCESS = "success";
    private static String LOGIN_KEY_UID = "uid";
    private static String LOGIN_KEY_USERNAME = "uname";
    private static String LOGIN_KEY_EMAIL = "email";
    private static String LOGIN_KEY_INTERESTS = "interests";
    private Toolbar toolbar;


    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        fbLoginButton = (LoginButton)findViewById(R.id.fb_login_button);

        loginEmail = (AutoCompleteTextView) findViewById(R.id.loginemail);
        loginPassword = (EditText) findViewById(R.id.loginpassword);
        btnLogin = (Button) findViewById(R.id.loginbutton);
        //loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);

        initializeEmailSuggestions();

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if ((!loginEmail.getText().toString().equals(""))
                        && (!loginPassword.getText().toString().equals(""))) {

                    // suggestions part start
                    SharedPreferences.Editor editor = SP.edit();

                    int EC = SP.getInt("emailCounter", 1);
                    Map<String, ?> keys = SP.getAll();

                    ArrayList<String> strings = new ArrayList<String>();
                    for (Map.Entry<String, ?> entry : keys.entrySet()) {
                        if (entry.getValue() instanceof String) {
                            strings.add((String) entry.getValue());
                        }
                    }
                    arrayToStoreEmails = strings.toArray(new String[strings
                            .size()]);

                    suggestionsAdapter.notifyDataSetChanged();

                    if (!Arrays.asList(arrayToStoreEmails).contains(
                            loginEmail.getText().toString())) {
                        editor.putString("email" + EC, loginEmail.getText()
                                .toString());
                        EC++;
                        editor.putInt("emailCounter", EC);
                        editor.commit();
                    }

                    // suggestions part end

                    NetAsyncLogin(view);
                } else if ((!loginEmail.getText().toString().equals(""))) {

                } else if ((!loginPassword.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("Facebook Login Successful!");
                System.out.println("Logged in user Details : ");
                System.out.println("--------------------------");
                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();


                //hide button
                fbLoginButton.setVisibility(View.GONE);

                //read profile info
                //store profile info in SQLite DB
                //navigate home
                //on logout remove DB column & accessToken


                Intent home = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(home);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();

            }



        });


    }

    private void initializeEmailSuggestions() {

        SP = getSharedPreferences("emailSugesstions", Context.MODE_PRIVATE);
        // create suggestions array start

        Map<String, ?> keys = SP.getAll();

        ArrayList<String> strings = new ArrayList<String>();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            if (entry.getValue() instanceof String) {
                strings.add((String) entry.getValue());
                // Toast.makeText(getApplicationContext(),
                // (CharSequence) entry.getValue(),
                // Toast.LENGTH_LONG).show();
            }
        }
        arrayToStoreEmails = strings.toArray(new String[strings.size()]);

        // create suggestions array end
        suggestionsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arrayToStoreEmails);
        loginEmail.setThreshold(0);

        loginEmail.setAdapter(suggestionsAdapter);

    }

    public void NetAsyncLogin(View view) {
        new NetCheckLogin().execute();
    }


    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        callbackManager.onActivityResult(reqCode, resCode, i);
    }

    public void navRegister(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private class NetCheckLogin extends AsyncTask<String, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
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
                nDialog.dismiss();
                new ProcessLogin().execute();
            } else {
                nDialog.dismiss();
                //loginErrorMsg.setText("Couldn't Contact Server");
                Toast.makeText(LoginActivity.this, "Couldn't Contact Server", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        String email, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loginEmail = (AutoCompleteTextView) findViewById(R.id.loginemail);
            loginPassword = (EditText) findViewById(R.id.loginpassword);
            email = loginEmail.getText().toString();
            password = loginPassword.getText().toString();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(email, password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString(LOGIN_KEY_SUCCESS) != null) {

                    String res = json.getString(LOGIN_KEY_SUCCESS);

                    if (Integer.parseInt(res) == 1) {
                        // pDialog.setMessage("Loading User Space");
                        // pDialog.setTitle("Getting Data");
                        DatabaseHandler db = new DatabaseHandler(
                                getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");
                        JSONObject json_profile = json.getJSONObject("profile");
                        JSONObject json_app = json.getJSONObject("app");
                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(LOGIN_KEY_EMAIL),
                                json_user.getString(LOGIN_KEY_USERNAME),
                                json_user.getString(LOGIN_KEY_UID),
                                json_profile.getString(LOGIN_KEY_INTERESTS));

checkAppUpdates(json_app.getString("version"));

                        /**
                         * If JSON array details are stored in SQlite it
                         * launches the User Panel.
                         **/
                        Intent MA = new Intent(getApplicationContext(),
                                MainActivity.class);
                        MA.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(MA);
                        /**
                         * Close Login Screen
                         **/
                        finish();
                    } else {

                        pDialog.dismiss();
                        //loginErrorMsg.setText("Incorrect E-mail/Password");
                        Toast.makeText(LoginActivity.this, "Incorrect E-mail/Password", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkAppUpdates(String version) {

        //import market nav code here
    }

}