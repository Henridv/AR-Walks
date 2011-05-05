package com.vop.augumented;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

/**
 * Login screen. This is the launcher activity.
 * 
 * @author gbostoen
 * 
 */
public class Login extends FullscreenActivity {
	static Context context;
	Facebook facebook = new Facebook("128192193922051");
	Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startupactivity_layout);
		context = getApplicationContext();
		activity = this;
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
	
	public void facebook(View v) {
		facebook.authorize(this, new String[] { "email", "read_stream",
				"publish_stream" }, new DialogListener() {

			@Override
			public void onComplete(Bundle values) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
			    b.putString("access_token", facebook.getAccessToken());
			    b.putString("query", "SELECT first_name,last_name,contact_email FROM user where uid=me()");
			    try {
					String myResult = Util.openUrl("https://api.facebook.com/method/fql.query", "GET", b);
					try {
						JSONArray json=new JSONArray(myResult);
						String first_name = (String)json.getJSONObject(0).get("first_name");
						String last_name = (String)json.getJSONObject(0).get("last_name");
						String contact_email = (String)json.getJSONObject(0).get("contact_email");
						String phone = "unknown";
						Person p = DBWrapper.getProfile(contact_email);
						//Log.e("p==null", first_name+" "+last_name+" "+contact_email+" "+phone);
						if (p == null) {
							Log.e("p==null", first_name+" "+last_name+" "+contact_email+" "+phone);
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Log.e("hello",myResult);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

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

					Intent myIntent = new Intent(Login.this, MainScreen.class);
					Login.this.startActivity(myIntent);
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
				Intent myIntent = new Intent(Login.this, MainScreen.class);
				Login.this.startActivity(myIntent);
				finish();
			}
		}
	}
}
