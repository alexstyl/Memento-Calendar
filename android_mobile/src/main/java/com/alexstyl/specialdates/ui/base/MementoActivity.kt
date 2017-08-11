package com.alexstyl.specialdates.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.MenuItem
import com.alexstyl.android.Version

open class MementoActivity : AppCompatActivity() {

    /**
     * Override this method in order to let the activity handle the up button.
     * When pressed it will navigate the user to the parent of the activity
     */
    protected open fun shouldUseHomeAsUp(): Boolean {
        return false
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (shouldUseHomeAsUp()) {
            val bar = supportActionBar
            if (bar != null) {
                bar.setHomeButtonEnabled(true)
                bar.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            return handleUp()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments = supportFragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                fragment?.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun handleUp(): Boolean {
        if (!shouldUseHomeAsUp()) {
            return false
        }
        NavUtils.navigateUpFromSameTask(this)
        return true
    }

    protected fun context(): Context {
        return this
    }

    protected fun supportsTransitions(): Boolean {
        return Version.hasKitKat()
    }

    protected fun thisActivity(): Activity {
        return this
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return onKeyMenuPressed()
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }

    open fun onKeyMenuPressed(): Boolean {
        return false
    }

}
