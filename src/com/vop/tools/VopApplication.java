package com.vop.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import com.vop.overlays.Marker;
import com.vop.tools.data.Location;
import com.vop.tools.data.Person;
import com.vop.tools.data.Traject;

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
	private Traject traject;
	private Person persoon;
	private Marker POI[];

	public VopApplication() {
		super();
		state = new HashMap<String, String>();
	}

	public Person getPersoon() {
		return persoon;
	}

	public void setPersoon(Person persoon) {
		this.persoon = persoon;
	}

	public Traject getTraject() {
		return traject;
	}

	public void setTraject(Traject traject) {
		this.traject = traject;
	}

	public int getDichtste_punt() {
		return dichtste_punt;
	}

	public float getMax_afstand() {
		return max_afstand;
	}

	public void setMax_afstand(float max_afstand) {
		this.max_afstand = max_afstand;
	}

	/**
	 * @deprecated Use {@link #getRotationMatrix()} instead
	 */
	public synchronized float[] getValues() {
		return getRotationMatrix();
	}

	public synchronized float[] getRotationMatrix() {
		return rotationMatrix;
	}

	/**
	 * @deprecated Use {@link #setRotationMatrix(float[])} instead
	 */
	public synchronized void setValues(float[] values) {
		setRotationMatrix(values);
	}

	public synchronized void setRotationMatrix(float[] values) {
		this.rotationMatrix = values.clone();
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getAlt() {
		return alt;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

	public float getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(float azimuth) {
		this.azimuth = azimuth;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	public Marker[] getPunten() {
		return punten;
	}

	public void setPunten(Marker[] punten) {
		this.punten = punten;
	}

	public HashMap<String, String> getState() {
		return state;
	}

	public void setState(HashMap<String, String> state) {
		this.state = state;
	}

	public String putState(String k, String v) {
		return state.put(k, v);
	}
	
	/*
	private boolean isLocked = false;

	public synchronized void lock() throws InterruptedException {
		while (isLocked) {
			wait();
		}
		isLocked = true;
	}

	public synchronized void unlock() {
		isLocked = false;
		notify();
	}*/
	
	public void construeer() {
		Toast.makeText(getApplicationContext(), "POI inladen", Toast.LENGTH_SHORT).show();
		
		ArrayList<Location> loc = DBWrapper.getLocations(Integer.parseInt(state.get("userid")));
		Marker POI[] = new Marker[loc.size()];
		
		List<Marker> list = new ArrayList<Marker>();
		for (Location l : loc) {
			//list.add(new Marker(l.getName(), l.getDescription(), l.getLongitude(), l.getLatitute(), alt, lat, lng, alt, azimuth));
			list.add(new Marker(l, (float)lat, (float)lng, (float)alt));
		}

		Collections.sort(list);
		list.toArray(POI);

		this.setPunten(POI);
	}

	public void vernieuw() {
		for (int j = 0; j < punten.length; j++) {
			punten[j] = new Marker(punten[j].getTitle(), punten[j].getInfo(), punten[j].getLng(), punten[j].getLat(), punten[j].getAlt(), getApplicationContext());
		}
		Toast toast = Toast.makeText(getApplicationContext(), "POI vernieuwen", Toast.LENGTH_SHORT);
		toast.show();
	}
	public void construeer2(final Activity activity) {
				ArrayList<com.vop.tools.data.Location> loc = DBWrapper.getLocations(2);
				POI = new Marker[loc.size()];
				List<Marker> list = new ArrayList<Marker>();
				for (com.vop.tools.data.Location l : loc) {
					list.add(new Marker(l.getName(), l.getDescription(),
							l.getLongitude(), l.getLatitute(), alt, lat, lng,
							alt, roll));
				}
				Collections.sort(list);
				for(int i=0;i<list.size();i++){
					POI[i] = list.get(i);
				}
				setPunten(POI);	
	}

}
