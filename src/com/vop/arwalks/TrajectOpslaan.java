package com.vop.arwalks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vop.services.TrackService;
import com.vop.tools.FullscreenActivity;

/**
 * save a traject
 * 
 * @author gbostoen
 * 
 */
public class TrajectOpslaan extends FullscreenActivity {
	private Intent trackService;
	private Button toggleButton;
	private NotificationManager mNotificationManager;
	private static final int HELLO_ID = 2;
	private Notification notification;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// init notification
		CharSequence tickerText = "[REC]";
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.appicon, tickerText, System.currentTimeMillis());
		Context context = getApplicationContext();
		CharSequence contentTitle = "AR WALKS";
		CharSequence contentText = "traject wordt opgenomen!";
		Intent notificationIntent = new Intent(this, TrajectOpslaan.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.flags = Notification.FLAG_NO_CLEAR;
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		setContentView(R.layout.trajectopslaan_layout);

		toggleButton = (Button) findViewById(R.id.toggleRecording);
		if (!TrackService.isRunning()) {
			toggleButton.setText("Start recording");
		} else {
			toggleButton.setText("Stop");
			TextView trackName = (TextView) findViewById(R.id.track_name);
			trackName.setEnabled(false);
			trackName.setText(TrackService.getName());
		}
		trackService = new Intent(this, TrackService.class);
	}

	/**
	 * start/stop action
	 * 
	 * @param v
	 */
	public void startstop(View v) {
		TextView trackName = (TextView) findViewById(R.id.track_name);
		if (toggleButton.getText().equals("Start recording")) {
			if (trackName.getText().length() > 0) {
				toggleButton.setText("Stop");
				trackName.setEnabled(false);
				v.invalidate();
				startService(trackService);
				TrackService.setName(trackName.getText().toString());
				mNotificationManager.notify(HELLO_ID, notification);
			} else {
				Toast.makeText(getApplicationContext(), "Please give your track a name.", Toast.LENGTH_SHORT).show();
			}
		} else {
			mNotificationManager.cancel(HELLO_ID);
			stopService(trackService);
			finish();
		}
	}
}
