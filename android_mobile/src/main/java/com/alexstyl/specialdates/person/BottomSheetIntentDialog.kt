package com.alexstyl.specialdates.person

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.ui.base.MementoDialog
import com.alexstyl.specialdates.ui.widget.SpacesItemDecoration
import com.novoda.notils.caster.Classes
import com.novoda.notils.caster.Views.findById


class BottomSheetIntentDialog : MementoDialog() {

    companion object {
        private const val KEY_INTENTS = "key:intents"
        private const val KEY_TITLE = "key:title"

        fun newIntent(title: String, intents: ArrayList<Intent>): BottomSheetIntentDialog {
            return BottomSheetIntentDialog().apply {
                val bundle = Bundle()
                bundle.putParcelableArrayList(KEY_INTENTS, intents)
                bundle.putString(KEY_TITLE, title)
                this.arguments = bundle
            }
        }
    }

    lateinit var listener: BottomSheetIntentListener

    @Suppress("OverridingDeprecatedMember", "DEPRECATION")
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.listener = Classes.from(activity)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = activity
        val dialog = BottomSheetDialog(context!!)
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_bottom_dialog, null, false)

        val title = view.findViewById<TextView>(R.id.bottom_sheet_title)
        title.text = arguments?.getString(KEY_TITLE)

        val grid = findById<RecyclerView>(view, R.id.bottom_sheet_grid)
        val gridLayoutManager = GridLayoutManager(context, resources.getInteger(R.integer.bottom_sheet_span_count))
        grid.addItemDecoration(SpacesItemDecoration(
                resources.getDimensionPixelSize(R.dimen.add_event_image_option_vertical),
                gridLayoutManager.spanCount
        ))
        grid.layoutManager = gridLayoutManager

        val intents = arguments?.get(KEY_INTENTS) as ArrayList<Intent>

        val createViewModelsFor = createViewModelsFor(intents)
        val adapter = BottomSheetIntentAdapter(listener, createViewModelsFor)
        grid.adapter = adapter

        dialog.setContentView(view)
        return dialog
    }

    private fun createViewModelsFor(intents: List<Intent>): List<IntentOptionViewModel> {
        val packageManager = context!!.packageManager
        val viewModels = ArrayList<IntentOptionViewModel>(intents.size)

        for (intent in intents) {
            val resolveInfos = packageManager.queryIntentActivities(intent, 0)
            for (resolveInfo in resolveInfos) {
                val icon = resolveInfo.loadIcon(packageManager)
                val label = resolveInfo.loadLabel(packageManager).toString()
                val launchingIntent = Intent(intent.action)
                launchingIntent.data = intent.data
                launchingIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)
                viewModels.add(IntentOptionViewModel(icon, label, launchingIntent))
            }
        }
        return viewModels
    }
}
