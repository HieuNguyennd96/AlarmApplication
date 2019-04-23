package com.example.alarmsystem.model

class Vibration{
    companion object {
        enum class Type{BasicCall,HeartBeat,Ticktock,Waltz,Zigzigzig,Off}

        fun getPattern(name: String):LongArray = when(name){
            "Basic call" -> longArrayOf(0,500,0,500,0,500)
            "HeartBeat" -> longArrayOf(0,100,250,125,900,100,250,125)
            "Ticktock" -> longArrayOf(0,300,200,200)
            "Waltz" -> longArrayOf(0,500,500,200,500,100)
            "Zig-zig-zig" -> longArrayOf(0,500,300,500,300,500)
            else -> longArrayOf()
        }

        fun getName(type:Type):String{
            return when(type){
                Companion.Type.BasicCall -> "Basic call"
                Companion.Type.HeartBeat -> "HeartBeat"
                Companion.Type.Ticktock -> "Ticktock"
                Companion.Type.Waltz -> "Waltz"
                Companion.Type.Zigzigzig -> "Zig-zig-zig"
                Companion.Type.Off -> "Off"
            }
        }

        fun getType(name:String):Type{
            return when(name){
                "Basic call" -> Companion.Type.BasicCall
                "HeartBeat" -> Companion.Type.HeartBeat
                "Ticktock" -> Companion.Type.Ticktock
                "Waltz" -> Companion.Type.Waltz
                "Zig-zig-zig" -> Companion.Type.Zigzigzig
                else -> Companion.Type.Off
            }
        }

        fun getListOfVibration():MutableList<VibrationItem> = mutableListOf(
            VibrationItem(getName(Companion.Type.BasicCall),false),
            VibrationItem(getName(Companion.Type.HeartBeat),false),
            VibrationItem(getName(Companion.Type.Ticktock),false),
            VibrationItem(getName(Companion.Type.Waltz),false),
            VibrationItem(getName(Companion.Type.Zigzigzig),false)
        )

        fun getDefaultVibration():VibrationItem = VibrationItem(getName(Companion.Type.BasicCall),true)
    }
}