package com.vop.arwalks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.vop.services.LocationService;
import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Location;

public class SaveLocation extends FullscreenActivity {
	Intent intent;
	Facebook facebook = new Facebook("128192193922051");
	private Activity activity;
	Location loc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.activity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_location);
		intent = new Intent(this, LocationService.class);

	}

	public void saveLocation(View v) {

		EditText name = (EditText) findViewById(R.id.loc_name);
		String locName = name.getText().toString();

		VopApplication app = (VopApplication) getApplicationContext();
		Integer id = Integer.parseInt(app.getState().get("userid"));
		double lng = app.getLng();
		double lat = app.getLat();
		double alt = app.getAlt();

		facebook.authorize(this, new String[] { "email", "read_stream",
				"publish_stream" }, new DialogListener() {

			@Override
			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(Bundle values) {
				try {
					String tekst = facebook.request("me");
					try {
						JSONObject obj = new JSONObject(tekst);
						Log.e("hello", obj.getString("id"));
						Bundle parameters = new Bundle();
						parameters.putString("message", "test2");
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						Drawable photo = activity.getResources().getDrawable(R.drawable.ar);
						Bitmap bitmap = ((BitmapDrawable) photo).getBitmap();
						bitmap.compress(Bitmap.CompressFormat.JPEG /* FileType */, 100 /* Ratio */, stream);
						byte[] bitmapdata = stream.toByteArray();
						parameters.putByteArray("picture", bitmapdata);
						Log.e("hello", bitmapdata.toString());
						String response = facebook.request("/me/photos", parameters, "POST");
						Log.e("response", response);
						DBWrapper.save(loc);
						finish();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		});

		Location loc = new Location(locName, lat, lng, alt, id);
		DBWrapper.save(loc);
		finish();
	}

	@Override
	public void onResume() {
		super.onResume();
		startService(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopService(intent);
	}
}
