package com.latihangoding.tittle_tattle.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper(context: Context, name: String) {

//    mengambil dan menahan content sesuai preferensi nama file
    private var mPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE)

//    mmendapatkan nilai dari key "IS_LIST" dengan default value true
//    kemudian set value preferences dengan key "IS_LIST" dengan value yang didapatkan di get()
    var isList: Boolean
        get() = mPreference.getBoolean(IS_LIST, true)
        set(value) = mPreference.editPref {
            it.putBoolean(IS_LIST, value)
        }

//    melakukan edit pada shared preferences dan apply
    private inline fun SharedPreferences.editPref(callback: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        callback.invoke(editMe)
        editMe.apply()
    }

    companion object {
        const val IS_LIST = "IS_LIST"
    }
}