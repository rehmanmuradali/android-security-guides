package com.android.androidsecurity.utils

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import crystalapps.net.mint.service.pool.ContextService
import crystalapps.net.mint.tools.service.pool.ServiceLocator

object BroadcastService {

    private var applicationContext = ServiceLocator.getService(ContextService::class.java).appContext
    private var localBroadcastManager: LocalBroadcastManager = LocalBroadcastManager.getInstance(applicationContext)

    fun registerReceiver(receiver: BroadcastReceiver, filter: IntentFilter) {
        localBroadcastManager.registerReceiver(receiver, filter)
    }

    fun unregisterReceiver(receiver: BroadcastReceiver) {
        try {
            localBroadcastManager.unregisterReceiver(receiver)
        } catch (e: Exception) {
            Log.e(Constant.TAG, e.message)
        }

    }

    fun sendBroadcast(intent: Intent) {
        localBroadcastManager.sendBroadcast(intent)
    }


}