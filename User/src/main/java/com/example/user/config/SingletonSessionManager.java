package com.example.user.config;

import java.util.*;
import java.util.HashSet;
import java.util.Set;


public class SingletonSessionManager {

    private static SingletonSessionManager instance;

    private final Set<Long> activeUserSessions;

    // Private constructor to prevent direct instantiation
    private SingletonSessionManager() {
        activeUserSessions = new HashSet<>();
    }

    // Get the single instance (lazy initialization)
    public static synchronized SingletonSessionManager getInstance() {
        if (instance == null) {
            instance = new SingletonSessionManager();
        }
        return instance;
    }

    // Add a user to active session (Login)
    public void startSession(Long userId) {
        activeUserSessions.add(userId);
    }

    // Remove a user from active session (Logout)
    public void endSession(Long userId) {
        activeUserSessions.remove(userId);
    }

    // Check if user is logged in
    public boolean isLoggedIn(Long userId) {
        return activeUserSessions.contains(userId);
    }

    // Optional: Clear all sessions (e.g., for testing or shutdown)
    public void clearAllSessions() {
        activeUserSessions.clear();
    }
}
