package profile.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import profile.model.Profile;
import profile.repository.IUserRepository;

/**
 * Concrete implementation of {@link IProfileService}.
 */
public class ProfileServiceImplementor implements IProfileService {

    private final IUserRepository userRepo;

    /** Holds the currently signed-in user (null when no session active). */
    private final AtomicReference<Profile> currentUser = new AtomicReference<>();

    public ProfileServiceImplementor(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Optional<Profile> getById(Integer id) {
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
    public Optional<Profile> openSession(Integer id) {
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
