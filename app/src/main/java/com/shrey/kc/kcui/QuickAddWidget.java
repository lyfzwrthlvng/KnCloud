package com.shrey.kc.kcui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.shrey.kc.kcui.entities.User;
import com.shrey.kc.kcui.objects.CurrentUserInfo;
import com.shrey.kc.kcui.objects.LocalDBHolder;
import com.shrey.kc.kcui.objects.RuntimeConstants;

import androidx.annotation.RequiresApi;

/**
 * Implementation of App Widget functionality.
 */
@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class QuickAddWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        Intent addKnowledgeIntent = new Intent(context, LoggedInHomeOne.class);
        addKnowledgeIntent.putExtra("requestCode", RuntimeConstants.INSTANCE.START_FROM_WIDGET_FOR_ADD);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, RuntimeConstants.INSTANCE.START_FROM_WIDGET_FOR_ADD, addKnowledgeIntent, 0);
        // Construct the RemoteViews object
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        CurrentUserInfo.getUserInfo().setUser(new User(account.getId(), account));
        //LocalDBHolder.INSTANCE.getSetLocalDB(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quick_add_widget);
        views.setOnClickPendingIntent(R.id.button_add_quick, pendingIntent);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

