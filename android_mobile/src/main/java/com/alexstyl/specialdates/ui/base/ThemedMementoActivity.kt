package com.alexstyl.specialdates.ui.base

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.MenuRes
import android.support.transition.TransitionManager
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.Menu
import com.alexstyl.specialdates.home.HomeActivity
import com.alexstyl.specialdates.theming.AttributeExtractor
import com.alexstyl.specialdates.theming.MementoTheme
import com.alexstyl.specialdates.theming.Themer

open class ThemedMementoActivity : MementoActivity() {

    private var themer: Themer? = null
    private var attributeExtractor: AttributeExtractor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        themer = Themer.get(this)
        attributeExtractor = AttributeExtractor()
        themer!!.initialiseActivity(this)
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if(supportActionBar!=null){

        }
    }

    fun setContentView(@LayoutRes layoutResID: Int, theme: MementoTheme) {
        val wrapper = ContextThemeWrapper(this, theme.androidTheme())
        val inflate = LayoutInflater.from(wrapper).inflate(layoutResID, null, false)
        setContentView(inflate)
    }

    fun applyNewTheme() {
        val intent = Intent(ACTION_UPDATE_THEME)
        intent.setClass(this, HomeActivity::class.java)
        finish()
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        for (i in 0 until menu?.size()!!) {
            val item = menu.getItem(i)
            if (item.icon == null) {
                continue
            }
            val wrappedDrawable = DrawableCompat.wrap(item.icon)
            val color = attributeExtractor!!.extractToolbarIconColors(this)
            DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color))
        }
        return super.onPrepareOptionsMenu(menu)
    }

    fun getTintedDrawable(@DrawableRes drawableResId: Int): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(resources.getDrawable(drawableResId))
        val color = attributeExtractor!!.extractToolbarIconColors(this)
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color))
        return wrappedDrawable
    }

    companion object {
        /**
         * Intent action that the theme of the app was just updated
         */
        const val ACTION_UPDATE_THEME = "ACTION_UPDATE_THEME"
    }

}
