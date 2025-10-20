package com.example.toxictweet;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user in the propagation graph.
 */
public class User {
    private final String userId;
    // Set of user IDs this user has retweeted FROM.
    // Represents a directed edge: this -> otherUser
    private final Set<String> connections;

    public User(String userId) {
        this.userId = userId;
        this.connections = new HashSet<>();
    }

    public String getUserId() {
        return userId;
    }

    public void addConnection(String toUserId) {
        connections.add(toUserId);
    }

    public Set<String> getConnections() {
        return connections;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", connections=" + connections +
                '}';
    }
}
