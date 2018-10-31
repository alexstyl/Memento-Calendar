package com.alexstyl.specialdates.search

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.search.SearchResultViewHolder.ContactReadPermissionRequestViewHolder
import com.alexstyl.specialdates.search.SearchResultViewHolder.SearchResultContactViewHolder
import com.alexstyl.specialdates.search.SearchResultViewHolder.SearchResultNamedayViewHolder
import com.alexstyl.specialdates.search.SearchResultViewModel.ContactReadPermissionRequestViewModel
import com.alexstyl.specialdates.search.SearchResultViewModel.ContactSearchResultViewModel
import com.alexstyl.specialdates.search.SearchResultViewModel.NamedaySearchResultViewModel
import com.alexstyl.specialdates.ui.widget.ColorImageView
import java.lang.IllegalStateException

class SearchResultsAdapter(private val imageLoader: ImageLoader,
                           private val listener: SearchResultClickListener)
    : RecyclerView.Adapter<SearchResultViewHolder<SearchResultViewModel>>() {

    private val searchResults = mutableListOf<SearchResultViewModel>()

    override fun getItemViewType(position: Int): Int {
        val viewModel = searchResults[position]
        return when (viewModel) {
            is NamedaySearchResultViewModel -> VIEWTYPE_NAMEDAY
            is ContactSearchResultViewModel -> VIEWTYPE_CONTACT
            is ContactReadPermissionRequestViewModel -> VIEWTYPE_PERMISSION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder<SearchResultViewModel> = when (viewType) {
        VIEWTYPE_CONTACT -> contactViewHolder(parent)
        VIEWTYPE_NAMEDAY -> namedayViewHolder(parent)
        VIEWTYPE_PERMISSION -> permissionRequestViewHolder(parent)
        else -> {
            throw IllegalStateException("Cannot create ViewHolder for viewType [$parent]")
        }
    }

    private fun contactViewHolder(parent: ViewGroup): SearchResultViewHolder<SearchResultViewModel> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_search_result_contact_event, parent, false)
        val nameView = view.findViewById(R.id.search_result_contact_name) as TextView
        val avatarView = view.findViewById(R.id.search_result_avatar) as ColorImageView
        return SearchResultContactViewHolder(view, avatarView, nameView, imageLoader)
                as SearchResultViewHolder<SearchResultViewModel> // <- TODO how to get rid of this?
    }

    private fun namedayViewHolder(parent: ViewGroup): SearchResultViewHolder<SearchResultViewModel> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_search_result_nameday, parent, false)
        val namedayView = view.findViewById(R.id.name_celebrating) as TextView
        val datesLayout = view.findViewById(R.id.dates) as LinearLayout
        return SearchResultNamedayViewHolder(view, namedayView, datesLayout)
                as SearchResultViewHolder<SearchResultViewModel>// <- TODO how to get rid of this?
    }

    private fun permissionRequestViewHolder(parent: ViewGroup): SearchResultViewHolder<SearchResultViewModel> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_search_result_permission_needed, parent, false)
        return ContactReadPermissionRequestViewHolder(view)
                as SearchResultViewHolder<SearchResultViewModel> // <- TODO how to get rid of this?
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder<SearchResultViewModel>, position: Int) {
        val viewModel = searchResults[position]
        holder.bind(viewModel, listener)
    }

    override fun getItemCount() = searchResults.size

    fun displaySearchResults(searchResults: List<SearchResultViewModel>) {
        val diffCallback = SearchResultDiffCallback(this.searchResults, searchResults)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.searchResults.clear()
        this.searchResults.addAll(searchResults)
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        private const val VIEWTYPE_CONTACT = 0
        private const val VIEWTYPE_NAMEDAY = 1
        private const val VIEWTYPE_PERMISSION = 2
    }

}
