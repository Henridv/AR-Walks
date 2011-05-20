package com.vop.arwalks;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.vop.tools.DBWrapper;
import com.vop.tools.FullscreenActivity;
import com.vop.tools.data.Location;

public class ShowMessage extends FullscreenActivity{

	private Location l;
	protected Bitmap bMap;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.show_message);
		int id = getIntent().getIntExtra("id", 0);
		if (id == 0)
			finish();
		else {
			l = DBWrapper.getLocation(id);
			if (l == null)
				finish();
		}

		TextView name = (TextView) findViewById(R.id.messagename);
		name.setText(l.getName());
		TextView description = (TextView) findViewById(R.id.messagedescription);
		description.setText(l.getDescription());
		TextView date = (TextView) findViewById(R.id.messagedate);
		date.setText(l.getDate().substring(0,10));
		ImageView image = (ImageView) findViewById(R.id.messageimage);
		BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize = 4;
	    
	    FileInputStream in;
		try {
			in = new FileInputStream(l.getImg());
			BufferedInputStream buf = new BufferedInputStream(in);
	        bMap = BitmapFactory.decodeStream(buf);
	        image.setImageBitmap(bMap);
	        if (in != null) {
	         	in.close();
	        }
	            if (buf != null) {
	         	buf.close();
	            }
		} catch (Exception e) {} 

	}

	
	public void onDestroy(){
		super.onDestroy();
		try{
		bMap.recycle();
		} catch(Exception e){}
		
	}
}
