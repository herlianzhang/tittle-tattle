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

        val mModel = intent.getParcelableExtra<MyModel>(KEY) //ciptakan objek sesuai dengan data class yang didapat dari mainactivity
        binding.tvMain.text = mModel?.message // menampilkan text sesuai data class ke object
    }
}

//Key extra untuk data class MyModel
const val KEY = "KEY"