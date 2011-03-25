package com.vop.augumented;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

public class StartupActivity extends FullscreenActivity {
	static Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startupactivity_layout);
		context = getApplicationContext();
	}

	// go-klik
	public void go_klik(View v) {
		final EditText emailbox = (EditText) findViewById(R.id.invul_box);
		final VopApplication app = (VopApplication) getApplicationContext();
		app.putState("login", "true");
		final ProgressDialog dialog = ProgressDialog.show(this, "",
				"Bezig met inloggen. Even geduld...", true);
		new Thread() {
			public void run() {
				try {
					Person p = DBWrapper.getProfile(emailbox.getText()
							.toString());
					if (p != null) {
						app.putState("userid", p.getId().toString());
						Intent myIntent = new Intent(StartupActivity.this,
								Hoofdmenu.class);
						StartupActivity.this.startActivity(myIntent);
						dialog.dismiss();
					} else {
						dialog.dismiss();
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(getApplicationContext(),
										"emailadres is niet gevonden",
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (Exception e) {
				}
			}
		}.start();
	}

	// menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.hoofdmenu_menu, menu);
		return true;
	}

}
