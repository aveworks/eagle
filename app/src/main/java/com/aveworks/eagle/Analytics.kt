package com.aveworks.eagle

import android.util.Log

interface Analytics{
    fun trackScreenView();
    fun trackException(it: Throwable)
}

class AnalyticsImpl : Analytics{
    override fun trackScreenView(){
        Log.v(javaClass.name, "Sending track data")
    }

    override fun trackException(it: Throwable) {
        Log.v(javaClass.name, "Sending exception data")
    }
}