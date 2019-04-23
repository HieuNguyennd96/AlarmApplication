package com.example.alarmsystem.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alarmsystem.AlarmInfoActivity
import com.example.alarmsystem.R
import com.example.alarmsystem.database.AlarmRepository
import com.example.alarmsystem.model.Alarm
import com.example.alarmsystem.services.AlarmController
import java.text.SimpleDateFormat
import java.util.*

class AlarmAdapter(var context: Context, var alarms: MutableList<Alarm>) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    private var deleteMode = false
    private var selectAll = false
    private var selectedList = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): AlarmViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    override fun getItemCount(): Int = alarms.size

    override fun onBindViewHolder(item: AlarmViewHolder, pos: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = alarms[pos].date
        Log.e("Repeat", alarms[pos].repeat)

        item.container.setOnClickListener {
            if (deleteMode) {
                item.select.isChecked = !item.select.isChecked
            } else {
                val intent = AlarmInfoActivity.editAlarm(context, alarms[pos])
                context.startActivity(intent)
            }
        }

        item.select.visibility = if (deleteMode) View.VISIBLE else View.GONE
        item.select.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedList.add(alarms[pos].id)
            } else {
                selectedList.remove(alarms[pos].id)
            }
        }

        item.time.text = SimpleDateFormat("HH:mm").format(Date(alarms[pos].date))
        item.time.isEnabled = alarms[pos].isActive == 1
        item.repeat.text = if (alarms[pos].isRepeat == 0) {
            SimpleDateFormat(context.getString(R.string.date_format)).format(Date(alarms[pos].date))
        } else {
            getRepeatDays(alarms[pos].repeat)
        }
        item.repeat.isEnabled = alarms[pos].isActive == 1

        item.activeSwitch.isChecked = alarms[pos].isActive == 1
        item.activeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            item.time.isEnabled = isChecked
            item.repeat.isEnabled = isChecked
            alarms[pos].isActive = if (isChecked) {
                1
            } else {
                0
            }
            if (isChecked) {
                AlarmController.createAlarm(context, alarms[pos])
            } else {
                AlarmController.cancelAlarm(context, alarms[pos])
            }
            AlarmRepository().update(alarm = alarms[pos])
        }
        item.activeSwitch.visibility = if (!deleteMode) View.VISIBLE else View.GONE

        if (deleteMode) {
            if (selectAll) {
                item.select.isChecked = selectAll
            } else {
                item.select.isChecked = selectAll
            }
        }
    }

    private fun getRepeatDays(repeat: String): String {
        var days = ""
        for (i in 0 until repeat.length) {
            days += getRepeat(repeat[i].toInt() - 48)
        }
        Log.e("Days", days)
        return days
    }

    private fun getRepeat(pos: Int): String = when (pos) {
        1 -> context.getString(R.string.mon) + " "
        2 -> context.getString(R.string.tue) + " "
        3 -> context.getString(R.string.wed) + " "
        4 -> context.getString(R.string.thu) + " "
        5 -> context.getString(R.string.fri) + " "
        6 -> context.getString(R.string.sat) + " "
        0 -> context.getString(R.string.sun) + " "
        else -> ""
    }

    fun deleteMode(active: Boolean) {
        deleteMode = active
        notifyDataSetChanged()
    }

    fun setSelectAll(selectAll: Boolean) {
        this.selectAll = selectAll
        notifyDataSetChanged()
    }

    fun delete() {
        for (item in selectedList) {
            AlarmRepository().delete(item)
        }
        notifyDataSetChanged()
    }

    fun getSelectedItems(): Int = selectedList.size

    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val select = itemView.findViewById<AppCompatCheckBox>(R.id.cb_item)
        val container = itemView.findViewById<ConstraintLayout>(R.id.item_container)
        val time = itemView.findViewById<AppCompatTextView>(R.id.tv_time)
        val repeat = itemView.findViewById<AppCompatTextView>(R.id.tv_repeat)
        val activeSwitch = itemView.findViewById<SwitchCompat>(R.id.active_switch)
    }
}