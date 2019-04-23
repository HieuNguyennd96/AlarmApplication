package com.example.alarmsystem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.alarmsystem.adapter.IntervalAdapter
import com.example.alarmsystem.adapter.RepeatAdapter
import com.example.alarmsystem.model.Interval
import com.example.alarmsystem.model.Repeat
import com.example.alarmsystem.model.SnoozeItem
import kotlinx.android.synthetic.main.activity_snooze.*
import kotlinx.android.synthetic.main.sub_action_bar.view.*

class AlarmSnoozeActivity : AppCompatActivity() {

    companion object {
        const val SNOOZE = "snooze"
        const val IS_ACTIVE = "is_active"
        const val INTERVAL = "interval"
        const val REPEAT = "repeat"

        fun newIntent(context: Context, interval: Int = 0, repeat: Int = 0, isActive: Boolean): Intent {
            val intent = Intent(context, AlarmSnoozeActivity::class.java)
            intent.putExtra(INTERVAL, interval)
            intent.putExtra(REPEAT, repeat)
            intent.putExtra(IS_ACTIVE, isActive)
            return intent
        }
    }

    private var interval: Int = 0
    private var repeat: Int = 0
    private var isActive = true
    private lateinit var repeatAdapter: RepeatAdapter
    private lateinit var intervalAdapter: IntervalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snooze)

        interval = intent.getIntExtra(INTERVAL, 0)
        repeat = intent.getIntExtra(REPEAT, 0)
        isActive = intent.getBooleanExtra(IS_ACTIVE, true)

        initView()
    }

    private fun initView() {
        initIntervals()
        initRepeat()

        action_bar.tv_title.text = getString(R.string.snooze)
        action_bar.tv_add.visibility = View.GONE
        action_bar.iv_back.setOnClickListener {
            onBackPressed()
        }

        active_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            isActive = if (isChecked) {
                enableAll()
                true
            } else {
                disableAll()
                false
            }
        }

        active_switch.isChecked = isActive
        if (isActive) {
            enableAll()
        } else {
            disableAll()
        }
    }

    private fun initRepeat() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        repeat_list.layoutManager = manager
        repeatAdapter = RepeatAdapter(this, Repeat(repeat))
        repeat_list.adapter = repeatAdapter
    }

    private fun initIntervals() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        interval_list.layoutManager = manager
        intervalAdapter = IntervalAdapter(this, Interval(interval))
        interval_list.adapter = intervalAdapter
    }

    private fun enableView(view: View, isEnable: Boolean) {
        view.isEnabled = isEnable
    }

    private fun enableAll() {
        tv_status.text = getString(R.string.on)
        enableView(interval_list, true)
        enableView(repeat_list, true)
        intervalAdapter.enableItem(true)
        repeatAdapter.enableItem(true)
    }

    private fun disableAll() {
        tv_status.text = getString(R.string.off)
        enableView(interval_list, false)
        enableView(repeat_list, false)
        intervalAdapter.enableItem(false)
        repeatAdapter.enableItem(false)
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(IS_ACTIVE, isActive)
        intent.putExtra(SNOOZE,SnoozeItem(intervalAdapter.getSelectedInterval(),repeatAdapter.getSelectedRepeat()))
        setResult(SNOOZE_RESULT,intent)
        finish()
    }
}