package com.vop.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.vop.ar.AugmentedReality;
import com.vop.arwalks.Locations;
import com.vop.arwalks.R;
import com.vop.arwalks.SaveLocation;
import com.vop.map.overlays.LocationsOverlay;
import com.vop.tools.LocationListener;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Marker;

/**
 * Shows locations on a map
 * 
 */
public class LocationMap extends MapActivity implements LocationListener {
	private MapController mapController;
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private LocationsOverlay itemizedoverlay;
	private VopApplication app;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		app = (VopApplication) getApplicationContext();
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.locations_on_map);

		this.mapView = (MapView) findViewById(R.id.myMapView);
		this.mapController = this.mapView.getController();

		this.mapView.setSatellite(false);
		this.mapView.setBuiltInZoomControls(true);
		Drawable drawable1 = this.getResources().getDrawable(R.drawable.androidmarker);
		itemizedoverlay = new LocationsOverlay(drawable1, this);
		initMap();
		this.mapController.setZoom(17);
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
		app.getPOIs();
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
		app.getPOIs();
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
		inflater.inflate(R.menu.locations_on_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(60);
		switch (item.getItemId()) {
		case R.id.augmentedView:
			Intent myIntent = new Intent(LocationMap.this, AugmentedReality.class);
			LocationMap.this.startActivity(myIntent);
			return true;
		case R.id.opslaan:
			myIntent = new Intent(LocationMap.this, SaveLocation.class);
			LocationMap.this.startActivity(myIntent);
			return true;
		case R.id.refresh:
			app.getPOIs();
			refreshMap();
			return true;
		case R.id.lijstloc:
			myIntent = new Intent(LocationMap.this, Locations.class);
			LocationMap.this.startActivity(myIntent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void locationUpdated() {
		double lat = app.getLat();
		double lng = app.getLng();
		GeoPoint punt = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		this.mapController.animateTo(punt);
		app.getPOIs();
	}
	
	/**
	 * Set listeners
	 */
	@Override
	protected void onResume() {
		super.onResume();
		app.addLocationListener(this);
	}

	/**
	 * Remove listeners
	 */
	@Override
	protected void onStop() {
		super.onStop();
		app.removeLocationListener(this);
	}
}
