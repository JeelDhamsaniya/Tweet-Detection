package com.example.toxictweet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Detects similar tweets using string hashing (Jaccard similarity of character sets).
 */
public class HashSimilarity {

    private final double similarityThreshold;

    public HashSimilarity(double thresholdPercent) {
        this.similarityThreshold = thresholdPercent / 100.0;
    }

    /**
     * Calculates the Jaccard similarity between two strings.
     * Jaccard Index = |Intersection| / |Union|
     */
    private double calculateSimilarity(String s1, String s2) {
        Set<Character> set1 = new HashSet<>();
        for (char c : s1.toCharArray()) {
            set1.add(c);
        }

        Set<Character> set2 = new HashSet<>();
        for (char c : s2.toCharArray()) {
            set2.add(c);
        }

        Set<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<Character> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) {
            return 1.0; // Two empty strings are perfectly similar
        }

        return (double) intersection.size() / union.size();
    }

    /**
     * Finds groups of similar tweets from a list.
     * @param tweets The list of all tweets to compare.
     * @return A list of groups (lists) of similar tweets.
     */
    public List<List<Tweet>> findSimilarGroups(List<Tweet> tweets) {
        List<List<Tweet>> similarGroups = new ArrayList<>();
        boolean[] visited = new boolean[tweets.size()];

        for (int i = 0; i < tweets.size(); i++) {
            if (!visited[i]) {
                List<Tweet> currentGroup = new ArrayList<>();
                currentGroup.add(tweets.get(i));
                visited[i] = true;

                for (int j = i + 1; j < tweets.size(); j++) {
                    if (!visited[j]) {
                        double similarity = calculateSimilarity(
                                tweets.get(i).getOriginalText().toLowerCase(),
                                tweets.get(j).getOriginalText().toLowerCase()
                        );
                        if (similarity >= similarityThreshold) {
                            currentGroup.add(tweets.get(j));
                            visited[j] = true;
                        }
                    }
                }
                if (currentGroup.size() > 1) {
                    similarGroups.add(currentGroup);
                }
            }
        }
        return similarGroups;
    }
}
