package com.vop.arwalks;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

public class AddMessage extends FullscreenActivity{

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmessage);
	}

	/**
	 * back action
	 * 
	 * @param v
	 */
	public void go_back(View v) {
		finish();
	}
	
	public void with_picture(View v){
		Toast.makeText(AddMessage.this, "Met foto", Toast.LENGTH_SHORT).show();
	}
	
	public void without_picture(View v){
		final ProgressDialog wachten = ProgressDialog.show(this, "", "Saving location and info. Please be patient...", true);
		final EditText locationName = (EditText) findViewById(R.id.loca_name);
		final EditText locationDescription = (EditText) findViewById(R.id.loca_descr);
		final EditText extraInformation = (EditText) findViewById(R.id.new_text);
		new Thread(new Runnable() {
			public void run() {
				VopApplication app = (VopApplication) getApplicationContext();
				int id = Integer.parseInt(app.getState().get("userid"));
				String locName = locationName.getText().toString();
				String locDescr = locationDescription.getText().toString();
				String extraInfo = locationDescription.getText().toString();
				
				//hier functie voor op te slaan naar databank- met henri overleggen
				/*DBWrapper.save(id,lat,lng,alt,)
				runOnUiThread(new)
				p.setName(newnamebox.getText().toString());
				p.setPhone(newphonebox.getText().toString());
				p.setEmail(newemailbox.getText().toString());
				DBWrapper.save(p);*/
				
				//wachten.dismiss();->wa doet da?
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), "Location with extra information added!", Toast.LENGTH_LONG).show();

					}
				});
			}
		}).start();
		finish();
	}
	
}
