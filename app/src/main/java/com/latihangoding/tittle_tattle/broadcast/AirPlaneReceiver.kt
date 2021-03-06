package com.latihangoding.tittle_tattle.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.provider.Settings.Global.AIRPLANE_MODE_ON

class AirPlaneReceiver : BroadcastReceiver() {

//    function ketika airplane telah terjadi aksi, maka melakukan broadcast ke apk untuk memberitahu bahwa user sedang aktif airplane mode
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
//           mengirimkan broadcast status airplane
            val isOn = Settings.System.getInt(context.contentResolver, AIRPLANE_MODE_ON, 0) == 1
            context.sendBroadcast(
                Intent(airPlainState).also {
                    it.putExtra(isAirPlaneOn, isOn)
                }
            )
        }
    }

    companion object {
        const val airPlainState = "AIRPLAINSTATE"
        const val isAirPlaneOn = "ISAIRPLANEON"
    }
}