package com.alexstyl.specialdates.upcoming.widget.today;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateLabelCreator;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.alexstyl.specialdates.upcoming.widget.today.UpcomingWidgetConfigurationPanel.ConfigurationListener;
import com.novoda.notils.caster.Views;

import javax.inject.Inject;

public class UpcomingWidgetConfigureActivity extends ThemedMementoActivity {

    private ImageView backgroundView;
    private UpcomingWidgetPreviewLayout previewLayout;
    private UpcomingWidgetConfigurationPanel configurationPanel;
    private UpcomingWidgetPreferences preferences;
    @Inject DateLabelCreator labelCreator;

    private int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);

        setContentView(R.layout.activity_upcoming_events_widget_configure);

        MementoToolbar mementoToolbar = Views.findById(this, R.id.memento_toolbar);
        setSupportActionBar(mementoToolbar);

        backgroundView = Views.findById(this, R.id.upcoming_widget_wallpaper);
        previewLayout = Views.findById(this, R.id.upcoming_widget_preview);
        configurationPanel = Views.findById(this, R.id.upcoming_widget_configure_panel);
        configurationPanel.setListener(configurationListener);

        preferences = new UpcomingWidgetPreferences(this);
        initialisePreview(preferences);
        considerAsNotComplete();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            );
        }
    }

    private void considerAsNotComplete() {
        setResult(RESULT_CANCELED);
    }

    private void initialisePreview(UpcomingWidgetPreferences preferences) {
        float startingOpacity = preferences.getOppacityLevel();
        WidgetVariant startingVariant = preferences.getSelectedVariant();
        configurationPanel.setOpacityLevel(startingOpacity);
        configurationPanel.setWidgetVariant(startingVariant);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String title = labelCreator.createWithYearPreferred(Date.Companion.today());
        previewLayout.setTitle(title);
        previewLayout.setSubtitle(R.string.upcoming_widget_configure_subtitle);

        WidgetVariant variant = preferences.getSelectedVariant();
        previewLayout.previewWidgetVariant(variant);

        float oppacityLevel = preferences.getOppacityLevel();
        previewLayout.previewBackgroundOpacityLevel(oppacityLevel);

    }

    @Override
    protected void onResume() {
        super.onResume();
        displayCurrentWallpaper();
    }

    private void displayCurrentWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        backgroundView.setImageDrawable(wallpaperDrawable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_upcoming_widget_configure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            saveConfigurations();
            finishAsSuccess();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveConfigurations() {
        UserOptions userOptions = configurationPanel.getUserOptions();
        preferences.storeUserOptions(userOptions);
        new TodayUpcomingEventsView(this, AppWidgetManager.getInstance(this)).reloadUpcomingEventsView();
    }

    private void finishAsSuccess() {
        setResult(RESULT_OK, new Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId));
        finish();
    }

    private final ConfigurationListener configurationListener = new ConfigurationListener() {
        @Override
        public void onOpacityLevelChanged(float percentage) {
            previewLayout.previewBackgroundOpacityLevel(percentage);
        }

        @Override
        public void onWidgetVariantSelected(WidgetVariant variant) {
            previewLayout.previewWidgetVariant(variant);
        }
    };
}
