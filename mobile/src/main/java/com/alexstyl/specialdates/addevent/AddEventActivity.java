package com.alexstyl.specialdates.addevent;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.alexstyl.android.AndroidDateLabelCreator;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.BottomSheetPicturesDialog.OnImageOptionPickedListener;
import com.alexstyl.specialdates.addevent.EventDatePickerDialogFragment.OnEventDatePickedListener;
import com.alexstyl.specialdates.addevent.ui.AvatarCameraButtonView;
import com.alexstyl.specialdates.addevent.ui.ContactSuggestionView;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.caster.Views;

import java.io.File;
import java.util.List;

public class AddEventActivity extends ThemedActivity implements OnImageOptionPickedListener, OnEventDatePickedListener {

    private static final int CODE_TAKE_PICTURE = 501;
    private static final int CODE_PICK_A_FILE = 502;
    private static final int CODE_CROP_IMAGE = 503;

    private AddEventsPresenter presenter;
    private Uri croppedImageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in_from_below, R.anim.stay);
        setContentView(R.layout.activity_add_event);

        // TODO analytics
        // TODO black and white icons for X
        ImageLoader imageLoader = ImageLoader.createSquareThumbnailLoader(getResources());

        MementoToolbar toolbar = Views.findById(this, R.id.memento_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white);
        AvatarCameraButtonView avatarView = Views.findById(this, R.id.add_event_avatar);
        ContactSuggestionView contactSuggestionView = Views.findById(this, R.id.add_event_contact_autocomplete);
        RecyclerView eventsView = Views.findById(this, R.id.add_event_events);
        eventsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        eventsView.setHasFixedSize(true);
        ContactEventsAdapter adapter = new ContactEventsAdapter(onEventTappedListener);
        eventsView.setAdapter(adapter);

        PeopleEventsProvider peopleEventsProvider = PeopleEventsProvider.newInstance(this);
        ContactEventViewModelFactory factory = new ContactEventViewModelFactory(new AndroidDateLabelCreator(this));
        AndroidStringResources stringResources = new AndroidStringResources(getResources());
        AddEventViewModelFactory newEventFactory = new AddEventViewModelFactory(stringResources);
        ContactEventsFetcher contactEventsFetcher = new ContactEventsFetcher(
                getSupportLoaderManager(),
                this,
                peopleEventsProvider,
                factory,
                newEventFactory
        );

        final WriteableAccountsProvider accountsProvider = WriteableAccountsProvider.from(this);
        ContactEventPersister contactEventPersister = new ContactEventPersister(getContentResolver(), accountsProvider, peopleEventsProvider);
        MessageDisplayer messageDisplayer = new MessageDisplayer(getApplicationContext());
        presenter = new AddEventsPresenter(
                imageLoader,
                contactEventsFetcher,
                adapter,
                contactSuggestionView,
                avatarView,
                toolbar,
                factory,
                new AddEventViewModelFactory(stringResources),
                contactEventPersister,
                messageDisplayer
        );
        presenter.startPresenting(
                new OnCameraClickedListener() {
                    @Override
                    public void onPictureRetakenRequested() {
                        BottomSheetPicturesDialog
                                .newInstance(makeAFileAndKeep())
                                .includeClearOption()
                                .show(getSupportFragmentManager(), "picture_pick");
                    }

                    @Override
                    public void onNewPictureTakenRequested() {
                        BottomSheetPicturesDialog
                                .newInstance(makeAFileAndKeep())
                                .show(getSupportFragmentManager(), "picture_pick");
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            // TODO delete temp file?
            startCropIntent(croppedImageUri);
        } else if (requestCode == CODE_PICK_A_FILE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            startCropIntent(imageUri);
        } else if (requestCode == CODE_CROP_IMAGE && resultCode == RESULT_OK) {
            presenter.presentAvatar(croppedImageUri);
            revokeReadPermissionIfNeeded(croppedImageUri);
        }
    }

    private void startCropIntent(Uri imageUri) {
        Intent intent = ImageCrop.newIntent(imageUri);

        File photoFile = TempImageFile.newInstance(getApplicationContext());
        croppedImageUri = FileProvider.getUriForFile(
                this,
                MementoApplication.PACKAGE + ".fileprovider",
                photoFile
        );
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
        intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, croppedImageUri));
        startActivityForResult(intent, CODE_CROP_IMAGE);
    }

    protected void finishActivitySuccessfully() {
        setResult(RESULT_OK);
        finish();
    }

    protected void cancelActivity() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void finish() {
        overridePendingTransition(0, R.anim.slide_out_from_below);
        super.finish();
    }

    private final OnEventTappedListener onEventTappedListener = new OnEventTappedListener() {
        @Override
        public void onEventTapped(ContactEventViewModel viewModel) {
            final EventType eventType = viewModel.getEventType();
            final Optional<Date> initialDate = viewModel.getDate();
            EventDatePickerDialogFragment dialog = EventDatePickerDialogFragment.newInstance(eventType, initialDate);
            dialog.show(getSupportFragmentManager(), "pick_event");
        }

        @Override
        public void onEventRemoved(EventType eventType) {
            presenter.onEventRemoved(eventType);
        }
    };

    @Override
    public void onDatePicked(EventType eventType, Date date) {
        presenter.onEventDatePicked(eventType, date);
    }

    @Override
    public void onIntentSelected(Intent intent) {
        grandReadPermissionIfNeeded(croppedImageUri, intent);
        startActivityForResult(intent, getRequestCodeFor(intent));
    }

    @Override
    public void onClearSelected() {
        presenter.removeAvatar();
    }

    private static int getRequestCodeFor(Intent intent) {
        String action = intent.getAction();
        if (ImageIntentFactory.ACTION_IMAGE_CAPTURE.equals(action)) {
            return CODE_TAKE_PICTURE;
        } else if (ImageIntentFactory.ACTION_IMAGE_PICK.equals(action)) {
            return CODE_PICK_A_FILE;
        } else {
            throw new IllegalArgumentException("Don't know how to handle " + action);
        }
    }

    private void grandReadPermissionIfNeeded(Uri photoUri, Intent intent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
    }

    private void revokeReadPermissionIfNeeded(Uri photoUri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    private Uri makeAFileAndKeep() {
        File photoFile = TempImageFile.newInstance(getApplicationContext());
        croppedImageUri = FileProvider.getUriForFile(
                this,
                MementoApplication.PACKAGE + ".fileprovider",
                photoFile
        );
        return croppedImageUri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // TODO ask for save
                cancelActivity();
                break;
            case R.id.menu_add_event_save:
                presenter.saveChanges();
                finishActivitySuccessfully();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
