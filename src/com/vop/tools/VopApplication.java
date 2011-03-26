package com.vop.tools;

import java.util.HashMap;

import com.vop.augumented.Marker;

import android.app.Application;

public class VopApplication extends Application {
	private HashMap<String, String> state;
	private Marker[] punten;

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
