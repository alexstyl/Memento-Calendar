package com.alexstyl.specialdates.addevent

import android.graphics.Bitmap
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.addevent.ui.AvatarPickerView
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.images.ImageLoadedConsumer
import com.alexstyl.specialdates.images.ImageLoader
import com.novoda.notils.meta.AndroidUtils
import java.net.URI

class AndroidAddEventView(private val avatarView: AvatarPickerView,
                          private val eventsAdapter: ContactEventsAdapter,
                          private val imageLoader: ImageLoader,
                          private val toolbarAnimator: ToolbarBackgroundAnimator) : AddEventView {

    private var currentImageLoaded = Optional.absent<Bitmap>()

    override fun displayContact(contact: Contact) {
        display(contact.imagePath)
        AndroidUtils.requestHideKeyboard(avatarView.context, avatarView)
    }

    override fun replace(viewModel: AddEventContactEventViewModel) {
        eventsAdapter.replace(viewModel)
    }


    override fun display(viewModels: List<AddEventContactEventViewModel>) {
        eventsAdapter.display(viewModels)
    }

    override fun display(uri: URI) {
        imageLoader
                .load(uri)
                .withSize(avatarView.width, avatarView.height)
                .into(object : ImageLoadedConsumer {
                    override fun onImageLoaded(loadedImage: Bitmap?) {
                        avatarView.setImageBitmap(loadedImage)
                        toolbarAnimator.fadeOut()
                        avatarView.requestFocus()
                    }

                    override fun onLoadingFailed() {
                        removeAvatar()
                    }
                })
    }

    override fun removeAvatar() {
        avatarView.setImageBitmap(null)
        currentImageLoaded = Optional.absent()
        toolbarAnimator.fadeIn()
    }

}
