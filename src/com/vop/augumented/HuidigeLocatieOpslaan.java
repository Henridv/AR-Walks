package com.vop.augumented;

import com.vop.tools.VopApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * save current location
 * @author gbostoen
 *
 */

public class HuidigeLocatieOpslaan extends Activity{
	VopApplication app;
	Button knop;
	Intent intent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.huidigelocatielayout);
		app = (VopApplication) getApplicationContext();
		knop = (Button) findViewById(R.id.widget27) ;
		if(app.getState().get("starthuidig") ==null) knop.setText("start");
		else knop.setText("stop");
		intent=new Intent(this,LocationService.class);
	}
	/**
	 * action when button pressed
	 * @param v
	 */
	public void go_klik(View v) {
		if(app.getState().get("starthuidig") == null ){
			knop.setText("stop");
			v.invalidate();
			app.putState("starthuidig", "hello");
			startService(intent);
		}
		else{
			knop.setText("start");
			v.invalidate();
			stopService(intent);
			app.getState().remove("starthuidig");
		}
	}
	
}
