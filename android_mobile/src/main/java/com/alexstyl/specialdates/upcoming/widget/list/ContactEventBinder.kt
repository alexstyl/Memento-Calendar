package com.alexstyl.specialdates.upcoming.widget.list

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.support.annotation.Px
import android.widget.RemoteViews
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.upcoming.UpcomingContactEventViewModel


internal class ContactEventBinder(private val remoteViews: RemoteViews,
                                  private val resources: Resources,
                                  private val context: Context,
                                  private val avatarFactory: CircularAvatarFactory)
    : UpcomingEventViewBinder<UpcomingContactEventViewModel> {

    override fun bind(viewModel: UpcomingContactEventViewModel) {
        remoteViews.setTextViewText(R.id.row_upcoming_event_contact_name, viewModel.contactName)
        remoteViews.setTextViewText(R.id.row_upcoming_event_contact_event, viewModel.eventLabel)
        remoteViews.setTextColor(R.id.row_upcoming_event_contact_event, viewModel.eventColor)

        val avatar = createAvatarFor(viewModel.contact)
        remoteViews.setImageViewBitmap(R.id.row_upcoming_event_contact_image, avatar)

        val fillInIntent = WidgetRouterActivity.buildContactIntent(context, viewModel.contact)
        remoteViews.setOnClickFillInIntent(R.id.widgetrow_upcoming_contact_event, fillInIntent)
    }

    private fun createAvatarFor(contact: Contact): Bitmap {
        @Px val targetSize = resources.getDimensionPixelSize(R.dimen.widget_upcoming_avatar_size)
        val avatar = avatarFactory.circularAvatarFor(contact, targetSize)
        return if (avatar.isPresent) {
            avatar.get()
        } else {
            val textSize = resources.getDimensionPixelSize(R.dimen.widget_upcoming_avatar_text_size)
            avatarFactory.createLetterAvatarFor(contact.displayName, targetSize, textSize)
        }
    }

    override fun getViews(): RemoteViews = remoteViews
}
