package com.latihangoding.tittle_tattle.ui.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.FragmentLoginBinding
import com.latihangoding.tittle_tattle.utils.AdsPreferences
import com.latihangoding.tittle_tattle.utils.FirebaseConfiguration
import com.latihangoding.tittle_tattle.vo.User
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private lateinit var adsPreferences: AdsPreferences

    private var premiumUsers = listOf<String>() // mencatat uid premium member

    private val loginResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!) // setelah login berhasil masuk ke fungsi untuk meyimpan data user.
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Google sign in failed", Toast.LENGTH_SHORT).show()
                Timber.e("Masuk fail $e")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        adsPreferences = AdsPreferences(requireContext())

        initObject()
        initListener()
        getPremiumUsers()

        return binding.root
    }

    private fun initObject() {
        // init googlesigninclient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        auth = Firebase.auth
    }

    private fun initListener() {
        binding.buttonLogin.setOnClickListener { // perform login saat button ini ditekan
            googleSignInClient.signOut()
            val signInIntent = googleSignInClient.signInIntent
            loginResult.launch(signInIntent)
        }
    }

    private fun getPremiumUsers() {
        Firebase.database.getReference(FirebaseConfiguration.PREMIUM).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { // fetch uid user premium
                val tmp = mutableListOf<String>()
                for (d in snapshot.children) {
                    tmp.add(d.key.toString())
                }
                premiumUsers = tmp // memasukan data uid user premium ke wadah yang tadi sudah dibuat
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential) // melakukan signin with gogole
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid ?: return@addOnCompleteListener
                    val email = task.result?.user?.email ?: return@addOnCompleteListener
                    val photoUrl = task.result?.user?.photoUrl ?: return@addOnCompleteListener
                    val displayName = task.result?.user?.displayName ?: return@addOnCompleteListener
                    binding.pbLoading.isVisible = true
                    val data = User(displayName, email, photoUrl.toString())
                    Firebase.database.getReference(FirebaseConfiguration.USER).child(userId) // menyimpan data user setelah login
                        .setValue(data).addOnCompleteListener {
                            binding.pbLoading.isVisible = false
                            if (it.isSuccessful) {
                                adsPreferences.isMuteAds = premiumUsers.contains(userId) // jika user merupakan user premium
                                                                                        // maka iklan ditiadakan
                                findNavController().navigate(R.id.action_loginFragment_to_roomChatFragment)
                            } else {
                                Toast.makeText(requireContext(), "Login Fail", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else
                    Toast.makeText(requireContext(), "Google sign in failed", Toast.LENGTH_SHORT)
                        .show()
            }
    }
}