package com.alexstyl.specialdates.search

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.images.ImageLoader

class SearchResultsAdapter(private val imageLoader: ImageLoader,
                           private val labelCreator: DateLabelCreator,
                           private val listener: SearchResultClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val searchResults = mutableListOf<SearchResultViewModel>()

    internal fun displaySearchResults(searchResults: List<SearchResultViewModel>) {
        this.searchResults.clear()
        this.searchResults.addAll(searchResults)
        notifyDataSetChanged() // TODO Diff
    }

    override fun getItemViewType(position: Int): Int {
        val viewModel = searchResults[position]
        return when (viewModel) {
            is NamedaySearchResultViewModel -> VIEWTYPE_NAMEDAY
            is ContactSearchResultViewModel -> VIEWTYPE_CONTACT
            else -> throw IllegalArgumentException("There is no assigned view type for ${viewModel.javaClass.simpleName}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                VIEWTYPE_CONTACT -> SearchResultContactViewHolder.createFor(parent, imageLoader)
                VIEWTYPE_NAMEDAY -> SearchResultNamedayViewHolder.createFor(parent, labelCreator)
                else -> throw IllegalStateException("There is no assigned view type for view type ${viewType}")
            }!!

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        val viewModel = searchResults[position]

        // TODO just bind nicey
        when (type) {
            VIEWTYPE_CONTACT -> {
                val viewHolder = vh as SearchResultContactViewHolder
                viewHolder.bind(viewModel as ContactSearchResultViewModel, listener)
            }
            VIEWTYPE_NAMEDAY -> (vh as SearchResultNamedayViewHolder).bind(viewModel as NamedaySearchResultViewModel, listener)
            else -> throw IllegalStateException("Unhandled type $type")
        }

    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    companion object {
        private const val VIEWTYPE_CONTACT = 0
        private const val VIEWTYPE_NAMEDAY = 1
    }

}
