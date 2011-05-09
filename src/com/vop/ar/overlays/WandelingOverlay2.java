package com.vop.ar.overlays;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class WandelingOverlay2 extends Overlay {
	private ArrayList<GeoPoint> listOfGeoPoints;
	private int color;

	public WandelingOverlay2(ArrayList<GeoPoint> listOfGeoPoints, int color) {
		this.listOfGeoPoints = listOfGeoPoints;
		this.color = color;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		GeoPoint point1 = listOfGeoPoints.get(0);
		GeoPoint point2;
		for (int i = 1; i < listOfGeoPoints.size(); i++) {
			point2 = listOfGeoPoints.get(i);
			Projection proj = mapView.getProjection();
			Paint paint = new Paint();
			Point punt = new Point();
			proj.toPixels(point1, punt);
			paint.setColor(color);
			Point punt2 = new Point();
			proj.toPixels(point2, punt2);
			paint.setStrokeWidth(5);
			paint.setAlpha(120);
			paint.setAntiAlias(true);
			canvas.drawLine(punt.x, punt.y, punt2.x, punt2.y, paint);
			super.draw(canvas, mapView, shadow);
			point1 = point2;
		}
	}
}
