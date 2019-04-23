package com.example.alarmsystem.adapter

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alarmsystem.R
import com.example.alarmsystem.model.SoundItem
import kotlinx.android.synthetic.main.item_list.view.*

class SoundAdapter(val context: Context, var sounds: MutableList<SoundItem>) :
    RecyclerView.Adapter<SoundAdapter.SoundItemViewHolder>() {

    private var lastSelectedPosition: Int = -1
    private var enable = true
    private var mediaPlayer = MediaPlayer()

    init {
        lastSelectedPosition = getPosition()
    }

    private fun getPosition(): Int {
        for (i in 0 until sounds.size) {
            if (sounds[i].isSelected) return i
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): SoundItemViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return SoundItemViewHolder(view)
    }

    override fun onBindViewHolder(item: SoundItemViewHolder, pos: Int) {
        item.container.isEnabled = enable
        item.radioBtn.isEnabled = enable
        item.radioBtn.isChecked = lastSelectedPosition == pos
        item.name.isEnabled = enable
        item.name.text = sounds[pos].soundName
    }

    fun getSelectedSound(): SoundItem = sounds[lastSelectedPosition]

    fun enableItem(enable: Boolean){
        this.enable = enable
        if(enable){
            mediaPlayer = MediaPlayer()
        }else{
            if(!enable && isPlaying()) stopMusic()
        }
        notifyDataSetChanged()
    }

    fun playMusic(){
        Log.e("Play music", getSelectedSound().soundUri.toString())
        if(mediaPlayer.isPlaying) stopMusic()
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM)
        mediaPlayer.setDataSource(context,getSelectedSound().soundUri)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    private fun isPlaying():Boolean = mediaPlayer.isPlaying

    fun stopMusic(){
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun getItemCount(): Int = sounds.size

    inner class SoundItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var container: ConstraintLayout = itemView.findViewById(R.id.item_container)
        var radioBtn: AppCompatRadioButton = itemView.findViewById(R.id.rd_select)
        var name: AppCompatTextView = itemView.findViewById(R.id.tv_name)

        init {
            container.setOnClickListener {
                onClick()
            }
            radioBtn.setOnClickListener {
                onClick()
            }
        }

        private fun onClick(){
            lastSelectedPosition = adapterPosition
            notifyDataSetChanged()
            playMusic()
        }
    }
}