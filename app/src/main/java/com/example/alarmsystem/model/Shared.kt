package com.example.alarmsystem.model

import android.content.Context
import com.example.alarmsystem.database.DataBaseOpenHelper

class Shared{
    companion object {
        lateinit var context:Context
        lateinit var database:DataBaseOpenHelper
    }
}