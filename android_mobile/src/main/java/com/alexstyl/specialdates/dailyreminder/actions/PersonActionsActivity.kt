package com.alexstyl.specialdates.dailyreminder.actions

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.ACTION_SENDTO
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactIntentExtractor
import com.alexstyl.specialdates.person.AndroidContactActions
import com.alexstyl.specialdates.person.ContactActionsAdapter
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import javax.inject.Inject

class PersonActionsActivity : ThemedMementoActivity() {

    @Inject lateinit var presenter: ContactActionsPresenter
    @Inject lateinit var extractor: ContactIntentExtractor
    @Inject lateinit var errorTracker: CrashAndErrorTracker

    var view: ContactActionsView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        (application as MementoApplication).applicationModule.inject(this)

        val recyclerView = findViewById<RecyclerView>(R.id.actions_list)!!
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = ContactActionsAdapter {
            it.action.run()
            finish()
        }
        recyclerView.adapter = adapter
        val contact = extractor.getContactExtra(intent)
        if (contact != null) {
            view = AndroidContactActionsView(contact, adapter)
        } else {
            errorTracker.track(RuntimeException("Tried to load the actions for a contact from $intent"))
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        if (view != null) {
            startPresentingInto(view!!)
        }
    }

    private fun startPresentingInto(view: ContactActionsView) {
        val action = AndroidContactActions(this, attributeExtractor)
        when {
            intent.action == ACTION_CALL -> presenter.startPresentingCallsInto(view, action)
            intent.action == ACTION_SENDTO -> presenter.startPresentingMessagingInto(view, action)
            else -> {
                throw IllegalArgumentException("Invalid action to show ${intent.action}")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.stopPresenting()
    }


    companion object {

        fun buildCallIntentFor(context: Context, contact: Contact): Intent {
            return Intent(context, PersonActionsActivity::class.java)
                    .setAction(ACTION_CALL)
                    .putContactExtra(contact)
        }

        fun buildSendIntentFor(context: Context, contact: Contact): Intent {
            return Intent(context, PersonActionsActivity::class.java)
                    .setAction(ACTION_SENDTO)
                    .putContactExtra(contact)
        }

        private fun Intent.putContactExtra(contact: Contact): Intent {
            return putExtra(ContactIntentExtractor.EXTRA_CONTACT_ID, contact.contactID)
                    .putExtra(ContactIntentExtractor.EXTRA_CONTACT_SOURCE, contact.source)
        }
    }
}
