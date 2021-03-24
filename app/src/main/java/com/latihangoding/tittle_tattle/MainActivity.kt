package com.latihangoding.tittle_tattle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.latihangoding.tittle_tattle.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val airPlainStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isAirPlaneOn = intent?.getBooleanExtra(AirPlaneReceiver.isAirPlaneOn, false)
            binding.llMain.isVisible = isAirPlaneOn == true
        }
    }

    private val airPlainReceiver = AirPlaneReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerReceiver(
            airPlainReceiver,
            IntentFilter().also {
                it.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            }
        )

        registerReceiver(
            airPlainStateReceiver,
            IntentFilter(AirPlaneReceiver.airPlainState)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airPlainReceiver)
        unregisterReceiver(airPlainStateReceiver)
    }
}