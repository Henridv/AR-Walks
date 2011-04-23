package com.vop.augumented;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vop.tools.DBWrapper;
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
		
		//berichten
		ImageButton berichten=(ImageButton) findViewById(R.id.berichten);
		berichten.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				final CharSequence[] items = { "Walk", "Edit", "Delete" };

				AlertDialog.Builder builder = new AlertDialog.Builder(Hoofdmenu.this);
				builder.setTitle("hello");
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("Walk")) {
							Toast.makeText(Hoofdmenu.this, "not yet implemented", Toast.LENGTH_SHORT).show();
						} else if (items[item].equals("Edit")) {
							Toast.makeText(Hoofdmenu.this, "not yet implemented", Toast.LENGTH_SHORT).show();
						} else if (items[item].equals("Delete")) {
							Toast.makeText(Hoofdmenu.this, "not yet implemented", Toast.LENGTH_SHORT).show();
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}
		});
		//trajecten
		ImageButton trajecten=(ImageButton) findViewById(R.id.trajecten);
		trajecten.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				final CharSequence[] items = { "lijst", "toevoegen traject"};

				AlertDialog.Builder builder = new AlertDialog.Builder(Hoofdmenu.this);
				builder.setTitle("trajecten");
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("lijst")) {
							Intent myIntent = new Intent(Hoofdmenu.this, Trajecten.class);
							Hoofdmenu.this.startActivity(myIntent);
						} else if (items[item].equals("toevoegen traject")) {
							Intent myIntent = new Intent(Hoofdmenu.this, TrajectOpslaan.class);
							Hoofdmenu.this.startActivity(myIntent);
						}}
				});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}
		});
		//locaties
		ImageButton locaties=(ImageButton) findViewById(R.id.locaties);
		locaties.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				final CharSequence[] items = { "augmented reality", "lijst","map","locatie opslaan","huidig opslaan"};

				AlertDialog.Builder builder = new AlertDialog.Builder(Hoofdmenu.this);
				builder.setTitle("locaties");
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("augmented realtiy")) {
							Intent myIntent = new Intent(Hoofdmenu.this, AugmentedRealityLocaties.class);
							Hoofdmenu.this.startActivity(myIntent);
						} else if (items[item].equals("lijst")) {
							Intent myIntent = new Intent(Hoofdmenu.this, ListLocaties.class);
							Hoofdmenu.this.startActivity(myIntent);
						}
						else if(items[item].equals("map")){
							Intent myIntent = new Intent(Hoofdmenu.this, LocatieMap.class);
							Hoofdmenu.this.startActivity(myIntent);
						}
						else if(items[item].equals("locatie opslaan")){
							Intent myIntent = new Intent(Hoofdmenu.this, LocatieOpslaan.class);
							Hoofdmenu.this.startActivity(myIntent);
						}
						else{
							Intent myIntent = new Intent(Hoofdmenu.this, HuidigeLocatieOpslaan.class);
							Hoofdmenu.this.startActivity(myIntent);
						}
						}
				});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}
		});
		//vrienden
		ImageButton vrienden=(ImageButton) findViewById(R.id.vrienden);
		vrienden.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				final CharSequence[] items = { "Walk", "Edit", "Delete" };

				AlertDialog.Builder builder = new AlertDialog.Builder(Hoofdmenu.this);
				builder.setTitle("hello");
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("Walk")) {
							Toast.makeText(Hoofdmenu.this, "not yet implemented", Toast.LENGTH_SHORT).show();
						} else if (items[item].equals("Edit")) {
							Toast.makeText(Hoofdmenu.this, "not yet implemented", Toast.LENGTH_SHORT).show();
						} else if (items[item].equals("Delete")) {
							Toast.makeText(Hoofdmenu.this, "not yet implemented", Toast.LENGTH_SHORT).show();
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}
		});
		//profiel
		ImageButton profiel=(ImageButton) findViewById(R.id.profiel);
		profiel.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				final CharSequence[] items = { "Walk", "Edit", "Delete" };

				AlertDialog.Builder builder = new AlertDialog.Builder(Hoofdmenu.this);
				builder.setTitle("hello");
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("Walk")) {
							Toast.makeText(Hoofdmenu.this, "not yet implemented", Toast.LENGTH_SHORT).show();
						} else if (items[item].equals("Edit")) {
							Toast.makeText(Hoofdmenu.this, "not yet implemented", Toast.LENGTH_SHORT).show();
						} else if (items[item].equals("Delete")) {
							Intent myIntent = new Intent(Hoofdmenu.this, Trajecten.class);
							Hoofdmenu.this.startActivity(myIntent);
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}
		});
		
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
