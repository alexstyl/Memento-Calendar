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
    lateinit var todayUpcomingEventsView: TodayUpcomingEventsView
        @Inject set

    private lateinit var backgroundView: ImageView
    private lateinit var previewLayout: UpcomingWidgetPreviewLayout
    private lateinit var configurationPanel: UpcomingWidgetConfigurationPanel
    private lateinit var loadWallpaperButton: ImageButton
    private lateinit var scrimView: ImageView
    private lateinit var closeButton: ImageButton
    private lateinit var titleView: TextView

    private var mAppWidgetId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)
        setResult(Activity.RESULT_CANCELED)
        setContentView(R.layout.activity_upcoming_events_widget_configure)

        mAppWidgetId = extractAppWidgetIdFrom(intent)

        decorateStatusBarOrHide()
        backgroundView = findViewById(R.id.upcoming_widget_wallpaper)
        previewLayout = findViewById(R.id.upcoming_widget_preview)
        configurationPanel = findViewById(R.id.upcoming_widget_configure_panel)
        titleView = findViewById(R.id.upcoming_widget_title)
        closeButton = findViewById(R.id.upcoming_widget_close)
        scrimView = findViewById(R.id.scrim)
        loadWallpaperButton = findViewById(R.id.upcoming_widget_load_wallpaper)

        initialiseViews()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun initialiseViews() {
        configurationPanel.setListener(object : UpcomingWidgetConfigurationPanel.ConfigurationListener {
            override fun onApplyButtonPressed() {
                preferences.storeUserOptions(configurationPanel.userOptions)
                todayUpcomingEventsView.reloadUpcomingEventsView()

                if (mAppWidgetId != null) {
                    val intent = Intent()
                            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId!!)
                    setResult(Activity.RESULT_OK, intent)
                }
                finish()
            }

            override fun onOpacityLevelChanged(percentage: Float) {
                previewLayout.previewBackgroundOpacityLevel(percentage)
            }

            override fun onWidgetVariantSelected(variant: WidgetVariant) {
                previewLayout.previewWidgetVariant(variant)
            }
        })

        initialisePreview()
        TooltipCompat.setTooltipText(closeButton, getString(R.string.Close))
        TooltipCompat.setTooltipText(loadWallpaperButton, getString(R.string.Load_my_wallpaper))
        closeButton.setOnClickListener {
            finish()
        }
        loadWallpaperButton.setOnClickListener {
            requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSION_WALLPAPER)
        }
        if (permissions.canReadExternalStorage()) {
            displayWallpaper()
        }
    }

    private fun displayWallpaper() {
        val wallpaperManager = WallpaperManager.getInstance(this)
        val wallpaper = wallpaperManager.drawable.toBitmap()

        backgroundView.setImageBitmap(wallpaper)
        updateUIColorsFor(wallpaper)
        loadWallpaperButton.visibility = View.GONE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION_WALLPAPER && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            val wallpaperManager = WallpaperManager.getInstance(this)
            val wallpaper = wallpaperManager.drawable.toBitmap()

            revealWallpaper(wallpaper)
            updateUIColorsFor(wallpaper)

            loadWallpaperButton.visibility = View.GONE
        }
    }

    private fun extractAppWidgetIdFrom(intent: Intent?): Int? {
        return intent?.extras?.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }


    private fun decorateStatusBarOrHide() {
        if (supportsTransparentStatusbar()) {
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            window.statusBarColor = Color.TRANSPARENT
        } else {
            val toolbar = findViewById<LinearLayout>(R.id.upcoming_widget_virtual_toolbar)
            val params = toolbar.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            toolbar.layoutParams = params
        }
    }

    private fun initialisePreview() {
        configurationPanel.opacityLevel = preferences.oppacityLevel
        configurationPanel.widgetVariant = preferences.selectedVariant
    }

    override fun onStart() {
        super.onStart()

        val title = labelCreator.createWithYearPreferred(Date.today())
        previewLayout.setTitle(title)
        previewLayout.setSubtitle(R.string.demo_contact_name)

        val variant = preferences.selectedVariant
        previewLayout.previewWidgetVariant(variant)

        val oppacityLevel = preferences.oppacityLevel
        previewLayout.previewBackgroundOpacityLevel(oppacityLevel)
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

    private fun loadLightUI() {
        scrimView.visibility = View.VISIBLE

        titleView.setTextColor(Color.WHITE)
        closeButton.setImageResource(R.drawable.ic_close_white)
        loadWallpaperButton.setImageResource(R.drawable.ic_round_wallpaper_light_24px)
        if (supportsTransparentStatusbar()) {
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun loadDarkUI() {
        scrimView.visibility = View.GONE

        titleView.setTextColor(ResourcesCompat.getColor(resources, R.color.dark_text, null))
        closeButton.setImageResource(R.drawable.ic_close_black)
        loadWallpaperButton.setImageResource(R.drawable.ic_round_wallpaper_dark_24px)
        if (supportsTransparentStatusbar()) {
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSION_WALLPAPER = 9990

        private fun supportsTransparentStatusbar() = Version.hasMarshmallow()
    }
}
