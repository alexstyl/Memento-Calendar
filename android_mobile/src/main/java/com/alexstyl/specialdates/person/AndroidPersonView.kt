package com.alexstyl.specialdates.person

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alexstyl.android.Version
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.images.ImageLoadedConsumer
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.theming.Themer
import com.nostra13.universalimageloader.core.assist.LoadedFrom
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware

class AndroidPersonView(private val personNameView: TextView,
                        private val ageAndSignView: TextView,
                        private val imageLoader: ImageLoader,
                        private val avatarView: ImageView,
                        private val adapter: ContactItemsAdapter,
                        private val tabLayout: TabLayout,
                        private val appBarLayout: AppBarLayout,
                        private val toolbarGradient: ImageView,
                        private val resources: Resources,
                        private val onOffsetChangedListener: AppBarLayout.OnOffsetChangedListener,
                        private val themer: Themer) : PersonView {

    override fun displayPersonInfo(viewModel: PersonInfoViewModel) {
        personNameView.text = viewModel.displayName
        ageAndSignView.text = viewModel.ageAndStarSignlabel
        ageAndSignView.visibility = viewModel.AgeAndStarSignVisibility

        imageLoader.load(viewModel.image)
                .withSize(avatarView.width, avatarView.height)
                .into(object : ImageLoadedConsumer {

                    override fun onImageLoaded(loadedImage: Bitmap) {
                        if (Version.hasLollipop()) {
                            appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener)
                        }
                        FadeInBitmapDisplayer(ANIMATION_DURATION).display(loadedImage, ImageViewAware(avatarView), LoadedFrom.DISC_CACHE)
                        val layers = arrayOfNulls<Drawable>(2)
                        layers[0] = resources.getColorDrawable(android.R.color.transparent)
                        layers[1] = resources.getDrawableCompat(R.drawable.black_to_transparent_gradient_facing_down)
                        val transitionDrawable = TransitionDrawable(layers)
                        toolbarGradient.setImageDrawable(transitionDrawable)
                        transitionDrawable.startTransition(ANIMATION_DURATION)
                        toolbarGradient.visibility = View.VISIBLE
                    }

                    override fun onLoadingFailed() {
                        val layers = arrayOfNulls<Drawable>(2)
                        layers[0] = resources.getColorDrawable(android.R.color.transparent)
                        layers[1] = resources.getDrawableCompat(R.drawable.ic_person_96dp)
                        val transitionDrawable = TransitionDrawable(layers)
                        avatarView.setImageDrawable(transitionDrawable)
                        transitionDrawable.startTransition(ANIMATION_DURATION)
                        toolbarGradient.visibility = View.GONE
                    }
                })
    }

    override fun displayAvailableActions(viewModel: PersonAvailableActionsViewModel) {
        adapter.displayEvents(viewModel)

        updateTabIfNeeded(0, R.drawable.ic_gift)
        updateTabIfNeeded(1, R.drawable.ic_call)
        updateTabIfNeeded(2, R.drawable.ic_message)

        if (tabLayout.tabCount <= 1) {
            tabLayout.visibility = View.GONE
        } else {
            tabLayout.visibility = View.VISIBLE
        }
    }

    override fun showPersonAsVisible() {
        throw UnsupportedOperationException("Visibility is not currently available")
    }

    override fun showPersonAsHidden() {
        throw UnsupportedOperationException("Visibility is not currently available")
    }

    private fun updateTabIfNeeded(index: Int, @DrawableRes iconResId: Int) {
        if (tabLayout.getTabAt(index) != null) {
            tabLayout.getTabAt(index)!!.icon = themer.getTintedDrawable(iconResId, personNameView.context)
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 400
    }


}

private fun Resources.getDrawableCompat(@DrawableRes drawableResId: Int): Drawable? =
        ResourcesCompat.getDrawable(this, drawableResId, null)

private fun Resources.getColorDrawable(@ColorRes colorRes: Int): Drawable? =
        ColorDrawable(ResourcesCompat.getColor(this, colorRes, null))

