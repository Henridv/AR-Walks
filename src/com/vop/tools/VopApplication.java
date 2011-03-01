package com.vop.tools;

import java.util.HashMap;

import android.app.Application;

public class VopApplication extends Application {
	private HashMap<String, String> state;
	
	public VopApplication() {
		super();
		state = new HashMap<String, String>();
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
