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

    // penampung untuk live data dengan model MyModel
    private val _mData = MutableLiveData<Resource<MyModel>>()
    val mData: LiveData<Resource<MyModel>>
        get() = _mData

    // menjalan fungsi getEntries saat viewmodel diciptakan
    init {
        getEntries()
    }

    private fun getEntries() {
        // menjalankan perintah di thread i/o
        viewModelScope.launch(Dispatchers.IO) {
            // mengambil data dari api secara konkurensi
            homeRep.getEntries().collect {
                _mData.postValue(it)
            }
        }
    }
}
