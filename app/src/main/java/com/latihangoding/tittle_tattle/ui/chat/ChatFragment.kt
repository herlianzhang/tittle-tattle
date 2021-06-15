package com.latihangoding.tittle_tattle.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.latihangoding.tittle_tattle.databinding.FragmentChatBinding
import com.latihangoding.tittle_tattle.utils.AdsPreferences
import com.latihangoding.tittle_tattle.utils.FirebaseConfiguration
import com.latihangoding.tittle_tattle.vo.Chat
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

    private lateinit var adsPreferences: AdsPreferences
    private var mRewardedAd: RewardedAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        adsPreferences = AdsPreferences(requireContext())

        // jika ads tidak di mute maka, ads akan diinisialisasi
        if (!adsPreferences.isMuteAds)
            initAds()
        initObject()
        initAdapter()
        initListener()

        return binding.root
    }

    private fun initAds() {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd

                    mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdShowedFullScreenContent() {
                            mRewardedAd = null
                            initAds() // inisialisasi ads baru setelah selesai menonton reward ads
                        }
                    }
                }
            })
    }

    private fun initObject() {
        val user = args.chatUser

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
            Timber.d("Masuk ivSend = $mRewardedAd")
            val message = binding.etMain.text.toString().trim()

            // cek terlebih dahulu apakah message menggandung kata istimewa dari app ini
            // dan ads tidak di mute
            if (message.contains("tittle tattle", ignoreCase = true) && !adsPreferences.isMuteAds) {
                if (mRewardedAd == null) return@setOnClickListener // jangan jalankan apapun jika ads belum siap

                // meminta konfirmasi kepada user.
                MaterialAlertDialogBuilder(requireContext()).setTitle("Warning")
                    .setMessage("Anda Menggunakan kata Istimewa dalam kalimat ini, jika ingin menggirim pesan ini anda harus menonton iklan terlebih dahulu")
                    .setPositiveButton(
                        "Setuju"
                    ) { _, _ ->
                        // jika user setuju maka ads akan ditampilkan
                        mRewardedAd?.show(requireActivity()) {
                            // jika reward ads sudah di tonton kirim message yang menggandung kata istimewa tersebut.
                            sendChat(message)
                        }
                    }
                    .setNegativeButton("Ngamau aku") { _, _ ->
                        Toast.makeText(requireContext(), "Okela kalau gitu", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .show()
            } else // jika tidak menggandung kata istimewa maka langsung kirim isi chat nya
                sendChat(message)
        }
    }

    // mengirim chat
    private fun sendChat(message: String) {
        if (message.isEmpty()) return

        binding.etMain.text.clear()
        binding.ivSend.isEnabled = false
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