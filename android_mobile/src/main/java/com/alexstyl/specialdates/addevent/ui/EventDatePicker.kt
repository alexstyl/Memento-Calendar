package com.alexstyl.specialdates.addevent.ui

import android.content.Context
import android.support.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import android.widget.CheckedTextView
import android.widget.LinearLayout
import android.widget.NumberPicker
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.MonthInt
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.upcoming.MonthLabels
import com.novoda.notils.caster.Views
import java.util.Locale

class EventDatePicker(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val labels: MonthLabels

    private val dayPicker: NumberPicker
    private val monthPicker: NumberPicker
    private val yearPicker: NumberPicker

    private val includesYearCheckbox: CheckedTextView

    private val today: Date

    var displayingDate: Date
        get() {
            val dayOfMonth = dayOfMonth
            val month = month
            if (isDisplayingYear) {
                val year = year
                return dateOn(dayOfMonth, month, year)
            } else {
                return dateOn(dayOfMonth, month, null)
            }
        }
        set(dateToDisplay) = if (dateToDisplay.hasYear()) {
            dayPicker.value = dateToDisplay.dayOfMonth
            monthPicker.value = dateToDisplay.month
            yearPicker.value = dateToDisplay.year!!
            yearPicker.visibility = View.VISIBLE
            includesYearCheckbox.isChecked = true
        } else {
            dayPicker.value = dateToDisplay.dayOfMonth
            monthPicker.value = dateToDisplay.month
            yearPicker.value = currentYear()!!
            yearPicker.visibility = View.GONE
            includesYearCheckbox.isChecked = false
        }

    private val isDisplayingYear: Boolean
        get() = includesYearCheckbox.isChecked

    private val dayOfMonth: Int
        get() = dayPicker.value

    private val month: Int
        @MonthInt
        get() = monthPicker.value

    private val year: Int
        get() = yearPicker.value

    private val dateValidator = NumberPicker.OnValueChangeListener { picker, oldVal, newVal -> updateMaximumDaysInCurrentMonth() }

    init {
        super.setOrientation(LinearLayout.HORIZONTAL)
        labels = MonthLabels.forLocale(Locale.getDefault())
        today = todaysDate()
        View.inflate(getContext(), R.layout.merge_birthday_picker, this)

        dayPicker = Views.findById(this, R.id.day_picker)
        setupDayPicker()

        monthPicker = Views.findById(this, R.id.month_picker)
        setupMonthPicker()

        yearPicker = Views.findById(this, R.id.year_picker)
        setupYearPicker()

        includesYearCheckbox = Views.findById(this, R.id.include_year_checkbox)
        includesYearCheckbox.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val checked = includesYearCheckbox.isChecked
                includesYearCheckbox.isChecked = !checked

                if (includesYearCheckbox.isChecked) {
                    showYearPicker()
                } else {
                    hideYearPicker()
                }
                updateMaximumDaysInCurrentMonth()
            }

            private fun hideYearPicker() {
                TransitionManager.beginDelayedTransition(this@EventDatePicker)
                yearPicker.visibility = View.GONE
            }

            private fun showYearPicker() {
                TransitionManager.beginDelayedTransition(this@EventDatePicker)
                yearPicker.visibility = View.VISIBLE
            }
        })
    }

    private fun setupDayPicker() {
        dayPicker.minValue = FIRST_DAY_OF_MONTH
        dayPicker.maxValue = today.daysInCurrentMonth

        dayPicker.value = today.dayOfMonth

    }

    private fun setupMonthPicker() {
        monthPicker.minValue = FIRST_MONTH
        monthPicker.maxValue = LAST_MONTH

        monthPicker.displayedValues = labels.monthsOfYear

        monthPicker.value = today.month
        monthPicker.setOnValueChangedListener(dateValidator)
    }

    private fun setupYearPicker() {
        yearPicker.minValue = FIRST_YEAR
        yearPicker.maxValue = currentYear()!!

        yearPicker.value = currentYear()!!
        yearPicker.setOnValueChangedListener(dateValidator)
    }

    private fun currentYear(): Int? {
        return today.year
    }

    private fun updateMaximumDaysInCurrentMonth() {
        val maxDays: Int
        if (isDisplayingYear) {
            maxDays = dateOn(FIRST_DAY_OF_MONTH, month, year).daysInCurrentMonth
        } else {
            maxDays = dateOn(FIRST_DAY_OF_MONTH, month).daysInCurrentMonth
        }

        dayPicker.maxValue = maxDays
    }

    companion object {
        private const val FIRST_DAY_OF_MONTH = 1
        private const val FIRST_MONTH = 1
        private const val LAST_MONTH = 12
        private const val FIRST_YEAR = 1900
    }
}
