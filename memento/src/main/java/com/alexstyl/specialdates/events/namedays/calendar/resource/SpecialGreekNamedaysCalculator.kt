package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateComparator
import com.alexstyl.specialdates.date.MonthInt
import com.alexstyl.specialdates.date.Months
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.namedays.MapNamedaysList
import com.alexstyl.specialdates.events.namedays.MutableNamedaysList
import com.alexstyl.specialdates.events.namedays.Namedays
import com.alexstyl.specialdates.events.namedays.calendar.EasternNameday
import java.util.*

class SpecialGreekNamedaysCalculator(private val easternNamedays: List<EasternNameday>) {

    fun calculateForEasterDate(easter: Date): Namedays {
        val node = SoundNode()
        val namedaysList = MapNamedaysList()

        for (easternNameday in easternNamedays) {
            val daysUntilEaster = easternNameday.dateToEaster
            val date = easter.addDay(daysUntilEaster)

            for (name in easternNameday.namesCelebrating) {
                node.addDate(name, date)
                namedaysList.addSpecificYearNameday(date, name)
            }
        }
        appendSpecialScenarios(easter, node, namedaysList)
        return Namedays(node, namedaysList)
    }

    private fun appendSpecialScenarios(easter: Date, node: Node, namedaysList: MutableNamedaysList) {
        addSpecialPropatorwn(node, namedaysList)
        addSpecialMarkos(node, namedaysList, easter)
        addSpecialGiwrgos(node, namedaysList, easter)
        addSpecialChloe(node, namedaysList)
    }

    private fun addSpecialPropatorwn(node: Node, namedaysList: MutableNamedaysList) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 11)
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val date = createDayDateFrom(calendar)
        for (variation in PROPATORWN) {
            node.addDate(variation, date)
            namedaysList.addSpecificYearNameday(date, variation)

        }
    }

    private fun addSpecialMarkos(node: Node, namedaysList: MutableNamedaysList, easter: Date) {
        val year = todaysDate().year
        var date = dateOn(23, Months.APRIL, year)
        if (COMPARATOR.compare(easter, date) > 0) {
            date = date.addDay(2)
        } else {
            date = dateOn(25, Months.APRIL, year)
        }

        for (variation in MARKOS_ALTS) {
            node.addDate(variation, date)
            namedaysList.addSpecificYearNameday(date, variation)
        }
    }

    private fun addSpecialGiwrgos(node: Node, namedaysList: MutableNamedaysList, easter: Date) {
        val date = dateOn(23, Months.APRIL, todaysDate().year)

        val actualDate: Date
        if (COMPARATOR.compare(easter, date) > 0) {
            actualDate = easter.addDay(1)
        } else {
            actualDate = date
        }

        for (variation in GEORGE_ALTS) {
            node.addDate(variation, actualDate)
            namedaysList.addSpecificYearNameday(actualDate, variation)
        }
    }

    private fun addSpecialChloe(node: Node, namedaysList: MutableNamedaysList) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.MONTH, Calendar.FEBRUARY)
        cal.set(Calendar.DAY_OF_MONTH, 13)

        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        val date = createDayDateFrom(cal)

        val variation = CLOE
        node.addDate(variation, date)
        namedaysList.addSpecificYearNameday(date, variation)
    }

    companion object {

        private val COMPARATOR = DateComparator.INSTANCE
        private val PROPATORWN = listOf(
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
        )
        private val CLOE = "Χλόη"

        private val MARKOS_ALTS = listOf(
                "Μάρκος",
                "Μαρκής",
                "Μαρκία",
                "Μαρκούλης",
                "Μαρκούλ"
        )

        private val GEORGE_ALTS = listOf(
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
        )

        private fun createDayDateFrom(calendar: Calendar): Date {
            @MonthInt val month = calendar.get(Calendar.MONTH)
            return dateOn(calendar.get(Calendar.DAY_OF_MONTH), month, calendar.get(Calendar.YEAR))
        }
    }

}
