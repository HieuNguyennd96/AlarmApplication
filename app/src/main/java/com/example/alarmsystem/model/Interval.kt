package com.example.alarmsystem.model

import com.example.alarmsystem.R

class Interval(var pos: Int=0) {

    companion object {
        var interval = arrayOf(5,10,15,30)

        var modes = arrayOf(
            "${interval[0]} ${Shared.context.getString(R.string.minutes)}",
            "${interval[1]} ${Shared.context.getString(R.string.minutes)}",
            "${interval[2]} ${Shared.context.getString(R.string.minutes)}",
            "${interval[3]} ${Shared.context.getString(R.string.minutes)}"
        )

        fun getMode(pos: Int): String = modes[pos]

        fun getInterval(pos: Int): Int = interval[pos]

        fun getDefaultMode():String = modes[0]

        fun getDefaultInterval():Int = interval[0]
    }
}