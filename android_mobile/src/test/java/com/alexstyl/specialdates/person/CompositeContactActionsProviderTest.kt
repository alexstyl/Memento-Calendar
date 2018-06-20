package com.alexstyl.specialdates.person

import android.graphics.drawable.Drawable
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.dailyreminder.NoActions
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.runners.MockitoJUnitRunner
import java.net.URI

@RunWith(MockitoJUnitRunner::class)
class CompositeContactActionsProviderTest {

    private var mockProviderA = mock(ContactActionsProvider::class.java)
    private val SOURCE_A = 1
    private var mockProviderB = mock(ContactActionsProvider::class.java)
    private val SOURCE_B = 2

    private lateinit var provider: CompositeContactActionsProvider

    @Before
    fun setup() {
        provider = CompositeContactActionsProvider(
                mapOf(
                        Pair(SOURCE_A, mockProviderA),
                        Pair(SOURCE_B, mockProviderB))
        )
    }

    @Test
    fun givenASpecificContact_thenReturnMessagingActionsForContactOfTheSpecificSource() {
        val aContact = Contact(5, DisplayName.from("Alex"), URI.create(""), SOURCE_A)

        val expectedValues = listOf(
                ContactActionViewModel(
                        ContactAction("aValeu", "aLabel", {}),
                        0, mock(Drawable::class.java)
                )
        )
        given(mockProviderA.messagingActionsFor(aContact, NoActions())).willReturn(expectedValues)
        given(mockProviderB.messagingActionsFor(aContact, NoActions())).willReturn(listOf(
                ContactActionViewModel(
                        ContactAction("bValeu", "bLabel", {}),
                        0, mock(Drawable::class.java)
                )
        ))
        val actualValues = provider.messagingActionsFor(aContact, NoActions())

        assertThat(actualValues).isEqualTo(expectedValues)
    }

    @Test
    fun givenASpecificContact_thenReturnCallingActionsForContactOfTheSpecificSource() {
        val aContact = Contact(5, DisplayName.from("Alex"), URI.create(""), SOURCE_A)

        val expectedValues = listOf(
                ContactActionViewModel(
                        ContactAction("aValeu", "aLabel", {}),
                        0, mock(Drawable::class.java)
                )
        )
        given(mockProviderA.callActionsFor(aContact, NoActions())).willReturn(expectedValues)
        given(mockProviderB.callActionsFor(aContact, NoActions())).willReturn(listOf(
                ContactActionViewModel(
                        ContactAction("bValeu", "bLabel", {}),
                        0, mock(Drawable::class.java)
                )
        ))
        val actualValues = provider.callActionsFor(aContact, NoActions())

        assertThat(actualValues).isEqualTo(expectedValues)
    }
}
