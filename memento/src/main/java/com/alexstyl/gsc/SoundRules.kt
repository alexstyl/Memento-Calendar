package com.alexstyl.gsc

import kotlin.coroutines.experimental.buildIterator


/**
 * Holds all rules based on which we make the comparisons.
 */
class SoundRules private constructor() {
    /**
     * A dictionary to hold all double character sounds like 'TS' and 'PS'.
     */
    private val doubleSounds = HashMap<String, Sound>()
    /**
     * A dictionary to hold all single symbol sounds like 'A' and 'S'
     */
    private val singleSounds = HashMap<Char, Sound>()
    /**
     * A dictionary to provide an easy way to change Greek accented vowels to
     * their non accented form.
     */
    private val accentCapitals = HashMap<Char, Char>()

    /**
     * A string which contains all chars appearing as the first character of a
     * double character sound.
     * ex. AI, OI , AY, EI => "AOE"
     */
    private var startOfDouble = ""

    // Define all possible sound symbols which can be found in the Greek language.

    private val ALPHA = 'Α'
    private val SIGMA = 'Σ'
    private val DELTA = 'Δ'
    private val DI = 'D'
    private val FI = 'Φ'
    private val GAMMA = 'Γ'
    private val GKOU = 'G'
    private val HI = 'Χ'
    private val JI = 'J'
    private val KAPPA = 'Κ'
    private val LAMBDA = 'Λ'
    private val ZITA = 'Ζ'
    private val XI = 'Ξ'
    private val PSI = 'Ψ'
    private val BETA = 'Β'
    private val NI = 'Ν'
    private val MI = 'Μ'
    private val PI = 'Π'
    private val OMIKRON = 'Ο'
    private val IOTA = 'Ι'
    private val THITA = 'Θ'
    private val RO = 'Ρ'
    private val EPSILON = 'Ε'
    private val TAU = 'Τ'
    private val BI = '~'
    private val OY = '!'
    private val EPSILON_YPSILON = '$'
    private val ALPHA_YPSILONOY = '#'

    init {
        this.initialize()
    }

    private fun initialize() {

        /// chars and CSV are implicitly converted to Sounds
        /// Associate all letters to their sound

        /// English double sounds
        doubleSounds.put("TH", sound(THITA))
        doubleSounds.put("PS", sound(PSI))
        doubleSounds.put("PH", sound(FI))
        doubleSounds.put("KS", sound(XI))
        doubleSounds.put("CH", sound(HI))
        doubleSounds.put("GG", sound(GKOU))
        doubleSounds.put("OY", sound(OY))
        doubleSounds.put("OU", sound(OY))
        doubleSounds.put("TZ", sound(JI))
        doubleSounds.put("MP", sound(BI))
        doubleSounds.put("AI", sound(EPSILON))
        doubleSounds.put("EY", sound(EPSILON_YPSILON))
        doubleSounds.put("EF", sound(EPSILON_YPSILON))
        doubleSounds.put("AF", sound(ALPHA_YPSILONOY))
        doubleSounds.put("AY", sound(ALPHA_YPSILONOY))
        doubleSounds.put("AU", sound(ALPHA_YPSILONOY))
        doubleSounds.put("EI", sound(IOTA))
        doubleSounds.put("OI", sound(IOTA))

        /// Greek double sounds
        doubleSounds.put("ΓΓ", sound(GKOU))
        doubleSounds.put("ΑΙ", sound(EPSILON))
        doubleSounds.put("ΓΚ", sound(GKOU))
        doubleSounds.put("ΤΖ", sound(JI))
        doubleSounds.put("ΟΥ", sound(OY))
        doubleSounds.put("ΜΠ", sound(BI))
        doubleSounds.put("ΕΥ", sound(EPSILON_YPSILON))
        doubleSounds.put("ΕΦ", sound(EPSILON_YPSILON))
        doubleSounds.put("ΑΦ", sound(ALPHA_YPSILONOY))
        doubleSounds.put("ΑΥ", sound(ALPHA_YPSILONOY))
        doubleSounds.put("ΟΙ", sound(IOTA))
        doubleSounds.put("ΕΙ", sound(IOTA))
        doubleSounds.put("ΚΣ", sound(XI))
        doubleSounds.put("ΠΣ", sound(PSI))

        /// English Sounds
        this.singleSounds.put('Q', sound(KAPPA))
        this.singleSounds.put('8', sound(THITA))
        this.singleSounds.put('9', sound(THITA))
        this.singleSounds.put('3', sound(EPSILON))
        this.singleSounds.put('4', sound(ALPHA))
        this.singleSounds.put('0', sound(OMIKRON))
        this.singleSounds.put('W', sound(OMIKRON))
        this.singleSounds.put('O', sound(OMIKRON))
        this.singleSounds.put('E', sound(EPSILON + "," + IOTA))
        this.singleSounds.put('R', sound(RO))
        this.singleSounds.put('T', sound(TAU))
        this.singleSounds.put('Y', sound(IOTA + "," + GAMMA))
        this.singleSounds.put('U', sound(IOTA + "," + OY))
        this.singleSounds.put('I', sound(IOTA))
        this.singleSounds.put('P', sound(PI))
        this.singleSounds.put('A', sound(ALPHA))
        this.singleSounds.put('S', sound(SIGMA))
        this.singleSounds.put('D', sound(DELTA + "," + DI))
        this.singleSounds.put('F', sound(FI))
        this.singleSounds.put('G', sound(GKOU + "," + GAMMA))
        this.singleSounds.put('H', sound(IOTA + "," + HI))
        this.singleSounds.put('J', sound(JI))
        this.singleSounds.put('K', sound(KAPPA))
        this.singleSounds.put('L', sound(LAMBDA))
        this.singleSounds.put('Z', sound(ZITA))
        this.singleSounds.put('X', sound(HI + "," + XI))
        this.singleSounds.put('C', sound(KAPPA + "," + SIGMA))
        this.singleSounds.put('V', sound(BETA))
        this.singleSounds.put('B', sound(BETA + "," + BI))
        this.singleSounds.put('N', sound(NI))
        this.singleSounds.put('M', sound(MI))

        /// Greek letters
        this.singleSounds.put('Γ', sound(GAMMA))
        this.singleSounds.put('Ω', sound(OMIKRON))
        this.singleSounds.put('Η', sound(IOTA))
        this.singleSounds.put('Ι', sound(IOTA))
        this.singleSounds.put('Υ', sound(IOTA))
        this.singleSounds.put('Η', sound(IOTA))
        this.singleSounds.put('ς', sound(SIGMA))
        this.singleSounds.put('Ξ', sound(XI))

        this.accentCapitals.put('Ά', ALPHA)
        this.accentCapitals.put('Ί', IOTA)
        this.accentCapitals.put('Ό', OMIKRON)
        this.accentCapitals.put('Ύ', IOTA)
        this.accentCapitals.put('Ή', IOTA)
        this.accentCapitals.put('Έ', EPSILON)
        this.accentCapitals.put('Ώ', OMIKRON)
        this.accentCapitals.put('Ί', IOTA)
        this.accentCapitals.put('Ϊ', IOTA)
        this.accentCapitals.put('\u0390', IOTA) // small iota with dyalitika and tonos does not have a upper case counter part

        // TODO
        /// Find all characters which may start a double symbol sound
        val str = StringBuilder()
        val var2 = this.doubleSounds.keys.iterator()

        while (var2.hasNext()) {
            val charA = var2.next()[0].toString()
            if (!str.toString().contains(charA)) {
                str.append(charA)
            }
        }

        this.startOfDouble = str.toString()
    }

    /**
     * Compare the two given string and determines if they sound the same.
     */
    fun compare(first: String, second: String, startsWith: Boolean): Boolean {
        val enumerator2 = getNextSound(second, startsWith).iterator()

        getNextSound(first, startsWith).forEach { sound1 ->
            if (enumerator2.hasNext()) {
                sound1 as Sound
                val sound2 = enumerator2.next() as Sound

                if (!sound1.soundsLike(sound2)) {
                    /// Found a non matching sound thus
                    /// we are sure the two words don't sound the same
                    return false
                }
            } else {
                /// The first word sounds exactly as second up until now.
                /// Return true if we check that first starts with second
                /// Return false if first and second must sound the same.
                return startsWith
            }
        }

        /// Special case where first is a substring of second
        /// We need to make sure that both string have reached their end.
        if (enumerator2.hasNext()) {
            return false
        }
        return true

    }

    /**
     * The core function of the library.
     * Returns the next sound of the string.
     *
     */
    fun getNextSound(string: String, retrieveAll: Boolean) = buildIterator {
        /// Used to indicate the beginning of a double char sound
        var doubleSoundPending = false

        /// Store the first char of a double symbol sound
        var previousCharacter = 0.toChar()


        val stringUpper = string.toUpperCase()
        stringUpper.forEach { c ->
            var character = c

            /// Remove the acute accent from the character if present
            if (accentCapitals.containsKey(character)) {
                character = accentCapitals[character]!!
            }
            /// We have a pending double sound.
            /// Check if the previousCharacter followed up by character is a
            /// doubleSymbol Sound
            if (doubleSoundPending) {
                doubleSoundPending = false

                /// Create the key and check if it exists as a double symbol sounds
                val key = (previousCharacter + "" + character)

                if (doubleSounds.containsKey(key)) {
                    yield(doubleSounds[key])
                    return@forEach
                } else {
                    yield(getSound(previousCharacter))
                }
            }
            if (startOfDouble.indexOf(character) >= 0) {
                doubleSoundPending = true
                previousCharacter = character
            } else {
                yield(getSound(character))
            }
        }

        /// Return any pending sounds
        if (doubleSoundPending) {
            if (retrieveAll) {
                yield(getAllSoundsInternal(previousCharacter))
            } else {
                yield(getSound(previousCharacter))
            }
        }
    }

    /**
     * Get the sound of a single character
     *
     * @param character
     * @return
     */
    private fun getSound(character: Char): Sound {
        return if (singleSounds.containsKey(character)) {
            singleSounds[character]!!
        } else sound(character)
    }


    /**
     * Returns all sounds which are possible starting with the given character.
     *
     * p => P, PS, PH
     *
     */
    private fun getAllSoundsInternal(upperCharacter: Char): Sound {
        var sound = getSound(upperCharacter)

        if (startOfDouble.indexOf(upperCharacter) >= 0) {

            val map = doubleSounds.filter {
                it.key[0] == upperCharacter
            }.map {
                it.value
            }
            sound += Sound.flatten(map);
        }
        return sound
    }

    companion object {
        private var sInstance: SoundRules? = null

        val INSTANCE: SoundRules
            get() {
                if (sInstance == null) {
                    sInstance = SoundRules()
                }

                return sInstance!!
            }
    }
}

