package com.example.alarmsystem.utility

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.alarmsystem.R
import com.example.alarmsystem.model.Shared
import java.util.*
import java.util.concurrent.TimeUnit

class Utils {
    companion object {
        private val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        fun showKeyBoard(context: Context) {
            val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        fun hideKeyBoard(context: Context, view: View) {
            val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun getDefaultRingtoneUri(): Uri = uri

        fun getDefaultRingtoneTitle(): String {
            val ringtone: Ringtone = RingtoneManager.getRingtone(Shared.context, getDefaultRingtoneUri())
            return ringtone.getTitle(Shared.context)
        }

        fun getTimeLeft(current: Long, dest: Long): String {
            if(dest < current) return ""
            val diffTime = dest - current
            val hour = diffTime / (1000 * 60 * 60)
            val min = (diffTime / (1000 * 60)) % 60
            val sec = (diffTime / 1000) % 60
            var result = "${Shared.context.getString(R.string.time_left)} "
            if (hour > 0) result += "$hour ${Shared.context.getString(R.string.hour)} "
            if (min > 0) result += "$min ${Shared.context.getString(R.string.minutes)} "
            if (sec > 0) result += "$sec ${Shared.context.getString(R.string.second)} "
            return result
        }
    }
}