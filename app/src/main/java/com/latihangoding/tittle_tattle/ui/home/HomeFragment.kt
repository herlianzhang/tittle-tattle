package com.latihangoding.tittle_tattle.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.FragmentHomeBinding
import com.latihangoding.tittle_tattle.vo.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

//Tampilan: Ini mewakili UI aplikasi tanpa Logika Aplikasi apa pun. Itu mengamati ViewModel.
// Ini memberikan aliran data ke View. Ia juga menggunakan hook atau callback untuk memperbarui View. Ini akan meminta data dari Model.

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
        //        binding button fabGallery untuk navigate ke galleryfragment ketika ditekan
        binding.fabGallery.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_galleryFragment)
        }

        //        binding button fabTimer untuk navigate ke timer fragment ketika ditekan
        binding.fabTimer.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_timerFragment)
        }

//        binding button fabcontact untuk navigate ke contact fragment ketika ditekan
        binding.fabContact.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_contactFragment)
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