package com.alexstyl.specialdates.person;

import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexstyl.specialdates.R;
import com.novoda.notils.caster.Views;

class ContactItemsAdapter extends PagerAdapter {

    private static final int PAGE_COUNT = 3;
    private static final int PAGE_EVENTS = 0;
    private static final int PAGE_CALLS = 1;
    private static final int PAGE_MESSAGES = 2;

    private final LayoutInflater inflater;
    private final EventPressedListener listener;

    private final SparseArrayCompat<PageViewHolder> viewHolders = new SparseArrayCompat<>(PAGE_COUNT);

    ContactItemsAdapter(LayoutInflater inflater, EventPressedListener listener) {
        this.inflater = inflater;
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.page_person_items, viewGroup, false);
        viewGroup.addView(layout);
        RecyclerView recyclerView = Views.findById(layout, R.id.page_person_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewGroup.getContext(), LinearLayoutManager.VERTICAL, false));
        if (position == PAGE_EVENTS) {
            EventAdapter adapter = new EventAdapter(listener);
            PageViewHolder viewHolder = new EventPageViewHolder(adapter);
            viewHolders.put(position, viewHolder);
            recyclerView.setAdapter(adapter);
        } else if (position == PAGE_CALLS) {
            CallAdapter adapter = new CallAdapter(listener);
            PageViewHolder viewHolder = new CallPageViewHolder(adapter);
            viewHolders.put(position, viewHolder);
            recyclerView.setAdapter(adapter);
        }
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @SuppressWarnings("unchecked")
    void displayEvents(PersonContactViewModel events) {
        viewHolders.get(PAGE_EVENTS).bind(events.getEvents());
        viewHolders.get(PAGE_CALLS).bind(events.getCalls());
//        viewHolders.get(PAGE_MESSAGES).bind(events.getMessages());
    }

}

