package com.latihangoding.tittle_tattle.ui.music

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.FragmentMusicBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MusicFragment : Fragment(), MusicAdapter.AudioListener {

    private lateinit var binding: FragmentMusicBinding

    private val viewModel: MusicViewModel by viewModels()

    private val musicAdapter = MusicAdapter(this)

    private val mediaPlayer = mutableMapOf<Int, MediaPlayer?>()

    private lateinit var soundPool: SoundPool
    var sound = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicBinding.inflate(layoutInflater, container, false)

        val audioAttrs: AudioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        soundPool = SoundPool.Builder().setMaxStreams(10).setAudioAttributes(audioAttrs).build()

        sound = soundPool.load(requireContext(), R.raw.button_clicked, 1)

        initAdapter()
        initListener()
        initObserver()

        return binding.root
    }

    private fun initAdapter() {
        binding.rvMedia.adapter = musicAdapter
    }

    private fun initListener() {
        binding.fabIsList.setOnClickListener {
            if (sound != 0)
                soundPool.play(sound, 1f, 1f, 0, 0, 1f)
            viewModel.checkIsList(true)
        }
    }

    private fun initObserver() {
        viewModel.audios.observe(viewLifecycleOwner) {
            musicAdapter.submitList(it)
        }
        viewModel.isList.observe(viewLifecycleOwner) { isList ->
            if (isList) {
                binding.rvMedia.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.fabIsList.setImageResource(R.drawable.ic_baseline_view_list_24)
            } else {
                binding.rvMedia.layoutManager =
                    GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                binding.fabIsList.setImageResource(R.drawable.ic_baseline_grid_on_24)
            }
            binding.rvMedia.adapter = musicAdapter
        }
    }

    private fun removeAudio() {
        mediaPlayer.forEach {
            it.value?.release()
        }
        mediaPlayer.clear()
    }

    override fun startAudio(uri: Uri, position: Int) {
        if (mediaPlayer[position] == null) {
            mediaPlayer[position] = MediaPlayer.create(requireContext(), uri)
            mediaPlayer[position]?.setOnCompletionListener {
                musicAdapter.onCompleteListener()
                mediaPlayer[position]?.release()
                mediaPlayer.remove(position)
            }
        }
        mediaPlayer[position]?.start()
    }

    override fun stopAudio(position: Int) {
        Timber.d("Pause position $position")
        mediaPlayer[position]?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeAudio()
    }
}