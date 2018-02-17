package com.alexstyl.specialdates.person;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexstyl.specialdates.R;
import com.novoda.notils.caster.Views;

import java.util.Collections;
import java.util.List;

class ContactItemsAdapter extends PagerAdapter {

    private static final int PAGE_COUNT = 3;
    private static final int PAGE_EVENTS = 0;
    private static final int PAGE_CALLS = 1;
    private static final int PAGE_MESSAGES = 2;

    private final LayoutInflater inflater;
    private final EventPressedListener listener;

    private PersonAvailableActionsViewModel viewModel = new PersonAvailableActionsViewModel(Collections.<ContactEventViewModel>emptyList(),
            Collections.<ContactActionViewModel>emptyList(), Collections.<ContactActionViewModel>emptyList());

    ContactItemsAdapter(LayoutInflater inflater, EventPressedListener listener) {
        this.inflater = inflater;
        this.listener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.page_person_items, viewGroup, false);
        viewGroup.addView(layout);
        RecyclerView recyclerView = Views.findById(layout, R.id.page_person_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewGroup.getContext(), LinearLayoutManager.VERTICAL, false));
        if (viewModel == null) {
            return layout;
        }
        if (position == PAGE_EVENTS) {
            EventAdapter adapter = new EventAdapter();
            PageViewHolder viewHolder = new EventPageViewHolder(adapter);
            recyclerView.setAdapter(adapter);
            viewHolder.bind(viewModel.getEvents());
        } else if (position == PAGE_CALLS) {
            ContactActionsAdapter adapter = new ContactActionsAdapter(listener);
            PageViewHolder viewHolder = new ContactActionsPageViewHolder(adapter);
            recyclerView.setAdapter(adapter);
            viewHolder.bind(viewModel.getCalls());
        } else if (position == PAGE_MESSAGES) {
            ContactActionsAdapter adapter = new ContactActionsAdapter(listener);
            PageViewHolder viewHolder = new ContactActionsPageViewHolder(adapter);
            recyclerView.setAdapter(adapter);
            viewHolder.bind(viewModel.getMessages());
        }
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return pageCountOf(viewModel);
    }

    private int pageCountOf(PersonAvailableActionsViewModel viewModel) {
        return count(viewModel.getCalls()) + count(viewModel.getMessages()) + count(viewModel.getEvents());
    }

    private int count(List<?> item) {
        return item.isEmpty() ? 0 : 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    void displayEvents(PersonAvailableActionsViewModel viewModel) {
        this.viewModel = viewModel;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

