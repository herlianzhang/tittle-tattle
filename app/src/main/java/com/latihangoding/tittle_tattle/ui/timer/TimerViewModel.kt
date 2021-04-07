package com.latihangoding.tittle_tattle.ui.timer

import android.app.Application
import android.app.PendingIntent
import android.app.job.JobScheduler
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.latihangoding.tittle_tattle.broadcast.AlarmReceiver
import com.latihangoding.tittle_tattle.repository.WeatherRepository
import com.latihangoding.tittle_tattle.service.WeatherService.Companion.JOB_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(app: Application, weatherRep: WeatherRepository) :
    AndroidViewModel(app) {
    val weathers = weatherRep.getAllWeather

    val isScheduled = MutableLiveData(isJobIdRunning())
    val isAlarmActive = MutableLiveData(isAlarmRunning())

    private fun isJobIdRunning(): Boolean {
        val jobScheduler =
            getApplication<Application>().applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        for (job in jobScheduler.allPendingJobs) {
            if (job.id == JOB_ID) return true
        }
        return false
    }

    private fun isAlarmRunning(): Boolean {
        val context = getApplication<Application>().applicationContext
        return PendingIntent.getBroadcast(
            context,
            101,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    fun checkScheduler() {
        isScheduled.postValue(isJobIdRunning())
    }

    fun checkAlarm() {
        isAlarmActive.postValue(isAlarmRunning())
    }
}
