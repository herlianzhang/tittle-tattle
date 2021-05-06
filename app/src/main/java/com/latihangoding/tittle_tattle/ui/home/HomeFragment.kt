package com.latihangoding.tittle_tattle.ui.home

import android.media.AudioAttributes
import android.media.SoundPool
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
class HomeFragment : Fragment(), View.OnClickListener {

    // binding fragmenthomebinding
    private lateinit var binding: FragmentHomeBinding

    // view model
    private val viewModel: HomeViewModel by viewModels()

    // menciptakan objek homeadapter
    private val mAdapter = HomeAdapter()

    private lateinit var soundPool: SoundPool
    var sound = 0

    // ketika fragment create view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // assign binding fragment ke xml fragment_home
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val audioAttrs: AudioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        soundPool = SoundPool.Builder().setMaxStreams(10).setAudioAttributes(audioAttrs).build()

        sound = soundPool.load(requireContext(), R.raw.button_clicked, 1)

        setupAdapter()
        initListener()
        initObserver()

        return binding.root
    }

    private fun initListener() {
        //        binding button fabGallery untuk navigate ke galleryfragment ketika ditekan
        binding.fabGallery.setOnClickListener(this)

        //        binding button fabTimer untuk navigate ke timer fragment ketika ditekan
        binding.fabTimer.setOnClickListener(this)

        //        binding button fabcontact untuk navigate ke contact fragment ketika ditekan
        binding.fabContact.setOnClickListener(this)

        //        binding button fabMusic untuk navigate ke music fragment ketika ditekan
        binding.fabMusic.setOnClickListener(this)
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

//    ketika floating action button ditekan maka akan menjalankan soundpool
    override fun onClick(v: View?) {
        if (sound != 0)
            soundPool.play(sound, 1f, 1f, 0, 0, 1f)

        when (v?.id) {
            R.id.fab_gallery ->
                findNavController().navigate(R.id.action_homeFragment_to_galleryFragment)
            R.id.fab_timer ->
                findNavController().navigate(R.id.action_homeFragment_to_timerFragment)
            R.id.fab_contact ->
                findNavController().navigate(R.id.action_homeFragment_to_contactFragment)
            R.id.fab_music ->
                findNavController().navigate(R.id.action_homeFragment_to_MusicFragment)
        }
    }

}