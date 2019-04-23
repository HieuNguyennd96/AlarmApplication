package com.example.alarmsystem.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VibrationItem(var name:String, var isSelected:Boolean=false): Parcelable