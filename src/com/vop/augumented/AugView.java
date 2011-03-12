package com.vop.augumented;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.location.Location;
import android.view.*;
import android.widget.Toast;
import android.util.AttributeSet;
import android.content.res.Resources;

public class AugView extends View {

	private double lat;
	private double lng;

	private double lat_POI[];
	private double lng_POI[];
	private double positie[];
	private boolean zichtbaar[];
	private int dichtste_punt;

	public String getDichtste_punt() {
		if (dichtste_punt != -1)
			return info[dichtste_punt];
		else
			return "";
	}

	private String naam[];
	private String info[];
	double PI = 4.0 * Math.atan(1.0);

	double angle_of_view = 54.4;

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
		myPaint2.setTextSize(20);

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
		naam = new String[aantal];
		positie = new double[aantal];
		info = new String[aantal];
		zichtbaar = new boolean[aantal];

		lat_POI[0] = 51.150186;
		lng_POI[0] = 3.194329;
		naam[0] = "autolocatie";
		info[0] = "De locatie van onze auto op de oprit";

		lat_POI[1] = 51.221508;
		lng_POI[1] = 4.397964;
		naam[1] = "Antwerpen";
		info[1] = "De stad antwerpen";

		lat_POI[2] = 51.058444;
		lng_POI[2] = 3.719902;
		naam[2] = "Gent";
		info[2] = "Gent the place to be";

		lat_POI[3] = 51.212691;
		lng_POI[3] = 3.220024;
		naam[3] = "Brugge";
		info[3] = "Bruges, venetie van het noorden";

		canvas.drawRect(0, 0, getMeasuredWidth(), 2 * getMeasuredHeight() / 10,
				kader_kleur);
		for (int i = 0; i < aantal; i++) {

			// afstanden in x en y coordinaten worden berekend
			double x = Math.abs(lng_POI[i] - lng);
			double y = Math.abs(lat_POI[i] - lat);

			positie[i] = -1;
			double hoek;
			zichtbaar[i] = false;

			// wiskundige berekening voor de positie op het scherm te vinden
			// horizontaal, verticaal wordt er geen rekening mee gehouden
			if (lat_POI[i] - lat > 0) {
				if (lng_POI[i] - lng > 0) {
					hoek = Math.atan(x / y) / (2 * PI) * 360;
					if (Math.abs(hoek - roll) < angle_of_view / 2)
						positie[i] = (hoek - roll) / (angle_of_view / 2);
					else if (Math.abs(hoek + (360 - roll)) < angle_of_view / 2)
						positie[i] = Math.abs(hoek + (360 - roll))
								/ (angle_of_view / 2);
				} else if (lng_POI[i] - lng < 0) {
					hoek = 360 - Math.atan(x / y) / (2 * PI) * 360;
					if (Math.abs(hoek - roll) < angle_of_view / 2)
						positie[i] = (hoek - roll) / (angle_of_view / 2);
					else {
						hoek = Math.atan(x / y) / (2 * PI) * 360;
						if (Math.abs(hoek + roll) < angle_of_view / 2)
							positie[i] = -Math.abs(hoek + roll)
									/ (angle_of_view / 2);
					}
				} else if (roll < angle_of_view / 2)
					positie[i] = -roll / (angle_of_view / 2);
				else if (Math.abs(360 - roll) < angle_of_view / 2)
					positie[i] = Math.abs(roll - 360) / (angle_of_view / 2);
			} else if (lat_POI[i] - lat < 0) {
				if (lng_POI[i] - lng > 0) {
					hoek = 90 + Math.abs(Math.atan(y / x) / (2 * PI) * 360);
					if (Math.abs(hoek - roll) < angle_of_view / 2)
						positie[i] = (hoek - roll) / (angle_of_view / 2);
				} else if (lng_POI[i] - lng < 0) {
					hoek = 180 + ((Math.atan(x / y) / (2 * PI) * 360));
					if (Math.abs(hoek - roll) < angle_of_view / 2)
						positie[i] = (hoek - roll) / (angle_of_view / 2);
				} else if (Math.abs(roll - 180) < angle_of_view / 2)
					positie[i] = (180 - roll) / (angle_of_view / 2);
			} else if (lng_POI[i] - lng > 0) {
				if (Math.abs(roll - 90) < angle_of_view / 2)
					positie[i] = (90 - roll) / (angle_of_view / 2);
			} else if (lng_POI[i] - lng < 0) {
				if (Math.abs(roll - 270) < angle_of_view / 2)
					positie[i] = (270 - roll) / (angle_of_view / 2);
			}
			afstand_tot_punt = new float[3];

			// afstand tot punt berekenen
			Location.distanceBetween(lat, lng, lat_POI[i], lng_POI[i],
					afstand_tot_punt);
			// teken punt wanneer het binnen het bereik ligt
			if (positie[i] != -1 && afstand_tot_punt[0] < afstand) {
				zichtbaar[i] = true;
				// aanduiding punt
				canvas.drawCircle(getMeasuredWidth() / 2 + (float) positie[i]
						* getMeasuredWidth() / 2,
						(float) getMeasuredHeight() / 2, (float) 20, myPaint);
			}
		}
		dichtste_punt = -1;
		for (int i = 0; i < aantal; i++) {
			if (dichtste_punt == -1 && zichtbaar[i])
				dichtste_punt = i;
			else if (dichtste_punt != -1 && zichtbaar[i]) {
				if (Math.abs(positie[dichtste_punt]) > Math.abs(positie[i]))
					dichtste_punt = i;
			}
		}
		if (dichtste_punt != -1) {
			canvas.drawText(naam[dichtste_punt], 0, getMeasuredHeight() / 10,
					myPaint2);
			canvas.drawCircle(getMeasuredWidth() / 2
					+ (float) positie[dichtste_punt] * getMeasuredWidth() / 2,
					(float) getMeasuredHeight() / 2, (float) 20, cirkel_select);
		}
	}
}