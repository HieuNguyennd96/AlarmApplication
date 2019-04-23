package com.example.alarmsystem.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.alarmsystem.AlarmFragment
import com.example.alarmsystem.R
import com.example.alarmsystem.model.Shared

class PageAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager){


    private val fragments = arrayOfNulls<Fragment>(1)

    override fun getItem(pos: Int): Fragment? {
        when(pos){
            0 -> {
                if(fragments[0]==null){
                    fragments[0] = AlarmFragment.newInstance()
                }
                return fragments[0]
            }
        }
        return null
    }
    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): String = Shared.context.getString(R.string.alarm_title)
}