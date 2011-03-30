package com.vop.augumented;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;

public class TutorialPartI extends Activity {

	private Boolean first_1 = true;
	private Boolean first_2 = true;
	static float r[];
	static float values[];
	static Bitmap foto;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GLSurfaceView view = new GLSurfaceView(this);
		OpenGLRenderer renderer = new OpenGLRenderer(this);
		view.setRenderer(renderer);
		setContentView(view);

		//sensors
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor mfSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sm.registerListener(myAccelerometerListener, aSensor,
				SensorManager.SENSOR_DELAY_GAME);
		sm.registerListener(myMagneticFieldListener, mfSensor,
				SensorManager.SENSOR_DELAY_GAME);
		
	}

	float[] accelerometerValues;
	float[] magneticFieldValues;
	final SensorEventListener myAccelerometerListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent sensorEvent) {
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				accelerometerValues = sensorEvent.values;
				first_1 = false;
			}
			if (!first_2) {

			}
			values = new float[16];
			r = new float[16];
			SensorManager.getRotationMatrix(r, null, accelerometerValues,
					magneticFieldValues);
			float[] outR = new float[16];
			SensorManager.remapCoordinateSystem(r, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, outR);
			values = outR;

		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
	final SensorEventListener myMagneticFieldListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent sensorEvent) {
			if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				magneticFieldValues = sensorEvent.values;
				first_2 = false;
			}

		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
}