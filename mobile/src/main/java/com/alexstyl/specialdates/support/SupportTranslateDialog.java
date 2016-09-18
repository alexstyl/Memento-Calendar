package com.alexstyl.specialdates.support;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.theming.Themer;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.novoda.notils.caster.Views;

public class SupportTranslateDialog extends ThemedActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_translate, Themer.get(this).getCurrentTheme());
        Views.findById(this, android.R.id.content).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        Views.findById(this, R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Views.findById(this, R.id.button_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyLinkToClipboard();
                Toast.makeText(context(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean copyLinkToClipboard() {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(getResources().getString(R.string.app_name), getString(R.string.link_translate_short));
        clipboard.setPrimaryClip(clip);
        return true;
    }

}
