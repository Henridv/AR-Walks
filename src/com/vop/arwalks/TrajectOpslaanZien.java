package com.vop.arwalks;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.vop.ar.overlays.WandelingOverlay2;
import com.vop.ar.overlays.punten_overlay;
import com.vop.arwalks.R;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Point;

/**
 * preview of current traject
 * 
 * @author gbostoen
 * 
 */
public class TrajectOpslaanZien extends MapActivity implements
		com.vop.tools.LocationListener {
	private MapController mapController;
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private punten_overlay itemizedoverlay;
	private Drawable draw;
	private VopApplication app;

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (VopApplication) getApplicationContext();
		// int walk_id = getIntent().getIntExtra("walk_id", 0); // nummer van
		// traject
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.locatiesmap_layout);

		this.mapView = (MapView) findViewById(R.id.myMapView);
		this.mapController = this.mapView.getController();

		this.mapView.setBuiltInZoomControls(true);
		this.mapView.setSatellite(true);
		this.mapView.setStreetView(true);

		draw = this.getResources().getDrawable(R.drawable.androidmarker);

		initMap();
		// t = DBWrapper.getTraject(walk_id);

		if (app.getHuidige_walk() != null)
			drawPath(app.getHuidige_walk(), -65536);

	}

	/**
	 * initialize map
	 */
	private void initMap() {
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		this.mapView.getOverlays().add(myLocationOverlay);
		this.mapController.setZoom(19);
		this.mapController.animateTo(new GeoPoint((int) (app.getLat() * 1E6), (int) (app.getLng() * 1E6)));
		this.itemizedoverlay = new punten_overlay(this.draw);
		myLocationOverlay.enableMyLocation();
	}

	// door drawline functie in wandeling_overlay
	private void drawPath(ArrayList<Point> listOfPoints, int color) {

		List<Overlay> overlays = this.mapView.getOverlays();
		overlays.clear();
		ArrayList<GeoPoint> listOfGeoPoints = new ArrayList<GeoPoint>();
		for (int i = 0; i < listOfPoints.size(); i++) {
			listOfGeoPoints.add(new GeoPoint((int) (listOfPoints.get(i).getLatitute() * 1E6), (int) (listOfPoints.get(i).getLongitude() * 1E6)));

		}
		overlays.add(new WandelingOverlay2(listOfGeoPoints, color));
		overlays.add(myLocationOverlay);
		mapView.invalidate();
	}

	/**
	 * update traject on map when location changed
	 */
	public void locationUpdated() {
		double lat = app.getLat();
		double lng = app.getLng();

		GeoPoint punt = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		this.mapController.animateTo(punt);
		if (app.getHuidige_walk() != null)
			drawPath(app.getHuidige_walk(), -65536);
	}
}
