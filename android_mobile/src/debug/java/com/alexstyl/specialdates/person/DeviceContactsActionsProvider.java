package com.alexstyl.specialdates.person;

import com.alexstyl.specialdates.contact.Contact;

import java.util.List;

import org.jetbrains.annotations.NotNull;

interface DeviceContactsActionsProvider {
    List<ContactActionViewModel> buildActionsFor(@NotNull Contact contact);
}
