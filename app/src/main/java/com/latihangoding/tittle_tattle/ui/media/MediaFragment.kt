package com.latihangoding.tittle_tattle.ui.media

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import com.latihangoding.tittle_tattle.databinding.FragmentMediaBinding

// Loader
class MediaFragment : Fragment(), LoaderManager.LoaderCallbacks<List<Uri>>,
    MediaAdapter.OnClickListener {

    private lateinit var binding: FragmentMediaBinding
    private val viewModel: MediaViewModel by viewModels()

    private val mediaAdapter = MediaAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaBinding.inflate(inflater, container, false)

        binding.rvMain.adapter = mediaAdapter

        LoaderManager.getInstance(this).initLoader(1, null, this)

        initObserver()

        return binding.root
    }

    private fun initObserver() {
        viewModel.media.observe(viewLifecycleOwner) {
            mediaAdapter.submitList(it)
        }
    }

    override fun onClick(uri: Uri) {
        setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to uri.toString()))
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY = "request_key"
        const val BUNDLE_KEY = "bundle_key"
    }

    override fun onCreateLoader(id: Int, args: Bundle?) =
        MediaAsyncTaskLoader(requireContext())


    override fun onLoadFinished(loader: Loader<List<Uri>>, data: List<Uri>) {
        viewModel.setMedia(data)
    }

    override fun onLoaderReset(loader: Loader<List<Uri>>) {

    }
}

// AsyncTaskLoader
// ref: https://riptutorial.com/android/example/15330/basic-asynctaskloader
class MediaAsyncTaskLoader(context: Context) : AsyncTaskLoader<List<Uri>>(context) {
    override fun loadInBackground(): List<Uri> {
        val currentMedia = mutableListOf<Uri>()
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Images.Media._ID
            ),
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )
        if (cursor != null) {
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                currentMedia.add(uri)
            }
        }
        cursor?.close()
        return currentMedia
    }

    override fun deliverResult(data: List<Uri>?) {
        if (isStarted)
            super.deliverResult(data)
    }

    override fun onStartLoading() {
        forceLoad()
    }

    override fun onReset() {
        super.onReset()

        onStopLoading()
    }
}