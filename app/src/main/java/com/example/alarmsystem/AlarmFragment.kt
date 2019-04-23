package com.example.alarmsystem

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alarmsystem.adapter.AlarmAdapter
import com.example.alarmsystem.database.AlarmRepository
import com.example.alarmsystem.interfaces.OnDelete
import com.example.alarmsystem.model.Alarm
import kotlinx.android.synthetic.main.fragment_alarm.*

class AlarmFragment: Fragment(), OnDelete{
    companion object {
        fun newInstance(): AlarmFragment{
            return AlarmFragment()
        }
    }

    private lateinit var adapter: AlarmAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_alarm,container,false)
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        (activity as MainActivity).registerListener(this)
    }

    override fun deleteMode(active: Boolean) {
        adapter.deleteMode(active)
    }

    override fun selectAll(selectAll: Boolean) {
        adapter.setSelectAll(selectAll)
    }

    override fun delete() {
        adapter.delete()
        loadAlarm()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_add.setOnClickListener {
            val intent = AlarmInfoActivity.newAlarm(context!!)
            startActivity(intent)
        }
        loadAlarm()
    }

    override fun onResume() {
        super.onResume()
        loadAlarm()
    }

    private fun loadAlarm() {
        var alarms = AlarmRepository().findAll()
        for(alarm in alarms){
            if(alarm.isRepeat == 0 && alarm.date <  System.currentTimeMillis()){
                AlarmRepository().unActiveAlarm(alarm.id)
                alarms = AlarmRepository().findAll()
            }
        }
        initRecyclerView(alarms)
    }

    private fun initRecyclerView(alarms: ArrayList<Alarm>) {
        val manager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        alarm_list.layoutManager = manager
        adapter = AlarmAdapter(context!!,alarms)
        alarm_list.adapter = adapter
    }
}