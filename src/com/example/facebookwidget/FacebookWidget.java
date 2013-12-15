package com.example.facebookwidget;

import java.util.Random;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class FacebookWidget extends AppWidgetProvider {

	  private static final String ACTION_CLICK = "ACTION_CLICK";

	  @Override
	  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	      int[] appWidgetIds) {

	    // Get all ids
	    ComponentName thisWidget = new ComponentName(context,FacebookWidget.class);
	    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
	    for (int widgetId : allWidgetIds) {
	      // create some random data
	      int number = (new Random().nextInt(100));

	      RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.main);
	      Log.w("WidgetExample", String.valueOf(number));
	      
	      // Set the text
	      remoteViews.setTextViewText(R.id.widget_textview, String.valueOf(number));

	      // Register an onClickListener
	      Intent intent = new Intent(context, FacebookWidget.class);

	      intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
	      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

	      PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	      remoteViews.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);
	      appWidgetManager.updateAppWidget(widgetId, remoteViews);
	      
	    }
	  }
	
//    @Override
//    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds )
//    {
//        RemoteViews remoteViews;
//        ComponentName watchWidget;
//        DateFormat format = SimpleDateFormat.getTimeInstance( SimpleDateFormat.MEDIUM, Locale.getDefault() );
//
//        remoteViews = new RemoteViews( context.getPackageName(), R.layout.main );
//        watchWidget = new ComponentName( context, FacebookWidget.class );
//        remoteViews.setTextViewText( R.id.LinearLayout01, "Time = " + format.format( new Date()));
//        appWidgetManager.updateAppWidget( watchWidget, remoteViews );
//    }
    
}
