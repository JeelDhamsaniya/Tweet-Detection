package com.example.toxictweet;

import java.util.*;

/**
 * Builds and analyzes the user propagation graph to find toxic clusters.
 */
public class GraphAnalyzer {
    private final Map<String, User> users; // Adjacency list representation

    public GraphAnalyzer(Map<String, User> users) {
        this.users = users;
    }

    /**
     * Finds clusters of users involved in spreading toxic tweets using DFS.
     * A cluster is a connected component in the user graph.
     * @param toxicTweets A list of tweets identified as toxic.
     * @return A list of sets, where each set contains the user IDs of a cluster.
     */
    public List<Set<String>> findToxicClusters(List<Tweet> toxicTweets) {
        List<Set<String>> clusters = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        // Get the set of users who posted toxic tweets
        Set<String> toxicUsers = new HashSet<>();
        for (Tweet tweet : toxicTweets) {
            toxicUsers.add(tweet.getUserId());
        }

        for (String userId : toxicUsers) {
            if (!visited.contains(userId)) {
                Set<String> currentCluster = new HashSet<>();
                dfs(userId, visited, currentCluster);
                if (!currentCluster.isEmpty()) {
                    clusters.add(currentCluster);
                }
            }
        }

        return clusters;
    }

    /**
     * Depth-First Search to find all users in a connected component.
     */
    private void dfs(String userId, Set<String> visited, Set<String> currentCluster) {
        visited.add(userId);
        currentCluster.add(userId);

        User user = users.get(userId);
        if (user != null && user.getConnections() != null) {
            for (String neighborId : user.getConnections()) {
                if (!visited.contains(neighborId) && users.containsKey(neighborId)) {
                    dfs(neighborId, visited, currentCluster);
                }
            }
        }
    }

    /**
     * A heuristic to find a potential source user within a cluster.
     * It looks for a user who has no incoming connections from others within the same cluster.
     * @param cluster The set of user IDs in the cluster.
     * @param allUsers The map of all users and their connections.
     * @return The ID of a potential source user, or the first user if none is found.
     */
    public static String findSourceUserInCluster(Set<String> cluster, Map<String, User> allUsers) {
        for (String potentialSource : cluster) {
            boolean isPointedToByClusterMember = false;
            for (String otherUser : cluster) {
                if (!potentialSource.equals(otherUser) && allUsers.get(otherUser).getConnections().contains(potentialSource)) {
                    isPointedToByClusterMember = true;
                    break;
                }
            }
            if (!isPointedToByClusterMember) {
                return potentialSource; // Found a user not retweeted by anyone else in the cluster
            }
        }
        return cluster.iterator().next(); // Default fallback
    }
}
