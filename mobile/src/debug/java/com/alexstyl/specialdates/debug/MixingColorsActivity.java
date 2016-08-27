package com.alexstyl.specialdates.debug;

import android.os.Bundle;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ui.base.MementoActivity;
import com.alexstyl.specialdates.widgetprovider.WidgetColorCalculator;
import com.novoda.notils.caster.Views;

public class MixingColorsActivity extends MementoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_activity_mixing_colors);

        TextView text = Views.findById(this, R.id.text);

        WidgetColorCalculator calculator = new WidgetColorCalculator(getResources().getColor(android.R.color.white));
        int color = calculator.getColorForDaysDifference(0);
        text.setTextColor(color);
    }
}
