package com.alexstyl.specialdates.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.alexstyl.specialdates.R

class NameSuggestionsAdapter(private val listener: (String) -> Unit) : RecyclerView.Adapter<SuggstedNameViewHolder>() {

    private val suggestedNames = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggstedNameViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_suggested_name, parent, false)
        val nameView = view.findViewById(R.id.suggested_name_text) as TextView
        return SuggstedNameViewHolder(view, nameView)
    }

    override fun onBindViewHolder(holder: SuggstedNameViewHolder, position: Int) {
        holder.bind(suggestedNames[position], listener)
    }

    override fun getItemCount() = suggestedNames.size

    fun clearNames() {
        suggestedNames.clear()
        notifyDataSetChanged() // TODO
    }

    fun updateNames(names: List<String>) {
        this.suggestedNames.clear()
        this.suggestedNames.addAll(names)
        notifyDataSetChanged() // TODO
    }

}
