package com.example.alarmsystem

import com.example.alarmsystem.model.Shared

const val DB_NAME = "ClockDB"
const val ALARM_TABLE = "tblAlarm"
const val ID = "id"
const val ALARM = "alarm"
const val ALARM_NAME = "name"
const val SOUND_NAME = "sound_name"
const val SOUND_URI = "sound_uri"
const val IS_ACTIVE = "is_active"
const val IS_SOUND = "is_sound"
const val IS_VIBRATION = "is_vibration"
const val IS_REPEAT = "is_repeat"
const val IS_SNOOZE = "is_snooze"
const val SNOOZE_INTERVAL = "snooze_interval"
const val SNOOZE_REPEAT = "snooze_repeat"
const val DATE = "date"
const val REPEAT = "repeat"
const val VIBRATION = "vibration"
const val REQUEST_CODE = 400
const val SOUND_RESULT = 100
const val VIBRATION_RESULT = 101
const val SNOOZE_RESULT = 102
val NONE:String = Shared.context.resources.getString(R.string.none)