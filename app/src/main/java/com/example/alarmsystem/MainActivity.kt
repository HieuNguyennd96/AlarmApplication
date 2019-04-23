package com.example.alarmsystem

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.example.alarmsystem.adapter.PageAdapter
import com.example.alarmsystem.database.DataBaseOpenHelper
import com.example.alarmsystem.interfaces.OnDelete
import com.example.alarmsystem.model.Shared
import kotlinx.android.synthetic.main.main_action_bar.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_alarm.*

class MainActivity : AppCompatActivity() {
    private var isDelete = false
    private lateinit var onDeleteModeListener: OnDelete
    private val WAKELOCK_REQUEST_CODE = 1
    private val FOREGROUND_REQUEST_CODE = 2
    private val BOOT_REQUEST_CODE = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Shared.context = applicationContext
        Shared.database = DataBaseOpenHelper.getInstance(applicationContext)
        initView()
        registerListener()
        checkPermission()
    }

    private fun registerListener() {
        action_bar.iv_menu.setOnClickListener {
            showPopupMenu()
        }
    }

    private fun initView() {
        val tabContainer: TabLayout = findViewById(R.id.tab_container)
        tabContainer.addTab(tabContainer.newTab().setText(getText(R.string.alarm_title)))
        tabContainer.setTabTextColors(getColor(R.color.colorGray), getColor(R.color.colorTealBlue))
        val pageAdapter = PageAdapter(supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.vp_main)
        viewPager.adapter = pageAdapter
        tabContainer.setupWithViewPager(vp_main)
        tabContainer.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(pos: TabLayout.Tab?) {}

            override fun onTabUnselected(pos: TabLayout.Tab?) {}

            override fun onTabSelected(pos: TabLayout.Tab?) {

            }
        })
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(this@MainActivity, action_bar.iv_menu, Gravity.TOP)
        popupMenu.menuInflater.inflate(R.menu.alarm_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_delete -> {
                    action_bar.selector_container.visibility = View.VISIBLE
                    action_bar.iv_menu.visibility = View.GONE
                    action_bar.tv_delete.visibility = View.VISIBLE
                    action_bar.tv_title.text = getText(R.string.select_alarms)
                    isDelete = true
                    btn_add.hide()
                    setDeleteMode(isDelete)
                    action_bar.cb_item.setOnCheckedChangeListener { buttonView, isChecked ->
                        setSelectAll(isChecked)
                    }
                    action_bar.tv_delete.setOnClickListener {
                        delete()
                        disableDeleteMode()
                    }
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    @Synchronized
    fun setDeleteMode(active: Boolean) {
        onDeleteModeListener.deleteMode(active)
    }

    @Synchronized
    fun setSelectAll(selectAll: Boolean) {
        onDeleteModeListener.selectAll(selectAll)
    }

    @Synchronized
    fun delete() {
        onDeleteModeListener.delete()
    }

    override fun onBackPressed() {
        if (isDelete) {
            disableDeleteMode()
        } else super.onBackPressed()
    }

    @Synchronized
    fun registerListener(onDelete: OnDelete) {
        onDeleteModeListener = onDelete
    }

    private fun disableDeleteMode() {
        if (isDelete) {
            action_bar.selector_container.visibility = View.GONE
            action_bar.iv_menu.visibility = View.VISIBLE
            action_bar.tv_title.text = getText(R.string.app_name)
            action_bar.tv_delete.visibility = View.GONE
            btn_add.show()
            isDelete = false
            setDeleteMode(isDelete)
        }
    }

    private fun checkPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.RECEIVE_BOOT_COMPLETED
            ),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            BOOT_REQUEST_CODE,
            FOREGROUND_REQUEST_CODE,
            WAKELOCK_REQUEST_CODE,
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish()
                }
            }
        }
    }

}
