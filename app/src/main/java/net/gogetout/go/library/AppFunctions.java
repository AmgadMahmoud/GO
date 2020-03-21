package net.gogetout.go.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

public class AppFunctions {
	private JSONParser jsonParser;

    //URL of the PHP API
    private static String getVersionURL = "http://gogetout.net/android/APIs/getversion.php";


public AppFunctions(){
    jsonParser = new JSONParser();
}

    public JSONObject getAppVersion() {
		// TODO Auto-generated method stub
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject json = jsonParser.getJSONFromUrl(getVersionURL, params);
        return json;   	
    	}
}
