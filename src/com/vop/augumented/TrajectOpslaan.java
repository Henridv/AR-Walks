package com.vop.augumented;

import java.util.ArrayList;
import java.util.Collections;

import com.vop.tools.DBWrapper;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;
import com.vop.tools.data.Point;
import com.vop.tools.data.Traject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
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
	LocationManager locationManager;
	Criteria criteria = new Criteria();
	int aantal=0;
	Intent intent;
	Button knop;
	

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
		knop = (Button) findViewById(R.id.startstop) ;
		if(app.getState().get("startstop") ==null) knop.setText("start");
		else knop.setText("stop");
		intent=new Intent(this,TrajectService.class);
		
	}

	//knop
	public void startstop(View v) {
		TextView veld1 = (TextView) findViewById(R.id.naam);
		TextView veld2 = (TextView) findViewById(R.id.info);
		if(app.getState().get("startstop") == null ){
			if(veld1.getText().length()  > 0 && veld2.getText().length()> 0){
				knop.setText("stop");
				v.invalidate();
				app.putState("naamtraject", veld1.getText().toString());
				app.putState("infotraject", veld2.getText().toString());
				startService(intent);
				app.putState("startstop", "hello");
			}
			else{
				Toast toast = Toast.makeText(getApplicationContext(), "velden invullen", Toast.LENGTH_SHORT);
				toast.show();
			}
			
		}
		else{
			stopService(intent);
			app.getState().remove("startstop");
			finish();
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
}
