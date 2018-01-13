package com.alexstyl.specialdates.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.MenuItem
import com.alexstyl.android.Version

open class MementoActivity : AppCompatActivity() {

    /**
     * Override this method in order to let the activity handle the up button.
     * When pressed it will navigate the user to the parent of the activity
     */
    private fun shouldUseHomeAsUp(): Boolean = parentActivityIntent != null

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
            navigateUpToParent()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun navigateUpToParent() {
        val upIntent = NavUtils.getParentActivityIntent(this) ?: return
        if (NavUtils.shouldUpRecreateTask(this, upIntent) || isTaskRoot) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities()
        } else {
            NavUtils.navigateUpTo(this, upIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments = supportFragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                fragment?.onActivityResult(requestCode, resultCode, data)
            }
        }
    }


    protected fun context(): Context = this

    protected fun supportsTransitions(): Boolean = Version.hasKitKat()

    protected fun thisActivity(): Activity = this

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_MENU) {
            onKeyMenuPressed()
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    open fun onKeyMenuPressed(): Boolean = false

}
