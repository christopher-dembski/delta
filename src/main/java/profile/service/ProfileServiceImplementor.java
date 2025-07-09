package profile.service;

import profile.model.Profile;
import profile.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Concrete implementation of {@link ProfileService}.
 */
public class ProfileServiceImplementor implements ProfileService {

    private final UserRepository userRepo;

    /** Holds the currently signed-in user (null when no session active). */
    private final AtomicReference<Profile> currentUser = new AtomicReference<>();

    public ProfileServiceImplementor(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Optional<Profile> getById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public void add(Profile profile) {
        userRepo.add(profile);
    }

    @Override
    public void update(Profile profile) {
        userRepo.update(profile);
    }

    @Override
    public List<Profile> listAll() {
        return new ArrayList<>(userRepo.findAll());
    }

    @Override
    public Optional<Profile> openSession(String id) {
        Optional<Profile> candidate = userRepo.findById(id);
        candidate.ifPresent(currentUser::set);      // only set if profile exists
        return candidate;
    }

    @Override
    public void closeSession() {
        currentUser.set(null);
    }

    @Override
    public Optional<Profile> getCurrentSession() {
        return Optional.ofNullable(currentUser.get());
    }
}
