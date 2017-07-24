package com.alexstyl.specialdates.person

class CallPageViewHolder(val adapter: CallAdapter) : PageViewHolder<ContactCallViewModel> {

    override fun bind(viewModel: List<ContactCallViewModel>) {
        adapter.displayCallMethods(viewModel)
    }
}
