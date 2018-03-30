package com.alexstyl.specialdates.person

import java.net.URI

interface ContactActions {
    fun dial(phoneNumber: String): () -> Unit
    fun view(data: URI, mimetype: String): () -> Unit
    fun view(data: URI): () -> Unit
    fun message(phoneNumber: String): () -> Unit
    fun email(emailAdress: String): () -> Unit
}
