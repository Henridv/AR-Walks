package com.vop.augumented;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
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
		
		final EditText edittext = (EditText) findViewById(R.id.login_password);
		edittext.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
		          go_klik(v);
		          return true;
		        }
		        return false;
		    }
		});
	}

	// go-klik
	public void go_klik(View v) {
		final EditText emailbox = (EditText) findViewById(R.id.login_email);
		final EditText password = (EditText) findViewById(R.id.login_password);

		final VopApplication app = (VopApplication) getApplicationContext();
		final ProgressDialog dialog = ProgressDialog.show(this, "",
				"Bezig met inloggen. Even geduld...", true);
		new Thread() {
			public void run() {
				Person p = DBWrapper.getProfile(emailbox.getText().toString(),
						password.getText().toString());
				app.setPersoon(p);
				if (p != null) {
					app.putState("login", "true");
					app.putState("userid", p.getId().toString());
					
					SharedPreferences.Editor editor = getSharedPreferences(VopApplication.PREFS, MODE_PRIVATE).edit();
					editor.putBoolean("login", true);
					editor.putInt("userid", p.getId());
					editor.commit();
					
					Intent myIntent = new Intent(StartupActivity.this,
							Hoofdmenu.class);
					StartupActivity.this.startActivity(myIntent);
					dialog.dismiss();
					finish();
				} else {
					dialog.dismiss();
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(),
									"Email address and/or password not valid!",
									Toast.LENGTH_SHORT).show();
						}
					});
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

	@Override
	protected void onResume() {
		super.onResume();
		
		VopApplication app = (VopApplication) getApplicationContext();
		SharedPreferences prefs = getSharedPreferences(VopApplication.PREFS, MODE_PRIVATE);
        
		boolean login = prefs.getBoolean("login", false);
		int userid = prefs.getInt("userid", 0);
		if (login && userid != 0) {
			app.putState("login", "true");
			app.putState("userid", Integer.toString(userid));
			Intent myIntent = new Intent(StartupActivity.this, Hoofdmenu.class);
			StartupActivity.this.startActivity(myIntent);
			finish();
		}
	}
}
