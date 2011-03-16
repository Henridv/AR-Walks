package com.vop.augumented;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;

public class AugView extends View {

	private double lat;
	private double lng;

	private double lat_POI[];
	private double lng_POI[];
	private String naam[];
	double PI = 4.0 * Math.atan(1.0);

	double angle_of_view = 54.4;
	double positie;

	float pitch;
	float heading;
	float roll;
	double afstand;
	float afstand_tot_punt[];
	int aantal;

	public double getAfstand() {
		return afstand;
	}

	public void setAfstand(double afstand) {
		this.afstand = afstand;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLat(double latitude) {
		lat = latitude;
	}

	public void setLng(double longitude) {
		lng = longitude;
	}

	public float getRoll() {
		return roll;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getPitch() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	public AugView(Context context) {
		super(context);

	}

	public AugView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public AugView(Context context, AttributeSet ats, int defaultStyle) {
		super(context, ats, defaultStyle);

	}

	private float bearing;

	public void setBearing(float _bearing) {
		bearing = _bearing;
	}

	public float getBearing() {
		return bearing;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		/*
		 * RectF azimuthOval = new RectF((getMeasuredWidth() / 3) -
		 * getMeasuredWidth() / 7, (getMeasuredHeight() / 2) -
		 * getMeasuredWidth() / 7, (getMeasuredWidth() / 3) + getMeasuredWidth()
		 * / 7, (getMeasuredHeight() / 2) + getMeasuredWidth() / 7);
		 * canvas.drawText("roll: " + roll + "heading: " + heading + "pitch :" +
		 * pitch, getMeasuredWidth() / 10, getMeasuredHeight() / 4,
		 * markerPaint); // canvas.drawText(azimuth +"", 0, 0, // //
		 * markerPaint); markerPaint.setStyle(Paint.Style.STROKE);
		 * canvas.drawOval(azimuthOval, markerPaint);
		 * markerPaint.setStyle(Paint.Style.FILL);
		 * markerPaint.setStrokeWidth(1); canvas.save(); canvas.rotate(roll,
		 * getMeasuredWidth() / 3, getMeasuredHeight() / 2);
		 * canvas.drawArc(azimuthOval, 0, 180, false, markerPaint);
		 * canvas.restore();
		 */

		// instellingen van de paint die gebruikt wordt voor de cirkels en tekst
		Paint myPaint = new Paint();
		myPaint.setStrokeWidth(1);
		myPaint.setColor(0xFF097286);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setTextSize(20);

		Paint myPaint2 = new Paint();
		myPaint2.setColor(0xFF097286);
		myPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
		myPaint2.setTextSize(20);

		// aantal punten worden ingegeven
		aantal = 4;
		lat_POI = new double[aantal];
		lng_POI = new double[aantal];
		naam = new String[aantal];

		lat_POI[0] = 50.850608;
		lng_POI[0] = 4.350929;
		naam[0] = "Brussel";

		lat_POI[1] = 51.221508;
		lng_POI[1] = 4.397964;
		naam[1] = "Antwerpen";

		lat_POI[2] = 51.058444;
		lng_POI[2] = 3.719902;
		naam[2] = "Gent";
		
		lat_POI[3] = 51.212691;
		lng_POI[3] = 3.220024;
		naam[3] = "Brugge";

		for (int i = 0; i < aantal; i++) {

			// afstanden in x en y coordinaten worden berekend
			double x = Math.abs(lng_POI[i] - lng);
			double y = Math.abs(lat_POI[i] - lat);

			positie = -1;
			double hoek;

			// wiskundige berekening voor de positie op het scherm te vinden
			// horizontaal, verticaal wordt er geen rekening mee gehouden
			if (lat_POI[i] - lat > 0) {
				if (lng_POI[i] - lng > 0) {
					hoek = Math.atan(x / y) / (2 * PI) * 360;
					if (Math.abs(hoek - roll) < angle_of_view / 2)
						positie = (hoek - roll) / (angle_of_view / 2);
					else if (Math.abs(hoek + (360 - roll)) < angle_of_view / 2)
						positie = Math.abs(hoek + (360 - roll))
								/ (angle_of_view / 2);
				} else if (lng_POI[i] - lng < 0) {
					hoek = 360 - Math.atan(x / y) / (2 * PI) * 360;
					if (Math.abs(hoek - roll) < angle_of_view / 2)
						positie = (hoek - roll) / (angle_of_view / 2);
					else {
						hoek = Math.atan(x / y) / (2 * PI) * 360;
						if (Math.abs(hoek + roll) < angle_of_view / 2)
							positie = -Math.abs(hoek + roll)
									/ (angle_of_view / 2);
					}
				} else if (roll < angle_of_view / 2)
					positie = -roll / (angle_of_view / 2);
				else if (Math.abs(360 - roll) < angle_of_view / 2)
					positie = Math.abs(roll - 360) / (angle_of_view / 2);
			} else if (lat_POI[i] - lat < 0) {
				if (lng_POI[i] - lng > 0) {
					hoek = 90 + Math.abs(Math.atan(y / x) / (2 * PI) * 360);
					if (Math.abs(hoek - roll) < angle_of_view / 2)
						positie = (hoek - roll) / (angle_of_view / 2);
				} else if (lng_POI[i] - lng < 0) {
					hoek = 180 + ((Math.atan(x / y) / (2 * PI) * 360));
					if (Math.abs(hoek - roll) < angle_of_view / 2)
						positie = (hoek - roll) / (angle_of_view / 2);
				} else if (Math.abs(roll - 180) < angle_of_view / 2)
					positie = (180 - roll) / (angle_of_view / 2);
			} else if (lng_POI[i] - lng > 0) {
				if (Math.abs(roll - 90) < angle_of_view / 2)
					positie = (90 - roll) / (angle_of_view / 2);
			} else if (lng_POI[i] - lng < 0) {
				if (Math.abs(roll - 270) < angle_of_view / 2)
					positie = (270 - roll) / (angle_of_view / 2);
			}
			afstand_tot_punt = new float[3];

			// afstand tot punt berekenen
			Location.distanceBetween(lat, lng, lat_POI[i], lng_POI[i],
					afstand_tot_punt);
			// teken punt wanneer het binnen het bereik ligt
			if (positie != -1 && afstand_tot_punt[0] < afstand) {
				// aanduiding punt
				canvas.drawCircle(getMeasuredWidth() / 2 + (float) positie
						* getMeasuredWidth() / 2,
						(float) getMeasuredHeight() / 2,
						(float) ((float) (1 - afstand_tot_punt[0] / afstand)
								* getMeasuredHeight() / 2), myPaint);
				// info over het punt
				canvas.drawText(naam[i], getMeasuredWidth() / 2
						+ (float) positie * getMeasuredWidth() / 2,
						getMeasuredHeight() / 10, myPaint2);
			}
		}
	}
}
