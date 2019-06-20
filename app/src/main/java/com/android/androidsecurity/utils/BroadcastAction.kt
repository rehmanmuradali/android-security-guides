package com.android.androidsecurity.utils

import com.android.androidsecurity.BuildConfig

class BroadcastAction {

    companion object {

        val APP_ID = BuildConfig.APPLICATION_ID
        val ACTION_DEMO_BROADCAST = "$APP_ID.demo_broadcast"

    }
}