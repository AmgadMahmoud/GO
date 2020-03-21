package net.gogetout.go;

import android.os.AsyncTask;

import net.gogetout.go.library.EventFunctions;

import org.json.JSONException;
import org.json.JSONObject;


class GetImageURL extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... args) {

        EventFunctions evFunction = new EventFunctions();
        JSONObject json = evFunction.getImageURL(args[0]);

        try {
            String imurl = json.getJSONObject("event").getString("imageURL");
            return imurl;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "http://axandroid.host56.com/images/GO.jpg";
    }

}