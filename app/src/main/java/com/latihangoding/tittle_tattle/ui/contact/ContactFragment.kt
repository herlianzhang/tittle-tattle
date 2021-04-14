package com.latihangoding.tittle_tattle.ui.contact

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.*
import android.provider.ContactsContract.CommonDataKinds.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.latihangoding.tittle_tattle.databinding.FragmentContactBinding
import com.latihangoding.tittle_tattle.vo.Contact
import com.latihangoding.tittle_tattle.vo.ContactName
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ContactFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var binding: FragmentContactBinding
    private val viewModel: ContactViewModel by viewModels()

    private val contactAdapter = ContactAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)

        LoaderManager.getInstance(this).initLoader(2, null, this)

        binding.rvMain.adapter = contactAdapter

        initListener()
        initObserver()

        return binding.root
    }

    private fun initListener() {
        binding.etSearch.doOnTextChanged { _, _, _, _ ->
            viewModel.contact.value?.let { search(binding.etSearch.text.toString(), it) }
        }
    }

    private fun initObserver() {
        viewModel.contact.observe(viewLifecycleOwner) {
            search(binding.etSearch.text.toString(), it)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            Phone.CONTENT_URI,
            arrayOf(
                RawContacts.CONTACT_ID,
                Contacts.DISPLAY_NAME,
                Phone.NUMBER,
            ),
            null,
            null,
            "${Phone.NUMBER} ASC"
        )

    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null && data.moveToFirst()) {
            val contact = mutableListOf<Contact>()
            data.moveToFirst()
            val idColumn = data.getColumnIndex(RawContacts.CONTACT_ID)
            val fullnameColumn = data.getColumnIndex(Contacts.DISPLAY_NAME)
            val phoneNumberColumn = data.getColumnIndex(Phone.NUMBER)
            do {
                val id = data.getInt(idColumn)
                val fullname = data.getString(fullnameColumn)
                val phoneNumber = data.getString(phoneNumberColumn)
                val email = getEmail(id)
                val name = getName(id)
                val photo = getPhoto(id)
                contact.add(
                    Contact(
                        phoneNumber,
                        photo,
                        email,
                        fullname,
                        name?.firstname,
                        name?.lastname
                    )
                )
            } while (data.moveToNext())
            viewModel.addContact(contact)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }

    private fun getEmail(contactId: Int): String {
        var email = "No Email"
        val cursor = requireContext().contentResolver.query(
            Email.CONTENT_URI, arrayOf(Email.DATA),
            "${RawContacts.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )
        if (cursor?.moveToFirst() == true) {
            email = cursor.getString(cursor.getColumnIndex(Email.DATA))
        }
        cursor?.close()
        return email
    }

    private fun getName(contactId: Int): ContactName? {
        var contactName: ContactName? = null
        val cursor = requireContext().contentResolver.query(
            Data.CONTENT_URI, arrayOf(
                StructuredName.GIVEN_NAME,
                StructuredName.FAMILY_NAME
            ),
            "${RawContacts.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )
        if (cursor?.moveToFirst() == true) {
            val firstname = cursor.getString(cursor.getColumnIndex(StructuredName.GIVEN_NAME))
            val lastname = cursor.getString(cursor.getColumnIndex(StructuredName.FAMILY_NAME))
            contactName = ContactName(
                if (firstname == null || firstname == "2") "No First Name" else firstname,
                lastname ?: "No Last Name"
            )
        }
        cursor?.close()
        return contactName
    }

    private fun getPhoto(contactId: Int): Uri {
        val contactUri: Uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId.toLong())
        return Uri.withAppendedPath(contactUri, Contacts.Photo.DISPLAY_PHOTO)
    }

    private fun search(q: String, contact: List<Contact>) {
        val currContact = contact.filter {
            it.fullname?.contains(q, true) == true || it.email?.contains(
                q,
                true
            ) == true || it.phoneNumber.contains(
                q, true
            )
        }
        Timber.d("masuk search $currContact")
        contactAdapter.submitList(currContact)
    }
}