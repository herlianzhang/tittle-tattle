package com.latihangoding.tittle_tattle.ui.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.latihangoding.tittle_tattle.vo.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor() : ViewModel() {
    private val _contact = MutableLiveData<List<Contact>>()
    val contact: LiveData<List<Contact>>
        get() = _contact

    fun addContact(contact: List<Contact>) {
        _contact.postValue(contact)
    }
}