package com.latihangoding.tittle_tattle.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
class UploadService : JobIntentService(), ProgressListener {
    @Inject
    lateinit var galleryRepository: GalleryRepository

    private val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
        setContentTitle("Picture Upload")
        setContentText("Upload in progress")
        setOnlyAlertOnce(true)

        setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
        priority = NotificationCompat.PRIORITY_HIGH
    }

    lateinit var notificationManager: NotificationManagerCompat

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onHandleWork(intent: Intent) {
        scope.launch {
            Toast.makeText(this@UploadService, "Uploading", Toast.LENGTH_SHORT).show()
        }
        sendBroadcast(Intent(buttonStatus).also {
            it.putExtra(isButtonEnable, false)
        })
        createNotificationChannel()
        notificationManager = NotificationManagerCompat.from(this).apply {
            builder.setProgress(100, 0, false)
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
            builder.setProgress(100, progress.times(100).roundToInt(), false)
            notify(notificationId, builder.build())
        }
    }

    override fun onSuccess(gallery: GalleryModel?) {
        val data = gallery?.copy(addedDate = System.currentTimeMillis()) ?: return

        galleryRepository.insertGallery(data)

        notificationManager.apply {
            builder.setContentText("Download complete")
                .setProgress(0, 0, false)
            notify(notificationId, builder.build())
        }
        sendBroadcast(Intent(buttonStatus).also {
            it.putExtra(isButtonEnable, true)
        })
        Timber.d("Masuk success $data")
    }

    override fun onFail(message: String?) {
        notificationManager.apply {
            builder.setContentText("Download Fail")
                .setProgress(0, 0, false)
            notify(notificationId, builder.build())
        }
        sendBroadcast(Intent(buttonStatus).also {
            it.putExtra(isButtonEnable, true)
        })
        Timber.e("Masuk fail $message")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                CHANNEL_ID,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mChannel.description = "This is default channel used for all other notifications"

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    companion object {
        const val JOB_ID = 2
        const val CHANNEL_ID = "channel_id"
        const val notificationId = 10
        const val buttonStatus = "button_status"
        const val isButtonEnable = "is_button_enable"
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, UploadService::class.java, JOB_ID, intent)
        }
    }
}