package com.vop.arwalks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vop.ar.AugmentedReality;
import com.vop.ar.LocatieMap;
import com.vop.popup.ActionItem;
import com.vop.popup.QuickAction;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;

/**
 * This is the main menu of the application. It contains six buttons, each with
 * its submenus.
 * 
 * @author gbostoen
 * 
 */
public class MainScreen extends FullscreenActivity {
	VopApplication app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (VopApplication) getApplicationContext();
		app.startLocationService();

		setContentView(R.layout.hoofdmenu_layout);
		final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// messages
		ImageButton berichten = (ImageButton) findViewById(R.id.berichten);
		berichten.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrator.vibrate(60);
				final QuickAction qa = new QuickAction(v);

				ActionItem msg_view = new ActionItem();
				ActionItem actie2 = new ActionItem();
				ActionItem actie3 = new ActionItem();
				ActionItem msg_send = new ActionItem();

				msg_view.setTitle("View messages");
				msg_view.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Toast.makeText(MainScreen.this, "lijst berichten", Toast.LENGTH_SHORT).show();
						qa.dismiss();
					}
				});
				actie2.setTitle("Messages on camera");
				actie2.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Toast.makeText(MainScreen.this, "AR berichten", Toast.LENGTH_SHORT).show();
						qa.dismiss();
					}
				});
				actie3.setTitle("Messages on map");
				actie3.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Toast.makeText(MainScreen.this, "map berichten", Toast.LENGTH_SHORT).show();
						qa.dismiss();
					}
				});
				msg_send.setTitle("Send message");
				msg_send.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Toast.makeText(MainScreen.this, "bericht maken", Toast.LENGTH_SHORT).show();
						qa.dismiss();
					}
				});
				qa.addActionItem(msg_view);
				qa.addActionItem(actie2);
				qa.addActionItem(actie3);
				qa.addActionItem(msg_send);
				qa.setAnimStyle(QuickAction.ANIM_AUTO);
				qa.show();
			}
		});

		// tracks
		ImageButton trajecten = (ImageButton) findViewById(R.id.trajecten);
		trajecten.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrator.vibrate(60);
				final QuickAction qa = new QuickAction(v);

				ActionItem actie1 = new ActionItem();
				ActionItem actie2 = new ActionItem();
				ActionItem actie3 = new ActionItem();

				actie1.setTitle("List tracks");
				actie1.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, Tracks.class);
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				actie2.setTitle("Add track");
				actie2.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, TrajectOpslaan.class);
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				actie3.setTitle("Current track");
				actie3.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, TrajectOpslaanZien.class);
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				qa.addActionItem(actie1);
				qa.addActionItem(actie2);
				qa.addActionItem(actie3);
				qa.setAnimStyle(QuickAction.ANIM_AUTO);
				qa.show();
			}
		});

		// AR
		ImageButton locaties = (ImageButton) findViewById(R.id.locaties);
		locaties.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrator.vibrate(60);
				final QuickAction qa = new QuickAction(v);
				ActionItem actie1 = new ActionItem();
				ActionItem actie2 = new ActionItem();
				ActionItem actie3 = new ActionItem();
				ActionItem actie4 = new ActionItem();

				actie1.setTitle("View locations");
				actie1.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, AugmentedReality.class);
						myIntent.putExtra("type", "locations");
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				actie2.setTitle("List locations");
				actie2.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, ListLocaties.class);
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				actie3.setTitle("Locations on map");
				actie3.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, LocatieMap.class);
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				actie4.setTitle("Add current location");
				actie4.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, SaveLocation.class);
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});

				qa.addActionItem(actie1);
				qa.addActionItem(actie2);
				qa.addActionItem(actie3);
				qa.addActionItem(actie4);
				qa.setAnimStyle(QuickAction.ANIM_AUTO);
				qa.show();
			}
		});

		// friends
		ImageButton vrienden = (ImageButton) findViewById(R.id.vrienden);
		vrienden.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrator.vibrate(60);
				final QuickAction qa = new QuickAction(v);
				ActionItem actie1 = new ActionItem();
				ActionItem actie2 = new ActionItem();

				actie1.setTitle("List friends");
				actie1.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, Friends.class);
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				actie2.setTitle("Add friend");
				actie2.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, AddFriend.class);
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				qa.addActionItem(actie1);
				qa.addActionItem(actie2);
				qa.setAnimStyle(QuickAction.ANIM_AUTO);
				qa.show();
			}
		});

		// profile
		ImageButton profiel = (ImageButton) findViewById(R.id.profiel);
		profiel.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrator.vibrate(60);
				final QuickAction qa = new QuickAction(v);
				ActionItem actie1 = new ActionItem();
				ActionItem actie2 = new ActionItem();
				ActionItem actie3 = new ActionItem();

				actie1.setTitle("View profile");
				actie1.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, Profile.class);
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				actie2.setTitle("Edit profile");
				actie2.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, EditProfile.class);
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				actie3.setTitle("Edit password");
				actie3.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						vibrator.vibrate(60);
						Intent myIntent = new Intent(MainScreen.this, EditPassword.class);
						MainScreen.this.startActivity(myIntent);
						qa.dismiss();
					}
				});
				qa.addActionItem(actie1);
				qa.addActionItem(actie2);
				qa.addActionItem(actie3);
				qa.setAnimStyle(QuickAction.ANIM_AUTO);
				qa.show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		app.stopLocationService();
	}

	@Override
	public void onStart() {
		super.onStart();

		if (app.getState().get("userid") == null) {
			finish();
		}
	}

	/**
	 * log out action
	 * 
	 * @param v
	 */
	public void uitloggen_klik(View v) {
		final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(60);
		app.putState("userid", null);

		SharedPreferences.Editor editor = getSharedPreferences(VopApplication.PREFS, MODE_PRIVATE).edit();
		editor.remove("userid");
		editor.commit();

		moveTaskToBack(true);
		finish();
	}
}
