package com.alexstyl.specialdates.addevent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.bottomsheet.ImagePickResolver;
import com.alexstyl.specialdates.addevent.bottomsheet.IntentOptionViewModel;
import com.alexstyl.specialdates.addevent.bottomsheet.ImagePickerOptionsAdapter;
import com.alexstyl.specialdates.addevent.bottomsheet.IntentResolver;
import com.alexstyl.specialdates.ui.base.MementoDialog;
import com.alexstyl.specialdates.ui.widget.SpacesItemDecoration;
import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;

import java.util.List;

final public class BottomSheetPicturesDialog extends MementoDialog {

    private static final String KEY_OUTPUT_URI = "key_output_uri";
    private static final String KEY_INCLUDE_CLEAR = "key_include_clear";

    private OnImageOptionPickedListener parentListener;
    private ImagePickerOptionsAdapter adapter;
    private ImagePickResolver resolver;

    public static BottomSheetPicturesDialog newInstance(Uri outputUri) {
        Bundle args = new Bundle(1);
        args.putString(KEY_OUTPUT_URI, outputUri.toString());
        BottomSheetPicturesDialog fragment = new BottomSheetPicturesDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public BottomSheetPicturesDialog includeClearOption() {
        getArguments().putBoolean(KEY_INCLUDE_CLEAR, true);
        return this;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentListener = Classes.from(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolver = new ImagePickResolver(new IntentResolver(getActivity().getPackageManager()), new ImageIntentFactory());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_pick_image, null, false);

        RecyclerView grid = Views.findById(view, R.id.pick_image_grid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        grid.addItemDecoration(new SpacesItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.add_event_image_option_vertical),
                gridLayoutManager.getSpanCount()
        ));
        grid.setLayoutManager(gridLayoutManager);
        adapter = getIncludeClear()
                ? ImagePickerOptionsAdapter.createWithClear(internalListener)
                : ImagePickerOptionsAdapter.newInstance(internalListener);
        grid.setAdapter(adapter);
        dialog.setContentView(view);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new AsyncTask<Object, Object, List<IntentOptionViewModel>>() {
            @Override
            protected List<IntentOptionViewModel> doInBackground(Object... params) {
                return resolver.createFor(getOutputUri());
            }

            @Override
            protected void onPostExecute(List<IntentOptionViewModel> viewModels) {
                adapter.updateWith(viewModels);
            }
        }.execute();
    }

    private Uri getOutputUri() {
        String rawUri = getArguments().getString(KEY_OUTPUT_URI);
        return Uri.parse(rawUri);
    }

    private boolean getIncludeClear() {
        return getArguments().getBoolean(KEY_INCLUDE_CLEAR, false);
    }

    private final OnImageOptionPickedListener internalListener = new OnImageOptionPickedListener() {
        @Override
        public void onIntentSelected(Intent intent) {
            dismiss();
            parentListener.onIntentSelected(intent);
        }

        @Override
        public void onClearSelected() {
            dismiss();
            parentListener.onClearSelected();
        }
    };

    public interface OnImageOptionPickedListener {
        void onIntentSelected(Intent intent);

        void onClearSelected();
    }
}
