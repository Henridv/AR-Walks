package com.vop.augumented;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.vop.tools.DBWrapper;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Point;
import com.vop.tools.data.Traject;

public class locatie_map2 extends MapActivity {
	LocationManager locationManager;
	String provider, context;
	Location location;
	MapController mapController;
	MapView mapView;
	int minDistance = 0;
	int minTime = 1000;
	MyLocationOverlay myLocationOverlay;
	punten_overlay itemizedoverlay;
	static Context content;

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
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.locatiesmap_layout);

		this.mapView = (MapView) findViewById(R.id.myMapView);
		this.mapController = this.mapView.getController();

		this.mapView.setSatellite(true);
		this.mapView.setStreetView(true);
		this.mapView.setBuiltInZoomControls(true);
		// this.mapView.displayZoomControls(true);
		Drawable drawable1 = this.getResources().getDrawable(
				R.drawable.androidmarker);
		itemizedoverlay = new punten_overlay(drawable1, this);
		try {
			// Create the file
			initMap();
		} catch (Exception e) {
			// Print out the exception that occurred
			Toast toast = Toast.makeText(getApplicationContext(), "hello",
					Toast.LENGTH_SHORT);
			toast.show();
			Log.w("hello", e.getMessage());
		}

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
		
		ArrayList<Traject> trajecten = DBWrapper.getTrajects();
		for(int i=0;i<trajecten.size();i++){
		showTrajectOnMap(DBWrapper.getTrajects().get(i));
		}
		
		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(provider, minTime, minDistance,
				locationListener);
	}

	private void showTrajectOnMap(Traject t) {
		VopApplication app = (VopApplication) content ;	
		Iterator <Point> it = t.getWalk().iterator();
		Point tmp = new Point();
		Marker[] POI = new Marker[t.getWalk().size()];
		int i=0;
		while (it.hasNext()) {
			tmp = it.next();
			POI[i] = new Marker("Punt "+i,"Punt van wandeling",
					tmp.getLongitude(),tmp.getLatitute(),tmp.getAltitude(),content);
			i++;
			}
			app.setPunten(POI);
	}

	private void updateWithNewLocation(Location location) {
		if (location != null) {
			// Create the file
			VopApplication app = (VopApplication) getApplicationContext();
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			app.setAlt(location.getAltitude());
			app.setLng(location.getLongitude());
			app.setLat(location.getLatitude());
			GeoPoint punt = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			this.mapController.animateTo(punt);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private void initMap() {
		construeer();
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		
		myLocationOverlay.enableCompass();

		myLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mapController.animateTo(myLocationOverlay.getMyLocation());
			}
		});
		VopApplication app = (VopApplication) getApplicationContext();
		Marker POI[] = app.getPunten();

		for (int i = 0; i < POI.length; i++) {
			GeoPoint punt = new GeoPoint((int) (POI[i].getLat() * 1E6),
					(int) (POI[i].getLng() * 1E6));
			OverlayItem overlayitem = new OverlayItem(punt, POI[i].getTitel(),
					POI[i].getTitel());
			itemizedoverlay.addOverlay(overlayitem);
		}

		myLocationOverlay.enableMyLocation();
		if (POI.length != 0)
			mapView.getOverlays().add(itemizedoverlay);
	}

	public void construeer() {
		VopApplication app = (VopApplication) content;
		Marker POI[];
		ArrayList<com.vop.tools.data.Location> loc = DBWrapper.getLocations(2);
		POI = new Marker[loc.size()];
		int j = 0;
		for (com.vop.tools.data.Location l : loc) {
			POI[j] = new Marker(l.getName(), l.getDescription(),
					l.getLongitude(), l.getLatitute(), l.getAltitude(),
					getApplicationContext());
			j++;
		}
		app.setPunten(POI);
	}
}
