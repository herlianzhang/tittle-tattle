package com.latihangoding.tittle_tattle.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper(context: Context, name: String) {

    private var mPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    var isList: Boolean
        get() = mPreference.getBoolean(IS_LIST, true)
        set(value) = mPreference.editPref {
            it.putBoolean(IS_LIST, value)
        }

    private inline fun SharedPreferences.editPref(callback: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        callback.invoke(editMe)
        editMe.apply()
    }

    companion object {
        const val IS_LIST = "IS_LIST"
    }
}