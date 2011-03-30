package com.vop.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.Pair;

import com.vop.tools.data.Location;
import com.vop.tools.data.Person;
import com.vop.tools.data.Traject;

/**
 * This class provides an abstraction of the database. HTML connections are
 * hidden from the user
 * 
 * @author henri
 * 
 */
public class DBWrapper {
	private static String log_tag;

	public DBWrapper() {
		log_tag = "AR Walks";
	}

	/**
	 * Get all trajects from the database
	 * 
	 * @return
	 */
	public static ArrayList<Traject> getTrajects() {
		return null;
	}

	/**
	 * Get a list of friends
	 * 
	 * @param personId
	 *            id of person who's logged in
	 * @return
	 */
	public ArrayList<Person> getFriends(int personId) {
		String page = "persons.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues
				.add(new BasicNameValuePair("id", Integer.toString(personId)));

		ArrayList<Person> p = new ArrayList<Person>();

		// parse json data
		try {
			JSONArray jArray = doPOST(page, null);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				p.add(new Person(json_data.getInt("id"), json_data
						.getString("name"), json_data.getString("phone"),
						json_data.getString("password"), json_data
								.getString("email")));
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return p;
	}

	/**
	 * Get a single profile
	 * 
	 * @param email
	 *            authentication via email
	 * @return
	 */
	public Person getProfile(String email) {
		String page = "persons.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("action", "profile"));
		postValues.add(new BasicNameValuePair("email", email));

		Person p = null;
		try {
			JSONArray jArray = doPOST(page, null);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				p = new Person(json_data.getInt("id"), json_data
						.getString("name"), json_data.getString("phone"),
						json_data.getString("password"), json_data
								.getString("email"));
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return p;
	}

	/**
	 * Get a list of locations from a person and his friends
	 * @param personId
	 * @return
	 */
	public ArrayList<Location> getLocations(int personId) {
		String page = "locations.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues
				.add(new BasicNameValuePair("id", Integer.toString(personId)));

		ArrayList<Location> l = new ArrayList<Location>();

		// parse json data
		try {
			JSONArray jArray = doPOST(page, null);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				l.add(new Location(json_data.getInt("id"),
						json_data.getString("name"),
						new Pair<Double, Double>(json_data.getDouble("lat"), json_data.getDouble("lng")),
						json_data.getString("date")));
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return l;
	}
	
	/**
	 * Save a location
	 * @param l
	 */
	public static void save(Location l) {
		//TODO: save location
	}
	
	/**
	 * Create a location
	 * @param l
	 */
	public static void create(Location l) {
		//TODO: create location
	}
	
	/**
	 * Perform actual HTTP POST
	 * 
	 * @param page
	 *            page to send request to
	 * @param postValues
	 *            pairs of values to send
	 * @return JSON array
	 * @throws JSONException
	 */
	private JSONArray doPOST(String page, ArrayList<NameValuePair> postValues)
			throws JSONException {
		String result = "";
		InputStream is = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://move.ugent.be/~vop/"
					+ page);
			httppost.setEntity(new UrlEncodedFormEntity(postValues));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e(log_tag, "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e(log_tag, "Error converting result " + e.toString());
		}
		return new JSONArray(result);
	}
}
