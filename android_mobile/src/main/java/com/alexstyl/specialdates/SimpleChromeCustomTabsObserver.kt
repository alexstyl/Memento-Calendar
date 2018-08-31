package com.alexstyl.specialdates

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.alexstyl.specialdates.ui.base.MementoActivity
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs

class SimpleChromeCustomTabsObserver(private val activity: MementoActivity)
    : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun connectListener() {
        SimpleChromeCustomTabs.getInstance().connectTo(activity)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun disconnectListener() {
        SimpleChromeCustomTabs.getInstance().disconnectFrom(activity)
    }
}
