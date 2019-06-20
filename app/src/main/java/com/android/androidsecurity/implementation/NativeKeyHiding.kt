package com.android.androidsecurity.implementation

import android.util.Log
import com.android.androidsecurity.utils.Constant
import com.android.androidsecurity.contracts.Point
import java.util.*

class NativeKeyHiding : Point {

    companion object {
        init {
            System.loadLibrary("keys")
        }

    }

    private external fun getArray(): Array<String>


    private external fun getString(): String

    override fun explain() {

        // Break Keys into multiple parts and assemble to make it hard to exploit
        Log.e(Constant.TAG, Arrays.toString(getArray()))
        Log.e(Constant.TAG, getString())

    }
}