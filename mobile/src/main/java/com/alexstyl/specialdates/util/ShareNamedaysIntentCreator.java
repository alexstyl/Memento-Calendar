package com.alexstyl.specialdates.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

public class ShareNamedaysIntentCreator {

    private final Context context;
    private final DateDisplayStringCreator stringCreator;

    public ShareNamedaysIntentCreator(Context context, DateDisplayStringCreator stringCreator) {
        this.context = context.getApplicationContext();
        this.stringCreator = stringCreator;
    }

    public Intent createNamedaysShareIntent(NamesInADate namesInADate) {
        String text = buildSharingTextFor(namesInADate);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return intent;
    }

    private String buildSharingTextFor(NamesInADate namesInADate) {
        StringBuilder str = new StringBuilder();
        String dateString = stringCreator.fullyFormattedDate(namesInADate.getDate());
        str
                .append(context.getString(R.string.days_namedays, dateString))
                .append("\n").append("\n")
                .append(TextUtils.join(", ", namesInADate.getNames()));
        return str.toString();
    }
}
