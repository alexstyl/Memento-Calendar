package com.alexstyl.specialdates.person

class ContactActionsPageViewHolder(val adapter: ContactActionsAdapter) : PageViewHolder<ContactActionViewModel> {

    override fun bind(viewModel: List<ContactActionViewModel>) {
        adapter.displayCallMethods(viewModel)
    }
}
