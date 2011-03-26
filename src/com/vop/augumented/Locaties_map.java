package com.vop.augumented;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.vop.tools.VopApplication;

public class Locaties_map extends MapActivity {
	LocationManager locationManager;
	String provider, context;
	Location location;
	MapController mapController;
	MapView mapView;
	int minDistance = 0;
	int minTime = 1000;
	MyLocationOverlay myLocationOverlay;
	List<Overlay> mapOverlays;
	punten_overlay itemizedoverlay;

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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locatiesmap_layout);

		this.mapView = (MapView) findViewById(R.id.myMapView);
		this.mapController = this.mapView.getController();

		this.mapView.setSatellite(true);
		this.mapView.setStreetView(true);
		this.mapView.displayZoomControls(true);
		
		mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		itemizedoverlay = new punten_overlay(drawable,this);

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
		locationManager.requestLocationUpdates(provider, minTime, minDistance,
				locationListener);
	}

	private void updateWithNewLocation(Location location) {

		String latLong;
		TextView myLocationText;

		myLocationText = (TextView) findViewById(R.id.myLocationText);
		String adresStr = "Geen adres gevonden";

		if (location != null) {

			double lat = location.getLatitude();
			double lng = location.getLongitude();
			GeoPoint punt = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			this.mapController.animateTo(punt);
			latLong = "Lat: " + lat + "\nLng: " + lng;
			Geocoder gc = new Geocoder(this, Locale.getDefault());
			try {
				List<Address> adressen = gc.getFromLocation(lat, lng, 1);
				StringBuilder sb = new StringBuilder();
				if (adressen.size() > 0) {
					Address adres = adressen.get(0);

					for (int i = 0; i < adres.getMaxAddressLineIndex(); i++) {
						sb.append(adres.getAddressLine(i)).append("\n");
					}
					sb.append(adres.getCountryName());
				}

				adresStr = sb.toString();
			} catch (IOException e) {
			}

		} else
			latLong = "Geen current location";
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private void initMap() {
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
		for(int i=0;i<POI.length;i++){
			GeoPoint punt = new GeoPoint((int) (POI[i].getLat() *1E6), (int) (POI[i].getLng() *1E6));
			OverlayItem overlayitem = new OverlayItem(punt, POI[i].getTitel(), POI[i].getTitel());
			itemizedoverlay.addOverlay(overlayitem);
		}
		mapOverlays.add(itemizedoverlay);
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
		switch (item.getItemId()) {

		case R.id.augmentedView:
			Intent myIntent = new Intent(Locaties_map.this, Locaties.class);
			Locaties_map.this.startActivity(myIntent);
			return true;
		case R.id.opslaan:
			myIntent = new Intent(Locaties_map.this, Locatie_opslaan.class);
			Locaties_map.this.startActivity(myIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}