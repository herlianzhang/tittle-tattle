package com.latihangoding.tittle_tattle.ui.media

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.content.Loader
import com.latihangoding.tittle_tattle.databinding.FragmentMediaBinding

class MediaFragment : Fragment() {

    private lateinit var binding: FragmentMediaBinding

    private val mediaAdapter = MediaAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaBinding.inflate(inflater, container, false)

        binding.rvMain.adapter = mediaAdapter

        return binding.root
    }
}

class MediaLoader(context: Context) : android.content.Loader<Cursor>(context) {

}