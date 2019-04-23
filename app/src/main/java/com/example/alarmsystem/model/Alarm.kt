package com.example.alarmsystem.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Alarm(val id:Int = 0,
                 var alarmName: String="None",
                 var soundName: String,
                 var soundUri: String,
                 var isActive:Int=1,
                 var isRepeat:Int=0,
                 var isSound:Int=1,
                 var isVibration:Int=1,
                 var isSnooze:Int = 1,
                 var snoozeInterval:Int = 0,
                 var snoozeRepeat:Int = 0,
                 var date: Long,
                 var repeat: String,
                 var vibration: String):Parcelable

