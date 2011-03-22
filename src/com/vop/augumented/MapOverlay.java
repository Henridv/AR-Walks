package com.vop.augumented;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapOverlay extends Overlay {
	Context context1;

	MapOverlay(Context context) {
		super();

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		context1 = context;
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		Paint myPaint2 = new Paint();
		myPaint2.setColor(Color.RED);
		myPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
		myPaint2.setTextSize(10);

		canvas.drawText("hello", 0, 0, myPaint2);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		// ---when user lifts his finger---
		if (event.getAction() == 1) {
			GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(),
					(int) event.getY());
			Toast.makeText(context1.getApplicationContext(),
					p.getLatitudeE6() / 1E6 + "," + p.getLongitudeE6() / 1E6,
					Toast.LENGTH_SHORT).show();
		}
		return false;
	}

}
