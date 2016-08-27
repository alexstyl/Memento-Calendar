package com.alexstyl.specialdates.util;

import android.content.Context;

import com.alexstyl.specialdates.R;

import java.text.Normalizer;

public class GreekNameUtils {

    public static boolean isGreekLocaleSelected(Context context) {
        return context.getResources().getBoolean(R.bool.isGreekLocaleSelected);
    }

    public static String removeAccents(CharSequence title) {
        String decomposed = Normalizer.normalize(title, Normalizer.Form.NFD);
        String removed = decomposed.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return removed;
    }
}
