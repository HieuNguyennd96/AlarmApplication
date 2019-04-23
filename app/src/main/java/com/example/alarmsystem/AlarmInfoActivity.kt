package com.example.alarmsystem

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.NumberPicker
import com.example.alarmsystem.database.AlarmRepository
import com.example.alarmsystem.model.*
import com.example.alarmsystem.services.AlarmController
import com.example.alarmsystem.utility.Utils
import kotlinx.android.synthetic.main.activity_alarm_info.*
import kotlinx.android.synthetic.main.dialog_alarm_name.view.*
import kotlinx.android.synthetic.main.fragment_date_picker.view.*
import org.jetbrains.anko.startActivityForResult
import java.util.*

class AlarmInfoActivity : AppCompatActivity() {

    private var isCalendarShow = false
    private var isDialogShow = false
    private var startHour: Int = 0
    private var startMin: Int = 0
    private val repeatDays = arrayOf(false, false, false, false, false, false, false)
    private lateinit var alarm: Alarm
    private var isNew: Boolean = false
    private lateinit var sound: SoundItem
    private var vibration: VibrationItem = Vibration.getDefaultVibration()
    private var snooze = SnoozeItem(0, 0)
    private var selectedTimeStamp: Long = System.currentTimeMillis()

    companion object {
        private const val ALARM = "alarm"
        private const val IS_NEW = "is_new"

        fun editAlarm(context: Context, alarm: Alarm): Intent {
            val intent = Intent(context, AlarmInfoActivity::class.java)
            intent.putExtra(ALARM, alarm)
            intent.putExtra(IS_NEW, false)
            return intent
        }

        fun newAlarm(context: Context): Intent {
            val intent = Intent(context, AlarmInfoActivity::class.java)
            val alarm = Alarm(
                alarmName = NONE,
                soundName = Utils.getDefaultRingtoneTitle(),
                soundUri = Utils.getDefaultRingtoneUri().toString(),
                isActive = 0,
                isRepeat = 0,
                isSound = 1,
                isVibration = 1,
                isSnooze = 1,
                snoozeInterval = 0,
                snoozeRepeat = 0,
                date = getCurrentTime(),
                repeat = "",
                vibration = Vibration.getDefaultVibration().name
            )
            intent.putExtra(ALARM, alarm)
            intent.putExtra(IS_NEW, true)
            return intent
        }

        private fun getCurrentTime(): Long = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_info)
        alarm = intent.getParcelableExtra(ALARM)
        sound = SoundItem(alarm.soundName, Uri.parse(alarm.soundUri))
        isNew = intent.getBooleanExtra(IS_NEW, true)
        initView()
        registerListener()
    }

    private fun getTime() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = alarm.date
        startHour = calendar.get(Calendar.HOUR_OF_DAY)
        startMin = calendar.get(Calendar.MINUTE)
    }

    private fun initView() {

        // base view
        hour_picker.minValue = 0
        hour_picker.maxValue = 23

        hour_picker.setFormatter(object : NumberPicker.Formatter {
            override fun format(value: Int): String {
                return String.format("%02d", value)
            }
        })

        minute_picker.minValue = 0
        minute_picker.maxValue = 59

        minute_picker.setFormatter(object : NumberPicker.Formatter {
            override fun format(value: Int): String {
                return String.format("%02d", value)
            }
        })

        //value

        sound_switch.isChecked = alarm.isSound == 1
        snooze_switch.isChecked = alarm.isSnooze == 1
        vibration_switch.isChecked = alarm.isVibration == 1

        tv_alarm_name.text = alarm.alarmName
        setSnoozeSwitch(snooze_switch.isChecked)
        setSoundSwitch(sound_switch.isChecked)
        setVibrationSwitch(vibration_switch.isChecked)

        getTime()
        hour_picker.value = startHour
        minute_picker.value = startMin

        if (!isNew) {
            if (alarm.isRepeat == 0) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = alarm.date
                tv_date.text = DateFormat.format(getString(R.string.date_format), calendar).toString()
            } else {
                for (i in 0 until alarm.repeat.length) {
                    repeatDays[alarm.repeat[i].toInt() - 48] = true
                }
                checkedRepeat()
            }
        }
    }

    private fun registerListener() {
        container.setOnClickListener {
            if (isCalendarShow) {
                slideDown()
            }
            if (isDialogShow) {
                hideDialog()
            }
        }

        ic_date.setOnClickListener {
            if (!isCalendarShow) {
                val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
                calendar_container.visibility = View.VISIBLE
                calendar_container.startAnimation(slideUp)
                isCalendarShow = true
                calendar_container.calendar_view.minDate = System.currentTimeMillis()
                calendar_container.calendar_view.date = System.currentTimeMillis()
                calendar_container.calendar_view.setOnDateChangeListener { view, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)
                    selectedTimeStamp = calendar.timeInMillis
                }
            }
        }

        calendar_container.btn_picker_cancel.setOnClickListener {
            slideDown()
        }

        calendar_container.btn_picker_ok.setOnClickListener {
            setDate()
            slideDown()
        }

        //repeat picker

        tv_monday.setOnClickListener {
            repeatDays[1] = !repeatDays[1]
            checkedRepeat()
        }

        tv_tuesday.setOnClickListener {
            repeatDays[2] = !repeatDays[2]
            checkedRepeat()
        }

        tv_wednesday.setOnClickListener {
            repeatDays[3] = !repeatDays[3]
            checkedRepeat()
        }

        tv_thursday.setOnClickListener {
            repeatDays[4] = !repeatDays[4]
            checkedRepeat()
        }

        tv_friday.setOnClickListener {
            repeatDays[5] = !repeatDays[5]
            checkedRepeat()
        }

        tv_saturday.setOnClickListener {
            repeatDays[6] = !repeatDays[6]
            checkedRepeat()
        }

        tv_sunday.setOnClickListener {
            repeatDays[0] = !repeatDays[0]
            checkedRepeat()
        }

        //dialog
        name_container.setOnClickListener {
            if (!isDialogShow) {
                dialog_name.visibility = View.VISIBLE
                isDialogShow = true
                dialog_name.edt_name.requestFocus()
                Utils.showKeyBoard(Shared.context)

                dialog_name.btn_dialog_cancel.setOnClickListener {
                    hideDialog()
                }

                dialog_name.btn_dialog_ok.setOnClickListener {
                    if (dialog_name.edt_name.text.isNullOrEmpty()) {
                        tv_alarm_name.text = NONE
                    } else {
                        tv_alarm_name.text = dialog_name.edt_name.text
                    }
                    hideDialog()
                }
            }
        }

        //switch
        sound_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            setSoundSwitch(isChecked)
        }

        sound_switch.isChecked = alarm.isSound == 1

        vibration_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            setVibrationSwitch(isChecked)
        }

        vibration_switch.isChecked = alarm.isVibration == 1

        snooze_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            setSnoozeSwitch(isChecked)
        }

        snooze_switch.isChecked = alarm.isSnooze == 1

        //container
        sound_container.setOnClickListener {
            val intent = AlarmSoundActivity.newIntent(
                Shared.context,
                alarm.soundName,
                sound_switch.isChecked
            )
            startActivityForResult(intent, REQUEST_CODE)
        }

        vibration_container.setOnClickListener {
            val intent = AlarmVibrationActivity.newIntent(
                Shared.context,
                vibration.name,
                vibration_switch.isChecked
            )
            startActivityForResult(intent, REQUEST_CODE)
        }

        snooze_container.setOnClickListener {
            val intent = AlarmSnoozeActivity.newIntent(
                Shared.context,
                alarm.snoozeInterval,
                alarm.snoozeRepeat,
                snooze_switch.isChecked
            )
            startActivityForResult(intent, REQUEST_CODE)
        }

        //btn
        btn_save.setOnClickListener {
            val time = selectedTimeStamp + (hour_picker.value * 3600 + minute_picker.value * 60) * 1000
            alarm.alarmName = tv_alarm_name.text.toString()
            alarm.soundName = sound.soundName
            alarm.soundUri = sound.soundUri.toString()
            alarm.date = if (time > System.currentTimeMillis()) time else {
                time + 24 * 60 * 60 * 1000
            }
            Log.e("Timestamp: ","$time ${alarm.date}")
            alarm.isActive = 1
            alarm.vibration = vibration.name
            if (isNew) {
                AlarmRepository().add(alarm)
            } else {
                AlarmRepository().update(alarm)
            }
            AlarmController.createAlarm(this, alarm)
            onBackPressed()
        }

        btn_cancel.setOnClickListener {
            onBackPressed()
        }
        setTimeStamp()
    }


    private fun setDate() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedTimeStamp
        tv_date.text = DateFormat.format(getString(R.string.date_format), calendar).toString()
        alarm.isRepeat = 0
        unCheckedRepeat()
    }

    private fun unCheckedRepeat() {
        for (i in 0 until repeatDays.size) {
            val textView = getTextView(i)
            if (i == 0) {
                textView!!.setTextColor(getColor(R.color.colorAccent))
            } else {
                textView!!.setTextColor(getColor(R.color.colorGray))
            }
            textView.background = null
        }
    }


    private fun setTimeStamp() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedTimeStamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        selectedTimeStamp = calendar.timeInMillis
    }

    private fun hideDialog() {
        Utils.hideKeyBoard(Shared.context, dialog_name.edt_name)
        dialog_name.visibility = View.GONE
        dialog_name.edt_name.text?.clear()
        isDialogShow = false
    }

    private fun checkedRepeat() {
        alarm.isRepeat = 1
        for (i in 0 until repeatDays.size) {
            val textView = getTextView(i)
            if (repeatDays[i]) {
                textView!!.setTextColor(getColor(R.color.colorTealBlue))
                textView.background = getDrawable(R.drawable.ic_circular)
            } else {
                if (i == 0) {
                    textView!!.setTextColor(getColor(R.color.colorAccent))
                } else {
                    textView!!.setTextColor(getColor(R.color.colorGray))
                }
                textView.background = null
            }
        }
        setRepeat()
    }

    private fun getTextView(pos: Int): AppCompatTextView? = when (pos) {
        1 -> tv_monday
        2 -> tv_tuesday
        3 -> tv_wednesday
        4 -> tv_thursday
        5 -> tv_friday
        6 -> tv_saturday
        0 -> tv_sunday
        else -> null
    }

    private fun setRepeat() {
        alarm.isRepeat = 1
        var repeat = ""
        var isEveryDay = true
        alarm.repeat = ""
        for (i in 0 until repeatDays.size) {
            if (repeatDays[i]) {
                repeat += "${getDay(i)},"
                alarm.repeat += "$i"
            } else {
                isEveryDay = false
            }
        }
        if (isEveryDay) {
            tv_date.text = getString(R.string.every_day)
        } else {
            tv_date.text = if (repeat.isNotEmpty()) repeat.substring(0, repeat.length - 1) else ""
        }
    }

    private fun getDay(pos: Int): String = when (pos) {
        1 -> getString(R.string.monday)
        2 -> getString(R.string.tuesday)
        3 -> getString(R.string.wednesday)
        4 -> getString(R.string.thursday)
        5 -> getString(R.string.friday)
        6 -> getString(R.string.saturday)
        0 -> getString(R.string.sunday)
        else -> ""
    }

    private fun slideDown() {
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        calendar_container.startAnimation(slideDown)
        calendar_container.visibility = View.GONE
        isCalendarShow = false
    }

    override fun onBackPressed() {
        when {
            isDialogShow -> {
                hideDialog()
                return
            }
            isCalendarShow -> {
                slideDown()
                return
            }
            else -> super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == SOUND_RESULT) {
                if (data != null) {
                    sound_switch.isChecked = data.getBooleanExtra(AlarmSoundActivity.IS_ACTIVE, false)
                    sound = data.getParcelableExtra(AlarmSoundActivity.ALARM_SOUND)
                    setSound()
                }
            } else if (resultCode == VIBRATION_RESULT) {
                if (data != null) {
                    vibration_switch.isChecked = data.getBooleanExtra(AlarmVibrationActivity.IS_ACTIVE, false)
                    vibration = data.getParcelableExtra(AlarmVibrationActivity.VIBRATION)
                    setVibration()
                }
            } else if (resultCode == SNOOZE_RESULT) {
                if (data != null) {
                    snooze_switch.isChecked = data.getBooleanExtra(AlarmSnoozeActivity.IS_ACTIVE, false)
                    snooze = data.getParcelableExtra(AlarmSnoozeActivity.SNOOZE)
                    setSnooze()
                }
            }
        }
    }

    private fun setSnooze() {
        alarm.snoozeInterval = snooze.interval
        alarm.snoozeRepeat = snooze.repeat
        setSnoozeSwitch(snooze_switch.isChecked)
    }

    private fun setVibration() {
        alarm.vibration = vibration.name
        setVibrationSwitch(vibration_switch.isChecked)
    }

    private fun setSoundSwitch(checked: Boolean) {
        if (checked) {
            if (sound.soundName.contains("(")) sound.soundName =
                sound.soundName.substring(sound.soundName.indexOf("(") + 1, sound.soundName.length - 1)
            tv_sound_name.text = sound.soundName
        } else {
            tv_sound_name.text = getString(R.string.off)
        }
        alarm.isSound = if (checked) 1 else 0
    }

    private fun setVibrationSwitch(checked: Boolean) {
        if (checked) {
            tv_vibration_name.text = vibration.name
        } else {
            tv_vibration_name.text = getString(R.string.off)
        }
        alarm.isVibration = if (checked) 1 else 0
    }

    private fun setSnoozeSwitch(checked: Boolean) {
        if (checked) {
            tv_snooze_name.text = "${Interval.getMode(alarm.snoozeInterval)}, ${Repeat.getMode(alarm.snoozeRepeat)}"
        } else {
            tv_snooze_name.text = getString(R.string.off)
        }
        alarm.isSnooze = if (checked) 1 else 0
    }

    private fun setSound() {
        alarm.soundName = sound.soundName
        alarm.soundUri = sound.soundUri.toString()
        setSoundSwitch(sound_switch.isChecked)
    }
}