package com.vop.augumented;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.vop.tools.DBWrapper;
import com.vop.tools.VopApplication;

public class AugView extends View {

	private Marker POI[];
	private Context kontekst;
	private double lat;
	private double lng;
	private double alt;


	private int dichtste_punt;



	public String getDichtste_punt() {
		if (dichtste_punt != -1)
			return POI[dichtste_punt].getInfo();
		else
			return "";
	}

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
		VopApplication app = (VopApplication) context;
		app.putState("first",Boolean.toString(true));
		kontekst = context;

	}

	public AugView(Context context, AttributeSet attrs) {
		super(context, attrs);
		VopApplication app = (VopApplication) context;
		app.putState("first",Boolean.toString(true));
		kontekst = context;

	}

	public AugView(Context context, AttributeSet ats, int defaultStyle) {
		super(context, ats, defaultStyle);
		VopApplication app = (VopApplication) context;
		app.putState("first",Boolean.toString(true));
		kontekst = context;
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
		VopApplication app = (VopApplication) kontekst;
		lng = Double.parseDouble(app.getState().get("long"));
		lat = Double.parseDouble(app.getState().get("lat"));
		alt = Double.parseDouble(app.getState().get("alt"));
		
		//lng = 3.726897;
		//lat = 51.038662;
		//alt = 40;
		
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

		Boolean first = Boolean.parseBoolean((app.getState().get("first")));
		if (first) {
			construeer();
			app.putState("first", "false");
		} else {
			for (int i = 0; i < POI.length; i++) {
				POI[i].bereken_zichtbaarheid(lat, lng, alt, roll);
			}

		}
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
						(float) (getMeasuredHeight() / 2 + POI[i]
								.getVerticale_positie()
								* getMeasuredHeight()
								/ 2), (float) 20, myPaint);
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

			canvas.drawText(POI[dichtste_punt].getTitel(),
					getMeasuredWidth() / 10, getMeasuredHeight() / 10, myPaint2);

			canvas.drawText(
					"afstand: " + POI[dichtste_punt].getAfstand_marker(),
					getMeasuredWidth() / 10, 2 * getMeasuredHeight() / 10,
					myPaint2);
			canvas.drawCircle(
					getMeasuredWidth()
							/ 2
							+ (float) POI[dichtste_punt]
									.getHorizontale_positie()
							* getMeasuredWidth() / 2,
					(float) (getMeasuredHeight() / 2 + POI[dichtste_punt]
							.getVerticale_positie() * getMeasuredHeight() / 2),
					(float) 20, cirkel_select);
		}

		/*
		 * canvas.drawText("provider: "+provider, getMeasuredWidth() / 10,
		 * getMeasuredHeight() / 10, myPaint2);
		 */

	}

	public void construeer() {
		ArrayList<com.vop.tools.data.Location> loc = DBWrapper.getLocations(2);
		aantal = loc.size();
		POI = new Marker[aantal];
		int j = 0;
		for (com.vop.tools.data.Location l : loc) {
			POI[j] = new Marker(l.getName(), l.getDescription(),
					l.getLongitude(), l.getLatitute(), alt, lat, lng, alt, roll);
			//POI[j] = new Marker("overpoortresto", "resto",
					//3.726259, 51.039326, alt, lat, lng, alt, roll);
			j++;
		}
	}
	/*public void construeer(){
		POI = new Marker[1];
		POI[0] = new Marker("overpoortresto", "resto",
				3.726259, 51.039326, alt, lat, lng, alt, roll);
	}*/
	
}
