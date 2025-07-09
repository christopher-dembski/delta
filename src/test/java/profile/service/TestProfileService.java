package profile.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import profile.model.Profile;

/**
 * Simple in-memory implementation of ProfileService for unit testing
 */
public class TestProfileService implements ProfileService {
    private final ConcurrentHashMap<String, Profile> profiles = new ConcurrentHashMap<>();
    private Profile currentSession = null;
    private boolean shouldThrowException = false;
    private String exceptionMessage = "Test exception";

    @Override
    public void add(Profile profile) {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        profiles.put(profile.getId(), profile);
    }

    @Override
    public Optional<Profile> getById(String id) {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        return Optional.ofNullable(profiles.get(id));
    }

    @Override
    public List<Profile> listAll() {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        return new ArrayList<>(profiles.values());
    }

    @Override
    public void update(Profile profile) {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        profiles.put(profile.getId(), profile);
    }

    @Override
    public Optional<Profile> openSession(String profileId) {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        Profile profile = profiles.get(profileId);
        if (profile != null) {
            currentSession = profile;
            return Optional.of(profile);
        }
        return Optional.empty();
    }

    @Override
    public void closeSession() {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        currentSession = null;
    }

    @Override
    public Optional<Profile> getCurrentSession() {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        return Optional.ofNullable(currentSession);
    }

    // Test helper methods
    public void clear() {
        profiles.clear();
        currentSession = null;
    }

    public void setShouldThrowException(boolean shouldThrow) {
        this.shouldThrowException = shouldThrow;
    }

    public void setExceptionMessage(String message) {
        this.exceptionMessage = message;
    }

    public int size() {
        return profiles.size();
    }

    public void addProfile(Profile profile) {
        profiles.put(profile.getId(), profile);
    }
}
