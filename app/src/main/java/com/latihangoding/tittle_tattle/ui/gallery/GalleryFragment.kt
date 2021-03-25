package com.latihangoding.tittle_tattle.ui.gallery

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.broadcast.AirPlaneReceiver
import com.latihangoding.tittle_tattle.databinding.FragmentGalleryBinding
import com.latihangoding.tittle_tattle.service.UploadService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private val viewModel: GalleryViewModel by viewModels()

    private val galleryAdapter = GalleryAdapter()

    private val startForResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                val mIntent = Intent(requireContext(), UploadService::class.java).also {
                    it.data = imageUri
                }
                UploadService.enqueueWork(requireContext(), mIntent)
            }
        }
    }

// untuk menerima broadcast receiver yang terjadi perubahan di proses sehinga mengubah status button tersebut
    private val buttonStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isButtonEnable = intent?.getBooleanExtra(UploadService.isButtonEnable, true)
            binding.fab.isEnabled = isButtonEnable == true
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d("requestCode $requestCode\npermissions $permissions\ngrantResults $grantResults")
        var isAllGranted = true
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false
                break
            }
        }
        if (isAllGranted) {
            pickImageFromGallery()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)

        setupAdapter()
        initListener()
        initObserver()

        requireContext().registerReceiver(
            buttonStatusReceiver,
            IntentFilter(UploadService.buttonStatus)
        )

        return binding.root
    }

//    mengunregister receiver button status ketika fragment dihancurkan
    override fun onDestroy() {
        super.onDestroy()
        requireContext().unregisterReceiver(buttonStatusReceiver)
    }

    private fun setupAdapter() {
        binding.rvMain.adapter = galleryAdapter
    }

    private fun initListener() {
//  untuk kembali ke fragment sebelumnya
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
//        request permission kemudian membuka gallery untuk mengambil gambar
        binding.fab.setOnClickListener {
            requestPermission()
        }
    }

    private fun initObserver() {
//        ketika menerima update dari database (room) kemudian menampilkan data tersebut di recycle view
        viewModel.galleryList.observe(viewLifecycleOwner) {
            binding.tvNoData.isVisible = it.isEmpty()
            galleryAdapter.submitList(it)
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                //permission already granted
                pickImageFromGallery()
            }
        } else {
            //system OS is < Marshmallow
            pickImageFromGallery();
        }
    }

//    setelah permission telah didapat, akan memanggil fungsinya untuk mengambil foto dari gallery
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/jpg"
        startForResult.launch(intent)
    }

    companion object {
        //Permission code
        private const val PERMISSION_CODE = 1001;
    }
}