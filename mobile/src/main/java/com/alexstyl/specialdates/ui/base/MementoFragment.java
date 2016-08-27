package com.alexstyl.specialdates.ui.base;

import android.app.Activity;
import android.content.ContentResolver;
import android.support.v4.app.Fragment;

public class MementoFragment extends Fragment {

    private MementoActivity mActivity;

    public MementoActivity getMementoActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        if (!(activity instanceof MementoActivity)) {
            throw new IllegalStateException(MementoFragment.class.getSimpleName()
                                                    + " must be attached to a BaseActivity.");
        }
        mActivity = (MementoActivity) activity;

        super.onAttach(activity);
    }

    protected ContentResolver getContentResolver() {
        return getActivity().getContentResolver();
    }

    protected void setActivityTitle(String title) {
        if (getActivity() != null) {
            getActivity().setTitle(title);
        }
    }

}
