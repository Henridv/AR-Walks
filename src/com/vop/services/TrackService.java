package com.vop.services;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.vop.tools.DBWrapper;
import com.vop.tools.LocationListener;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Point;
import com.vop.tools.data.Traject;

/**
 * This service keeps track of the current track. When the service is stopped
 * the track is saved to the database.
 * 
 * @author henridv
 * 
 */
public class TrackService extends Service implements LocationListener {
	ArrayList<Point> walk = new ArrayList<Point>();
	VopApplication app;

	@Override
	public void onCreate() {
		app = (VopApplication) getApplicationContext();
	}

	@Override
	public void onDestroy() {
		Traject traject = new Traject(app.getState().get("naamtraject"), DBWrapper.getProfile(Integer.parseInt(app.getState().get("userid"))), walk);
		DBWrapper.save(traject);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void locationUpdated() {
		walk.add(new Point(app.getLat(), app.getLng(), app.getAlt()));
		app.setHuidige_walk(walk);
	}
}
