package com.vop.overlays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.view.View;
import com.vop.tools.VopApplication;

/**
 * This overlay presents information to the user.
 * It contains the viewing angle and info about closest location
 * @author henridv
 *
 */
public class InfoView extends View {
	static private Context kontekst;
	VopApplication app;

//	private int dichtste_punt;

//	public Marker getDichtste_punt() {
//		if (dichtste_punt != -1) {
//			VopApplication app = (VopApplication) kontekst;
//			Marker POI[] = app.getPunten();
//			return POI[dichtste_punt];
//		} else
//			return null;
//	}

	public InfoView(Context context) {
		super(context);
		app = (VopApplication) context;
		kontekst = context;
		app.construeer();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setTextSize(12);
		canvas.drawText("" + (int)app.getAzimuth() + "°", getMeasuredWidth() / 10, getMeasuredHeight() / 10, paint);
//		canvas.drawText("" + app.getLat(), getMeasuredWidth() / 10, getMeasuredHeight() / 10 + 15, paint);
//		canvas.drawText("" + app.getLng(), getMeasuredWidth() / 10, getMeasuredHeight() / 10 + 30, paint);
		
		paint.setTextSize(20);
		paint.setColor(Color.BLACK);
		paint.setTextAlign(Align.CENTER);
		Rect r = new Rect(0, getMeasuredHeight() - 80, getMeasuredWidth(), getMeasuredHeight());
		Paint bgPaint = new Paint();
		bgPaint.setColor(Color.WHITE);
		if (app.getCenter() != null) {
			canvas.drawRect(r, bgPaint);
			canvas.drawText(app.getCenter().getTitle(), getMeasuredWidth() / 2, getMeasuredHeight() - 30, paint);
		} else {
			canvas.drawText("", getMeasuredWidth() / 2, getMeasuredHeight() - 30, paint);
		}
	}
}
