
package com.alexstyl.specialdates.datedetails;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.entity.DataType;

import java.util.List;

public class SimpleDataAdapter extends BaseAdapter {
    private final List<DataType> dataTypes;
    private final Resources resources;
    private final LayoutInflater mInflater;

    public SimpleDataAdapter(Context context, List<DataType> dataTypes) {
        this.mInflater = LayoutInflater.from(context);
        this.dataTypes = dataTypes;
        this.resources = context.getResources();

    }

    @Override
    public int getCount() {
        return dataTypes.size();
    }

    @Override
    public DataType getItem(int position) {
        return dataTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView data;
        TextView label;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = mInflater.inflate(R.layout.row_datatype, parent, false);
            vh.label = (TextView) convertView.findViewById(android.R.id.text1);
            vh.data = (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        DataType dataType = getItem(position);
        vh.data.setText(dataType.getData());
        vh.label.setText(dataType.getDisplayingLabel(resources));
        return convertView;
    }
}
