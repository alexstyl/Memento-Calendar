package com.alexstyl.specialdates.addevent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.novoda.notils.caster.Views;

final class PictureSelectAdapter extends BaseAdapter {

    private final PictureSelectOption[] values;

    PictureSelectAdapter(PictureSelectOption[] values) {
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public PictureSelectOption getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return values[position].getIndex();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView textView = Views.findById(convertView, android.R.id.text1);
        PictureSelectOption item = getItem(position);
        textView.setText(item.getLabel());
        return convertView;
    }
}
