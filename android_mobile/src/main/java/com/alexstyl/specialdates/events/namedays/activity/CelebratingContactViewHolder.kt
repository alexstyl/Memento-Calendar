package com.alexstyl.specialdates.events.namedays.activity

import android.view.View
import android.widget.TextView
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.widget.ColorImageView

class CelebratingContactViewHolder(view: View,
                                   private val imageLoader: ImageLoader,
                                   private val avatarView: ColorImageView,
                                   private val contactNameView: TextView)
    : NamedayScreenViewHolder<CelebratingContactViewModel>(view) {
    override fun bind(viewModel: CelebratingContactViewModel, onContactClicked: (Contact) -> Unit) {
        val contact = viewModel.contact
        contactNameView.text = contact.displayName.toString()
        avatarView.setLetter(viewModel.letter)
        avatarView.setCircleColorVariant(viewModel.colorVariant)
        imageLoader.load(contact.imagePath)
                .asCircle()
                .into(avatarView.imageView)
        itemView.setOnClickListener { onContactClicked(contact) }
    }


}
