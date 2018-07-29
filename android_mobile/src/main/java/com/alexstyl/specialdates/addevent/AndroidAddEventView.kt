package com.alexstyl.specialdates.addevent

import android.graphics.Bitmap
import android.view.View
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.addevent.ui.AvatarPickerView
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ImageURL
import com.alexstyl.specialdates.images.ImageLoadedConsumer
import com.alexstyl.specialdates.images.ImageLoader
import com.novoda.notils.meta.AndroidUtils

class AndroidAddEventView(private val avatarView: AvatarPickerView,
                          private val eventsAdapter: ContactEventsAdapter,
                          private val imageLoader: ImageLoader,
                          private val toolbarAnimator: ToolbarBackgroundAnimator,
                          private val saveButton: View) : AddEventView {

    override fun allowImagePick() {
        avatarView.isEnabled = true
    }

    override fun preventImagePick() {
        avatarView.isEnabled = false
    }

    override fun allowSave() {
        saveButton.isEnabled = true
    }

    override fun preventSave() {
        saveButton.isEnabled = false
    }

    private var currentImageLoaded = Optional.absent<Bitmap>()

    override fun displayContact(contact: Contact) {
        display(contact.imagePath)
        AndroidUtils.requestHideKeyboard(avatarView.context, avatarView)
    }

    override fun display(viewModels: List<AddEventContactEventViewModel>) {
        eventsAdapter.display(viewModels)
    }

    override fun display(uri: ImageURL) {
        imageLoader
                .load(uri)
                .withSize(avatarView.width, avatarView.height)
                .into(object : ImageLoadedConsumer {
                    override fun onImageLoaded(loadedImage: Bitmap?) {
                        avatarView.setImageBitmap(loadedImage)
                        toolbarAnimator.fadeOut()
                    }

                    override fun onLoadingFailed() {
                        clearAvatar()
                    }
                })
    }

    override fun clearAvatar() {
        avatarView.setImageBitmap(null)
        currentImageLoaded = Optional.absent()
        toolbarAnimator.fadeIn()
    }

}
