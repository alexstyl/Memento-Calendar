package com.alexstyl.gsc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Holds all rules based on which we make the comparisons.
 */
public class SoundRules {
    private static SoundRules sInstance;
    /**
     * A dictionary to hold all double character sounds like 'TS' and 'PS'.
     */
    private HashMap<String, Sound> doubleSounds = new HashMap<String, Sound>();
    /**
     * A dictionary to hold all single symbol sounds like 'A' and 'S'
     */
    private HashMap<Character, Sound> singleSounds = new HashMap<Character, Sound>();
    /**
     * A dictionary to provide an easy way to change Greek accented vowels to
     * their non accented form.
     */
    private HashMap<Character, Character> accentCapitals = new HashMap<Character, Character>();

    /**
     * A string which contains all chars appearing as the first character of a
     * double character sound.
     * ex. AI, OI , AY, EI => "AOE"
     */
    private String startOfDouble;

    // Define all possible sound symbols which can be found in the Greek language.

    private final char ALPHA = 'Α';
    private final char SIGMA = 'Σ';
    private final char DELTA = 'Δ';
    private final char DI = 'D';
    private final char FI = 'Φ';
    private final char GAMMA = 'Γ';
    private final char GKOU = 'G';
    private final char HI = 'Χ';
    private final char JI = 'J';
    private final char KAPPA = 'Κ';
    private final char LAMBDA = 'Λ';
    private final char ZITA = 'Ζ';
    private final char XI = 'Ξ';
    private final char PSI = 'Ψ';
    private final char BETA = 'Β';
    private final char NI = 'Ν';
    private final char MI = 'Μ';
    private final char PI = 'Π';
    private final char OMIKRON = 'Ο';
    private final char IOTA = 'Ι';
    private final char THITA = 'Θ';
    private final char RO = 'Ρ';
    private final char EPSILON = 'Ε';
    private final char TAU = 'Τ';
    private final char BI = '~';
    private final char OY = '!';
    private final char EPSILON_YPSILON = '$';
    private final char ALPHA_YPSILONOY = '#';

    public static SoundRules getInstance() {
        if (sInstance == null) {
            sInstance = new SoundRules();
        }

        return sInstance;
    }

    private SoundRules() {
        this.initialize();
    }

    private void initialize() {

        /// chars and CSV are implicitly converted to Sounds
        /// Associate all letters to their sound

        /// English double sounds
        doubleSounds.put("TH", Sound.createSound(THITA));
        doubleSounds.put("PS", Sound.createSound(PSI));
        doubleSounds.put("PH", Sound.createSound(FI));
        doubleSounds.put("KS", Sound.createSound(XI));
        doubleSounds.put("CH", Sound.createSound(HI));
        doubleSounds.put("GG", Sound.createSound(GKOU));
        doubleSounds.put("OY", Sound.createSound(OY));
        doubleSounds.put("OU", Sound.createSound(OY));
        doubleSounds.put("TZ", Sound.createSound(JI));
        doubleSounds.put("MP", Sound.createSound(BI));
        doubleSounds.put("AI", Sound.createSound(EPSILON));
        doubleSounds.put("EY", Sound.createSound(EPSILON_YPSILON));
        doubleSounds.put("EF", Sound.createSound(EPSILON_YPSILON));
        doubleSounds.put("AF", Sound.createSound(ALPHA_YPSILONOY));
        doubleSounds.put("AY", Sound.createSound(ALPHA_YPSILONOY));
        doubleSounds.put("AU", Sound.createSound(ALPHA_YPSILONOY));
        doubleSounds.put("EI", Sound.createSound(IOTA));
        doubleSounds.put("OI", Sound.createSound(IOTA));

        /// Greek double sounds
        doubleSounds.put("ΓΓ", Sound.createSound(GKOU));
        doubleSounds.put("ΑΙ", Sound.createSound(EPSILON));
        doubleSounds.put("ΓΚ", Sound.createSound(GKOU));
        doubleSounds.put("ΤΖ", Sound.createSound(JI));
        doubleSounds.put("ΟΥ", Sound.createSound(OY));
        doubleSounds.put("ΜΠ", Sound.createSound(BI));
        doubleSounds.put("ΕΥ", Sound.createSound(EPSILON_YPSILON));
        doubleSounds.put("ΕΦ", Sound.createSound(EPSILON_YPSILON));
        doubleSounds.put("ΑΦ", Sound.createSound(ALPHA_YPSILONOY));
        doubleSounds.put("ΑΥ", Sound.createSound(ALPHA_YPSILONOY));
        doubleSounds.put("ΟΙ", Sound.createSound(IOTA));
        doubleSounds.put("ΕΙ", Sound.createSound(IOTA));
        doubleSounds.put("ΚΣ", Sound.createSound(XI));
        doubleSounds.put("ΠΣ", Sound.createSound(PSI));

        /// English Sounds
        this.singleSounds.put('Q', Sound.createSound(KAPPA));
        this.singleSounds.put('8', Sound.createSound(THITA));
        this.singleSounds.put('9', Sound.createSound(THITA));
        this.singleSounds.put('3', Sound.createSound(EPSILON));
        this.singleSounds.put('4', Sound.createSound(ALPHA));
        this.singleSounds.put('0', Sound.createSound(OMIKRON));
        this.singleSounds.put('W', Sound.createSound(OMIKRON));
        this.singleSounds.put('O', Sound.createSound(OMIKRON));
        this.singleSounds.put('E', Sound.createSound(EPSILON, IOTA));
        this.singleSounds.put('R', Sound.createSound(RO));
        this.singleSounds.put('T', Sound.createSound(TAU));
        this.singleSounds.put('Y', Sound.createSound(IOTA, GAMMA));
        this.singleSounds.put('U', Sound.createSound(IOTA, OY));
        this.singleSounds.put('I', Sound.createSound(IOTA));
        this.singleSounds.put('P', Sound.createSound(PI));
        this.singleSounds.put('A', Sound.createSound(ALPHA));
        this.singleSounds.put('S', Sound.createSound(SIGMA));
        this.singleSounds.put('D', Sound.createSound(DELTA, DI));
        this.singleSounds.put('F', Sound.createSound(FI));
        this.singleSounds.put('G', Sound.createSound(GKOU, GAMMA));
        this.singleSounds.put('H', Sound.createSound(IOTA, HI));
        this.singleSounds.put('J', Sound.createSound(JI));
        this.singleSounds.put('K', Sound.createSound(KAPPA));
        this.singleSounds.put('L', Sound.createSound(LAMBDA));
        this.singleSounds.put('Z', Sound.createSound(ZITA));
        this.singleSounds.put('X', Sound.createSound(HI, XI));
        this.singleSounds.put('C', Sound.createSound(KAPPA, SIGMA));
        this.singleSounds.put('V', Sound.createSound(BETA));
        this.singleSounds.put('B', Sound.createSound(BETA, BI));
        this.singleSounds.put('N', Sound.createSound(NI));
        this.singleSounds.put('M', Sound.createSound(MI));

        /// Greek letters
        this.singleSounds.put('Γ', Sound.createSound(GAMMA));
        this.singleSounds.put('Ω', Sound.createSound(OMIKRON));
        this.singleSounds.put('Η', Sound.createSound(IOTA));
        this.singleSounds.put('Ι', Sound.createSound(IOTA));
        this.singleSounds.put('Υ', Sound.createSound(IOTA));
        this.singleSounds.put('Η', Sound.createSound(IOTA));
        this.singleSounds.put('ς', Sound.createSound(SIGMA));
        this.singleSounds.put('Ξ', Sound.createSound(XI));

        this.accentCapitals.put('Ά', ALPHA);
        this.accentCapitals.put('Ί', IOTA);
        this.accentCapitals.put('Ό', OMIKRON);
        this.accentCapitals.put('Ύ', IOTA);
        this.accentCapitals.put('Ή', IOTA);
        this.accentCapitals.put('Έ', EPSILON);
        this.accentCapitals.put('Ώ', OMIKRON);
        this.accentCapitals.put('Ί', IOTA);
        this.accentCapitals.put('Ϊ', IOTA);
        this.accentCapitals.put('\u0390', IOTA); // small iota with dyalitika and tonos does not have a upper case counter part


        /// Find all characters which may start a double symbol sound
        StringBuilder str = new StringBuilder();
        Iterator var2 = this.doubleSounds.keySet().iterator();

        while (var2.hasNext()) {
            String a = (String) var2.next();
            String charA = String.valueOf(a.charAt(0));
            if (!str.toString().contains(charA)) {
                str.append(charA);
            }
        }

        this.startOfDouble = str.toString();
    }

    /**
     * Compare the two given string and determines if they sound the same.
     *
     * @param first
     * @param second
     * @param startsWith Indicates if a partial match is considered positive or negative result
     * @return
     */
    protected boolean compare2(String first, String second, boolean startsWith) {
        Index index1 = new Index(first.length());
        Index index2 = new Index(second.length());

        Sound sound1;
        Sound sound2;
        do {
            if (index1.hasEnded()) {
                if (!index2.hasEnded()) {
                    return false;
                }

                return true;
            }

            if (index2.hasEnded()) {
                return startsWith;
            }

            sound1 = this.getNextSound(first, index1, startsWith);
            sound2 = this.getNextSound(second, index2, startsWith);
        } while (sound1 == null || sound2 == null || sound1.equals(sound2));

        return false;
    }

    protected boolean compare(String first, String second, boolean startsWith) {
        Index index1 = new Index(first.length());
        Index index2 = new Index(second.length());

        while (!index1.hasEnded()) {
            if (!index2.hasEnded()) {
                Sound sound1 = getNextSound(first, index1, startsWith);
                Sound sound2 = getNextSound(second, index2, startsWith);

                if (!sound1.equals(sound2)) {
                    /// Found a non matching sound thus
                    /// we are sure the two words don't sound the same
                    return false;
                }
            } else {
                /// The first word sounds exactly as second up until now.
                /// Return true if we check that first starts with second
                /// Return false if first and second must sound the same.
                return startsWith;
            }
        }

        /// Special case where first is a substring of second
        /// We need to make sure that both string have reached their end.
        if (!index2.hasEnded()) {
            return false;
        }

        return true;
    }

    protected List<Sound> soundsLike(String first) {
        Index index1 = new Index(first.length());
        ArrayList soundList = new ArrayList();

        while (!index1.hasEnded()) {
            Sound sound1 = this.getNextSound(first, index1, false);
            if (sound1 != null) {
                soundList.add(sound1);
            }
        }

        return soundList;
    }

    /**
     * The core function of the library.
     * Returns the next sound of the string.
     *
     * @param string
     * @param index
     * @param retrieveAll
     * @return
     */
//    public Sound getNextSound2(String string, Index index, boolean retrieveAll) {
//
//        /// Used to indicate the beginning of a double char sound
//        boolean doubleSoundPending = false;
//
//        /// Store the first char of a double symbol sound
//        char previousCharacter = '\0';
//
//        string = string.toUpperCase();
//        int length = string.length();
//
//        while (index.intValue() < length - 1) {
//            index.stepUp();
//            char character = string.charAt(index.intValue());
//
//            /// Remove the acute accent from the character if present
//            if (accentCapitals.containsKey(character)) {
//                character = accentCapitals.get(character);
//            }
//
//            /// We have a pending double sound.
//            /// Check if the previousCharacter followed up by character is a
//            /// doubleSymbol Sound
//            if (doubleSoundPending) {
//                doubleSoundPending = false;
//
//                /// Create the key and check if it exists as a double symbol sounds
////                var key = String.Concat(previousCharacter, character);
//                String key = String.valueOf(previousCharacter) + String.valueOf(character);
//                if (doubleSounds.containsKey(key)) {
//                    return doubleSounds.get(key);
//                } else {
//                    index.stepDown();//go a step back
//                    return getSound(previousCharacter);
//                }
//            }
//
//            /// If this character could potentially lead a double symbol
//            /// sound we need to check the following character too.
//            if (startOfDouble.indexOf(character) >= 0) {
//                doubleSoundPending = true;
//                previousCharacter = character;
//            } else {
//                return getSound(character);
//            }
//        }
//        index.end();
//        //index = ENDED;
//
//        /// Return any pending sounds
//        if (doubleSoundPending) {
//            if (retrieveAll) {
//                return getAllSounds(previousCharacter);
//            } else {
//                return getSound(previousCharacter);
//            }
//        }else {
//            return null;
//        }
//    }

    public Sound getNextSound(String string, Index index, boolean retrieveAll) {
        boolean doubleSoundPending = false;
        char previousCharacter = 0;
        string = string.toUpperCase();

        char character;
        for (int length = string.length(); index.intValue() + 1 < length; previousCharacter = character) {
            index.stepUp();
            character = string.charAt(index.intValue());
            if (this.accentCapitals.containsKey(Character.valueOf(character))) {
                character = this.accentCapitals.get(Character.valueOf(character));
            }

            if (doubleSoundPending) {
                doubleSoundPending = false;
                char[] chars = new char[]{previousCharacter, character};
                String twoChars = new String(chars);
                if (this.doubleSounds.containsKey(twoChars)) {
                    return (Sound) this.doubleSounds.get(twoChars);
                }

                index.stepDown();
                return this.getSound(previousCharacter);
            }

            if (!this.startOfDouble.contains(String.valueOf(character))) {
                if (index.intValue() + 1 == length) {
                    index.end();
                }

                return this.getSound(character);
            }

            doubleSoundPending = true;
        }

        index.end();
        if (doubleSoundPending) {
            if (retrieveAll) {
                return this.getAllSounds(previousCharacter);
            } else {
                return this.getSound(previousCharacter);
            }
        } else {
            return null;
        }
    }

    /**
     * Get the sound of a single character
     *
     * @param character
     * @return
     */
    private Sound getSound(char character) {
        if (singleSounds.containsKey(character)) {
            return singleSounds.get(character);
        }
        return new Sound(character);
    }

    /**
     * Returns all sounds which are possible starting with the given character.
     * <p>p => P, PS, PH</p>
     *
     * @param character
     * @return
     */
    private Sound getAllSounds(char character) {
        Sound sound = this.getSound(character);
        if (startOfDouble.indexOf(character) >= 0) {
            for (Entry<String, Sound> pair : doubleSounds.entrySet()) {
                if (pair.getKey().charAt(0) == character) {
                    sound = Sound.append(sound, pair.getValue());
                }
            }
        }
        return sound;
    }
}

