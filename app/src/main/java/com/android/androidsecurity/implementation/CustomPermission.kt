package com.android.androidsecurity.implementation

import com.android.androidsecurity.contracts.Point

class CustomPermission: Point {

    override fun explain() {

        // See Android Manifest <permission> tag for simple creation
        // add this permission to all Android Components
        // Verify that activity is opening or not through ADB( it will require permission)
    }
}