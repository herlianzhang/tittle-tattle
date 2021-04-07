package com.latihangoding.tittle_tattle.ui.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.latihangoding.tittle_tattle.broadcast.AlarmReceiver
import com.latihangoding.tittle_tattle.databinding.FragmentTimerBinding
import com.latihangoding.tittle_tattle.service.WeatherService
import com.latihangoding.tittle_tattle.service.WeatherService.Companion.JOB_ID
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private val viewModel: TimerViewModel by viewModels()

    private val timerAdapter = TimerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)

        viewModel.checkAlarm()

        initAdapter()
        initListener()
        initObserver()

        return binding.root
    }

    private fun initAdapter() {
        binding.rvMain.adapter = timerAdapter
    }

    private fun initListener() {
        //  untuk kembali ke fragment sebelumnya
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.smAlarm.setOnCheckedChangeListener { v, isChecked ->
            if (v.isPressed) {
                val alarmManager =
                    requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    101,
                    Intent(requireContext(), AlarmReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                if (isChecked) {
                    val alarmTimer = Calendar.getInstance()

                    alarmTimer.add(Calendar.SECOND, 15)
                    alarmManager.setInexactRepeating(
                        AlarmManager.RTC,
                        alarmTimer.timeInMillis,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                        pendingIntent
                    )
                } else {
                    alarmManager.cancel(pendingIntent)
                    pendingIntent.cancel()
                }
                viewModel.checkAlarm()
            }
        }

        binding.smScheduler.setOnCheckedChangeListener { v, isChecked ->
            if (v.isPressed) {
                val jobWeather =
                    requireContext().getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                if (isChecked) {
                    val serviceComponent =
                        ComponentName(requireContext(), WeatherService::class.java)
                    val jobInfo = JobInfo.Builder(JOB_ID, serviceComponent)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setRequiresDeviceIdle(false)
                        .setRequiresCharging(false)
                        .setPeriodic(15 * 1000 * 60)
                        .build()
                    jobWeather.schedule(jobInfo)
                } else {
                    jobWeather.cancel(JOB_ID)
                }
                viewModel.checkScheduler()
            }
        }
    }

    private fun initObserver() {
        viewModel.weathers.observe(viewLifecycleOwner) {
            timerAdapter.submitList(it)
            viewModel.checkScheduler()
        }
        viewModel.isScheduled.observe(viewLifecycleOwner) {
            binding.smScheduler.isChecked = it
        }
        viewModel.isAlarmActive.observe(viewLifecycleOwner) {
            binding.smAlarm.isChecked = it
        }
    }
}