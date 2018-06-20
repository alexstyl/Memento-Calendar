package com.alexstyl.specialdates.upcoming.widget.today;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.alexstyl.specialdates.R;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;

public class UpcomingWidgetConfigurationPanel extends ConstraintLayout {

    private final SeekBar seekbar;
    private final PercentToValueConverter valueConverter = new PercentToValueConverter();
    private ConfigurationListener listener = ConfigurationListener.NO_OP;

    private final SwitchCompat darkThemeSwitch;

    public UpcomingWidgetConfigurationPanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.merge_upcoming_widget_configure_panel, this, true);

        seekbar = Views.findById(this, R.id.upcoming_widget_opacity);
        seekbar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float percentage = valueConverter.progressToPercent(progress);
                setOpacityLevel(percentage);
                listener.onOpacityLevelChanged(percentage);
            }
        });

        darkThemeSwitch = Views.findById(this, R.id.upcoming_widget_dark_theme);
        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WidgetVariant selectedVariant = (isChecked ? WidgetVariant.DARK : WidgetVariant.LIGHT);
                setWidgetVariant(selectedVariant);
                listener.onWidgetVariantSelected(selectedVariant);
            }
        });

        Button applyButton = Views.findById(this, R.id.upcoming_widget_apply);
        applyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onApplyButtonPressed();
            }
        });
    }

    public void setOpacityLevel(float percentage) {
        int progress = valueConverter.percentToProgress(percentage);
        seekbar.setProgress(progress);
    }

    public float getOpacityLevel() {
        int opacityProgress = seekbar.getProgress();
        return valueConverter.progressToPercent(opacityProgress);
    }

    public WidgetVariant getWidgetVariant() {
        return darkThemeSwitch.isChecked() ? WidgetVariant.DARK : WidgetVariant.LIGHT;
    }

    public void setWidgetVariant(WidgetVariant variant) {
        darkThemeSwitch.setChecked(variant == WidgetVariant.DARK);
    }

    public UserOptions getUserOptions() {
        float opacityLevel = getOpacityLevel();
        WidgetVariant widgetVariant = getWidgetVariant();
        return new UserOptions(opacityLevel, widgetVariant);
    }

    public void setListener(ConfigurationListener listener) {
        this.listener = listener;
    }

    interface ConfigurationListener {

        ConfigurationListener NO_OP = new ConfigurationListener() {
            @Override
            public void onOpacityLevelChanged(float percentage) {
                Log.w("onOpacityLevelChanged with no listener set");

            }

            @Override
            public void onWidgetVariantSelected(WidgetVariant variant) {
                Log.w("onWidgetVariantSelected with no listener set");
            }

            @Override
            public void onApplyButtonPressed() {
                Log.w("onApplyButtonPressed with no listener set");
            }
        };

        void onOpacityLevelChanged(float percentage);

        void onWidgetVariantSelected(WidgetVariant variant);

        void onApplyButtonPressed();
    }
}
