package com.latihangoding.tittle_tattle.broadcast

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.latihangoding.tittle_tattle.R

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("NewApi")
    override fun onReceive(context: Context?, intent: Intent?) {
        val notifyid = 30103
        val channel_id = "my_channel_01"
        val name = "ON/OFF"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val nNotifyChannel = NotificationChannel(
            channel_id,
            name,
            importance
        )

//        membuat notification builder ketika notifikasi muncul
        val mBuilder = NotificationCompat.Builder(context!!, channel_id)
            .setSmallIcon(R.drawable.ic_notif)
            .setContentText("Ayo Buka kembali aplikasi ini dan nikmati keseruannya")
            .setContentTitle("Alarm Manager")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        val mNotificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

//        menghapus semua notification yang ada
        for (s in mNotificationManager.notificationChannels) {
            mNotificationManager.deleteNotificationChannel(s.id)
        }

//        membuat notification channel dan notify notification
        mNotificationManager.createNotificationChannel(nNotifyChannel)
        mNotificationManager.notify(notifyid, mBuilder.build())
    }
}