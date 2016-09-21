package com.alexstyl.specialdates.events.namedays.calendar.resource;

import android.content.Context;

import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedaysList;
import com.alexstyl.specialdates.events.namedays.calendar.EasternNameday;

import java.util.Calendar;
import java.util.List;

class SpecialGreekNamedaysCalculator {

    private final List<EasternNameday> easternNamedays;
    private final Context context;

    SpecialGreekNamedaysCalculator(List<EasternNameday> easternNamedays) {
        this.easternNamedays = easternNamedays;
        this.context = MementoApplication.getContext();
    }

    NamedayBundle calculateForEasterDate(DayDate easter) {
        Node node = new SoundNode();
        NamedaysList namedaysList = new NamedaysList();

        for (EasternNameday easternNameday : easternNamedays) {
            int daysUntilEaster = easternNameday.getDateToEaster();
            DayDate date = easter.addDay(daysUntilEaster);

            for (String name : easternNameday.getNamesCelebrating()) {
                node.addDate(name, date);
                namedaysList.addNameday(date, name);
            }
        }
        appendSpecialScenarios(easter, node, namedaysList);
        return new NamedayBundle(node, namedaysList);
    }

    private void appendSpecialScenarios(DayDate easter, Node node, NamedaysList namedaysList) {
        addSpecialPropatorwn(node, namedaysList, context);
        addSpecialMarkos(node, namedaysList, context, easter);
        addSpecialGiwrgos(node, namedaysList, context, easter);
        addSpecialChloe(node, namedaysList, context);
    }

    private void addSpecialPropatorwn(Node node, NamedaysList namedaysList, Context context) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 11);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        DayDate date = DayDate.newInstance(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
        String[] vars = context.getResources().getStringArray(R.array.special_propatorwn_alts);
        for (String variation : vars) {
            node.addDate(variation, date);
            namedaysList.addNameday(date, variation);
        }
    }

    private void addSpecialMarkos(Node node, NamedaysList namedaysList, Context context, DayDate easter) {
        int year = DayDate.today().getYear();
        DayDate dayDate = DayDate.newInstance(23, DayDate.APRIL, year);
        if (easter.isAfter(dayDate)) {
            dayDate = dayDate.addDay(2);
        } else {
            dayDate = DayDate.newInstance(25, DayDate.APRIL, year);
        }

        String[] vars = context.getResources().getStringArray(R.array.special_markos_alts);
        for (String variation : vars) {
            node.addDate(variation, dayDate);
            namedaysList.addNameday(dayDate, variation);
        }
    }

    private void addSpecialGiwrgos(Node node, NamedaysList namedaysList, Context context, DayDate easter) {
        DayDate date = DayDate.newInstance(23, DayDate.APRIL, DayDate.today().getYear());

        DayDate actualDate;
        if (easter.isAfter(date)) {
            actualDate = easter.addDay(1);
        } else {
            actualDate = date;
        }

        String[] vars = context.getResources().getStringArray(R.array.special_george_alts);
        for (String variation : vars) {
            node.addDate(variation, actualDate);
            namedaysList.addNameday(actualDate, variation);
        }
    }

    private void addSpecialChloe(Node node, NamedaysList namedaysList, Context context) {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
//        cal.set(Calendar.DAY_OF_MONTH, 13);
//
//        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
//            cal.add(Calendar.DAY_OF_MONTH, 1);
//        }
//        DayDate date = DayDate.newInstance(cal);
//
//        String variation = context.getString(R.string.specialname_chloe);
//        node.addDate(variation, date);
//        namedaysList.addNameday(date, variation);
    }

}
