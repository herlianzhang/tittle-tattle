package com.latihangoding.tittle_tattle

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.latihangoding.tittle_tattle.utils.FirebaseConfiguration
import com.latihangoding.tittle_tattle.vo.User
import timber.log.Timber

/**
 * Implementation of App Widget functionality.
 */

class MyWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        try {
            Firebase.database.getReference(FirebaseConfiguration.USER) // firebase realtime database
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val intent = Intent(context, MyWidget::class.java)
                        intent.action = ACTION_AUTO_UPDATE
                        context.sendBroadcast(intent) // setiap data yang berubah di realtime database
                                                    // firebase, send broadcast untuk update widget
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action?.equals(ACTION_AUTO_UPDATE) == true) { // broadcast dari firebase realtime database
            val appWidgetManager = AppWidgetManager.getInstance(context)
            for (appWidgetId in appWidgetManager.getAppWidgetIds(
                ComponentName(
                    context!!,
                    MyWidget::class.java
                )
            )?.toList() ?: emptyList()) {
                updateAppWidget(
                    context,
                    appWidgetManager,
                    appWidgetId
                ) // perform update widget
            }
        } else if (intent?.action?.equals(ACTION_POST) == true) { // broadcast dari item listview terclick
            // ambil data yang di send dari item listview
            val data = intent.getParcelableExtra<User>("data")

            context!!.startActivity(
                Intent(context, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    it.putExtra("data", data)
                }) // start main activity dengan membawa data yang tadi kita dapat
        }
        super.onReceive(context, intent)
    }

    companion object {
        private const val ACTION_AUTO_UPDATE = "android.appwidget.action.APPWIDGET_UDPATE"
        const val ACTION_POST = "actionPost"
    }
}

// update app widget
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // membuat instance intent untuk onclick dalam list view
    val postIntent = Intent(context, MyWidget::class.java)
    postIntent.action = MyWidget.ACTION_POST
    val postPendingIntent = PendingIntent.getBroadcast(context, 0, postIntent, 0)

    // membuat instance intent untuk adapter listview
    val intent = Intent(context, WidgetService::class.java)

    // membuat remoteview
    val views = RemoteViews(context.packageName, R.layout.my_widget_layout)
    views.setRemoteAdapter(R.id.list, intent) // memasukkan instance intent pada listview
    views.setPendingIntentTemplate(R.id.list, postPendingIntent) // memasukkan instance intent ke
                                                                // listview untuk onclicklistener

    // mendaftarkan pending intent untuk button chat room
    views.setOnClickPendingIntent(
        R.id.button_chatroom,
        NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.roomChatFragment)
            .createPendingIntent()
    )

    appWidgetManager.updateAppWidget(appWidgetId, views) // udate app widget
}