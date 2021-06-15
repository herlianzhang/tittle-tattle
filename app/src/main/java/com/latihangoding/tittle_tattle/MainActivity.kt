package com.latihangoding.tittle_tattle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.latihangoding.tittle_tattle.broadcast.AirPlaneReceiver
import com.latihangoding.tittle_tattle.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

//    objek ketika menerima state airplane mode
    private val airPlaneStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
//            ketika airplane hidup maka akan menampilkan UI bahwa airplane dalam keadaan hidup
            val isAirPlaneOn = intent?.getBooleanExtra(AirPlaneReceiver.isAirPlaneOn, false)
            binding.llMain.isVisible = isAirPlaneOn == true
        }
    }

    private val airPlaneReceiver = AirPlaneReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launch {
            val data = intent.getParcelableExtra<User>("data")
            if (data != null) {
                delay(1000)
                mainNavController?.navigate(R.id.chatFragment, Bundle().also {
                    it.putParcelable("data", data)
                })
            }
        }

        // menginisialisasi mobileads
        MobileAds.initialize(this)

// mendaftarkan broadcast receiver bahwa untuk menerima perubahan status airplane
        registerReceiver(
            airPlaneReceiver,
            IntentFilter().also {
                it.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            }
        )
// mendaftarkan receiver state airplane mode sehingga dapat menerima perubahan yang terjadi pada fitur airplane
        registerReceiver(
            airPlaneStateReceiver,
            IntentFilter(AirPlaneReceiver.airPlainState)
        )
    }

//    ketika menghancurkan main activity, unregister fitur airplane
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airPlaneReceiver)
        unregisterReceiver(airPlaneStateReceiver)
    }
}