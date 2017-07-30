package com.alexstyl.specialdates.addevent.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;
import java.util.List;

class ContactsAdapter extends BaseAdapter implements Filterable {

    private final ImageLoader imageLoader;
    private final ArrayList<Contact> contacts = new ArrayList<>();

    private final Filter filter;

    ContactsAdapter(ContactsSearch contactsSearch, ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        this.filter = new DeviceContactsFilter(contactsSearch) {

            @Override
            protected void onContactsFiltered(List<Contact> contacts) {
                setSuggestions(contacts);
            }
        };
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ContactViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact, parent, false);
            vh = new ContactViewHolder();
            vh.contactName = Views.findById(view, R.id.display_name);
            vh.avatar = Views.findById(view, R.id.search_result_avatar);
            view.setTag(vh);
        } else {
            vh = (ContactViewHolder) view.getTag();
        }

        Contact contact = getItem(position);
        String displayName = contact.toString();
        vh.contactName.setText(displayName);
        vh.avatar.setCircleColorVariant((int) contact.getContactID());
        vh.avatar.setLetter(displayName);

        imageLoader
                .load(contact.getImagePath())
                .into(vh.avatar.getImageView());

        return view;
    }

    private static class ContactViewHolder {
        private ColorImageView avatar;
        private TextView contactName;
    }

    private void setSuggestions(List<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);

        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
}
