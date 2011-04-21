package com.vop.augumented;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;

public class Hoofdmenu extends FullscreenActivity {
	Vibrator vibrator;
	VopApplication app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (VopApplication) getApplicationContext();
		setContentView(R.layout.hoofdmenu_layout);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public void onStart() {
		super.onStart();

		if (app.getState().get("userid") == null) {
			finish();
		}
	}


	// knoppen
	public void locaties_klik(View v) {
		vibrator.vibrate(60);
		Intent myIntent = new Intent(Hoofdmenu.this, AugmentedRealityLocaties.class);
		Hoofdmenu.this.startActivity(myIntent);
	}

	public void trajecten_klik(View v) {
		vibrator.vibrate(60);
		Intent myIntent = new Intent(Hoofdmenu.this, Trajecten.class);
		Hoofdmenu.this.startActivity(myIntent);
	}

	public void profiel_klik(View v) {
		vibrator.vibrate(60);
		Intent myIntent = new Intent(Hoofdmenu.this, Profiel.class);
		Hoofdmenu.this.startActivity(myIntent);
	}

	public void vrienden_klik(View v) {
		vibrator.vibrate(60);
		Intent myIntent = new Intent(Hoofdmenu.this, TrajectOpslaan.class);
		Hoofdmenu.this.startActivity(myIntent);
	}

	public void berichten_klik(View v) {
		vibrator.vibrate(60);
		Intent myIntent = new Intent(Hoofdmenu.this, StartWandeling.class);
		Hoofdmenu.this.startActivity(myIntent);
	}

	public void uitloggen_klik(View v) {
		vibrator.vibrate(60);
		app.putState("userid", null);

		SharedPreferences.Editor editor = getSharedPreferences(VopApplication.PREFS, MODE_PRIVATE).edit();
		editor.remove("userid");
		editor.commit();

		moveTaskToBack(true);
		finish();
	}

	// menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.hoofdmenu_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.optie_1:
			Toast toast = Toast.makeText(getApplicationContext(),
					"u selecteerde:" + item.getTitle(), Toast.LENGTH_SHORT);
			toast.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
