package com.latihangoding.tittle_tattle.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.api.ProgressListener
import com.latihangoding.tittle_tattle.repository.GalleryRepository
import com.latihangoding.tittle_tattle.vo.GalleryModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class UploadService : JobIntentService(),
    ProgressListener { // menginheritance progresslistener untuk progress upload
    @Inject
    lateinit var galleryRepository: GalleryRepository

    //    membuat notifikasi upload
    private lateinit var notificationView: RemoteViews
    private lateinit var builder: NotificationCompat.Builder

    private lateinit var notificationManager: NotificationManagerCompat

    //   thread
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate() {
        super.onCreate()
        notificationView = RemoteViews(packageName, R.layout.notification_view)
//        membuat aksi notifikasi ketika ditekan, yaitu menuju gallery ketika upload selesai
        notificationView.setOnClickPendingIntent(R.id.button_go_to_gallery, pendingIntent())
        builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setStyle(NotificationCompat.DecoratedCustomViewStyle())
            setCustomContentView(notificationView)
            setOnlyAlertOnce(true)
            setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
            priority = NotificationCompat.PRIORITY_HIGH
        }
    }

//    berfungsi untuk berpindah ke fragment gallery
    // explicit deep link, ref: https://developer.android.com/guide/navigation/navigation-deep-link#explicit
    private fun pendingIntent(): PendingIntent {
        return NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.galleryFragment)
            .createPendingIntent()
    }

    override fun onHandleWork(intent: Intent) {

        scope.launch {
            Toast.makeText(this@UploadService, "Uploading", Toast.LENGTH_SHORT).show()
        }
//        mengirim broadcast untuk disable button
        sendBroadcast(Intent(buttonStatus).also {
            it.putExtra(isButtonEnable, false)
        })
        createNotificationChannel()
        notificationManager = NotificationManagerCompat.from(this).apply {
//            builder.setProgress(100, 0, false)
            notificationView.setTextViewText(R.id.tv_title, "Progress")
            notify(notificationId, builder.build())
        }

        val imageUri = intent.data
        if (imageUri != null) {
            scope.launch {
                galleryRepository.upload(imageUri, this@UploadService)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Timber.d("masuk service terdestroy")
    }

    override fun onProgress(progress: Float) {
        Timber.d("Masuk progress $progress")
        notificationManager.apply {
//            builder.setProgress(100, progress.times(100).roundToInt(), false)
            notificationView.setProgressBar(
                R.id.pb_main,
                100,
                progress.times(100).roundToInt(),
                false
            )
            notify(notificationId, builder.build())
        }
    }

    //    notifikasi ketika download telah berhasil
    override fun onSuccess(gallery: GalleryModel?) {
        val data = gallery?.copy(addedDate = System.currentTimeMillis()) ?: return

        galleryRepository.insertGallery(data)

        notificationManager.apply {
//            builder.setContentText("Download complete")
//                .setProgress(0, 0, false)
            notificationView.setTextViewText(R.id.tv_title, "Success")
            notificationView.setViewVisibility(R.id.iv_main, View.VISIBLE)
            notificationView.setViewVisibility(R.id.pb_main, View.GONE)

            Glide.with(this@UploadService).asBitmap().load(gallery.imgPath)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        notificationView.setImageViewBitmap(R.id.iv_main, resource)
                        notify(notificationId, builder.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        Timber.d("onLoadcleared")
                    }
                })


            notify(notificationId, builder.build())
        }
        //        mengirim broadcast untuk enable button
        sendBroadcast(Intent(buttonStatus).also {
            it.putExtra(isButtonEnable, true)
        })
        Timber.d("Masuk success $data")
    }

    //    notifikasi ketika download fail
    override fun onFail(message: String?) {
        notificationManager.apply {
//            builder.setContentText("Download Fail")
//                .setProgress(0, 0, false)

            notificationView.setTextViewText(R.id.tv_title, "fail")
            notify(notificationId, builder.build())
        }
        //        mengirim broadcast untuk enable button
        sendBroadcast(Intent(buttonStatus).also {
            it.putExtra(isButtonEnable, true)
        })
        Timber.e("Masuk fail $message")
    }

    //    membuat sebuah notifikasi channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                CHANNEL_ID,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mChannel.setShowBadge(true)
            mChannel.description = "This is default channel used for all other notifications"

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    //    objek untuk android oreo, jika diatas android oreo tidak perlu membuat notifikasi channel
    companion object {
        private const val JOB_ID = 2
        const val CHANNEL_ID = "channel_id"
        const val notificationId = 10
        const val buttonStatus = "button_status"
        const val isButtonEnable = "is_button_enable"
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, UploadService::class.java, JOB_ID, intent)
        }
    }
}