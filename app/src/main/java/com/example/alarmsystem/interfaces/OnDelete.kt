package com.example.alarmsystem.interfaces

interface OnDelete {
    fun deleteMode(active:Boolean)

    fun selectAll(selectAll:Boolean)

    fun delete()
}