package com.latihangoding.tittle_tattle.service

import android.app.job.JobParameters
import android.app.job.JobService
import com.latihangoding.tittle_tattle.repository.WeatherRepository
import com.latihangoding.tittle_tattle.vo.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class WeatherService : JobService() {
    @Inject
    lateinit var weatherRepository: WeatherRepository

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.d("Masuk onStartJob")
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepository.fetchWeather().collect {
                when (it) {
                    is Resource.SUCCESS -> {
                        val data = it.data?.weathers ?: return@collect
                        for (weather in data) {
                            weatherRepository.insertWeather(weather.copy(addedDate = System.currentTimeMillis()))
                        }
                        Timber.d("Masuk success pak eko")
                    }
                    is Resource.ERROR -> {
                        Timber.d("Masuk error pak eko")
                    }
                    is Resource.LOADING -> {
                        Timber.d("Masuk loading pak eko")
                    }
                }
                jobFinished(params, true)
            }
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Timber.d("Masuk onStopJob")
        return true
    }

    companion object {
        const val JOB_ID = 3
    }
}