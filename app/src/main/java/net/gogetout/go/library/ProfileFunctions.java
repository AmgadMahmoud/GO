package net.gogetout.go.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class ProfileFunctions {

	private JSONParser jsonParser;

	// URL of the PHP API
	private static String fetchURL = "http://gogetout.net/android/APIs/profiles_api/";
	private static String editURL = "http://gogetout.net/android/APIs/profiles_api/";
	// private static String editURL =
	// "http://gogetout.net/android/APIs/profiles_api/";

	private static String fetch_tag = "fetch";
	private static String edit_tag = "edit";

	// private static String edit_tag = "edit";

	// constructor
	public ProfileFunctions() {
		jsonParser = new JSONParser();
	}

	/**
	 * Function to Register
	 **/
	public JSONObject fetchProfile(String user_unique_id) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", fetch_tag));
		params.add(new BasicNameValuePair("user_unique_id", user_unique_id));
		JSONObject json = jsonParser.getJSONFromUrl(fetchURL, params);
		return json;
	}

	public JSONObject editProfile(String uid, String about, String contact_phone,
			String interests, String personal_birth, String personal_education, String email, String lat,
			String lng) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", edit_tag));
		params.add(new BasicNameValuePair("uid", uid));
		params.add(new BasicNameValuePair("about", about));
		params.add(new BasicNameValuePair("contact_phone", contact_phone));
		params.add(new BasicNameValuePair("interests", interests));
		params.add(new BasicNameValuePair("personal_birth", personal_birth));
		params.add(new BasicNameValuePair("personal_education", personal_education));
		params.add(new BasicNameValuePair("personal_email", email));
		params.add(new BasicNameValuePair("lat", lat));
		params.add(new BasicNameValuePair("lng", lng));
		JSONObject json = jsonParser.getJSONFromUrl(editURL, params);
		return json;
	}

}
