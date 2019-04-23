package com.example.alarmsystem.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.alarmsystem.*
import com.example.alarmsystem.model.Alarm
import com.example.alarmsystem.utility.Utils

class AlarmController {

    companion object {
        fun createAlarm(context: Context, alarm: Alarm) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)

            intent.putExtra(ID, alarm.id)
            intent.putExtra(ALARM_NAME, alarm.alarmName)
            intent.putExtra(SOUND_URI, alarm.soundUri)
            intent.putExtra(DATE, alarm.date)
            intent.putExtra(IS_REPEAT, alarm.isRepeat)
            intent.putExtra(IS_SNOOZE, alarm.isSnooze)
            intent.putExtra(VIBRATION, alarm.vibration)

            val pendingIntent = PendingIntent.getBroadcast(context, alarm.id, intent, PendingIntent.FLAG_ONE_SHOT)
            if (alarm.isSnooze == 1) {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    alarm.date,
                    alarm.snoozeInterval.toLong() * 60 * 1000,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.date, pendingIntent)
                alarmManager.nextAlarmClock
            }
            val timeLeft = Utils.getTimeLeft(System.currentTimeMillis(), alarm.date)
            if (timeLeft.isNotEmpty()) Toast.makeText(context, timeLeft, Toast.LENGTH_SHORT).show()
        }

        fun cancelAlarm(context: Context, alarm: Alarm) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}