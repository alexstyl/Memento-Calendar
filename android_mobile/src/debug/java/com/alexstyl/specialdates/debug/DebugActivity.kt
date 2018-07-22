package com.alexstyl.specialdates.debug

import android.os.Bundle
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.ui.widget.MementoToolbar

class DebugActivity : ThemedMementoActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug);

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, DebugOptionsFragment()).commit();
        }

        val toolbar = findViewById<MementoToolbar>(R.id.mementoToolbar)
        setSupportActionBar(toolbar)
    }
}
