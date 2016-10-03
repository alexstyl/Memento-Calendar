package com.alexstyl.specialdates.contact;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.actions.LabeledAction;
import com.alexstyl.specialdates.date.Date;
import com.novoda.notils.exception.DeveloperError;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ContactTest {

    private Contact contact;
    private final DisplayName displayName = DisplayName.from("Alex Styl");

    @Before
    public void setUp() {
        contact = new TestableContact(0, displayName);
    }

    @Test
    public void toStringReturnsTheDisplayNameRepresentation() {
        assertThat(contact.toString()).isEqualTo(displayName.toString());
    }

    private final class TestableContact extends Contact {

        public TestableContact(long id, DisplayName displayName) {
            super(id, displayName, Optional.<Date>absent());
        }

        @Override
        protected List<LabeledAction> onBuildActions(Context context) {
            throw new DeveloperError("Not impletemented");
        }

        @Override
        public Uri getLookupUri() {
            throw new DeveloperError("Not impletemented");
        }

        @Override
        public void displayQuickInfo(Context context, View view) {
            throw new DeveloperError("Not impletemented");
        }

        @Override
        public String getImagePath() {
            throw new DeveloperError("Not impletemented");
        }
    }
}
