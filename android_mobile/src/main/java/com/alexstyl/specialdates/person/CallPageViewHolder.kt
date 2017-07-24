package com.alexstyl.specialdates.person

class CallPageViewHolder(val adapter: CallAdapter) : PageViewHolder<ContactActionViewModel> {

    override fun bind(viewModel: List<ContactActionViewModel>) {
        adapter.displayCallMethods(viewModel)
    }
}
