package com.latihangoding.tittle_tattle.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latihangoding.tittle_tattle.repository.HomeRepository
import com.latihangoding.tittle_tattle.vo.MyModel
import com.latihangoding.tittle_tattle.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRep: HomeRepository) : ViewModel() {

    private val _mData = MutableLiveData<Resource<MyModel>>()
    val mData: LiveData<Resource<MyModel>>
        get() = _mData

    init {
        getEntries()
    }

    private fun getEntries() {
        viewModelScope.launch(Dispatchers.IO) { // INI THREAD NYA
            homeRep.getEntries().collect {
                _mData.postValue(it)
            }
        }
    }
}
