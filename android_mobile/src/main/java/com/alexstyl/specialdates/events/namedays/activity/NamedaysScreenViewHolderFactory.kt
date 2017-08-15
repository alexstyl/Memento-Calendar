package com.alexstyl.specialdates.events.namedays.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.widget.ColorImageView

class NamedaysScreenViewHolderFactory(private val layoutInflater: LayoutInflater,
                                      private val imageLoader: ImageLoader) {

    fun viewHolderFor(parent: ViewGroup?, @NamedayScreenViewType viewType: Int): NamedayScreenViewHolder<*> {
        when (viewType) {
            NamedayScreenViewType.NAMEDAY -> {
                val view = layoutInflater.inflate(R.layout.row_nameday_name, parent, false)
                val nameView = view.findViewById(R.id.nameday_name) as TextView
                return NameViewHolder(view, nameView)
            }
            NamedayScreenViewType.CONTACT -> {
                val view = layoutInflater.inflate(R.layout.row_nameday_contact, parent, false)
                val nameView = view.findViewById(R.id.row_nameday_contact_name) as TextView
                val avatarView = view.findViewById(R.id.row_nameday_contact_avatar) as ColorImageView
                return CelebratingContactViewHolder(view, imageLoader, avatarView, nameView)
            }
            else -> throw UnsupportedOperationException("Unsupported view type " + viewType)
        }
    }
}





