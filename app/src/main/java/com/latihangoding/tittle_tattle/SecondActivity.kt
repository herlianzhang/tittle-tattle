package com.latihangoding.tittle_tattle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.latihangoding.tittle_tattle.databinding.ActivitySecondBinding
import com.latihangoding.tittle_tattle.vo.MyModel

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mModel = intent.getParcelableExtra<MyModel>(KEY)
        binding.tvMain.text = mModel?.message
    }
}

const val KEY = "KEY"