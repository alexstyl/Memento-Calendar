package com.alexstyl.specialdates.person

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.alexstyl.specialdates.images.ImageLoadedConsumer
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.HideStatusBarListener
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.ui.widget.MementoToolbar
import com.nostra13.universalimageloader.core.assist.LoadedFrom
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware
import com.novoda.notils.caster.Views
import javax.inject.Inject

class PersonActivity : ThemedMementoActivity(), PersonView, BottomSheetIntentListener {

    var analytics: Analytics? = null
        @Inject set
    var imageLoader: ImageLoader? = null
        @Inject set
    var contactsProvider: ContactsProvider? = null
        @Inject set
    var tracker: CrashAndErrorTracker? = null
        @Inject set
    var presenter: PersonPresenter? = null
        @Inject set

    private var appBarLayout: AppBarLayout? = null
    private var toolbarGradient: ImageView? = null
    private var avatarView: ImageView? = null
    private var personNameView: TextView? = null
    private var ageAndSignView: TextView? = null
    private var tabLayout: TabLayout? = null

    private var displayingContact = Optional.absent<Contact>()
    private var navigator: PersonDetailsNavigator? = null
    private var adapter: ContactItemsAdapter? = null

    private val isVisibleContactOptional = Optional.absent<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        appBarLayout = findViewById(R.id.person_appbar)

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)
        analytics!!.trackScreen(Screen.PERSON)
        navigator = PersonDetailsNavigator(ExternalNavigator(this, analytics, tracker))
        val toolbar = Views.findById<MementoToolbar>(this, R.id.person_toolbar)
        if (wasCalledFromMemento()) {
            toolbar.displayNavigationIconAsUp()
        } else {
            toolbar.displayNavigationIconAsClose()
        }

        setSupportActionBar(toolbar)
        title = null
        avatarView = Views.findById(this, R.id.person_avatar)
        personNameView = Views.findById(this, R.id.person_name)
        ageAndSignView = Views.findById(this, R.id.person_age_and_sign)
        val viewPager = Views.findById<ViewPager>(this, R.id.person_viewpager)
        toolbarGradient = Views.findById(this, R.id.person_toolbar_gradient)
        adapter = ContactItemsAdapter(LayoutInflater.from(thisActivity()), EventPressedListener { (action) ->
            try {
                action.run()
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(thisActivity(), R.string.no_app_found, Toast.LENGTH_SHORT).show()
                tracker!!.track(ex)
            }
        }
        )

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2

        tabLayout = Views.findById(this, R.id.person_tabs)
        tabLayout!!.setupWithViewPager(viewPager, true)

    }

    override fun onResume() {
        super.onResume()
        displayingContact = extractContactFrom(intent)
        if (displayingContact.isPresent) {
            presenter!!.startPresentingInto(this, displayingContact.get(), AndroidContactActions(this))
        } else {
            tracker!!.track(IllegalArgumentException("No contact to display"))
            finish()
        }
    }

    private fun wasCalledFromMemento(): Boolean {
        val extras = intent.extras
        return extras != null && intent.extras!!.containsKey(EXTRA_CONTACT_ID)
    }

    private fun extractContactFrom(intent: Intent): Optional<Contact> {
        val data = intent.data
        if (data != null) {
            val contactId = java.lang.Long.valueOf(data.lastPathSegment)
            return contactFor(contactId!!, SOURCE_DEVICE)
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
            Optional(contactsProvider!!.getContact(contactID, contactSource))
        } catch (e: ContactNotFoundException) {
            tracker!!.track(e)
            Optional.absent()
        }

    }

    override fun displayPersonInfo(viewModel: PersonInfoViewModel) {
        personNameView!!.text = viewModel.displayName
        ageAndSignView!!.text = viewModel.ageAndStarSignlabel
        ageAndSignView!!.visibility = viewModel.AgeAndStarSignVisibility

        imageLoader!!.load(viewModel.image)
                .withSize(avatarView!!.width, avatarView!!.height)
                .into(object : ImageLoadedConsumer {

                    override fun onImageLoaded(loadedImage: Bitmap) {
                        if (Version.hasLollipop()) {
                            appBarLayout!!.addOnOffsetChangedListener(HideStatusBarListener(window))
                        }
                        FadeInBitmapDisplayer(ANIMATION_DURATION).display(loadedImage, ImageViewAware(avatarView!!), LoadedFrom.DISC_CACHE)
                        val layers = arrayOfNulls<Drawable>(2)
                        layers[0] = resources.getColorDrawable(android.R.color.transparent)
                        layers[1] = resources.getDrawableCompat(R.drawable.black_to_transparent_gradient_facing_down)
                        val transitionDrawable = TransitionDrawable(layers)
                        toolbarGradient!!.setImageDrawable(transitionDrawable)
                        transitionDrawable.startTransition(ANIMATION_DURATION)
                        toolbarGradient!!.visibility = View.VISIBLE
                    }

                    override fun onLoadingFailed() {
                        val layers = arrayOfNulls<Drawable>(2)
                        layers[0] = resources.getColorDrawable(android.R.color.transparent)
                        layers[1] = resources.getDrawableCompat(R.drawable.ic_person_96dp)
                        val transitionDrawable = TransitionDrawable(layers)
                        avatarView!!.setImageDrawable(transitionDrawable)
                        transitionDrawable.startTransition(ANIMATION_DURATION)
                        toolbarGradient!!.visibility = View.GONE
                    }
                })
    }

    override fun displayAvailableActions(viewModel: PersonAvailableActionsViewModel) {
        adapter!!.displayEvents(viewModel)

        updateTabIfNeeded(0, R.drawable.ic_gift)
        updateTabIfNeeded(1, R.drawable.ic_call)
        updateTabIfNeeded(2, R.drawable.ic_message)

        if (tabLayout!!.tabCount <= 1) {
            tabLayout!!.visibility = View.GONE
        } else {
            tabLayout!!.visibility = View.VISIBLE
        }
    }

    private fun updateTabIfNeeded(index: Int, @DrawableRes iconResId: Int) {
        if (tabLayout!!.getTabAt(index) != null) {
            tabLayout!!.getTabAt(index)!!.icon = getTintedDrawable(iconResId)
        }
    }

    override fun showPersonAsVisible() {
        throw UnsupportedOperationException("Visibility is not currently available")
    }

    override fun showPersonAsHidden() {
        throw UnsupportedOperationException("Visibility is not currently available")
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
            if (isVisible!!) {
                presenter!!.hideContact(this)
            } else {
                presenter!!.showContact(this)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        presenter!!.stopPresenting()
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
        private const val ANIMATION_DURATION = 400

        private const val ID_TOGGLE_VISIBILITY = 1023

        fun buildIntentFor(context: Context, contact: Contact): Intent {
            val intent = Intent(context, PersonActivity::class.java)
            intent.putExtra(EXTRA_CONTACT_ID, contact.contactID)
            intent.putExtra(EXTRA_CONTACT_SOURCE, contact.source)
            return intent
        }
    }
}

private fun Resources.getDrawableCompat(@DrawableRes drawableResId: Int): Drawable? =
        ResourcesCompat.getDrawable(this, drawableResId, null)

private fun Resources.getColorDrawable(@ColorRes colorRes: Int): Drawable? =
        ColorDrawable(ResourcesCompat.getColor(this, colorRes, null))
