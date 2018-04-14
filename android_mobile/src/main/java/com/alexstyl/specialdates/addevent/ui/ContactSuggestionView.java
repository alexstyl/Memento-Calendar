package com.alexstyl.specialdates.addevent.ui;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ContactsSearch;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.search.NameMatcher;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;
import com.novoda.notils.meta.AndroidUtils;

import javax.inject.Inject;

public class ContactSuggestionView extends LinearLayout {

    private OnContactSelectedListener listener = OnContactSelectedListener.NO_CALLBACKS;
    private AutoCompleteTextView autoCompleteView;
    @Inject ImageLoader imageLoader;
    @Inject ContactsProvider contactsProvider;

    public ContactSuggestionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            AppComponent applicationModule = ((MementoApplication) context.getApplicationContext()).getApplicationModule();
            applicationModule.inject(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        super.setOrientation(HORIZONTAL);
        inflate(getContext(), R.layout.merge_contact_suggestion_view, this);
        autoCompleteView = Views.findById(this, R.id.contact_suggestion_autocomplete);

        if (isInEditMode()) {
            return;
        }
        ContactsSearch contactsSearch = new ContactsSearch(contactsProvider, NameMatcher.INSTANCE);
        final ContactsAdapter adapter = new ContactsAdapter(contactsSearch, imageLoader);
        autoCompleteView.setAdapter(adapter);
        autoCompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onContactSelected(adapter.getItem(position));
                AndroidUtils.requestHideKeyboard(view.getContext(), view);
            }
        });
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        autoCompleteView.addTextChangedListener(textWatcher);
    }

    public void setOnContactSelectedListener(OnContactSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnContactSelectedListener {
        void onContactSelected(Contact contact);

        OnContactSelectedListener NO_CALLBACKS = new OnContactSelectedListener() {
            @Override
            public void onContactSelected(Contact contact) {
                Log.w("onContactSelected called with no callbacks");
            }

        };
    }
}
