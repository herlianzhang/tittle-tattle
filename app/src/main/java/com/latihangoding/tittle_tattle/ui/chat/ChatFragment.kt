package com.latihangoding.tittle_tattle.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.latihangoding.tittle_tattle.databinding.FragmentChatBinding
import com.latihangoding.tittle_tattle.utils.FirebaseConfiguration
import com.latihangoding.tittle_tattle.vo.Chat
import com.latihangoding.tittle_tattle.vo.User
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val args: ChatFragmentArgs by navArgs()

    private val myUserId = Firebase.auth.uid.toString()
    private lateinit var receiverUserId: String

    private lateinit var chatId: String

    private val adapter = ChatAdapter()

    val sdf = SimpleDateFormat("dd MMM yy", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        initObject()
        initAdapter()
        initListener()

        return binding.root
    }

    private fun initObject() {
        val argumentUser = arguments?.getParcelable<User>("data")

        val user = argumentUser ?: args.chatUser

        receiverUserId = user.uid.toString()
        Glide.with(requireContext()).load(user.photoUrl).into(binding.ivAvatar)
        binding.tvName.text = user.name

        chatId = createChatId(receiverUserId, myUserId)

        Firebase.database.getReference(FirebaseConfiguration.CHAT).child(chatId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var currentDate = ""
                    var currentDatePosition = 0L
                    val today = sdf.format(Date(System.currentTimeMillis()))
                    val mList = mutableListOf<ItemData>()
                    for (d in snapshot.children) {
                        val k = d.key
                        val v = d.getValue(Chat::class.java)
                        val curr = v?.copy(timestamp = k)
                        if (curr != null) {
                            val date = sdf.format(Date(k?.toLongOrNull() ?: 0L))
                            if (date != currentDate) {
                                currentDate = date
                                val tmp = if (date == today) "Today" else date
                                mList.add(ItemData.Separator(tmp, currentDatePosition))
                                currentDatePosition += 1
                            }

                            mList.add(ItemData.Content(curr))
                        }
                    }
                    submitList(mList.reversed())

                    binding.pbLoading.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun initAdapter() {
        binding.rvMain.adapter = adapter
    }

    private fun initListener() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivSend.setOnClickListener {
            val message = binding.etMain.text.toString().trim()
            if (message.isNotEmpty()) {
                sendChat(message)
                binding.etMain.text.clear()
                binding.ivSend.isEnabled = false
            }
        }
    }

    private fun sendChat(message: String) {
        val chat = Chat(message, receiverUserId, myUserId, isPending = true)

        updatePendingChat(chat)

        Firebase.database.getReference(FirebaseConfiguration.CHAT).child(chatId)
            .child(System.currentTimeMillis().toString()).setValue(chat).addOnCompleteListener {
                binding.ivSend.isEnabled = true
            }
    }

    private fun updatePendingChat(chat: Chat) {
        val currList = adapter.currentList.toMutableList()
        currList.add(0, ItemData.Content(chat))

        submitList(currList)
    }

    private fun submitList(list: List<ItemData>) {
        adapter.submitList(list)

        Timber.d("Masuk submitlist $list")

        if (list.isNotEmpty())
            binding.rvMain.smoothScrollToPosition(0)
    }

    private fun createChatId(uid1: String, uid2: String): String {
        return if (uid1 > uid2)
            "$uid1-$uid2"
        else
            "$uid2-$uid1"
    }
}