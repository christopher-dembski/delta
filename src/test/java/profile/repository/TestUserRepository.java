package profile.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import profile.model.Profile;

/**
 * Simple in-memory implementation of UserRepository for unit testing
 */
public class TestUserRepository implements UserRepository {
    private final ConcurrentHashMap<Integer, Profile> profiles = new ConcurrentHashMap<>();
    private boolean shouldThrowException = false;
    private String exceptionMessage = "Test exception";

    @Override
    public boolean add(Profile profile) {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        profiles.put(profile.getId(), profile);
        return true;
    }

    @Override
    public Optional<Profile> findById(Integer id) {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        return Optional.ofNullable(profiles.get(id));
    }

    @Override
    public List<Profile> findAll() {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        return new ArrayList<>(profiles.values());
    }

    @Override
    public boolean update(Profile profile) {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        profiles.put(profile.getId(), profile);
        return true;
    }

    // Test utility methods
    public void clear() {
        profiles.clear();
    }

    public int size() {
        return profiles.size();
    }

    public void setShouldThrowException(boolean shouldThrow) {
        this.shouldThrowException = shouldThrow;
    }

    public void setExceptionMessage(String message) {
        this.exceptionMessage = message;
    }
}
