package com.vop.augumented;


import java.util.ArrayList;

import com.vop.tools.DBWrapper;
import com.vop.tools.VopApplication;

import android.R.integer;
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

public class HuidigeLocatieService extends Service {
	private static final String TAG = "MyService";
	VopApplication app;
	LocationManager locationManager;
	Criteria criteria = new Criteria();   
	private Location vorigeLocatie;
	private Location nieuweLocatie;
	
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
			ArrayList<com.vop.tools.data.Location> locations;
			Log.d(TAG, "location != null");
			DBWrapper.delete(new com.vop.tools.data.Location("huidig",null,null,null,null,null,Integer.parseInt(app.getState().get("userid"))));
			if( Integer.parseInt(app.getState().get("userid"))==2){
				locations=DBWrapper.getLocations(45);
			}
			else{
				locations=DBWrapper.getLocations(2);
			}
			for(com.vop.tools.data.Location l:locations){
				float afstand_tot_punt[] = new float[3];
				Location.distanceBetween(l.getLatitute(), l.getLongitude(), location.getLatitude(), location.getLongitude(), afstand_tot_punt);
				if(afstand_tot_punt[0]<25) Toast.makeText(this, "distance under 25meter", Toast.LENGTH_SHORT).show();
				else Toast.makeText(this, "distance above 25meter", Toast.LENGTH_SHORT).show();
			}
			DBWrapper.save(new com.vop.tools.data.Location("huidig", "huidig", location.getLatitude(), location.getLongitude(), location.getAltitude(), "default", Integer.parseInt(app.getState().get("userid"))));
			vorigeLocatie=location;						
		}
	}
	@Override
	public void onCreate() {
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
		app = (VopApplication) getApplicationContext();
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(provider, 100, 1,
				locationListener);
		Log.e(TAG, "oncreate afgehandeld");
		
	}
	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		/*Log.d(TAG, "onDestroy");
		locationManager.removeUpdates(locationListener);*/
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
