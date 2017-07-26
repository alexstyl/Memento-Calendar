package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import io.reactivex.Observable

class PersonCallProvider(
        private val androidActionsProvider: AndroidContactActionsProvider,
        private val facebookContactActionsProvider: FacebookContactActionsProvider) {


    fun getCallsFor(contact: Contact): Observable<List<ContactActionViewModel>> {
        return Observable.fromCallable {
            if (contact.source == ContactSource.SOURCE_FACEBOOK) {
                facebookContactActionsProvider.callActionsFor(contact)
            } else if (contact.source == ContactSource.SOURCE_DEVICE) {
                androidActionsProvider.callActionsFor(contact)
            } else {
                throw IllegalArgumentException("unknown contact type " + contact.source)
            }
        }
    }

    fun getMessagesFor(contact: Contact): Observable<List<ContactActionViewModel>> {
        return Observable.fromCallable {
            if (contact.source == ContactSource.SOURCE_FACEBOOK) {
                facebookContactActionsProvider.messagingActionsFor(contact)
            } else if (contact.source == ContactSource.SOURCE_DEVICE) {
                androidActionsProvider.messagingActionsFor(contact)
            } else {
                throw IllegalArgumentException("unknown contact type " + contact.source)
            }
        }
    }

}
