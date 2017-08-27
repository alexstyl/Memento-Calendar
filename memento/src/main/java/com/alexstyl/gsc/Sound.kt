package com.alexstyl.gsc


/**
 * This class holds all different sounds a single symbol can be associated with.</br>
 * <p>H can be associated with the sound X and the sound I</p>
 */
data class Sound(private val soundSymbols: List<Char>) {
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


    operator fun plus(other: Sound): Sound = Sound(this.soundSymbols + other.soundSymbols)

    companion object {
        /**
         * Combines a series of different sounds into one.
         */
        fun flatten(sounds: Iterable<Sound>): Sound {
            val fold = sounds.fold<Sound, List<Char>>(emptyList(), { list, name ->
                list + name.soundSymbols
            })
            return Sound(fold)
        }

    }
}

fun sound(char: Char) = Sound(listOf(char))
fun sound(chars: String): Sound {
    val sounds = chars.split(',')
    return sound(CharArray(sounds.size, { sounds[it].single() }))
}

fun sound(soundSymbols: CharArray) = Sound(soundSymbols.toList())
