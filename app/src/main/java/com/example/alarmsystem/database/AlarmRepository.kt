package com.example.alarmsystem.database

import android.content.ContentValues
import com.example.alarmsystem.*
import com.example.alarmsystem.model.Alarm
import com.example.alarmsystem.model.Shared
import org.jetbrains.anko.db.*

class AlarmRepository{

    fun findAll(): ArrayList<Alarm> = Shared.database.use {
        val alarms = arrayListOf<Alarm>()

        select(ALARM_TABLE,ID, ALARM_NAME, SOUND_NAME, SOUND_URI, IS_ACTIVE, IS_REPEAT,
            IS_SOUND, IS_VIBRATION, IS_SNOOZE, SNOOZE_INTERVAL, SNOOZE_REPEAT, DATE, REPEAT, VIBRATION)
            .parseList(object : MapRowParser<ArrayList<Alarm>>{
                override fun parseRow(columns: Map<String, Any?>): ArrayList<Alarm> {
                    val id = columns[ID].toString().toInt()
                    val alarmName = columns[ALARM_NAME].toString()
                    val soundName = columns[SOUND_NAME].toString()
                    val soundUri = columns[SOUND_URI].toString()
                    val isActive = columns[IS_ACTIVE].toString().toInt()
                    val isRepeat = columns[IS_REPEAT].toString().toInt()
                    val isSound = columns[IS_SOUND].toString().toInt()
                    val isVibration = columns[IS_VIBRATION].toString().toInt()
                    val isSnooze = columns[IS_SNOOZE].toString().toInt()
                    val snoozeInterval = columns[SNOOZE_INTERVAL].toString().toInt()
                    val snoozeRepeat = columns[SNOOZE_REPEAT].toString().toInt()
                    val date = columns[DATE].toString().toLong()
                    val repeat = columns[REPEAT].toString()
                    val vibration = columns[VIBRATION].toString()
                    val alarm = Alarm(id = id,
                        alarmName = alarmName,
                        soundName = soundName,
                        soundUri = soundUri,
                        isActive = isActive,
                        isRepeat = isRepeat,
                        isSound = isSound,
                        isVibration = isVibration,
                        isSnooze = isSnooze,
                        snoozeInterval = snoozeInterval,
                        snoozeRepeat = snoozeRepeat,
                        date = date,
                        repeat = repeat,
                        vibration = vibration)

                    alarms.add(alarm)

                    return alarms
                }
        })
        alarms
    }

    fun findById(alarmId: Int):Alarm = Shared.database.use {
        select(ALARM_TABLE,ID, ALARM_NAME, SOUND_NAME, SOUND_URI, IS_ACTIVE, IS_REPEAT,
            IS_SOUND, IS_VIBRATION, IS_SNOOZE, SNOOZE_INTERVAL, SNOOZE_REPEAT, DATE, REPEAT, VIBRATION)
            .whereArgs("$ID = {alarmID}", "alarmID" to alarmId)
            .parseSingle(object : MapRowParser<Alarm>{
                override fun parseRow(columns: Map<String, Any?>): Alarm {
                    val id = columns[ID].toString().toInt()
                    val alarmName = columns[ALARM_NAME].toString()
                    val soundName = columns[SOUND_NAME].toString()
                    val soundUri = columns[SOUND_URI].toString()
                    val isActive = columns[IS_ACTIVE].toString().toInt()
                    val isRepeat = columns[IS_REPEAT].toString().toInt()
                    val isSound = columns[IS_SOUND].toString().toInt()
                    val isVibration = columns[IS_VIBRATION].toString().toInt()
                    val isSnooze = columns[IS_SNOOZE].toString().toInt()
                    val snoozeInterval = columns[SNOOZE_INTERVAL].toString().toInt()
                    val snoozeRepeat = columns[SNOOZE_REPEAT].toString().toInt()
                    val date = columns[DATE].toString().toLong()
                    val repeat = columns[REPEAT].toString()
                    val vibration = columns[VIBRATION].toString()

                    return Alarm(id = id,
                        alarmName = alarmName,
                        soundName = soundName,
                        soundUri = soundUri,
                        isActive = isActive,
                        isRepeat = isRepeat,
                        isSound = isSound,
                        isVibration = isVibration,
                        isSnooze = isSnooze,
                        snoozeInterval = snoozeInterval,
                        snoozeRepeat = snoozeRepeat,
                        date = date,
                        repeat = repeat,
                        vibration = vibration)
                }
            })
    }

    fun add(alarm: Alarm) = Shared.database.use {
        val contentValues = ContentValues()
        contentValues.put(ALARM_NAME,alarm.alarmName)
        contentValues.put(SOUND_NAME,alarm.soundName)
        contentValues.put(SOUND_URI,alarm.soundUri)
        contentValues.put(IS_ACTIVE,alarm.isActive)
        contentValues.put(IS_REPEAT,alarm.isRepeat)
        contentValues.put(IS_SOUND,alarm.isSound)
        contentValues.put(IS_VIBRATION,alarm.isVibration)
        contentValues.put(IS_SNOOZE,alarm.isSnooze)
        contentValues.put(SNOOZE_INTERVAL,alarm.snoozeInterval)
        contentValues.put(SNOOZE_REPEAT,alarm.snoozeRepeat)
        contentValues.put(DATE,alarm.date.toString())
        contentValues.put(REPEAT,alarm.repeat)
        contentValues.put(VIBRATION,alarm.vibration)
        insert(ALARM_TABLE,null,contentValues)
    }

    fun update(alarm: Alarm) = Shared.database.use {
        update(ALARM_TABLE, ALARM_NAME to alarm.alarmName,
                                    SOUND_NAME to alarm.soundName,
                                    SOUND_URI to alarm.soundUri,
                                    IS_ACTIVE to alarm.isActive,
                                    IS_REPEAT to alarm.isRepeat,
                                    IS_SOUND to alarm.isSound,
                                    IS_VIBRATION to alarm.isVibration,
                                    IS_SNOOZE to alarm.isSnooze,
                                    SNOOZE_INTERVAL to alarm.snoozeInterval,
                                    SNOOZE_REPEAT to alarm.snoozeRepeat,
                                    DATE to alarm.date,
                                    REPEAT to alarm.repeat,
                                    VIBRATION to alarm.vibration)
            .where("$ID = {alarmID}", "alarmID" to alarm.id).exec()
    }

    fun unActiveAlarm(id:Int)= Shared.database.use {
        update(ALARM_TABLE,IS_ACTIVE to 0)
            .where("$ID = {alarmID}", "alarmID" to id).exec()

    }

    fun delete(id: Int) = Shared.database.use {
        delete(ALARM_TABLE, "$ID = {alarmID}", "alarmID" to id)
    }

    fun deleteAll() = Shared.database.use {
        delete(ALARM_TABLE,null,null)
    }

    fun dropTable() = Shared.database.use {
        dropTable(ALARM_TABLE,true)
    }
}