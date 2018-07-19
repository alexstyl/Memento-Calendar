package com.alexstyl.specialdates.debug

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.alexstyl.specialdates.BuildConfig

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.donate.DonateActivity
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity

import java.sql.DriverManager.println

class DebugActivity : ThemedMementoActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug);
    }


    companion object {
        const val ACTION_DEBUG_OPTIONS =  "com.alexstyl.specialdates.DEBUG_OPTIONS"
    }
}
