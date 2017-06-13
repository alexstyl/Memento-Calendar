package com.alexstyl.specialdates.upcoming.widget.today;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.alexstyl.specialdates.R;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;

public class UpcomingWidgetConfigurationPanel extends LinearLayout {

    private final SeekBar seekbar;
    private final PercentToValueConverter valueConverter = new PercentToValueConverter();
    private ConfigurationListener listener = ConfigurationListener.NO_OP;

    private final CheckBox darkThemeCheckbox;

    public UpcomingWidgetConfigurationPanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.merge_upcoming_widget_configure_panel, this, true);

        super.setOrientation(VERTICAL);
        super.setGravity(Gravity.CENTER_HORIZONTAL);

        seekbar = Views.findById(this, R.id.upcoming_widget_opacity);
        seekbar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float percentage = valueConverter.progressToPercent(progress);
                setOpacityLevel(percentage);
                listener.onOpacityLevelChanged(percentage);
            }
        });

        darkThemeCheckbox = Views.findById(this, R.id.upcoming_widget_dark_theme);
        darkThemeCheckbox.setOnCheckedChangeListener(onDarkThemeSelectedListener);
    }

    @Override
    public void setOrientation(int orientation) {
        Log.w("Cannot change the orientation of [%s]", UpcomingWidgetConfigurationPanel.class.getSimpleName());
    }

    private final CompoundButton.OnCheckedChangeListener onDarkThemeSelectedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            WidgetVariant selectedVariant = (isChecked ? WidgetVariant.DARK : WidgetVariant.LIGHT);
            setWidgetVariant(selectedVariant);
            listener.onWidgetVariantSelected(selectedVariant);
        }
    };

    public void setOpacityLevel(float percentage) {
        int progress = valueConverter.percentToProgress(percentage);
        seekbar.setProgress(progress);
    }

    public float getOpacityLevel() {
        int opacityProgress = seekbar.getProgress();
        return valueConverter.progressToPercent(opacityProgress);
    }

    public WidgetVariant getWidgetVariant() {
        return darkThemeCheckbox.isChecked() ? WidgetVariant.DARK : WidgetVariant.LIGHT;
    }

    public void setWidgetVariant(WidgetVariant variant) {
        darkThemeCheckbox.setChecked(variant == WidgetVariant.DARK);
    }

    public UserOptions getUserOptions() {
        float opacityLevel = getOpacityLevel();
        WidgetVariant widgetVariant = getWidgetVariant();
        return new UserOptions(opacityLevel, widgetVariant);
    }

    public void setListener(ConfigurationListener listener) {
        this.listener = listener;
    }

    class UserOptions {
        private final float opacityLevel;
        private final WidgetVariant widgetVariant;

        public UserOptions(float opacityLevel, WidgetVariant widgetVariant) {
            this.opacityLevel = opacityLevel;
            this.widgetVariant = widgetVariant;
        }

        public float getOpacityLevel() {
            return opacityLevel;
        }

        public WidgetVariant getWidgetVariant() {
            return widgetVariant;
        }
    }

    public interface ConfigurationListener {

        ConfigurationListener NO_OP = new ConfigurationListener() {
            @Override
            public void onOpacityLevelChanged(float percentage) {
                Log.w("onOpacityLevelChanged with no listener set");

            }

            @Override
            public void onWidgetVariantSelected(WidgetVariant variant) {
                Log.w("onWidgetVariantSelected with no listener set");
            }
        };

        void onOpacityLevelChanged(float percentage);

        void onWidgetVariantSelected(WidgetVariant variant);
    }
}
