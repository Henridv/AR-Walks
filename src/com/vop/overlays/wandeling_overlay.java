package com.vop.overlays;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class wandeling_overlay extends Overlay{
	private GeoPoint point1,point2;
	private int color;
	
	public wandeling_overlay(GeoPoint point1,GeoPoint point2, int color){
		this.point1 = point1;
		this.point2 = point2;
		this.color = color;
	}
	
	public wandeling_overlay(GeoPoint point1,GeoPoint point2){
		this.point1 = point1;
		this.point2 = point2;
		this.color =  -65536;
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		Projection proj = mapView.getProjection();
		Paint paint = new Paint();
		Point punt = new Point();
		proj.toPixels(this.point1,punt);
		paint.setColor(color);
		Point punt2 = new Point();
		proj.toPixels(this.point2,punt2);
		paint.setStrokeWidth(5);
		paint.setAlpha(120);
		paint.setAntiAlias(true);
		//canvas.drawPoint(punt.x, punt.y, paint);
		//canvas.drawPoint(punt2.x,punt2.y,paint);
		canvas.drawLine(punt.x,punt.y,punt2.x,punt2.y,paint);
		super.draw(canvas, mapView, shadow);		
	}
	

}


