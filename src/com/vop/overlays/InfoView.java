package com.vop.overlays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import com.vop.tools.VopApplication;

public class InfoView extends View {
	static private Context kontekst;
	VopApplication app;

	private int dichtste_punt;

	public Marker getDichtste_punt() {
		if (dichtste_punt != -1) {
			VopApplication app = (VopApplication) kontekst;
			Marker POI[] = app.getPunten();
			return POI[dichtste_punt];
		} else
			return null;
	}

	public InfoView(Context context) {
		super(context);
		app = (VopApplication) context;
		app.putState("first", Boolean.toString(true));
		kontekst = context;
		app.construeer();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// instellingen van de paint die gebruikt wordt voor de cirkels en tekst
		/*
		double lng = app.getLng();
		double lat = app.getLat();
		double alt = app.getAlt();
		double roll = app.getAzimuth();

		Marker POI[];

		Paint myPaint2 = new Paint();
		myPaint2.setColor(Color.RED);
		myPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
		myPaint2.setTextSize(10);


		POI = app.getPunten();
		for (int i = 0; i < POI.length; i++) {
			POI[i].bereken_zichtbaarheid(lat, lng, alt, roll);
		}
		dichtste_punt = -1;
		for (int i = 0; i < POI.length; i++) {
			if (dichtste_punt == -1 && POI[i].isZichtbaarheid())
				dichtste_punt = i;
			else if (dichtste_punt != -1 && POI[i].isZichtbaarheid()) {
				if (Math.abs(POI[dichtste_punt].getHorizontale_positie()) > Math
						.abs(POI[i].getHorizontale_positie()))
					dichtste_punt = i;
			}
		}
		if (dichtste_punt != -1) {
			//app.setDichtste_punt(dichtste_punt);
			canvas.drawText(POI[dichtste_punt].getTitel(),
					getMeasuredWidth() / 10, getMeasuredHeight() / 10, myPaint2);
		}*/
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setTextSize(12);
		canvas.drawText("" + (int)app.getAzimuth() + "°", getMeasuredWidth() / 10, getMeasuredHeight() / 10, paint);
		//canvas.drawText("roll: " + app.getRoll(), getMeasuredWidth() / 10, getMeasuredHeight() / 10 + 15, paint);
		//canvas.drawText("pitch: " + app.getPitch(), getMeasuredWidth() / 10, getMeasuredHeight() / 10 + 30, paint);
	}
}
