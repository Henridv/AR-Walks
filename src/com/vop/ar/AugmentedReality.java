package com.vop.ar;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.vop.ar.overlays.CameraOverlay;
import com.vop.ar.overlays.InfoView;
import com.vop.ar.overlays.LocationRenderer;
import com.vop.ar.overlays.TrackRenderer;
import com.vop.arwalks.ListLocaties;
import com.vop.arwalks.R;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.LocationListener;
import com.vop.tools.VopApplication;

/**
 * This class sets up the environment for the augmented reality
 * 
 * @author henridv
 * 
 */
public class AugmentedReality extends FullscreenActivity implements LocationListener {
	float[] accelerometerValues = null;
	float[] magneticFieldValues = null;
	Renderer openGLRenderer;
	InfoView infoView;
	VopApplication app;
	SensorManager sensorManager;
	Sensor accelSensor;
	Sensor magneticSensor;

	final SensorEventListener sensorListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			double killfactor = 0.075;
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
				} else
					accelerometerValues = event.values.clone();
				break;

			case Sensor.TYPE_MAGNETIC_FIELD:
				if (magneticFieldValues != null) {
					magneticFieldValues[0] = (float) (event.values[0]
							* killfactor + magneticFieldValues[0]
							* (1 - killfactor));
					magneticFieldValues[1] = (float) (event.values[1]
							* killfactor + magneticFieldValues[1]
							* (1 - killfactor));
					magneticFieldValues[2] = (float) (event.values[2]
							* killfactor + magneticFieldValues[2]
							* (1 - killfactor));
				} else
					magneticFieldValues = event.values.clone();
				break;

			default:
				break;
			}

			if (magneticFieldValues != null && accelerometerValues != null) {
				// use 4x4 matrix which can be used for OpenGL purposes
				float[] rotationMatrix = new float[16];
				if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magneticFieldValues)) {
					float[] outR = new float[16];
					SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, outR);
					app.setRotationMatrix(outR);

					// calculate azimuth
					float[] orientation = new float[3];
					SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR);
					SensorManager.getOrientation(outR, orientation);
					app.setAzimuth((float) (Math.toDegrees(orientation[0]) + 360f) % 360);
					app.setPitch((float) (Math.toDegrees(orientation[1]) + 360f) % 360);
					app.setRoll((float) (Math.toDegrees(orientation[2]) + 360f) % 360);
					infoView.invalidate();
				}
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String type = getIntent().getExtras().get("type").toString();
		if (type == null) type = "locations";

		app = (VopApplication) getApplicationContext();

		RelativeLayout layout = new RelativeLayout(this);

		// Camera overlay
		CameraOverlay cameraOverlay = new CameraOverlay(this);

		// Info overlay
		infoView = new InfoView(this);

		// openGL overlay
		GLSurfaceView glSurfaceView = new GLSurfaceView(this);
		glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

		// make surface transparent and on top
		glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		glSurfaceView.setZOrderOnTop(true);

		// adding overlays to the screen
		layout.addView(cameraOverlay);
		layout.addView(glSurfaceView);

		// define sensors
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		// Creating and attaching the renderer.
		if (type.equals("locations")) {
			openGLRenderer = new LocationRenderer(this);
			layout.addView(infoView);
		} else if (type.equals("track")) {
			openGLRenderer = new TrackRenderer(this);
		}
		glSurfaceView.setRenderer(openGLRenderer);
		setContentView(layout);
	}

	/**
	 * Set listeners
	 */
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(sensorListener, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(sensorListener, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
		app.addLocationListener(this);
	}

	/**
	 * Remove listeners
	 */
	@Override
	protected void onStop() {
		super.onStop();
		sensorManager.unregisterListener(sensorListener);
		app.removeLocationListener(this);
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
			Intent myIntent = new Intent(AugmentedReality.this, LocatieMap.class);
			AugmentedReality.this.startActivity(myIntent);
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
			myIntent = new Intent(AugmentedReality.this, SaveLocation.class);
			AugmentedReality.this.startActivity(myIntent);
			return true;
		case R.id.refresh:
			app.construeer();
			return true;
		case R.id.lijstloc:
			myIntent = new Intent(AugmentedReality.this, ListLocaties.class);
			AugmentedReality.this.startActivity(myIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void locationUpdated() {
		app.construeer();
	}
}
