package com.latihangoding.tittle_tattle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import com.latihangoding.tittle_tattle.databinding.ActivityMainBinding
import com.latihangoding.tittle_tattle.vo.MyModel
import dagger.hilt.android.AndroidEntryPoint

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

        binding.buttonMain.setOnClickListener {
            val mIntent = Intent(this, SecondActivity::class.java)
            mIntent.putExtra(KEY, viewModel.myData.value)
            startActivity(mIntent)
        }
    }

    private fun initObserver() {
        viewModel.myData.observe(this) {
            binding.buttonMain.isEnabled = true
            binding.tvMain.text = "The message is: ${it.message}"
        }
    }
}