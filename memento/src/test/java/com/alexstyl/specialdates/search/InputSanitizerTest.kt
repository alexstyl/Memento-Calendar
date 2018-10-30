package com.alexstyl.specialdates.search

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class InputSanitizerTest {

    @Test
    fun weirdInputs() {
        assertThat(" {][][][][____';'   Al  e3    x    ".sanitize()).isEqualTo("Al e3 x")
        assertThat("____Al______".sanitize()).isEqualTo("Al")
        assertThat("Al±`<><><>".sanitize()).isEqualTo("Al")
        assertThat("[[[[[[Alex]]]]]]".sanitize()).isEqualTo("Alex")
        assertThat("[[[[[[A[]l[]e[]x]]]]]]".sanitize()).isEqualTo("A l e x")
    }
}

private fun String.sanitize(): String {
    return InputSanitizer.removeNonAlphaNumericFrom(this)
}
