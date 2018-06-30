package com.alexstyl.specialdates.home

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.alexstyl.specialdates.BuildConfig
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import javax.inject.Inject


class DonationBannerView(context: Context, attrs: AttributeSet?) : LinearLayoutCompat(context, attrs) {

    private var closeView: View? = null
    private var closeListener: (CloseBannerListener)? = null


    lateinit var errorTracker: CrashAndErrorTracker
        @Inject set

    private var adView: AdView? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        (context.applicationContext as MementoApplication).applicationModule.inject(this)

        LayoutInflater.from(context).inflate(R.layout.merge_donation_banner_view, this, true)
        super.setOrientation(LinearLayout.HORIZONTAL)

        setBackgroundColor(Color.WHITE)

        adView = findViewById(R.id.banner_ad)

        closeView = findViewById(R.id.banner_close)
        closeView!!.setOnClickListener { closeListener?.invoke() }
    }

    fun loadAd() {
        adView!!.adListener = object : AdListener() {

            override fun onAdFailedToLoad(errorCode: Int) {
                val message = "Couldn't load ad $errorCode"
                errorTracker.track(RuntimeException(message))
                if (BuildConfig.DEBUG) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAdLoaded() {
                showBanner()
            }
        }

        adView!!.loadAd(
                AdRequest
                        .Builder()
                        .applyTestDevice()
                        .build())
    }

    private fun AdRequest.Builder.applyTestDevice() = apply {
        if (BuildConfig.DEBUG) {
            addTestDevice(DonationBannerView.TEST_DEVICE)
        }
    }

    fun setOnCloseBannerListener(onCloseBannerListener: CloseBannerListener) {
        this.closeListener = onCloseBannerListener
    }

    override fun setOrientation(orientation: Int) {
        throw UnsupportedOperationException(DonationBannerView::class.java.toString() + " can only be HORIZONTAL")
    }

    fun showBanner() {
        visibility = View.VISIBLE
//        this.animate()
//                .yBy(height.toFloat())
//                .setInterpolator(AnticipateOvershootInterpolator())
//                .start()
    }

    fun hide() {
        visibility = View.GONE

//        animate()
//                .yBy(height.toFloat())
//                .setInterpolator(DecelerateInterpolator())
//                .setListener(object : SimpleAnimatorListener() {
//                    override fun onAnimationEnd(animator: Animator) {
//                        visibility = View.GONE
//                    }
//                }).start()
    }

    companion object {

        const val TEST_DEVICE = "544D83E87B224A20DDBE6B1FE2710E74"
    }

}

private typealias CloseBannerListener = () -> Unit


