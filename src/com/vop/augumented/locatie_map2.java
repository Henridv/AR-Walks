package com.vop.augumented;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



import com.vop.overlays.Marker;
import com.vop.overlays.punten_overlay;
import com.vop.overlays.wandeling_overlay;

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
import com.vop.overlays.Marker;
import com.vop.overlays.punten_overlay;
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
	ArrayList<Traject> walks;
	Drawable draw;
	
	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			//updateWithNewLocation(location);
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.locatiesmap_layout);
		
		this.mapView =(MapView) findViewById(R.id.myMapView);
		this.mapController = this.mapView.getController();
		
		this.mapView.setBuiltInZoomControls(true);
		this.mapView.setSatellite(true);
		this.mapView.setStreetView(true);
		
		draw = this.getResources().getDrawable(R.drawable.androidmarker);
		
		try{
			startLocationListening();
		}catch(Exception e){
			Toast toast = Toast.makeText(content,
					"Er is iets fout gegaan bij het ophalen van de locatie", Toast.LENGTH_SHORT);
			toast.show();
			Log.w("Er is iets fout gegaan bij het ophalen van de locatie",e.getMessage());
		}
		
		//haal alle trajecten binnen
		this.walks = DBWrapper.getTrajects();
		try{
			initMap();
		} catch(Exception e){
			Toast toast = Toast.makeText(content,
					"Er is iets fout gegaan bij het initialiseren van de map", Toast.LENGTH_SHORT);
			toast.show();
			Log.w("Er is iets fout gegaan bij het initialiseren van de map",e.getMessage());
		}
		
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
		this.mapController.setZoom(17);
		this.itemizedoverlay = new punten_overlay(this.draw);
		showTrajectsOnMap();
		//moet hier animateTo huidige locatie bij? irrelevant atm
		//updateWithNewLocation(this.location);
	}
	
	
	//////////////////////////////////////
	//				FUNCTIES 			//
	//////////////////////////////////////
	
	@SuppressWarnings("unchecked") //door drawline functie in wandeling_overlay
	private void drawPath(ArrayList<Point>listOfPoints, int color){
		List overlays = this.mapView.getOverlays();
		ArrayList<GeoPoint> listOfGeoPoints = new ArrayList<GeoPoint>();
		for(int i=0;i<listOfPoints.size();i++){
			listOfGeoPoints.add(
					new GeoPoint((int)(listOfPoints.get(i).getLatitute()*1E6),
							     (int)(listOfPoints.get(i).getLongitude()*1E6)));
		}
		for(int j=0;j<(listOfGeoPoints.size()-1);j++){
			overlays.add(new wandeling_overlay(listOfGeoPoints.get(j),listOfGeoPoints.get(j+1),color));
		}
	}
	//twee verschillende methodes - eerste niet gecheckt maar met verbinding tussen punten
	private void showTrajectsOnMap(){
		for(int i=0;i<this.walks.size();i++){
			drawPath(this.walks.get(2).getWalk(),-65536);
		}
		
	}
	
	private void showTrajectsOnMap2(){
		Traject temp;
		ArrayList<Point> move;
		Iterator<Point> it;
		Point punt;
		GeoPoint geoPunt;
		ArrayList<Marker> allePunten = new ArrayList<Marker>();
		Marker marker;
		OverlayItem item;
		for(int i=0;i<this.walks.size();i++){
			temp = this.walks.get(i);
			move = temp.getWalk();
			it = move.iterator();
			
			while(it.hasNext()){
				punt = it.next();
				allePunten.add(new Marker("Punt "+i,"Punt van wandeling",
						punt.getLongitude(),punt.getLatitute(),punt.getAltitude(),content));
				}
		}
			for (int j=0;j<allePunten.size();j++){
				marker = allePunten.get(j);
				geoPunt = new GeoPoint((int)(marker.getLat()*1E6),(int)(marker.getLng()*1E6));
				item = new OverlayItem(geoPunt,marker.getTitel(),marker.getInfo());
				itemizedoverlay.addOverlay(item);
			}
		this.mapView.getOverlays().add(itemizedoverlay);
		System.out.println("useless, enkel voor breakpoint");
		
	}
	
	private void updateWithNewLocation(Location location){
		if(location!=null){

			double lat = location.getLatitude();
			double lng = location.getLongitude();
			//double alt = location.getAltitude();
			
			GeoPoint punt = new GeoPoint((int) (lat*1E6),(int)(lng*1E6));
			this.mapController.animateTo(punt);
		}
	}
	
	
}
