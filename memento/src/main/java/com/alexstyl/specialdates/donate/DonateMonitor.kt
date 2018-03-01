package com.alexstyl.specialdates.donate


class DonateMonitor {

    fun onDonationUpdated() {
        for (listener in listeners) {
            listener.onUserDonated()
        }
    }

    fun addListener(l: DonateMonitorListener) {
        listeners.add(l)
    }

    fun removeListener(l: DonateMonitorListener) {
        listeners.remove(l)
    }

    interface DonateMonitorListener {
        fun onUserDonated()
    }

    companion object {
        private val listeners = ArrayList<DonateMonitorListener>()
    }
}
