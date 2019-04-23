package com.example.alarmsystem

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.SeekBar
import com.example.alarmsystem.adapter.SoundAdapter
import com.example.alarmsystem.model.SoundItem
import kotlinx.android.synthetic.main.activity_alarm_sound.*
import kotlinx.android.synthetic.main.sub_action_bar.view.*

class AlarmSoundActivity: AppCompatActivity(){

    companion object {
        const val SOUND_NAME = "name"
        const val ALARM_SOUND = "arlam_sound"
        const val IS_ACTIVE = "active"

        fun newIntent(context: Context, name: String, isActive:Boolean): Intent {
            val intent = Intent(context, AlarmSoundActivity::class.java)
            intent.putExtra(SOUND_NAME,name)
            intent.putExtra(IS_ACTIVE,isActive)
            return intent
        }
    }

    private lateinit var sound:String
    private var isActive:Boolean = false
    private lateinit var adapter:SoundAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_sound)

        sound = intent.getStringExtra(SOUND_NAME)
        isActive = intent.getBooleanExtra(IS_ACTIVE,false)

        initView()
    }

    private fun enableView(view: View, isEnable:Boolean){
        view.isEnabled = isEnable
    }

    private fun enableAll(){
        tv_status.text = getString(R.string.on)
        enableView(action_bar.tv_add,true)
        enableView(item_list,true)
        enableView(iv_speaker,true)
        enableView(volume_bar,true)
        adapter.enableItem(true)
    }

    private fun disableAll(){
        tv_status.text = getString(R.string.off)
        enableView(action_bar.tv_add,false)
        enableView(item_list,false)
        enableView(iv_speaker,false)
        enableView(volume_bar,false)
        adapter.enableItem(false)
    }

    private fun setImage(isVolume:Boolean){
        iv_speaker.setImageDrawable(if(isVolume)getDrawable(R.drawable.iv_speaker) else getDrawable(R.drawable.iv_mute))
    }

    private fun initView() {
        initRecyclerView()

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

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        volume_bar.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
        volume_bar.progress = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)

        if(volume_bar.progress==0){
            setImage(false)
        }else{
            setImage(true)
        }

        volume_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress==0){
                    setImage(false)
                }else{
                    setImage(true)
                }
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM,progress,0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun initRecyclerView() {
        val linearLayout = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        item_list.layoutManager = linearLayout
        val sounds = getSounds()
        adapter = SoundAdapter(this, sounds)
        item_list.adapter = adapter
        enableAll()
    }

    private fun getSounds():MutableList<SoundItem>{
        val sounds = mutableListOf<SoundItem>()
        val manager = RingtoneManager(this)
        manager.setType(RingtoneManager.TYPE_ALARM)

        val cursor = manager.cursor
        val count = cursor.count
        if(count ==0 && !cursor.moveToFirst() ){
            return sounds
        }
        while (cursor.moveToNext()){
            val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX)
            Log.e("Sounds", "${cursor.position} $title $uri")
            sounds.add(SoundItem(title, Uri.parse(uri), title!!.contentEquals(sound)))
        }
        return sounds
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when(keyCode){
            KeyEvent.KEYCODE_VOLUME_UP ->{
                volume_bar.progress += 1
                true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN ->{
                volume_bar.progress -= 1
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onBackPressed() {
        adapter.stopMusic()
        val intent = Intent()
        intent.putExtra(IS_ACTIVE, isActive)
        intent.putExtra(ALARM_SOUND,if(volume_bar.progress>0)adapter.getSelectedSound()else SoundItem(getString(R.string.mute)))
        setResult(SOUND_RESULT,intent)
        finish()
    }
}