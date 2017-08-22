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

    fun soundsLike(other: Sound): Boolean {
        soundSymbols.forEach { thisSymbol ->
            other.soundSymbols.forEach { otherSymbol ->
                if (thisSymbol == otherSymbol) {
                    return true
                }
            }
        }
        return false
    }


    operator fun plus(other: Sound): Sound {
        val length = this.soundSymbols.size + other.soundSymbols.size
        val soundSymbols = CharArray(length)

        System.arraycopy(this.soundSymbols, 0, soundSymbols, 0, this.soundSymbols.size)
        System.arraycopy(other.soundSymbols, 0, soundSymbols, this.soundSymbols.size, other.soundSymbols.size)

        return Sound(soundSymbols)
    }

    companion object {
        /**
         * Combines a series of different sounds into one.
         */
        fun flatten(sounds: Iterable<Sound>): Sound {
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
