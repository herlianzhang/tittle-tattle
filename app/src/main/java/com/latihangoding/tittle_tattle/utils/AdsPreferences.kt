package com.latihangoding.tittle_tattle.utils

import android.content.Context
import androidx.core.content.edit

class AdsPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(ADS, Context.MODE_PRIVATE)

    val isInterstitialTime: Boolean
        get() {
            // mengambil total sudah berapa kali masuk
            val tmp = sharedPreferences.getInt(INTERSTITIAL_COUNT, 1)
            var isTrue = false
            sharedPreferences.edit {
                putInt(
                    INTERSTITIAL_COUNT, if (tmp == 5) {
                        // jika sudah 5 kali, mengubah istrue menjadi true
                        // kemudian set INTERSTITIAL_COUNT kembali ke 0
                        isTrue = true
                        0
                    } else {
                        // istrue tetap false
                        // INTERSTITIAL_COUNT tetambah nilai dari sebelumnya
                        tmp + 1
                    }
                )
            }
            // mengembalikan nilai dari isTrue
            return isTrue
        }

    // shared preferences untuk Fitur untuk menghilangkan iklan
    var isMuteAds: Boolean
        get() = sharedPreferences.getBoolean(IS_MUTE_ADS, false)
        set(value) {
            sharedPreferences.edit {
                putBoolean(IS_MUTE_ADS, value)
            }
        }

    companion object {
        private const val ADS = "ads"
        private const val INTERSTITIAL_COUNT = "interstitial_count"
        private const val IS_MUTE_ADS = "is_mute_ads"
    }
}