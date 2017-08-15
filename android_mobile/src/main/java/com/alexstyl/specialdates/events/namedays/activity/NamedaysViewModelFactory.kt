package com.alexstyl.specialdates.events.namedays.activity

import android.content.res.Resources
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.ui.widget.LetterPainter

class NamedaysViewModelFactory(val resources: Resources) {

    fun viewModelsFor(name: String): NamedaysViewModel {
        return NamedaysViewModel(name)
    }

    fun viewModelsFor(contact: Contact): CelebratingContactViewModel {
        val colorVariant = LetterPainter.getVariant(resources, contact.hashCode())
        return CelebratingContactViewModel(contact, contact.displayName.toString(), colorVariant)
    }

}

