package com.alexstyl.android

import android.content.Context
import android.net.ConnectivityManager


fun Context.isOnline(): Boolean {
    val service = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (service.activeNetworkInfo != null) {
        return service.activeNetworkInfo.isConnected
    }
    return false
}
