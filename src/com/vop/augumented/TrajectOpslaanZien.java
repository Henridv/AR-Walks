package com.vop.augumented;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.vop.overlays.wandeling_overlay;
import com.vop.overlays.punten_overlay;
import com.vop.tools.DBWrapper;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Point;
import com.vop.tools.data.Traject;

public class TrajectOpslaanZien extends MapActivity {
	LocationManager locationManager;
	String provider, context;
	Location location;
	MapController mapController;
	MapView mapView;
	int minDistance = 1;
	int minTime = 1000;
	MyLocationOverlay myLocationOverlay;
	punten_overlay itemizedoverlay;
	static Context content;
	ArrayList<Traject> walks;
	Drawable draw;
	Traject t;
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
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	//////////////////////////////////////
	//			INITIALISATIE			//
	//////////////////////////////////////
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		content = getApplicationContext();
		super.onCreate(savedInstanceState);
		int walk_id = getIntent().getIntExtra("walk_id", 0); //nummer van traject
		app=(VopApplication) getApplicationContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.locatiesmap_layout);
		
		this.mapView =(MapView) findViewById(R.id.myMapView);
		this.mapController = this.mapView.getController();
		
		this.mapView.setBuiltInZoomControls(true);
		this.mapView.setSatellite(false);
		//this.mapView.set
		//this.mapView.setStreetView(true);
		
		draw = this.getResources().getDrawable(R.drawable.androidmarker);
		
		try{
			startLocationListening();
		}catch(Exception e){
			Toast toast = Toast.makeText(content,
					"Er is iets fout gegaan bij het ophalen van de locatie", Toast.LENGTH_SHORT);
			toast.show();
			Log.w("Foute start locationlistening",e.getMessage());
		}
		
		//haal alle trajecten binnen
		//this.walks = DBWrapper.getTrajects();
		initMap();
		
		drawPath(app.getHuidige_walk(), -65536);
		
		this.locationManager.requestLocationUpdates(provider, minTime, minDistance, locationListener);
	}
	
	//(BIJNA) AF - initialiseert locationlistening 
	private void startLocationListening(){
		this.context = Context.LOCATION_SERVICE;
		this.locationManager = (LocationManager) getSystemService(this.context);
		
		//eerst mag onze applicatie over de grootste resources beschikken, kunnen dit later altijd aanpassen
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(true);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);	
		
		this.provider = this.locationManager.getBestProvider(criteria,true);
		this.location = locationManager.getLastKnownLocation(provider);
		
		//updateWithNewLocation(location);
	}

	private void initMap(){
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		this.mapView.getOverlays().add(myLocationOverlay);
		this.mapController.setZoom(17);
		this.mapController.animateTo(new GeoPoint((int)(location.getLatitude()*1E6),(int)(location.getLongitude()*1E6)));
		this.itemizedoverlay = new punten_overlay(this.draw);
		myLocationOverlay.enableMyLocation();
		}
	
	
	//////////////////////////////////////
	//				FUNCTIES 			//
	//////////////////////////////////////
	
	@SuppressWarnings("unchecked") //door drawline functie in wandeling_overlay
	private void drawPath(ArrayList<Point>listOfPoints, int color){
		List overlays = this.mapView.getOverlays();
		overlays.clear();
		ArrayList<GeoPoint> listOfGeoPoints = new ArrayList<GeoPoint>();
		for(int i=0;i<listOfPoints.size();i++){
			listOfGeoPoints.add(
					new GeoPoint((int)(listOfPoints.get(i).getLatitute()*1E6),
							     (int)(listOfPoints.get(i).getLongitude()*1E6)));
					
		}
		for(int j=0;j<(listOfGeoPoints.size()-1);j++){
			overlays.add(new wandeling_overlay(listOfGeoPoints.get(j),listOfGeoPoints.get(j+1),color));
			
		}
		overlays.add(myLocationOverlay);
		mapView.invalidate();
	}

	private void updateWithNewLocation(Location location){
		if(location!=null){

			double lat = location.getLatitude();
			double lng = location.getLongitude();
			//double alt = location.getAltitude();
			
			GeoPoint punt = new GeoPoint((int) (lat*1E6),(int)(lng*1E6));
			this.mapController.animateTo(punt);
			drawPath(app.getHuidige_walk(), -65536);
		}
	}
	
}
