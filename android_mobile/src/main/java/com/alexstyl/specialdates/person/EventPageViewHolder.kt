package com.alexstyl.specialdates.person

class EventPageViewHolder(val adapter: EventAdapter) : PageViewHolder<ContactEventViewModel> {

    override fun bind(viewModel: List<ContactEventViewModel>) {
        adapter.displayEvents(viewModel)
    }
}
