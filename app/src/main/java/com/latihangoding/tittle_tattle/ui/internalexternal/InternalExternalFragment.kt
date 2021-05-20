package com.latihangoding.tittle_tattle.ui.internalexternal

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.latihangoding.tittle_tattle.databinding.FragmentInternalExternalBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

@AndroidEntryPoint
class InternalExternalFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentInternalExternalBinding

    var spinnerValue = listOf("New File")

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            123 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Toast.makeText(requireContext(), "Izin Diberikan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Izin Ditolak", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        binding.etFilename.isEnabled = position == 0
        if (position != 0) {
            if (binding.switchMain.isChecked)
                readFileExternal(position)
            else
                readFileInternal(position)
        } else {
            binding.etFilename.setText("")
            binding.etNote.text.clear()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInternalExternalBinding.inflate(inflater, container, false)

        binding.spinnerMain.onItemSelectedListener = this

        initListener()
        getInternalFiles()

        return binding.root
    }

    private fun initListener() {
        binding.buttonSave.setOnClickListener {
            if (!binding.switchMain.isChecked)
                writeFileInternal()
            else
                writeFileExternal()

            if (binding.spinnerMain.selectedItemPosition == 0) {

                if (!binding.switchMain.isChecked)
                    getInternalFiles()
                else
                    getExternalFiles()

                binding.etFilename.text.clear()
                binding.etNote.text.clear()
            }
        }

        binding.switchMain.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isExternalStorageReadable() && isChecked) {
                buttonView.isChecked = false
                return@setOnCheckedChangeListener
            }
            if (!isChecked) {
                getInternalFiles()
            } else {
                getExternalFiles()
            }

            binding.switchMain.text = if (isChecked) "External" else "Internal"
        }
    }

    private fun getInternalFiles() {
        val tmp = mutableListOf("New File")
        for (file in activity?.fileList()?.toList() ?: emptyList()) {
            if (file.contains(".txt"))
                tmp.add(file)
        }
        spinnerValue = tmp
        initSpinner()
    }

    private fun writeFileInternal() {
        var filename = binding.etFilename.text.toString()
        if (filename.contains(".txt")) {
            filename = filename.substring(0, filename.indexOf(".txt"))
        }

        activity?.openFileOutput("$filename.txt", Context.MODE_PRIVATE)
                ?.apply {
                    write(binding.etNote.text.toString().trim().toByteArray())
                    close()
                }
        Toast.makeText(requireContext(), "File Save", Toast.LENGTH_SHORT).show()
    }

    private fun readFileInternal(position: Int) {
        val filename = spinnerValue[position]
        binding.etFilename.setText(filename)

        binding.etNote.text.clear()

        try {
            activity?.openFileInput(filename)?.apply {
                var mtext = ""
                bufferedReader().useLines {
                    for (text in it.toList()) {
                        mtext += "$text\n"
                    }
                }
                binding.etNote.setText(mtext.trim())
            }
        } catch (e: FileNotFoundException) {
            binding.etNote.setText("File Not Found")
        } catch (e: IOException) {
            binding.etNote.setText("File Can't be Read")
        }
    }

    private fun getExternalFiles() {
        val tmp = mutableListOf("New File")
        for (file in activity?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.listFiles()
            ?.toList() ?: emptyList()) {
            if (file.toString().contains(".txt"))
                tmp.add(file.name.toString())
        }
        spinnerValue = tmp
        initSpinner()
    }

    private fun writeFileExternal() {
        if (!isExternalStorageReadable()) return
        var filename = binding.etFilename.text.toString()
        if (filename.contains(".txt")) {
            filename = filename.substring(0, filename.indexOf(".txt"))
        }
        val myDir = File(activity?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.toURI())
        if (!myDir.exists()) {
            myDir.mkdir()
        }
        File(myDir, "$filename.txt").apply {
            writeText(binding.etNote.text.toString().trim())
            Toast.makeText(requireContext(), "File Save", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFileExternal(position: Int) {
        if (!isExternalStorageReadable()) return
        val filename = spinnerValue[position]
        val myDir = File(activity?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.toURI())
        var readFile = ""
        binding.etFilename.setText(filename)
        File(myDir, filename).forEachLine(Charsets.UTF_8) {
            readFile += "$it\n"
        }
        binding.etNote.setText(readFile.trim())
    }

    private fun isExternalStorageReadable(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                123
            )
            return false
        }
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            return true
        }
        Toast.makeText(requireContext(), "Storage Sd tidak dapat dibaca", Toast.LENGTH_SHORT).show()
        return false
    }


    private fun initSpinner() {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerValue)
        binding.spinnerMain.adapter = adapter
    }
}