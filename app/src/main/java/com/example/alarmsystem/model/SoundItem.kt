package com.example.alarmsystem.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoundItem(var soundName:String, var soundUri:Uri= Uri.parse(""), var isSelected:Boolean=false):Parcelable