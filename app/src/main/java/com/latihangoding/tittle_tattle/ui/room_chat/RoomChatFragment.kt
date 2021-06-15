package com.latihangoding.tittle_tattle.ui.room_chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.FragmentRoomChatBinding
import com.latihangoding.tittle_tattle.utils.AdsPreferences
import com.latihangoding.tittle_tattle.utils.FirebaseConfiguration
import com.latihangoding.tittle_tattle.vo.User

class RoomChatFragment : Fragment(), RoomChatAdapter.OnClickListener {

    private lateinit var binding: FragmentRoomChatBinding

    private val adapter = RoomChatAdapter(this)

    private lateinit var adsPreferences: AdsPreferences
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoomChatBinding.inflate(inflater, container, false)

        adsPreferences = AdsPreferences(requireContext())
        // jika ads tidak di mute maka, ads akan diinisialisasi
        if (!adsPreferences.isMuteAds)
            initAds()

        getChatRoom()
        initAdapter()
        initListener()

        return binding.root
    }

    private fun initAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        InterstitialAd.load(requireContext(),"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
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
        binding.ivAction.setOnClickListener {
            val popupmenu = PopupMenu(requireContext(), it)
            popupmenu.menuInflater.inflate(R.menu.room_menu, popupmenu.menu)

            popupmenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.other -> findNavController().navigate(R.id.action_roomChatFragment_to_homeFragment)
                    R.id.logout -> {
                        Firebase.auth.signOut()
                        findNavController().navigate(R.id.action_roomChatFragment_to_loginFragment)
                    }
                }
                true
            }
            popupmenu.show()
        }
    }

    override fun onClick(user: User) {
        // jangan biarkan user navigasi ke chat jika ads belum siap
        if (mInterstitialAd == null && !adsPreferences.isMuteAds)
            return

        // jika sudah keluar masuk ke chat 5 kali maka ads Interstitial akan ditampilkan
        if (adsPreferences.isInterstitialTime)
            mInterstitialAd?.show(requireActivity())
        else
            goToChat(user)
    }

    private fun goToChat(user: User) {
        val action = RoomChatFragmentDirections.actionRoomChatFragmentToChatFragment(user)
        findNavController().navigate(action)
    }
}