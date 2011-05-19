package com.vop.ar.overlays;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.vop.arwalks.MainScreen;
import com.vop.tools.data.Location;

public class ImageOverlay extends Overlay{
	
	private final GeoPoint geoPoint;
	//private final Context context;
	private Location l;
	
	public ImageOverlay(Location l){
		this.geoPoint = new GeoPoint((int)(l.getLatitute()*1E6),(int)(l.getLongitude()*1E6));
		//this.context = context;
		this.l = l;
	}
	
	public boolean draw(Canvas canvas,MapView mapView, boolean shadow,long when){
		super.draw(canvas, mapView, shadow);
		
		Point screenPoint = new Point();
		mapView.getProjection().toPixels(geoPoint, screenPoint);
		
		FileInputStream in;
		Bitmap markerImage = null;
		try {
			in = new FileInputStream(l.getImg());
			BufferedInputStream buf = new BufferedInputStream(in);
	        markerImage = BitmapFactory.decodeStream(buf);
	        if (in != null) {
	         	in.close();
	        }
	            if (buf != null) {
	         	buf.close();
	            }
		} catch (Exception e) {}
		
		markerImage = android.graphics.Bitmap.createScaledBitmap(markerImage, 40, 40, true);
		
		canvas.drawBitmap(markerImage, screenPoint.x-markerImage.getWidth()/2,screenPoint.y-markerImage.getHeight()/2, null);
		return true;
	}
	
	public boolean onTap(GeoPoint p, MapView mapView){
		
		return true;
	}


}
