package com.alexstyl.gsc


/**
 * This class holds all different sounds a single symbol can be associated with.</br>
 * <p>H can be associated with the sound X and the sound I</p>
 */
class Sound {

    // All different sounds symbols associated with this Sound.
    private val soundSymbols: CharArray

    constructor(char: Char) {
        this.soundSymbols = charArrayOf(char)
    }

    constructor(soundSymbols: CharArray) {
        this.soundSymbols = soundSymbols
    }


    constructor(chars: String) {
        val sounds = chars.split(',')
        this.soundSymbols = CharArray(sounds.size, { sounds[it].single() })
    }


    override fun hashCode(): Int = this.soundSymbols.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sound

        soundSymbols.forEach { thisSymbol ->
            other.soundSymbols.forEach { otherSymbol ->
                if (thisSymbol == otherSymbol) {
                    return true
                }

            }
        }
        return false
    }

    override fun toString(): String {
        val str = StringBuilder()
        soundSymbols.forEach {
            if (str.isNotBlank()) {
                str.append(", ")
            }
            str.append(it)
        }
        return str.toString()
    }

    companion object {
        fun combine(left: Sound, right: Sound): Sound {
            val length = left.soundSymbols.size + right.soundSymbols.size
            val soundSymbols = CharArray(length)

            System.arraycopy(left.soundSymbols, 0, soundSymbols, 0, left.soundSymbols.size)
            System.arraycopy(right.soundSymbols, 0, soundSymbols, left.soundSymbols.size, right.soundSymbols.size)

            return Sound(soundSymbols)
        }

        /**
         * Combines a series of different sounds into one.
         */
        fun append(sounds: Iterable<Sound>): Sound {
            val soundList = sounds.toList()
            var length = 0
            soundList.forEach {
                length += it.soundSymbols.size
            }
            val soundSymbols = CharArray(length)
            var offset = 0
            soundList.forEach {
                System.arraycopy(it.soundSymbols, 0, soundSymbols, offset, it.soundSymbols.size)
                offset += it.soundSymbols.size
            }
            return Sound(soundSymbols)
        }

    }
}
