package com.alexstyl.specialdates.addevent.bottomsheet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.alexstyl.specialdates.addevent.FilePathProvider;
import com.alexstyl.specialdates.addevent.ImageIntentFactory;
import com.alexstyl.specialdates.ui.base.MementoDialog;
import com.alexstyl.specialdates.ui.widget.SpacesItemDecoration;
import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;

import java.util.List;

final public class BottomSheetPicturesDialog extends MementoDialog {

    private static final String KEY_INCLUDE_CLEAR = "key_include_clear";

    private Listener parentListener;
    private ImagePickerOptionsAdapter adapter;
    private ImagePickResolver resolver;

    public static BottomSheetPicturesDialog newInstance() {
        return new BottomSheetPicturesDialog();
    }

    public static BottomSheetPicturesDialog withClearOption() {
        Bundle args = new Bundle(1);
        args.putBoolean(KEY_INCLUDE_CLEAR, true);
        BottomSheetPicturesDialog fragment = new BottomSheetPicturesDialog();
        fragment.setArguments(args);
        return fragment;
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
        Resources resources = getResources();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, resources.getInteger(R.integer.bottom_sheet_span_count));
        grid.addItemDecoration(new SpacesItemDecoration(
                resources.getDimensionPixelSize(R.dimen.add_event_image_option_vertical),
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
        final FilePathProvider filePathProvider = new FilePathProvider(getActivity());
        new AsyncTask<Object, Object, List<IntentOptionViewModel>>() {
            @Override
            protected List<IntentOptionViewModel> doInBackground(Object... params) {
                Uri outputFileUri = filePathProvider.createTemporaryCacheFile();
                return resolver.createFor(outputFileUri);
            }

            @Override
            protected void onPostExecute(List<IntentOptionViewModel> viewModels) {
                adapter.updateWith(viewModels);
            }
        }.execute();
    }

    private boolean getIncludeClear() {
        return getArguments() != null && getArguments().getBoolean(KEY_INCLUDE_CLEAR, false);
    }

    private final Listener internalListener = new Listener() {
        @Override
        public void onActivitySelected(Intent intent) {
            dismiss();
            parentListener.onActivitySelected(intent);
        }

        @Override
        public void clearSelectedAvatar() {
            dismiss();
            parentListener.clearSelectedAvatar();
        }
    };

    public static Uri getImageCaptureResultUri(FilePathProvider filePathProvider, Intent data) {
        Uri uri = data.getData();
        if (uri == null) {
            return filePathProvider.createTemporaryCacheFile();
        }
        return uri;
    }

    public static Uri getImageResultUri(Intent data) {
        return data.getData();
    }

    public interface Listener {
        void onActivitySelected(Intent intent);

        void clearSelectedAvatar();
    }
}
