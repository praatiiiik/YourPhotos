package com.yourphotos.utils

import android.content.Context
import android.os.Build
import android.widget.Toast

inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}
