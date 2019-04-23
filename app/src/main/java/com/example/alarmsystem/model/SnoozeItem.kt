package com.example.alarmsystem.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SnoozeItem(var interval:Int, var repeat:Int): Parcelable