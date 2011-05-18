package com.vop.arwalks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Location;

public class AddMessage extends FullscreenActivity{

	private static final String PHOTO_TAKEN = "photo_taken";
	protected String _path;
	protected boolean _taken;
	
	protected EditText locationName;
	protected EditText locationDescription;
	protected EditText extraInformation;
	
	protected String locName;
	protected String locDescr;
	protected VopApplication app;
	protected int id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmessage);
		_taken = false;
		_path = Environment.getExternalStorageDirectory() +"/images/example.jpg";
		locationName = (EditText) findViewById(R.id.loca_name);
		locationDescription = (EditText) findViewById(R.id.loca_descr);
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
		app = (VopApplication) getApplicationContext();
		id = Integer.parseInt(app.getState().get("userid"));
		locName = locationName.getText().toString();
		locDescr = locationDescription.getText().toString();
		if(locName.equals("")||locDescr.equals("")){
			Toast.makeText(getApplicationContext(), "Fill in all fields!", Toast.LENGTH_LONG).show();
		}
		else{
			startCameraActivity();
			
			
		}
	}
	
	public void without_picture(View v){
		final ProgressDialog waiting = ProgressDialog.show(this, "", "Saving location and info. Please be patient...", true);
		new Thread(new Runnable() {
			public void run() {
				app = (VopApplication) getApplicationContext();
				id = Integer.parseInt(app.getState().get("userid"));
				locName = locationName.getText().toString();
				locDescr = locationDescription.getText().toString();
				
				DBWrapper.save(new Location(locName,locDescr,app.getLat(),app.getLng(),app.getAlt(),id));

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), "Location with extra information added!", Toast.LENGTH_LONG).show();

					}
				});
			}
		}).start();
		finish();
	}
	
	protected void startCameraActivity(){
		File file = new File(_path);
		new File(Environment.getExternalStorageDirectory() +"/images/").mkdirs();
//		if(!file.exists()){
//			file.mkdirs();
//		}
		
		Uri outputFileUri = Uri.fromFile(file);
		
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		
		startActivityForResult(intent,0);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(resultCode){
			case 0:
				break;
			case -1:
				onPhotoTaken();
				break;
		}
	}
	
	protected void onPhotoTaken(){
		DBWrapper.save(new Location(locName,locDescr,app.getLat(),app.getLng(),app.getAlt(),id,new File(_path)));
		Toast.makeText(getApplicationContext(), "Location with extra information added!", Toast.LENGTH_LONG).show();
	}
	
	protected void onSaveInstanceState(Bundle outState){
		outState.putBoolean(this.PHOTO_TAKEN,_taken);
	}
	
	protected  void onRestoreInstanceState(Bundle savedInstanceState){
		if(savedInstanceState.getBoolean(this.PHOTO_TAKEN)){
			onPhotoTaken();
		}
	}
}
