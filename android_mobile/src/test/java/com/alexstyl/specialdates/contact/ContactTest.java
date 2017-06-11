package com.alexstyl.specialdates.contact;

import android.content.Context;
import android.net.Uri;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.datedetails.actions.LabeledAction;

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

        TestableContact(long id, DisplayName displayName) {
            super(id, displayName);
        }

        @Override
        protected List<LabeledAction> onBuildActions(Context context) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Uri getLookupUri() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Uri getImagePath() {
            throw new UnsupportedOperationException();
        }
    }
}
