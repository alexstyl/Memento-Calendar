package com.alexstyl.specialdates.addevent;

import android.Manifest;
import android.content.ContentResolver;
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

import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.addevent.EventDatePickerDialogFragment.OnEventDatePickedListener;
import com.alexstyl.specialdates.addevent.bottomsheet.BottomSheetPicturesDialog;
import com.alexstyl.specialdates.addevent.bottomsheet.BottomSheetPicturesDialog.Listener;
import com.alexstyl.specialdates.addevent.ui.AvatarPickerView;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.AndroidDateLabelCreator;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.ShortDateLabelCreator;
import com.alexstyl.specialdates.images.ImageDecoder;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.permissions.AndroidPermissionChecker;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.caster.Views;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import javax.inject.Inject;
import java.net.URI;

public class AddEventActivity extends ThemedMementoActivity implements Listener, OnEventDatePickedListener, DiscardPromptDialog.Listener {

    private static final int CODE_TAKE_PICTURE = 404;
    private static final int CODE_PICK_A_FILE = 405;
    private static final int CODE_CROP_IMAGE = CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
    private static final int CODE_PERMISSION_EXTERNAL_STORAGE = 406;

    private AddContactEventsPresenter presenter;
    private AndroidPermissionChecker permissionChecker;
    private FilePathProvider filePathProvider;
    @Inject Analytics analytics;
    @Inject Strings strings;
    @Inject ImageLoader imageLoader;
    @Inject NamedayUserSettings namedayUserSettings;
    @Inject ContactsProvider contactsProvider;
    @Inject PeopleEventsProvider peopleEventsProvider;
    @Inject CrashAndErrorTracker tracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in_from_below, R.anim.stay);
        setContentView(R.layout.activity_add_event);

        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);
        analytics.trackScreen(Screen.ADD_EVENT);
        filePathProvider = new FilePathProvider(this);
        MementoToolbar toolbar = Views.findById(this, R.id.memento_toolbar);
        setSupportActionBar(toolbar);
        toolbar.displayNavigationIconAsClose();

        AvatarPickerView avatarView = Views.findById(this, R.id.add_event_avatar);
        RecyclerView eventsView = Views.findById(this, R.id.add_event_events);
        eventsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        eventsView.setHasFixedSize(true);
        ContactDetailsAdapter adapter = new ContactDetailsAdapter(contactDetailsListener);
        eventsView.setAdapter(adapter);

        AddEventContactEventViewModelFactory factory = new AddEventContactEventViewModelFactory(new AndroidDateLabelCreator(this));
        AddEventViewModelFactory addEventFactory = new AddEventViewModelFactory(strings);
        ContactEventsFetcher contactEventsFetcher = new ContactEventsFetcher(
                getSupportLoaderManager(),
                this,
                peopleEventsProvider,
                factory,
                addEventFactory
        );

        WriteableAccountsProvider accountsProvider = WriteableAccountsProvider.from(this);
        ContactOperations contactOperations = new ContactOperations(
                getContentResolver(),
                accountsProvider,
                peopleEventsProvider,
                ShortDateLabelCreator.INSTANCE
        );
        MessageDisplayer messageDisplayer = new ToastDisplayer(getApplicationContext());
        ContactOperationsExecutor operationsExecutor = new ContactOperationsExecutor(getContentResolver(), tracker);
        ImageDecoder imageDecoder = new ImageDecoder();
        AvatarPresenter avatarPresenter = new AvatarPresenter(imageLoader, avatarView, createToolbarAnimator(toolbar), imageDecoder);
        EventsPresenter eventsPresenter = new EventsPresenter(contactEventsFetcher, adapter, factory, addEventFactory);
        presenter = new AddContactEventsPresenter(
                analytics, avatarPresenter,
                eventsPresenter,
                contactOperations,
                messageDisplayer,
                operationsExecutor
        );
        permissionChecker = new AndroidPermissionChecker(tracker, this);
        presenter.startPresenting(
                new OnCameraClickedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onPictureRetakenRequested() {
                        if (permissionChecker.canReadExternalStorage()) {
                            BottomSheetPicturesDialog
                                    .includeClearImageOption()
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
                    if (isFinishing()) {
                        return;
                    }
                    if (presenter.displaysAvatar()) {
                        BottomSheetPicturesDialog
                                .includeClearImageOption()
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
            analytics.trackImageCaptured();
            Uri imageUri = BottomSheetPicturesDialog.getImageCaptureResultUri(filePathProvider);
            startCropIntent(imageUri);
        } else if (requestCode == CODE_PICK_A_FILE && resultCode == RESULT_OK) {
            analytics.trackExistingImagePicked();
            Uri imageUri = BottomSheetPicturesDialog.getImagePickResultUri(data);
            startCropIntent(imageUri);
        } else if (requestCode == CODE_CROP_IMAGE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                presenter.presentAvatar(result.getUri() == null ? null : URI.create(result.getUri().toString()));

            } else if (resultCode == RESULT_CANCELED && result != null) {
                tracker.track(result.getError());
            }
        }
    }

    private void startCropIntent(Uri imageToCrop) {
        int size = queryCropSize(getContentResolver());
        CropImage.activity(imageToCrop)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setRequestedSize(size, size)
                .start(this);
    }

    private static final int MAX_RESOLUTION = 720;

    private static int queryCropSize(ContentResolver resolver) {
        Cursor cursor = resolver.query(
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
        public void onAddEventClicked(AddEventContactEventViewModel viewModel) {
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
        grantUriPermission(intent.getComponent().getPackageName(), filePathProvider.createTemporaryCacheFile(), Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, getRequestCodeFor(intent));
    }

    @Override
    public void onClearAvatarSelected() {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (presenter.isHoldingModifiedData()) {
                    promptToDiscardBeforeExiting();
                } else {
                    cancelActivity();
                }
                return true;
            case R.id.menu_add_event_save:
                presenter.saveChanges();
                finishActivitySuccessfully();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void promptToDiscardBeforeExiting() {
        new DiscardPromptDialog()
                .show(getSupportFragmentManager(), "discard_prompt");
    }

    private void finishActivitySuccessfully() {
        analytics.trackEventAddedSuccessfully();
        setResult(RESULT_OK);
        finish();
    }

    private void cancelActivity() {
        analytics.trackAddEventsCancelled();
        setResult(RESULT_CANCELED);
        navigateUpToParent();
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

    public static Intent buildIntent(Context context) {
        return new Intent(context, AddEventActivity.class);
    }
}
