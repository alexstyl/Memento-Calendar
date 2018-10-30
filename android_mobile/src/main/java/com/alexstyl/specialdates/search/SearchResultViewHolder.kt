package com.alexstyl.specialdates.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.search.SearchResultViewModel.ContactSearchResultViewModel
import com.alexstyl.specialdates.search.SearchResultViewModel.NamedaySearchResultViewModel
import com.alexstyl.specialdates.ui.widget.ColorImageView


sealed class SearchResultViewHolder<in T : SearchResultViewModel>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(viewModel: T, listener: SearchResultClickListener)

    class SearchResultNamedayViewHolder(convertView: View,
                                        private val namedayView: TextView,
                                        private val datesLayout: LinearLayout) : SearchResultViewHolder<NamedaySearchResultViewModel>(convertView) {
        override fun bind(viewModel: NamedaySearchResultViewModel, listener: SearchResultClickListener) {
            namedayView.text = viewModel.nameday
            this.datesLayout.removeAllViews()
            val inflater = LayoutInflater.from(namedayView.context)

            viewModel.namedays.forEach { date ->
                val view = inflater.inflate(R.layout.nameday_date, datesLayout, false)
                val dateView = view.findViewById<TextView>(android.R.id.text1)
                dateView.text = date.dateLabel
                view.setOnClickListener { listener.onNamedayClicked(date.date) }
                datesLayout.addView(dateView)
            }
        }
    }


    class SearchResultContactViewHolder(convertView: View,
                                        private val avatar: ColorImageView,
                                        private val displayName: TextView,
                                        private val imageLoader: ImageLoader)
        : SearchResultViewHolder<ContactSearchResultViewModel>(convertView) {

        override fun bind(viewModel: ContactSearchResultViewModel, listener: SearchResultClickListener) {
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

    class ContactReadPermissionRequestViewHolder(view: View) : SearchResultViewHolder<ContactSearchResultViewModel>(view) {
        override fun bind(viewModel: ContactSearchResultViewModel, listener: SearchResultClickListener) {
            this.itemView.setOnClickListener { listener.onContactReadPermissionClicked() }
        }
    }
}

