package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import io.reactivex.Observable

class PersonCallProvider(
        private val deviceActionsProvider: ContactActionsProvider,
        private val facebookContactActionsProvider: ContactActionsProvider) {


    fun getCallsFor(contact: Contact, executor: ContactActions): Observable<List<ContactActionViewModel>> {
        return Observable.fromCallable {
            when {
                contact.source == ContactSource.SOURCE_FACEBOOK -> facebookContactActionsProvider.callActionsFor(contact, executor)
                contact.source == ContactSource.SOURCE_DEVICE -> deviceActionsProvider.callActionsFor(contact, executor)
                else -> throw IllegalArgumentException("unknown contact type " + contact.source)
            }
        }
    }

    fun getMessagesFor(contact: Contact, executor: ContactActions): Observable<List<ContactActionViewModel>> {
        return Observable.fromCallable {
            when {
                contact.source == ContactSource.SOURCE_FACEBOOK -> facebookContactActionsProvider.messagingActionsFor(contact, executor)
                contact.source == ContactSource.SOURCE_DEVICE -> deviceActionsProvider.messagingActionsFor(contact, executor)
                else -> throw IllegalArgumentException("unknown contact type " + contact.source)
            }
        }
    }

}
