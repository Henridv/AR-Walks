package com.vop.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.vop.ar.overlays.Marker;
import com.vop.services.LocationService;
import com.vop.tools.data.Location;
import com.vop.tools.data.Person;
import com.vop.tools.data.Point;
import com.vop.tools.data.Track;

/**
 * An Android Application extending Application. It provides functions to
 * maintain a state for the app.
 * 
 * @author henridv
 * 
 */
public class VopApplication extends Application {
	public final static String PREFS = "VOPPREFS";
	public final static String LOGTAG = "vop_log";

	private HashMap<String, String> state;
	private Marker[] punten;
	private double lng;
	private double lat;
	private double alt;

	private float azimuth;
	private float pitch;
	private float roll;
	private double heading;
	private float rotationMatrix[];
	private float max_afstand;
	private int dichtste_punt;
	private Track track;
	private Person persoon;
	private ArrayList<Point> huidige_walk;
	private Marker center;

	private Intent locationServiceIntent;

	private ArrayList<LocationListener> locationListeners;

	public VopApplication() {
		super();
		state = new HashMap<String, String>();
		locationListeners = new ArrayList<LocationListener>();
	}

	public void construeer() {
		Toast.makeText(getApplicationContext(), "POI inladen", Toast.LENGTH_SHORT).show();

		ArrayList<Location> loc = DBWrapper.getLocations(Integer.parseInt(state.get("userid")));
		Marker POI[] = new Marker[loc.size()];

		List<Marker> list = new ArrayList<Marker>();
		for (Location l : loc) {
			list.add(new Marker(l, (float) lat, (float) lng, (float) alt));
		}

		Collections.sort(list);
		list.toArray(POI);

		this.setPunten(POI);
	}

	public double getAlt() {
		return alt;
	}

	public float getAzimuth() {
		return azimuth;
	}

	public Marker getCenter() {
		return center;
	}

	public int getDichtste_punt() {
		return dichtste_punt;
	}

	public double getHeading() {
		return heading;
	}

	public ArrayList<Point> getHuidige_walk() {
		return huidige_walk;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public float getMax_afstand() {
		return max_afstand;
	}

	public Person getPersoon() {
		return persoon;
	}

	public float getPitch() {
		return pitch;
	}

	public Marker[] getPunten() {
		return punten;
	}

	public float getRoll() {
		return roll;
	}

	/**
	 * Get the current rotation matrix.
	 * 
	 * @return a 16x16 matrix which can be used to calculate the screen
	 *         orientation.
	 */
	public synchronized float[] getRotationMatrix() {
		return rotationMatrix;
	}

	public HashMap<String, String> getState() {
		return state;
	}

	public Track getTraject() {
		return track;
	}

	/**
	 * check if user is online
	 * 
	 * @return TRUE if user is connected
	 */
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() == null)
			return false;
		else
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();

	}

	public String putState(String k, String v) {
		return state.put(k, v);
	}

	/**
	 * @deprecated Use {@link #setLocation(lat, lng, alt)} instead
	 * @param alt
	 */
	@Deprecated
	public void setAlt(double alt) {
		this.alt = alt;
	}

	public void setAzimuth(float azimuth) {
		this.azimuth = azimuth;
	}

	public void setCenter(Marker marker) {
		this.center = marker;

	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	public void setCurrentTrack(ArrayList<Point> huidige_walk) {
		this.huidige_walk = huidige_walk;
	}

	/**
	 * @deprecated Use {@link #setLocation(lat, lng, alt)} instead
	 * @param lat
	 */
	@Deprecated
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @deprecated Use {@link #setLocation(lat, lng, alt)} instead
	 * @param lng
	 */
	@Deprecated
	public void setLng(double lng) {
		this.lng = lng;
	}

	public void setLocation(double lat, double lng, double alt) {
		this.lat = lat;
		this.lng = lng;
		this.alt = alt;
		for (LocationListener l : locationListeners)
			l.locationUpdated();
	}

	public void setMax_afstand(float max_afstand) {
		this.max_afstand = max_afstand;
	}

	public void setPersoon(Person persoon) {
		this.persoon = persoon;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setPunten(Marker[] punten) {
		this.punten = punten;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	/**
	 * Set the current rotation matrix.
	 * 
	 * @param values
	 */
	public synchronized void setRotationMatrix(float[] values) {
		this.rotationMatrix = values.clone();
	}

	public void setState(HashMap<String, String> state) {
		this.state = state;
	}

	public void setTraject(Track track) {
		this.track = track;
	}

	/**
	 * Start the location service. This service keeps track of the current
	 * location throughout the lifetime of the application.
	 */
	public void startLocationService() {
		if (locationServiceIntent == null) {
			locationServiceIntent = new Intent(this, LocationService.class);
			startService(locationServiceIntent);
		}
	}

	/**
	 * Stop the location service.
	 */
	public void stopLocationService() {
		if (locationServiceIntent != null) {
			stopService(locationServiceIntent);
			locationServiceIntent = null;
		}
	}

	public void addLocationListener(LocationListener locationListener) {
		this.locationListeners.add(locationListener);
	}

	public void removeLocationListener(LocationListener locationListener) {
		locationListeners.remove(locationListener);
	}
}
