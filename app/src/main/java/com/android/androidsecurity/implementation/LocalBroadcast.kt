package com.android.androidsecurity.implementation

import android.content.BroadcastReceiver
import android.content.Intent
import com.android.androidsecurity.contracts.Point
import com.android.androidsecurity.utils.BroadcastAction
import com.android.androidsecurity.utils.BroadcastService

class LocalBroadcast: Point {

    override fun explain() {
        BroadcastService.sendBroadcast(Intent(BroadcastAction.ACTION_DEMO_BROADCAST))
    }
}