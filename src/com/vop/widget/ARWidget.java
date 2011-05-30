package com.vop.widget;

import java.util.Random;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.vop.arwalks.R;

public class ARWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		for (int widgetId : appWidgetIds) {
			int number = (new Random().nextInt(100));

			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			views.setTextViewText(R.id.widget_location, String.valueOf(number));
			appWidgetManager.updateAppWidget(widgetId, views);
		}
	}
}
