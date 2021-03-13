package com.latihangoding.tittle_tattle

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.latihangoding.tittle_tattle.databinding.ActivityMainBinding
import com.latihangoding.tittle_tattle.vo.MyModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initObserver()
    }

    private fun initListener() {
        binding.etMain.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val message = binding.etMain.text.toString().trim()
                if (message.isNotEmpty()) {
                    viewModel.addWord(MyModel(message))
                    binding.etMain.setText("")
                }
            }
            true
        }

        lifecycleScope.launch {  }

//        Ketika menekan button, maka akan pindah ke second activity
        binding.buttonMain.setOnClickListener {
            val mIntent = Intent(this, SecondActivity::class.java)
            mIntent.putExtra(
                KEY,
                viewModel.myData.value
            ) //passing data class ke intent menggunakan extra
            startActivity(mIntent) //passing ke second activity
        }
    }

    private fun initObserver() {
//  LiveData adalah kelas pemegang data yang dapat diamati. Tidak seperti observable biasa, LiveData peka terhadap siklus proses, artinya LiveData menghormati siklus proses komponen aplikasi lain, seperti aktivitas,
//  fragmen, atau layanan. Kesadaran ini memastikan LiveData hanya memperbarui pengamat komponen aplikasi yang berada dalam status siklus proses aktif.
        viewModel.myData.observe(this) {
            binding.buttonMain.isEnabled = true
            binding.tvMain.text = "The message is: ${it.message}"
        }
    }
}