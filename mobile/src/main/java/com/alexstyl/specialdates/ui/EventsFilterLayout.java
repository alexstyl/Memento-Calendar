package com.alexstyl.specialdates.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import java.util.HashMap;
import java.util.Map;

public class EventsFilterLayout extends TabLayout {

    private OnSectionPressedListener listener;

    private Map<Section, Boolean> categoryStates;

    public EventsFilterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialiseTabs();

        addOnTabSelectedListener(new SimpleTabSelectedListener() {

            @Override
            public void onTabSelected(Tab tab) {
                int position = tab.getPosition();
                Section sectionPressed = Section.ofId(position);
                Boolean newState = updateStateOf(sectionPressed);

                int updatedIcon = getIconFor(sectionPressed, newState);
                tab.setIcon(updatedIcon);
                listener.onSectionPressed(sectionPressed, newState);
            }

            @DrawableRes
            private int getIconFor(Section section, boolean enabled) {
                if (enabled) {
                    return section.getEnabledResId();
                } else {
                    return section.getDisabledResId();
                }
            }

            private Boolean updateStateOf(Section sectionPressed) {
                Boolean oldState = categoryStates.get(sectionPressed);
                Boolean newState = !oldState;
                categoryStates.put(sectionPressed, newState);
                return newState;
            }

            @Override
            public void onTabReselected(Tab tab) {
                onTabSelected(tab);
            }
        });
    }

    private void initialiseTabs() {
        categoryStates = new HashMap<>(Section.values().length);
        for (Section section : Section.values()) {
            Tab contactsTab = newTab();
            contactsTab.setIcon(section.getEnabledResId());
            addTab(contactsTab);
        }

        for (Section section : Section.values()) {
            categoryStates.put(section, true);
        }
    }

    public interface OnSectionPressedListener {
        void onSectionPressed(Section section, boolean enabled);
    }

    public void setOnSectionPressedListener(OnSectionPressedListener l) {
        this.listener = l;
    }
}
