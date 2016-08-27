package com.alexstyl.specialdates.support;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.alexstyl.specialdates.Navigator;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.facebook.FacebookUtils;
import com.alexstyl.specialdates.ui.base.MementoDialog;

/**
 * <p>Created by alexstyl on 05/02/15.</p>
 */
public class SupportDialog extends MementoDialog {

    private static final int DONATE = 0;
    private static final int LIKE_FB = 1;
    private static final int TRANSLATE = 2;
    private static final int RATE = 3;

    private Navigator navigator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = new Navigator(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getThemedContext())
                .setView(onCreateDialogView())
                .setTitle(R.string.thanks_for_support)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private View onCreateDialogView() {
        View view = LayoutInflater.from(getThemedContext()).inflate(R.layout.dialog_support, null, false);
        ListView list = (ListView) view.findViewById(android.R.id.list);
        list.setAdapter(getOptionsAdapter());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == DONATE) {
                    startActivity(new Intent(getActivity(), SupportDonateDialog.class));
                } else if (position == LIKE_FB) {
                    FacebookUtils.openFacebookPage(getActivity());
                } else if (position == TRANSLATE) {
                    startActivity(new Intent(getActivity(), SupportTranslateDialog.class));
                } else if (position == RATE) {
                    navigator.toPlayStore();
                }
                dismiss();
            }
        });
        return view;
    }

    private ListAdapter getOptionsAdapter() {
        String[] options = getResources().getStringArray(R.array.support_options);
        return new ArrayAdapter<>(getActivity(), R.layout.simple_list_item_1, android.R.id.text1, options);
    }

    private DialogInterface.OnClickListener mOptionsListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };
}
