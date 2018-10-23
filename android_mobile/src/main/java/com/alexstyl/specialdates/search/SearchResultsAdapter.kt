package com.alexstyl.specialdates.search

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.widget.ColorImageView

class SearchResultsAdapter(private val imageLoader: ImageLoader,
                           private val labelCreator: DateLabelCreator,
                           private val listener: SearchResultClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val searchResults = mutableListOf<SearchResultViewModel>()

    internal fun displaySearchResults(searchResults: List<SearchResultViewModel>) {
        val diffCallback = SearchResultDiffCallback(this.searchResults, searchResults)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.searchResults.clear()
        this.searchResults.addAll(searchResults)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        val viewModel = searchResults[position]
        return when (viewModel) {
            is NamedaySearchResultViewModel -> VIEWTYPE_NAMEDAY
            is ContactSearchResultViewModel -> VIEWTYPE_CONTACT
            is ContactReadPermissionRequestViewModel -> VIEWTYPE_PERMISSION
            else -> throw IllegalArgumentException("There is no assigned view type for ${viewModel.javaClass.simpleName}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                VIEWTYPE_CONTACT -> contactViewHolder(parent)
                VIEWTYPE_NAMEDAY -> namedayViewHolder(parent)
                VIEWTYPE_PERMISSION -> permissionRequestViewHolder(parent)
                else -> throw IllegalStateException("There is no assigned view type for view type $viewType")
            }

    private fun contactViewHolder(parent: ViewGroup): SearchResultContactViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_search_result_contact_event, parent, false)
        val nameView = view.findViewById(R.id.search_result_contact_name) as TextView
        val avatarView = view.findViewById(R.id.search_result_avatar) as ColorImageView
        return SearchResultContactViewHolder(view, avatarView, nameView, imageLoader)
    }

    private fun namedayViewHolder(parent: ViewGroup): SearchResultNamedayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_search_result_nameday, parent, false)
        val namedayView = view.findViewById(R.id.name_celebrating) as TextView
        val datesLayout = view.findViewById(R.id.dates) as LinearLayout
        return SearchResultNamedayViewHolder(view, namedayView, datesLayout)
    }

    private fun permissionRequestViewHolder(parent: ViewGroup): ContactReadPermissionRequestViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_search_result_permission_needed, parent, false)
        return ContactReadPermissionRequestViewHolder(view)
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        val viewModel = searchResults[position]

        // TODO just bind nicely
        when (type) {
            VIEWTYPE_CONTACT -> {
                val viewHolder = vh as SearchResultContactViewHolder
                viewHolder.bind(viewModel as ContactSearchResultViewModel, listener)
            }
            VIEWTYPE_NAMEDAY -> (vh as SearchResultNamedayViewHolder).bind(viewModel as NamedaySearchResultViewModel, listener)
            VIEWTYPE_PERMISSION -> (vh as ContactReadPermissionRequestViewHolder).bind(listener)
            else -> throw IllegalStateException("Unhandled type $type")
        }

    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    companion object {
        private const val VIEWTYPE_CONTACT = 0
        private const val VIEWTYPE_NAMEDAY = 1
        private const val VIEWTYPE_PERMISSION = 2
    }

}
