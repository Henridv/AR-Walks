package com.vop.services;

import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.vop.arwalks.MainScreen;
import com.vop.arwalks.R;
import com.vop.tools.VopApplication;

/**
 * This service runs as long as the application runs. It keeps track of the
 * current location.
 * 
 * @author gbostoen
 * 
 */
public class LocationService extends Service {
	private static final int HELLO_ID = 1;
	private VopApplication app;
	private LocationManager locationManager;

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	private Location currentBestLocation;
	private Notification notification;
	private NotificationManager mNotificationManager;
	private PendingIntent contentIntent;

	private static final int TWO_MINUTES = 1000 * 60 * 2;

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		app = (VopApplication) getApplicationContext();

		String ns = Context.NOTIFICATION_SERVICE;
		int icon = R.drawable.appicon;
		long when = System.currentTimeMillis();
		CharSequence tickerText = "Location service started";
		CharSequence contentTitle = "AR Walks Location";
		CharSequence contentText = "No location found yet";

		mNotificationManager = (NotificationManager) getSystemService(ns);

		notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_ONGOING_EVENT
				| Notification.FLAG_NO_CLEAR;

		Intent notificationIntent = new Intent(this, MainScreen.class);
		contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(app, contentTitle, contentText, contentIntent);

		mNotificationManager.notify(HELLO_ID, notification);
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location locationWIFI = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (locationWIFI != null && isBetterLocation(locationWIFI, locationGPS))
			updateWithNewLocation(locationWIFI);
		else if (locationGPS != null)
			updateWithNewLocation(locationGPS);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 2, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 2, locationListener);
	}

	@Override
	public void onDestroy() {
		locationManager.removeUpdates(locationListener);
		mNotificationManager.cancel(HELLO_ID);
		Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * Update location
	 * 
	 * @param location
	 *            New location
	 */
	private void updateWithNewLocation(Location location) {
		if (location != null && isBetterLocation(location, currentBestLocation)) {
			this.currentBestLocation = new Location(location);
			app.setLocation(location.getLatitude(), location.getLongitude(), location.getAltitude());

			// Update notification
			Geocoder g = new Geocoder(getApplicationContext());
			String address = "";
			try {
				Address add = g.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
				int i = add.getMaxAddressLineIndex();
				for (int j=0; j<i; j++) {
					if (j != 0) address += ", ";
					address += add.getAddressLine(j);
				}
			} catch (IOException e) {
			}
			if (!address.equals("") && contentIntent != null) {
				notification.setLatestEventInfo(app, "AR Walks Location", address, contentIntent);
				mNotificationManager.notify(HELLO_ID, notification);
			}
		}
	}
}
