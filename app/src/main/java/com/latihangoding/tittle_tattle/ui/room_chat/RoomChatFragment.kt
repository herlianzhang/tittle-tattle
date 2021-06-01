package com.latihangoding.tittle_tattle.ui.room_chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.FragmentRoomChatBinding

class RoomChatFragment : Fragment() {

    private lateinit var binding: FragmentRoomChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoomChatBinding.inflate(inflater, container, false)

        initListener()

        return binding.root
    }

    private fun initListener() {
        binding.buttonLogout.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_roomChatFragment_to_loginFragment)
        }
    }
}