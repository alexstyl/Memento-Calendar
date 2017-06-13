
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
    private final List<DataType> objects;
    private final Resources res;
    private final int dataType;
    private final LayoutInflater mInflater;

    @SuppressWarnings("unchecked")
    public SimpleDataAdapter(Context context, List<? extends DataType> object, int dataType) {
        this.mInflater = LayoutInflater.from(context);
        this.dataType = dataType;
        this.objects = (List<DataType>) object;
        this.res = context.getResources();

    }

    @Override
    public int getCount() {
        return objects.size();
    }

    public int getDataType() {
        return dataType;
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
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
            convertView = mInflater.inflate(R.layout.row_datatype, null, false);
            vh.label = (TextView) convertView.findViewById(android.R.id.text1);
            vh.data = (TextView) convertView.findViewById(android.R.id.text2);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        DataType obj = (DataType) getItem(position);
        vh.data.setText(obj.getData());
        vh.label.setText(obj.getDisplayingLabel(res));

        return convertView;
    }
}
