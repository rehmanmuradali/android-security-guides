package com.android.androidsecurity

import android.app.Application
import com.thz.keystorehelper.KeyStoreManager
import crystalapps.net.mint.tools.service.pool.ServiceLocator

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.loadContextService(this)
        KeyStoreManager.init(this);
    }
}