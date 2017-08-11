package com.alexstyl.specialdates.events.namedays.activity

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NamedayPresenterTest {

    private val mockView = Mockito.mock(NamedaysMVPView::class.java)

    @Test
    fun assertThatTheViewGetsPopulatedCorrectly() {
        val presenter = NamedayPresenter()
        presenter.startPresentingInto(mockView)
        assertThat(mockView)

        val expectedViewModels = arrayListOf<NamedaysViewModel>()
        Mockito.verify(mockView).displayNamedays(expectedViewModels)
    }
}
