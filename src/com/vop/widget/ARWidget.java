package com.vop.widget;

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

			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			views.setTextViewText(R.id.widget_location, "No location found yet");
			appWidgetManager.updateAppWidget(widgetId, views);
		}
	}
}
