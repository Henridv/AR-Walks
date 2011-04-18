package com.vop.augumented;

import android.app.AlertDialog;
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
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vop.overlays.CameraOverlay;
import com.vop.overlays.InfoView;
import com.vop.overlays.Marker;
import com.vop.overlays.OpenGLRenderer;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;

/**
 * This class sets up the environment for the augmented reality
 * 
 * @author henridv
 * 
 */
public class AugmentedRealityLocaties extends FullscreenActivity {
	float[] accelerometerValues = null;
	float[] magneticFieldValues = null;
	OpenGLRenderer openGLRenderer;
	InfoView infoView;
	VopApplication app;
	SensorManager sm;
	LocationManager locationManager;
	Sensor accelSensor;
	Sensor magneticSensor;
	Sensor orientationSensor;
	String provider;

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

	final SensorEventListener sensorListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			double killfactor = 0.1;
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				if (accelerometerValues != null) {
					accelerometerValues[0] = (float) (event.values[0]
							* killfactor + accelerometerValues[0]
							* (1 - killfactor));
					accelerometerValues[1] = (float) (event.values[1]
							* killfactor + accelerometerValues[1]
							* (1 - killfactor));
					accelerometerValues[2] = (float) (event.values[2]
							* killfactor + accelerometerValues[2]
							* (1 - killfactor));
				} else {
					accelerometerValues = event.values;
				}
				break;
				
			case Sensor.TYPE_MAGNETIC_FIELD:
				if (magneticFieldValues != null) {
					magneticFieldValues[0] = (float) (event.values[0] * killfactor
							+ magneticFieldValues[0] * (1 - killfactor));
					magneticFieldValues[1] = (float) (event.values[1] * killfactor
							+ magneticFieldValues[1] * (1 - killfactor));
					magneticFieldValues[2] = (float) (event.values[2] * killfactor
							+ magneticFieldValues[2] * (1 - killfactor));
				} else {
					magneticFieldValues = event.values;
				}
				break;
				
			case Sensor.TYPE_ORIENTATION:
				//app.setAzimuth(event.values[0]);
				infoView.invalidate();
				break;
			}

			if (magneticFieldValues != null && accelerometerValues != null) {
				float[] rotationMatrix = new float[9];
				if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magneticFieldValues)) {
					float[] outR = new float[9];
					SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Z, SensorManager.AXIS_MINUS_X, outR);
					app.setValues(outR);
	
//					float[] looking = {1.0f, 0.0f, 0.0f};
//					Vector l = new Vector(looking);
//					Matrix m = new Matrix(outR, 3, 3);
//					m.transpose();
//					l = Matrix.mult(m, l);
//					float d = (float) Math.sqrt(l.get(0) * l.get(0) + l.get(1) * l.get(1));
//					float cos = l.get(1) / d;
//					float angle = (float) Math.toDegrees(Math.acos(cos));
//					angle = (l.get(0) < 0) ? angle * -1 : angle;
					
					float[] orientation = new float[3];
					SensorManager.getOrientation(outR, orientation);
					app.setAzimuth((float) (Math.toDegrees(orientation[0]) + 360f)%360);
					app.setPitch((float) (Math.toDegrees(orientation[1]) + 360f)%360);
					app.setRoll((float) (Math.toDegrees(orientation[2]) + 360f)%360);
					
//					app.setAzimuth((angle + 360f)%360);
				}
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (VopApplication) getApplicationContext();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(true);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		provider = locationManager.getBestProvider(criteria, true);
		if (provider == null) {
			provider = locationManager.getBestProvider(criteria, false);
			Toast.makeText(this, "Please, turn on GPS", Toast.LENGTH_SHORT).show();
			startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
		}
		Location location = locationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);

		RelativeLayout layout = new RelativeLayout(this);

		// Camera overlay
		CameraOverlay cameraOverlay = new CameraOverlay(this);

		// Info overlay
		infoView = new InfoView(getApplicationContext());

		// openGL overlay
		GLSurfaceView glSurfaceView = new GLSurfaceView(this);

		glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		// glSurfaceView.setEGLConfigChooser(true);
		// glSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_LOG_GL_CALLS);

		// make surface transparent and on top
		glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		glSurfaceView.setZOrderOnTop(true);

		// adding overlays to the screen
		layout.addView(cameraOverlay);
		layout.addView(infoView);
		layout.addView(glSurfaceView);

		// define sensors
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magneticSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		orientationSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		// Creating and attaching the renderer.
		openGLRenderer = new OpenGLRenderer(this);
		glSurfaceView.setRenderer(openGLRenderer);
		setContentView(layout);
	}

	/**
	 * Set listeners
	 */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 2, 10, locationListener);
		sm.registerListener(sensorListener, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
		sm.registerListener(sensorListener, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
		sm.registerListener(sensorListener, orientationSensor, SensorManager.SENSOR_DELAY_UI);
	}

	/**
	 * Remove listeners
	 */
	@Override
	protected void onStop() {
		super.onStop();
		sm.unregisterListener(sensorListener);
		locationManager.removeUpdates(locationListener);
	}

	/**
	 * 
	 * @param location
	 */
	private void updateWithNewLocation(Location location) {
		if (location != null) {
			app.setAlt(location.getAltitude());
			app.setLng(location.getLongitude());
			app.setLat(location.getLatitude());
			app.construeer();
		}
	}

	@Override
	public boolean onTouchEvent(final MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// get position of touch
			float posX = ev.getX();
			float posY = ev.getY();

			if ((posX >= 0 && posX <= infoView.getMeasuredWidth())
					&& (posY >= 0 && posY <= 2 * infoView.getMeasuredHeight() / 10)) {
				// we are in the square
				Marker punt = infoView.getDichtste_punt();
				if (punt != null) {
					Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					vibrator.vibrate(60);
					AlertDialog.Builder dialog = new AlertDialog.Builder(this);
					dialog.setTitle(punt.getTitel());
					dialog.setMessage(punt.getInfo());
					dialog.show();
				}

			} else {

				// we are somewhere else on the canvas
			}
			break;
		default:
			break;
		}
		return true;
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
			Intent myIntent = new Intent(AugmentedRealityLocaties.this, LocatieMap.class);
			AugmentedRealityLocaties.this.startActivity(myIntent);
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
			myIntent = new Intent(AugmentedRealityLocaties.this, LocatieOpslaan.class);
			AugmentedRealityLocaties.this.startActivity(myIntent);
			return true;
		case R.id.refresh:
			app.construeer();
			return true;
		case R.id.lijstloc:
			myIntent = new Intent(AugmentedRealityLocaties.this, ListLocaties.class);
			AugmentedRealityLocaties.this.startActivity(myIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
