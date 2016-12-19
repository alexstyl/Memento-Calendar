package com.alexstyl.specialdates.addevent.ui;

import android.content.Context;
import android.text.Editable;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;
import com.alexstyl.specialdates.util.Utils;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;
import com.novoda.notils.text.SimpleTextWatcher;

public class ContactHeroView extends LinearLayout {

    private ImageLoader imageLoader;
    private ContactSuggestionView nameSuggestion;
    private ColorImageView avatar;
    private ViewGroup avatarHolder;

    public ContactHeroView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
        imageLoader = ImageLoader.createCircleThumbnailLoader(getResources());
        inflate(getContext(), R.layout.merge_contact_hero_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        nameSuggestion = Views.findById(this, R.id.contacthero_name_suggestion);
        avatar = Views.findById(this, R.id.contacthero_avatar);
        avatarHolder = Views.findById(this, R.id.contact_hero_avatar_holder);

        nameSuggestion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    return listener.onEditorAction(view);
                }
                return false;
            }
        });

        nameSuggestion.setOnContactSelectedListener(new ContactSuggestionView.OnContactSelectedListener() {
            @Override
            public void onContactSelected(Contact contact) {
                listener.onContactSelected(nameSuggestion, contact);
            }
        });

        nameSuggestion.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                listener.onUserChangedContactName(s.toString().trim());
            }
        });
    }

    private Listener listener = NO_LISTENER;

    public void setListener(Listener listener) {
        if (listener == null) {
            this.listener = NO_LISTENER;
        } else {
            this.listener = listener;
        }
    }

    public Editable getText() {
        return nameSuggestion.getText();
    }

    public void displayAvatarFor(Contact contact) {
        imageLoader.loadThumbnail(contact.getImagePath(), avatar.getImageView());
        avatar.setLetter(contact.getGivenName());
        avatar.setBackgroundVariant((int) contact.getContactID());
        animateAvatarIn();
    }

    public void displayAvatarFor(String inputText) {
        avatar.setLetter(inputText);
        avatar.setBackgroundVariant(inputText.length());
        animateAvatarIn();
    }

    private void animateAvatarIn() {
        if (avatar.getWidth() > 0) {
            return;
        }
        int width = getResources().getDimensionPixelSize(R.dimen.add_birthday_avatar_actual_width);
        if (Utils.hasKitKat()) {
            TransitionManager.beginDelayedTransition(avatarHolder);
        }
        int height = getResources().getDimensionPixelSize(R.dimen.add_birthday_avatar_actual_height);
        ViewGroup.LayoutParams params = avatar.getLayoutParams();
        params.width = width;
        params.height = height;
        avatar.setLayoutParams(params);
    }

    public void hideAvatar() {
        if (avatar.getWidth() == 0) {
            return;
        }
        int width = getResources().getDimensionPixelSize(R.dimen.add_birthday_avatar_starting_width);
        if (Utils.hasKitKat()) {
            TransitionManager.beginDelayedTransition(avatarHolder);
        }
        int height = getResources().getDimensionPixelSize(R.dimen.add_birthday_avatar_starting_height);
        ViewGroup.LayoutParams params = avatar.getLayoutParams();
        params.width = width;
        params.height = height;
        avatar.setLayoutParams(params);
    }

    public interface Listener {

        void onContactSelected(ContactSuggestionView nameSuggestion, Contact contact);

        boolean onEditorAction(TextView view);

        void onUserChangedContactName(String newText);

    }

    private static final Listener NO_LISTENER = new Listener() {
        @Override
        public void onContactSelected(ContactSuggestionView nameSuggestion, Contact contact) {
            Log.w("onContactSeleceted called with an empty listener");
        }

        @Override
        public boolean onEditorAction(TextView view) {
            Log.w("onEditorAction called with an empty listener");
            return false;
        }

        @Override
        public void onUserChangedContactName(String newText) {
            Log.w("presentNoContact called with an empty listener");
        }
    };
}
