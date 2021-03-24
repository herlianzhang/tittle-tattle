package com.latihangoding.tittle_tattle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.latihangoding.tittle_tattle.vo.MyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _myData = MutableLiveData<MyModel>()
    val myData: LiveData<MyModel>
        get() = _myData

    fun addWord(data: MyModel) {
        _myData.postValue(data)
    }
}
