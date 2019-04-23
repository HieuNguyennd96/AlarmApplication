package com.example.alarmsystem.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alarmsystem.R
import com.example.alarmsystem.model.Interval
import com.example.alarmsystem.model.Interval.Companion.interval
import com.example.alarmsystem.model.Repeat

class RepeatAdapter(val context: Context, val repeat: Repeat) :
    RecyclerView.Adapter<RepeatAdapter.RepeatItemViewHolder>() {

    private var lastSelectedPosition = getPosition()

    private fun getPosition(): Int = repeat.pos

    private var enable = true
    private var repeats = Repeat.modes

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): RepeatItemViewHolder{
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return RepeatItemViewHolder(view)
    }

    override fun onBindViewHolder(item: RepeatItemViewHolder, pos: Int) {
        item.container.isEnabled = enable
        item.radioBtn.isEnabled = enable
        item.radioBtn.isChecked = lastSelectedPosition == pos
        item.name.isEnabled = enable
        item.name.text = Repeat.getMode(pos)
    }

    fun getSelectedRepeat():Int = lastSelectedPosition

    fun enableItem(enable: Boolean){
        this.enable = enable
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = Repeat.modes.size

    inner class RepeatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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