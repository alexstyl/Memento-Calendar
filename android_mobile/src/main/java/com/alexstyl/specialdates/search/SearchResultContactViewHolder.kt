package com.alexstyl.specialdates.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.widget.ColorImageView

class SearchResultContactViewHolder(convertView: View,
                                    private val avatar: ColorImageView,
                                    private val displayName: TextView,
                                    private val imageLoader: ImageLoader) : RecyclerView.ViewHolder(convertView) {

    fun bind(viewModel: ContactSearchResultViewModel, listener: SearchResultClickListener) {
        avatar.setCircleColorVariant(viewModel.backgroundVariant)
        avatar.setLetter(viewModel.displayName, true)

        displayName.text = viewModel.displayName
        imageLoader
                .load(viewModel.contactAvatarURI)
                .asCircle()
                .into(avatar.imageView)
        itemView.setOnClickListener { listener.onContactClicked(viewModel.contact) }
    }

}
