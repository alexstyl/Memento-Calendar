package com.alexstyl.specialdates.ui.base

import android.content.Intent

open class MementoPreferenceActivity : ThemedMementoActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments = supportFragmentManager.fragments
        if (fragments != null) {
            for (frag in fragments) {
                frag.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

}
