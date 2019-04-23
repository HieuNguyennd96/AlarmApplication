package com.example.alarmsystem

import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.alarmsystem.adapter.VibrationAdapter
import com.example.alarmsystem.model.Shared
import com.example.alarmsystem.model.Vibration
import com.example.alarmsystem.model.VibrationItem
import kotlinx.android.synthetic.main.activity_alarm_sound.*
import kotlinx.android.synthetic.main.sub_action_bar.view.*

class AlarmVibrationActivity: AppCompatActivity(){

    companion object {
        const val VIBRATION_NAME = "name"
        const val VIBRATION = "vibration"
        const val IS_ACTIVE = "active"

        fun newIntent(context: Context, name: String, isActive:Boolean): Intent {
            val intent = Intent(context, AlarmVibrationActivity::class.java)
            intent.putExtra(VIBRATION_NAME,name)
            intent.putExtra(IS_ACTIVE,isActive)
            return intent
        }
    }

    private lateinit var vibration:String
    private var isActive:Boolean = false
    private lateinit var adapter:VibrationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_sound)

        vibration = intent.getStringExtra(VIBRATION_NAME)
        isActive = intent.getBooleanExtra(IS_ACTIVE,false)

        initView()
    }

    private fun enableView(view: View, isEnable:Boolean){
        view.isEnabled = isEnable
    }

    private fun enableAll(){
        tv_status.text = getString(R.string.on)
        enableView(item_list,true)
        adapter.enableItem(true)
    }

    private fun disableAll(){
        tv_status.text = getString(R.string.off)
        enableView(item_list,false)
        adapter.enableItem(false)
    }

    private fun setImage(isVolume:Boolean){
        iv_speaker.setImageDrawable(if(isVolume)getDrawable(R.drawable.iv_speaker) else getDrawable(R.drawable.iv_mute))
    }

    private fun initView() {
        initRecyclerView()
        action_bar.tv_title.text = getString(R.string.vibration)
        action_bar.tv_add.visibility = View.GONE
        action_bar.iv_back.setOnClickListener {
            onBackPressed()
        }
        volume_container.visibility = View.GONE
        active_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            isActive = if(isChecked){
                enableAll()
                true
            }else{
                disableAll()
                false
            }
        }

        if(!isActive){
            active_switch.isChecked = false
            disableAll()
        }else{
            enableAll()
            active_switch.isChecked = true
        }
    }

    private fun initRecyclerView() {
        val linearLayout = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        item_list.layoutManager = linearLayout
        val vibrations = getVibrations()
        adapter = VibrationAdapter(Shared.context,vibrations)
        item_list.adapter = adapter
        adapter.enableItem(true)
    }

    private fun getVibrations(): MutableList<VibrationItem> {
        val vibrations = Vibration.getListOfVibration()
        for(i in vibrations){
            if(i.name == vibration){
                i.isSelected = true
                break
            }
        }
        return vibrations
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(IS_ACTIVE, isActive)
        intent.putExtra(VIBRATION,adapter.getSelectedVibration())
        setResult(VIBRATION_RESULT,intent)
        finish()
    }
}