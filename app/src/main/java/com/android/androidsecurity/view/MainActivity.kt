package com.android.androidsecurity.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.android.androidsecurity.R
import com.android.androidsecurity.implementation.EncryptedStorage
import com.android.androidsecurity.implementation.LocalBroadcast
import com.android.androidsecurity.utils.BroadcastAction
import com.android.androidsecurity.utils.BroadcastService
import com.android.androidsecurity.utils.Constant
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            Log.e(Constant.TAG, getString(R.string.broadcast_receiver))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupViews()
        registerBroadcast()
        LocalBroadcast().explain()
    }

    private fun setupViews() {
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    private fun registerBroadcast() {
        val intentFilter = IntentFilter(BroadcastAction.ACTION_DEMO_BROADCAST)
        BroadcastService.registerReceiver(receiver , intentFilter)
    }


    override fun onPause() {
        super.onPause()
        BroadcastService.unregisterReceiver(receiver)
    }


}
