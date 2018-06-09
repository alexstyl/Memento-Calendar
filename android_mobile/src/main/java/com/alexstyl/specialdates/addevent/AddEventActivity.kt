package com.alexstyl.specialdates.addevent

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import com.alexstyl.android.toURI
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.addevent.EventDatePickerDialogFragment.OnEventDatePickedListener
import com.alexstyl.specialdates.addevent.bottomsheet.BottomSheetPicturesDialog
import com.alexstyl.specialdates.addevent.bottomsheet.BottomSheetPicturesDialog.Listener
import com.alexstyl.specialdates.addevent.bottomsheet.ImagePickerOptionViewModel
import com.alexstyl.specialdates.addevent.ui.AvatarPickerView
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.analytics.Screen
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.ShortDateLabelCreator
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.ui.widget.MementoToolbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.net.URI
import javax.inject.Inject

class AddEventActivity : ThemedMementoActivity(), Listener, OnEventDatePickedListener, DiscardPromptDialog.Listener {

    lateinit var presenter: AddEventsPresenter
        @Inject set
    lateinit var permissionChecker: MementoPermissions
        @Inject set
    lateinit var uriFilePathProvider: UriFilePathProvider
        @Inject set
    lateinit var analytics: Analytics
        @Inject set
    lateinit var imageLoader: ImageLoader
        @Inject set
    lateinit var tracker: CrashAndErrorTracker
        @Inject set
    lateinit var shortDateLabelCreator: ShortDateLabelCreator
        @Inject set

    lateinit var view: AddEventView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_from_below, R.anim.stay)
        setContentView(R.layout.activity_add_event)

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)
        analytics.trackScreen(Screen.ADD_EVENT)
        val toolbar = findViewById<MementoToolbar>(R.id.memento_toolbar)
        setSupportActionBar(toolbar)
        toolbar.displayNavigationIconAsClose()

        val avatarView = findViewById<AvatarPickerView>(R.id.add_event_avatar)
        val eventsView = findViewById<RecyclerView>(R.id.add_event_events)

        eventsView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        eventsView.setHasFixedSize(true)
        val adapter = ContactEventsAdapter(contactDetailsListener)
        eventsView.adapter = adapter

        avatarView.setOnClickListener {
            if (avatarView.isDisplayingAvatar) {
                if (permissionChecker.canReadExternalStorage()) {
                    BottomSheetPicturesDialog
                            .includeClearImageOption()
                            .show(supportFragmentManager, "picture_pick")
                } else {
                    requestExternalStoragePermission()
                }
            } else {
                if (permissionChecker.canReadExternalStorage()) {
                    BottomSheetPicturesDialog.newInstance()
                            .show(supportFragmentManager, "picture_pick")
                } else {
                    requestExternalStoragePermission()
                }
            }
        }

        val saveButton = findViewById<View>(R.id.add_event_save)
        saveButton.setOnClickListener {
            presenter.saveChanges()
            finishActivitySuccessfully()
        }
        view = AndroidAddEventView(avatarView, adapter, imageLoader, createToolbarAnimator(toolbar), saveButton)
        presenter.startPresentingInto(view)
    }


    @TargetApi(Build.VERSION_CODES.M)
    private fun requestExternalStoragePermission() {
        requestPermissions(
                arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE, // read any images from disk to use as avatar
                        Manifest.permission.WRITE_EXTERNAL_STORAGE // save to disk any picture taken by camera
                ),
                CODE_PERMISSION_EXTERNAL_STORAGE)
    }

    private fun createToolbarAnimator(toolbar: MementoToolbar): ToolbarBackgroundAnimator {
        return if (resources.getBoolean(R.bool.isLandscape)) {
            ToolbarBackgroundStubAnimator()
        } else {
            ToolbarBackgroundFadingAnimator.setupOn(toolbar)
        }
    }

    private val contactDetailsListener = object : ContactDetailsListener {
        override fun onContactCleared() {
            presenter.removeContact()
        }

        override fun onAddEventClicked(viewModel: AddEventContactEventViewModel) {
            val eventType = viewModel.eventType
            val initialDate = viewModel.date

            val dialog = EventDatePickerDialogFragment.newInstance(eventType, initialDate, shortDateLabelCreator)
            dialog.show(supportFragmentManager, "pick_event")
        }

        override fun onRemoveEventClicked(eventType: EventType) {
            presenter.removeEvent(eventType)
        }

        override fun onContactSelected(contact: Contact) {
            presenter.presentContact(contact)
        }

        override fun onNameModified(newName: String) {
            presenter.removeContact()
            presenter.presentName(newName)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODE_PERMISSION_EXTERNAL_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            findViewById<View>(android.R.id.content).post(Runnable {
                if (isFinishing) {
                    return@Runnable
                }
                if (presenter.isDisplayingAvatar()) {
                    BottomSheetPicturesDialog
                            .includeClearImageOption()
                            .show(supportFragmentManager, "picture_pick")
                } else {
                    BottomSheetPicturesDialog
                            .newInstance()
                            .show(supportFragmentManager, "picture_pick")
                }
            })
        }
    }


    var viewModel: ImagePickerOptionViewModel? = null

    override fun onImagePickerOptionSelected(viewModel: ImagePickerOptionViewModel) {
        this.viewModel = viewModel
//        grantUriPermission(viewModel.intent.component.packageName,
//                viewModel.absolutePath,
//                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
//        )
        startActivityForResult(viewModel.intent, getRequestCodeFor(viewModel.intent))
    }


    private fun getRequestCodeFor(intent: Intent): Int {
        val action = intent.action
        return when (action) {
            ImageIntentFactory.ACTION_IMAGE_CAPTURE -> CODE_TAKE_PICTURE
            ImageIntentFactory.ACTION_IMAGE_PICK -> CODE_PICK_A_FILE
            else -> throw IllegalArgumentException("Don't know how to handle $action")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            analytics.trackImageCaptured()
            startCropIntent((URI.create("file://" + viewModel!!.absolutePath))) // TODO get rid of file
        } else if (requestCode == CODE_PICK_A_FILE && resultCode == Activity.RESULT_OK) {
            analytics.trackExistingImagePicked()
            val imageUri = BottomSheetPicturesDialog.getImagePickResultUri(data!!)
            startCropIntent(imageUri)
        } else if (requestCode == CODE_CROP_IMAGE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                analytics.trackAvatarSelected()
                presenter.present(result.uri.toURI())
            } else if (resultCode == Activity.RESULT_CANCELED && result != null) {
                tracker.track(result.error)
            }
        }
    }

    private fun startCropIntent(imageToCrop: URI) {
        val size = queryCropSize(contentResolver)
        CropImage.activity(Uri.parse(imageToCrop.toString()))
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setRequestedSize(size, size)
                .start(this)
    }

    override fun onDatePicked(eventType: EventType, date: Date) {
        presenter.onEventDatePicked(eventType, date)
    }

    private fun queryCropSize(resolver: ContentResolver): Int {
        resolver.query(
                ContactsContract.DisplayPhoto.CONTENT_MAX_DIMENSIONS_URI,
                arrayOf(ContactsContract.DisplayPhoto.DISPLAY_MAX_DIM), null, null, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getInt(0)
            }
        }
        return MAX_RESOLUTION
    }

    override fun onClearAvatarSelected() {
        view.clearAvatar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (presenter.isHoldingModifiedData) {
                    promptToDiscardBeforeExiting()
                } else {
                    cancelActivity()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun promptToDiscardBeforeExiting() {
        DiscardPromptDialog()
                .show(supportFragmentManager, "discard_prompt")
    }

    private fun finishActivitySuccessfully() {
        analytics.trackEventAddedSuccessfully()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun cancelActivity() {
        analytics.trackAddEventsCancelled()
        setResult(Activity.RESULT_CANCELED)
        navigateUpToParent()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_out_from_below)
    }

    override fun onBackPressed() {
        if (presenter.isHoldingModifiedData) {
            promptToDiscardBeforeExiting()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stopPresenting()
    }

    override fun onDiscardChangesSelected() {
        cancelActivity()
    }

    companion object {

        private const val CODE_TAKE_PICTURE = 404
        private const val CODE_PICK_A_FILE = 405
        private const val CODE_CROP_IMAGE = CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
        private const val CODE_PERMISSION_EXTERNAL_STORAGE = 406

        private const val MAX_RESOLUTION = 720

        fun buildIntent(context: Context): Intent {
            return Intent(context, AddEventActivity::class.java)
        }
    }
}
