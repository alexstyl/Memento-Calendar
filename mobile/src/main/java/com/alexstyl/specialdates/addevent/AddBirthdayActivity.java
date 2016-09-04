package com.alexstyl.specialdates.addevent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ui.ContactHeroView;
import com.alexstyl.specialdates.addevent.ui.ContactsAutoCompleteView;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.AnalyticsEvent;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.contact.Birthday;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.theming.MementoTheme;
import com.alexstyl.specialdates.theming.Themer;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.novoda.notils.caster.Views;
import com.novoda.notils.meta.AndroidUtils;

import java.util.ArrayList;

public class AddBirthdayActivity extends ThemedActivity {

    private ContactHeroView contactHeroView;
    private BirthdayLabelView birthdayLabel;
    private BirthdayFormPresenter presenter;
    private Analytics analytics;
    private boolean eventCreated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        analytics = Analytics.get(this);
        analytics.trackScreen(Screen.ADD_BIRTHDAY);
        MementoTheme theme = Themer.get().getCurrentTheme();
        setContentView(R.layout.activity_add_birthday, theme);

        contactHeroView = Views.findById(this, R.id.addbirthday_hero);
        birthdayLabel = Views.findById(this, R.id.addbirthday_datepicker);
        Button addButton = Views.findById(this, R.id.addbirthday_add_button);
        Button dismissButton = Views.findById(this, R.id.addbirthday_dismiss_button);

        contactHeroView.setListener(listener);
        birthdayLabel.setOnEditListener(onBirthdayLabelClicked);
        addButton.setOnClickListener(onAddButtonClickListener);
        dismissButton.setOnClickListener(onDismissButtonClickListener);

        presenter = new BirthdayFormPresenter(contactHeroView, birthdayLabel, addButton);

        BirthdayPickerDialog.resetListenerToDialog(getSupportFragmentManager(), birthdaySelectedListener);
        addButton.setEnabled(false);
    }

    private final View.OnClickListener onAddButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (presenter.isPresentingNoStoredContact()) {
                // store contact
                AccountData account = getAccountToStoreContact();
                new ContactPersister(context(), getContentResolver())
                        .createContactWithNameAndBirthday(
                                contactHeroView.getText().toString(),
                                birthdayLabel.getDisplayingBirthday(),
                                account
                        );
            } else {
                new ContactPersister(context(), getContentResolver())
                        .addBirthdayToExistingContact(presenter.getDisplayingBirthday(), presenter.getSelectedContact());
            }
            finishSuccessfully();
        }

        private AccountData getAccountToStoreContact() {
            WriteableAccountsProvider accountsProvider = WriteableAccountsProvider.from(context());
            ArrayList<AccountData> availableAccounts = accountsProvider.getAvailableAccounts();
            if (availableAccounts.size() == 0) {
                return AccountData.NO_ACCOUNT;
            } else {
                return availableAccounts.get(0);
            }
        }

    };

    private void finishSuccessfully() {
        eventCreated = true;
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        trackEventCreation();
    }

    private void trackEventCreation() {
        AnalyticsEvent analyticsEvent;
        if (eventCreated) {
            analyticsEvent = birthdayCreationSuccess();
        } else {
            analyticsEvent = birthdayCreationFailed();
        }
        analytics.track(analyticsEvent);
    }

    private AnalyticsEvent birthdayCreationSuccess() {
        return new AnalyticsEvent(AnalyticsEvent.Events.ADD_BIRTHDAY).withData("success", "true");
    }

    private AnalyticsEvent birthdayCreationFailed() {
        return new AnalyticsEvent(AnalyticsEvent.Events.ADD_BIRTHDAY).withData("success", "false");
    }

    private final BirthdayLabelView.OnEditListener onBirthdayLabelClicked = new BirthdayLabelView.OnEditListener() {
        @Override
        public void onEdit() {
            displayBirthdayPickerDialog();
        }

        private void displayBirthdayPickerDialog() {
            Birthday birthday = birthdayLabel.getDisplayingBirthday();
            BirthdayPickerDialog dialog;
            if (birthday == null) {
                dialog = BirthdayPickerDialog.createDialog(birthdaySelectedListener);
            } else {
                dialog = BirthdayPickerDialog.createDialogFor(birthday, birthdaySelectedListener);
            }
            dialog.show(getSupportFragmentManager(), BirthdayPickerDialog.TAG);
        }
    };

    private final ContactHeroView.Listener listener = new ContactHeroView.Listener() {

        @Override
        public void onContactSelected(ContactsAutoCompleteView nameSuggestion, Contact contact) {
            presenter.displayContactAsSelected(contact);
            AndroidUtils.requestHideKeyboard(context(), nameSuggestion);
            nameSuggestion.clearFocus();
        }

        @Override
        public boolean onEditorAction(TextView view) {
            AndroidUtils.requestHideKeyboard(context(), view);
            view.clearFocus();
            return true;
        }

        @Override
        public void onUserChangedContactName(String newText) {
            presenter.presentNoContact();
        }

    };

    private BirthdayPickerDialog.OnBirthdaySelectedListener birthdaySelectedListener = new BirthdayPickerDialog.OnBirthdaySelectedListener() {
        @Override
        public void onBirthdaySet(Birthday birthday) {
            presenter.presentBirthday(birthday);
        }
    };

    private final View.OnClickListener onDismissButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

}
