package com.latihangoding.tittle_tattle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.latihangoding.tittle_tattle.vo.MyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//Kelas ViewModel dirancang untuk menyimpan dan mengelola data terkait UI dengan cara sadar siklus hidup.
//Kelas ViewModel memungkinkan data bertahan dari perubahan konfigurasi seperti rotasi layar.
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _myData = MutableLiveData<MyModel>()
    val myData: LiveData<MyModel>
        get() = _myData

    fun addWord(data: MyModel) {
        _myData.postValue(data)
    }
}
