package com.alexstyl.specialdates.events.namedays.activity

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.alexstyl.specialdates.R

class NamedaysAdapter(private val layoutInflater: LayoutInflater) : RecyclerView.Adapter<NameViewHolder>() {
    private val viewModels = arrayListOf<NamedaysViewModel>()
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NameViewHolder {
        val view = layoutInflater.inflate(R.layout.row_nameday_name, parent, false)
        val nameView = view.findViewById(R.id.nameday_name) as TextView
        return NameViewHolder(view, nameView)
    }


    override fun onBindViewHolder(holder: NameViewHolder?, position: Int) {
        holder?.bind(viewModels[position])
    }

    override fun getItemCount(): Int = viewModels.size
    fun display(viewModels: List<NamedaysViewModel>) {
        val diffResult = DiffUtil.calculateDiff(NamedaysViewModelDiff(this.viewModels, viewModels))
        this.viewModels.clear()
        this.viewModels.addAll(viewModels)
        diffResult.dispatchUpdatesTo(this)
    }

}

