package com.vop.augumented;

import java.util.ArrayList;

import com.vop.tools.DBWrapper;
import com.vop.tools.VopApplication;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TutorialPartI extends Activity {
	static float r[];
	OpenGLRenderer renderer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String context = Context.LOCATION_SERVICE;
		LocationManager locationManager = (LocationManager) getSystemService(context);

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

		// Remove the title bar from the window.
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Make the windows into full screen mode.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Create a OpenGL view.
		RelativeLayout layout = new RelativeLayout(this);
		GLSurfaceView view = new GLSurfaceView(this);

		Preview img = new Preview(this);

		Button btn = new Button(this);
		btn.setText("click me");
		view.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

		view.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		view.setZOrderOnTop(true);

		layout.addView(img);
		layout.addView(view);

		// Creating and attaching the renderer.
		renderer = new OpenGLRenderer(this);
		view.setRenderer(renderer);
		setContentView(layout);

		// sensors
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor mfSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sm.registerListener(myAccelerometerListener, aSensor,
				SensorManager.SENSOR_DELAY_FASTEST);
		sm.registerListener(myAccelerometerListener, mfSensor,
				SensorManager.SENSOR_DELAY_FASTEST);
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
			construeer();
		}
	}

	float[] accelerometerValues;
	float[] magneticFieldValues;

	final SensorEventListener myAccelerometerListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent sensorEvent) {
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				accelerometerValues = sensorEvent.values;
			} else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				magneticFieldValues = sensorEvent.values;
			}
			if (magneticFieldValues != null && accelerometerValues != null) {
				r = new float[16];
				SensorManager.getRotationMatrix(r, null, accelerometerValues,
						magneticFieldValues);
				VopApplication app = (VopApplication) getApplicationContext();
				float[] values = new float[3];
				SensorManager.getOrientation(r, values);
				values[0] = (float) Math.toDegrees(values[0]);
				values[1] = (float) Math.toDegrees(values[1]);
				values[2] = (float) Math.toDegrees(values[2]);
				app.setRoll(0);
				float[] outR = new float[16];
				SensorManager.remapCoordinateSystem(r, SensorManager.AXIS_Y,
						SensorManager.AXIS_MINUS_X, outR);
				app.setValues(outR);
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	public void construeer() {
		VopApplication app = (VopApplication) getApplicationContext();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.locaties_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		VopApplication app = (VopApplication) getApplicationContext();
		switch (item.getItemId()) {
		case R.id.kaart:
			Intent myIntent = new Intent(TutorialPartI.this, locatie_map2.class);
			TutorialPartI.this.startActivity(myIntent);
			finish();
			return true;
		case R.id.km_1:
			if (item.isChecked())
				item.setChecked(true);
			else
				item.setChecked(false);
			app.setMax_afstand(1000);
			return true;
		case R.id.km_5:
			if (item.isChecked())
				item.setChecked(true);
			else
				item.setChecked(false);
			app.setMax_afstand(5000);
			return true;
		case R.id.km_10:
			if (item.isChecked())
				item.setChecked(true);
			else
				item.setChecked(false);
			app.setMax_afstand(10000);
			return true;
		case R.id.km_20:
			if (item.isChecked())
				item.setChecked(true);
			else
				item.setChecked(false);
			app.setMax_afstand(20000);
			return true;
		case R.id.m_500:
			if (item.isChecked())
				item.setChecked(true);
			else
				item.setChecked(false);
			app.setMax_afstand(500);
			return true;
		case R.id.opslaan:
			myIntent = new Intent(TutorialPartI.this, Locatie_opslaan.class);
			TutorialPartI.this.startActivity(myIntent);
			return true;
		case R.id.refresh:
			construeer();
			return true;
		case R.id.lijstloc:
			myIntent = new Intent(TutorialPartI.this, ListView_Locaties.class);
			TutorialPartI.this.startActivity(myIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
