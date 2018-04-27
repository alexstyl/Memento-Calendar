package com.alexstyl.specialdates.addevent

import android.content.res.Resources
import android.graphics.Bitmap
import android.os.AsyncTask
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.images.DecodedImage
import com.alexstyl.specialdates.images.ImageDecoder
import com.novoda.notils.logger.simple.Log
import io.reactivex.Scheduler


class AddContactEventsPresenter(private val analytics: Analytics,
                                private val eventsPresenter: EventsPresenter,
                                private val contactOperations: ContactOperations,
                                private val messageDisplayer: MessageDisplayer,
                                private val operationsExecutor: ContactOperationsExecutor,
                                private val resources: Resources,
                                private val imageDecoder: ImageDecoder,
                                private val workScheduler: Scheduler,
                                private val resultScheduler: Scheduler) {

    private val currentImageLoaded = Optional.absent<Bitmap>()

    private var selectedContact = SelectedContact.anonymous()
    var isHoldingModifiedData = false


    var view: AddEventView? = null

    fun startPresentingInto(view: AddEventView) {
        eventsPresenter.startPresentingInto(view)
        this.view = view
    }

    fun presentContact(contact: Contact) {
        analytics.trackContactSelected()
        eventsPresenter.onContactSelected(contact)
        selectedContact = SelectedContact.forContact(contact)
        isHoldingModifiedData = false
        this.view?.displayContact(contact)
    }

    fun onEventDatePicked(eventType: EventType, date: Date) {
        analytics.trackEventDatePicked(eventType)
        eventsPresenter.onEventDatePicked(eventType, date)
        isHoldingModifiedData = true
    }

    fun removeEvent(eventType: EventType) {
        analytics.trackEventRemoved(eventType)
        eventsPresenter.removeEvent(eventType)
        isHoldingModifiedData = true
    }

    fun onNameModified(text: String) {
        if (selectedContact.containsContact()) {
            view?.removeAvatar()
        }
        selectedContact = SelectedContact.anonymous(text)
        isHoldingModifiedData = true
    }

    fun getDecodedImage(): DecodedImage {
        return if (currentImageLoaded.isPresent)
            imageDecoder.decodeFrom(currentImageLoaded.get())
        else
            DecodedImage.EMPTY
    }

    fun saveChanges() {
        if (!isHoldingModifiedData) {
            Log.w("Nothing to save. Bailing fast")
            return
        }
        if (selectedContact.containsContact()) {
            object : AsyncTask<Void, Void, Boolean>() {
                override fun doInBackground(vararg params: Void): Boolean? {
                    val contact = selectedContact.contact

                    val builder = contactOperations.updateExistingContact(contact)
                            .withEvents(eventsPresenter.events.events)
                            .updateContactImage(getDecodedImage())
                    return operationsExecutor.execute(builder.build())
                }

                override fun onPostExecute(success: Boolean?) {
                    if (success!!) {
                        analytics.trackContactUpdated()
                        messageDisplayer.showMessage(resources.getString(R.string.add_event_contact_updated))
                    } else {
                        messageDisplayer.showMessage(resources.getString(R.string.add_event_failed_to_update_contact))
                    }

                }
            }.execute()
        } else {
            object : AsyncTask<Void, Void, Boolean>() {
                override fun doInBackground(vararg params: Void): Boolean? {

                    val builder = contactOperations.createNewContact(selectedContact.displayName)
                            .withEvents(eventsPresenter.events.events)
                    val decodedImage = getDecodedImage()
                    if (decodedImage != DecodedImage.EMPTY) {
                        builder.addContactImage(decodedImage)
                    }
                    return operationsExecutor.execute(builder.build())
                }

                override fun onPostExecute(success: Boolean?) {
                    if (success!!) {
                        analytics.trackContactCreated()
                        messageDisplayer.showMessage(resources.getString(R.string.add_birthday_contact_added))
                    } else {
                        messageDisplayer.showMessage(resources.getString(R.string.add_birthday_failed_to_add_contact))
                    }
                }
            }.execute()
        }
    }

    fun isDisplayingAvatar(): Boolean {
        return currentImageLoaded.isPresent
    }
}
