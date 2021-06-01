package com.latihangoding.tittle_tattle.ui.splashscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)

        if (Firebase.auth.currentUser != null)
            findNavController().navigate(R.id.action_splashScreenFragment_to_roomChatFragment)
        else
            findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)

        return binding.root
    }
}