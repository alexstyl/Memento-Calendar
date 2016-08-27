package com.alexstyl.specialdates.support;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.novoda.notils.caster.Views;

public class SupportViewHolder extends RecyclerView.ViewHolder {

    private static final String smiley = " " + Emoticon.SMILEY.asText();

    private final ImageView heartView;
    private final TextView textDescription;

    private final Animation heartAnimation;

    public SupportViewHolder(View view, final OnSupportCardClickListener listener) {
        super(view);
        this.heartView = Views.findById(view, R.id.support_card_heroimage);
        this.textDescription = Views.findById(view, R.id.support_card_description);
        this.heartAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.heartbeat);
        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onSupportCardClicked(v);
                    }
                }
        );
    }

    public void bind() {
        heartView.startAnimation(heartAnimation);
        textDescription.append(smiley);
    }

}
