package com.alexstyl.specialdates.upcoming

import android.view.View
import android.widget.TextView
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.widget.ColorImageView
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener

internal class ContactEventViewHolder(view: View,
                                      private val avatarView: ColorImageView,
                                      private val contactName: TextView,
                                      private val eventLabel: TextView,
                                      private val imageLoader: ImageLoader)
    : UpcomingRowViewHolder<UpcomingContactEventViewModel>(view) {

    override fun bind(viewModel: UpcomingContactEventViewModel, listener: OnUpcomingEventClickedListener) {
        avatarView.setCircleColorVariant(viewModel.backgroundVariant)
        avatarView.setLetter(viewModel.contactName)

        contactName.text = viewModel.contactName
        eventLabel.text = viewModel.eventLabel
        eventLabel.setTextColor(viewModel.eventColor)
        imageLoader.load(viewModel.contactImagePath)
                .asCircle()
                .into(avatarView.imageView)
        itemView.setOnClickListener { listener.onContactClicked(viewModel.contact) }
    }
}
