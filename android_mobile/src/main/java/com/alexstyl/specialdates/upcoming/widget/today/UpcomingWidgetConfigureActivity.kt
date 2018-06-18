package com.alexstyl.specialdates.upcoming.widget.today

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.WallpaperManager
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.TooltipCompat
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alexstyl.android.Version
import com.alexstyl.android.toBitmap

import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.upcoming.widget.today.UpcomingWidgetConfigurationPanel.*
import com.novoda.notils.caster.Views

import javax.inject.Inject

class UpcomingWidgetConfigureActivity : ThemedMementoActivity() {

    lateinit var luminanceAnalyzer: LuminanceAnalyzer
        @Inject set
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
    private lateinit var closeButton: ImageButton
    private lateinit var titleView: TextView


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

        if (supportsTransparentStatusbar()) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)

        setContentView(R.layout.activity_upcoming_events_widget_configure)

        backgroundView = Views.findById(this, R.id.upcoming_widget_wallpaper)
        previewLayout = Views.findById(this, R.id.upcoming_widget_preview)
        configurationPanel = Views.findById(this, R.id.upcoming_widget_configure_panel)
        configurationPanel.setListener(configurationListener)
        titleView = findViewById(R.id.upcoming_widget_title)
        initialisePreview()
        setResult(Activity.RESULT_CANCELED)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        closeButton = findViewById(R.id.upcoming_widget_close)
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


        if (!supportsTransparentStatusbar()) {
            val toolbar = findViewById<LinearLayout>(R.id.upcoming_widget_virtual_toolbar)
            val params = toolbar.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            toolbar.layoutParams = params
        }

        if (permissions.canReadExternalStorage()) {
            loadWallpaper()
        }
    }

    private fun supportsTransparentStatusbar() = Version.hasMarshmallow()


    private fun loadWallpaper() {
        val wallpaperManager = WallpaperManager.getInstance(this)
        val wallpaper = wallpaperManager.drawable.toBitmap()

        backgroundView.setImageBitmap(wallpaper)
        updateUIColorsFor(wallpaper)
        loadWallpaperButton.visibility = View.GONE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION_WALLPAPER && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            displayCurrentWallpaper()
            loadWallpaperButton.visibility = View.GONE
        }
    }

    private fun initialisePreview() {
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


    private fun displayCurrentWallpaper() {
        val wallpaperManager = WallpaperManager.getInstance(this)
        val wallpaper = wallpaperManager.drawable.toBitmap()

        revealWallpaper(wallpaper)
        updateUIColorsFor(wallpaper)
    }

    private fun revealWallpaper(wallpaper: Bitmap) {
        backgroundView.setImageBitmap(wallpaper)
        backgroundView.visibility = View.INVISIBLE

        if (Version.hasLollipop()) {
            val cx = loadWallpaperButton.x + loadWallpaperButton.width / 2
            val cy = loadWallpaperButton.y + loadWallpaperButton.height / 2

            val finalRadius = backgroundView.height

            val anim =
                    ViewAnimationUtils.createCircularReveal(backgroundView, cx.toInt(),
                            cy.toInt(), 0F, finalRadius.toFloat())
            anim.interpolator = LinearInterpolator()
            backgroundView.visibility = View.VISIBLE
            anim.interpolator = FastOutSlowInInterpolator()
            anim.duration = 700L
            anim.start()
        } else {
            backgroundView.alpha = 0f
            backgroundView.animate().alpha(1f).setDuration(250).start()
        }
    }

    private fun updateUIColorsFor(wallpaper: Bitmap) {

        luminanceAnalyzer.analyse(wallpaper, { isLight ->
            if (isLight) {
                loadDarkUI()
            } else {
                loadLightUI()
            }
        })
    }

    private fun loadLightUI() {
        scrimView.visibility = View.VISIBLE

        titleView.setTextColor(Color.WHITE)
        closeButton.setImageResource(R.drawable.ic_close_white)
        loadWallpaperButton.setImageResource(R.drawable.ic_round_wallpaper_light_24px)
        if (supportsTransparentStatusbar()) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
    }


    private fun loadDarkUI() {
        scrimView.visibility = View.GONE

        titleView.setTextColor(ResourcesCompat.getColor(resources, R.color.dark_text, null))
        closeButton.setImageResource(R.drawable.ic_close_black)
        loadWallpaperButton.setImageResource(R.drawable.ic_round_wallpaper_dark_24px)
        if (supportsTransparentStatusbar()) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
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
