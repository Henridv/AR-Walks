package com.vop.services;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.vop.tools.DBWrapper;
import com.vop.tools.LocationListener;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Point;
import com.vop.tools.data.Track;

/**
 * This service keeps track of the current track. When the service is stopped
 * the track is saved to the database.
 * 
 * @author henridv
 * 
 */
public class TrackService extends Service implements LocationListener {
	private static boolean RUNNING;
	private ArrayList<Point> walk;
	private VopApplication app;
	
	private static String name;

	/**
	 * @param name the name to set
	 */
	public static void setName(String name) {
		TrackService.name = name;
	}

	/**
	 * @return the name
	 */
	public static String getName() {
		return name;
	}

	@Override
	public void onCreate() {
		app = (VopApplication) getApplicationContext();
		app.addLocationListener(this);
		walk = new ArrayList<Point>();
	}

	@Override
	public void onDestroy() {
		app.removeLocationListener(this);
		Track track = new Track(name, DBWrapper.getProfile(Integer.parseInt(app.getState().get("userid"))), walk);
		DBWrapper.save(track);
		RUNNING = false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		RUNNING = true;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void locationUpdated() {
		walk.add(new Point(app.getLat(), app.getLng(), app.getAlt()));
		app.setCurrentTrack(walk);
	}

	public static boolean isRunning() {
		return RUNNING;
	}
}
