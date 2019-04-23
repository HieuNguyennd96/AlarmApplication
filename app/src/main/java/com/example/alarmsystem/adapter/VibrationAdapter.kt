package com.example.alarmsystem.adapter

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alarmsystem.R
import com.example.alarmsystem.model.Vibration
import com.example.alarmsystem.model.VibrationItem
import kotlinx.android.synthetic.main.item_list.view.*

class VibrationAdapter(val context: Context, var vibrations: MutableList<VibrationItem>) :
    RecyclerView.Adapter<VibrationAdapter.VibrationItemViewHolder>() {

    private var lastSelectedPosition = getPosition()
    private var enable = true
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    private fun getPosition(): Int {
        for (i in 0 until vibrations.size) {
            if (vibrations[i].isSelected) return i
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): VibrationItemViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return VibrationItemViewHolder(view)
    }

    override fun onBindViewHolder(item: VibrationItemViewHolder, pos: Int) {
        item.container.isEnabled = enable
        item.radioBtn.isEnabled = enable
        item.radioBtn.isChecked = lastSelectedPosition == pos
        item.name.isEnabled = enable
        item.name.text = vibrations[pos].name
    }

    fun getSelectedVibration():VibrationItem = vibrations[lastSelectedPosition]

    fun enableItem(enable: Boolean){
        this.enable = enable
        notifyDataSetChanged()
    }


    private fun vibrate(){
        val pattern:LongArray = Vibration.getPattern(getSelectedVibration().name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        }else{
            for(i in 0 until pattern.size){
                if(i%2==0){
                    delay(pattern[i])
                }else{
                    vibrator.vibrate(pattern[i])
                }
            }
        }
    }

    private fun delay(long:Long){
        Handler().postDelayed({

        },long)
    }

    override fun getItemCount(): Int = vibrations.size

    inner class VibrationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
            vibrate()
        }
    }
}