package com.vop.augumented;

import java.util.ArrayList;

import com.vop.tools.DBWrapper;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Point;
import com.vop.tools.data.Traject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TrajectService extends Service {
	private static final String TAG = "MyService";
	ArrayList<Point> walk = new ArrayList<Point>();
	VopApplication app;
	LocationManager locationManager;
	Criteria criteria = new Criteria();   
	
	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}
		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	private void updateWithNewLocation(Location location) {
		if (location != null) {
				Toast.makeText(this, "nieuw punt", Toast.LENGTH_SHORT).show();
				walk.add(new Point(location.getLatitude(), location.getLongitude(), location.getAltitude()));
				app.setHuidige_walk(walk);
			}
		}	
	@Override
	public void onCreate() {
		app = (VopApplication) getApplicationContext();
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(provider, 100, 1,
				locationListener);
		
	}
	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
		Traject traject=new Traject(app.getState().get("naamtraject"), DBWrapper.getProfile(Integer.parseInt(app.getState().get("userid"))), walk);
		DBWrapper.save(traject);
		locationManager.removeUpdates(locationListener);
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
