package com.vop.augumented;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

/**
 * start screen
 * 
 * @author gbostoen
 * 
 */
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
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					login(v);
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * login action
	 * 
	 * @param v
	 */
	public void login(View v) {
		final EditText emailbox = (EditText) findViewById(R.id.login_email);
		final EditText password = (EditText) findViewById(R.id.login_password);

		final VopApplication app = (VopApplication) getApplicationContext();
		final ProgressDialog dialog = ProgressDialog.show(this, "", "Bezig met inloggen. Even geduld...", true);
		new Thread() {
			public void run() {
				Person p = DBWrapper.getProfile(emailbox.getText().toString(), password.getText().toString());
				if (p != null) {
					app.putState("userid", p.getId().toString());

					SharedPreferences.Editor editor = getSharedPreferences(VopApplication.PREFS, MODE_PRIVATE).edit();
					editor.putInt("userid", p.getId());
					editor.commit();

					Intent myIntent = new Intent(StartupActivity.this, Hoofdmenu.class);
					StartupActivity.this.startActivity(myIntent);
					dialog.dismiss();
					finish();
				} else {
					dialog.dismiss();
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), "Credentials not valid!", Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}.start();
	}

	/**
	 * register action
	 * 
	 * @param v
	 */
	public void register(View v) {
		EditText emailbox = (EditText) findViewById(R.id.login_email);
		EditText password = (EditText) findViewById(R.id.login_password);

		Intent myIntent = new Intent(this, RegisterProfile.class);
		myIntent.putExtra("email", emailbox.getText().toString());
		myIntent.putExtra("password", password.getText().toString());
		startActivity(myIntent);
	}

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
		
		// first check if the user is online
		if (!app.isOnline()) {
			Toast.makeText(this, "You have no connection. Try again later.", Toast.LENGTH_LONG).show();
			finish();
		} else {
			SharedPreferences prefs = getSharedPreferences(VopApplication.PREFS, MODE_PRIVATE);

			int userid = prefs.getInt("userid", 0);
			if (userid != 0) {
				app.putState("userid", Integer.toString(userid));
				Intent myIntent = new Intent(StartupActivity.this, Hoofdmenu.class);
				StartupActivity.this.startActivity(myIntent);
				finish();
			}
		}
	}
}
