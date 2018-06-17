package com.alexstyl.specialdates.upcoming.widget.today

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.WallpaperManager
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.TooltipCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView

import com.alexstyl.specialdates.AppComponent
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.ui.widget.MementoToolbar
import com.alexstyl.specialdates.upcoming.widget.today.UpcomingWidgetConfigurationPanel.*
import com.novoda.notils.caster.Views

import javax.inject.Inject

class UpcomingWidgetConfigureActivity : ThemedMementoActivity() {


    lateinit var preferences: UpcomingWidgetPreferences
        @Inject set
    lateinit var labelCreator: DateLabelCreator
        @Inject set
    lateinit var permissions: MementoPermissions
        @Inject set

    private lateinit var backgroundView: ImageView
    private lateinit var previewLayout: UpcomingWidgetPreviewLayout
    private lateinit var configurationPanel: UpcomingWidgetConfigurationPanel
    private lateinit var loadWallpaperButton: ImageButton
    private lateinit var scrimView: ImageView

    private var mAppWidgetId: Int = 0

    private val configurationListener = object : ConfigurationListener {
        override fun onApplyButtonPressed() {
            saveConfigurations()
            finishAsSuccess()
        }

        override fun onOpacityLevelChanged(percentage: Float) {
            previewLayout.previewBackgroundOpacityLevel(percentage)
        }

        override fun onWidgetVariantSelected(variant: WidgetVariant) {
            previewLayout.previewWidgetVariant(variant)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)

        setContentView(R.layout.activity_upcoming_events_widget_configure)

        backgroundView = Views.findById(this, R.id.upcoming_widget_wallpaper)
        previewLayout = Views.findById(this, R.id.upcoming_widget_preview)
        configurationPanel = Views.findById(this, R.id.upcoming_widget_configure_panel)
        configurationPanel.setListener(configurationListener)

        preferences = UpcomingWidgetPreferences(this)
        initialisePreview(preferences)
        considerAsNotComplete()

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        val closeButton = findViewById<View>(R.id.upcoming_widget_close)
        TooltipCompat.setTooltipText(closeButton, getString(R.string.Close))
        closeButton.setOnClickListener {
            finish()
        }
        scrimView = findViewById(R.id.scrim)
        loadWallpaperButton = findViewById(R.id.upcoming_widget_load_wallpaper)
        TooltipCompat.setTooltipText(loadWallpaperButton, getString(R.string.Load_my_wallpaper))
        loadWallpaperButton.setOnClickListener {
            requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSION_WALLPAPER)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION_WALLPAPER && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            displayCurrentWallpaper()
            loadWallpaperButton.visibility = View.GONE

        }
    }

    private fun considerAsNotComplete() {
        setResult(Activity.RESULT_CANCELED)
    }

    private fun initialisePreview(preferences: UpcomingWidgetPreferences) {
        val startingOpacity = preferences.oppacityLevel
        val startingVariant = preferences.selectedVariant
        configurationPanel.opacityLevel = startingOpacity
        configurationPanel.widgetVariant = startingVariant
    }

    override fun onStart() {
        super.onStart()

        val title = labelCreator.createWithYearPreferred(Date.today())
        previewLayout.setTitle(title)
        previewLayout.setSubtitle(R.string.upcoming_widget_configure_subtitle)

        val variant = preferences.selectedVariant
        previewLayout.previewWidgetVariant(variant)

        val oppacityLevel = preferences.oppacityLevel
        previewLayout.previewBackgroundOpacityLevel(oppacityLevel)

    }

    override fun onResume() {
        super.onResume()
        if (permissions.canReadExternalStorage()) {
            loadWallpaperButton.visibility = View.GONE
            displayCurrentWallpaper()
        } else {
            scrimView.visibility = View.GONE
        }
    }

    private fun displayCurrentWallpaper() {
        val wallpaperManager = WallpaperManager.getInstance(this)
        val wallpaperDrawable = wallpaperManager.drawable
        backgroundView.setImageDrawable(wallpaperDrawable)
        backgroundView.alpha = 0f
        backgroundView.animate().alpha(1f).setDuration(250).start()

        scrimView.animate().alpha(1f).setDuration(250).start()
        scrimView.visibility = View.VISIBLE
    }

    private fun saveConfigurations() {
        val userOptions = configurationPanel.userOptions
        preferences.storeUserOptions(userOptions)
        TodayUpcomingEventsView(this, AppWidgetManager.getInstance(this)).reloadUpcomingEventsView()
    }

    private fun finishAsSuccess() {
        setResult(Activity.RESULT_OK, Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId))
        finish()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSION_WALLPAPER = 9990
    }
}
