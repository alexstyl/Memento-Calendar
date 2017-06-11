package com.alexstyl.specialdates.datedetails;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alexstyl.specialdates.BuildConfig;
import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.ActionWithParameters;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.actions.LabeledAction;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.date.MonthInt;
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider;
import com.alexstyl.specialdates.events.bankholidays.GreekBankHolidaysCalculator;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.permissions.PermissionNavigator;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.support.AskForSupport;
import com.alexstyl.specialdates.ui.base.MementoFragment;
import com.alexstyl.specialdates.ui.dialog.ProgressFragmentDialog;
import com.alexstyl.specialdates.util.ContactsObserver;
import com.alexstyl.specialdates.util.ShareNamedaysIntentCreator;
import com.novoda.notils.caster.Views;

import java.util.List;

public class DateDetailsFragment extends MementoFragment {

    private static final ActionWithParameters CONTACT_INTERACT_EXTERNAL = new ActionWithParameters(Action.INTERACT_CONTACT, "source", "external");

    public static final String KEY_DISPLAYING_YEAR = BuildConfig.APPLICATION_ID + ".displaying_year";
    public static final String KEY_DISPLAYING_MONTH = BuildConfig.APPLICATION_ID + ".displaying_month";
    public static final String KEY_DISPLAYING_DAY_OF_MONTH = BuildConfig.APPLICATION_ID + ".displaying_dayOfMonth";
    private static final String FM_TAG_ACTIONS = "alexstyl:contacts_actions";

    private static final int LOADER_ID_EVENTS = 503;

    private static final int SPAN_SIZE = 1;
    private Date date;
    private ProgressBar progress;
    private GridLayoutManager layoutManager;
    private DateDetailsAdapter adapter;
    private Analytics analytics;
    private ContactPermissionRequest permissions;
    private ExternalNavigator externalNavigator;
    private RecyclerView recyclerView;

    public static Fragment newInstance(Date date) {
        Fragment fragment = new DateDetailsFragment();

        Bundle args = new Bundle(3);
        args.putInt(KEY_DISPLAYING_YEAR, date.getYear());
        args.putInt(KEY_DISPLAYING_MONTH, date.getMonth());
        args.putInt(KEY_DISPLAYING_DAY_OF_MONTH, date.getDayOfMonth());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_DISPLAYING_YEAR, date.getYear());
        outState.putInt(KEY_DISPLAYING_MONTH, date.getMonth());
        outState.putInt(KEY_DISPLAYING_DAY_OF_MONTH, date.getDayOfMonth());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (permissions.permissionIsPresent()) {
            getLoaderManager().initLoader(LOADER_ID_EVENTS, null, loaderCallbacks);
        } else {
            permissions.requestForPermission();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissions.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = AnalyticsProvider.getAnalytics(getActivity());
        PermissionNavigator navigator = new PermissionNavigator(getActivity(), analytics);
        PermissionChecker checker = new PermissionChecker(getActivity());
        permissions = new ContactPermissionRequest(navigator, checker, permissionCallbacks);
        externalNavigator = new ExternalNavigator(getActivity(), analytics);
        stringResources = new AndroidStringResources(getResources());
    }

    private final ContactPermissionRequest.PermissionCallbacks permissionCallbacks = new ContactPermissionRequest.PermissionCallbacks() {
        @Override
        public void onPermissionGranted() {
            getLoaderManager().initLoader(LOADER_ID_EVENTS, null, loaderCallbacks);
        }

        @Override
        public void onPermissionDenied() {
            getActivity().finishAffinity();
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_upcoming_light, menu);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            date = getDateFrom(savedInstanceState);
        } else {
            date = getDate();
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.contacts_grid);
        progress = (ProgressBar) view.findViewById(android.R.id.progress);
        emptyView = Views.findById(view, R.id.date_details_empty);
        layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);

        layoutManager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        // TODO
                        return SPAN_SIZE;
                    }
                }
        );

        adapter = new DateDetailsAdapter(DateDetailsViewHolderFactory.createCompactFactory(LayoutInflater.from(getActivity()), UILImageLoader.createLoader(getResources())), dateDetailsClickListener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private Date getDate() {
        int year = getArguments().getInt(KEY_DISPLAYING_YEAR);
        @MonthInt int month = getArguments().getInt(KEY_DISPLAYING_MONTH);
        int dayOfMonth = getArguments().getInt(KEY_DISPLAYING_DAY_OF_MONTH);
        return Date.on(dayOfMonth, month, year);
    }

    private Date getDateFrom(Bundle savedInstanceState) {
        int year = savedInstanceState.getInt(KEY_DISPLAYING_YEAR);
        @MonthInt int month = savedInstanceState.getInt(KEY_DISPLAYING_MONTH);
        int dayOfMonth = savedInstanceState.getInt(KEY_DISPLAYING_DAY_OF_MONTH);
        return Date.on(dayOfMonth, month, year);
    }

    private View emptyView;
    private AndroidStringResources stringResources;
    private LoaderManager.LoaderCallbacks<List<DateDetailsViewModel>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<DateDetailsViewModel>>() {

        @Override
        public Loader<List<DateDetailsViewModel>> onCreateLoader(int loaderID, Bundle bundle) {
            if (loaderID == LOADER_ID_EVENTS) {
                PeopleEventsProvider peopleEventsProvider = PeopleEventsProvider.newInstance(getActivity());
                ContactsObserver contactsObserver = new ContactsObserver(getContentResolver(), new Handler());
                return new DateDetailsLoader(
                        getActivity(),
                        date,
                        new AskForSupport(getContext()),
                        peopleEventsProvider,
                        contactsObserver,
                        NamedayPreferences.newInstance(getContext()),
                        new BankHolidayProvider(new GreekBankHolidaysCalculator(OrthodoxEasterCalculator.INSTANCE)),
                        new SupportViewModelFactory(getContext(), new AndroidStringResources(getResources())),
                        new PeopleEventViewModelFactory(date, stringResources),
                        new BankHolidayViewModelFactory(),
                        new NamedayViewModelFactory(),
                        NamedayCalendarProvider.newInstance(getResources())
                );
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<List<DateDetailsViewModel>> EventItemLoader, List<DateDetailsViewModel> result) {
            adapter.displayEvents(result);
            showData();
            hideLoading();
        }

        private void showData() {
            if (adapter.getItemCount() == 0) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        private void hideLoading() {
            progress.setVisibility(View.GONE);
        }

        @Override
        public void onLoaderReset(Loader<List<DateDetailsViewModel>> EventItemLoader) {
            // do nothing
        }
    };

    private final DateDetailsClickListener dateDetailsClickListener = new DateDetailsClickListener() {

        @Override
        public void onCardClicked(Contact contact) {
            analytics.trackAction(CONTACT_INTERACT_EXTERNAL);
            externalNavigator.toContactDetails(contact);
        }

        @Override
        public void onContactActionsMenuClicked(View view, Contact contact) {
            final List<LabeledAction> actions = contact.getUserActions(getActivity());
            if (actions == null) {
                return;
            }
            int size = actions.size();
            PopupMenu popup = new PopupMenu(getActivity(), view);
            for (int i = 0; i < size; i++) {
                LabeledAction action = actions.get(i);
                popup.getMenu().add(contact.hashCode(), i, 0, getString(action.getName()));
            }

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return actions.get(menuItem.getItemId()).fire(getActivity());
                }
            });

            popup.show();
        }

        @Override
        public void onNamedayShared(NamesInADate namedays) {
            Intent intent = new ShareNamedaysIntentCreator(getActivity(), DateDisplayStringCreator.INSTANCE).createNamedaysShareIntent(namedays);
            try {
                Intent chooserIntent = Intent.createChooser(intent, getString(R.string.share_via));
                startActivity(chooserIntent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), R.string.no_app_found, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onSupportCardClicked() {
            Toast.makeText(getActivity(), R.string.support_thanks_for_rating, Toast.LENGTH_SHORT).show();
            AskForSupport askForSupport = new AskForSupport(getActivity());
            askForSupport.onRateEnd();
            externalNavigator.toPlayStore();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onActionClicked(View v, LabeledAction action) {
            // an action on the card was pressed. The action might open a different app that takes ages to load.
            // display the Loading progress
            if (action.hasSlowStart()) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isVisible() && isResumed()) {
                            // if after a second the activity is still showing, show the loading dialog
                            ProgressFragmentDialog dialog = ProgressFragmentDialog.newInstance(getString(R.string.loading), true);
                            dialog.show(getFragmentManager(), FM_TAG_ACTIONS);
                        }
                    }
                }, DateUtils.SECOND_IN_MILLIS);
            }
            action.fire(getActivity());
            ActionWithParameters actionWithParameters = new ActionWithParameters(Action.INTERACT_CONTACT, "source", action.getAction().getName());
            analytics.trackAction(actionWithParameters);
        }

    };

}
