package com.example.alarmsystem.model

import com.example.alarmsystem.R

class Repeat(var pos:Int) {

    companion object {
        var times = arrayOf(3,5,-1)

        var modes = arrayOf(
            "${times[0]} ${Shared.context.getString(R.string.times)}",
            "${times[1]} ${Shared.context.getString(R.string.times)}",
            Shared.context.getString(R.string.continuously)
        )

        fun getMode(pos:Int):String = modes[pos]

        fun getTimes(pos: Int): Int = times[pos]
    }
}