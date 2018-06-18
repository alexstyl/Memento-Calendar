package com.alexstyl.specialdates.person

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.alexstyl.android.Version
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.ExternalNavigator
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.analytics.Screen
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactNotFoundException
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.DummyHideStatusBarListener
import com.alexstyl.specialdates.ui.LolipopHideStatusBarListener
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.ui.widget.MementoToolbar
import com.novoda.notils.caster.Views
import javax.inject.Inject

class PersonActivity : ThemedMementoActivity(), BottomSheetIntentListener {

    lateinit var analytics: Analytics
        @Inject set
    lateinit var imageLoader: ImageLoader
        @Inject set
    lateinit var contactsProvider: ContactsProvider
        @Inject set
    lateinit var tracker: CrashAndErrorTracker
        @Inject set
    lateinit var presenter: PersonPresenter
        @Inject set

    private var navigator: PersonDetailsNavigator? = null
    private var displayingContact = Optional.absent<Contact>()
    private var adapter: ContactItemsAdapter? = null

    private val isVisibleContactOptional = Optional.absent<Boolean>()

    private lateinit var personView: AndroidPersonView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)
        analytics.trackScreen(Screen.PERSON)
        navigator = PersonDetailsNavigator(ExternalNavigator(this, analytics, tracker))
        val toolbar = Views.findById<MementoToolbar>(this, R.id.person_toolbar)
        if (wasCalledFromMemento()) {
            toolbar.displayNavigationIconAsUp()
        } else {
            toolbar.displayNavigationIconAsClose()
        }

        setSupportActionBar(toolbar)
        title = null
        adapter = ContactItemsAdapter(LayoutInflater.from(thisActivity()), EventPressedListener { (action) ->
            try {
                action.run()
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(thisActivity(), R.string.no_app_found, Toast.LENGTH_SHORT).show()
                tracker.track(ex)
            }
        })

        val appBarLayout = findViewById<AppBarLayout>(R.id.person_appbar)
        val avatarView = findViewById<ImageView>(R.id.person_avatar)
        val personNameView = findViewById<TextView>(R.id.person_name)
        val ageAndSignView = findViewById<TextView>(R.id.person_age_and_sign)
        val viewPager = findViewById<ViewPager>(R.id.person_viewpager)
        val toolbarGradient = findViewById<ImageView>(R.id.person_toolbar_gradient)
        val tabLayout = findViewById<TabLayout>(R.id.person_tabs)

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2

        tabLayout.setupWithViewPager(viewPager, true)

        val onOffsetChangedListener = if (Version.hasLollipop()) LolipopHideStatusBarListener(window) else DummyHideStatusBarListener()

        personView = AndroidPersonView(
                personNameView,
                ageAndSignView, imageLoader, avatarView, adapter!!, tabLayout, appBarLayout,
                toolbarGradient, resources,
                onOffsetChangedListener,
                themer)

    }

    override fun onResume() {
        super.onResume()
        displayingContact = extractContactFrom(intent)
        if (displayingContact.isPresent) {
            presenter.startPresentingInto(personView, displayingContact.get(), AndroidContactActions(this))
        } else {
            tracker.track(IllegalArgumentException("No contact to display"))
            finish()
        }
    }

    private fun wasCalledFromMemento(): Boolean {
        val extras = intent.extras
        return extras != null && intent.extras.containsKey(EXTRA_CONTACT_ID)
    }

    private fun extractContactFrom(intent: Intent): Optional<Contact> {
        val data = intent.data
        if (data != null) {
            val contactId = data.lastPathSegment.toLong()
            return contactFor(contactId, SOURCE_DEVICE)
        }

        val contactID = intent.getLongExtra(EXTRA_CONTACT_ID, -1)
        if (contactID == -1L) {
            return Optional.absent()
        }
        @ContactSource val contactSource = intent.getIntExtra(EXTRA_CONTACT_SOURCE, -1)

        return if (contactSource == -1) {
            Optional.absent()
        } else contactFor(contactID, contactSource)
    }

    private fun contactFor(contactID: Long, contactSource: Int): Optional<Contact> {
        return try {
            Optional(contactsProvider.getContact(contactID, contactSource))
        } catch (e: ContactNotFoundException) {
            tracker.track(e)
            Optional.absent()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_person_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == android.R.id.home && !wasCalledFromMemento()) {
            finish()
            return true
        } else if (itemId == R.id.menu_view_contact) {
            navigator!!.toViewContact(displayingContact)
        } else if (itemId == ID_TOGGLE_VISIBILITY) {
            val isVisible = isVisibleContactOptional.get()
            if (isVisible) {
                presenter.hideContact(personView)
            } else {
                presenter.showContact(personView)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        presenter.stopPresenting()
    }

    override fun onActivitySelected(intent: Intent) {
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        private const val EXTRA_CONTACT_SOURCE = "extra:source"
        private const val EXTRA_CONTACT_ID = "extra:id"

        private const val ID_TOGGLE_VISIBILITY = 1023

        fun buildIntentFor(context: Context, contact: Contact): Intent {
            val intent = Intent(context, PersonActivity::class.java)
            intent.putExtra(EXTRA_CONTACT_ID, contact.contactID)
            intent.putExtra(EXTRA_CONTACT_SOURCE, contact.source)
            return intent
        }
    }
}

