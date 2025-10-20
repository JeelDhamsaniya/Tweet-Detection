package com.example.toxictweet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements the Rabin-Karp algorithm for finding multiple patterns (toxic words) in a text.
 */
public class RabinKarp {
    private final List<String> patterns;
    private final int prime = 101; // A prime number for hashing

    public RabinKarp(List<String> patterns) {
        this.patterns = patterns;
    }

    /**
     * Searches for all occurrences of the patterns in the given text.
     * @param text The text to search within.
     * @return A set of toxic words found in the text.
     */
    public Set<String> search(String text) {
        Set<String> foundPatterns = new HashSet<>();
        int n = text.length();

        for (String pattern : patterns) {
            int m = pattern.length();
            if (m > n) continue;

            long patternHash = calculateHash(pattern, m);
            long textHash = calculateHash(text, m);

            for (int i = 0; i <= n - m; i++) {
                if (patternHash == textHash) {
                    // Check for collision
                    if (text.substring(i, i + m).equals(pattern)) {
                        foundPatterns.add(pattern);
                    }
                }

                // Recalculate hash for the next window of text
                if (i < n - m) {
                    textHash = recalculateHash(textHash, text.charAt(i), text.charAt(i + m), m);
                }
            }
        }
        return foundPatterns;
    }

    /**
     * Calculates the initial hash for a string of a given length.
     */
    private long calculateHash(String str, int len) {
        long hash = 0;
        for (int i = 0; i < len; i++) {
            hash += str.charAt(i) * Math.pow(prime, i);
        }
        return hash;
    }

    /**
     * Recalculates the hash for the next sliding window efficiently.
     */
    private long recalculateHash(long oldHash, char oldChar, char newChar, int patternLen) {
        long newHash = (oldHash - oldChar) / prime;
        newHash += newChar * Math.pow(prime, patternLen - 1);
        return newHash;
    }
}
