package com.alexstyl.specialdates.upcoming.widget.list

import android.content.res.Resources
import android.support.annotation.Px
import android.widget.RemoteViews
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.upcoming.UpcomingContactEventViewModel


internal class ContactEventBinder(private val remoteViews: RemoteViews,
                                  private val resources: Resources,
                                  private val avatarFactory: CircularAvatarFactory)
    : UpcomingEventViewBinder<UpcomingContactEventViewModel> {

    override fun bind(viewModel: UpcomingContactEventViewModel) {
        remoteViews.setTextViewText(R.id.row_upcoming_event_contact_name, viewModel.contactName)
        remoteViews.setTextViewText(R.id.row_upcoming_event_contact_event, viewModel.eventLabel)
        @Px val targetSize = resources.getDimensionPixelSize(R.dimen.widget_upcoming_avatar_size)
        val avatar = avatarFactory.circularAvatarFor(viewModel.contact, targetSize)
        if (avatar.isPresent) {
            remoteViews.setImageViewBitmap(R.id.row_upcoming_event_contact_image, avatar.get())
        }
    }

    override fun getViews(): RemoteViews = remoteViews
}
