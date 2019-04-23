package com.example.alarmsystem.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.alarmsystem.*
import com.example.alarmsystem.model.Shared
import org.jetbrains.anko.db.*

class DataBaseOpenHelper(context: Context) : ManagedSQLiteOpenHelper(context, DB_NAME, null, 3) {

    companion object {
        private var instance: DataBaseOpenHelper? = null

        @Synchronized
        fun getInstance(context: Context): DataBaseOpenHelper {
            if (instance == null) {
                instance = DataBaseOpenHelper(context)
            }
            return instance!!
        }

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(ALARM_TABLE, true,
             ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                      ALARM_NAME to TEXT,
                      SOUND_NAME to TEXT,
                      SOUND_URI to TEXT,
                      IS_ACTIVE to INTEGER,
                      IS_REPEAT to INTEGER,
                      IS_SOUND to INTEGER,
                      IS_VIBRATION to INTEGER,
                      IS_SNOOZE to INTEGER,
                      SNOOZE_INTERVAL to INTEGER,
                      SNOOZE_REPEAT to INTEGER,
                      DATE to TEXT,
                      REPEAT to TEXT,
                      VIBRATION to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.dropTable(ALARM_TABLE,true)
    }

}