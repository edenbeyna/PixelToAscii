package image_char_matching;

import java.util.*;

/**
 * The SubImgCharMatcher class matches image brightness to characters in a character set.
 * It provides methods to add, remove, and retrieve characters based on image brightness.
 */
public class SubImgCharMatcher {
    /** Map to store the brightness value of each character */
    private HashMap<Character, Double> charBrightness;

    /** Map to store the normalized brightness value of each character */
    private HashMap<Character, Double> Norm;

    private char[] charSet;

    /** Maximum brightness value among all characters */
    double maxBrightness;

    /** Minimum brightness value among all characters */
    double minBrightness;

    /**
     * Constructor for SubImgCharMatcher.
     *
     * @param charset The character set to be used for matching image brightness.
     */
    public SubImgCharMatcher(char[] charset) {
        this.charBrightness = new HashMap<Character, Double>();
        this.Norm = new HashMap<Character, Double>();
        this.charSet = charset;
        this.maxBrightness = Double.MIN_VALUE;
        this.minBrightness = Double.MAX_VALUE;
        for (char c : charset) {
            double brightness = getCharBrightness(c);
            updateMinMaxBrightness(brightness);
            charBrightness.put(c, brightness);
        }
        NormaliseBrightness();

    }


    /**
     * Retrieves the character that best matches the given image brightness.
     *
     * @param brightness The brightness value of the image.
     * @return The character that best matches the image brightness.
     */

    public char getCharByImageBrightness(double brightness){
        double closestDiff = Double.MAX_VALUE;
        char closestChar = Character.MIN_VALUE;
        for (char key : Norm.keySet()) {
            double diff = Math.abs(Norm.get(key) - brightness);
            if (diff < closestDiff || (diff == closestDiff && key < closestChar)) {
                closestDiff = diff;
                closestChar = key;
            }
        }
        return closestChar;
    }

    /**
     * Adds a new character to the character set and computes its brightness.
     *
     * @param c The character to be added.
     */

    public void addChar(char c) {
        if (!this.charBrightness.containsKey(c)) {
            char[] result = new char[this.charSet.length + 1];
            int i = 0;
            for (char ch : this.charSet) {
                if (ch > c) {
                    break;
                }
                result[i++] = ch;
            }
            result[i] = c;
            for (int j = i + 1; j < result.length; j++) {
                result[j] = this.charSet[j - 1];
            }
            this.charSet = result;
            double brightness = getCharBrightness(c);
            this.charBrightness.put(c, getCharBrightness(c));
            updateMinMaxBrightness(brightness);
            NormaliseBrightness();
        }
    }

    /**
     * Removes a character from the character set.
     *
     * @param c The character to be removed.
     */
    public void removeChar(char c){
        if (this.charBrightness.containsKey(c)) {
            char[] result = new char[this.charSet.length - 1];
            int i = 0;
            for (char ch : this.charSet) {
                if (ch == c) {
                    continue;
                }
                result[i++] = ch;
            }
            this.charSet = result;
            double removedBrightness = this.charBrightness.remove(c);
            if (this.charBrightness.isEmpty()) {
                this.maxBrightness = Double.MIN_VALUE;
                this.minBrightness = Double.MAX_VALUE;
            } else if (removedBrightness == this.maxBrightness || removedBrightness == this.minBrightness) {
                updateMinMaxBrightnessAfterRemoval();
            }
            NormaliseBrightness();
        }
    }

    /**
     * Updates the minimum and maximum brightness values.
     */

    private void updateMinMaxBrightness(double brightness) {
        this.maxBrightness = Math.max(this.maxBrightness, brightness);
        this.minBrightness = Math.min(this.minBrightness, brightness);
    }

    /**
     * Updates the minimum and maximum brightness values after removing a character from the character set.
     */

    private void updateMinMaxBrightnessAfterRemoval() {
        this.maxBrightness = Double.MIN_VALUE;
        this.minBrightness = Double.MAX_VALUE;
        for (double b : this.charBrightness.values()) {
            this.maxBrightness = Math.max(this.maxBrightness, b);
            this.minBrightness = Math.min(this.minBrightness, b);
        }
    }
    /**
     * Computes the brightness value of a character.
     *
     * @param c The character for which brightness is to be computed.
     * @return The brightness value of the character.
     */
    private double getCharBrightness(char c){
        if (this.charBrightness.containsKey(c)) {
            return this.charBrightness.get(c);
        }
        boolean[][] boolArray = CharConverter.convertToBoolArray(c);
        int whitePixels = 0;
        for (boolean[] pixels : boolArray) {
            for (boolean pixel : pixels) {
                if (pixel) {
                    whitePixels++;
                }
            }
        }
        return (double) whitePixels / (boolArray.length * boolArray[0].length);
    }

    /**
     * Normalizes the brightness values of all characters in the character set.
     */
    private void NormaliseBrightness() {
        this.Norm.clear();
        for (char c : this.charBrightness.keySet()) {
            double brightness = this.charBrightness.get(c);
            double normalizedBrightness = (brightness - this.minBrightness) /
                    (this.maxBrightness - this.minBrightness);
            this.Norm.put(c, normalizedBrightness);
        }
    }

    /**
     * Retrieves the character set.
     *
     * @return The character set.
     */
    public char[] getCharSet(){
        return this.charSet;
    }

}

