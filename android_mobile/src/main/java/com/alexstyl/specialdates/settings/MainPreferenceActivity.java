package com.alexstyl.specialdates.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.theming.Themer;
import com.alexstyl.specialdates.ui.activity.MainActivity;
import com.alexstyl.specialdates.ui.base.MementoPreferenceActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.caster.Views;

public class MainPreferenceActivity extends MementoPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Themer.get(this).initialiseActivity(this);
        setContentView(R.layout.activity_settings);

        MementoToolbar toolbar = Views.findById(this, R.id.memento_toolbar);
        setSupportActionBar(toolbar);
        toolbar.displayAsUp();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent up = new Intent(this, MainActivity.class);
                up.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(up);
                finish();
                return true;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
