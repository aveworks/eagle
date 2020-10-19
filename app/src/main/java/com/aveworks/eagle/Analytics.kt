package com.aveworks.eagle

import android.util.Log

/**
 * A simple Analytics service. Can track screen view and exceptions
 * Currently is just a stub.
 */

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