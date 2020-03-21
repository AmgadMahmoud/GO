package net.gogetout.go.library;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventFunctions {

    private JSONParser jsonParser;

    // URL of the PHP API
    private static String APIURL = "http://gogetout.net/android/APIs/events_api/";

    private static String add_tag = "add";
    private static String list_tag = "list";
    private static String engine_tag = "engine";
    private static String display_tag = "display";
    private static String getImageUrl_tag = "getImageURL";
    private static String going_tag = "going";
    private static String addpost_tag = "addpost";
    private static String addquestion_tag = "addquestion";
    private static String addanswer_tag = "addanswer";
    private static String search_tag = "search";
    private static String nearby_tag = "nearby";

    // constructor
    public EventFunctions() {
        jsonParser = new JSONParser();
    }

    /**
     * Function to Add Event
     */
    public JSONObject addEvent(String email, String title, String location,
                               String date, String time, String description) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", add_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("location", location));
        params.add(new BasicNameValuePair("date", date));
        params.add(new BasicNameValuePair("time", time));
        params.add(new BasicNameValuePair("description", description));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }

    public JSONObject listEvents(String istart) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", list_tag));
        params.add(new BasicNameValuePair("start", istart));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }

    public JSONObject listSortedEvents(String loadNumber, String userInterests) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", engine_tag));
        params.add(new BasicNameValuePair("load_number", loadNumber));
        params.add(new BasicNameValuePair("interests", userInterests));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }

    public JSONObject grabNearbyEvents(String lat, String lng) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", nearby_tag));
        params.add(new BasicNameValuePair("lat", lat));
        params.add(new BasicNameValuePair("lng", lng));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }

    public JSONObject displayEvent(String EID) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", display_tag));
        params.add(new BasicNameValuePair("eid", EID));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;

    }

    public JSONObject getImageURL(String EID) {
        // TODO Auto-generated method stub
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getImageUrl_tag));
        params.add(new BasicNameValuePair("eid", EID));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }

    public JSONObject going(String eid, String uid) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", going_tag));
        params.add(new BasicNameValuePair("eid", eid));
        params.add(new BasicNameValuePair("uid", uid));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }

    public JSONObject uploadPost(String uid, String eid, String post) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", addpost_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("eid", eid));
        params.add(new BasicNameValuePair("post", post));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }

    public JSONObject uploadQuestion(String uid, String eid, String question) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", addquestion_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("eid", eid));
        params.add(new BasicNameValuePair("question", question));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }

    public JSONObject uploadAnswer(String uid, String qid, String answer) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", addanswer_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("qid", qid));
        params.add(new BasicNameValuePair("answer", answer));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }

    public JSONObject search(String query) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", search_tag));
        params.add(new BasicNameValuePair("query", query));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }
}
