package com.alexstyl.specialdates.addevent.bottomsheet

import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore

import com.alexstyl.specialdates.addevent.ImageIntentFactory
import com.alexstyl.specialdates.addevent.UriFilePathProvider

class PhotoPickerViewModelFactory(private val uriFilePathProvider: UriFilePathProvider,
                                  private val intentResolver: IntentResolver,
                                  private val intentCreator: ImageIntentFactory,
                                  private val packageManager: PackageManager) {

    fun createViewModels(): List<PhotoPickerViewModel> {
        return capturePhotoViewModels() + pickPhotoViewModels()
    }

    private fun capturePhotoViewModels(): List<PhotoPickerViewModel> {
        val file = uriFilePathProvider.createImageFile()
        val outputUri = uriFilePathProvider.uriFor(file)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)

        return if (takePictureIntent.resolveActivity(packageManager) != null) {
            intentResolver.createViewModelsFor(takePictureIntent, file.absolutePath)
        } else {
            emptyList()
        }
    }

    private fun pickPhotoViewModels(): List<PhotoPickerViewModel> {
        val pickAnImage = intentCreator.pickExistingImage()
        return intentResolver.createViewModelsFor(pickAnImage, "")
    }

}
