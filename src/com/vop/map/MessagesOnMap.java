package com.vop.map;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.vop.arwalks.AddMessage;
import com.vop.arwalks.R;
import com.vop.map.overlays.CustomOverlayItem;
import com.vop.map.overlays.ImageOverlay;
import com.vop.tools.DBWrapper;
import com.vop.tools.LocationListener;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Location;

/**
 * show all messages
 * 
 * @author Niels
 * 
 */
public class MessagesOnMap extends MapActivity implements LocationListener {
	
	private MapController mapController;
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private VopApplication app;
	private ImageOverlay itemizedoverlay;

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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.locations_on_map);
		this.mapView = (MapView) findViewById(R.id.myMapView);
		this.mapController = this.mapView.getController();

		this.mapView.setBuiltInZoomControls(true);
		this.mapView.setSatellite(false);
		
		int personId = Integer.parseInt(app.getState().get("userid"));
		//glenn
		Drawable drawable1 = this.getResources().getDrawable(R.drawable.androidmarker);
		itemizedoverlay=new ImageOverlay(drawable1,this);
		
		initMap();
		ArrayList<Location> locations = DBWrapper.getLocations(personId);
		for(int i=0;i<locations.size();i++){
			drawImage(locations.get(i));
		}
		mapView.getOverlays().add(itemizedoverlay);
	}
	private void refreshMap() {
		mapView.getOverlays().clear();
		mapView.getOverlays().add(myLocationOverlay);
		int personId = Integer.parseInt(app.getState().get("userid"));
		ArrayList<Location> locations = DBWrapper.getLocations(personId);
		for(int i=0;i<locations.size();i++){
			drawImage(locations.get(i));
		}
		mapView.getOverlays().add(itemizedoverlay);
		mapView.invalidate();
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


	// door drawline functie in wandeling_overlay
	private void drawImage(Location l) {
		//List<Overlay> overlays = this.mapView.getOverlays();

		//overlays.add(new ImageOverlay(l));
		
		GeoPoint punt = new GeoPoint((int) (l.getLatitute() * 1E6), (int) (l.getLongitude() * 1E6));
		CustomOverlayItem overlayitem = new CustomOverlayItem(punt, l);
		setDrawable(overlayitem, l);
		itemizedoverlay.addOverlay(overlayitem);
	}
	
	private void setDrawable(OverlayItem item,Location l){
		FileInputStream in;
		Bitmap markerImage = null;
		try {
			in = new FileInputStream(l.getImg());
			BufferedInputStream buf = new BufferedInputStream(in);
	        markerImage = BitmapFactory.decodeStream(buf);
	        if (in != null) {
	         	in.close();
	        }
	            if (buf != null) {
	         	buf.close();
	            }
		} catch (Exception e) {}
		if(markerImage!=null){
			markerImage = android.graphics.Bitmap.createScaledBitmap(markerImage, 64, 48, true);
			Drawable d=new BitmapDrawable(markerImage);
			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			item.setMarker(d);
		}
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.messages, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.message_post:
			Intent myIntent = new Intent(MessagesOnMap.this, AddMessage.class);
			myIntent.putExtra("type", "locations");
			MessagesOnMap.this.startActivity(myIntent);
			refreshMap();
			return true;
		case R.id.message_update:
			refreshMap();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
