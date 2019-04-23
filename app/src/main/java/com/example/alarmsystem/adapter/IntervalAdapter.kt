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
import com.example.alarmsystem.model.Interval
import com.example.alarmsystem.model.Vibration
import com.example.alarmsystem.model.VibrationItem
import kotlinx.android.synthetic.main.item_list.view.*

class IntervalAdapter(val context: Context, val interval: Interval) :
    RecyclerView.Adapter<IntervalAdapter.IntervalItemViewHolder>() {

    private var lastSelectedPosition = getPosition()

    private fun getPosition(): Int = interval.pos

    private var enable = true
    private var intervals = Interval.modes

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): IntervalItemViewHolder{
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return IntervalItemViewHolder(view)
    }

    override fun onBindViewHolder(item: IntervalItemViewHolder, pos: Int) {
        item.container.isEnabled = enable
        item.radioBtn.isEnabled = enable
        item.radioBtn.isChecked = lastSelectedPosition == pos
        item.name.isEnabled = enable
        item.name.text = Interval.getMode(pos)
    }

    fun getSelectedInterval():Int = lastSelectedPosition

    fun enableItem(enable: Boolean){
        this.enable = enable
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = Interval.modes.size

    inner class IntervalItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
        }
    }
}