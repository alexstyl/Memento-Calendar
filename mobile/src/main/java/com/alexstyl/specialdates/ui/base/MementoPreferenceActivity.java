package com.alexstyl.specialdates.ui.base;

import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.List;

public class MementoPreferenceActivity extends ThemedMementoActivity {

    @Override
    protected boolean shouldUseHomeAsUp() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment frag : fragments) {
                frag.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
