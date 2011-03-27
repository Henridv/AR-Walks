package com.vop.augumented;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package com.example.android.apis.graphics;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.vop.tools.VopApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
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
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class Locaties extends Activity {
	private Preview mPreview;
	AugView compassView;
	SensorManager sensorManager;
	LocationManager locationManager;

	@Override
	public boolean onTouchEvent(final MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// Code on finger down
			float posX = ev.getX();
			float posY = ev.getY();

			if ((posX >= 0 && posX <= compassView.getMeasuredWidth())
					&& (posY >= 0 && posY <= 2 * compassView
							.getMeasuredHeight() / 10)) {
				// we are in the square
				String tekst = compassView.getDichtste_punt();
				if (tekst != "") {
					Toast toast = Toast.makeText(getApplicationContext(), ""
							+ tekst, Toast.LENGTH_SHORT);
					toast.show();
				}

			} else {

				// we are somewhere else on the canvas
			}
		}
		case MotionEvent.ACTION_UP: {
			// Code on finger up

		}
		case MotionEvent.ACTION_MOVE: {
			// Code on finger move

		}
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Marker.setAfstand(1000000);
		super.onCreate(savedInstanceState);

		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Create our Preview view and set it as the content of our activity.

		mPreview = new Preview(this);
		compassView = new AugView(getApplicationContext());

		setContentView(mPreview);
		addContentView(compassView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		updateOrientation(0, 0, 0);

		String context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(context);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(provider, 2, 10,
				locationListener);
		VopApplication app = (VopApplication) getApplicationContext();
		if (location != null) {
			app.setAlt(location.getAltitude());
			app.setLng(location.getLongitude());
			app.setLat(location.getLatitude());
		}
		app.putState("first", "true");

	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private void updateWithNewLocation(Location location) {
		if (location != null) {
			VopApplication app = (VopApplication) getApplicationContext();
			app.setAlt(location.getAltitude());
			app.setLng(location.getLongitude());
			app.setLat(location.getLatitude());
			Geocoder gc = new Geocoder(getApplicationContext(),
					Locale.getDefault());
			try {
				List<Address> addresses = gc.getFromLocation(
						location.getLatitude(), location.getLongitude(), 1);
				Address adres = addresses.get(0);
				if (adres.getThoroughfare() != null)
					app.putState("adres", adres.getThoroughfare());
				else
					app.putState("adres", "null");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), "locatie geupdated",
					Toast.LENGTH_SHORT).show();
			compassView.invalidate();
		}
	}

	private void updateOrientation(float _roll, float _pitch, float _heading) {
		if (compassView != null) {
			VopApplication app = (VopApplication) getApplicationContext();
			app.setHeading(_heading);
			app.setRoll(_roll);
			app.setPitch(_pitch);
			compassView.invalidate();
		}
	}

	private final SensorListener sensorListener = new SensorListener() {
		public void onSensorChanged(int sensor, float[] values) {

			float headingAngle = values[0];
			float pitchAngle = values[1];
			float rollAngle = values[2];

			updateOrientation(headingAngle, pitchAngle, rollAngle);
			// TODO Apply the orientation changes to your application.
		}

		public void onAccuracyChanged(int sensor, int accuracy) {
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(sensorListener,
				SensorManager.SENSOR_ORIENTATION,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onStop() {
		sensorManager.unregisterListener(sensorListener);
		locationManager.removeUpdates(locationListener);
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.locaties_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.kaart:
			Intent myIntent = new Intent(Locaties.this, Locaties_map.class);
			Locaties.this.startActivity(myIntent);
			finish();
			return true;
		case R.id.km_1:
			if (item.isChecked())
				item.setChecked(true);
			else
				item.setChecked(false);
			Marker.setAfstand(1000);
			return true;
		case R.id.km_5:
			if (item.isChecked())
				item.setChecked(true);
			else
				item.setChecked(false);
			Marker.setAfstand(5000);
			return true;
		case R.id.km_10:
			if (item.isChecked())
				item.setChecked(true);
			else
				item.setChecked(false);
			Marker.setAfstand(10000);
			return true;
		case R.id.km_20:
			if (item.isChecked())
				item.setChecked(true);
			else
				item.setChecked(false);
			Marker.setAfstand(20000);
			return true;
		case R.id.m_500:
			if (item.isChecked())
				item.setChecked(true);
			else
				item.setChecked(false);
			Marker.setAfstand(500);
			return true;
		case R.id.opslaan:
			myIntent = new Intent(Locaties.this, Locatie_opslaan.class);
			Locaties.this.startActivity(myIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
