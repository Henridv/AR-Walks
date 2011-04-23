package com.vop.overlays;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.vop.tools.DBWrapper;
import com.vop.tools.VopApplication;

public class AugView extends View {
	static private Context kontekst;

	private int dichtste_punt;

	public String getDichtste_punt() {
		if (dichtste_punt != -1) {
			VopApplication app = (VopApplication) kontekst;
			Marker POI[] = app.getPunten();
			return POI[dichtste_punt].getInfo();
		} else
			return "";
	}

	double PI = 4.0 * Math.atan(1.0);

	double angle_of_view_horizontal = 54.4 / 2;
	double angle_of_view_vertical = 37.8 / 2;

	double afstand;
	float afstand_tot_punt[];

	public AugView(Context context) {
		super(context);
		VopApplication app = (VopApplication) context;
		app.putState("first", Boolean.toString(true));
		kontekst = context;
		construeer();
	}

	public AugView(Context context, AttributeSet attrs) {
		super(context, attrs);
		VopApplication app = (VopApplication) context;
		app.putState("first", Boolean.toString(true));
		kontekst = context;
		construeer();
	}

	public AugView(Context context, AttributeSet ats, int defaultStyle) {
		super(context, ats, defaultStyle);
		VopApplication app = (VopApplication) context;
		app.putState("first", Boolean.toString(true));
		kontekst = context;
		construeer();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// instellingen van de paint die gebruikt wordt voor de cirkels en tekst
		VopApplication app = (VopApplication) kontekst;
		double lng = app.getLng();
		double lat = app.getLat();
		double alt = app.getAlt();
		double roll = app.getAzimuth();

		Marker POI[];

		Paint myPaint = new Paint();
		myPaint.setStrokeWidth(2);
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

		POI = app.getPunten();
		for (int i = 0; i < POI.length; i++) {
			POI[i].bereken_zichtbaarheid(lat, lng, alt, roll);
		}
		/*RectF rectangle = new RectF(getMeasuredWidth() / 70,
				getMeasuredHeight() / 70, 69 * getMeasuredWidth() / 70,
				2 * getMeasuredHeight() / 10);*/
		/*Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.androidmarker);

		canvas.drawCircle(9 * getMeasuredWidth() / 10,
				9 * getMeasuredHeight() / 10, 20, myPaint);
		canvas.drawCircle(9 * getMeasuredWidth() / 10,
				9 * getMeasuredHeight() / 10, 1, kader_kleur);*/

		/*for (int i = 0; i < POI.length; i++) {
			canvas.rotate((float) roll, 9 * getMeasuredWidth() / 10,
					9 * getMeasuredHeight() / 10);
			if (POI[i].getBinnen_afstand())
				canvas.drawCircle(
						9 * getMeasuredWidth() / 10 + POI[i].getAfstand_x(),
						9 * getMeasuredHeight() / 10 + POI[i].getAfstand_y(),
						1, kader_kleur);
			canvas.rotate((float) -roll, 9 * getMeasuredWidth() / 10,
					9 * getMeasuredHeight() / 10);
			if (POI[i].isZichtbaarheid()) {
				canvas.drawRoundRect(rectangle, 8, 8, kader_kleur);
				canvas.drawCircle(9 * getMeasuredWidth() / 10,
						9 * getMeasuredHeight() / 10, 1, kader_kleur);
				/*
				 * canvas.drawCircle( getMeasuredWidth() / 2 + (float)
				 * POI[i].getHorizontale_positie() getMeasuredWidth() / 2,
				 * (float) (getMeasuredHeight() / 2 + POI[i]
				 * .getVerticale_positie() getMeasuredHeight() / 2), (float) 20,
				 * myPaint);
				 */
				/*canvas.drawBitmap(
						bmp,
						getMeasuredWidth() / 2
								+ (float) POI[i].getHorizontale_positie()
								* getMeasuredWidth() / 2 - 10,
						(float) (getMeasuredHeight() / 2
								+ POI[i].getVerticale_positie()
								* getMeasuredHeight() / 2 - 10), null);
			}

		}*/

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
		/*Bitmap bmp2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.marker_pos);
		Bitmap bmp3 = BitmapFactory.decodeResource(getResources(),
				R.drawable.arrow);*/
		if (dichtste_punt != -1) {
			canvas.drawText(POI[dichtste_punt].getTitel(),
					getMeasuredWidth() / 10, getMeasuredHeight() / 10, myPaint2);
			/*canvas.rotate((float) roll, 9 * getMeasuredWidth() / 10,
					9 * getMeasuredHeight() / 10);*/
			/*canvas.drawCircle(
					9 * getMeasuredWidth() / 10
							+ POI[dichtste_punt].getAfstand_x(),
					9 * getMeasuredHeight() / 10
							+ POI[dichtste_punt].getAfstand_y(), 1, myPaint2);*/
			//canvas.restore();
			/*canvas.drawText(
					"afstand: "
							+ Math.round(POI[dichtste_punt].getAfstand_marker()),
					getMeasuredWidth() / 10, 2 * getMeasuredHeight() / 10,
					myPaint2);*/
			/*
			 * canvas.drawCircle( getMeasuredWidth() / 2 + (float)
			 * POI[dichtste_punt] .getHorizontale_positie() getMeasuredWidth() /
			 * 2, (float) (getMeasuredHeight() / 2 + POI[dichtste_punt]
			 * .getVerticale_positie() * getMeasuredHeight() / 2), (float) 20,
			 * cirkel_select);
			 */
			/*canvas.drawBitmap(bmp2, getMeasuredWidth() / 2
					+ (float) POI[dichtste_punt].getHorizontale_positie()
					* getMeasuredWidth() / 2 - 25, (float) (getMeasuredHeight()
					/ 2 + POI[dichtste_punt].getVerticale_positie()
					* getMeasuredHeight() / 2 - 25), null);
			canvas.drawBitmap(bmp3, getMeasuredWidth() / 2
					+ (float) POI[dichtste_punt].getHorizontale_positie()
					* getMeasuredWidth() / 2 - 5, 2 * getMeasuredHeight() / 10,
					null);
			if (app.getState().get("adres") != null)
				canvas.drawText("adres: " + app.getState().get("adres"),
						5 * getMeasuredWidth() / 10,
						15 * getMeasuredHeight() / 100, myPaint2);*/

		}
		/*
		 * canvas.drawText("lat: " + lat + "lng" + lng, 3 * getMeasuredWidth() /
		 * 10, getMeasuredHeight() / 10, myPaint2);
		 */
	}

	static public void construeer() {
		new Thread() {
			public void run() {
				VopApplication app = (VopApplication) kontekst;
				double lng = app.getLng();
				double lat = app.getLat();
				double alt = app.getAlt();
				// double roll = app.getRoll();

				double roll = 0;

				Marker POI[];
				ArrayList<com.vop.tools.data.Location> loc = DBWrapper
						.getLocations(2);
				POI = new Marker[loc.size()];
				int j = 0;
				for (com.vop.tools.data.Location l : loc) {
					POI[j] = new Marker(l.getName(), l.getDescription(),
							l.getLongitude(), l.getLatitute(), alt, lat, lng,
							alt, roll);
					j++;
				}
				app.setPunten(POI);
			}
		}.start();

	}
}
