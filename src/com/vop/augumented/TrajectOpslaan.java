package com.vop.augumented;

import com.vop.tools.VopApplication;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




public class TrajectOpslaan extends Activity {
	VopApplication app;
	Intent intent;
	Button knop;
	String ns;
	NotificationManager mNotificationManager;
	int icon;
	CharSequence tickerText;
	long when;
	private static final int HELLO_ID = 1;
	Notification notification;
	
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//standaard
		super.onCreate(savedInstanceState);
		app = (VopApplication) getApplicationContext();
		
		//initialisatie van notification
		ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);
		icon = R.drawable.appicon;
		tickerText = "[REC]";
		when = System.currentTimeMillis();
		notification = new Notification(icon, tickerText, when);
		Context context = getApplicationContext();
		CharSequence contentTitle = "AR WALKS";
		CharSequence contentText = "traject wordt opgenomen!";
		Intent notificationIntent = new Intent(this, TrajectOpslaan.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.flags=Notification.FLAG_NO_CLEAR;
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		
		
		
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
				
				//notification manager starten
				mNotificationManager.notify(HELLO_ID, notification);

			}
			else{
				Toast toast = Toast.makeText(getApplicationContext(), "velden invullen", Toast.LENGTH_SHORT);
				toast.show();
			}
			
		}
		else{
			stopService(intent);
			app.getState().remove("startstop");
			
			//notificatie beindigen
			mNotificationManager.cancel(HELLO_ID);
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
