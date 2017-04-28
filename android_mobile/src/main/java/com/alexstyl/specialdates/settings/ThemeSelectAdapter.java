package com.alexstyl.specialdates.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.theming.AttributeExtractor;
import com.alexstyl.specialdates.theming.MementoTheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ThemeSelectAdapter extends BaseAdapter {

    private final List<MementoTheme> themes;
    private final AttributeExtractor extractor;

    public ThemeSelectAdapter() {
        this.themes = Arrays.asList(MementoTheme.values());
        Collections.sort(themes, new MementoThemeComparator());
        extractor = new AttributeExtractor();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ThemeViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_theme_select, parent, false);
            viewHolder = new ThemeViewHolder(convertView, extractor);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ThemeViewHolder) convertView.getTag();
        }
        viewHolder.bind(getItem(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return themes.size();
    }

    @Override
    public MementoTheme getItem(int position) {
        return themes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class MementoThemeComparator implements Comparator<MementoTheme> {
        @Override
        public int compare(MementoTheme lhs, MementoTheme rhs) {
            return lhs.getThemeName().compareTo(rhs.getThemeName());
        }
    }
}
