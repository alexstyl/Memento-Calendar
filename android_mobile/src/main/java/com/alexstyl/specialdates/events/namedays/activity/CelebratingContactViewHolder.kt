package com.alexstyl.specialdates.events.namedays.activity

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.images.ImageLoader

class CelebratingContactViewHolder(view: View,
                                   private val imageLoader: ImageLoader,
                                   private val avatarView: ImageView,
                                   private val contactNameView: TextView)
    : NamedayScreenViewHolder<CelebratingContactViewModel>(view) {
    override fun bind(viewModel: CelebratingContactViewModel, onContactClicked: (Contact) -> Unit) {
        val contact = viewModel.contact
        contactNameView.text = contact.displayName.toString()
        imageLoader.load(contact.imagePath)
                .asCircle()
                .into(avatarView)
        itemView.setOnClickListener { onContactClicked(contact) }
    }


}
