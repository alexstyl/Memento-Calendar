package com.alexstyl.specialdates.addevent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.alexstyl.android.AndroidDateLabelCreator;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.EventDatePickerDialogFragment.OnEventDatePickedListener;
import com.alexstyl.specialdates.addevent.bottomsheet.BottomSheetPicturesDialog;
import com.alexstyl.specialdates.addevent.bottomsheet.BottomSheetPicturesDialog.Listener;
import com.alexstyl.specialdates.addevent.ui.AvatarPickerView;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.images.ImageDecoder;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.caster.Views;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddEventActivity extends ThemedActivity implements Listener, OnEventDatePickedListener, DiscardPromptDialog.Listener {

    private static final int CODE_TAKE_PICTURE = 404;
    private static final int CODE_PICK_A_FILE = 405;
    private static final int CODE_CROP_IMAGE = CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
    private static final int CODE_PERMISSION_EXTERNAL_STORAGE = 406;

    private AddContactEventsPresenter presenter;
    private PermissionChecker permissionChecker;

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
        AvatarPickerView avatarView = Views.findById(this, R.id.add_event_avatar);
        RecyclerView eventsView = Views.findById(this, R.id.add_event_events);
        eventsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        eventsView.setHasFixedSize(true);
        ContactDetailsAdapter adapter = new ContactDetailsAdapter(contactDetailsListener);
        eventsView.setAdapter(adapter);

        PeopleEventsProvider peopleEventsProvider = PeopleEventsProvider.newInstance(this);
        ContactEventViewModelFactory factory = new ContactEventViewModelFactory(new AndroidDateLabelCreator(this));
        AndroidStringResources stringResources = new AndroidStringResources(getResources());
        AddEventViewModelFactory addEventFactory = new AddEventViewModelFactory(stringResources);
        ContactEventsFetcher contactEventsFetcher = new ContactEventsFetcher(
                getSupportLoaderManager(),
                this,
                peopleEventsProvider,
                factory,
                addEventFactory
        );

        WriteableAccountsProvider accountsProvider = WriteableAccountsProvider.from(this);
        ContactOperations contactOperations = new ContactOperations(getContentResolver(), accountsProvider, peopleEventsProvider);
        MessageDisplayer messageDisplayer = new ToastDisplayer(getApplicationContext());
        ContactOperationsExecutor operationsExecutor = new ContactOperationsExecutor(getContentResolver());
        ImageDecoder imageDecoder = new ImageDecoder();
        AvatarPresenter avatarPresenter = new AvatarPresenter(imageLoader, avatarView, createToolbarAnimator(toolbar), imageDecoder);
        EventsPresenter eventsPresenter = new EventsPresenter(contactEventsFetcher, adapter, factory, addEventFactory);
        presenter = new AddContactEventsPresenter(
                avatarPresenter,
                eventsPresenter,
                contactOperations,
                messageDisplayer,
                operationsExecutor
        );
        permissionChecker = new PermissionChecker(this);
        presenter.startPresenting(
                new OnCameraClickedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onPictureRetakenRequested() {
                        if (permissionChecker.canReadExternalStorage()) {
                            BottomSheetPicturesDialog
                                    .withClearOption()
                                    .show(getSupportFragmentManager(), "picture_pick");
                        } else {
                            requestExternalStoragePermission();
                        }
                    }

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onNewPictureTakenRequested() {
                        if (permissionChecker.canReadExternalStorage()) {
                            BottomSheetPicturesDialog.newInstance()
                                    .show(getSupportFragmentManager(), "picture_pick");
                        } else {
                            requestExternalStoragePermission();
                        }
                    }

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    private void requestExternalStoragePermission() {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_PERMISSION_EXTERNAL_STORAGE);
                    }
                }
        );
    }

    private ToolbarBackgroundAnimator createToolbarAnimator(MementoToolbar toolbar) {
        if (getResources().getBoolean(R.bool.isLandscape)) {
            return new ToolbarBackgroundStubAnimator();
        } else {
            return ToolbarBackgroundFadingAnimator.setupOn(toolbar);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_PERMISSION_EXTERNAL_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            findViewById(android.R.id.content).post(new Runnable() {
                @Override
                public void run() {
                    if (presenter.displaysAvatar()) {
                        BottomSheetPicturesDialog
                                .withClearOption()
                                .show(getSupportFragmentManager(), "picture_pick");
                    } else {
                        BottomSheetPicturesDialog
                                .newInstance()
                                .show(getSupportFragmentManager(), "picture_pick");
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            Uri imageUri = BottomSheetPicturesDialog.getImageCaptureResultUri(new FilePathProvider(context()), data);
            startCropIntent(imageUri);
        } else if (requestCode == CODE_PICK_A_FILE && resultCode == RESULT_OK) {
            Uri imageUri = BottomSheetPicturesDialog.getImagePickResultUri(data);
            startCropIntent(imageUri);
        } else if (requestCode == CODE_CROP_IMAGE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            presenter.presentAvatar(result.getUri());
        }
    }

    private void startCropIntent(Uri imageToCrop) {
        int size = queryCropSize();
        CropImage.activity(imageToCrop)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setRequestedSize(size, size)
                .start(this);
    }

    private static final int MAX_RESOLUTION = 720;

    private static int queryCropSize() {
        Context context = MementoApplication.getContext();
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.DisplayPhoto.CONTENT_MAX_DIMENSIONS_URI,
                new String[]{ContactsContract.DisplayPhoto.DISPLAY_MAX_DIM}, null, null, null
        );
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return MAX_RESOLUTION;
    }

    private final ContactDetailsListener contactDetailsListener = new ContactDetailsListener() {
        @Override
        public void onAddEventClicked(ContactEventViewModel viewModel) {
            EventType eventType = viewModel.getEventType();
            Optional<Date> initialDate = viewModel.getDate();

            EventDatePickerDialogFragment dialog = EventDatePickerDialogFragment.newInstance(eventType, initialDate);
            dialog.show(getSupportFragmentManager(), "pick_event");
        }

        @Override
        public void onRemoveEventClicked(EventType eventType) {
            presenter.removeEvent(eventType);
        }

        @Override
        public void onContactSelected(Contact contact) {
            presenter.onContactSelected(contact);
        }

        @Override
        public void onNameModified(String newName) {
            presenter.onNameModified(newName);
        }
    };

    @Override
    public void onDatePicked(EventType eventType, Date date) {
        presenter.onEventDatePicked(eventType, date);
    }

    @Override
    public void onActivitySelected(Intent intent) {
        startActivityForResult(intent, getRequestCodeFor(intent));
    }

    @Override
    public void clearSelectedAvatar() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (presenter.isHoldingModifiedData()) {
                    promptToDiscardBeforeExiting();
                } else {
                    cancelActivity();
                }
                break;
            case R.id.menu_add_event_save:
                presenter.saveChanges();
                finishActivitySuccessfully();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void promptToDiscardBeforeExiting() {
        new DiscardPromptDialog()
                .show(getSupportFragmentManager(), "discard_prompt");
    }

    private void finishActivitySuccessfully() {
        setResult(RESULT_OK);
        finish();
    }

    private void cancelActivity() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_from_below);
    }

    @Override
    public void onBackPressed() {
        if (presenter.isHoldingModifiedData()) {
            promptToDiscardBeforeExiting();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDiscardChangesSelected() {
        cancelActivity();
    }
}
