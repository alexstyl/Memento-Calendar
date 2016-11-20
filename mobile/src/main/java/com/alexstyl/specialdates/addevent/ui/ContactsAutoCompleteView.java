package com.alexstyl.specialdates.addevent.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.search.ContactsSearch;
import com.alexstyl.specialdates.search.NameMatcher;
import com.novoda.notils.logger.simple.Log;

/**
 * A view that displays
 */
public class ContactsAutoCompleteView extends AutoCompleteTextView {

    private ContactsAdapter adapter;
    private OnContactSelectedListener listener;

    public ContactsAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Context context = getContext();
        adapter = new ContactsAdapter(new ContactsSearch(context, ContactsProvider.get(context), NameMatcher.INSTANCE), ImageLoader.createCircleThumbnailLoader(getResources()));
        setAdapter(adapter);
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onContactSelected(adapter.getItem(position));
            }
        });
    }

    public void setOnContactSelectedListener(OnContactSelectedListener listener) {
        if (listener == null) {
            this.listener = NO_CALLBACKS;
        } else {
            this.listener = listener;
        }
    }

    private final OnContactSelectedListener NO_CALLBACKS = new OnContactSelectedListener() {
        @Override
        public void onContactSelected(Contact contact) {
            Log.w("onContactSelected called with no callbacks");
        }

    };

    public interface OnContactSelectedListener {
        void onContactSelected(Contact contact);
    }
}
