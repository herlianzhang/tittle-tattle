package com.latihangoding.tittle_tattle.ui.gallery

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.FragmentGalleryBinding
import com.latihangoding.tittle_tattle.service.UploadService
import com.latihangoding.tittle_tattle.ui.media.MediaFragment
import com.latihangoding.tittle_tattle.vo.GalleryModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GalleryFragment : Fragment(), GalleryAdapter.OnClickListener {

    private lateinit var binding: FragmentGalleryBinding
    private val viewModel: GalleryViewModel by viewModels()

    private val galleryAdapter = GalleryAdapter(this)

    private val x = registerForActivityResult(StartActivityForResult()) { result ->
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


//      mendaftarkan listener ke fragment untuk mengambil data untuk di tampilkan
        setFragmentResultListener(MediaFragment.REQUEST_KEY) { _, bundle ->
            val uri = Uri.parse(bundle.getString(MediaFragment.BUNDLE_KEY))
            val mIntent = Intent(requireContext(), UploadService::class.java).also {
                it.data = uri
            }
            UploadService.enqueueWork(requireContext(), mIntent)
        }

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
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/jpg"
//        startForResult.launch(intent)

//    setelah mendapatkan image maka navigate ke action_galleryFragment_to_mediaFragment
        findNavController().navigate(R.id.action_galleryFragment_to_mediaFragment)
    }

    companion object {
        //Permission code
        private const val PERMISSION_CODE = 1001;
    }

    override fun onClick(item: GalleryModel) {
        val builder = MaterialAlertDialogBuilder(requireContext())

        // dialog title
        builder.setTitle("Change url.")

        // dialog message view
        val constraintLayout = getEditTextLayout(requireContext())
        builder.setView(constraintLayout)

        val textInputLayout =
            constraintLayout.findViewWithTag<TextInputLayout>("textInputLayoutTag")
        val textInputEditText =
            constraintLayout.findViewWithTag<TextInputEditText>("textInputEditTextTag")

        textInputEditText.setText(item.imgPath)

        // alert dialog positive button
        builder.setPositiveButton("Submit") { dialog, which ->
            val updatedItem = item.copy(imgPath = textInputEditText.text.toString())
            updatedItem.id = item.id
            viewModel.updateGallery(updatedItem)
            Toast.makeText(requireContext(), "Sukses Update Gallery Url", Toast.LENGTH_SHORT).show()
        }

        // alert dialog other buttons
        builder.setNegativeButton("No", null)
        builder.setNeutralButton("Cancel", null)

        // set dialog non cancelable
        builder.setCancelable(false)

        // finally, create the alert dialog and show it
        val dialog = builder.create()

        dialog.show()
    }

    // get edit text layout
    private fun getEditTextLayout(context: Context): ConstraintLayout {
        val constraintLayout = ConstraintLayout(context)
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        constraintLayout.layoutParams = layoutParams
        constraintLayout.id = View.generateViewId()

        val textInputLayout = TextInputLayout(context)
        textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
        layoutParams.setMargins(
            32.toDp(context),
            8.toDp(context),
            32.toDp(context),
            8.toDp(context)
        )
        textInputLayout.layoutParams = layoutParams
        textInputLayout.hint = "Input url"
        textInputLayout.id = View.generateViewId()
        textInputLayout.tag = "textInputLayoutTag"


        val textInputEditText = TextInputEditText(context)
        textInputEditText.id = View.generateViewId()
        textInputEditText.tag = "textInputEditTextTag"

        textInputLayout.addView(textInputEditText)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintLayout.addView(textInputLayout)
        return constraintLayout
    }


    // extension method to convert pixels to dp
    fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()
}