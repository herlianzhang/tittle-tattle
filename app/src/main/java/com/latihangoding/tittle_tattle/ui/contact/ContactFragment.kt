package com.latihangoding.tittle_tattle.ui.contact

import android.content.ContentUris
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract.*
import android.provider.ContactsContract.CommonDataKinds.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        initObserver()

        return binding.root
    }

    private fun initObserver() {
        viewModel.contact.observe(viewLifecycleOwner) {
            contactAdapter.submitList(it)
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
                Contacts.PHOTO_ID,
            ),
            null,
            null,
            "${Phone.NUMBER} ASC"
        )

    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null) {
            val contact = mutableListOf<Contact>()
            data.moveToFirst()
            val idColumn = data.getColumnIndex(RawContacts.CONTACT_ID)
            val fullnameColumn = data.getColumnIndex(Contacts.DISPLAY_NAME)
            val phoneNumberColumn = data.getColumnIndex(Phone.NUMBER)
            val photoIdColumn = data.getColumnIndex(Contacts.PHOTO_ID)
            while (data.moveToNext()) {
                val id = data.getInt(idColumn)
                val fullname = data.getString(fullnameColumn)
                val phoneNumber = data.getString(phoneNumberColumn)
                val photoId = data.getLong(photoIdColumn)
                val photoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, photoId)
                val email = getEmail(id)
                val name = getName(id)
                Timber.d(
                    "Masuk id $id name $fullname phonenumber $phoneNumber email $email photoId $photoId photoUri $photoUri name : ${
                        getName(
                            id
                        )
                    }"
                )
                contact.add(
                    Contact(
                        phoneNumber,
                        photoUri,
                        email,
                        fullname,
                        name?.firstname,
                        name?.lastname
                    )
                )
            }
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

}