package com.alexstyl.specialdates.datedetails;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.support.AskForSupport;
import com.alexstyl.specialdates.support.OnSupportCardClickListener;
import com.alexstyl.specialdates.support.SupportViewHolder;
import com.novoda.notils.exception.DeveloperError;

import java.util.ArrayList;
import java.util.List;

import static com.alexstyl.specialdates.Optional.absent;

public class DateDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int DETAILED_CARDS_NUMBER_LIMIT = 2;

    private final List<ContactEvent> events = new ArrayList<>();
    private final Optional<NamesInADate> nameday;
    private final Optional<BankHoliday> bankholiday;
    private final ImageLoader imageLoader;
    private final CardActionRecycler cardActionRecycler;
    private final AskForSupport askForSupport;
    private final OnSupportCardClickListener supportListener;
    private final NamedayCardView.OnShareClickListener namedayListener;
    private final ContactCardListener contactCardListener;

    private boolean isShowingFullDetailedCards;
    private final StringResources stringResources;
    private final ColorResources colorResources;
    private Date dateToDisplay;

    public static DateDetailsAdapter newInstance(Context context,
                                                 Date dateToDisplay,
                                                 OnSupportCardClickListener supportListener,
                                                 NamedayCardView.OnShareClickListener namedayListener,
                                                 ContactCardListener contactCardListener,
                                                 Optional<BankHoliday> bankholiday,
                                                 StringResources stringResources,
                                                 ColorResources colorResources
    ) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        Resources resources = context.getResources();
        ImageLoader imageLoader = ImageLoader.createSquareThumbnailLoader(resources);
        CardActionRecycler cardActionRecycler = new CardActionRecycler(layoutInflater);
        Optional<NamesInADate> nameday = getNamedayOptionalForDate(dateToDisplay, context);

        return new DateDetailsAdapter(
                imageLoader, nameday,
                bankholiday, cardActionRecycler,
                new AskForSupport(context), contactCardListener, namedayListener, supportListener,
                stringResources, colorResources
                , dateToDisplay
        );
    }

    private static Optional<NamesInADate> getNamedayOptionalForDate(Date dateToDisplay, Context context) {
        NamedayPreferences namedayPreferences = NamedayPreferences.newInstance(context);
        if (namedayPreferences.isEnabled() && !namedayPreferences.isEnabledForContactsOnly()) {
            NamedayLocale locale = namedayPreferences.getSelectedLanguage();
            NamedayCalendar namedayCalendar = NamedayCalendarProvider.newInstance(context.getResources()).loadNamedayCalendarForLocale(locale, dateToDisplay.getYear());
            NamesInADate names = namedayCalendar.getAllNamedayOn(dateToDisplay);
            if (names.nameCount() > 0) {
                return new Optional<>(names);
            }
        }
        return absent();

    }

    private DateDetailsAdapter(ImageLoader imageLoader,
                               Optional<NamesInADate> nameday,
                               Optional<BankHoliday> bankholiday,
                               CardActionRecycler cardActionRecycler,
                               AskForSupport askForSupport,
                               ContactCardListener contactCardListener,
                               NamedayCardView.OnShareClickListener namedayListener,
                               OnSupportCardClickListener supportListener,
                               StringResources stringResources, ColorResources colorResources,
                               Date dateToDisplay) {
        this.nameday = nameday;
        this.imageLoader = imageLoader;
        this.cardActionRecycler = cardActionRecycler;
        this.namedayListener = namedayListener;
        this.contactCardListener = contactCardListener;
        this.askForSupport = askForSupport;
        this.supportListener = supportListener;
        this.bankholiday = bankholiday;
        this.stringResources = stringResources;
        this.colorResources = colorResources;
        this.dateToDisplay = dateToDisplay;
    }

    boolean isLoadingDetailedCards() {
        return isShowingFullDetailedCards;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int itemCount = events.size();
        if (askForSupport.shouldAskForRating()) {
            itemCount++;
        }
        if (nameday.isPresent()) {
            itemCount++;
        }
        if (bankholiday.isPresent()) {
            itemCount++;
        }
        return itemCount;
    }

    void displayEvents(List<ContactEvent> events) {
        this.events.clear();
        this.events.addAll(events);
        isShowingFullDetailedCards = (this.events.size() <= DETAILED_CARDS_NUMBER_LIMIT);
        notifyDataSetChanged();
    }

    private static final int VIEW_TYPE_NAMEDAY = 0;
    private static final int VIEW_TYPE_DETAILED = 1;
    private static final int VIEW_TYPE_COMPACT = 2;
    private static final int VIEW_TYPE_RATE_APP_COMPACT = 3;
    private static final int VIEW_TYPE_RATE_APP_FULL = 4;
    private static final int VIEW_TYPE_BANKHOLIDAY = 5;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_NAMEDAY) {
            View view = layoutInflater.inflate(R.layout.card_namedays, parent, false);
            return new NameDayCardViewHolder(view, namedayListener);
        }
        if (viewType == VIEW_TYPE_DETAILED) {
            View view = layoutInflater.inflate(R.layout.card_contact_event_full, parent, false);
            return new DetailedDateDetailsViewHolder(view, stringResources, colorResources, contactCardListener, imageLoader, cardActionRecycler);
        }
        if (viewType == VIEW_TYPE_COMPACT) {
            View view = layoutInflater.inflate(R.layout.base_card_compact, parent, false);
            return new CompactDateDetailsViewHolder(view, imageLoader, stringResources, colorResources);
        }

        if (viewType == VIEW_TYPE_RATE_APP_FULL) {
            View view = layoutInflater.inflate(R.layout.card_full_support_heart, parent, false);
            return new SupportViewHolder(view, supportListener);
        }
        if (viewType == VIEW_TYPE_RATE_APP_COMPACT) {
            View view = layoutInflater.inflate(R.layout.card_compact_support_heart, parent, false);
            return new SupportViewHolder(view, supportListener);
        }
        if (viewType == VIEW_TYPE_BANKHOLIDAY) {
            View view = layoutInflater.inflate(R.layout.card_bankholiday, parent, false);
            return new BankHolidayCardViewHolder(view);
        }
        throw new DeveloperError("Invalid viewType " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == VIEW_TYPE_RATE_APP_COMPACT || type == VIEW_TYPE_RATE_APP_FULL) {
            ((SupportViewHolder) holder).bind();
        } else if (type == VIEW_TYPE_NAMEDAY) {
            ((NameDayCardViewHolder) holder).bind(nameday.get());
        } else if (type == VIEW_TYPE_DETAILED || type == VIEW_TYPE_COMPACT) {
            ContactEvent event = getEvent(position);
            ((DateDetailsViewHolder) holder).bind(event, dateToDisplay, contactCardListener);
        } else if (type == VIEW_TYPE_BANKHOLIDAY) {
            ((BankHolidayCardViewHolder) holder).bind(bankholiday.get());
        } else {
            throw new DeveloperError("Invalid type : " + type);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isBankholidayposition(position)) {
            return VIEW_TYPE_BANKHOLIDAY;
        }
        if (isNamecardPosition(position)) {
            return VIEW_TYPE_NAMEDAY;
        }
        if (isASupportCardAt(position)) {
            return getSupportCardViewType();
        }
        return getContactCardViewType();
    }

    boolean isBankholidayposition(int position) {
        return bankholiday.isPresent() && position == 0;
    }

    boolean isNamecardPosition(int position) {
        if (nameday.isPresent()) {
            return bankholiday.isPresent() ? position == 1 : position == 0;
        }
        return false;
    }

    private int getContactCardViewType() {
        if (isShowingFullDetailedCards) {
            return VIEW_TYPE_DETAILED;
        } else {
            return VIEW_TYPE_COMPACT;
        }
    }

    private int getSupportCardViewType() {
        if (isShowingFullDetailedCards) {
            return VIEW_TYPE_RATE_APP_FULL;
        }
        return VIEW_TYPE_RATE_APP_COMPACT;
    }

    private boolean isASupportCardAt(int currentPosition) {
        if (!askForSupport.shouldAskForRating()) {
            return false;
        }

        return getSupportCardPosition() == currentPosition;
    }

    private int getSupportCardPosition() {
        return nameday.isPresent() ? 1 : 0;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        // from cache get all avatars associated to holder.
        // and put them in the
        if (holder.getItemViewType() != VIEW_TYPE_DETAILED) {
            return;
        }
        DetailedDateDetailsViewHolder dayHolder = (DetailedDateDetailsViewHolder) holder;
        dayHolder.clearActions();
    }

    public ContactEvent getEvent(int position) {
        int eventPosition = getEventPositionFrom(position);
        return events.get(eventPosition);
    }

    private int getEventPositionFrom(int position) {
        int eventPosition = position;
        if (nameday.isPresent()) {
            eventPosition--;
        }
        if (bankholiday.isPresent()) {
            eventPosition--;
        }
        if (askForSupport.shouldAskForRating() && isOnOrAfterSupportCardPosition(eventPosition)) {
            eventPosition--;
        }
        return eventPosition;
    }

    private boolean isOnOrAfterSupportCardPosition(int position) {
        return position >= getSupportCardPosition();
    }

    public boolean isFullRowAt(int itemPos) {
        return isShowingFullDetailedCards || isNamecardPosition(itemPos) || isBankholidayposition(itemPos);
    }

    public int getHeaderCount() {
        int count = bankholiday.isPresent() ? 1 : 0;
        count += (nameday.isPresent() ? 1 : 0);
        return count;
    }
}
