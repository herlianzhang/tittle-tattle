package com.latihangoding.tittle_tattle.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.latihangoding.tittle_tattle.databinding.FragmentHomeBinding
import com.latihangoding.tittle_tattle.vo.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() { // INI INI FRAGMENTNYA

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private val mAdapter = HomeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initAdapter()
        initObserver()
        return binding.root
    }

    private fun initAdapter() {
        binding.rvMain.adapter = mAdapter
    }

    private fun initObserver() {
        viewModel.mData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.SUCCESS -> {
                    Timber.d("masuk success ${it.data}")
                    mAdapter.submitList(it.data?.entries)
                    binding.loading.isVisible = false
                }
                is Resource.ERROR -> {
                    binding.loading.isVisible = false
                }
                is Resource.LOADING -> {
                    binding.loading.isVisible = true
                }
            }
        }
    }

}