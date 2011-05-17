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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Person;

public class AddMessage extends FullscreenActivity{

	private static final String PHOTO_TAKEN = "photo_taken";
	protected String _path;
	protected boolean _taken;
	
	protected EditText locationName;
	protected EditText locationDescription;
	protected EditText extraInformation;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmessage);
		_taken = false;
		_path = Environment.getExternalStorageDirectory() +"/images/example.jpg";
		locationName = (EditText) findViewById(R.id.loca_name);
		locationDescription = (EditText) findViewById(R.id.loca_descr);
		extraInformation = (EditText) findViewById(R.id.new_text);
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
		String locName = locationName.getText().toString();
		String locDescr = locationDescription.getText().toString();
		String extraInfo = locationDescription.getText().toString();
		if(locName.equals("")||locDescr.equals("")||extraInfo.equals("")){
			Toast.makeText(getApplicationContext(), "Fill in all fields!", Toast.LENGTH_LONG).show();
		}
		else{
			startCameraActivity();
			
		}
	}
	
	public void without_picture(View v){
		final ProgressDialog wachten = ProgressDialog.show(this, "", "Saving location and info. Please be patient...", true);
		new Thread(new Runnable() {
			public void run() {
				VopApplication app = (VopApplication) getApplicationContext();
				int id = Integer.parseInt(app.getState().get("userid"));
				String locName = locationName.getText().toString();
				String locDescr = locationDescription.getText().toString();
				String extraInfo = locationDescription.getText().toString();
				
				//sendInformation(Location location, locName,locDescr,extraInfo, null);

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
		if(!file.exists()){
			file.mkdir();
		}
		
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
		//sendInformation(Location location,locName,locDescr,extraInfo,image)
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
