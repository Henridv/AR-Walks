package com.vop.arwalks;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.vop.map.overlays.WandelingOverlay2;
import com.vop.tools.DBWrapper;
import com.vop.tools.LocationListener;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Point;
import com.vop.tools.data.Track;

/**
 * start a track
 * 
 * @author gbostoen
 * 
 */
public class StartEenWandeling extends MapActivity implements LocationListener {
	private MapController mapController;
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private Track t;
	private VopApplication app;

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	// ////////////////////////////////////
	// INITIALISATIE //
	// ////////////////////////////////////

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		this.app = (VopApplication) getApplicationContext();
		super.onCreate(savedInstanceState);
		int walk_id = getIntent().getIntExtra("walk_id", 0); // nummer van
		// traject
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.locatiesmap_layout);

		this.mapView = (MapView) findViewById(R.id.myMapView);
		this.mapController = this.mapView.getController();

		this.mapView.setBuiltInZoomControls(true);
		this.mapView.setSatellite(true);
		this.mapView.setStreetView(true);

		initMap();
		t = DBWrapper.getTrack(walk_id);

		drawPath(t.getWalk(), -65536);
	}

	/**
	 * initialize the map
	 */
	private void initMap() {
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		this.mapView.getOverlays().add(myLocationOverlay);
		this.mapController.setZoom(17);
		this.mapController.animateTo(new GeoPoint((int) (app.getLat() * 1E6), (int) (app.getLng() * 1E6)));
		myLocationOverlay.enableMyLocation();
	}

	// ////////////////////////////////////
	// FUNCTIES //
	// ////////////////////////////////////

	@SuppressWarnings("unchecked")
	// door drawline functie in wandeling_overlay
	private void drawPath(ArrayList<Point> listOfPoints, int color) {
		List overlays = this.mapView.getOverlays();
		ArrayList<GeoPoint> listOfGeoPoints = new ArrayList<GeoPoint>();
		for (int i = 0; i < listOfPoints.size(); i++) {
			listOfGeoPoints.add(new GeoPoint((int) (listOfPoints.get(i).getLatitute() * 1E6), (int) (listOfPoints.get(i).getLongitude() * 1E6)));

		}
		overlays.add(new WandelingOverlay2(listOfGeoPoints, color));
	}

	@Override
	public void locationUpdated() {
		double lat = app.getLat();
		double lng = app.getLng();

		GeoPoint punt = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		this.mapController.animateTo(punt);
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
