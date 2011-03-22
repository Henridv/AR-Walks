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
		aantal = 9;
		Marker POI[] = new Marker[aantal];
		//Marker("titel","info",long,lat,alt, cur_lat,cur_lng,cur_alt,roll)
		POI[0] = new Marker("hello","hello",3.722281028086385,51.04615330799309, alt + 4, lat, lng, alt, roll);
		POI[1] = new Marker("hello","hello",3.722314331480408,51.04615326177683, alt - 10, lat, lng, alt, roll);
		POI[2] = new Marker("hello","hello",3.722354263472321,51.04615320666444, alt + 4, lat, lng, alt, roll);
		POI[3] = new Marker("hello","hello",3.722420739099623,51.04615311457567, alt - 10, lat, lng, alt, roll);
		POI[4] = new Marker("hello","hello",3.722493750467275,51.04615301342314, alt + 4, lat, lng, alt, roll);
		POI[5] = new Marker("hello","hello",3.722520243787253,51.04615706501512, alt - 10, lat, lng, alt, roll);
		POI[6] = new Marker("hello","hello",3.722540124390397,51.0461570364843, alt + 4, lat, lng, alt, roll);
		POI[7] = new Marker("hello","hello",3.722579806879691,51.04616515131642, alt - 10, lat, lng, alt, roll);
		POI[8] = new Marker("hello","hello",3.722619508791369,51.04616509092327, alt + 4, lat, lng, alt, roll);
		
		


		RectF rectangle = new RectF(getMeasuredWidth() / 70,
				getMeasuredHeight() / 70, 69 * getMeasuredWidth() / 70,
				2 * getMeasuredHeight() / 10);

		canvas.drawRoundRect(rectangle, 8, 8, kader_kleur);

		for (int i = 0; i < aantal; i++) {
			if (POI[i].isZichtbaarheid())
				canvas.drawCircle(
						getMeasuredWidth() / 2
								+ (float) POI[i].getHorizontale_positie()
								* getMeasuredWidth() / 2,
						(float) (getMeasuredHeight() / 2 + POI[i].getVerticale_positie()*getMeasuredHeight() / 2), (float) 20, myPaint);
		}

		dichtste_punt = -1;
		for (int i = 0; i < aantal; i++) {
			if (dichtste_punt == -1 && POI[i].isZichtbaarheid())
				dichtste_punt = i;
			else if (dichtste_punt != -1 && POI[i].isZichtbaarheid()) {
				if (Math.abs(POI[dichtste_punt].getHorizontale_positie()) > Math
						.abs(POI[i].getHorizontale_positie()))
					dichtste_punt = i;
			}
		}
		if (dichtste_punt != -1) {
			/*canvas.drawText(naam[dichtste_punt], getMeasuredWidth() / 10,
					getMeasuredHeight() / 10, myPaint2);*/
			canvas.drawText("afstand: " + POI[dichtste_punt].getAfstand_marker(),
					getMeasuredWidth() / 10, 2 * getMeasuredHeight() / 10,
					myPaint2);
			canvas.drawCircle(getMeasuredWidth() / 2
					+ (float) POI[dichtste_punt].getHorizontale_positie()
					* getMeasuredWidth() / 2, (float) (getMeasuredHeight() / 2 + POI[dichtste_punt].getVerticale_positie()*getMeasuredHeight() / 2),
					(float) 20, cirkel_select);
		}

		/*
		 * canvas.drawText("provider: "+provider, getMeasuredWidth() / 10,
		 * getMeasuredHeight() / 10, myPaint2);
		 */

	}
}
