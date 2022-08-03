package com.techfii.bustracking

import android.app.Application

class BusTrackingApplication : Application() {

    companion object {

        lateinit var instance: BusTrackingApplication
            private set

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}