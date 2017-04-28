package com.alexstyl.gsc;

public class Sound {

    private char[] soundSymbols;

    public Sound(char c) {
        char[] carr = new char[]{c};
        this.soundSymbols = carr;
    }

    public Sound(char[] soundSymbols) {
        this.soundSymbols = soundSymbols;
    }

    public static Sound createSound(char c) {
        char[] carr = new char[]{c};
        return new Sound(carr);
    }

    public static Sound createSound(char first, char second) {
        return createSound(first + "," + second);
    }

    /**
     * Converts implicitly a CSV string into a sound containing an array of all values.
     * @param chars
     * @return
     */
    public static Sound createSound(String chars) {
        String[] sounds = chars.split(",");

        char[] soundSymbols = new char[sounds.length];
        int i = 0;

        for (String sound : sounds) {
            soundSymbols[i] = sound.charAt(0);
            i++;
        }

        return new Sound(soundSymbols);
    }

    public static Sound append(Sound sound1, Sound sound2) {
        int length = sound1.soundSymbols.length + sound2.soundSymbols.length;
        char[] soundSymbols = new char[length];

        System.arraycopy(sound1.soundSymbols, 0, soundSymbols, 0, sound1.soundSymbols.length);
        System.arraycopy(sound2.soundSymbols, 0, soundSymbols, sound1.soundSymbols.length, sound2.soundSymbols.length);

        return new Sound(soundSymbols);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof Sound)) {
            return false;
        } else {
            Sound other = (Sound) obj;
            char[] var3 = this.soundSymbols;
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                char s1 = var3[var5];
                char[] var7 = other.soundSymbols;
                int var8 = var7.length;

                for (int var9 = 0; var9 < var8; ++var9) {
                    char s2 = var7[var9];
                    if (s1 == s2) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public int hashCode() {
        return this.soundSymbols.hashCode();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        char[] var2 = this.soundSymbols;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            char c = var2[var4];
            str.append(c);
        }

        return str.toString();
    }
}
