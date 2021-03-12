package com.latihangoding.tittle_tattle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import com.latihangoding.tittle_tattle.databinding.ActivityMainBinding
import com.latihangoding.tittle_tattle.vo.MyModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(KEY, binding.tvMain.text.toString())
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        binding.tvMain.text = savedInstanceState?.getString(KEY)?: "Kosong"
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initObserver()
//        binding.buttonMain.setOnClickListener{
//            binding.tvMain.text = binding.etMain.text
//            binding.etMain.setText("")
//        }
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

//        Ketika menekan button, maka akan pindah ke second activity
        binding.buttonMain.setOnClickListener {
            val mIntent  = Intent(this, SecondActivity::class.java)
            mIntent.putExtra(KEY, viewModel.myData.value) //passing data class ke intent menggunakan extra
            startActivity(mIntent) //passing ke second activity
        }
    }

    private fun initObserver() {
        viewModel.myData.observe(this) {
            binding.buttonMain.isEnabled = true
            binding.tvMain.text = "The message is: ${it.message}"
        }
    }
}