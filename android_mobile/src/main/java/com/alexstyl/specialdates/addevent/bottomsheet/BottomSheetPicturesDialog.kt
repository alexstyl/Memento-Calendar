package com.alexstyl.specialdates.addevent.bottomsheet

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.addevent.ImageIntentFactory
import com.alexstyl.specialdates.addevent.UriFilePathProvider
import com.alexstyl.specialdates.ui.base.MementoDialog
import com.alexstyl.specialdates.ui.widget.SpacesItemDecoration
import com.novoda.notils.caster.Classes
import com.novoda.notils.caster.Views
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.net.URI

class BottomSheetPicturesDialog : MementoDialog() {

    private var parentListener: Listener? = null

    private lateinit var adapter: ImagePickerOptionsAdapter
    private lateinit var imagePickViewModelFactory: ImagePickViewModelFactory
    private var disposable: Disposable? = null

    private val includeClear: Boolean
        get() = arguments != null && arguments!!.getBoolean(KEY_INCLUDE_CLEAR, false)

    private val internalListener = object : Listener {
        override fun onImagePickerOptionSelected(viewModel: ImagePickerOptionViewModel) {
            dismiss()
            parentListener?.onImagePickerOptionSelected(viewModel)
        }

        override fun onClearAvatarSelected() {
            dismiss()
            parentListener?.onClearAvatarSelected()
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        parentListener = Classes.from<Listener>(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePickViewModelFactory = ImagePickViewModelFactory(
                UriFilePathProvider(activity!!),
                IntentResolver(activity!!.packageManager),
                ImageIntentFactory()
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(activity!!)
        val layoutInflater = LayoutInflater.from(activity!!)
        val view = layoutInflater.inflate(R.layout.dialog_pick_image, null, false)

        val grid = Views.findById<RecyclerView>(view, R.id.pick_image_grid)
        val resources = resources
        val gridLayoutManager = GridLayoutManager(activity!!, resources.getInteger(R.integer.bottom_sheet_span_count))
        grid.addItemDecoration(SpacesItemDecoration(
                resources.getDimensionPixelSize(R.dimen.add_event_image_option_vertical),
                gridLayoutManager.spanCount
        ))
        grid.layoutManager = gridLayoutManager
        adapter = if (includeClear)
            ImagePickerOptionsAdapter.createWithClear(internalListener)
        else
            ImagePickerOptionsAdapter.newInstance(internalListener)
        grid.adapter = adapter
        dialog.setContentView(view)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        disposable = Observable.fromCallable {
            imagePickViewModelFactory.createViewModels()
        }.doOnError {
            it.printStackTrace()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewModels -> adapter.updateWith(viewModels) }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    interface Listener {
        /**
         * Called when the user selects the option to select an picture as an avatar, via the [BottomSheetPicturesDialog]
         *
         */
        fun onImagePickerOptionSelected(viewModel: ImagePickerOptionViewModel)

        /**
         * Called when the user selects the option to clear the existing avatar, via the [BottomSheetPicturesDialog]
         */
        fun onClearAvatarSelected()
    }

    companion object {

        private const val KEY_INCLUDE_CLEAR = "key_include_clear"

        fun newInstance(): BottomSheetPicturesDialog {
            return BottomSheetPicturesDialog()
        }

        fun includeClearImageOption(): BottomSheetPicturesDialog {
            val args = Bundle(1)
            args.putBoolean(KEY_INCLUDE_CLEAR, true)
            val fragment = BottomSheetPicturesDialog()
            fragment.arguments = args
            return fragment
        }

        fun getImagePickResultUri(data: Intent): URI {
            return URI.create(data.data.toString())
        }
    }
}
