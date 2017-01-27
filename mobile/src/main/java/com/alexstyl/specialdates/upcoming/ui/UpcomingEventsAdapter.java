package com.alexstyl.specialdates.upcoming.ui;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.upcoming.MonthLabels;
import com.alexstyl.specialdates.upcoming.MonthOfYear;
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;
import com.novoda.notils.exception.DeveloperError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpcomingEventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<CelebrationDate> celebrationDates = new ArrayList<>();
    private final SparseArray<Integer> viewTypes = new SparseArray<>();
    private final HashMap<Integer, MonthOfYear> headers = new HashMap<>();

    private final SparseArray<Integer> positionToCelebration = new SparseArray<>();
    private final SparseArray<Integer> rowToMonth = new SparseArray<>();

    private final ImageLoader imageLoader;

    private int closestDay = -1;
    private final Date today;

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_DAY_EVENTS = 1;

    private final MonthLabels monthLabels;

    private OnUpcomingEventClickedListener listener;

    public UpcomingEventsAdapter(Date today, ImageLoader imageLoader, MonthLabels monthLabels) {
        this.imageLoader = imageLoader;
        this.today = today;
        this.monthLabels = monthLabels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_DAY_EVENTS) {
            return UpcomingEventsViewHolder.createFor(parent, imageLoader);
        } else if (viewType == VIEWTYPE_HEADER) {
            return MonthHeaderViewHolder.createFor(parent, monthLabels);
        } else {
            throw new DeveloperError("Invalid viewType " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEWTYPE_HEADER) {
            MonthOfYear monthOfYear = headers.get(position);
            ((MonthHeaderViewHolder) vh).displayMonth(monthOfYear);
        } else if (viewType == VIEWTYPE_DAY_EVENTS) {
            CelebrationDate celebrationDate = getCelebrationAtPosition(position);
            ((UpcomingEventsViewHolder) vh).bind(celebrationDate, today, listener);
        } else {
            throw new DeveloperError("Unknown view type " + viewType);
        }
    }

    private CelebrationDate getCelebrationAtPosition(int position) {
        Integer index = positionToCelebration.get(position);
        return celebrationDates.get(index);
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypes.get(position);
    }

    @Override
    public int getItemCount() {
        return celebrationDates.size() + headers.size();
    }

    public void setUpcomingEvents(List<CelebrationDate> upcomingEvents, OnUpcomingEventClickedListener listener) {
        this.listener = listener;
        if (this.celebrationDates == upcomingEvents) {
            return;
        }

        this.celebrationDates.clear();
        if (upcomingEvents != null) {
            this.celebrationDates.addAll(upcomingEvents);
        }
        setUpHeadersAndClosestDay();
        notifyDataSetChanged();
    }

    private void setUpHeadersAndClosestDay() {
        headers.clear();
        rowToMonth.clear();
        viewTypes.clear();
        positionToCelebration.clear();

        clearClosestDay();
        if (containsNoCelebration()) {
            return;
        }

        int dateCount = celebrationDates.size();

        int previousMonth = 0;
        int actualPosition = 0;

        for (int i = 0; i < dateCount; i++) {
            CelebrationDate celebrationDate = celebrationDates.get(i);
            int currentMonth = celebrationDate.getMonth();

            if (currentMonth != previousMonth) {
                markPositionAsHeader(actualPosition, celebrationDate);
                previousMonth = currentMonth;
                actualPosition++;
            }

            rowToMonth.put(actualPosition, currentMonth);
            positionToCelebration.put(actualPosition, i);

            viewTypes.put(actualPosition, VIEWTYPE_DAY_EVENTS);

            checkForClosestDay(actualPosition, celebrationDate);
            actualPosition++;
        }
    }

    private void clearClosestDay() {
        closestDay = -1;
    }

    private void markPositionAsHeader(int position, CelebrationDate date) {
        viewTypes.put(position, VIEWTYPE_HEADER);
        headers.put(position, MonthOfYear.of(date));
        rowToMonth.put(position, date.getMonth());
    }

    private void checkForClosestDay(int actualPosition, CelebrationDate event) {
        if (closestDayIsUnknown() && isOnOrAfterToday(event)) {
            closestDay = actualPosition;
        }
    }

    private boolean closestDayIsUnknown() {
        return closestDay == -1;
    }

    private boolean isOnOrAfterToday(CelebrationDate event) {
        return DateComparator.INSTANCE.compare(event.getDate(), today) >= 0;
    }

    private boolean containsNoCelebration() {
        return celebrationDates.isEmpty();
    }

    public int getClosestDayPosition() {
        return closestDay;
    }

}
