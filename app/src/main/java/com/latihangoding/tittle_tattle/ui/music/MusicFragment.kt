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

//    binding fragment music binding
    private lateinit var binding: FragmentMusicBinding

//    view model
    private val viewModel: MusicViewModel by viewModels()

    private val musicAdapter = MusicAdapter(this)

    private val mediaPlayer = mutableMapOf<Int, MediaPlayer?>()

    private lateinit var soundPool: SoundPool
    var sound = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // assign binding fragment ke xml fragment_music
        binding = FragmentMusicBinding.inflate(layoutInflater, container, false)

//        build audio attribute untuk terima tipe kontet musik dan usage media
        val audioAttrs: AudioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

//        build soundpool dengan 10 soundpool maksimal yang dapat dimainkan secara bersamaan
        soundPool = SoundPool.Builder().setMaxStreams(10).setAudioAttributes(audioAttrs).build()

//        soundpool load data audio dengan nama file "button_clicked.mp3"
        sound = soundPool.load(requireContext(), R.raw.button_clicked, 1)

        initAdapter()
        initListener()
        initObserver()

        return binding.root
    }

//    membinding media adapter dengan musicadapter
    private fun initAdapter() {
        binding.rvMedia.adapter = musicAdapter
    }

//    ketika floating action button di klik maka akan memunculkan suara dari soundpool yang telah di load
    private fun initListener() {
        binding.fabIsList.setOnClickListener {
            if (sound != 0)
                soundPool.play(sound, 1f, 1f, 0, 0, 1f)
            viewModel.checkIsList(true)
        }
    }

    private fun initObserver() {
        // megobservasi data ketika ada perubahan data
        viewModel.audios.observe(viewLifecycleOwner) {
            musicAdapter.submitList(it)
        }

//        view model untuk mengobservasi button layoutManager, apabila aktif maka ditampilkan secara view list, bila tidak aktif maka ditampilkan secara grid
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

    //  menghancurkan juga semua media player yang berjalan
    private fun removeAudio() {
        mediaPlayer.forEach {
            it.value?.release()
        }
        mediaPlayer.clear()
    }

//    menjalankan media player
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

//    memberhentikan audio pada media player yang sedang aktif
    override fun stopAudio(position: Int) {
        Timber.d("Pause position $position")
        mediaPlayer[position]?.pause()
    }


//    menjalankan func removeAudio ketika musicFragment destroyed
    override fun onDestroy() {
        super.onDestroy()
        removeAudio()
    }
}