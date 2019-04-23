package com.example.alarmsystem.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.example.alarmsystem.*
import com.example.alarmsystem.database.AlarmRepository
import com.example.alarmsystem.database.DataBaseOpenHelper
import com.example.alarmsystem.model.Shared
import com.example.alarmsystem.model.Vibration
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    private var mediaPlayer = MediaPlayer()
    private var id:Int = 0
    private var isRepeat:Int = 0

    override fun onReceive(context: Context, intent: Intent) {
        id = intent.getIntExtra(ID,0)
        val name = intent.getStringExtra(ALARM_NAME)
        val soundUri = intent.getStringExtra(SOUND_URI)
        val date = intent.getLongExtra(DATE,0)
        isRepeat = intent.getIntExtra(IS_REPEAT,0)
        val isSnooze = intent.getIntExtra(IS_SNOOZE,0)
        val vibration = intent.getStringExtra(VIBRATION)
        val layout = RemoteViews(context.packageName, R.layout.alarm_notification)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        if(name != context.getString(R.string.none)){
            layout.setViewVisibility(R.id.tv_alarm_name, View.VISIBLE)
            layout.setTextViewText(R.id.tv_alarm_name,name)
        }else{
            layout.setViewVisibility(R.id.tv_alarm_name, View.GONE)
        }
        layout.setTextViewText(R.id.tv_time, DateFormat.format("HH:mm",calendar).toString())

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val contentIntent = PendingIntent
            .getActivity(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        layout.setOnClickPendingIntent(R.id.tv_cancel,contentIntent)

        if(isSnooze == 0){
            layout.setViewVisibility(R.id.tv_snooze, View.GONE)
        }else{
            layout.setViewVisibility(R.id.tv_snooze, View.VISIBLE)
        }
        Log.e("Service: ","$id $name $soundUri $date $isSnooze $vibration")

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(id.toString(),name, NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context,id.toString())
            .setSmallIcon(R.drawable.ic_alarm_app)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentIntent(contentIntent)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(layout)
            .setVibrate(Vibration.getPattern(vibration))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)

        manager.notify(1,builder.build())
        if(!soundUri.isNullOrEmpty()){
            startMusic(context, soundUri)
        }
    }

    private fun startMusic(context: Context, uri: String) {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM)
        mediaPlayer.setDataSource(context, Uri.parse(uri))
        mediaPlayer.prepare()
        mediaPlayer.start()
        mediaPlayer.isLooping= true
    }
}