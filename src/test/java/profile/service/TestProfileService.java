package profile.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import profile.model.Profile;

/**
 * Simple in-memory implementation of IProfileService for unit testing
 */
public class TestProfileService implements IProfileService {
    private final ConcurrentHashMap<Integer, Profile> profiles = new ConcurrentHashMap<>();
    private Profile currentSession = null;
    private boolean shouldThrowException = false;
    private String exceptionMessage = "Test exception";
    private int nextId = 1; 

    @Override
    public void add(Profile profile) {
        if (shouldThrowException) {
            throw new RuntimeException(exceptionMessage);
        }
        
        // If profile doesn't have an ID, auto-generate one (simulating database behavior)
        Profile profileToStore = profile;
        if (profile.getId() == null) {
            profileToStore = new Profile.Builder()
                    .id(nextId++)
                    .name(profile.getName())
                    .age(profile.getAge())
                    .sex(profile.getSex())
                    .dateOfBirth(profile.getDateOfBirth())
                    .height(profile.getHeight())
                    .weight(profile.getWeight())
                    .unitSystem(profile.getUnitSystem())
                    .build();
        }
        
        profiles.put(profileToStore.getId(), profileToStore);
    }

    @Override
    public Optional<Profile> getById(Integer id) {
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
    public Optional<Profile> openSession(Integer profileId) {
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
        nextId = 1; // Reset 
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
        // Use the same logic as add() to handle null IDs
        Profile profileToStore = profile;
        if (profile.getId() == null) {
            profileToStore = new Profile.Builder()
                    .id(nextId++)
                    .name(profile.getName())
                    .age(profile.getAge())
                    .sex(profile.getSex())
                    .dateOfBirth(profile.getDateOfBirth())
                    .height(profile.getHeight())
                    .weight(profile.getWeight())
                    .unitSystem(profile.getUnitSystem())
                    .build();
        }
        profiles.put(profileToStore.getId(), profileToStore);
    }

    public boolean getShouldThrowException() {
        return shouldThrowException;
    }
}
