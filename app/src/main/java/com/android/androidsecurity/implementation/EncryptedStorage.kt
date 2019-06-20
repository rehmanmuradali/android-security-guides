package com.android.androidsecurity.implementation

import android.util.Log
import com.android.androidsecurity.BuildConfig
import com.android.androidsecurity.utils.Constant
import com.android.androidsecurity.contracts.Point
import com.thz.keystorehelper.KeyStoreManager

class EncryptedStorage : Point {

    override fun explain() {
        val encryptedString = KeyStoreManager.encryptData("secret", BuildConfig.APPLICATION_ID);
        val decryptedString = KeyStoreManager.decryptData(encryptedString, BuildConfig.APPLICATION_ID);
        Log.e(Constant.TAG, "Decrypted String: $decryptedString")
    }
}