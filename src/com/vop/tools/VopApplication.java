package com.vop.tools;

import java.util.HashMap;

import com.vop.augumented.Marker;

import android.app.Application;

public class VopApplication extends Application {
	private HashMap<String, String> state;
	private Marker[] punten;
	private double lng;
	private double lat;
	private double alt;
	
	private double roll;
	private double pitch;
	private double heading;

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

	public double getRoll() {
		return roll;
	}

	public void setRoll(double roll) {
		this.roll = roll;
	}

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	public VopApplication() {
		super();
		state = new HashMap<String, String>();
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

}
