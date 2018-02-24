package com.alexstyl.specialdates.home

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.alexstyl.specialdates.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class DonationBannerView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var closeView: View? = null
    private var listener: OnCloseBannerListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        LayoutInflater.from(context).inflate(R.layout.merge_donation_banner_view, this, true)
        super.setOrientation(LinearLayout.HORIZONTAL)

        setBackgroundColor(Color.WHITE)

        val adView = findViewById<AdView>(R.id.banner_ad)


        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: Int) {
                println("Couldn't load stuff")
            }
        }

        adView.loadAd(AdRequest.Builder()
                .addTestDevice("544D83E87B224A20DDBE6B1FE2710E74")
                .build())

        closeView = findViewById(R.id.banner_close)
        closeView!!.setOnClickListener { listener!!.onCloseButtonPressed() }
    }

    fun setOnCloseBannerListener(listener: OnCloseBannerListener) {
        this.listener = listener
    }

    override fun setOrientation(orientation: Int) {
        throw UnsupportedOperationException(DonationBannerView::class.java.toString() + " can only be HORIZONTAL")
    }
}
