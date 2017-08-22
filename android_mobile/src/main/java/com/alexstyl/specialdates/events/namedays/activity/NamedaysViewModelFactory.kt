package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.ui.widget.LetterPainter

class NamedaysViewModelFactory(private val letterPainter: LetterPainter) {

    fun viewModelsFor(name: String): NamedaysViewModel {
        return NamedaysViewModel(name)
    }

    fun viewModelsFor(contact: Contact): CelebratingContactViewModel {
        val colorVariant = letterPainter.getVariant(contact.hashCode())
        return CelebratingContactViewModel(contact, contact.displayName.toString(), colorVariant)
    }

}

