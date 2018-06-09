package com.alexstyl.specialdates.addevent.bottomsheet

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.alexstyl.specialdates.addevent.bottomsheet.BottomSheetPicturesDialog.Listener

class ImagePickerOptionViewHolder(view: View,
                                  private val iconView: ImageView,
                                  private val labelView: TextView
) : RecyclerView.ViewHolder(view) {

    fun bind(viewModel: ImagePickerOptionViewModel, listener: Listener) {
        iconView.setImageDrawable(viewModel.activityIcon)
        labelView.text = viewModel.label
        itemView.setOnClickListener { listener.onImagePickerOptionSelected(viewModel) }
    }
}
