package com.alexstyl.specialdates.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.alexstyl.android.isOnline
import com.alexstyl.specialdates.BuildConfig
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.analytics.Screen
import com.alexstyl.specialdates.dailyreminder.DailyReminderNotifier
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler
import com.alexstyl.specialdates.dailyreminder.DailyReminderUserSettings
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.donate.DonateMonitor
import com.alexstyl.specialdates.donate.DonationPreferences
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.alexstyl.specialdates.ui.ViewFader
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.upcoming.DatePickerDialogFragment
import com.alexstyl.specialdates.upcoming.view.ExposedSearchToolbar
import com.google.android.gms.ads.MobileAds
import com.novoda.notils.caster.Views.findById
import com.novoda.notils.meta.AndroidUtils
import javax.inject.Inject

class HomeActivity : ThemedMementoActivity(), DatePickerDialogFragment.OnDateSetListener {


    @Inject lateinit var navigator: HomeNavigator
    @Inject lateinit var analytics: Analytics
    @Inject lateinit var dailyReminderNotifier: DailyReminderNotifier
    @Inject lateinit var donationPreferences: DonationPreferences
    @Inject lateinit var permissions: MementoPermissions
    @Inject lateinit var peopleEventsUpdater: PeopleEventsUpdater
    @Inject lateinit var dailyReminderScheduler: DailyReminderScheduler
    @Inject lateinit var dailyreminderUserSettings: DailyReminderUserSettings
    @Inject lateinit var donateMonitor: DonateMonitor

    private var searchTransitioner: SearchTransitioner? = null

    private var viewPager: ViewPager? = null
    private var banner: DonationBannerView? = null
    private var actionButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        MobileAds.initialize(this, BuildConfig.ADMOB_APP_ID)

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)

        analytics.trackScreen(Screen.HOME)

        val toolbar = findById<ExposedSearchToolbar>(this, R.id.home_toolbar)
        toolbar.setOnClickListener { v ->
            AndroidUtils.toggleKeyboard(v.context)
            onSearchRequested()
        }
        setSupportActionBar(toolbar)

        viewPager = findViewById(R.id.home_viewpager)
        val adapter = HomeViewPagerAdapter(supportFragmentManager)
        viewPager!!.adapter = adapter
        viewPager!!.offscreenPageLimit = adapter.count

        val content = findViewById<LinearLayout>(R.id.home_content)
        val toolbarHolder = findViewById<FrameLayout>(R.id.home_toolbar_holder)
        searchTransitioner = SearchTransitioner(this, navigator, content, toolbar, toolbarHolder, ViewFader())

        setTitle(R.string.app_name)

        val tabLayout = findViewById<TabLayout>(R.id.home_tabs)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(HomeActivity.PAGE_EVENTS)!!.icon = getTintedDrawable(R.drawable.ic_events)
        tabLayout.getTabAt(HomeActivity.PAGE_CONTACTS)!!.icon = getTintedDrawable(R.drawable.ic_contacts)
        tabLayout.getTabAt(HomeActivity.PAGE_SETTINGS)!!.icon = getTintedDrawable(R.drawable.ic_settings)

        actionButton = findViewById(R.id.home_add_event)
        actionButton?.setOnClickListener { navigator.toAddEvent(thisActivity(), CODE_ADD_EVENT) }

        viewPager?.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if ((position == HomeActivity.PAGE_SETTINGS)
                        or (position == HomeActivity.PAGE_CONTACTS && positionOffset >= 0.5)) {
                    actionButton!!.hide()
                } else {
                    actionButton!!.show()
                }
            }
        })

        banner = findViewById(R.id.home_ad_banner)
        banner?.setOnCloseBannerListener {
            navigator.toDonate(this)
        }

        if (hasNotDonated() && isOnline()) {
            banner?.loadAd()
        }

        if (ThemedMementoActivity.ACTION_UPDATE_THEME == intent.action) {
            viewPager!!.currentItem = HomeActivity.PAGE_SETTINGS
        }
    }


    private fun hasNotDonated() = !donationPreferences.hasDonated()

    override fun onResume() {
        super.onResume()
        if (!permissions.canReadAndWriteContacts()) {
            navigator.toContactPermission(this, CODE_PERMISSION)
        }
        if (viewPager!!.currentItem != PAGE_SETTINGS) {
            actionButton!!.show()
        }
        searchTransitioner!!.onActivityResumed()
        donateMonitor.addListener(donateMonitorListener)
    }

    private val donateMonitorListener = object : DonateMonitor.DonateMonitorListener {
        override fun onUserDonated() {
            banner?.hide()
        }
    }

    override fun onStop() {
        super.onStop()
        donateMonitor.removeListener(donateMonitorListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                peopleEventsUpdater
                        .updateEvents()
                        .subscribe()
                dailyReminderScheduler
                        .scheduleReminderFor(dailyreminderUserSettings.getTimeSet())
            } else {
                finishAffinity()
            }
        }
    }

    override fun onDateSelected(dateSelected: Date) {
        navigator.toDateDetails(dateSelected, this)
    }

    override fun onSearchRequested(): Boolean {
        actionButton!!.hide()
        searchTransitioner!!.transitionToSearch()
        return true
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        dailyReminderNotifier.cancelAllEvents()
    }

    override fun onBackPressed() {
        if (viewPager!!.currentItem == HomeActivity.PAGE_EVENTS) {
            super.onBackPressed()
        } else {
            viewPager!!.currentItem = HomeActivity.PAGE_EVENTS
        }
    }

    companion object {

        private const val CODE_PERMISSION = 150
        const val PAGE_EVENTS = 0
        const val PAGE_CONTACTS = 1
        const val PAGE_SETTINGS = 2
        const val CODE_ADD_EVENT = 120

        fun getStartIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
}
