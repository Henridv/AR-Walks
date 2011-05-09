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

import com.vop.tools.data.Location;
import com.vop.tools.data.Person;
import com.vop.tools.data.Point;
import com.vop.tools.data.Traject;

/**
 * This class provides an abstraction of the database. HTML connections are
 * hidden from the user.
 * 
 * @author henri
 * 
 */
public class DBWrapper {

	/**
	 * Get all tracks from the database
	 * 
	 * @return
	 */
	public static ArrayList<Traject> getTrajects() {
		String page = "traject.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("action", "trajects"));

		ArrayList<Traject> t = new ArrayList<Traject>();
		try {
			JSONArray jArray = doPOST(page, postValues);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				int id = json_data.getInt("id");
				String name = json_data.getString("name");
				Person person = getProfile(json_data.getInt("person"));
				ArrayList<Point> walk = new ArrayList<Point>();

				JSONArray walkArray = json_data.getJSONArray("walk");
				for (int j = 0; j < walkArray.length(); j++) {
					JSONObject walkObject = walkArray.getJSONObject(j);
					walk.add(new Point(walkObject.getDouble("lat"), walkObject.getDouble("lng"), walkObject.getDouble("alt")));
				}

				t.add(new Traject(id, name, person, walk));
			}
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
		return t;
	}

	/**
	 * faster loading of tracks
	 * 
	 * @return track
	 */
	public static ArrayList<Traject> getTrajects2() {
		String page = "traject.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("action", "trajects"));

		ArrayList<Traject> t = new ArrayList<Traject>();
		try {
			JSONArray jArray = doPOST(page, postValues);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				int id = json_data.getInt("id");
				String name = json_data.getString("name");
				Person person = getProfile(json_data.getInt("person"));

				t.add(new Traject(id, name, person, null));
			}
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
		return t;
	}

	/**
	 * get a certain track with id
	 * 
	 * @param walk_id
	 * @return
	 */
	public static Traject getTraject(int walk_id) {
		String page = "traject.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("id", Integer.toString(walk_id)));
		postValues.add(new BasicNameValuePair("action", "get_walk"));

		Traject t = null;
		try {
			JSONArray jArray = doPOST(page, postValues);
			JSONObject json_data = jArray.getJSONObject(0);
			int id = json_data.getInt("id");
			String name = json_data.getString("name");
			Person person = getProfile(json_data.getInt("person"));
			ArrayList<Point> walk = new ArrayList<Point>();

			JSONArray walkArray = json_data.getJSONArray("walk");
			for (int j = 0; j < walkArray.length(); j++) {
				JSONObject walkObject = walkArray.getJSONObject(j);
				walk.add(new Point(walkObject.getDouble("lat"), walkObject.getDouble("lng"), walkObject.getDouble("alt")));
			}

			t = new Traject(id, name, person, walk);
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
		return t;
	}

	/**
	 * Get a list of friends
	 * 
	 * @param personId
	 *            id of person who's logged in
	 * @return
	 */
	public static ArrayList<Person> getFriends(int personId) {
		String page = "persons.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("id", Integer.toString(personId)));
		postValues.add(new BasicNameValuePair("action", "friends"));

		ArrayList<Person> p = new ArrayList<Person>();

		// parse json data
		try {
			JSONArray jArray = doPOST(page, postValues);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				p.add(new Person(Integer.parseInt(json_data.getString("id")), json_data.getString("name"), json_data.getString("phone"), json_data.getString("password"), json_data.getString("email")));
			}
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
		return p;
	}

	/**
	 * get list of not added persons on the network
	 * 
	 * @param personId
	 * @return list of persons
	 */
	public static ArrayList<Person> getNotAddedPersons(int personId) {
		String page = "persons.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("id", Integer.toString(personId)));
		postValues.add(new BasicNameValuePair("action", "getnotaddedpeople"));

		ArrayList<Person> p = new ArrayList<Person>();

		// parse json data
		try {
			JSONArray jArray = doPOST(page, postValues);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				p.add(new Person(Integer.parseInt(json_data.getString("id")), json_data.getString("name"), json_data.getString("phone"), json_data.getString("password"), json_data.getString("email")));
			}
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
		return p;
	}

	/**
	 * get list of people who added you as a friend
	 * 
	 * @param personId
	 * @return list of people you might know
	 */
	public static ArrayList<Person> getPeopelWhoAddedYou(int personId) {
		String page = "persons.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("id", Integer.toString(personId)));
		postValues.add(new BasicNameValuePair("action", "getpeoplewhoaddedyou"));

		ArrayList<Person> p = new ArrayList<Person>();

		// parse json data
		try {
			JSONArray jArray = doPOST(page, postValues);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				p.add(new Person(Integer.parseInt(json_data.getString("id")), json_data.getString("name"), json_data.getString("phone"), json_data.getString("password"), json_data.getString("email")));
			}
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
		return p;
	}

	/**
	 * add a person as friend in the friends table
	 * 
	 * @param p1
	 * @param p2
	 */
	public static void addFriend(int p1, int p2) {
		String page = "persons.php";

		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("id_1", Integer.toString(p1)));
		postValues.add(new BasicNameValuePair("id_2", Integer.toString(p2)));

		postValues.add(new BasicNameValuePair("action", "addfriend"));

		// Post data
		try {
			doPOST(page, postValues);
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
	}

	/**
	 * Get a single profile
	 * 
	 * @param email
	 *            authentication via email
	 * @return
	 */
	public static Person getProfile(String email, String password) {
		String page = "persons.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("action", "profile"));
		postValues.add(new BasicNameValuePair("email", email));
		postValues.add(new BasicNameValuePair("password", password));

		Person p = null;
		try {
			JSONArray jArray = doPOST(page, postValues);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				p = new Person(json_data.getInt("id"), json_data.getString("name"), json_data.getString("phone"), json_data.getString("password"), json_data.getString("email"));
			}
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
		return p;
	}

	public static Person getProfile(String email) {
		String page = "persons.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("action", "profileFacebook"));
		postValues.add(new BasicNameValuePair("email", email));

		Person p = null;
		try {
			JSONArray jArray = doPOST(page, postValues);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				p = new Person(json_data.getInt("id"), json_data.getString("name"), json_data.getString("phone"), json_data.getString("password"), json_data.getString("email"));
			}
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
		return p;
	}

	/**
	 * Get a single profile
	 * 
	 * @param id
	 *            authentication via id
	 * @return
	 */
	public static Person getProfile(int id) {
		String page = "persons.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("action", "profile"));
		postValues.add(new BasicNameValuePair("id", Integer.toString(id)));

		Person p = null;
		try {
			JSONArray jArray = doPOST(page, postValues);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				p = new Person(json_data.getInt("id"), json_data.getString("name"), json_data.getString("phone"), json_data.getString("password"), json_data.getString("email"));
			}
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
		return p;
	}

	/**
	 * Get a list of locations from a person and his friends
	 * 
	 * @param personId
	 * @return
	 */
	public static ArrayList<Location> getLocations(int personId) {
		String page = "locations.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("id", Integer.toString(personId)));

		postValues.add(new BasicNameValuePair("action", "getloc"));

		ArrayList<Location> l = new ArrayList<Location>();

		// parse json data
		try {
			JSONArray jArray = doPOST(page, postValues);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				l.add(new Location(json_data.getInt("id"), json_data.getString("name"), json_data.getString("description"), json_data.getDouble("lat"), json_data.getDouble("lng"), json_data.getDouble("alt"), json_data.getString("date"), json_data.getInt("pers_id")));
			}
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
		return l;
	}

	/**
	 * get locations of friends
	 * 
	 * @param personId
	 * @return list of locations
	 */
	public static ArrayList<Location> getLocationsFriends(int personId) {
		String page = "locations.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("pers_id", Integer.toString(personId)));

		postValues.add(new BasicNameValuePair("action", "getlocfriends"));

		ArrayList<Location> l = new ArrayList<Location>();

		// parse json data
		try {
			JSONArray jArray = doPOST(page, postValues);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				l.add(new Location(json_data.getInt("id"), json_data.getString("name"), json_data.getString("description"), json_data.getDouble("lat"), json_data.getDouble("lng"), json_data.getDouble("alt"), json_data.getString("date"), json_data.getInt("pers_id")));
			}
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
		return l;
	}

	/**
	 * get current location of friends
	 * 
	 * @param personId
	 * @return
	 */
	public static ArrayList<Location> getLocationsHuidigVrienden(int personId) {
		String page = "locations.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("userid", Integer.toString(personId)));
		postValues.add(new BasicNameValuePair("name", "huidig"));

		postValues.add(new BasicNameValuePair("action", "getlochuidigfriends"));

		ArrayList<Location> l = new ArrayList<Location>();

		// parse json data
		try {
			JSONArray jArray = doPOST(page, postValues);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				l.add(new Location(json_data.getInt("id"), json_data.getString("name"), json_data.getString("description"), json_data.getDouble("lat"), json_data.getDouble("lng"), json_data.getDouble("alt"), json_data.getString("date"), json_data.getInt("pers_id")));
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return l;
	}

	/**
	 * Save a location
	 * 
	 * @param l
	 */
	public static void save(Location l) {
		String page = "locations.php";

		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		if (l.getId() != null)
			postValues.add(new BasicNameValuePair("id", Integer.toString(l.getId())));
		postValues.add(new BasicNameValuePair("name", l.getName()));
		postValues.add(new BasicNameValuePair("description", l.getDescription()));
		postValues.add(new BasicNameValuePair("lat", Double.toString(l.getLatitute())));
		postValues.add(new BasicNameValuePair("lng", Double.toString(l.getLongitude())));
		postValues.add(new BasicNameValuePair("alt", Double.toString(l.getAltitude())));
		postValues.add(new BasicNameValuePair("date", l.getDate()));
		postValues.add(new BasicNameValuePair("pers_id", Integer.toString(l.getPersId())));

		postValues.add(new BasicNameValuePair("action", "addloc"));

		// Post data
		try {
			doPOST(page, postValues);
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
	}

	/**
	 * save current location
	 * 
	 * @param pers_id
	 * @param lat
	 * @param lng
	 * @param alt
	 */
	public static void saveHuidig(int pers_id, double lat, double lng,
			double alt) {
		String page = "recentelocatie.php";

		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("pers_id", Integer.toString(pers_id)));
		postValues.add(new BasicNameValuePair("lat", Double.toString(lat)));
		postValues.add(new BasicNameValuePair("lng", Double.toString(lng)));
		postValues.add(new BasicNameValuePair("alt", Double.toString(alt)));

		postValues.add(new BasicNameValuePair("action", "huidigelocatie"));

		// Post data
		try {
			doPOST(page, postValues);
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
	}

	/**
	 * Save a person
	 * 
	 * @param p
	 */
	public static void save(Person p) {
		String page = "persons.php";

		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		if (p.getId() != null)
			postValues.add(new BasicNameValuePair("id", Integer.toString(p.getId())));
		postValues.add(new BasicNameValuePair("name", p.getName()));
		postValues.add(new BasicNameValuePair("phone", p.getPhone()));
		postValues.add(new BasicNameValuePair("password", p.getPassword()));
		postValues.add(new BasicNameValuePair("email", p.getEmail()));

		postValues.add(new BasicNameValuePair("action", "adduser"));

		// Post data
		try {
			doPOST(page, postValues);
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
	}

	/**
	 * Save a traject
	 * 
	 * @param t
	 */
	public static void save(Traject t) {
		String page = "traject.php";

		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		if (t.getId() != null)
			postValues.add(new BasicNameValuePair("id", Integer.toString(t.getId())));
		postValues.add(new BasicNameValuePair("name", t.getName()));
		postValues.add(new BasicNameValuePair("person", t.getPerson().getId().toString()));

		JSONArray walk = new JSONArray(t.getWalk());

		postValues.add(new BasicNameValuePair("walk", walk.toString()));
		postValues.add(new BasicNameValuePair("action", "addtraject"));

		// Post data
		try {
			doPOST(page, postValues);
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
	}

	/**
	 * Delete a location glenn bostoen
	 * 
	 * @param l
	 */
	public static void delete(Location l) {
		// TODO: delete location(glenn bostoen)
		String page = "locations.php";
		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("name", l.getName().toString()));
		postValues.add(new BasicNameValuePair("userid", Integer.toString((l.getPersId()))));
		postValues.add(new BasicNameValuePair("action", "delloc2"));

		// delete location
		try {
			doPOST(page, postValues);
		} catch (Exception e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
	}

	/**
	 * Delete a person
	 * 
	 * @param p
	 */
	public static void delete(Person p) {
		String page = "persons.php";

		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		if (p.getId() != null)
			postValues.add(new BasicNameValuePair("id", Integer.toString(p.getId())));
		else
			return;

		postValues.add(new BasicNameValuePair("action", "deluser"));

		// Post data
		try {
			doPOST(page, postValues);
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
	}

	/**
	 * delete a person as friend
	 * 
	 * @param id1
	 * @param id2
	 */
	public static void deleteFriend(int id1, int id2) {
		String page = "persons.php";

		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		postValues.add(new BasicNameValuePair("id", Integer.toString(id1)));
		postValues.add(new BasicNameValuePair("idfriend", Integer.toString(id2)));
		postValues.add(new BasicNameValuePair("action", "delfriend"));

		// Post data
		try {
			doPOST(page, postValues);
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
	}

	/**
	 * Delete a traject
	 * 
	 * @param t
	 */
	public static void delete(Traject t) {
		String page = "traject.php";

		ArrayList<NameValuePair> postValues = new ArrayList<NameValuePair>();
		if (t.getId() != null)
			postValues.add(new BasicNameValuePair("id", Integer.toString(t.getId())));
		else
			return;

		postValues.add(new BasicNameValuePair("action", "deltraject"));

		// Post data
		try {
			doPOST(page, postValues);
		} catch (JSONException e) {
			Log.e(VopApplication.LOGTAG, "Error parsing data " + e.toString());
		}
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
	private static JSONArray doPOST(String page,
			ArrayList<NameValuePair> postValues) throws JSONException {
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
			Log.e(VopApplication.LOGTAG, "Error in http connection "
					+ e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e(VopApplication.LOGTAG, "Error converting result "
					+ e.toString());
		}
		return new JSONArray(result);
	}
}
