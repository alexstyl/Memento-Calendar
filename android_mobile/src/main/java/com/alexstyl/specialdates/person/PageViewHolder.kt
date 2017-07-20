package com.alexstyl.specialdates.person

internal interface PageViewHolder<in T : PersonDetailItem> {
    fun bind(viewModel: List<T>)
}
