package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.date.Months;
import com.alexstyl.specialdates.date.MonthInt;
import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedaysList;
import com.alexstyl.specialdates.events.namedays.calendar.EasternNameday;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

class SpecialGreekNamedaysCalculator {

    private static final DateComparator COMPARATOR = DateComparator.INSTANCE;
    private static final List<String> PROPATORWN = Arrays.asList(
            "Ααρών",
            "Αβραάμ",
            "Αδάμ",
            "Αδαμάντιος",
            "Διαμαντής",
            "Αδαμαντία",
            "Διαμαντούλα",
            "Διαμάντω",
            "Δαβίδ",
            "Δαυίδ",
            "Δανάη",
            "Δανιήλ",
            "Δεβόρα",
            "Εσθήρ",
            "Εύα",
            "Ισαάκ",
            "Ιώβ",
            "Νώε",
            "Ραχήλ",
            "Ρεβέκκα",
            "Ρουμπίνη",
            "Σάρα",
            "Μελχισεδέ"
    );
    private static final String CLOE = "Χλόη";

    private static final List<String> MARKOS_ALTS = Arrays.asList(
            "Μάρκος",
            "Μαρκής",
            "Μαρκία",
            "Μαρκούλης",
            "Μαρκούλ"
    );

    private final List<EasternNameday> easternNamedays;

    SpecialGreekNamedaysCalculator(List<EasternNameday> easternNamedays) {
        this.easternNamedays = easternNamedays;
    }

    NamedayBundle calculateForEasterDate(Date easter) {
        Node node = new SoundNode();
        NamedaysList namedaysList = new NamedaysList();

        for (EasternNameday easternNameday : easternNamedays) {
            int daysUntilEaster = easternNameday.getDateToEaster();
            Date date = easter.addDay(daysUntilEaster);

            for (String name : easternNameday.getNamesCelebrating()) {
                node.addDate(name, date);
                namedaysList.addNameday(date, name);
            }
        }
        appendSpecialScenarios(easter, node, namedaysList);
        return new NamedayBundle(node, namedaysList);
    }

    private void appendSpecialScenarios(Date easter, Node node, NamedaysList namedaysList) {
        addSpecialPropatorwn(node, namedaysList);
        addSpecialMarkos(node, namedaysList, easter);
        addSpecialGiwrgos(node, namedaysList, easter);
        addSpecialChloe(node, namedaysList);
    }

    @SuppressWarnings({"MagicNumber"})
    private void addSpecialPropatorwn(Node node, NamedaysList namedaysList) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Date date = createDayDateFrom(calendar);
        for (String variation : PROPATORWN) {
            node.addDate(variation, date);
            namedaysList.addNameday(date, variation);
        }
    }

    @SuppressWarnings({"MagicNumber"})
    private void addSpecialMarkos(Node node, NamedaysList namedaysList, Date easter) {
        int year = Date.Companion.today().getYear();
        Date date = Date.Companion.on(23, Months.APRIL, year);
        if (COMPARATOR.compare(easter, date) > 0) {
            date = date.addDay(2);
        } else {
            date = Date.Companion.on(25, Months.APRIL, year);
        }

        for (String variation : MARKOS_ALTS) {
            node.addDate(variation, date);
            namedaysList.addNameday(date, variation);
        }
    }

    private static final List<String> GEORGE_ALTS = Arrays.asList(
            "Γεώργιος",
            "Γεωργής",
            "Γιώργος",
            "Γκόγκος",
            "Γιώργης",
            "Γιωργίτσης",
            "Γεωργία",
            "Γιωργία",
            "Γεωργούλα",
            "Γιωργίτσα",
            "Γίτσα"
    );

    @SuppressWarnings({"MagicNumber"})
    private void addSpecialGiwrgos(Node node, NamedaysList namedaysList, Date easter) {
        Date date = Date.Companion.on(23, Months.APRIL, Date.Companion.today().getYear());

        Date actualDate;
        if (COMPARATOR.compare(easter, date) > 0) {
            actualDate = easter.addDay(1);
        } else {
            actualDate = date;
        }

        for (String variation : GEORGE_ALTS) {
            node.addDate(variation, actualDate);
            namedaysList.addNameday(actualDate, variation);
        }
    }

    @SuppressWarnings({"MagicNumber"})
    private void addSpecialChloe(Node node, NamedaysList namedaysList) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DAY_OF_MONTH, 13);

        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        Date date = createDayDateFrom(cal);

        String variation = CLOE;
        node.addDate(variation, date);
        namedaysList.addNameday(date, variation);
    }

    private static Date createDayDateFrom(Calendar calendar) {
        @MonthInt int month = calendar.get(Calendar.MONTH);
        return Date.Companion.on(calendar.get(Calendar.DAY_OF_MONTH), month, calendar.get(Calendar.YEAR));
    }

}
