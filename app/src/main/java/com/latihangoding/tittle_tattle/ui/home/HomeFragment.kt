package com.latihangoding.tittle_tattle.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.FragmentHomeBinding
import com.latihangoding.tittle_tattle.vo.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    // binding fragmenthomebinding
    private lateinit var binding: FragmentHomeBinding

    // view model
    private val viewModel: HomeViewModel by viewModels()

    // menciptakan objek homeadapter
    private val mAdapter = HomeAdapter()

    // ketika fragment create view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // assign binding fragment ke xml fragment_home
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupAdapter()
        initListener()
        initObserver()

        return binding.root
    }

    private fun initListener() {
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_galleryFragment)
        }
    }

    private fun setupAdapter() {
        // set adapter ke recycler view dengan id rvMain
        binding.rvMain.adapter = mAdapter
    }

    private fun initObserver() {
        // megobservasi data ketika ada perubahan data
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