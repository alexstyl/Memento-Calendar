package com.novoda.simplechromecustomtabs.navigation;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.customtabs.CustomTabsIntent;

import com.novoda.simplechromecustomtabs.connection.Connection;
import com.novoda.simplechromecustomtabs.connection.Session;

import java.util.ArrayList;
import java.util.List;

public class SimpleChromeCustomTabsIntentBuilder {

    private final Connection connection;
    private final List<Composer> composers;

    public SimpleChromeCustomTabsIntentBuilder(Connection connection, List<Composer> composers) {
        this.connection = connection;
        this.composers = composers;
    }

    public static SimpleChromeCustomTabsIntentBuilder newInstance(Connection connection) {
        List<Composer> composerList = new ArrayList<>();
        return new SimpleChromeCustomTabsIntentBuilder(connection, composerList);
    }

    public SimpleChromeCustomTabsIntentBuilder withToolbarColor(@ColorInt int color) {
        composers.add(new ToolbarColorComposer(color));
        return this;
    }

    public SimpleChromeCustomTabsIntentBuilder withUrlBarHiding() {
        composers.add(new UrlBarHidingComposer());
        return this;
    }

    public SimpleChromeCustomTabsIntentBuilder withMenuItem(String label, PendingIntent pendingIntent) {
        composers.add(new MenuItemComposer(label, pendingIntent));
        return this;
    }

    public SimpleChromeCustomTabsIntentBuilder withDefaultShareMenuItem() {
        composers.add(new DefaultShareMenuItemComposer());
        return this;
    }

    public SimpleChromeCustomTabsIntentBuilder withActionButton(Bitmap icon, String description, PendingIntent pendingIntent, boolean shouldTint) {
        composers.add(new ActionButtonComposer(icon, description, pendingIntent, shouldTint));
        return this;
    }

    public SimpleChromeCustomTabsIntentBuilder withCloseButtonIcon(Bitmap icon) {
        composers.add(new CloseButtonIconComposer(icon));
        return this;
    }

    public SimpleChromeCustomTabsIntentBuilder withExitAnimations(Context context, @AnimRes int enterResId, @AnimRes int exitResId) {
        composers.add(new ExitAnimationsComposer(context, enterResId, exitResId));
        return this;
    }

    public SimpleChromeCustomTabsIntentBuilder withStartAnimations(Context context, @AnimRes int enterResId, @AnimRes int exitResId) {
        composers.add(new StartAnimationsComposer(context, enterResId, exitResId));
        return this;
    }

    public SimpleChromeCustomTabsIntentBuilder showingTitle() {
        composers.add(new ShowTitleComposer());
        return this;
    }

    /**
     * TODO Allow intent creation without connection. Might be useful to re-customise a current session.
     */
    public CustomTabsIntent createIntent() {
        if (connection.isDisconnected()) {
            throw new IllegalStateException("An active connection to custom tabs service is required for intent creation");
        }

        Session session = connection.getSession();

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(session.getCustomTabsSession());
        for (Composer composer : composers) {
            builder = composer.compose(builder);
        }

        return builder.build();
    }

}
