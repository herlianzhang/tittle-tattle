package com.latihangoding.tittle_tattle.ui.room_chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.FragmentRoomChatBinding
import com.latihangoding.tittle_tattle.utils.FirebaseConfiguration
import com.latihangoding.tittle_tattle.vo.User
import timber.log.Timber

class RoomChatFragment : Fragment(), RoomChatAdapter.OnClickListener {

    private lateinit var binding: FragmentRoomChatBinding

    private val adapter = RoomChatAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoomChatBinding.inflate(inflater, container, false)

        getChatRoom()
        initAdapter()

        return binding.root
    }

    private fun getChatRoom() {
        Firebase.database.getReference(FirebaseConfiguration.USER)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val myUserId = Firebase.auth.uid
                    val mList = mutableListOf<User?>()
                    for (d in snapshot.children) {
                        val k = d.key
                        val v = d.getValue(User::class.java)
                        if (myUserId != k)
                            mList.add(v?.copy(uid = k))
                    }
                    adapter.submitList(mList)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun initAdapter() {
        binding.rvMain.adapter = adapter
    }

    override fun onClick(user: User) {
        // do something
    }

}