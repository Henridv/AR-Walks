package com.vop.ar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import com.vop.ar.overlays.Marker;
import com.vop.ar.overlays.punten_overlay;
import com.vop.arwalks.ListLocaties;
import com.vop.arwalks.R;
import com.vop.arwalks.SaveLocation;
import com.vop.tools.VopApplication;

/**
 * locations projected on a map
 * 
 * @author gbostoen
 * 
 */
public class LocatieMap extends MapActivity {
	LocationManager locationManager;
	String provider, context;
	Location location;
	MapController mapController;
	MapView mapView;
	int minDistance = 5;
	int minTime = 1000;
	MyLocationOverlay myLocationOverlay;
	punten_overlay itemizedoverlay;
	Context content;
	VopApplication app;

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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		content = getApplicationContext();
		app = (VopApplication) getApplicationContext();
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.locatiesmap_layout);

		this.mapView = (MapView) findViewById(R.id.myMapView);
		this.mapController = this.mapView.getController();

		this.mapView.setSatellite(true);
		this.mapView.setStreetView(true);
		this.mapView.setBuiltInZoomControls(true);
		// this.mapView.displayZoomControls(true);
		Drawable drawable1 = this.getResources().getDrawable(R.drawable.androidmarker);
		itemizedoverlay = new punten_overlay(drawable1, this);
		initMap();
		this.mapController.setZoom(17);
		this.context = Context.LOCATION_SERVICE;
		this.locationManager = (LocationManager) getSystemService(context);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // constant 1
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

		this.provider = locationManager.getBestProvider(criteria, true);
		this.location = locationManager.getLastKnownLocation(provider);

		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(provider, minTime, minDistance, locationListener);
	}

	/**
	 * update current location on the map
	 * 
	 * @param location
	 */
	private void updateWithNewLocation(Location location) {
		if (location != null) {
			// Create the file
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			app.setLocation(location.getLatitude(), location.getLongitude(), location.getAltitude());
			GeoPoint punt = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			this.mapController.animateTo(punt);
			app.construeer();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * initialize the map
	 */
	private void initMap() {
		app.construeer();
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();

		myLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mapController.animateTo(myLocationOverlay.getMyLocation());
			}
		});
		Marker POI[] = app.getPunten();

		for (int i = 0; i < POI.length; i++) {
			GeoPoint punt = new GeoPoint((int) (POI[i].getLat() * 1E6), (int) (POI[i].getLng() * 1E6));
			OverlayItem overlayitem = new OverlayItem(punt, POI[i].getTitle(), POI[i].getTitle());
			itemizedoverlay.addOverlay(overlayitem);
		}

		myLocationOverlay.enableMyLocation();
		if (POI.length != 0)
			mapView.getOverlays().add(itemizedoverlay);
	}

	/**
	 * method to refresh map with the latest points
	 */
	private void refreshMap() {
		app.construeer();
		mapView.getOverlays().clear();
		mapView.getOverlays().add(myLocationOverlay);
		Marker POI[] = app.getPunten();
		for (int i = 0; i < POI.length; i++) {
			GeoPoint punt = new GeoPoint((int) (POI[i].getLat() * 1E6), (int) (POI[i].getLng() * 1E6));
			OverlayItem overlayitem = new OverlayItem(punt, POI[i].getTitle(), POI[i].getAlt()
					+ " " + POI[i].getInfo());

			itemizedoverlay.addOverlay(overlayitem);
		}
		mapView.getOverlays().add(itemizedoverlay);
		mapView.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.locaties_map_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(60);
		switch (item.getItemId()) {
		case R.id.augmentedView:
			Intent myIntent = new Intent(LocatieMap.this, AugmentedReality.class);
			LocatieMap.this.startActivity(myIntent);
			return true;
		case R.id.opslaan:
			myIntent = new Intent(LocatieMap.this, SaveLocation.class);
			LocatieMap.this.startActivity(myIntent);
			return true;
		case R.id.refresh:
			app.construeer();
			refreshMap();
			return true;
		case R.id.lijstloc:
			myIntent = new Intent(LocatieMap.this, ListLocaties.class);
			LocatieMap.this.startActivity(myIntent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
