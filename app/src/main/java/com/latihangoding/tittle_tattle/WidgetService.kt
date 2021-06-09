package com.latihangoding.tittle_tattle

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import java.lang.Exception

class WidgetService: RemoteViewsService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return WidgetItemFactory(applicationContext, intent)
    }


    class WidgetItemFactory(private val context: Context, private val intent: Intent?): RemoteViewsFactory {
        private val list = listOf("test", "test1", "test2", "test3")
        val appWidgetId = intent?.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

        override fun onCreate() {

        }

        override fun onDataSetChanged() {

        }

        override fun onDestroy() {

        }

        override fun getCount() = list.size

        override fun getViewAt(position: Int): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.item_widget)
            views.setTextViewText(R.id.tv_name, list[position])
            try {
                val bitmap = Glide.with(context)
                    .asBitmap()
                    .load("")
                    .submit(512, 512)
                    .get()
                views.setImageViewBitmap(R.id.iv_avatar, bitmap)
            } catch (e: Exception) {

            }
            return views
        }

        override fun getLoadingView() = RemoteViews(context.packageName, R.layout.item_widget)


        override fun getViewTypeCount() = 1

        override fun getItemId(position: Int) = position.toLong()

        override fun hasStableIds() = true

    }
}