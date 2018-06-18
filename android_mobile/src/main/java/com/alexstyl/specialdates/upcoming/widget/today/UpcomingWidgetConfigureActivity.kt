package com.alexstyl.specialdates.upcoming.widget.today

import android.annotation.TargetApi
import android.app.Activity
import android.app.WallpaperManager
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.transition.ChangeBounds
import android.support.transition.TransitionManager
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.TooltipCompat
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alexstyl.android.Version
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.alexstyl.specialdates.ui.base.MementoActivity
import javax.inject.Inject

class UpcomingWidgetConfigureActivity : MementoActivity() {

    lateinit var luminanceAnalyzer: LuminanceAnalyzer
        @Inject set
    lateinit var preferences: UpcomingWidgetPreferences
        @Inject set
    lateinit var labelCreator: DateLabelCreator
        @Inject set
    lateinit var todayUpcomingEventsView: TodayUpcomingEventsView
        @Inject set
    lateinit var permission: MementoPermissions
        @Inject set

    private lateinit var previewLayout: UpcomingWidgetPreviewLayout
    private lateinit var configurationPanel: UpcomingWidgetConfigurationPanel
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
        previewLayout = findViewById(R.id.upcoming_widget_preview)
        configurationPanel = findViewById(R.id.upcoming_widget_configure_panel)
        titleView = findViewById(R.id.upcoming_widget_title)
        closeButton = findViewById(R.id.upcoming_widget_close)
        scrimView = findViewById(R.id.scrim)

        introduceViews()

        initialiseViews()
        if (permission.canReadExternalStorage()) {
            displayWallpaper()
        }
    }

    private fun introduceViews() {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.upcoming_widget_constraint)
        constraintLayout.postDelayed({
            val constraintSet = ConstraintSet()
            constraintSet.clone(this, R.layout.activity_upcoming_events_widget_configure_second_frame)

            val transition = ChangeBounds()
            transition.interpolator = AnticipateOvershootInterpolator(1.0f)
            transition.duration = resources.getInteger(android.R.integer.config_longAnimTime).toLong()

            TransitionManager.beginDelayedTransition(constraintLayout, transition)
            constraintSet.applyTo(constraintLayout)
        }, 450L)

        previewLayout.alpha = 0f

        previewLayout
                .animate()
                .setStartDelay(200L)
                .alpha(1f)
                .setDuration(400L)
                .start()
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
        closeButton.setOnClickListener {
            finish()
        }
    }

    private fun displayWallpaper() {
        val wallpaper = WallpaperManager.getInstance(this).drawable
        luminanceAnalyzer.analyse(wallpaper, { isLight ->
            if (isLight) {
                loadDarkUI()
            } else {
                loadLightUI()
            }
        })
    }

    private fun decorateStatusBarOrHide() {
        if (supportsTransparentStatusbar()) {
            window.decorView.systemUiVisibility =
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

    private fun extractAppWidgetIdFrom(intent: Intent?): Int? {
        return intent?.extras?.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
        )
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

    private fun loadLightUI() {
        scrimView.visibility = View.VISIBLE

        titleView.setTextColor(Color.WHITE)
        closeButton.setImageResource(R.drawable.ic_close_white)
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
        if (supportsTransparentStatusbar()) {
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    companion object {
        private fun supportsTransparentStatusbar() = Version.hasMarshmallow()
    }
}
