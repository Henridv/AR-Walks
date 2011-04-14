package com.vop.augumented;

import java.util.ArrayList;
import java.util.Collections;

import com.vop.tools.DBWrapper;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;
import com.vop.tools.data.Point;
import com.vop.tools.data.Traject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationManager;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.Toast;

public class TrajectOpslaan extends Activity {
	VopApplication app;
	private ArrayList<Point> walk;
	LocationManager locationManager;
	


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//standaard
		super.onCreate(savedInstanceState);
		app = (VopApplication) getApplicationContext();
		
		// Remove the title bar from the window.
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// Make the windows into full screen mode.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.trajectopslaan_layout);
		
		//initialiseren
		walk = new ArrayList<Point>();
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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
			Button knop = (Button) findViewById(R.id.startstop) ;
			if(knop.getText().equals("stop")){
				Toast toast = Toast.makeText(getApplicationContext(), "velden invullen", Toast.LENGTH_SHORT);
				toast.show();
				walk.add(new Point(location.getLatitude(), location.getLatitude(), location.getAltitude()));
			}
		}
	}
	//knop
	public void startstop(View v) {
		Button knop = (Button) findViewById(R.id.startstop) ;
		TextView veld1 = (TextView) findViewById(R.id.naam);
		TextView veld2 = (TextView) findViewById(R.id.info);
		if(knop.getText().equals("start")){
			if(veld1.getText().length()  > 0 && veld2.getText().length()> 0){
				knop.setText("stop");
				v.invalidate();
				
				//locationmanagement
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
			}
			else{
				Toast toast = Toast.makeText(getApplicationContext(), "velden invullen", Toast.LENGTH_SHORT);
				toast.show();
			}
			
		}
		else{
			//Person persoon = new Person(2,"Henri De Veene", "", "test", "henri.deveene@gmail.com");
			Traject traject=new Traject(veld1.getText().toString(), DBWrapper.getProfile(Integer.parseInt(app.getState().get("userid"))), walk);
			DBWrapper.save(traject);
			finish();
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
		locationManager.removeUpdates(locationListener);
	}
}
