package com.alexstyl.specialdates.debug.dailyreminder

class LoremIpsumBuilder() {

    private var times = 1

    fun times(times: Int): LoremIpsumBuilder {
        return LoremIpsumBuilder(times)
    }

    private constructor(times: Int) : this() {
        this.times = times
    }


    fun build(): String {
        val stringBuilder = StringBuilder()
        for (i in 0..times) {
            if (i != 0) {
                stringBuilder.append(" ")
            }
            stringBuilder.append(STANDARD)
        }
        return stringBuilder.toString()
    }

    companion object {
        const val STANDARD = """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."""
    }
}
