package com.alexstyl.specialdates.contact.actions;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ContactActionFactoryTest {

    private ContactActionFactory factory;

    private static final long CONTACT_ID = 51;

    @Before
    public void setup() {
        factory = new ContactActionFactory();
    }

    @Test
    public void testCreateCallAction() throws Exception {
        LabeledAction callAction = factory.createCallAction(CONTACT_ID);
        assertThat(callAction.getAction()).isInstanceOf(CallAction.class);
    }

    @Test
    public void testCreateSMSAction() throws Exception {
        LabeledAction callAction = factory.createSMSAction(CONTACT_ID);
        assertThat(callAction.getAction()).isInstanceOf(SMSAction.class);
    }

    @Test
    public void testCreateEmailAction() throws Exception {
        LabeledAction callAction = factory.createEmailAction(CONTACT_ID);
        assertThat(callAction.getAction()).isInstanceOf(EMailAction.class);
    }
}
