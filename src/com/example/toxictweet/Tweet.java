package com.example.toxictweet;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single tweet with its properties.
 */
public class Tweet {
    private final String tweetId;
    private final String userId;
    private final String originalText;
    private boolean isToxic;
    private Set<String> toxicWords;

    public Tweet(String tweetId, String userId, String text) {
        this.tweetId = tweetId;
        this.userId = userId;
        this.originalText = text;
        this.isToxic = false;
        this.toxicWords = new HashSet<>();
    }

    // Getters
    public String getTweetId() { return tweetId; }
    public String getUserId() { return userId; }
    public String getOriginalText() { return originalText; }
    public boolean isToxic() { return isToxic; }
    public Set<String> getToxicWords() { return toxicWords; }

    // Setters
    public void setToxic(boolean toxic) { isToxic = toxic; }
    public void setToxicWords(Set<String> toxicWords) { this.toxicWords = toxicWords; }

    @Override
    public String toString() {
        return "Tweet{" +
                "tweetId='" + tweetId + '\'' +
                ", userId='" + userId + '\'' +
                ", text='" + originalText + '\'' +
                ", isToxic=" + isToxic +
                '}';
    }
}
