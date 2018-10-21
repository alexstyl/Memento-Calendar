package com.alexstyl.specialdates.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.DateLabelCreator

class SearchResultNamedayViewHolder(convertView: View,
                                    private val namedayView: TextView,
                                    private val datesLayout: LinearLayout,
                                    private val labelCreator: DateLabelCreator) : RecyclerView.ViewHolder(convertView) {


    fun bind(dates: NamedaySearchResultViewModel, searchResultListener: SearchResultClickListener?) {
        namedayView.text = dates.name
        this.datesLayout.removeAllViews()
        val inflater = LayoutInflater.from(namedayView.context)
        for (i in 0 until dates.size()) {
            val view = inflater.inflate(R.layout.nameday_date, datesLayout, false)
            val dateView = view.findViewById<TextView>(android.R.id.text1)

            val date = dates.getDate(i)
            val prettyDate = labelCreator.createWithYearPreferred(date)
            dateView.text = prettyDate
            if (searchResultListener != null) {
                view.setOnClickListener { searchResultListener.onNamedayClicked(date) }
            }

            datesLayout.addView(dateView)
        }
    }
}
