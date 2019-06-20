package com.android.androidsecurity.implementation

import android.util.Log
import com.android.androidsecurity.utils.Constant
import com.android.androidsecurity.R
import com.android.androidsecurity.contracts.Point
import com.scottyab.rootbeer.RootBeer
import crystalapps.net.mint.service.pool.ContextService
import crystalapps.net.mint.tools.service.pool.ServiceLocator


class RootAccess : Point {
    override fun explain() {
        val applicationContext = ServiceLocator.getService(ContextService::class.java).appContext
        val rootBeer = RootBeer(applicationContext)
        if (rootBeer.isRooted) {
            Log.e(Constant.TAG, applicationContext.getString(R.string.root_success))
        } else {
            Log.e(Constant.TAG, applicationContext.getString(R.string.root_failure))
        }

    }
}