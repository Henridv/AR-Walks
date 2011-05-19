package com.vop.ar.overlays;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.view.MotionEvent;
import android.view.View;

import com.vop.tools.VopApplication;

/**
 * This overlay presents information to the user. It contains the viewing angle
 * and info about closest location
 * 
 * @author henridv
 * 
 */
public class InfoView extends View {
	VopApplication app;

	public InfoView(Context context) {
		super(context);
		app = (VopApplication) context.getApplicationContext();
		app.getPOIs();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// draw bearing background
		Rect r = new Rect(10, 10, 50, 40);
		Paint bgPaint = new Paint();
		bgPaint.setARGB(100, 255, 255, 255);
		canvas.drawRect(r, bgPaint);

		// draw bearing
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setTextSize(12);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText("" + (int) app.getAzimuth() + "°", 30, 30, paint);

		paint.setTextSize(20);
		paint.setColor(Color.BLACK);
		paint.setTextAlign(Align.CENTER);
		r = new Rect(0, getMeasuredHeight() - 80, getMeasuredWidth(), getMeasuredHeight());
		bgPaint.setColor(Color.WHITE);

		// only show info about centered location
		if (app.getCenter() != null) {
			canvas.drawRect(r, bgPaint);
			canvas.drawText(app.getCenter().getTitle(), getMeasuredWidth() / 2, getMeasuredHeight() - 30, paint);
		} else {
			canvas.drawText("", getMeasuredWidth() / 2, getMeasuredHeight() - 30, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getY() >= getMeasuredHeight() - 30) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
			dialog.setTitle(app.getCenter().getTitle());
			dialog.setMessage(app.getCenter().getInfo());
			dialog.show();
		}

		// event was handled so we return true
		return true;
	}

}
