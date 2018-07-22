package com.alexstyl.specialdates.debug.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexstyl.specialdates.debug.DebugOption
import com.alexstyl.specialdates.debug.MementoDebugFragment

class DebugFirebaseFragment : MementoDebugFragment() {

    override val debugOption: DebugOption
        get() = DebugOption.FIREBASE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}
