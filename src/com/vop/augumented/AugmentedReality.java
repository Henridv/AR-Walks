package com.vop.augumented;
import overlays.InfoView;
import overlays.Marker;
import overlays.OpenGLRenderer;
import overlays.Preview;

import com.vop.tools.VopApplication;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
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
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AugmentedReality extends Activity {
	static float r[];
	OpenGLRenderer renderer;
	InfoView infoview;
	double killfactor = 0.1;
	
	@Override
	public boolean onTouchEvent(final MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// Code on finger down
			float posX = ev.getX();
			float posY = ev.getY();

			if ((posX >= 0 && posX <= infoview.getMeasuredWidth())
					&& (posY >= 0 && posY <= 2 * infoview
							.getMeasuredHeight() / 10)) {
				// we are in the square
				Marker punt = infoview.getDichtste_punt();
				if (punt != null) {
					Toast toast = Toast.makeText(getApplicationContext(), ""
							+ punt.getTitel()+": "+punt.getInfo(), Toast.LENGTH_SHORT);
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
		infoview=new InfoView(getApplicationContext());

		Button btn = new Button(this);
		btn.setText("click me");
		view.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		//view.setEGLConfigChooser(true);

		view.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		view.setZOrderOnTop(true);

		layout.addView(img);
		layout.addView(infoview);
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
		sm.registerListener(sensorListener,
				SensorManager.SENSOR_ORIENTATION,
				SensorManager.SENSOR_DELAY_UI);
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
			app.construeer();
		}
	}

	float[] accelerometerValues;
	float[] magneticFieldValues;

	final SensorEventListener myAccelerometerListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent sensorEvent) {
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				if(accelerometerValues != null){
					accelerometerValues[0] = (float) (sensorEvent.values[0]*killfactor+accelerometerValues[0]*(1-killfactor)); 
					accelerometerValues[1] = (float) (sensorEvent.values[1]*killfactor+accelerometerValues[1]*(1-killfactor)); 
					accelerometerValues[2] = (float) (sensorEvent.values[2]*killfactor+accelerometerValues[2]*(1-killfactor)); 
				}
				else accelerometerValues = sensorEvent.values;
			} else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				if(magneticFieldValues != null){
					magneticFieldValues[0] = (float) (sensorEvent.values[0]*killfactor+magneticFieldValues[0]*(1-killfactor)); 
					magneticFieldValues[1] = (float) (sensorEvent.values[1]*killfactor+magneticFieldValues[1]*(1-killfactor)); 
					magneticFieldValues[2] = (float) (sensorEvent.values[2]*killfactor+magneticFieldValues[2]*(1-killfactor)); 
				}
				else magneticFieldValues = sensorEvent.values;
			}
			if (magneticFieldValues != null && accelerometerValues != null) {
				VopApplication app = (VopApplication) getApplicationContext();
				r = new float[16];
				//float outr[] = new float[16];
				SensorManager.getRotationMatrix(r, null, accelerometerValues,
						magneticFieldValues);

				float[] outR = new float[16];
				SensorManager.remapCoordinateSystem(r, SensorManager.AXIS_Y,
						SensorManager.AXIS_MINUS_X, outR);
				app.setValues(outR);
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
	private final SensorListener sensorListener = new SensorListener() {
		public void onSensorChanged(int sensor, float[] values) {
			VopApplication app = (VopApplication) getApplicationContext();
			app.setRoll(values[0]);
			infoview.invalidate();
						
			// TODO Apply the orientation changes to your application.
		}

		public void onAccuracyChanged(int sensor, int accuracy) {
		}
	};
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
			Intent myIntent = new Intent(AugmentedReality.this, locatie_map2.class);
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
			myIntent = new Intent(AugmentedReality.this, Locatie_opslaan.class);
			AugmentedReality.this.startActivity(myIntent);
			return true;
		case R.id.refresh:
			app.construeer();
			return true;
		case R.id.lijstloc:
			myIntent = new Intent(AugmentedReality.this, ListView_Locaties.class);
			AugmentedReality.this.startActivity(myIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
