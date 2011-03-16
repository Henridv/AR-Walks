package com.vop.augumented;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;

public class AugView extends View {

	private double lat;
	private double lng;
	private double alt;

	public double getAlt() {
		return alt;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

	private double lat_POI[];
	private double lng_POI[];
	private double alt_POI[];
	private double pos_horizontaal[];
	private double pos_verticaal[];
	private boolean zichtbaar[];
	private int dichtste_punt;
	private String provider;

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getDichtste_punt() {
		if (dichtste_punt != -1)
			return info[dichtste_punt];
		else
			return "";
	}

	private String naam[];
	private String info[];
	double PI = 4.0 * Math.atan(1.0);

	double angle_of_view_horizontal = 54.4 / 2;
	double angle_of_view_vertical = 37.8 / 2;

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
		// instellingen van de paint die gebruikt wordt voor de cirkels en tekst
		Paint myPaint = new Paint();
		myPaint.setStrokeWidth(4);
		myPaint.setColor(Color.WHITE);
		myPaint.setStyle(Paint.Style.STROKE);

		Paint myPaint2 = new Paint();
		myPaint2.setColor(Color.RED);
		myPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
		myPaint2.setTextSize(10);

		Paint kader_kleur = new Paint();
		kader_kleur.setColor(Color.WHITE);

		Paint cirkel_select = new Paint();
		cirkel_select.setColor(Color.RED);
		cirkel_select.setStyle(Paint.Style.STROKE);
		cirkel_select.setStrokeWidth(4);

		// aantal punten worden ingegeven
		aantal = 4;
		lat_POI = new double[aantal];
		lng_POI = new double[aantal];
		alt_POI = new double[aantal];
		naam = new String[aantal];
		pos_horizontaal = new double[aantal];
		pos_verticaal = new double[aantal];
		info = new String[aantal];
		zichtbaar = new boolean[aantal];

		lat_POI[0] = 51.045859;
		lng_POI[0] = 3.724902;
		naam[0] = "jozef-plateaustraat";
		info[0] = "faculteit ingenieurwetenschappen";
		alt_POI[0] = alt;

		lat_POI[1] = 51.040746;
		lng_POI[1] = 3.730009;
		naam[1] = "Kinepolis";
		info[1] = "Ter platen";
		alt_POI[1] = alt;

		lat_POI[2] = 51.038048;
		lng_POI[2] = 3.720438;
		naam[2] = "Citadelpark";
		info[2] = "Paul Bergmansdreef";
		alt_POI[2] = alt;

		lat_POI[3] = 51.025012;
		lng_POI[3] = 3.725181;
		naam[3] = "Universitair ziekenhuis gent";
		info[3] = "De pintenlaan 185";
		alt_POI[3] = alt;

		// pitch +=80;

		RectF rectangle = new RectF(getMeasuredWidth() / 70,
				getMeasuredHeight() / 70, 69 * getMeasuredWidth() / 70,
				2 * getMeasuredHeight() / 10);

		canvas.drawRoundRect(rectangle, 8, 8, kader_kleur);

		for (int i = 0; i < aantal; i++) {

			// afstanden in x en y coordinaten worden berekend
			double x = Math.abs(lng_POI[i] - lng);
			double y = Math.abs(lat_POI[i] - lat);

			pos_horizontaal[i] = -1;
			double hoek;
			zichtbaar[i] = false;

			// wiskundige berekening voor de positie op het scherm te vinden
			// horizontaal, verticaal wordt er geen rekening mee gehouden
			if (lat_POI[i] - lat > 0) {
				if (lng_POI[i] - lng > 0) {
					hoek = Math.atan(x / y) / (2 * PI) * 360;
					if (Math.abs(hoek - roll) < angle_of_view_horizontal)
						pos_horizontaal[i] = (hoek - roll)
								/ (angle_of_view_horizontal);
					else if (Math.abs(hoek + (360 - roll)) < angle_of_view_horizontal)
						pos_horizontaal[i] = Math.abs(hoek + (360 - roll))
								/ (angle_of_view_horizontal);
				} else if (lng_POI[i] - lng < 0) {
					hoek = 360 - Math.atan(x / y) / (2 * PI) * 360;
					if (Math.abs(hoek - roll) < angle_of_view_horizontal)
						pos_horizontaal[i] = (hoek - roll)
								/ (angle_of_view_horizontal);
					else {
						hoek = Math.atan(x / y) / (2 * PI) * 360;
						if (Math.abs(hoek + roll) < angle_of_view_horizontal)
							pos_horizontaal[i] = -Math.abs(hoek + roll)
									/ (angle_of_view_horizontal / 2);
					}
				} else if (roll < angle_of_view_horizontal)
					pos_horizontaal[i] = -roll / (angle_of_view_horizontal);
				else if (Math.abs(360 - roll) < angle_of_view_horizontal)
					pos_horizontaal[i] = Math.abs(roll - 360)
							/ (angle_of_view_horizontal);
			} else if (lat_POI[i] - lat < 0) {
				if (lng_POI[i] - lng > 0) {
					hoek = 90 + Math.abs(Math.atan(y / x) / (2 * PI) * 360);
					if (Math.abs(hoek - roll) < angle_of_view_horizontal)
						pos_horizontaal[i] = (hoek - roll)
								/ (angle_of_view_horizontal);
				} else if (lng_POI[i] - lng < 0) {
					hoek = 180 + ((Math.atan(x / y) / (2 * PI) * 360));
					if (Math.abs(hoek - roll) < angle_of_view_horizontal)
						pos_horizontaal[i] = (hoek - roll)
								/ (angle_of_view_horizontal);
				} else if (Math.abs(roll - 180) < angle_of_view_horizontal)
					pos_horizontaal[i] = (180 - roll)
							/ (angle_of_view_horizontal);
			} else if (lng_POI[i] - lng > 0) {
				if (Math.abs(roll - 90) < angle_of_view_horizontal)
					pos_horizontaal[i] = (90 - roll)
							/ (angle_of_view_horizontal);
			} else if (lng_POI[i] - lng < 0) {
				if (Math.abs(roll - 270) < angle_of_view_horizontal)
					pos_horizontaal[i] = (270 - roll)
							/ (angle_of_view_horizontal);
			}
			afstand_tot_punt = new float[3];

			// afstand tot punt berekenen
			Location.distanceBetween(lat, lng, lat_POI[i], lng_POI[i],
					afstand_tot_punt);
			// teken punt wanneer het binnen het bereik ligt
			if (pos_horizontaal[i] != -1 && afstand_tot_punt[0] < afstand) {
				/*
				 * double hoogte_verschil = Math.abs(alt_POI[i] - alt); double
				 * afstand = afstand_tot_punt[0]; double alpha =
				 * Math.atan(hoogte_verschil/afstand)/(2*PI)*360;
				 * 
				 * //verticaal de positie bepalen if( (alt_POI[i] - alt) > 0){
				 * if(Math.abs(heading) < Math.abs(heading -180)){
				 * pos_verticaal[i] = -((Math.abs(pitch)+Math.abs(alpha))); }
				 * else{ pos_verticaal[i] = Math.abs(pitch) - Math.abs(alpha); }
				 * } else{ if(Math.abs(heading) > Math.abs(heading -180)){
				 * pos_verticaal[i] = Math.abs(pitch) + Math.abs(alpha); } else{
				 * pos_verticaal[i] = (Math.abs(alpha) - Math.abs(pitch)); } }
				 * pos_verticaal[i] =
				 * pos_verticaal[i]/(angle_of_view_vertical/2);
				 */

				zichtbaar[i] = true;
				// aanduiding punt
				canvas.drawCircle(getMeasuredWidth() / 2
						+ (float) pos_horizontaal[i] * getMeasuredWidth() / 2,
						getMeasuredHeight() / 2, (float) 20, myPaint);
			}
		}
		dichtste_punt = -1;
		for (int i = 0; i < aantal; i++) {
			if (dichtste_punt == -1 && zichtbaar[i])
				dichtste_punt = i;
			else if (dichtste_punt != -1 && zichtbaar[i]) {
				if (Math.abs(pos_horizontaal[dichtste_punt]) > Math
						.abs(pos_horizontaal[i]))
					dichtste_punt = i;
			}
		}
		if (dichtste_punt != -1) {
			afstand_tot_punt = new float[3];
			Location.distanceBetween(lat, lng, lat_POI[dichtste_punt], lng_POI[dichtste_punt],
					afstand_tot_punt);
			canvas.drawText(naam[dichtste_punt], getMeasuredWidth() / 10,
					getMeasuredHeight() / 10, myPaint2);
			canvas.drawText("afstand: "+afstand_tot_punt[0], getMeasuredWidth() / 10,
					2*getMeasuredHeight() / 10, myPaint2);
			canvas.drawCircle(getMeasuredWidth() / 2
					+ (float) pos_horizontaal[dichtste_punt]
					* getMeasuredWidth() / 2, getMeasuredHeight() / 2,
					(float) 20, cirkel_select);
		}
		
		  /*canvas.drawText("provider: "+provider, getMeasuredWidth() / 10,
		  getMeasuredHeight() / 10, myPaint2);*/
		 
	}
}
