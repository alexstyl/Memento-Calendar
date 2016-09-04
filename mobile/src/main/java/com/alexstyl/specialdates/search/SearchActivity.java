package com.alexstyl.specialdates.search;

import android.os.Bundle;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.ui.base.ThemedActivity;

public class SearchActivity extends ThemedActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Analytics.get(this).trackScreen(Screen.SEARCH);
    }

}
