package com.android.androidsecurity.factory

import com.android.androidsecurity.contracts.Point
import com.android.androidsecurity.implementation.*

object PointFactory {

    val SSL_PINNING = 1
    val NATIVE_KEY_HIDING = 2
    val ROOT_ACCESS = 3
    val OBFUSCATION = 4
    val ENCRYPTED_STORAGE = 5
    val LOCAL_BROADCAST = 6
    val CUSTOM_PERMISSION = 7

    fun getInstance(id: Int): Point {

        return when (id) {

            SSL_PINNING -> SslPinning()
            NATIVE_KEY_HIDING -> NativeKeyHiding()
            ROOT_ACCESS -> RootAccess()
            OBFUSCATION -> Obfuscation()
            ENCRYPTED_STORAGE -> EncryptedStorage()
            LOCAL_BROADCAST -> LocalBroadcast()
            else -> CustomPermission()
        }
    }
}