package com.vop.arwalks;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Location;

public class AddMessage extends FullscreenActivity {

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
		_path = Environment.getExternalStorageDirectory()
				+ "/images/example.jpg";
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

	public void with_picture(View v) {
		app = (VopApplication) getApplicationContext();
		id = Integer.parseInt(app.getState().get("userid"));
		locName = locationName.getText().toString();
		locDescr = locationDescription.getText().toString();
		if (locName.equals("") || locDescr.equals(""))
			Toast.makeText(getApplicationContext(), "Fill in all fields!", Toast.LENGTH_LONG).show();
		else
			startCameraActivity();
	}

	public void without_picture(View v) {
		// final ProgressDialog waiting = ProgressDialog.show(this, "",
		// "Saving location and info. Please be patient...", true);
		new Thread(new Runnable() {
			public void run() {
				app = (VopApplication) getApplicationContext();
				id = Integer.parseInt(app.getState().get("userid"));
				locName = locationName.getText().toString();
				locDescr = locationDescription.getText().toString();

				DBWrapper.save(new Location(locName, locDescr, app.getLat(), app.getLng(), app.getAlt(), id));

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), "Location with extra information added!", Toast.LENGTH_LONG).show();

					}
				});
			}
		}).start();
		finish();
	}

	protected void startCameraActivity() {
		File file = new File(_path);
		new File(Environment.getExternalStorageDirectory() + "/images/").mkdirs();
		// if(!file.exists()){
		// file.mkdirs();
		// }

		Uri outputFileUri = Uri.fromFile(file);

		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 0:
			break;
		case -1:
			try {
				onPhotoTaken();
			} catch (FileNotFoundException e) {
			}
			break;
		}
	}

	protected void onPhotoTaken() throws FileNotFoundException {
		Bitmap bMap = null;
		FileInputStream in;
		try {
			in = new FileInputStream(new File(_path));
			BufferedInputStream buf = new BufferedInputStream(in);
			bMap = BitmapFactory.decodeStream(buf);
			if (in != null)
				in.close();
			if (buf != null)
				buf.close();
		} catch (Exception e) {
		}
		// nu deze bitmap resizen

		int width = bMap.getWidth();
		int height = bMap.getHeight();
		Bitmap tempMap = android.graphics.Bitmap.createScaledBitmap(bMap, width / 8, height / 8, true);
		bMap = null;
		FileOutputStream outStream = new FileOutputStream(new File(_path));
		tempMap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
		try {
			outStream.close();
		} catch (Exception e) {
		}
		tempMap = null;

		DBWrapper.save(new Location(locName, locDescr, app.getLat(), app.getLng(), app.getAlt(), id, new File(_path)));
		Toast.makeText(getApplicationContext(), "Location with extra information added!", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(AddMessage.PHOTO_TAKEN, _taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.getBoolean(AddMessage.PHOTO_TAKEN))
			try {
				onPhotoTaken();
			} catch (FileNotFoundException e) {
			}
	}

}
