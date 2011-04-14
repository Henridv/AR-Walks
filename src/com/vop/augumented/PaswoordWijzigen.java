package com.vop.augumented;


import android.app.ProgressDialog;
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


public class PaswoordWijzigen extends FullscreenActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.paswoordwijzigen);
		VopApplication app = (VopApplication) getApplicationContext();
		int id = Integer.parseInt(app.getState().get("userid"));
		Person p = DBWrapper.getProfile(id);
	}
	
	public void go_back(View v) {
		finish();
	}
	
	public void new_password(View v) {
		final ProgressDialog wachten = ProgressDialog.show(this, "",
				"Editing password. Please be patient...", true);
		final EditText newpasswordbox = (EditText) findViewById(R.id.passwordchange);
		final EditText newpasswordrepeatbox = (EditText) findViewById(R.id.passwordchange_repeat);
		new Thread(new Runnable(){  
			public void run(){  
				VopApplication app = (VopApplication) getApplicationContext();
				int id = Integer.parseInt(app.getState().get("userid"));
				Person p = DBWrapper.getProfile(id);
				if (newpasswordbox.getText().toString().equals(newpasswordrepeatbox.getText().toString())){
					p.setPassword(newpasswordbox.getText().toString());
					DBWrapper.save(p); 
					wachten.dismiss();
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(),
									"Changing password succeeded!",
									Toast.LENGTH_LONG).show();
							
						}
					});
					Intent myIntent = new Intent(PaswoordWijzigen.this, Profiel.class); 
					PaswoordWijzigen.this.startActivity(myIntent);
				} else {
					wachten.dismiss();
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(),
									"Repeated password is not the same as password!",
									Toast.LENGTH_LONG).show();
							
						}
					});
					newpasswordbox.setText("");
					newpasswordrepeatbox.setText("");
				}
			}  
			}).start();  
	}

	// menu openen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.profiel_menu, menu);
		return true;
	}
}