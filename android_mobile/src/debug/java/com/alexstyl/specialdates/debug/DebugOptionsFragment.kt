package com.alexstyl.specialdates.debug

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.ui.base.MementoFragment

class DebugOptionsFragment : MementoFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.debug_fragment_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.debug_options)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = DebugOptionsAdapter(DebugOption.values().toList()) { option ->
            swapFragment(option)
        }
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    private fun swapFragment(option: DebugOption) {
        fragmentManager!!
                .beginTransaction()
                .replace(
                        R.id.fragment_container,
                        OptionToFragment.mappings[option]
                )
                .addToBackStack(null)
                .commit()
    }

    override fun onStart() {
        super.onStart()
        activity?.title = "Memento Debug"
    }

}
